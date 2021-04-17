package com.cdoframework.transaction;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
/**
 * 
 * @author Kenel
 *
 */
public class TransactionThreadLocal implements Transaction {
	private static final ThreadLocal<TransactionImpl> tranManager = new ThreadLocal<TransactionImpl>(){
		
		protected TransactionImpl initialValue() {
	       
	        return new TransactionImpl();
	     }
	};
	
	@Override
	public Connection getConnection(String strDataGroupId) throws SQLException {
		return tranManager.get().getConnection(strDataGroupId);
	}

	@Override
	public void doBegin(String strDataGroupId) throws SQLException {
		tranManager.get().doBegin(strDataGroupId);
	}


	@Override
	public void commit(String strDataGroupId) throws SQLException {
		TransactionImpl trans=tranManager.get();
		try{
			trans.commit(strDataGroupId);
		}finally{
			if(trans.isEmpty())
				tranManager.remove();
		}
	
	}

	@Override
	public void rollback(String strDataGroupId) throws SQLException {
		TransactionImpl trans=tranManager.get();
		try{
			trans.rollback(strDataGroupId);
		}finally{
			if(trans.isEmpty())
				tranManager.remove();
		}
	}


	public ThreadLocal<TransactionImpl> getThreadLocal(){
		return tranManager;
	}
}
