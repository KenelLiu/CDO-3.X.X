package com.cdoframework.cdolib.database;

import java.util.Properties;
/**
 * 
 * @author Kenel
 *
 */
public class DBCP2Config {
	protected String strSystemCharset;

	// 属性对象,所有在本类中创建，并允许外部访问的对象在此声明并提供get/set方法-----------------------------------
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
	public String getSystemCharset(){
		return this.getSystemCharset();
	}
	
 
	public DBCP2Config(){
		strDriver="";
		strURI="";
		strCharset="GBK";
		properties=null;

		strUserName="";
		strPassword="";
		strSystemCharset=System.getProperty("sun.jnu.encoding");
	}
}
