package com.cdo.field;

import java.nio.Buffer;
import java.nio.ByteBuffer;

import com.cdoframework.cdolib.util.Utility;

/**
 * 定义float字段
 * @author KenelLiu
 *
 */
public class FloatField extends FieldImpl
{

	private static final long serialVersionUID = -6190593964278306132L;
	//属性对象,所有在本类中创建，并允许外部访问的对象在此声明并提供get/set方法-----------------------------------
	private final int dataIndex=1;//数据保存的起始位置
	private final int databuffer=4;//数据占用字节
	
	public void setValue(float fValue)
	{
		allocate(fValue);
	}
	public float getValue()
	{
		
		buffer.position(dataIndex);
		float f=buffer.getFloat();
		buffer.clear();
		return f;
	}
	
	public Object getObjectValue()
	{
		return new Float(getValue());
	}
	
	
	@Override
	public Buffer getBuffer() {	
		return buffer;
	}
	
	private void allocate(float dblValue){
		if(buffer==null){
			int len=dataIndex+databuffer;
			buffer=ByteBuffer.allocate(len);
			buffer.put((byte)FieldType.FLOAT_TYPE);
		}
		buffer.position(dataIndex);
		buffer.putFloat(dblValue);
		buffer.flip();
	}		
	//公共方法,所有可提供外部使用的函数在此定义为public方法------------------------------------------------------	
	@Override
	public void toXML(StringBuilder strbXML)
	{
		float fValue=getValue();
		strbXML.append("<FF N=\"").append(this.getName()).append("\"");
		strbXML.append(" V=\"").append(fValue).append("\"/>");
	}
	@Override
	public void toXMLWithIndent(int nIndentSize,StringBuilder strbXML)
	{
		float fValue=getValue();
		String strIndent=Utility.makeSameCharString('\t',nIndentSize);		

		strbXML.append(strIndent).append("<FF N=\"").append(this.getName()).append("\"");
		strbXML.append(" V=\"").append(fValue).append("\"/>\r\n");
	}
	@Override
	public String toJSON()
	{
		float fValue=getValue();
		StringBuffer str_JSON=new StringBuffer();
		str_JSON.append("\"").append(this.getName()).append("\"").append(":").append(fValue).append(",");
		return str_JSON.toString();
	}	

	//构造函数,所有构造函数在此定义------------------------------------------------------------------------------
	public FloatField(String strFieldName){
		super(strFieldName);		
		setFieldType(type.FLOAT);				
		setValue(0);
	}

	public FloatField(String strFieldName,float fValue){
		super(strFieldName);		
		setFieldType(type.FLOAT);		
		setValue(fValue);
	}

	public FloatField(float fValue){
		setFieldType(type.FLOAT);		
		setValue(fValue);
	}
	
	public FloatField(String strFieldName,ByteBuffer buffer){
		//仅作反序列化使用		
		super(strFieldName);		
		setFieldType(type.FLOAT);		
		this.buffer=buffer;
	}
}
