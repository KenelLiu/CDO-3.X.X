package com.cdoframework.transaction;

import java.sql.Connection;

public class ConnectionHolder {

	private Connection curConnction;
	private int referenceCount = 0; //连接被引用计数器
	
	public ConnectionHolder(Connection conn){
		this.curConnction=conn;
		this.referenceCount=1;
	}

	public Connection getCurConnction() {
		return curConnction;
	}

	public int getReferenceCount() {
		return referenceCount;
	}
	public void decReference(){
		this.referenceCount--;
	}
	public void addReference(){
		this.referenceCount++;
	}
}
