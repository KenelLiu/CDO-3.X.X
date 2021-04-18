package com.cdoframework.transaction;

import java.sql.Connection;
import java.sql.SQLException;

public interface Transaction {
	 /**	  
	  * 获取当前连接使用.  
	  * @param strDataGroupId
	  * @return
	  * @throws SQLException 
	  */
	 Connection getConnection(String strDataGroupId) throws SQLException;
	 /**
	  * 开启一个新事务,创建物理连接.
	  * 1 若连接不存在,首次创建连接,引用计数器为1
	  * 2 若连接已经存在,则引用计数器加1
	  * 在进入 transName方法前调用
	  * @param strDataGroupId
	  * @throws SQLException 
	  */
	 void doBegin(String strDataGroupId) throws SQLException;	 
	 /**
	  * 在退出 transName方法后调用
	  * 1.当连接引用次数大于0时,则计数器减一
	  * 2.当连接引用次数等于0时,则进行事务实际提交
	  * @param strDataGroupId
	  * @throws SQLException 
	  */
	 void commit(String strDataGroupId) throws SQLException;
	 /**
	  * 事务回滚
	  * 通过反射调用方法的抛出InvokeTargetException异常处理事务回滚.
	  * @param strDataGroupId
	  * @throws SQLException 
	  */
	 void rollback(String strDataGroupId) throws SQLException;
	 
	 
}
