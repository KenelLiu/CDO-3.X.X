
package com.cdo.field;

import java.nio.Buffer;
import java.nio.ByteBuffer;

import com.cdoframework.cdolib.util.Utility;

/**
 * 定义Integer字段
 * @author KenelLiu
 *
 */
public class IntegerField extends FieldImpl
{

	//内部类,所有内部类在此声明----------------------------------------------------------------------------------

	//静态对象,所有static在此声明并初始化------------------------------------------------------------------------

	//内部对象,所有在本类中创建并使用的对象在此声明--------------------------------------------------------------

	private static final long serialVersionUID = -4743998197912167653L;
	//属性对象,所有在本类中创建，并允许外部访问的对象在此声明并提供get/set方法-----------------------------------
	private final int dataIndex=1;//数据保存的起始位置
	private final int databuffer=4;//数据占用字节
	
	public void setValue(int nValue)
	{
		allocate(nValue);
	}
	public int getValue()
	{
		buffer.position(dataIndex);
		int v=buffer.getInt();
		buffer.clear();		
		return v;
	}
	
	public Object getObjectValue()
	{
		return new Integer(getValue());
	}

	
	@Override
	public Buffer getBuffer() {	
		return buffer;
	}
	
	private void allocate(int nValue){
		if(buffer==null){
			int len=dataIndex+databuffer;
			buffer=ByteBuffer.allocate(len);
			buffer.put((byte)FieldType.INTEGER_TYPE);
		}
		buffer.position(dataIndex);
		buffer.putInt(nValue);
		buffer.flip();
	}		
	//公共方法,所有可提供外部使用的函数在此定义为public方法------------------------------------------------------
	@Override
	public void toXML(StringBuilder strbXML)
	{
		int nValue=getValue();
		strbXML.append("<NF N=\"").append(this.getName()).append("\"");
		strbXML.append(" V=\"").append(nValue).append("\"/>");
	}
	@Override
	public void toXMLWithIndent(int nIndentSize,StringBuilder strbXML)
	{
		int nValue=getValue();
		String strIndent=Utility.makeSameCharString('\t',nIndentSize);
		
		strbXML.append(strIndent).append("<NF N=\"").append(this.getName()).append("\"");
		strbXML.append(" V=\"").append(nValue).append("\"/>\r\n");
	}
	@Override
	public String toJSON()
	{
		int nValue=getValue();
		StringBuffer str_JSON=new StringBuffer();
		str_JSON.append("\"").append(this.getName()).append("\"").append(":").append(nValue).append(",");
		return str_JSON.toString();
	}

	//构造函数,所有构造函数在此定义------------------------------------------------------------------------------
	public IntegerField(String strFieldName){
		super(strFieldName);		
		setFieldType(type.INTEGER);
		setValue(0);
	}

	public IntegerField(int nValue){
		setFieldType(type.INTEGER);
		setValue(nValue);
	}
	
	public IntegerField(String strFieldName,int nValue){
		super(strFieldName);		
		setFieldType(type.INTEGER);	
		setValue(nValue);
	}
	
	public IntegerField(String strFieldName,ByteBuffer buffer){
		super(strFieldName);		
		setFieldType(type.INTEGER);		
		this.buffer=buffer;
	}
}
