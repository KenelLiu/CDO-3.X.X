package com.cdoframework.cdolib.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.cdo.field.Field;
import com.cdoframework.cdolib.data.cdo.CDO;
import com.cdoframework.cdolib.data.cdo.CDOArrayField;

public interface IDataEngine{
	//==============数据库连接=======//
	public Connection getConnection() throws SQLException;
	
	public void setDBPool(DBPool dbPool);
	
	public DBPool getDBPool();
	/**
	 * 
	 * @param conn
	 * @param strSourceSQL 含有{}变量符的原始SQL
	 * @param cdoRequest
	 * @return
	 * @throws SQLException
	 */
	public PreparedStatement prepareStatement(Connection conn,String strSourceSQL,CDO cdoRequest) throws SQLException;
	
	/**
	 * 读取当前的记录数据
	 * @param rs
	 * @param strsFieldName 字段名 @ResultSetMetaData.getColumnLabel(i+1)
	 * @param naFieldType 字段类型 @ResultSetMetaData..getColumnType(i+1)
	 * @param nsPrecision @ResultSetMetaData.getPrecision(i+1)
	 * @param nsScale @ResultSetMetaData.getScale(i+1);
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	public CDO readRecord(ResultSet rs,String[] strsFieldName,int[] naFieldType,int[] nsPrecision,int[] nsScale) throws SQLException,IOException;
	/**
	 * 读取当前的记录数据
	 * @param rs
	 * @param strsFieldName 字段名 @ResultSetMetaData.getColumnLabel(i+1)
	 * @param naFieldType 字段类型 @ResultSetMetaData..getColumnType(i+1)
	 * @param nsPrecision @ResultSetMetaData.getPrecision(i+1)
	 * @param nsScale @ResultSetMetaData.getScale(i+1);
	 * @param cdoRecord
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	public int readRecord(ResultSet rs,String[] strsFieldName,int[] naFieldType,int[] nsPrecision,int[] nsScale,CDO cdoRecord) throws SQLException,IOException;

	/**
	 * 查询并输出第一条记录的第一个字段
	 * @param conn
	 * @param strSourceSQL 含有{}变量符的原始SQL
	 * @param cdoRequest
	 * @param cdoResponse
	 * @return
	 * @throws Exception
	 */
	public Field executeQueryField(Connection conn,String strSourceSQL,CDO cdoRequest) throws SQLException,IOException;

	/**
	 * 接查询并输出第一条记录的第一个字段(含类型)
	 * 
	 * @param conn
	 * @param strSourceSQL 含有{}变量符的原始SQL
	 * @param cdoRequest
	 * @param cdoResponse
	 * @return
	 * @throws Exception
	 */
	public Field executeQueryFieldExt(Connection conn,String strSourceSQL,CDO cdoRequest) throws SQLException,IOException;

	/**
	 * 查询并输出第一条记录
	 * 
	 * @param conn
	 * @param strSourceSQL 含有{}变量符的原始SQL
	 * @param cdoRequest
	 * @param cdoResponse
	 * @return
	 * @throws Exception
	 */
	public int executeQueryRecord(Connection conn,String strSourceSQL,CDO cdoRequest,CDO cdoResponse) throws SQLException,
					IOException;

	/**
	 * 查询并输出所有记录
	 * 
	 * @param conn
	 * @param strSourceSQL 含有{}变量符的原始SQL
	 * @param cdoRequest
	 * @param cafRecordSet
	 * @return
	 * @throws Exception
	 */
	public int executeQueryRecordSet(Connection conn,String strSourceSQL,CDO cdoRequest,CDOArrayField cdoArrayField)
					throws SQLException,IOException;

	/**
	 * 执行数据库插入,更新,删除语句,并返回影响的数据行
	 * 
	 * @param conn
	 * @param strSourceSQL 含有{}变量符的原始SQL
	 * @param cdoRequest
	 * @return
	 * @throws Exception
	 */
	public int executeUpdate(Connection conn,String strSourceSQL,CDO cdoRequest) throws SQLException,IOException;

	/**
	 * 执行数据库插入,更新,删除语句
	 * 
	 * @param conn
	 * @param strSourceSQL 含有{}变量符的原始SQL
	 * @param cdoRequest
	 * @return
	 * @throws Exception
	 */
	public void executeSQL(Connection conn,String strSourceSQL,CDO cdoRequest) throws SQLException,IOException;

	// 接口实现,所有实现接口函数的实现在此定义--------------------------------------------------------------------

	// 事件处理,所有重载派生类的事件类方法(一般为on...ed)在此定义-------------------------------------------------

	// 事件定义,所有在本类中定义并调用，由派生类实现或重载的事件类方法(一般为on...ed)在此定义---------------------
	public void onException(String strText,Exception e);

	public void onSQLStatement(String strSQL);
	
	public void onExecuteSQL(String strSQL,ArrayList<String> alParaName,CDO cdoRequest);
}
