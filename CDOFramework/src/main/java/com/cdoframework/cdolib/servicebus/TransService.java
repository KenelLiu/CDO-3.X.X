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
import com.cdoframework.cdolib.database.IDataEngine;
import com.cdoframework.transaction.TransactionChainThreadLocal;
import com.cdoframework.transaction.TransactionThreadLocal;
/**
 * 增加事务传播属性
 * 为了方便处理,在定义了transName名称的方法上,
 * 默认 自动开启事务 @see TransName autoStartTransaction=true,且传播属性值为Propagation.REQUIRED
 * 在 XML里的SQLTrans的Propagation属性 ,可以定义为
 * REQUIRED,
 * SUPPORTS,
 * MANDATORY
 * REQUIRES_NEW,
 * NOT_SUPPORTED
 * NEVER
 * NESTED
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
			boolean autoStartTransaction=transName.autoStartTransaction();
			String dataGroupId=transName.dataGroupId();
			TransactionThreadLocal transaction=null;
			TransactionChainThreadLocal transactionChain=new TransactionChainThreadLocal();			
			try {
				//=======当前方法autoStartTransaction属性值入栈==//
				Return ret=push(transactionChain,dataGroupId,autoStartTransaction);
				if(ret.getCode()!=Return.OK.getCode()){
					return Return.valueOf(-1,strTransName+ret.getText());
				}
				//=======当前方法是否是自动启动事务,开启事务================//
				if(autoStartTransaction){
					transaction=new TransactionThreadLocal();
					doBegin(transaction,dataGroupId);					
				}		
				//============具体方法调用====================//
				ret=(Return) method.invoke(this, cdoRequest, cdoResponse);
				//============当前方法是否是自动启动事务,提交处理====================//
				if(autoStartTransaction){
					commit(transaction,dataGroupId);					
				}
				return ret;			
			}catch (SQLException e) {
				logger.error(strTransName+":调用开启/提交事务时发生错误,message="+e.getMessage(),e);
				if(autoStartTransaction){try{rollback(transaction,dataGroupId);} catch (SQLException e1){}}
				return Return.valueOf(-99,"调用某些方法处理数据发生错误,请查看后台日志.");
			}catch(Throwable e){
				logger.error(strTransName+":函数调用发生错误,message="+e.getMessage(),e);
				if(autoStartTransaction){try{rollback(transaction,dataGroupId);} catch (SQLException e1){}}
				return Return.valueOf(-99, "调用某些方法处理数据发生错误,请查看后台日志.");
			}finally{
				pop(transactionChain);
			}
		} 

		return null;
	}
	
	
	@Override
	public boolean containsTrans(String strTransName) {
		return transMap.containsKey(strTransName);
	}

	/**
	 * 从threadlocal里获取数据库连接,
	 * 进入TransName定义的方法前,自动开启事务Transname.autoStartTransaction=true时,
	 * 才能获得到连接,否则为null
	 * @param strDataGroupId
	 * @return
	 * @throws SQLException
	 */
	public Connection getConnectionFromThreadLocal(String strDataGroupId) throws SQLException{
		TransactionThreadLocal transaction=new TransactionThreadLocal();
		return transaction.getConnection(strDataGroupId);
	}
	/**
	 * 提供连接池里的connection
	 * @param strDataGroupId
	 * @return
	 * @throws SQLException
	 */
	public Connection getConnection(String strDataGroupId) throws SQLException{
		IDataEngine dataEngine=this.serviceBus.getHMDataEngine().get(strDataGroupId);
		return dataEngine.getConnection();
	}
	/**
	 * 获取数据库所在系统的编码
	 * @param strDataGroupId
	 * @return
	 */
	public String getDBCharset(String strDataGroupId){
		IDataEngine dataEngine=this.serviceBus.getHMDataEngine().get(strDataGroupId);
		return dataEngine.getDBPool().getCharset();
	}
	
	//=======================threadlocal ==================//
	Return push(TransactionChainThreadLocal transactionChain,String dataGroupId,boolean autoStartTransaction){
		 Map<String,IDataEngine> hmEngine=this.serviceBus.getHMDataEngine();
		 if(autoStartTransaction &&
				 dataGroupId!=null && dataGroupId.length()>0){
			  //==============指定了对某一数据源开启事务======//
			 boolean bFind=false;
			 for(Iterator<String> it=hmEngine.keySet().iterator();it.hasNext();){
				 String curDataGroup=it.next();
				 if(curDataGroup.equals(dataGroupId)){
					 transactionChain.pushAutoStartTransaction(curDataGroup, autoStartTransaction);
					 bFind=true;
				 }else{
					 //其他数据源设置为未开启事务
					 transactionChain.pushAutoStartTransaction(curDataGroup,false);
				 }					 
			 }	
			 if(!bFind){
				 return Return.valueOf(-1, " Invalid datagroup id: "+dataGroupId);
			 }	
			 return Return.OK;
		 }		 		
		 //==========未指定对应某一数据源开启事务=========//
		 for(Iterator<String> it=hmEngine.keySet().iterator();it.hasNext();){
			 transactionChain.pushAutoStartTransaction(it.next(), autoStartTransaction);
		 }
		 return Return.OK; 
	}
	
	void pop(TransactionChainThreadLocal transactionChain){
		Map<String,IDataEngine> hmEngine=this.serviceBus.getHMDataEngine();	
		for( Iterator<String> it=hmEngine.keySet().iterator();it.hasNext();){
			 transactionChain.popAutoStartTransaction(it.next());
		}
	}
	
	void doBegin(TransactionThreadLocal transaction,String dataGroupId) throws SQLException{
		if(dataGroupId!=null && dataGroupId.length()>0){
			transaction.doBegin(dataGroupId);
			return;
		}		
		Map<String,IDataEngine> hmEngine=this.serviceBus.getHMDataEngine();	
		for(Iterator<String> it=hmEngine.keySet().iterator();it.hasNext();){
			 transaction.doBegin(it.next());
		}
	}
	
	void commit(TransactionThreadLocal transaction,String dataGroupId) throws SQLException{
		if(dataGroupId!=null && dataGroupId.length()>0){
			transaction.commit(dataGroupId);
			return;
		}
		Map<String,IDataEngine> hmEngine=this.serviceBus.getHMDataEngine();
		for(Iterator<String> it=hmEngine.keySet().iterator();it.hasNext();){
			 transaction.commit(it.next());
		 }
	}
	
	 void rollback(TransactionThreadLocal transaction,String dataGroupId) throws SQLException{
		if(dataGroupId!=null && dataGroupId.length()>0){
			transaction.rollback(dataGroupId);
			return;
		}		 
		Map<String,IDataEngine> hmEngine=this.serviceBus.getHMDataEngine();
		for( Iterator<String> it=hmEngine.keySet().iterator();it.hasNext();){
			 transaction.rollback(it.next());
		 }
	 }
		
}
