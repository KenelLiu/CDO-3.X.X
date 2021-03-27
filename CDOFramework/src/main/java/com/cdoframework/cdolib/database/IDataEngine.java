/**
 * www.cdoforum.com 2007版权所有
 * 
 * $Header: /CVSData/Frank/CVSROOT/CDOForum/CDODatabase/Source/com/cdoframework/cdolib/database/IDataEngine.java,v 1.1
 * 2008/04/12 13:35:46 Frank Exp $
 * 
 * $Log: IDataEngine.java,v $ Revision 1.1 2008/04/12 13:35:46 Frank *** empty log message ***
 * 
 * Revision 1.1 2008/03/05 01:34:58 Frank *** empty log message ***
 * 
 * Revision 1.8 2008/02/23 05:18:43 Frank *** empty log message ***
 * 
 * Revision 1.7 2008/01/11 12:31:44 Frank *** empty log message ***
 * 
 * Revision 1.6 2008/01/07 10:55:25 Frank *** empty log message ***
 * 
 * Revision 1.5 2008/01/04 13:20:34 Frank *** empty log message ***
 * 
 * Revision 1.4 2007/12/27 12:28:06 Frank *** empty log message ***
 * 
 * Revision 1.3 2007/12/15 09:35:39 Frank *** empty log message ***
 * 
 * Revision 1.2 2007/12/15 09:07:13 Frank *** empty log message ***
 * 
 * Revision 1.1 2007/11/03 03:00:58 Frank *** empty log message ***
 * 
 * 
 */

package com.cdoframework.cdolib.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import com.cdo.field.Field;
import com.cdoframework.cdolib.base.Return;
import com.cdoframework.cdolib.data.cdo.CDO;
import com.cdoframework.cdolib.data.cdo.CDOArrayField;
import com.cdoframework.cdolib.database.xsd.SQLTrans;

public interface IDataEngine
{
	// 属性对象,所有在本类中创建，并允许外部访问的对象在此声明并提供get/set方法-----------------------------------
	public void setDriver(String strDriver);
	public String getDriver();

	public void setURI(String strURI);
	public String getURI();

	public String getCharset();
	public void setCharset(String strCharset);

	public Properties getProperties();
	public void setProperties(Properties properties);

	public String getUserName();
	public void setUserName(String strUserName);

	public String getPassword();
	public void setPassword(String strPassword);
	
	public void setInitialSize(int nInitalSize);
	public int getInitialSize();

	public void setMinIdle(int nMinIdle);	
	public int getMinIdle();
	
	public void setMaxIdle(int nMaxIdle);
	public int getMaxIdle();	    
	
	public long getnMaxConnLifetimeMillis();
	public void setnMaxConnLifetimeMillis(long nMaxConnLifetimeMillis);
	
	public int getnMaxTotal();
	public void setnMaxTotal(int nMaxTotal);

	public void setRemoveAbandonedTimeout(int nRemoveAbandonedTimeout);		//removeAbandonedTimeout
	public void setTestWhileIdle(boolean bTestWhileIdle);
	public void setTestOnBorrow(boolean bTestOnBorrow);
	public void setValidationQuery(String strSql);
	public void setPoolPreparedStatements(boolean bPoolPreparedStatements);
	public void setRemoveAbandonedOnMaintenance(boolean bRemoveAbandonedOnMaintenance);
	public void setLogAbandoned(boolean bLogAbandoned);
	
	public boolean isOpened();
	/**
	 * 初始连接池
	 */
	public Return open();

	/**
	 * 关闭连接池
	 */
	public void close();

	/**
	 * 获取一个数据库连接
	 * 
	 * @return
	 */
	public Connection getConnection() throws SQLException;

	public void commit(Connection conn) throws SQLException;

	public void rollback(Connection conn);


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
