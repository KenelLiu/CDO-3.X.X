package com.cdoframework.transaction;

import java.sql.Connection;
import java.sql.SQLException;

public interface TransactionManager {
	 /**
	  * 获取当前连接,即为stack栈顶元素
	  * @param strDataGroupId
	  * @return
	 * @throws SQLException 
	  */
	 Connection getConnection(String strDataGroupId) throws SQLException;
	 /**
	  * 开启一个事务
	  * @param strDataGroupId
	 * @throws SQLException 
	  */
	 void beginTransaction(String strDataGroupId) throws SQLException;
	 /**
	  * 提交一个事务
	  * @param strDataGroupId
	 * @throws SQLException 
	  */
	 void commit(String strDataGroupId) throws SQLException;
	 /**
	  * 回滚一个事务
	  * @param strDataGroupId
	 * @throws SQLException 
	  */
	 void rollback(String strDataGroupId) throws SQLException;
	 /**
	  * 
	  * @param strDataGroupId
	  * @return
	  */
	 boolean isEmpty(String strDataGroupId);
}
