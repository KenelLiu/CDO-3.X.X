package com.cdo.util.sql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.cdo.field.Field;
import com.cdo.field.FieldType;
import com.cdoframework.cdolib.data.cdo.CDO;



public class Analyzed {

	private static Logger logger=Logger.getLogger(Analyzed.class);
	static Map<String,AnalyzedSQL> hmAnalyzedSQL=new HashMap<String,AnalyzedSQL>();
	
	static  AnalyzedSQL analyzeSourceSQL(String strSourceSQL)
	{
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
	
	public static void onExecuteSQL(String strSQL,ArrayList<String> alParaName,CDO cdoRequest){
		if (logger.isDebugEnabled()) {
			StringBuilder sb = new StringBuilder("\n{");
			for (int i = 0; i < alParaName.size(); i++) {
				Field object = cdoRequest.getObject(alParaName.get(i));
				Object objValue = object.getObjectValue();
				int nType = object.getFieldType().getType();
				sb.append(nType==FieldType.BYTE_ARRAY_TYPE?new String((byte[]) objValue):objValue);
				sb.append(',');
			}
			sb.append('}');
			//因为log4j中 debug 与disconf依赖的logback中的debug 冲突导致输出不了数据，这儿改成info,判断使用debug
			logger.info(strSQL + sb.toString());
		}
	}
	
	public static void onSQLStatement(String strSQL)
	{
		if(logger.isDebugEnabled()){
			logger.info("SQL:"+strSQL);
		}
	}
}
