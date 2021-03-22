package com.cdo.field;

import java.nio.Buffer;
import java.nio.ByteBuffer;

import com.cdoframework.cdolib.util.Utility;

/**
 * 定义Double字段
 * @author KenelLiu
 *
 */
public class DoubleField extends FieldImpl{
	private static final long serialVersionUID = -794889290408597843L;
	//属性对象,所有在本类中创建，并允许外部访问的对象在此声明并提供get/set方法-----------------------------------
	private final int dataIndex=1;//数据保存的起始位置
	private final int databuffer=8;//数据占用字节
	
	public void setValue(double dblValue)
	{
		allocate(dblValue);
	}
	public double getValue()
	{		
		buffer.position(dataIndex);
		double d=buffer.getDouble();
		buffer.clear();
		return d;
	}

	public Object getObjectValue()
	{
		 return new Double(getValue());
	}
	
	@Override
	public Buffer getBuffer() {	
		return buffer;
	}
	
	private void allocate(double dblValue){
		if(buffer==null){
			int len=dataIndex+databuffer;
			buffer=ByteBuffer.allocate(len);
			buffer.put((byte)FieldType.DOUBLE_TYPE);
		}
		buffer.position(dataIndex);
		buffer.putDouble(dblValue);
		buffer.flip();
	}	
	//公共方法,所有可提供外部使用的函数在此定义为public方法------------------------------------------------------
	@Override
	public void toXML(StringBuilder strbXML)
	{
		double dblValue=getValue();
		strbXML.append("<DBLF N=\"").append(this.getName()).append("\"");
		strbXML.append(" V=\"").append(dblValue).append("\"/>");
	}
	@Override
	public void toXMLWithIndent(int nIndentSize,StringBuilder strbXML)
	{
		double dblValue=getValue();
		String strIndent=Utility.makeSameCharString('\t',nIndentSize);		

		strbXML.append(strIndent).append("<DBLF N=\"").append(this.getName()).append("\"");
		strbXML.append(" V=\"").append(dblValue).append("\"/>\r\n");
	}
	@Override
	public String toJSON()
	{
		double dblValue=getValue();
		StringBuffer str_JSON=new StringBuffer();
		str_JSON.append("\"").append(this.getName()).append("\"").append(":").append(dblValue).append(",");
		return str_JSON.toString();
	}

	//构造函数,所有构造函数在此定义------------------------------------------------------------------------------

	public DoubleField(String strFieldName){
		super(strFieldName);		
		setFieldType(type.DOUBLE);		
		setValue(0);
	}

	public DoubleField(String strFieldName,double dblValue){
		super(strFieldName);		
		setFieldType(type.DOUBLE);		
		setValue(dblValue);
	}
	
	public DoubleField(double dblValue){
		setFieldType(type.DOUBLE);		
		setValue(dblValue);
	}
	
	public DoubleField(String strFieldName,ByteBuffer buffer){
		super(strFieldName);			
		setFieldType(type.DOUBLE);			
		this.buffer=buffer;
	} 
}
