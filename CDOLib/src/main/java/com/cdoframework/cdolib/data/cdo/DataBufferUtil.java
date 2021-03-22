package com.cdoframework.cdolib.data.cdo;

import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.cdo.field.BooleanField;
import com.cdo.field.ByteField;
import com.cdo.field.DateField;
import com.cdo.field.DateTimeField;
import com.cdo.field.DoubleField;
import com.cdo.field.Field;
import com.cdo.field.FieldType;
import com.cdo.field.FileField;
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
/**
 * 
 * @author KenelLiu
 *
 */
public class DataBufferUtil {

	
	private static ByteBuffer allocateBuffer(int arrlen,int dataType,ByteBuffer buffer,int dataIndex,int databuffer){
		int len=dataIndex+arrlen*databuffer;
		buffer=ByteBuffer.allocate(len);
		buffer.put((byte)dataType);
		return buffer;
	}
	/**
	 * 分配 数据占固定长度 的数组   内存
	 * @param strsValue 数组
	 * @param dataType 字段类型
	 * @param buffer  
	 * @param dataIndex  数据保存的起始位置
	 * @param databuffer 每个数据内容所占字节
	 */
	public static ByteBuffer allocate(int arrlen,int dataType,ByteBuffer buffer,int dataIndex,int databuffer){
		if(buffer==null){
			buffer=allocateBuffer(arrlen, dataType, buffer, dataIndex, databuffer);
		}else{
			buffer.position(1);
			int len=(buffer.capacity()-1)/databuffer;
			if(len<arrlen){
				//最新数组大于原来数组, 重新分配长度
				buffer=allocateBuffer(arrlen, dataType, buffer, dataIndex, databuffer);
			}else if(len>arrlen){
				//最新数组小于原来数组  截取原长度的一部分 作为本次数据存放,不需要重新分配内存
				len=dataIndex+arrlen*databuffer;
				buffer.position(0);
				buffer.limit(len);
				buffer=buffer.slice();
			}
			//若相等，数据进行覆盖即可;
		}
		return buffer;
	}
	/**
	 * 
	 * @param buffer
	 * @param dataIndex  数据保存的起始位置
	 * @param databuffer 每个数据内容所占字节
	 * @return
	 */
	static String[] getDateArrayValue(ByteBuffer buffer,int dataIndex,int databuffer)
	{
		buffer.position(1);
		int len=(buffer.capacity()-dataIndex)/databuffer;
		String[] strsValue=new String[len];
		byte[] bsValue=new byte[databuffer];
		int index;
		for(int i=0;i<strsValue.length;i++){			
			index=dataIndex+i*databuffer;
			buffer.position(index);
			buffer.limit(index+databuffer);
			(buffer.slice()).get(bsValue);
			strsValue[i]=new String(bsValue).trim();
		}
		buffer.clear();
		return strsValue;		
	}
	/**
	 * 
	 * @param nIndex
	 * @param buffer
	 * @param dataIndex 数据保存的起始位置
	 * @param databuffer 每个数据内容所占字节
	 * @return
	 */
	static String getDateArrayValueAt(int nIndex,ByteBuffer buffer,int dataIndex,int databuffer)
	{	
		
		byte[] bsValue=new byte[databuffer];
		int pos=dataIndex+databuffer*nIndex;
		buffer.position(pos);
		buffer.limit(pos+databuffer);
		(buffer.slice()).get(bsValue);
		buffer.clear();
		return new String(bsValue).trim();
	}
	
	static byte getByte(Field field)
	{
		Object objValue=field;
		switch(field.getFieldType().getType())
		{		
			case FieldType.BYTE_TYPE:
			{
				return ((ByteField)field).getValue();
			}
			case FieldType.SHORT_TYPE:
			{
				return ((Short)(objValue)).byteValue();
			}
			case FieldType.INTEGER_TYPE:
			{
				return ((Integer)(objValue)).byteValue();
			}
			case FieldType.LONG_TYPE:
			{
				return ((Long)(objValue)).byteValue();
			}
			case FieldType.FLOAT_TYPE:
			{
				return ((Float)(objValue)).byteValue();
			}
			case FieldType.DOUBLE_TYPE:
			{
				return ((Double)(objValue)).byteValue();
			}
			case FieldType.STRING_TYPE:		
			{
				return Byte.parseByte(objValue+"");
			}
		}		
		throw new RuntimeException(field.getFieldType().getName()+" cannot be cast to byte,field name="+field.getName());
	}
	
	static short getShort(Field field)
	{
		Object objValue=field.getObjectValue();
		switch(field.getFieldType().getType())
		{
			case FieldType.BYTE_TYPE:
			{
				return ((Byte)objValue).byteValue();
			}
			case FieldType.SHORT_TYPE:
			{
				return ((Short)objValue).shortValue();
			}
			case FieldType.INTEGER_TYPE:
			{
				return ((Integer)objValue).shortValue();
			}
			case FieldType.LONG_TYPE:
			{
				return ((Long)objValue).shortValue();
			}
			case FieldType.FLOAT_TYPE:
			{
				return ((Float)objValue).shortValue();
			}
			case FieldType.DOUBLE_TYPE:
			{
				return ((Double)objValue).shortValue();
			}
			case FieldType.STRING_TYPE:
			{
				return Short.parseShort((String)objValue);
			}
		}
		
		throw new RuntimeException(field.getFieldType().getName()+" cannot be cast to short,field name="+field.getName());
	}

	static int getInteger(Field field)
	{
		Object objValue=field.getObjectValue();
		switch(field.getFieldType().getType())
		{
			case FieldType.BYTE_TYPE:
			{
				return ((Byte)objValue).byteValue();
			}
			case FieldType.SHORT_TYPE:
			{
				return ((Short)objValue).shortValue();
			}
			case FieldType.INTEGER_TYPE:
			{
				return ((Integer)objValue).intValue();
			}
			case FieldType.LONG_TYPE:
			{
				return ((Long)objValue).intValue();
			}
			case FieldType.FLOAT_TYPE:
			{
				return ((Float)objValue).intValue();
			}
			case FieldType.DOUBLE_TYPE:
			{
				return ((Double)objValue).intValue();
			}
			case FieldType.STRING_TYPE:
			{
				return Integer.parseInt((String)objValue);
			}
		}
		
		throw new RuntimeException(field.getFieldType().getName()+" cannot be cast to integer,field name="+field.getName());
	}

	static  long getLong(Field field)
	{
		Object objValue=field.getObjectValue();
		switch(field.getFieldType().getType())
		{
			case FieldType.BYTE_TYPE:
			{
				return ((Byte)objValue).byteValue();
			}
			case FieldType.SHORT_TYPE:
			{
				return ((Short)objValue).shortValue();
			}
			case FieldType.INTEGER_TYPE:
			{
				return ((Integer)objValue).intValue();
			}
			case FieldType.LONG_TYPE:
			{
				return ((Long)objValue).longValue();
			}
			case FieldType.FLOAT_TYPE:
			{
				return ((Float)objValue).longValue();
			}
			case FieldType.DOUBLE_TYPE:
			{
				return ((Double)objValue).longValue();
			}
			case FieldType.STRING_TYPE:
			{
				return Long.parseLong((String)objValue);
			}
		}
		
		throw new RuntimeException(field.getFieldType().getName()+" cannot be cast to long,field name="+field.getName());
	}

	static float getFloat(Field field)
	{
		Object objValue=field.getObjectValue();
		switch(field.getFieldType().getType())
		{
			case FieldType.BYTE_TYPE:
			{
				return ((Byte)objValue).byteValue();
			}
			case FieldType.SHORT_TYPE:
			{
				return ((Short)objValue).shortValue();
			}
			case FieldType.INTEGER_TYPE:
			{
				return ((Integer)objValue).floatValue();
			}
			case FieldType.LONG_TYPE:
			{
				return ((Long)objValue).floatValue();
			}
			case FieldType.FLOAT_TYPE:
			{
				return ((Float)objValue).floatValue();
			}
			case FieldType.DOUBLE_TYPE:
			{
				return ((Double)objValue).floatValue();
			}
			case FieldType.STRING_TYPE:
			{
				return Float.parseFloat((String)objValue);
			}
		}
		
		throw new RuntimeException(field.getFieldType().getName()+" cannot be cast to float,field name="+field.getName());
	}

	static double getDouble(Field field)
	{
		Object objValue=field.getObjectValue();
		switch(field.getFieldType().getType())
		{
			case FieldType.BYTE_TYPE:
			{
				return ((Byte)objValue).byteValue();
			}
			case FieldType.SHORT_TYPE:
			{
				return ((Short)objValue).shortValue();
			}
			case FieldType.INTEGER_TYPE:
			{
				return ((Integer)objValue).intValue();
			}
			case FieldType.LONG_TYPE:
			{
				return ((Long)objValue).longValue();
			}
			case FieldType.FLOAT_TYPE:
			{
				return ((Float)objValue).floatValue();
			}
			case FieldType.DOUBLE_TYPE:
			{
				return ((Double)objValue).doubleValue();
			}
			case FieldType.STRING_TYPE:
			{
				return Double.parseDouble((String)objValue);
			}
		}
		
		throw new RuntimeException(field.getFieldType().getName()+" cannot be cast to double,field name="+field.getName());
	}

	static String getString(Field field)
	{
		Object objValue=field.getObjectValue();
		if(objValue == null) {
			return null;
		}
		switch(field.getFieldType().getType())
		{
			case FieldType.BYTE_TYPE:
			case FieldType.SHORT_TYPE:
			case FieldType.INTEGER_TYPE:
			case FieldType.LONG_TYPE:
			case FieldType.FLOAT_TYPE:
			case FieldType.DOUBLE_TYPE:
			case FieldType.STRING_TYPE:
			case FieldType.DATE_TYPE:
			case FieldType.TIME_TYPE:
			case FieldType.DATETIME_TYPE:
			{
				return objValue.toString();
			}
		}
		
		throw new RuntimeException(field.getFieldType().getName()+" cannot be cast to string,field name="+field.getName());
	}
	
	static String getDate(Field field)
	{
		Object objValue=field.getObjectValue();
		if(objValue == null) {
			return null;
		}
		switch(field.getFieldType().getType())
		{
			case FieldType.DATE_TYPE:
			{
				return objValue.toString();
			}
			case FieldType.DATETIME_TYPE:
			{
				return objValue.toString().substring(0,10);
			}
		}
		
		throw new RuntimeException(field.getFieldType().getName()+" cannot be cast to date,field name="+field.getName());
	}
	
	static long getDateValue(Field field)
	{
		switch(field.getFieldType().getType())
		{
			case FieldType.DATE_TYPE:
			{
				return ((DateField)field).getLongValue();
			}
			case FieldType.DATETIME_TYPE:
			{
				return ((DateTimeField)field).getLongValue();
			}
		}
		
		throw new RuntimeException(field.getFieldType().getName()+" cannot be cast to date,field name="+field.getName());
	}
	
	static String getTime(Field field)
	{
		Object objValue=field.getObjectValue();
		if(objValue == null) {
			return null;
		}
		switch(field.getFieldType().getType())
		{
			case FieldType.TIME_TYPE:
			{
				return objValue.toString();
			}
			case FieldType.DATETIME_TYPE:
			{
				return objValue.toString().substring(11);
			}
		}
		
		throw new RuntimeException(field.getFieldType().getName()+" cannot be cast to time,field name="+field.getName());
	}
	
	static long getTimeValue(Field field)
	{
		switch(field.getFieldType().getType())
		{
			case FieldType.TIME_TYPE:
			{
				return ((TimeField)field).getLongValue();
			}
			case FieldType.DATETIME_TYPE:
			{
				 return ((DateTimeField)field).getLongValue();
			}
		}
		
		throw new RuntimeException(field.getFieldType().getName()+" cannot be cast to time,field name="+field.getName());
	}
	static String getDateTime(Field field)
	{
		
		Object objValue=field.getObjectValue();
		if(objValue == null) {
			return null;
		}
		switch(field.getFieldType().getType())
		{
			case FieldType.DATETIME_TYPE:
			{
				return objValue.toString();
			}
		}		
		throw new RuntimeException(field.getFieldType().getName()+" cannot be cast to dateTime,field name="+field.getName());
	}
	
	static long getDateTimeValue(Field field)
	{
		
		switch(field.getFieldType().getType())
		{
			case FieldType.DATETIME_TYPE:
			{
				return ((DateTimeField)field).getLongValue();
			}
		}		
		throw new RuntimeException(field.getFieldType().getName()+" cannot be cast to dateTime,field name="+field.getName());
	}
	
	 static void setField(CDO cdo,String strFieldId,Field field)
	    {
	  
			switch(field.getFieldType().getType())
			{
				case FieldType.BOOLEAN_TYPE:			
					cdo.setBooleanValue(strFieldId, ((BooleanField)field).getValue());
					break;
				case FieldType.BYTE_TYPE:
					cdo.setByteValue(strFieldId, ((ByteField)field).getValue());
					break;
				case FieldType.SHORT_TYPE:
					cdo.setShortValue(strFieldId, ((ShortField)field).getValue());
					break;
				case FieldType.INTEGER_TYPE:
					cdo.setIntegerValue(strFieldId, ((IntegerField)field).getValue());
					break;
				case FieldType.LONG_TYPE:
					cdo.setLongValue(strFieldId, ((LongField)field).getValue());
					break;
				case FieldType.FLOAT_TYPE:
					cdo.setFloatValue(strFieldId, ((FloatField)field).getValue());
					break;
				case FieldType.DOUBLE_TYPE:
					cdo.setDoubleValue(strFieldId, ((DoubleField)field).getValue());
					break;
				case FieldType.STRING_TYPE:
					cdo.setStringValue(strFieldId, ((StringField)field).getValue());
					break;
				case FieldType.DATE_TYPE:
					cdo.setDateValue(strFieldId, ((DateField)field).getValue());
					break;
				case FieldType.TIME_TYPE:
					cdo.setTimeValue(strFieldId, ((TimeField)field).getValue());
					break;
				case FieldType.DATETIME_TYPE:
					cdo.setDateTimeValue(strFieldId, ((DateTimeField)field).getValue());
				break;
				case FieldType.CDO_TYPE:
					{
						CDO dst=new CDO();
						CDO src=((CDOField)field).getValue();
				    	for(Iterator<Map.Entry<String,Field>> iterator=src.iterator();iterator.hasNext();){
				    		Entry<String,Field> entry=iterator.next();			    		
				    		setField(dst, entry.getKey(), entry.getValue());
				    	}					
						cdo.setCDOValue(strFieldId, dst);
					}
					break;
				case FieldType.FILE_TYPE:
					cdo.setFileValue(strFieldId, ((FileField)field).getValue());
					break;
				case FieldType.BOOLEAN_ARRAY_TYPE:
					cdo.setBooleanArrayValue(strFieldId, ((BooleanArrayField)field).getValue());
					break;
				case FieldType.BYTE_ARRAY_TYPE:
					cdo.setByteArrayValue(strFieldId, ((ByteArrayField)field).getValue());
					break;
				case FieldType.SHORT_ARRAY_TYPE:
					cdo.setShortArrayValue(strFieldId, ((ShortArrayField)field).getValue());
					break;
				case FieldType.INTEGER_ARRAY_TYPE:
					cdo.setIntegerArrayValue(strFieldId, ((IntegerArrayField)field).getValue());
					break;
				case FieldType.LONG_ARRAY_TYPE:
					cdo.setLongArrayValue(strFieldId, ((LongArrayField)field).getValue());
					break;
				case FieldType.FLOAT_ARRAY_TYPE:
					cdo.setFloatArrayValue(strFieldId, ((FloatArrayField)field).getValue());
					break;
				case FieldType.DOUBLE_ARRAY_TYPE:
					cdo.setDoubleArrayValue(strFieldId, ((DoubleArrayField)field).getValue());
					break;
				case FieldType.STRING_ARRAY_TYPE:
					cdo.setStringArrayValue(strFieldId, ((StringArrayField)field).getValue());
					break;
				case FieldType.DATE_ARRAY_TYPE:
					cdo.setDateArrayValue(strFieldId, ((DateArrayField)field).getValue());
					break;
				case FieldType.TIME_ARRAY_TYPE:
					cdo.setTimeArrayValue(strFieldId, ((TimeArrayField)field).getValue());
					break;
				case FieldType.DATETIME_ARRAY_TYPE:
					cdo.setDateTimeArrayValue(strFieldId, ((DateTimeArrayField)field).getValue());
					break;
				case FieldType.CDO_ARRAY_TYPE:					
					{
						List<CDO> src=((CDOArrayField)field).getValue();
						CDO[] dst=new CDO[src.size()];
						for(int i=0;i<src.size();i++){	
							dst[i]=new CDO();
					    	for(Iterator<Map.Entry<String,Field>> iterator=src.get(i).iterator();iterator.hasNext();){
					    		Entry<String,Field> entry=iterator.next();		    		
					    		setField(dst[i], entry.getKey(), entry.getValue());
					    	}								
						}																		
						cdo.setCDOArrayValue(strFieldId, dst);
					}					
					break;				
			}
	    }   
}
