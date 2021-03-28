package com.cdoframework.transaction;

import java.sql.Connection;
import java.sql.SQLException;

public interface TransactionManager {
	 /**
	  * 获取当前连接,即为stack栈顶元素,且连接不出栈
	  * @param strDataGroupId
	  * @return
	 * @throws SQLException 
	  */
	 Connection getConnection(String strDataGroupId) throws SQLException;
	 /**
	  * 开启一个事务
	  * 新建连接入栈 
	  * @param strDataGroupId
	  * @throws SQLException 
	  */
	 void beginTransaction(String strDataGroupId) throws SQLException;
	 /**
	  * 提交一个事务
	  * 获取stack栈顶连接,且连接出栈
	  * @param strDataGroupId
	 * @throws SQLException 
	  */
	 void commit(String strDataGroupId) throws SQLException;
	 /**
	  * 回滚一个事务
	  * 获取stack栈顶连接,且连接出栈
	  * @param strDataGroupId
	  * @throws SQLException 
	  */
	 void rollback(String strDataGroupId) throws SQLException;
	 /**
	  * 当前是否存在事务
	  * 通过判断stack里的连接
	  * @param strDataGroupId
	  * @return
	  */
	 boolean isExistsTransaction(String strDataGroupId);
}
