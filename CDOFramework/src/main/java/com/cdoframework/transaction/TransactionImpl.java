package com.cdoframework.transaction;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.apache.log4j.Logger;

import com.cdoframework.cdolib.base.Return;
import com.cdoframework.cdolib.database.DBPool;
import com.cdoframework.cdolib.database.DBPoolManager;
/**
 * 
 * @author Kenel
 *
 */
public class TransactionImpl implements Transaction {
	//================保存每个数据库事务连接===============//
	private Map<String,ConnectionHolder> connMap=new HashMap<String,ConnectionHolder>();
	private Logger logger=Logger.getLogger(TransactionManagerImpl.class);
	@Override
	public Connection getConnection(String strDataGroupId) throws SQLException 	{
		ConnectionHolder holder=connMap.get(strDataGroupId);
		if(holder==null)
			holder=this.addConn(strDataGroupId, false);
        return holder.getCurConnction();
	}
	/**
	  * 1 若连接不存在,首次创建连接,引用次数为1
	  * 2 若连接已经存在,则引用次数加1
	 */
	@Override
	public void doBegin(String strDataGroupId) throws SQLException {
		ConnectionHolder holder=connMap.get(strDataGroupId);
		if(holder!=null){
			holder.addReference();
			return;
		}
		this.addConn(strDataGroupId,false);
	}

	@Override
	public void commit(String strDataGroupId) throws SQLException {
		Connection conn=null;
        try {    
        	ConnectionHolder holder=connMap.get(strDataGroupId);
        	if(holder==null){
        		connMap.remove(strDataGroupId);
        		throw new SQLException("["+strDataGroupId+"] ConnectionHolder is null,can't commit");
        	}
        	holder.decReference();
        	if(holder.getReferenceCount()==0){
        		try{
        			conn=holder.getCurConnction();
        			conn.commit();
        		 }finally{
        			//=====事务能提交,则所有任务已结束====//
        			 connMap.remove(strDataGroupId);
        		 }
        	}  
        } catch (Exception e) {
        	logger.error(e.getMessage(),e);
            throw new SQLException(e.getMessage(),e);
        }finally{
        	try{if(conn!=null)conn.close();}catch(Exception ex){}	
        }

	}

	@Override
	public void rollback(String strDataGroupId) throws SQLException {
		  Connection conn=null;
	      try {
	    	  ConnectionHolder holder=connMap.get(strDataGroupId);
   			  if(holder==null){
   				throw new SQLException("["+strDataGroupId+"] ConnectionHolder is null,can't rollback"); 
  			  }
   			  conn=holder.getCurConnction();
  			  conn.rollback();
	        } catch (Exception e) {
	        	logger.error(e.getMessage(),e);
	            throw new SQLException(e.getMessage(),e);
	        }finally{
	        	try{if(conn!=null)conn.close();}catch(Exception ex){}
	        	//=====事务回滚,所有任务结束====//
	        	connMap.remove(strDataGroupId);
	        }		
	}
	
    private ConnectionHolder addConn(String strDataGroupId,boolean isAutoCommit) throws SQLException {
        try {
        	Map<String,DBPool> hmDBPool =DBPoolManager.getInstances().getHmDBPool();
        	DBPool dbPool=hmDBPool.get(strDataGroupId);
        	if(!dbPool.isOpened()){
        		Return ret=dbPool.open();
        		if(ret.getCode()!=Return.OK.getCode()){
        		  throw new SQLException(ret.getText(),ret.getThrowable());
        		}
        	}
            Connection conn=dbPool.getConnection(); 
            conn.setAutoCommit(isAutoCommit);
            ConnectionHolder holder=new ConnectionHolder(conn);
            connMap.put(strDataGroupId, holder);
            return holder;
        } catch (Exception e) {
        	logger.error(e.getMessage(),e);
            throw new SQLException(e.getMessage(),e);
        } 
    }
    
    public boolean isEmpty(){
    	 return connMap.isEmpty();
    }
  
}