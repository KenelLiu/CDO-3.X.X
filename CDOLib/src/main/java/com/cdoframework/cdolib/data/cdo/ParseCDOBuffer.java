package com.cdoframework.cdolib.data.cdo;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import com.cdo.field.FieldType;

public class ParseCDOBuffer extends CDOBuffer {
	
	
	/**
	 *  根据fieldName 中的[.]符号 依次递归调用
	 * @param cdo
	 * @param buffer
	 * @param fieldName
	 */
	protected void parseHierarchicCDO(CDO cdo,ByteBuffer buffer,String fieldName){
		 int index=fieldName.indexOf(".");//.表示层级概念			 		 
		 if(index==-1){
			  //到达最底层,设置数据
			  setCDOValue(cdo, fieldName, buffer);				
		 }else{
			 String preKey=fieldName.substring(0,index);
			 String sufKey=fieldName.substring(index+1);				 
			 int arrIndex=preKey.lastIndexOf("[");
			 if(arrIndex==-1){//普通CDO
				    CDO tmpCDO=null;
				    String cdoKey=preKey;
				    if(!cdo.exists(cdoKey)){
				    	tmpCDO=new CDO();
				    	cdo.setCDOValue(cdoKey, tmpCDO);
				    }else{
				    	tmpCDO=cdo.getCDOValue(cdoKey);
				    }
				    parseHierarchicCDO(tmpCDO,buffer,sufKey);
			 }else{//CDO 数组
				String cdoKey=preKey.substring(0,arrIndex);
				//数组下标
				int cdoIndex=Integer.parseInt(preKey.substring(arrIndex+1, preKey.length()-1));
				
				CDO tmpCDO=null;
			    if(!cdo.exists(cdoKey)){
			    	List<CDO> list=new ArrayList<CDO>();
			    	if(cdoIndex>-1){
			    		//arrIndex=-1  表示 是一个空CDO数组,不需要设置值
			    		tmpCDO=new CDO();			    	
			    		list.add(tmpCDO);
			    	}
			    	cdo.setCDOListValue(cdoKey, list);
			    }else{
			    	List<CDO> list=cdo.getCDOListValue(cdoKey);
			    	if(cdoIndex>(list.size()-1)){
			    		tmpCDO=new CDO();
			    		list.add(tmpCDO);
			    	}else{
			    		tmpCDO=list.get(cdoIndex);
			    	}
			    }
			    if(cdoIndex>-1)//arrIndex=-1  表示 是一个空CDO数组,不需要设置值
			    	parseHierarchicCDO(tmpCDO, buffer,sufKey);				
		 }   
	   }
	}
	
	/**
	 * 
	 * @param cdo
	 * @param key
	 * @param buffer
	 */
	@SuppressWarnings("unchecked")
	protected  void setCDOValue(CDO cdo,String key,ByteBuffer buffer){	
		 int dataType=buffer.get();	
		 buffer.clear();
		 if(dataType<FieldType.BOOLEAN_ARRAY_TYPE){
			 //普通类型
			 setCDOValue(cdo, key, dataType, buffer);
		 }else{
			 //数组类型 
			 setCDOValueArr(cdo, key, dataType, buffer);
		 }
		
	}
	private  void setCDOValue(CDO cdo,String key,int dataType,ByteBuffer buffer){
		switch (dataType) {		
			case FieldType.BOOLEAN_TYPE:
				  setBooleanValue(cdo, key, buffer);
				break;
			case FieldType.BYTE_TYPE:
				buffer.position(1);
				byte b=buffer.get();	
				buffer.clear();
				cdo.setByteValue(key, b);
				break;
			case FieldType.SHORT_TYPE:			
				 setShortValue(cdo, key, buffer);
				break;
			case FieldType.INTEGER_TYPE:
				setIntegerValue(cdo, key, buffer);
				break;
			case FieldType.LONG_TYPE:
				setLongValue(cdo, key, buffer);
				break;
			case FieldType.FLOAT_TYPE:
				setFloatValue(cdo, key, buffer);
				break;
			case FieldType.DOUBLE_TYPE:
				setDoubleValue(cdo, key, buffer);
				break;
			case FieldType.STRING_TYPE:
				setStringValue(cdo, key, buffer);
				break;
			case FieldType.DATE_TYPE:
				setDateValue(cdo, key, buffer);
				break;
			case FieldType.TIME_TYPE:
				setTimeValue(cdo, key, buffer);
				break;	
			case FieldType.DATETIME_TYPE:
				setDateTimeValue(cdo, key, buffer);
				break;	
			case FieldType.FILE_TYPE:
				setFileValue(cdo, key, buffer);
				break;					
			default:
				throw new java.lang.RuntimeException("unsupport object type! key="+key+",type="+dataType);
		}
		
	}
	
	
	private  void setCDOValueArr(CDO cdo,String key,int dataType,ByteBuffer buffer){
		switch (dataType) {		
			case FieldType.BOOLEAN_ARRAY_TYPE:
				setBooleanArrayValue(cdo, key, buffer);
				break;
			case FieldType.BYTE_ARRAY_TYPE:
				setByteArrayValue(cdo, key, buffer);
				break;
			case FieldType.SHORT_ARRAY_TYPE:
				setShortArrayValue(cdo, key, buffer);
				break;
			case FieldType.INTEGER_ARRAY_TYPE:
				setIntegerArrayValue(cdo, key, buffer);
				break;
			case FieldType.LONG_ARRAY_TYPE:
				setLongArrayValue(cdo, key, buffer);
				break;
			case FieldType.FLOAT_ARRAY_TYPE:
				setFloatArrayValue(cdo, key, buffer);
				break;
			case FieldType.DOUBLE_ARRAY_TYPE:
				setDoubleArrayValue(cdo, key, buffer);
				break;
			case FieldType.STRING_ARRAY_TYPE:
				setStringArrayValue(cdo, key, buffer);
				break;
			case FieldType.DATE_ARRAY_TYPE:
				setDateArrayValue(cdo, key, buffer);
				break;
			case FieldType.TIME_ARRAY_TYPE:
				setTimeArrayValue(cdo, key, buffer);
				break;	
			case FieldType.DATETIME_ARRAY_TYPE:
				setDateTimeArrayValue(cdo, key, buffer);
				break;	
			default:
				throw new java.lang.RuntimeException("unsupport object type! key="+key+",type="+dataType);
		}		
	}
}
