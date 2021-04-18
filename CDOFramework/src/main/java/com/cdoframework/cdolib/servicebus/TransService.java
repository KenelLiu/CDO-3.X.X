package com.cdoframework.cdolib.servicebus;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import com.cdoframework.cdolib.annotation.TransName;
import com.cdoframework.cdolib.base.Return;
import com.cdoframework.cdolib.data.cdo.CDO;
import com.cdoframework.cdolib.database.DBPool;
import com.cdoframework.cdolib.database.DBPoolManager;
import com.cdoframework.cdolib.database.IDataEngine;
import com.cdoframework.transaction.TransactionThreadLocal;
/**
 * 增加事务传播属性
 * 为了方便处理和事务使用频次,所有定义了transName名称的方法,事务的传播都为Propagation.REQUIRED
 * XML里SQLTrans的Propagation属性支持可以定义为
 * REQUIRED,SUPPORTS,MANDATORY
 * REQUIRES_NEW,
 * NOT_SUPPORTED
 * NESTED
 * 1.由于transName名称的方法定义了REQUIRED,故SQLTrans里的REQUIRED,SUPPORTS,MANDATORY 具有相同的行为
 * 2.由于transName名称的方法定义了REQUIRED,故SQLTrans里的传播属性Never不能使用.
 * 3.REQUIRED,SUPPORTS,MANDATORY 是相同行为,暂时用一个REQUIRED替代
 * @see com.cdoframework.transaction.Propagation 
 * @author Kenel
 */
public abstract class TransService implements ITransService
{
	private Logger logger = Logger.getLogger(TransService.class);
	private String strServiceName;
	protected IServiceBus serviceBus=null;
	protected IServicePlugin servicePlugin=null;
	protected IService service = null;
	
	protected Map<String, Method> transMap = new HashMap<String, Method>();


	final public void setServiceBus(IServiceBus serviceBus)
	{
		
		this.serviceBus=serviceBus;
	}

	final public void setServicePlugin(IServicePlugin servicePlugin)
	{
		this.servicePlugin=servicePlugin;
	}

	final public void setService(IService service)
	{
		this.service = service;
	}
	final public IService getService()
	{
		return this.service;
	}
	public Return init()
	{	
		return Return.OK;
	}
	
	@Override
	public void inject(ITransService child) {
		if(child != null)
		{
			Class<?> cls = child.getClass();
			Method[] methods = cls.getMethods();
			TransName transName = null;
			String name = null;
			for(Method method : methods) {
				// 查找所有带@TransName方法
				if(method.isAnnotationPresent(TransName.class)) {
					transName = method.getAnnotation(TransName.class);					
					name = transName.name();		
					if(name == null || name.equals("")) {
						name = method.getName();
					}
					// 一个服务类禁止出现重名的transName
					if(transMap.put(name, method) != null) {
						logger.error("存在同名的transName："+ name);
						System.exit(-1);
					}
				}
			}
		}
	}
	
		
	/**
	 * 设置服务名
	 * @param strServiceName
	 */
	final public void setServiceName(String strServiceName)
	{
		this.strServiceName = strServiceName;
	}

	/**
	 * 取服务名
	 * @return
	 */
	final public String getServiceName()
	{
		return this.strServiceName;
	}

	public void destroy()
	{
	}

	public void handleEvent(CDO cdoEvent)
	{
	}
		

	@Override
	public final Return processTrans(CDO cdoRequest, CDO cdoResponse) {
		String strTransName = cdoRequest.getStringValue(ITransService.TRANSNAME_KEY);
		Method method = null;
		if((method = transMap.get(strTransName)) != null) {
			TransName transName = method.getAnnotation(TransName.class);					
			boolean autoStartTransaction=!transName.denyAutoStartTransaction();
			TransactionThreadLocal transaction=null;
			try {
				if(autoStartTransaction){
					transaction=new TransactionThreadLocal();
					doBegin(transaction);
				}		
				Return ret=(Return) method.invoke(this, cdoRequest, cdoResponse);	
				if(autoStartTransaction){
					commit(transaction);
				}
				return ret;			
			}catch (SQLException e) {
				logger.error(strTransName+":调用开启/提交事务时发生错误,message="+e.getMessage(),e);
				if(autoStartTransaction){try{rollback(transaction);} catch (SQLException e1){}}
				return Return.valueOf(-1, strTransName+": 函数调用错误InvocationTargetException,message="+e.getMessage());
			}catch(Throwable e){
				logger.error(strTransName+":调用时发生异常,message="+e.getMessage(),e);
				if(autoStartTransaction){try{rollback(transaction);} catch (SQLException e1){}}
				return Return.valueOf(-1, strTransName+": 函数调用错误InvocationTargetException,message="+e.getMessage());
			}
		} 

		return null;
	}
	
	void doBegin(TransactionThreadLocal transaction) throws SQLException{
		 Map<String, DBPool> HmDBPool=DBPoolManager.getInstances().getHmDBPool();
		 for( Iterator<String> it=HmDBPool.keySet().iterator();it.hasNext();){
			 transaction.doBegin(it.next());
		 }
	}
	
	void commit(TransactionThreadLocal transaction) throws SQLException{
		 Map<String, DBPool> HmDBPool=DBPoolManager.getInstances().getHmDBPool();
		 for( Iterator<String> it=HmDBPool.keySet().iterator();it.hasNext();){
			 transaction.commit(it.next());
		 }
	}
	
	 void rollback(TransactionThreadLocal transaction) throws SQLException{
		 Map<String, DBPool> HmDBPool=DBPoolManager.getInstances().getHmDBPool();
		 for( Iterator<String> it=HmDBPool.keySet().iterator();it.hasNext();){
			 transaction.rollback(it.next());
		 }
	 }
	
	
	@Override
	public boolean containsTrans(String strTransName) {
		return transMap.containsKey(strTransName);
	}

	@Override
	public Connection getConnection(String strDataGroupId) throws SQLException{
		IDataEngine dataEngine=this.serviceBus.getHMDataEngine().get(strDataGroupId);
		return dataEngine.getConnection();
	}
	@Override
	public String getDBCharset(String strDataGroupId){
		IDataEngine dataEngine=this.serviceBus.getHMDataEngine().get(strDataGroupId);
		return dataEngine.getDBPool().getCharset();
	}
}
