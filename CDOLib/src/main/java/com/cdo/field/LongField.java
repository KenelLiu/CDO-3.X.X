package com.cdo.field;

import java.nio.Buffer;
import java.nio.ByteBuffer;

import com.cdoframework.cdolib.util.Utility;

/**
 * 定义long字段
 * @author KenelLiu
 *
 */
public class LongField extends FieldImpl{

	private static final long serialVersionUID = -4369163444075495461L;
	//属性对象,所有在本类中创建，并允许外部访问的对象在此声明并提供get/set方法-----------------------------------
	private final int dataIndex=1;//数据保存的起始位置
	private final int databuffer=8;//数据占用字节
	public void setValue(long lValue)
	{
		allocate(lValue);
	}
	
	public long getValue()
	{
		buffer.position(dataIndex);
		long v= buffer.getLong();
		buffer.clear();
		return v;
	}
	
	public Object getObjectValue()
	{
		return new Long(getValue());
	}

	@Override
	public Buffer getBuffer() {	
		return buffer;
	}
	
	private void allocate(long lValue){
		if(buffer==null){
			int len=dataIndex+databuffer;
			buffer=ByteBuffer.allocate(len);
			buffer.put((byte)FieldType.LONG_TYPE);
		}
		buffer.position(dataIndex);
		buffer.putLong(lValue);
		buffer.flip();
	}		
	//公共方法,所有可提供外部使用的函数在此定义为public方法------------------------------------------------------
	@Override
	public void toXML(StringBuilder strbXML)
	{
		long lValue=getValue();
		strbXML.append("<LF N=\"").append(this.getName()).append("\"");
		strbXML.append(" V=\"").append(lValue).append("\"/>");
	}
	@Override
	public void toXMLWithIndent(int nIndentSize,StringBuilder strbXML)
	{
		String strIndent=Utility.makeSameCharString('\t',nIndentSize);
		long lValue=getValue();
		strbXML.append(strIndent).append("<LF N=\"").append(this.getName()).append("\"");
		strbXML.append(" V=\"").append(lValue).append("\"/>\r\n");
	}
	@Override
	public String toJSON()
	{
		long lValue=getValue();
		StringBuffer str_JSON=new StringBuffer();
		str_JSON.append("\"").append(this.getName()).append("\"").append(":").append(lValue).append(",");		
		return str_JSON.toString();
	}

	//构造函数,所有构造函数在此定义------------------------------------------------------------------------------
	public LongField(String strFieldName){
		super(strFieldName);		
		setFieldType(type.LONG);
		setValue(0);
	}

	public LongField(long lValue){		
		setFieldType(type.LONG);
		setValue(lValue);
	}
	
	public LongField(String strFieldName,long lValue){
		super(strFieldName);		
		setFieldType(type.LONG);		
		setValue(lValue);
	}
	
	 public LongField(String strFieldName,ByteBuffer buffer){
		super(strFieldName);		
		setFieldType(type.LONG);		
		this.buffer=buffer;
	}

}
