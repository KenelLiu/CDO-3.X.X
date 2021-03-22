package com.cdoframework.cdolib.data.cdo;

import java.nio.ByteBuffer;

import com.cdo.field.BooleanField;
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
import com.cdoframework.cdolib.data.cdo.CDO.FieldId;
/**
 * 
 * @author KenelLiu
 *
 */
public class CDOBuffer {


    protected void setBooleanValue(CDO cdo,String strFieldId,ByteBuffer buffer)
    {
    	
    	FieldId fieldId=cdo.parseFieldId(strFieldId);
    	if(fieldId==null)
    	{
			throw new RuntimeException("Invalid FieldId "+strFieldId);
    	}
    	BooleanField field=new BooleanField(fieldId.strFieldId,buffer);
   		cdo.setObjectValue(fieldId,FieldType.BOOLEAN_TYPE,null,field,cdo);
    }
    
    protected void setShortValue(CDO cdo,String strFieldId,ByteBuffer buffer)
    {
    	
    	FieldId fieldId=cdo.parseFieldId(strFieldId);
    	if(fieldId==null)
    	{
			throw new RuntimeException("Invalid FieldId "+strFieldId);
    	}
    	Field field=new ShortField(fieldId.strFieldId,buffer);
		cdo.setObjectValue(fieldId,FieldType.SHORT_TYPE,null,field,cdo);
    }

    protected void setIntegerValue(CDO cdo,String strFieldId,ByteBuffer buffer)
    {
    	
    	FieldId fieldId=cdo.parseFieldId(strFieldId);
    	if(fieldId==null)
    	{
			throw new RuntimeException("Invalid FieldId "+strFieldId);
    	}
    	Field field=new IntegerField(fieldId.strFieldId,buffer);
		cdo.setObjectValue(fieldId,FieldType.INTEGER_TYPE,null,field,cdo);
    }

    protected void setLongValue(CDO cdo,String strFieldId,ByteBuffer buffer)
    {
    	
    	
    	FieldId fieldId=cdo.parseFieldId(strFieldId);

    	if(fieldId==null)
    	{
			throw new RuntimeException("Invalid FieldId "+strFieldId);
    	}
    	Field field=new LongField(fieldId.strFieldId,buffer);
		cdo.setObjectValue(fieldId,FieldType.LONG_TYPE,null,field,cdo);
    }

    protected void setFloatValue(CDO cdo,String strFieldId,ByteBuffer buffer)
    {
    	

    	FieldId fieldId=cdo.parseFieldId(strFieldId);
    	if(fieldId==null)
    	{
			throw new RuntimeException("Invalid FieldId "+strFieldId);
    	}
    	Field field=new FloatField(fieldId.strFieldId,buffer);
		cdo.setObjectValue(fieldId,FieldType.FLOAT_TYPE,null,field,cdo);
    }

    protected void setDoubleValue(CDO cdo,String strFieldId,ByteBuffer buffer)
    {
    	

    	FieldId fieldId=cdo.parseFieldId(strFieldId);
    	if(fieldId==null)
    	{
			throw new RuntimeException("Invalid FieldId "+strFieldId);
    	}
    	Field field=new DoubleField(fieldId.strFieldId,buffer);
		cdo.setObjectValue(fieldId,FieldType.DOUBLE_TYPE,null,field,cdo);
    }

    protected void setStringValue(CDO cdo,String strFieldId,ByteBuffer buffer)
    {
    	

    	FieldId fieldId=cdo.parseFieldId(strFieldId);
    	if(fieldId==null)
    	{
			throw new RuntimeException("Invalid FieldId "+strFieldId);
    	}
    	Field field=new StringField(fieldId.strFieldId,buffer);
		cdo.setObjectValue(fieldId,FieldType.STRING_TYPE,null,field,cdo);
    }

    
    protected void setDateValue(CDO cdo,String strFieldId,ByteBuffer buffer)
    {
    	

    	FieldId fieldId=cdo.parseFieldId(strFieldId);
    	if(fieldId==null)
    	{
			throw new RuntimeException("Invalid FieldId "+strFieldId);
    	}
    	Field field=new DateField(fieldId.strFieldId,buffer);
		cdo.setObjectValue(fieldId,FieldType.DATE_TYPE,null,field,cdo);
    }

    protected void setTimeValue(CDO cdo,String strFieldId,ByteBuffer buffer)
    {
    	

    	FieldId fieldId=cdo.parseFieldId(strFieldId);
    	if(fieldId==null)
    	{
			throw new RuntimeException("Invalid FieldId "+strFieldId);
    	}
    	Field field=new TimeField(fieldId.strFieldId,buffer);
		cdo.setObjectValue(fieldId,FieldType.TIME_TYPE,null,field,cdo);
    }

    protected void setDateTimeValue(CDO cdo,String strFieldId,ByteBuffer buffer)
    {
    	

    	FieldId fieldId=cdo.parseFieldId(strFieldId);
    	if(fieldId==null)
    	{
			throw new RuntimeException("Invalid FieldId "+strFieldId);
    	}
    	Field field=new DateTimeField(fieldId.strFieldId,buffer);
		cdo.setObjectValue(fieldId,FieldType.DATETIME_TYPE,null,field,cdo);
    }
    
    protected void setFileValue(CDO cdo,String strFieldId,ByteBuffer buffer)
    {
    	

    	FieldId fieldId=cdo.parseFieldId(strFieldId);
    	if(fieldId==null)
    	{
			throw new RuntimeException("Invalid FieldId "+strFieldId);
    	}
    	Field field=new FileField(fieldId.strFieldId,buffer);
		cdo.setObjectValue(fieldId,FieldType.FILE_TYPE,null,field,cdo);
		if(fieldId.nType==FieldId.SIMPLE){
			cdo.setFileCount(cdo.getSerialFileCount()+1);
		}
    }


    protected void setBooleanArrayValue(CDO cdo,String strFieldId,ByteBuffer buffer)
    {
    	

    	FieldId fieldId=cdo.parseFieldId(strFieldId);
    	if(fieldId==null)
    	{
			throw new RuntimeException("Invalid FieldId "+strFieldId);
    	}
    	
    	if(fieldId.nType==FieldId.ARRAY_ELEMENT)
    	{
			throw new RuntimeException("Invalid FieldId "+strFieldId);
    	}
    	else
    	{
    		Field field=new BooleanArrayField(fieldId.strFieldId,buffer);
    		cdo.setObjectValue(fieldId,FieldType.BOOLEAN_ARRAY_TYPE,null,field,cdo);
    	}
    }

    protected void setByteArrayValue(CDO cdo,String strFieldId,ByteBuffer buffer)
    {
    	

    	FieldId fieldId=cdo.parseFieldId(strFieldId);
    	if(fieldId==null)
    	{
			throw new RuntimeException("Invalid FieldId "+strFieldId);
    	}
    	
    	if(fieldId.nType==FieldId.ARRAY_ELEMENT)
    	{
			throw new RuntimeException("Invalid FieldId "+strFieldId);
    	}
    	else
    	{
    		Field field=new ByteArrayField(fieldId.strFieldId,buffer);
    		cdo.setObjectValue(fieldId,FieldType.BYTE_ARRAY_TYPE,null,field,cdo);
    	}
    }

    protected void setShortArrayValue(CDO cdo,String strFieldId,ByteBuffer buffer)
    {
    	

    	FieldId fieldId=cdo.parseFieldId(strFieldId);
    	if(fieldId==null)
    	{
			throw new RuntimeException("Invalid FieldId "+strFieldId);
    	}
    	
    	if(fieldId.nType==FieldId.ARRAY_ELEMENT)
    	{
			throw new RuntimeException("Invalid FieldId "+strFieldId);
    	}
    	else
    	{
    		Field field=new ShortArrayField(fieldId.strFieldId,buffer);
    		cdo.setObjectValue(fieldId,FieldType.SHORT_ARRAY_TYPE,null,field,cdo);
    	}
    }

    protected void setIntegerArrayValue(CDO cdo,String strFieldId,ByteBuffer buffer)
    {
    	

    	FieldId fieldId=cdo.parseFieldId(strFieldId);
    	if(fieldId==null)
    	{
			throw new RuntimeException("Invalid FieldId "+strFieldId);
    	}
    	
    	if(fieldId.nType==FieldId.ARRAY_ELEMENT)
    	{
			throw new RuntimeException("Invalid FieldId "+strFieldId);
    	}
    	else
    	{
    		Field field=new IntegerArrayField(fieldId.strFieldId,buffer); 
    		cdo.setObjectValue(fieldId,FieldType.INTEGER_ARRAY_TYPE,null,field,cdo);
    	}
    }
    protected void setFloatArrayValue(CDO cdo,String strFieldId,ByteBuffer buffer)
    {
    	

    	FieldId fieldId=cdo.parseFieldId(strFieldId);
    	if(fieldId==null)
    	{
			throw new RuntimeException("Invalid FieldId "+strFieldId);
    	}
    	
    	if(fieldId.nType==FieldId.ARRAY_ELEMENT)
    	{
			throw new RuntimeException("Invalid FieldId "+strFieldId);
    	}
    	else
    	{
    		Field field=new FloatArrayField(fieldId.strFieldId,buffer); 
    		cdo.setObjectValue(fieldId,FieldType.FLOAT_ARRAY_TYPE,null,field,cdo);
    	}
    }
    protected void setDoubleArrayValue(CDO cdo,String strFieldId,ByteBuffer buffer)
    {
    	
    	FieldId fieldId=cdo.parseFieldId(strFieldId);
    	if(fieldId==null)
    	{
			throw new RuntimeException("Invalid FieldId "+strFieldId);
    	}
    	
    	if(fieldId.nType==FieldId.ARRAY_ELEMENT)
    	{
			throw new RuntimeException("Invalid FieldId "+strFieldId);
    	}
    	else
    	{
    		Field field=new DoubleArrayField(fieldId.strFieldId,buffer); 
    		cdo.setObjectValue(fieldId,FieldType.DOUBLE_ARRAY_TYPE,null,field,cdo);
    	}
    }
    protected void setLongArrayValue(CDO cdo,String strFieldId,ByteBuffer buffer)
    {
    	

    	FieldId fieldId=cdo.parseFieldId(strFieldId);
    	if(fieldId==null)
    	{
			throw new RuntimeException("Invalid FieldId "+strFieldId);
    	}
    	
    	if(fieldId.nType==FieldId.ARRAY_ELEMENT)
    	{
			throw new RuntimeException("Invalid FieldId "+strFieldId);
    	}
    	else
    	{
    		Field field=new LongArrayField(fieldId.strFieldId,buffer); 
    		cdo.setObjectValue(fieldId,FieldType.LONG_ARRAY_TYPE,null,field,cdo);
    	}
    }
    protected void setStringArrayValue(CDO cdo,String strFieldId,ByteBuffer buffer)
    {
    	

    	FieldId fieldId=cdo.parseFieldId(strFieldId);
    	if(fieldId==null)
    	{
			throw new RuntimeException("Invalid FieldId "+strFieldId);
    	}
    	
    	if(fieldId.nType==FieldId.ARRAY_ELEMENT)
    	{
			throw new RuntimeException("Invalid FieldId "+strFieldId);
    	}
    	else
    	{
    		Field field=new StringArrayField(fieldId.strFieldId,buffer);  
    		cdo.setObjectValue(fieldId,FieldType.STRING_ARRAY_TYPE,null,field,cdo);
    	}
    }

    protected void setDateArrayValue(CDO cdo,String strFieldId,ByteBuffer buffer)
    {
    	

    	FieldId fieldId=cdo.parseFieldId(strFieldId);
    	if(fieldId==null)
    	{
			throw new RuntimeException("Invalid FieldId "+strFieldId);
    	}
    	
    	if(fieldId.nType==FieldId.ARRAY_ELEMENT)
    	{
			throw new RuntimeException("Invalid FieldId "+strFieldId);
    	}
    	else
    	{
    		Field field=new DateArrayField(fieldId.strFieldId,buffer);  
    		cdo.setObjectValue(fieldId,FieldType.DATE_ARRAY_TYPE,null,field,cdo);
    	}
    }

    protected void setTimeArrayValue(CDO cdo,String strFieldId,ByteBuffer buffer)
    {
    	

    	FieldId fieldId=cdo.parseFieldId(strFieldId);
    	if(fieldId==null)
    	{
			throw new RuntimeException("Invalid FieldId "+strFieldId);
    	}
    	
    	if(fieldId.nType==FieldId.ARRAY_ELEMENT)
    	{
			throw new RuntimeException("Invalid FieldId "+strFieldId);
    	}
    	else
    	{
    		Field field=new TimeArrayField(fieldId.strFieldId,buffer);  
    		cdo.setObjectValue(fieldId,FieldType.TIME_ARRAY_TYPE,null,field,cdo);
    	}
    }

    protected void setDateTimeArrayValue(CDO cdo,String strFieldId,ByteBuffer buffer)
    {
    	

    	FieldId fieldId=cdo.parseFieldId(strFieldId);
    	if(fieldId==null)
    	{
			throw new RuntimeException("Invalid FieldId "+strFieldId);
    	}
    	
    	if(fieldId.nType==FieldId.ARRAY_ELEMENT)
    	{
			throw new RuntimeException("Invalid FieldId "+strFieldId);
    	}
    	else
    	{
    		Field field=new DateTimeArrayField(fieldId.strFieldId,buffer);     		
    		cdo.setObjectValue(fieldId,FieldType.DATETIME_ARRAY_TYPE,null,field,cdo);
    	}
    }  
}
