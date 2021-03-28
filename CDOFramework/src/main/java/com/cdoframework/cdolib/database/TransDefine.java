package com.cdoframework.cdolib.database;

import com.cdoframework.cdolib.database.xsd.SQLTrans;

/**
 * @author KenelLiu
 */
public class TransDefine{

	private SQLTrans sqlTrans;	
	public SQLTrans getSqlTrans()
	{
		return sqlTrans;
	}

	public void setSqlTrans(SQLTrans sqlTrans)
	{
		this.sqlTrans=sqlTrans;
	}
	public TransDefine(){
		
	}

}
