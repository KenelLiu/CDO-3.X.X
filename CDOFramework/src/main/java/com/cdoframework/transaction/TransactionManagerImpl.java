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
public class TransactionManagerImpl implements TransactionManager {
	//================保存每个数据库事务连接===============//
	private Map<String,Stack<Connection>> connMap=new HashMap<String,Stack<Connection>>();
	
	Map<String,DBPool> hmDBPool =DBPoolManager.getInstances().getHmDBPool();
	private Logger logger=Logger.getLogger(TransactionManagerImpl.class);
	@Override
	public Connection getConnection(String strDataGroupId) throws SQLException 	{
        Stack<Connection> stack=connMap.get(strDataGroupId);
        if (stack==null || stack.isEmpty()) {
        	stack=this.addConn(strDataGroupId,false);
        }
        return stack.peek();
	}

	@Override
	public void beginTransaction(String strDataGroupId) throws SQLException {
		this.addConn(strDataGroupId,false);
	}
	
	@Override
	public void beginNonTransaction(String strDataGroupId) throws SQLException{
		this.addConn(strDataGroupId,true);
	}
	@Override
	public void commit(String strDataGroupId) throws SQLException {
		Stack<Connection> stack=connMap.get(strDataGroupId);
		Connection conn=null;
        try {        
            if (stack!=null && stack.peek() != null){  
            	conn=stack.pop();
            	if(conn.getAutoCommit()==false)
            	   conn.commit();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            throw new SQLException(e.getMessage(),e);
        }finally{
        	try{if(conn!=null)conn.close();}catch(Exception ex){}
        	connMap.put(strDataGroupId,stack);
        }

	}

	@Override
	public void rollback(String strDataGroupId) throws SQLException {
		  this.rollback(strDataGroupId, null);
	}

	@Override
	public void rollback(String strDataGroupId, Savepoint savePoint) throws SQLException {
		  Stack<Connection> stack=connMap.get(strDataGroupId);
		  Connection conn=null;
	      try {
	            if (stack!=null && stack.peek() != null) {
	            	conn=stack.pop();
	            	if(conn.getAutoCommit()==false){
	            		if(savePoint!=null){
	            			conn.rollback(savePoint);
	            		}else{
	            			conn.rollback();
	            		}
	            	}
	            			            	
	            }
	        } catch (SQLException e) {
	            logger.error(e.getMessage(),e);
	            throw new SQLException(e.getMessage(),e);
	        }finally{
	        	try{if(conn!=null)conn.close();}catch(Exception ex){}
	        	connMap.put(strDataGroupId,stack);
	        }
		
	}
    private Stack<Connection>  addConn(String strDataGroupId,boolean isAutoCommit) throws SQLException {
        try {
        	DBPool dbPool=hmDBPool.get(strDataGroupId);
        	if(!dbPool.isOpened()){
        		Return ret=dbPool.open();
        		if(ret.getCode()!=Return.OK.getCode()){
        		  throw new SQLException(ret.getText(),ret.getThrowable());
        		}
        	}
            Connection conn=dbPool.getConnection(); 
            conn.setAutoCommit(isAutoCommit);
            Stack<Connection> stack=connMap.get(strDataGroupId);
            if(stack==null){
            	stack=new Stack<Connection>();
            }            
            stack.push(conn);  
            connMap.put(strDataGroupId, stack);
            return stack;
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            throw new SQLException(e.getMessage(),e);
        } 
    }
    
    public boolean isExistsTransaction(String strDataGroupId) throws SQLException{
        Stack<Connection> stack=connMap.get(strDataGroupId);
        if(stack==null || stack.isEmpty()){
        	return false;
        } 
        return !stack.peek().getAutoCommit();
    }

   public boolean isEmpty(String strDataGroupId){
       Stack<Connection> stack=connMap.get(strDataGroupId);
       if(stack==null || stack.isEmpty()){
       		return true;
       } 
       return false;
    }
}
