package com.cdoframework.cdolib.database;

import java.util.HashMap;
import java.util.Map;

public class DBPoolManager {
	private Map<String,DBPool> hmDBPool=new HashMap<String,DBPool>(6);
	
	public static DBPoolManager instances=new DBPoolManager();
	private DBPoolManager(){
		
	}
	
	public static DBPoolManager getInstances(){
		return instances;
	}

	public Map<String, DBPool> getHmDBPool() {
		return hmDBPool;
	}

	public void setHmDBPool(Map<String, DBPool> hmDBPool) {
		this.hmDBPool = hmDBPool;
	}
	
	
}
