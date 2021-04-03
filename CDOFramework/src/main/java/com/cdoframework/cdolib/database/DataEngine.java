package com.cdoframework.cdolib.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import com.cdo.field.Field;
import com.cdo.util.sql.SQLUtil;
import com.cdoframework.cdolib.base.Return;
import com.cdoframework.cdolib.data.cdo.CDO;
import com.cdoframework.cdolib.data.cdo.CDOArrayField;

/**
 * 
 * @author Kenel
 *
 */
public class DataEngine implements IDataEngine{
	
	// 内部对象,所有在本类中创建并使用的对象在此声明--------------------------------------------------------------
	protected BasicDataSource ds;   
	protected String strSystemCharset;

	//==============================连接池相关========================//
	protected String strDriver;

	public void setDriver(String strDriver)
	{
		this.strDriver=strDriver;
	}

	public String getDriver()
	{
		return this.strDriver;
	}

	protected String strURI;

	public void setURI(String strURI)
	{
		this.strURI=strURI;
	}

	public String getURI()
	{
		return this.strURI;
	}

	protected String strCharset;

	public String getCharset()
	{
		return strCharset;
	}

	public void setCharset(String strCharset)
	{
		this.strCharset=strCharset;
	}

	protected Properties properties;

	public Properties getProperties()
	{
		return properties;
	}

	public void setProperties(Properties properties)
	{
		this.properties=properties;
	}

	protected String strUserName;

	public String getUserName()
	{
		return strUserName;
	}

	public void setUserName(String strUserName)
	{
		this.strUserName=strUserName;
	}

	protected String strPassword;

	public String getPassword()
	{
		return strPassword;
	}

	public void setPassword(String strPassword)
	{
		this.strPassword=strPassword;
	}
	
	protected int nInitialSize;
	
	public int getInitialSize() {
		return nInitialSize;
	}
	public void setInitialSize(int nInitialSize) {
		this.nInitialSize = nInitialSize;
	}

	protected int nMinIdle;

	public void setMinIdle(int nMinIdle)
	{
		this.nMinIdle=nMinIdle;
	}

	public int getMinIdle()
	{
		return this.nMinIdle;
	}

	protected int nMaxIdle;

	public void setMaxIdle(int nMaxIdle)
	{
		this.nMaxIdle=nMaxIdle;
	}

	public int getMaxIdle()
	{
		return this.nMaxIdle;
	}

	protected long nMaxConnLifetimeMillis=30*1000;
	protected int nMaxTotal;
	

	public long getnMaxConnLifetimeMillis() {
		return nMaxConnLifetimeMillis;
	}

	public void setnMaxConnLifetimeMillis(long nMaxConnLifetimeMillis) {
		this.nMaxConnLifetimeMillis = nMaxConnLifetimeMillis;
	}

	public int getnMaxTotal() {
		return nMaxTotal;
	}

	public void setnMaxTotal(int nMaxTotal) {
		this.nMaxTotal = nMaxTotal;
	}
	
	protected int nRemoveAbandonedTimeout=90;	
	public void setRemoveAbandonedTimeout(int nRemoveAbandonedTimeout){
		this.nRemoveAbandonedTimeout=nRemoveAbandonedTimeout;
	}
	protected boolean bTestWhileIdle=true;
	public void setTestWhileIdle(boolean bTestWhileIdle){
		this.bTestWhileIdle=bTestWhileIdle;
	}
	protected boolean bTestOnBorrow=true;
	public void setTestOnBorrow(boolean bTestOnBorrow){
		this.bTestOnBorrow=bTestOnBorrow;
	}
	protected String strValidationQuery="select 1";
	public void setValidationQuery(String strValidationQuery){
		this.strValidationQuery=strValidationQuery;
	}
	protected boolean bPoolPreparedStatements=true;
	public void setPoolPreparedStatements(boolean bPoolPreparedStatements){
		this.bPoolPreparedStatements=bPoolPreparedStatements;
	}
	protected boolean bRemoveAbandonedOnMaintenance=true;
	public void setRemoveAbandonedOnMaintenance(boolean bRemoveAbandonedOnMaintenance){
		this.bRemoveAbandonedOnMaintenance=bRemoveAbandonedOnMaintenance;
	}
	protected boolean bLogAbandoned=true;
	public void setLogAbandoned(boolean bLogAbandoned){
		this.bLogAbandoned=bLogAbandoned;
	}
	
	public boolean isOpened()
	{
		if(ds==null){
			return false;
		}else{
			return true;
		}
	}

	public synchronized Return open()
	{
		if(ds!=null)
		{
			return Return.OK;
		}

		
		ds=new BasicDataSource();   
        ds.setDriverClassName(strDriver);   
        ds.setUsername(strUserName);   
        ds.setPassword(strPassword);   
        ds.setUrl(strURI);   
        
        ds.setInitialSize(Math.max(nInitialSize,1));//initialSize
        ds.setMinIdle(Math.max(nMinIdle,1));	//minIdle
        ds.setMaxIdle(Math.max(nMaxIdle, 5));   // maxIdle     
        ds.setMaxTotal(Math.max(nMaxTotal,5));//maxTotal
        
        ds.setMaxConnLifetimeMillis(nMaxConnLifetimeMillis);//maxConnLifetimeMillis
        ds.setRemoveAbandonedTimeout(nRemoveAbandonedTimeout);		//removeAbandonedTimeout
        ds.setTestWhileIdle(bTestWhileIdle);
        ds.setTestOnBorrow(bTestOnBorrow);
        ds.setValidationQuery(strValidationQuery);
        ds.setPoolPreparedStatements(bPoolPreparedStatements);       
        ds.setRemoveAbandonedOnMaintenance(bRemoveAbandonedOnMaintenance);//removeAbandonedOnMaintenance        
        ds.setLogAbandoned(bLogAbandoned);           
		// 打开连接
		try
		{
			Connection conn=ds.getConnection();
			SQLUtil.closeConnection(conn);
			
		}catch(Exception e){
			callOnException("Open database error: "+e.getMessage(),e);

			Return ret=Return.valueOf(-1,e.getMessage(),"System.Error");
			ret.setThrowable(e);
			return ret;

		}

		return Return.OK;
	}

	/**
	 * 关闭数据库
	 * 
	 */
	public synchronized void close()
	{
		if(ds!=null)
		{
			try
			{
				ds.close();
			}
			catch(Exception e)
			{
			}
			ds=null;
		}
		SQLUtil.closeAnalyzedSQL();
	}

	/**
	 * 获取一个数据库连接
	 * 
	 * @return
	 */
	public Connection getConnection() throws SQLException
	{
		if(!isOpened()){
			return null;
		}
		return ds.getConnection();
	}

	public void commit(Connection conn) throws SQLException{
		conn.commit();
	}

	public void rollback(Connection conn){
		try
		{
			if(conn.getAutoCommit()==false)
			{
				conn.rollback();
			}
		}catch(Exception e){
		}
	}
	
  //==============================连接池相关 END========================//
	
	
	// 引用对象,所有在外部创建并传入使用的对象在此声明并提供set方法-----------------------------------------------

	// 内部方法,所有仅在本类或派生类中使用的函数在此定义为protected方法-------------------------------------------
	protected void callOnException(String strText,Exception e)
	{
		try
		{
			onException(strText,e);
		}
		catch(Exception ex)
		{
		}
	}
	/**
	 * 
	 * @param conn
	 * @param strSourceSQL 含有{}变量符的原始SQL
	 * @param cdoRequest
	 * @return
	 * @throws SQLException
	 */
	@Override
	public PreparedStatement prepareStatement(Connection conn,String strSourceSQL,CDO cdoRequest) throws SQLException{
		return SQLUtil.prepareStatement(conn, strSourceSQL, cdoRequest, strCharset);
	}
	
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
	public CDO readRecord(ResultSet rs,String[] strsFieldName,int[] naFieldType,int[] nsPrecision,int[] nsScale) throws SQLException,IOException{
		CDO cdoRecord=new CDO();

		if(readRecord(rs,strsFieldName,naFieldType,nsPrecision,nsScale,cdoRecord)==0)
		{
			return null;
		}
		
		return cdoRecord;
	}

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
	public int readRecord(ResultSet rs,String[] strsFieldName,int[] naFieldType,int[] nsPrecision,int[] nsScale,CDO cdoRecord) throws SQLException,IOException
	{
		
		return SQLUtil.readRecord(rs, strsFieldName, naFieldType, nsPrecision, nsScale, cdoRecord, strCharset);
		
	}

	/**
	 * 查询并输出第一条记录的第一个字段
	 * @param conn
	 * @param strSourceSQL 含有{}变量符的原始SQL
	 * @param cdoRequest
	 * @param cdoResponse
	 * @return
	 * @throws Exception
	 */
	public Field executeQueryField(Connection conn,String strSourceSQL,CDO cdoRequest) throws SQLException,IOException{
		// 准备JDBC语句
		PreparedStatement ps=prepareStatement(conn,strSourceSQL,cdoRequest);

		// 输出查询结果
		ResultSet rs=null;
		try
		{
			// 执行查询
			rs=ps.executeQuery();
			// 读取记录信息
			ResultSetMetaData meta=rs.getMetaData();
			String[] strsFieldName=new String[1];
			int[] nsFieldType=new int[1];
			int[] nsPrecision=new int[1];
			int[] nsScale=new int[1];
			for(int i=0;i<strsFieldName.length;i++)
			{
				strsFieldName[i]=meta.getColumnLabel(i+1);
				nsFieldType[i]=meta.getColumnType(i+1);
				nsPrecision[i]=meta.getPrecision(i+1);
				nsScale[i]=meta.getScale(i+1);
			}

			CDO cdoRecord=readRecord(rs,strsFieldName,nsFieldType,nsPrecision,nsScale);
			if(cdoRecord==null){
				return null;
			}
			// 输出
			if(cdoRecord.exists(strsFieldName[0]))
			{
				return cdoRecord.getObject(strsFieldName[0]);
			}
			else
			{
				return null;
			}
		}
		catch(SQLException e)
		{
			callOnException("executeQueryField Exception: "+strSourceSQL,e);
			throw e;
		}
		finally
		{
			SQLUtil.closeResultSet(rs);
			SQLUtil.closePreparedStatement(ps);
		}
	}

	/**
	 * 查询并输出第一条记录的第一个字段(含类型)
	 * 
	 * @param conn
	 * @param strSourceSQL 含有{}变量符的原始SQL
	 * @param cdoRequest
	 * @param cdoResponse
	 * @return
	 * @throws Exception
	 */
	public Field executeQueryFieldExt(Connection conn,String strSourceSQL,CDO cdoRequest) throws SQLException,IOException{
		// 准备JDBC语句
		PreparedStatement ps=prepareStatement(conn,strSourceSQL,cdoRequest);
		// 输出查询结果
		ResultSet rs=null;
		try
		{
			// 执行查询
			rs=ps.executeQuery();
			ResultSetMetaData meta=rs.getMetaData();
			String[] strsFieldName=new String[1];
			int[] nsFieldType=new int[1];
			int[] nsPrecision=new int[1];
			int[] nsScale=new int[1];
			for(int i=0;i<strsFieldName.length;i++)
			{
				strsFieldName[i]=meta.getColumnLabel(i+1);
				nsFieldType[i]=meta.getColumnType(i+1);
				nsPrecision[i]=meta.getPrecision(i+1);
				nsScale[i]=meta.getScale(i+1);
			}

			// 读取记录信息
			CDO cdoRecord=readRecord(rs,strsFieldName,nsFieldType,nsPrecision,nsScale);
			if(cdoRecord==null)
			{
				return null;
			}
			// 输出
			if(cdoRecord.exists(strsFieldName[0]))
			{
				return cdoRecord.getObject(strsFieldName[0]);
			}
			else
			{
				return null;
			}
		}
		catch(SQLException e)
		{
			callOnException("executeQueryField Exception: "+strSourceSQL,e);
			throw e;
		}
		finally
		{
			SQLUtil.closeResultSet(rs);
			SQLUtil.closePreparedStatement(ps);
		}
	}	
	
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
					IOException
	{
		// 准备JDBC语句 执行sql查询记录
		PreparedStatement ps=prepareStatement(conn,strSourceSQL,cdoRequest);
		// 输出查询结果
		ResultSet rs=null;
		try{
			// 执行查询
			rs=ps.executeQuery();
			ResultSetMetaData meta=rs.getMetaData();
			String[] strsFieldName=new String[meta.getColumnCount()];
			int[] nsFieldType=new int[strsFieldName.length];
			int[] nsPrecision=new int[strsFieldName.length];
			int[] nsScale=new int[strsFieldName.length];
			for(int i=0;i<strsFieldName.length;i++)
			{
				/**支持JDBC4**/
				strsFieldName[i]=meta.getColumnLabel(i+1);
				nsFieldType[i]=meta.getColumnType(i+1);
				nsPrecision[i]=meta.getPrecision(i+1);
				nsScale[i]=meta.getScale(i+1);
			}
			// 读取记录信息
			int nRecordCount=readRecord(rs,strsFieldName,nsFieldType,nsPrecision,nsScale,cdoResponse);
			//统计查询
			//int nCount=executeCount(conn, strSourceSQL, cdoRequest);
			return nRecordCount;
			
		}catch(SQLException e){
			callOnException("executeQueryRecord Exception: "+strSourceSQL,e);
			throw e;
		}finally{
			SQLUtil.closeResultSet(rs);
			SQLUtil.closePreparedStatement(ps);
		}
	}

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
	public int executeQueryRecordSet(Connection conn,String strSQL,CDO cdoRequest,CDOArrayField cdoArrayField)
					throws SQLException,IOException
	{
		// 准备JDBC语句 执行记录查询
		PreparedStatement ps=prepareStatement(conn,strSQL,cdoRequest);		
		// 输出查询结果
		ResultSet rs=null;
		try{
			// 执行查询
			rs=ps.executeQuery();
			// 读取Meta信息
			ResultSetMetaData meta=rs.getMetaData();
			String[] strsFieldName=new String[meta.getColumnCount()];
			int[] nsFieldType=new int[strsFieldName.length];
			int[] nsPrecision=new int[strsFieldName.length];
			int[] nsScale=new int[strsFieldName.length];
		
			for(int i=0;i<strsFieldName.length;i++){
				
				strsFieldName[i]=meta.getColumnLabel(i+1);
				nsFieldType[i]=meta.getColumnType(i+1);
				nsPrecision[i]=meta.getPrecision(i+1);
				nsScale[i]=meta.getScale(i+1);
			}
			// 读取记录
			ArrayList<CDO> alRecord=new ArrayList<CDO>();
			while(true)
			{
				// 读取记录信息
				CDO cdoRecord=readRecord(rs,strsFieldName,nsFieldType,nsPrecision,nsScale);
				if(cdoRecord==null)
				{
					break;
				}
				alRecord.add(cdoRecord);
			}

			cdoArrayField.setValue(alRecord);		
			//统计总数量查询
//			int nCount=executeCount(conn, strSQL, cdoRequest);
//			if(nCount==0)
//				nCount=alRecord.size();
			
			return alRecord.size();
		}catch(SQLException e){
			callOnException("executeQueryRecordSet Exception: "+strSQL,e);
			throw e;
		}finally{
			SQLUtil.closeResultSet(rs);
			SQLUtil.closePreparedStatement(ps);
		}
	}	
	
	/**
	 *  执行数据库插入,更新,删除语句
	 * 
	 * @param conn
	 * @param strSourceSQL 含有{}变量符的原始SQL
	 * @param cdoRequest
	 * @return
	 * @throws Exception
	 */
	public int executeUpdate(Connection conn,String strSourceSQL,CDO cdoRequest) throws SQLException
	{
		// 准备JDBC语句
		PreparedStatement ps=prepareStatement(conn,strSourceSQL,cdoRequest);
		// 输出查询结果
		try{
			return ps.executeUpdate();
		}catch(SQLException e){
			callOnException("executeUpdate Exception: "+strSourceSQL,e);
			throw e;
		}finally{
			SQLUtil.closePreparedStatement(ps);
		}
	}
	
	/**
	 * 执行数据库插入,更新,删除语句
	 * 
	 * @param conn
	 * @param strSourceSQL 含有{}变量符的原始SQL
	 * @param cdoRequest
	 * @return
	 * @throws Exception
	 */
	public void executeSQL(Connection conn,String strSourceSQL,CDO cdoRequest) throws SQLException
	{
		// 准备JDBC语句
		PreparedStatement ps=prepareStatement(conn,strSourceSQL,cdoRequest);
		try{
			// 执行查询
			ps.execute();
		}catch(SQLException e){
			callOnException("executeUpdate Exception: "+strSourceSQL,e);
			throw e;
		}
		finally{
			SQLUtil.closePreparedStatement(ps);
		}
	}	

	
	// 事件定义,所有在本类中定义并调用，由派生类实现或重载的事件类方法(一般为on...ed)在此定义---------------------
	public void onException(String strText,Exception e)
	{
	}

	public void onSQLStatement(String strSQL)
	{
		
	}
	
	public void onExecuteSQL(String strSQL,ArrayList<String> alParaName,CDO cdoRequest){
		
	}
	
	private int executeCount(Connection conn,String strSQL,CDO cdoRequest){
		//TODO SQL语法分析,统计数量
		return 0;
	}

	// 构造函数,所有构造函数在此定义------------------------------------------------------------------------------

	public DataEngine()
	{
		// 请在此加入初始化代码,内部对象和属性对象负责创建或赋初值,引用对象初始化为null，初始化完成后在设置各对象之间的关系
		strDriver="";
		strURI="";
		strCharset="GBK";
		properties=null;

		ds=null;

		strUserName="";
		strPassword="";
		strSystemCharset=System.getProperty("sun.jnu.encoding");
	}
	
}
