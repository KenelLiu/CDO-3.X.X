package com.cdoframework.cdolib.database;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import com.cdo.field.BooleanField;
import com.cdo.field.ByteField;
import com.cdo.field.DateField;
import com.cdo.field.DateTimeField;
import com.cdo.field.DoubleField;
import com.cdo.field.Field;
import com.cdo.field.FieldType;
import com.cdo.field.FloatField;
import com.cdo.field.IntegerField;
import com.cdo.field.LongField;
import com.cdo.field.ShortField;
import com.cdo.field.StringField;
import com.cdo.field.TimeField;
import com.cdo.field.array.BooleanArrayField;
import com.cdo.field.array.ByteArrayField;
import com.cdo.field.array.DateArrayField;
import com.cdo.field.array.DateTimeArrayField;
import com.cdo.field.array.DoubleArrayField;
import com.cdo.field.array.FloatArrayField;
import com.cdo.field.array.IntegerArrayField;
import com.cdo.field.array.LongArrayField;
import com.cdo.field.array.ShortArrayField;
import com.cdo.field.array.StringArrayField;
import com.cdo.field.array.TimeArrayField;
import com.cdoframework.cdolib.data.cdo.CDO;
import com.cdoframework.cdolib.data.cdo.CDOArrayField;
import com.cdoframework.cdolib.data.cdo.CDOField;
import com.cdoframework.cdolib.database.xsd.If;
import com.cdoframework.cdolib.database.xsd.SQLIf;
import com.cdoframework.cdolib.database.xsd.SetVar;
import com.cdoframework.cdolib.database.xsd.types.IfOperatorType;
import com.cdoframework.cdolib.database.xsd.types.IfTypeType;
import com.cdoframework.cdolib.database.xsd.types.SQLIfTypeType;

public class DataEngineHelp {
	private static final Logger logger=Logger.getLogger(DataEngineHelp.class);
	
	static void setVar(SetVar sv,CDO cdoRequest){
		String strVarId=sv.getVarId();
		String strValue=sv.getValue();
		
		StringBuilder strbText1=new StringBuilder();
		StringBuilder strbText2=new StringBuilder();
		handleFieldIdText(strVarId,strbText1);
		boolean bIsFieldId2=handleFieldIdText(strValue,strbText2);
		
		String strFieldId=strbText1.toString();
		String strValueId=strbText2.toString();
		if(bIsFieldId2){
			if(!cdoRequest.exists(strValueId)){
				logger.warn("setVal cdoRequest["+strValueId+"]不存在.");
				return ;
			}
			//value 为变量形式数据
			byte type =cdoRequest.getObject(strValueId).getFieldType().getType();
			switch(type){
				case FieldType.BOOLEAN_TYPE:
					cdoRequest.setBooleanValue(strFieldId, cdoRequest.getBooleanValue(strValueId));
					break;
				case FieldType.BYTE_TYPE:
					cdoRequest.setByteValue(strFieldId, cdoRequest.getByteValue(strValueId));
					break;
				case FieldType.CDO_TYPE:
					cdoRequest.setCDOValue(strFieldId, cdoRequest.getCDOValue(strValueId));					
					break;
				case FieldType.DATE_TYPE:
					cdoRequest.setDateValue(strFieldId, cdoRequest.getDateValue(strValueId));
					break;
				case FieldType.DATETIME_TYPE:
					cdoRequest.setDateTimeValue(strFieldId, cdoRequest.getDateTimeValue(strValueId));
					break;
				case FieldType.DOUBLE_TYPE:
					cdoRequest.setDoubleValue(strFieldId, cdoRequest.getDoubleValue(strValueId));
					break;
				case FieldType.FLOAT_TYPE:
					cdoRequest.setFloatValue(strFieldId, cdoRequest.getFloatValue(strValueId));
					break;
				case FieldType.INTEGER_TYPE:
					cdoRequest.setIntegerValue(strFieldId, cdoRequest.getIntegerValue(strValueId));
					break;
				case FieldType.LONG_TYPE:
					cdoRequest.setLongValue(strFieldId, cdoRequest.getLongValue(strValueId));
					break;
				case FieldType.SHORT_TYPE:
					cdoRequest.setShortValue(strFieldId, cdoRequest.getShortValue(strValueId));
					break;
				case FieldType.STRING_TYPE:
					cdoRequest.setStringValue(strFieldId, cdoRequest.getStringValue(strValueId));
					break;
				case FieldType.TIME_TYPE:
					cdoRequest.setTimeValue(strFieldId, cdoRequest.getTimeValue(strValueId));
					break;
					
				case FieldType.BOOLEAN_ARRAY_TYPE:
					cdoRequest.setBooleanArrayValue(strFieldId, cdoRequest.getBooleanArrayValue(strValueId));
					break;
				case FieldType.BYTE_ARRAY_TYPE:
					cdoRequest.setByteArrayValue(strFieldId, cdoRequest.getByteArrayValue(strValueId));
					break;
				case FieldType.CDO_ARRAY_TYPE:
					cdoRequest.setCDOArrayValue(strFieldId, cdoRequest.getCDOArrayValue(strValueId));					
					break;
				case FieldType.DATE_ARRAY_TYPE:
					cdoRequest.setDateArrayValue(strFieldId, cdoRequest.getDateArrayValue(strValueId));
					break;
				case FieldType.DATETIME_ARRAY_TYPE:
					cdoRequest.setDateTimeArrayValue(strFieldId, cdoRequest.getDateTimeArrayValue(strValueId));
					break;
				case FieldType.DOUBLE_ARRAY_TYPE:
					cdoRequest.setDoubleArrayValue(strFieldId, cdoRequest.getDoubleArrayValue(strValueId));
					break;
				case FieldType.FLOAT_ARRAY_TYPE:
					cdoRequest.setFloatArrayValue(strFieldId, cdoRequest.getFloatArrayValue(strValueId));
					break;
				case FieldType.INTEGER_ARRAY_TYPE:
					cdoRequest.setIntegerArrayValue(strFieldId, cdoRequest.getIntegerArrayValue(strValueId));
					break;
				case FieldType.LONG_ARRAY_TYPE:
					cdoRequest.setLongArrayValue(strFieldId, cdoRequest.getLongArrayValue(strValueId));
					break;
				case FieldType.SHORT_ARRAY_TYPE:
					cdoRequest.setShortArrayValue(strFieldId, cdoRequest.getShortArrayValue(strValueId));
					break;
				case FieldType.STRING_ARRAY_TYPE:
					cdoRequest.setStringArrayValue(strFieldId, cdoRequest.getStringArrayValue(strValueId));
					break;
				case FieldType.TIME_ARRAY_TYPE:
					cdoRequest.setTimeArrayValue(strFieldId, cdoRequest.getTimeArrayValue(strValueId));
					break;					
			}
			return;
		}
		//sv.getValue() 为普通文本,strbText2	
		if(sv.getType()==null){
			throw new RuntimeException("setVal,类型为定义, 当value值是普通文本时 ,需要定义类型");
		}
		switch(sv.getType())
		{
			case BYTE:
				cdoRequest.setByteValue(strFieldId,Byte.parseByte(sv.getValue()));
				break;
			case SHORT:
				cdoRequest.setShortValue(strFieldId,Short.parseShort(sv.getValue()));
				break;
			case INTEGER:
				cdoRequest.setIntegerValue(strFieldId,Integer.parseInt(sv.getValue()));
				break;
			case LONG:
				cdoRequest.setLongValue(strFieldId,Long.parseLong(sv.getValue()));
				break;
			case FLOAT:
				cdoRequest.setFloatValue(strFieldId,Float.parseFloat(sv.getValue()));
				break;
			case DOUBLE:
				cdoRequest.setDoubleValue(strFieldId,Double.parseDouble(sv.getValue()));
				break;
			case STRING:
				cdoRequest.setStringValue(strFieldId,sv.getValue());
				break;
			case DATE:
				cdoRequest.setDateValue(strFieldId,sv.getValue());
				break;
			case TIME:
				cdoRequest.setTimeValue(strFieldId,sv.getValue());
				break;
			case DATETIME:
				cdoRequest.setDateTimeValue(strFieldId,sv.getValue());
				break;
			default:
				throw new RuntimeException("Invalid type "+sv.getType().toString());
		}
	}
	
	static boolean checkCondition(SQLIf sqlIf,CDO cdoRequest){
		//sqlIf.getValue1(),sqlIf.getOperator().toString(),sqlIf.getValue2(),sqlIf.getType(),sqlIf.getType().toString(),cdoRequest
		//将SQLIF 转换成IF ,因为处理逻辑相同
		If ifItem=new If();
		ifItem.setValue1(sqlIf.getValue1());
		ifItem.setOperator(IfOperatorType.fromValue(sqlIf.getOperator().value()));
		ifItem.setValue2(sqlIf.getValue2());		
		ifItem.setType(IfTypeType.fromValue(sqlIf.getType().value()));	
		return checkCondition(ifItem, cdoRequest);
	}   
	static boolean checkCondition(If ifItem,CDO cdoRequest){
		//ifItem.getValue1(),ifItem.getOperator().toString(),ifItem.getValue2(),ifItem.getType(),ifItem.getType().toString(),cdoRequest
		String strValue1=ifItem.getValue1();
		String strOperator=ifItem.getOperator().value();
		String strValue2=ifItem.getValue2();
		IfTypeType ifType=ifItem.getType();				
    	// IS
		if(strOperator.equalsIgnoreCase("IS")==true){
			// 解析FieldId
			StringBuilder strbText1=new StringBuilder();
			StringBuilder strbText2=new StringBuilder();
			boolean bIsFieldId1=handleFieldIdText(strValue1,strbText1);
			boolean bIsFieldId2=handleFieldIdText(strValue2,strbText2);

			if(bIsFieldId1&&!bIsFieldId2)
			{// Value1是FieldId
				if(cdoRequest.exists(strbText1.toString()))
				{
					return false;
				}
				else
				{
					return true;
				}
			}
			else if(bIsFieldId2&&!bIsFieldId1)
			{// Value2是FieldId
				if(cdoRequest.exists(strbText2.toString()))
				{
					return false;
				}
				else
				{
					return true;
				}
			}
			
			throw new RuntimeException("Invalid IF condition");
		}
		// ISNOT
		if(strOperator.equalsIgnoreCase("ISNOT")==true)
		{
			// 解析FieldId
			StringBuilder strbText1=new StringBuilder();
			StringBuilder strbText2=new StringBuilder();
			boolean bIsFieldId1=handleFieldIdText(strValue1,strbText1);
			boolean bIsFieldId2=handleFieldIdText(strValue2,strbText2);

			if(bIsFieldId1&&!bIsFieldId2)
			{// Value1是FieldId
				if(cdoRequest.exists(strbText1.toString()))
				{
					return true;
				}
				else
				{// Value2是FieldId
					return false;
				}
			}
			else if(bIsFieldId2&&!bIsFieldId1)
			{
				if(cdoRequest.exists(strbText2.toString()))
				{
					return true;
				}
				else
				{
					return false;
				}
			}

			throw new RuntimeException("Invalid IF condition");
		}

		// =
		if(strOperator.equalsIgnoreCase("=")){
				//如果是boolean 值
			  if(ifType==IfTypeType.BOOLEAN){
				  return checkEqualBoolean(strValue1, strValue2, cdoRequest);
			  }else{
				  return checkEqual(strValue1, strValue2, ifType, cdoRequest);
			  }
			
		}else if(strOperator.equalsIgnoreCase("!=")){
			 //如果是boolean 值
			  if(ifType==IfTypeType.BOOLEAN){
				  return !checkEqualBoolean(strValue1, strValue2, cdoRequest);
			  }else{
				  return !checkEqual(strValue1, strValue2, ifType, cdoRequest);
			  }
		}else if(strOperator.equalsIgnoreCase(">")){
			return checkGreaterThan(strValue1, strValue2, ifType, cdoRequest);
		}else if(strOperator.equalsIgnoreCase("<")){
			return !checkEqual(strValue1, strValue2, ifType, cdoRequest)
					&& !checkGreaterThan(strValue1, strValue2, ifType, cdoRequest);
		}else if(strOperator.equalsIgnoreCase(">=")){
			return checkGreaterThan(strValue1, strValue2, ifType, cdoRequest) ||
					checkEqual(strValue1, strValue2, ifType, cdoRequest);
			
		}else if(strOperator.equalsIgnoreCase("<=")){
			return !checkGreaterThan(strValue1, strValue2, ifType, cdoRequest) 
					|| checkEqual(strValue1, strValue2, ifType, cdoRequest);
			
		}else if(strOperator.equalsIgnoreCase("MATCH")){
			switch(ifType)
			{
				case STRING:
				{
					String value1=getTimeValue(strValue1,cdoRequest);
					String value2=getTimeValue(strValue2,cdoRequest);
					return value1.matches(value2);
				}
				default:
				{
					throw new RuntimeException("Invalid type "+ifType.value());
				}
			}
	  }else if(strOperator.equalsIgnoreCase("NOTMATCH")){
			switch(ifType)
			{
				case STRING:
				{
					String value1=getTimeValue(strValue1,cdoRequest);
					String value2=getTimeValue(strValue2,cdoRequest);
					return !value1.matches(value2);
				}
				default:
				{
					throw new RuntimeException("Invalid type "+ifType.value());
				}
			}
		}
		else
		{
			throw new RuntimeException("Invalid operator "+strOperator);
		}
    }
	
    private static boolean checkEqualBoolean(String strValue1,String strValue2,CDO cdoRequest){
		boolean value1=getBooleanValue(strValue1,cdoRequest);
		boolean value2=getBooleanValue(strValue2,cdoRequest);
		return value1==value2;
    }
    /**
     * 判断是否相等	
     * @param strValue1
     * @param strValue2
     * @param ifType
     * @param cdoRequest
     * @return
     */
    private static boolean checkEqual(String strValue1,String strValue2,IfTypeType ifType,CDO cdoRequest){
    	switch(ifType)
		{
			case INTEGER:
			{
				int value1=getIntegerValue(strValue1,cdoRequest);
				int value2=getIntegerValue(strValue2,cdoRequest);
				return value1==value2;
			}
			case STRING:
			{
				String value1=getStringValue(strValue1,cdoRequest);
				String value2=getStringValue(strValue2,cdoRequest);
				return value1.equals(value2);
			}
			case LONG:
			{
				long value1=getLongValue(strValue1,cdoRequest);
				long value2=getLongValue(strValue2,cdoRequest);
				return value1==value2;
			}
			case BYTE:
			{
				byte value1=getByteValue(strValue1,cdoRequest);
				byte value2=getByteValue(strValue2,cdoRequest);
				return value1==value2;
			}
			case SHORT:
			{
				short value1=getShortValue(strValue1,cdoRequest);
				short value2=getShortValue(strValue2,cdoRequest);
				return value1==value2;
			}
			case DATE:
			{
				String value1=getDateValue(strValue1,cdoRequest);
				String value2=getDateValue(strValue2,cdoRequest);
				return value1.equals(value2);
			}
			case TIME:
			{
				String value1=getTimeValue(strValue1,cdoRequest);
				String value2=getTimeValue(strValue2,cdoRequest);
				return value1.equals(value2);
			}
			case DATETIME:
			{
				String value1=getDateTimeValue(strValue1,cdoRequest);
				String value2=getDateTimeValue(strValue2,cdoRequest);
				return value1.equals(value2);
			}
			case FLOAT:
			{
				float value1=getFloatValue(strValue1,cdoRequest);
				float value2=getFloatValue(strValue2,cdoRequest);
				return value1==value2;
			}
			case DOUBLE:
			{
				double value1=getDoubleValue(strValue1,cdoRequest);
				double value2=getDoubleValue(strValue2,cdoRequest);
				return value1==value2;
			}
			default:
			{
				throw new RuntimeException("Invalid type "+ifType.value());
			}
		}
    }
   
    /**
     * 判断是否 大于
     * @param strValue1
     * @param strValue2
     * @param ifType
     * @param cdoRequest
     * @return
     */
    private static boolean checkGreaterThan(String strValue1,String strValue2,IfTypeType ifType,CDO cdoRequest){
		switch(ifType)
		{
			case INTEGER:
			{
				int value1=getIntegerValue(strValue1,cdoRequest);
				int value2=getIntegerValue(strValue2,cdoRequest);
				return value1>value2;
			}
			case STRING:
			{
				String value1=getStringValue(strValue1,cdoRequest);
				String value2=getStringValue(strValue2,cdoRequest);
				return value1.compareTo(value2)>0;
			}
			case LONG:
			{
				long value1=getLongValue(strValue1,cdoRequest);
				long value2=getLongValue(strValue2,cdoRequest);
				return value1>value2;
			}
			case BYTE:
			{
				byte value1=getByteValue(strValue1,cdoRequest);
				byte value2=getByteValue(strValue2,cdoRequest);
				return value1>value2;
			}
			case SHORT:
			{
				short value1=getShortValue(strValue1,cdoRequest);
				short value2=getShortValue(strValue2,cdoRequest);
				return value1>value2;
			}
			case DATE:
			{
				String value1=getDateValue(strValue1,cdoRequest);
				String value2=getDateValue(strValue2,cdoRequest);
				return value1.compareTo(value2)>0;
			}
			case TIME:
			{
				String value1=getTimeValue(strValue1,cdoRequest);
				String value2=getTimeValue(strValue2,cdoRequest);
				return value1.compareTo(value2)>0;
			}
			case DATETIME:
			{
				String value1=getDateTimeValue(strValue1,cdoRequest);
				String value2=getDateTimeValue(strValue2,cdoRequest);
				return value1.compareTo(value2)>0;
			}
			case FLOAT:
			{
				float value1=getFloatValue(strValue1,cdoRequest);
				float value2=getFloatValue(strValue2,cdoRequest);
				return value1>value2;
			}
			case DOUBLE:
			{
				double value1=getDoubleValue(strValue1,cdoRequest);
				double value2=getDoubleValue(strValue2,cdoRequest);
				return value1>value2;
			}
			default:
			{
				throw new RuntimeException("Invalid type "+ifType.value());
			}
		}
    }
    
    
 // 去掉{和}，并得到是否为FieldId
 	static boolean handleFieldIdText(String strFieldIdText,StringBuilder strbOutput)
 	{
 		if(strFieldIdText==null||strFieldIdText.length()==0)
 		{
 			return false;
 		}
 		strbOutput.setLength(0);

 		char chFirst=strFieldIdText.charAt(0);
 		int nIndex=0;
 		int nLength=strFieldIdText.length();
 		while(true)
 		{
 			if(nIndex>=nLength)
 			{
 				break;
 			}

 			char ch=strFieldIdText.charAt(nIndex);
 			if(ch=='{'||ch=='}')
 			{
 				if(nIndex==nLength-1)
 				{
 					break;
 				}
 				if(strFieldIdText.charAt(nIndex+1)==ch)
 				{
 					strbOutput.append(ch);
 				}
 			}
 			else
 			{
 				strbOutput.append(ch);
 			}
 			nIndex++;
 		}

 		if(chFirst=='{'&&strbOutput.charAt(0)!='{')
 		{// 为FieldId
 			return true;
 		}
 		else
 		{// 为一般文本
 			return false;
 		}
 	}    
 	/**
	 * 根据FieldIdText得到值
	 * 
	 * @param strFieldIdText
	 * @param cdoRequest
	 * @return
	 * @throws Exception
	 */
	static byte getByteValue(String strFieldIdText,CDO cdoRequest)
	{
		// 解析strFieldIdText,判断出是否为FieldId
		StringBuilder strbFieldIdText=new StringBuilder();
		boolean bIsFieldId=handleFieldIdText(strFieldIdText,strbFieldIdText);

		if(bIsFieldId==false)
		{
			return Byte.parseByte(strbFieldIdText.toString());
		}
		else
		{
			return cdoRequest.getByteValue(strbFieldIdText.toString());
		}
	}

	/**
	 * 根据FieldIdText得到值
	 * 
	 * @param strFieldIdText
	 * @param cdoRequest
	 * @return
	 * @throws Exception
	 */
	static short getShortValue(String strFieldIdText,CDO cdoRequest)
	{
		// 解析strFieldIdText,判断出是否为FieldId
		StringBuilder strbFieldIdText=new StringBuilder();
		boolean bIsFieldId=handleFieldIdText(strFieldIdText,strbFieldIdText);

		if(bIsFieldId==false)
		{
			return Short.parseShort(strbFieldIdText.toString());
		}
		else
		{
			return cdoRequest.getShortValue(strbFieldIdText.toString());
		}
	}

	/**
	 * 根据FieldIdText得到值
	 * 
	 * @param strFieldIdText
	 * @param cdoRequest
	 * @return
	 * @throws Exception
	 */
	static int getIntegerValue(String strFieldIdText,CDO cdoRequest)
	{
		// 解析strFieldIdText,判断出是否为FieldId
		StringBuilder strbFieldIdText=new StringBuilder();
		boolean bIsFieldId=handleFieldIdText(strFieldIdText,strbFieldIdText);

		if(bIsFieldId==false)
		{
			return Integer.parseInt(strbFieldIdText.toString());
		}
		else
		{
			return cdoRequest.getIntegerValue(strbFieldIdText.toString());
		}
	}

	/**
	 * 根据FieldIdText得到值
	 * 
	 * @param strFieldIdText
	 * @param cdoRequest
	 * @return
	 * @throws Exception
	 */
	static float getFloatValue(String strFieldIdText,CDO cdoRequest)
	{
		// 解析strFieldIdText,判断出是否为FieldId
		StringBuilder strbFieldIdText=new StringBuilder();
		boolean bIsFieldId=handleFieldIdText(strFieldIdText,strbFieldIdText);

		if(bIsFieldId==false)
		{
			return Float.parseFloat(strbFieldIdText.toString());
		}
		else
		{
			return cdoRequest.getFloatValue(strbFieldIdText.toString());
		}
	}

	/**
	 * 根据FieldIdText得到值
	 * 
	 * @param strFieldIdText
	 * @param cdoRequest
	 * @return
	 * @throws Exception
	 */
	static double getDoubleValue(String strFieldIdText,CDO cdoRequest)
	{
		// 解析strFieldIdText,判断出是否为FieldId
		StringBuilder strbFieldIdText=new StringBuilder();
		boolean bIsFieldId=handleFieldIdText(strFieldIdText,strbFieldIdText);

		if(bIsFieldId==false)
		{
			return Double.parseDouble(strbFieldIdText.toString());
		}
		else
		{
			return cdoRequest.getDoubleValue(strbFieldIdText.toString());
		}
	}
	
	/**
	 * 根据FieldIdText得到值
	 * 
	 * @param strFieldIdText
	 * @param cdoRequest
	 * @return
	 * @throws Exception
	 */
	static boolean getBooleanValue(String strFieldIdText,CDO cdoRequest)
	{
		// 解析strFieldIdText,判断出是否为FieldId
		StringBuilder strbFieldIdText=new StringBuilder();
		boolean bIsFieldId=handleFieldIdText(strFieldIdText,strbFieldIdText);

		if(bIsFieldId==false)
		{
			return Boolean.parseBoolean(strbFieldIdText.toString());
		}
		else
		{
			return cdoRequest.getBooleanValue(strbFieldIdText.toString());
		}
	}

	/**
	 * 根据FieldIdText得到值
	 * 
	 * @param strFieldIdText
	 * @param cdoRequest
	 * @return
	 * @throws Exception
	 */
	static long getLongValue(String strFieldIdText,CDO cdoRequest)
	{
		// 解析strFieldIdText,判断出是否为FieldId
		StringBuilder strbFieldIdText=new StringBuilder();
		boolean bIsFieldId=handleFieldIdText(strFieldIdText,strbFieldIdText);

		if(bIsFieldId==false)
		{
			return Long.parseLong(strbFieldIdText.toString());
		}
		else
		{
			return cdoRequest.getLongValue(strbFieldIdText.toString());
		}
	}

	/**
	 * 根据FieldIdText得到值
	 * 
	 * @param strFieldIdText
	 * @param cdoRequest
	 * @return
	 * @throws Exception
	 */
	static String getStringValue(String strFieldIdText,CDO cdoRequest)
	{
		// 解析strFieldIdText,判断出是否为FieldId
		StringBuilder strbFieldIdText=new StringBuilder();
		boolean bIsFieldId=handleFieldIdText(strFieldIdText,strbFieldIdText);

		if(bIsFieldId==false)
		{
			return strbFieldIdText.toString();
		}
		else
		{
			return cdoRequest.getStringValue(strbFieldIdText.toString());
		}
	}

	/**
	 * 根据FieldIdText得到值
	 * 
	 * @param strFieldIdText
	 * @param cdoRequest
	 * @return
	 * @throws Exception
	 */
	static String getDateValue(String strFieldIdText,CDO cdoRequest)
	{
		// 解析strFieldIdText,判断出是否为FieldId
		StringBuilder strbFieldIdText=new StringBuilder();
		boolean bIsFieldId=handleFieldIdText(strFieldIdText,strbFieldIdText);

		if(bIsFieldId==false)
		{
			return strbFieldIdText.toString();
		}
		else
		{
			return cdoRequest.getDateValue(strbFieldIdText.toString());
		}
	}

	/**
	 * 根据FieldIdText得到值
	 * 
	 * @param strFieldIdText
	 * @param cdoRequest
	 * @return
	 * @throws Exception
	 */
	static String getTimeValue(String strFieldIdText,CDO cdoRequest)
	{
		// 解析strFieldIdText,判断出是否为FieldId
		StringBuilder strbFieldIdText=new StringBuilder();
		boolean bIsFieldId=handleFieldIdText(strFieldIdText,strbFieldIdText);

		if(bIsFieldId==false)
		{
			return strbFieldIdText.toString();
		}
		else
		{
			return cdoRequest.getTimeValue(strbFieldIdText.toString());
		}
	}

	/**
	 * 根据FieldIdText得到值
	 * 
	 * @param strFieldIdText
	 * @param cdoRequest
	 * @return
	 * @throws Exception
	 */
	static String getDateTimeValue(String strFieldIdText,CDO cdoRequest)
	{
		// 解析strFieldIdText,判断出是否为FieldId
		StringBuilder strbFieldIdText=new StringBuilder();
		boolean bIsFieldId=handleFieldIdText(strFieldIdText,strbFieldIdText);

		if(bIsFieldId==false)
		{
			return strbFieldIdText.toString();
		}
		else
		{
			return cdoRequest.getDateTimeValue(strbFieldIdText.toString());
		}
	}
    /**
     * 解释数组长度使用
     * @param strFieldIdText
     * @param cdoRequest
     * @return
     */
	static int getArrayLength(String strFieldIdText,CDO cdoRequest){
		// 解析strFieldIdText,判断出是否为FieldId
		StringBuilder strbFieldIdText=new StringBuilder();
		handleFieldIdText(strFieldIdText,strbFieldIdText);
		String arrKey= strbFieldIdText.toString();
		if(!cdoRequest.exists(arrKey))
			return 0;
		 Field field=cdoRequest.getObject(arrKey);
		switch(field.getFieldType().getType()){
			case FieldType.BOOLEAN_ARRAY_TYPE:
			{
				return ((BooleanArrayField)field).getLength();
			}
			case FieldType.BYTE_ARRAY_TYPE:
			{
				return ((ByteArrayField)field).getLength();
			}
			case FieldType.SHORT_ARRAY_TYPE:
			{
				return ((ShortArrayField)field).getLength();
			}
			case FieldType.INTEGER_ARRAY_TYPE:
			{
				return ((IntegerArrayField)field).getLength();
			}
			case FieldType.LONG_ARRAY_TYPE:
			{
				return  ((LongArrayField)field).getLength();
			}
			case FieldType.FLOAT_ARRAY_TYPE:
			{
				return ((FloatArrayField)field).getLength();
			}
			case FieldType.DOUBLE_ARRAY_TYPE:
			{
				return ((DoubleArrayField)field).getLength();
			}
			case FieldType.STRING_ARRAY_TYPE:
			{
				return ((StringArrayField)field).getLength();
			}
			case FieldType.DATE_ARRAY_TYPE:
			{
				return ((DateArrayField)field).getLength();
			}
			case FieldType.TIME_ARRAY_TYPE:
			{
				return ((TimeArrayField)field).getLength();
			}
			case FieldType.DATETIME_ARRAY_TYPE:
			{
				return ((DateTimeArrayField)field).getLength();
			}
			case FieldType.CDO_ARRAY_TYPE:
			{
				return ((CDOArrayField)field).getLength();
			}
		 }
		logger.warn(field.getFieldType().getName()+" Type is not array");	
		return 0;
	}
	
	@SuppressWarnings("unchecked")
	static void setFieldValue(CDO cdoData,FieldType.type fieldType,String strOutputId,Object objValue) throws SQLException{
		switch(fieldType.getType())
		{
			case FieldType.BYTE_TYPE:
			{
				cdoData.setByteValue(strOutputId,((Byte)objValue).byteValue());
				break;
			}
			case FieldType.SHORT_TYPE:
			{
				cdoData.setShortValue(strOutputId,((Short)objValue).shortValue());
				break;
			}
			case FieldType.INTEGER_TYPE:
			{
				cdoData.setIntegerValue(strOutputId,((Integer)objValue).intValue());
				break;
			}
			case FieldType.LONG_TYPE:
			{
				cdoData.setLongValue(strOutputId,((Long)objValue).longValue());
				break;
			}
			case FieldType.FLOAT_TYPE:
			{
				cdoData.setFloatValue(strOutputId,((Float)objValue).floatValue());
				break;
			}
			case FieldType.DOUBLE_TYPE:
			{
				cdoData.setDoubleValue(strOutputId,((Double)objValue).doubleValue());
				break;
			}
			case FieldType.STRING_TYPE:
			{
				cdoData.setStringValue(strOutputId,((String)objValue));
				break;
			}
			case FieldType.DATE_TYPE:
			{
				cdoData.setDateValue(strOutputId,((String)objValue));
				break;
			}
			case FieldType.TIME_TYPE:
			{
				cdoData.setTimeValue(strOutputId,((String)objValue));
				break;
			}
			case FieldType.DATETIME_TYPE:
			{
				cdoData.setDateTimeValue(strOutputId,((String)objValue));
				break;
			}
			case FieldType.BYTE_ARRAY_TYPE:
			{
				cdoData.setByteArrayValue(strOutputId,((byte[])objValue));
				break;
			}
			case FieldType.CDO_TYPE:
			{
				cdoData.setCDOValue(strOutputId,(CDO)objValue);
				break;
			}
			case FieldType.CDO_ARRAY_TYPE:
			{
				cdoData.setCDOListValue(strOutputId,(List<CDO>)objValue);
				break;
			}
			default:
			{
				throw new SQLException("Unsupported fieldType: "+fieldType.getName());
			}
		}
	}
}

