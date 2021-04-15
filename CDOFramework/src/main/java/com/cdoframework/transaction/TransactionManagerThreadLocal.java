package com.cdoframework.transaction;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
/**
 * 
 * @author Kenel
 *
 */
public class TransactionManagerThreadLocal implements TransactionManager {
	private static final ThreadLocal<TransactionManager> tranManager = new ThreadLocal<TransactionManager>(){
		
		protected TransactionManager initialValue() {
	       
	        return new TransactionManagerImpl();
	     }
	};
	
	@Override
	public Connection getConnection(String strDataGroupId) throws SQLException {
		return tranManager.get().getConnection(strDataGroupId);
	}

	@Override
	public void beginTransaction(String strDataGroupId) throws SQLException {
		tranManager.get().beginTransaction(strDataGroupId);
	}
	@Override
	public void beginNonTransaction(String strDataGroupId) throws SQLException {
		tranManager.get().beginNonTransaction(strDataGroupId);
	}

	@Override
	public void commit(String strDataGroupId) throws SQLException {
		tranManager.get().commit(strDataGroupId);
	}

	@Override
	public void rollback(String strDataGroupId) throws SQLException {
		tranManager.get().rollback(strDataGroupId);
	}

	@Override
	public void rollback(String strDataGroupId,Savepoint savePoint) throws SQLException {
		tranManager.get().rollback(strDataGroupId,savePoint);
	}
	@Override
	public boolean isExistsTransaction(String strDataGroupId) throws SQLException{
		return tranManager.get().isExistsTransaction(strDataGroupId);
	}
	
	@Override
	public boolean isEmpty(String strDataGroupId){
		return tranManager.get().isEmpty(strDataGroupId);
	}
	
	public ThreadLocal<TransactionManager> getThreadLocal(){
		return tranManager;
	}
}
