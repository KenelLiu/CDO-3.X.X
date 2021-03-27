package com.cdoframework.cdolib.database;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp2.BasicDataSource;

import com.cdo.util.sql.SQLUtil;
import com.cdoframework.cdolib.base.Return;

public class DBPool extends DBCP2Config{
	protected BasicDataSource ds;   
	
	public boolean isOpened(){
		if(ds==null){
			return false;
		}else{
			return true;
		}
	}	

	public Connection getConnection() throws SQLException{	
		if(!isOpened()){
			return null;
		}
		return ds.getConnection();
	}
	/**
	 * 初始化连接池
	 */
	public synchronized Return open()
	{
		if(ds!=null){
			return Return.OK;
		}
		Connection conn=null;
		try{
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
	        //========= 打开连接  测试=========//
		    conn=ds.getConnection();
			return Return.OK;
		}catch(Exception e){
			Return ret=Return.valueOf(-1,e.getMessage(),"System.Error");
			ret.setThrowable(e);
			ds=null;
			return ret;
		}finally{
			SQLUtil.closeConnection(conn);
		}
	
	}
	
	
}
