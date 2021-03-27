package com.cdoframework.transaction;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.apache.log4j.Logger;

import com.cdoframework.cdolib.base.Return;
import com.cdoframework.cdolib.database.DBPool;
import com.cdoframework.cdolib.database.DBPoolManager;

public class TransactionManagerImpl implements TransactionManager {

	private Map<String,Stack<Connection>> connMap=new HashMap<String,Stack<Connection>>();
	Map<String,DBPool> hmDBPool =DBPoolManager.getInstances().getHmDBPool();
	private Logger logger=Logger.getLogger(TransactionManagerImpl.class);
	@Override
	public Connection getConnection(String strDataGroupId) throws SQLException 	{
        Stack<Connection> connections=connMap.get(strDataGroupId);
        if(connections==null){
        	connections=new Stack<Connection>();
        } 
        if (connections.isEmpty()) {
        	connections=this.addConn(strDataGroupId);
        }
        return connections.peek();
	}

	@Override
	public void beginTransaction(String strDataGroupId) throws SQLException {
		this.addConn(strDataGroupId);
	}

	@Override
	public void commit(String strDataGroupId) throws SQLException {
        try {
        	Stack<Connection> connections=connMap.get(strDataGroupId);
            if (connections!=null && connections.peek() != null 
            		&& !connections.peek().isClosed()) {               
                connections.peek().commit();
                connections.pop().close();
                connMap.put(strDataGroupId, connections);
            }
 
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            throw new SQLException(e.getMessage(),e);
        }

	}

	@Override
	public void rollback(String strDataGroupId) throws SQLException {
	       try {
	    	    Stack<Connection> connections=connMap.get(strDataGroupId);
	            if (connections!=null && connections.peek() != null
	            		&& !connections.peek().isClosed()) {
	                
	                connections.peek().rollback();
	                connections.pop().close();
	                connMap.put(strDataGroupId, connections);
	            }
	        } catch (SQLException e) {
	            logger.error(e.getMessage(),e);
	            throw new SQLException(e.getMessage(),e);
	        }

	}

    private Stack<Connection>  addConn(String strDataGroupId) throws SQLException {
        try {
        	DBPool dbPool=hmDBPool.get(strDataGroupId);
        	if(!dbPool.isOpened()){
        		Return ret=dbPool.open();
        		if(ret.getCode()!=Return.OK.getCode()){
        		  throw new SQLException(ret.getText(),ret.getThrowable());
        		}
        	}
            Connection conn=dbPool.getConnection(); 
            conn.setAutoCommit(false);
            Stack<Connection> connections=connMap.get(strDataGroupId);
            if(connections==null){
            	connections=new Stack<Connection>();
            }            
            connections.push(conn);  
            connMap.put(strDataGroupId, connections);
            return connections;
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            throw new SQLException(e.getMessage(),e);
        } 
    }
    
    public boolean isEmpty(String strDataGroupId){
        Stack<Connection> connections=connMap.get(strDataGroupId);
        if(connections==null || connections.isEmpty()){
        	return true;
        } 
        return false;
    }
}
