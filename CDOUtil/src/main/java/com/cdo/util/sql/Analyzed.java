package com.cdo.util.sql;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.cdo.field.Field;
import com.cdo.field.FieldType;
import com.cdoframework.cdolib.data.cdo.CDO;



public class Analyzed {

	private static Logger logger=Logger.getLogger(Analyzed.class);
	Map<String,AnalyzedSQL> hmAnalyzedSQL=new HashMap<String,AnalyzedSQL>();
	private  boolean bSaveDBLog=false;//将框架里的日志保存数据表里
	//============内部类,简单定义一个结构=========//
	class AnalyzedSQL {
		String strSQL;
		ArrayList<String> alParaName;
	}
	
	final static Analyzed instance=new Analyzed();
	
	private Analyzed(){
		
	}
	
	public static Analyzed getInstance(){
		return instance;
	}
	public void setSaveDBLog(boolean bSaveDBLog){
		this.bSaveDBLog=bSaveDBLog;
	}

	AnalyzedSQL analyzeSourceSQL(String strSourceSQL){
		AnalyzedSQL anaSQL=hmAnalyzedSQL.get(strSourceSQL);		
		if(anaSQL!=null){
			return anaSQL;
		}
	
		ArrayList<String> alParaName=new ArrayList<String>();
		StringBuilder strbSQL=new StringBuilder();

		int nState=0;// 0 : {} 之外的字符, 1: {}之内字符.
		int nLength=strSourceSQL.length();

		StringBuilder strbParaName=new StringBuilder(nLength);
		int i=0;
		while(i<nLength)
		{
			char ch=strSourceSQL.charAt(i);
			switch(ch)
			{
				case '{':
					if(nState==0)
					{// 在{}之外
						if(i+1<nLength&&strSourceSQL.charAt(i+1)=='{')
						{// 为普通字符
							strbSQL.append("{");
							i+=2;
						}
						else
						{// 字段开始
							nState=1;
							i++;
						}
					}
					else
					{// 在{}之内，语法错误
						logger.error("analyzeSQL error",new Exception("SQL syntax Error: "+strSourceSQL));
						return null;
					}
					break;
				case '}':
					if(nState==0)
					{// 在{}之外
						if(i+1<nLength&&strSourceSQL.charAt(i+1)=='}')
						{// 为普通字符
							strbSQL.append("}");
							i++;
						}
						else
						{// 语法错误
							logger.error("analyzeSQL error",new Exception("SQL syntax Error: "+strSourceSQL));
							return null;
						}
					}
					else
					{// 在{}之内，字段结束
						if(strbParaName.length()==0)
						{
							logger.error("analyzeSQL error",new Exception("SQL syntax Error: "+strSourceSQL));
							return null;
						}
						nState=0;
						strbSQL.append("?");
						alParaName.add(strbParaName.toString());
						strbParaName.setLength(0);
					}
					i++;
					break;
				default:
					if(nState==0)
					{// 在{}之外
						strbSQL.append(ch);
					}
					else
					{
						strbParaName.append(ch);
					}
					i++;
					break;
			}
		}

		if(nState==1)
		{
			logger.error("analyzeSQL error",new Exception("SQL syntax Error: "+strSourceSQL));
			return null;
		}

		anaSQL=new AnalyzedSQL();
		anaSQL.strSQL=strbSQL.toString();
		anaSQL.alParaName=alParaName;

		synchronized(hmAnalyzedSQL)
		{
			hmAnalyzedSQL.put(strSourceSQL,anaSQL);
		}

		return anaSQL;
	}
	
	public  void onExecuteSQL(Connection conn,String strSQL,ArrayList<String> alParaName,CDO cdoRequest){
		if (logger.isDebugEnabled() || bSaveDBLog) {
			StringBuilder val = new StringBuilder("{");
			for (int i = 0; i < alParaName.size(); i++) {
				Field object = cdoRequest.getObject(alParaName.get(i));
				Object objValue = object.getObjectValue();
				int nType = object.getFieldType().getType();				
				if(i>0){
					val.append(",");
				}
				val.append(nType==FieldType.BYTE_ARRAY_TYPE?new String((byte[]) objValue):objValue);				
			}
			val.append('}');
			//因为log4j中 debug 与disconf依赖的logback中的debug 冲突导致输出不了数据，这儿改成info,判断使用debug
			if(logger.isDebugEnabled())
				logger.info(strSQL+"\n"+val);
			if(bSaveDBLog){
				saveDBLog(conn, strSQL,val, cdoRequest);
			}
		}
	}
	
	private  void saveDBLog(Connection conn,String strSQL,StringBuilder values,CDO cdoRequest){		
		strSQL=strSQL.trim();
		if(strSQL.length()<=6) return;
		String prefix=strSQL.substring(0,6).toUpperCase();
		switch(prefix){
			case "INSERT":
			case "UPDATE":
			case "DELETE":
				String sql="INSERT INTO T_SYS_SQLLog(strSQL,`values`,createUser) VALUES(?,?,?)";
				java.sql.PreparedStatement ps=null;
				try{
					ps=conn.prepareStatement(sql);
					ps.setString(1, strSQL);
					ps.setString(2, values.toString());
					String modifyUser="";
					if(cdoRequest.exists("modifyUser")){
						modifyUser=cdoRequest.getObjectValue("modifyUser").toString();
					}
					ps.setString(3, modifyUser);
					ps.execute();
				}catch(Exception ex){
					logger.warn("保存SQL操作日志[T_SYS_SQLLog]失败,"+ex.getMessage(),ex);
				}finally{
					if(ps!=null)try{ps.close();}catch(Exception e){};
				}							
				break;
			default:
				break;
		}
	}
	public  void onSQLStatement(String strSQL)
	{
		if(logger.isDebugEnabled()){
			logger.info("SQL:"+strSQL);
		}
	}
}
