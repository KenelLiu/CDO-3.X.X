
package com.cdo.field;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;

import com.cdoframework.cdolib.util.Utility;

/**
 * 定义DateTime字段
 * @author KenelLiu
 *
 */
public class DateTimeField extends FieldImpl{
	
	private static final long serialVersionUID = 885706546812383630L;
	//属性对象,所有在本类中创建，并允许外部访问的对象在此声明并提供get/set方法-----------------------------------
	private final int dataIndex=1;//数据保存的起始位置
	private final int databuffer=8;//数据占用字节
	
	public void setValue(String strValue)
	{
		try{	
			SimpleDateFormat sdf=new SimpleDateFormat(PATTERN_DATETIME);
			allocate(sdf.parse(strValue).getTime());
		}catch(Exception ex){
			throw new RuntimeException("["+strValue+"] Invalid datetime or Invalid datetime format,datetime format must is "+PATTERN_DATETIME);
		}

	}
	
	public void setLongValue(long lValue){		
		allocate(lValue);
		
	}
	
	public String getValue()
	{
		long v=getLongValue();
		SimpleDateFormat sdf=new SimpleDateFormat(PATTERN_DATETIME);		
		return sdf.format(new java.util.Date(v));
	}
	
	public long getLongValue(){
		buffer.position(dataIndex);
		long v= buffer.getLong();
		buffer.clear();
		return v;
	}
	
	public Object getObjectValue()
	{
		return getValue();
	}
	
	@Override
	public Buffer getBuffer() {	
		return buffer;
	}
	
	private void allocate(long lValue){		
		if(buffer==null){
			int len=dataIndex+databuffer;
			buffer=ByteBuffer.allocate(len);
			buffer.put((byte)FieldType.DATETIME_TYPE);
		}
		buffer.position(dataIndex);
		buffer.putLong(lValue);
		buffer.flip();		
	}	

	//公共方法,所有可提供外部使用的函数在此定义为public方法------------------------------------------------------
	@Override
	public void toXML(StringBuilder strbXML)
	{
		String strValue=getValue();
		strbXML.append("<DTF N=\"").append(this.getName()).append("\"");
		strbXML.append(" V=\"").append(strValue).append("\"/>");
	}
	@Override
	public void toXMLWithIndent(int nIndentSize,StringBuilder strbXML)
	{
		String strIndent=Utility.makeSameCharString('\t',nIndentSize);
		String strValue=getValue();
		strbXML.append(strIndent).append("<DTF N=\"").append(this.getName()).append("\"");
		strbXML.append(" V=\"").append(strValue).append("\"/>\r\n");
	}
	@Override
	public String toJSON()
	{
		String strValue=getValue();
		StringBuffer str_JSON=new StringBuffer();
		str_JSON.append("\"").append(this.getName()).append("\"").append(":\"").append(strValue).append("\",");
		return str_JSON.toString();
	}
	
	//构造函数,所有构造函数在此定义------------------------------------------------------------------------------
	public DateTimeField(String strFieldName){
		super(strFieldName);		
		setFieldType(type.DATETIME);		
		setLongValue(0);
	}

	public DateTimeField(String strFieldName,String strValue){
		super(strFieldName);		
		setFieldType(type.DATETIME);
		setValue(strValue);
	}

	public DateTimeField(String strFieldName,long lValue){
		super(strFieldName);		
		setFieldType(type.DATETIME);
		setLongValue(lValue);
	}
	
	public DateTimeField(String strFieldName,ByteBuffer buffer){
		super(strFieldName);		
		setFieldType(type.DATETIME);		
		this.buffer=buffer;
	}

}
