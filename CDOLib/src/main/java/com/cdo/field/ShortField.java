package com.cdo.field;

import java.nio.Buffer;
import java.nio.ByteBuffer;

import com.cdoframework.cdolib.util.Utility;

/**
 * 定义short字段
 * @author KenelLiu
 *
 */
public class ShortField extends FieldImpl{
	private static final long serialVersionUID = 5553898334046272956L;
	//属性对象,所有在本类中创建，并允许外部访问的对象在此声明并提供get/set方法-----------------------------------
	private final int dataIndex=1;//数据保存的起始位置
	private final int databuffer=2;//数据占用字节
	
	public void setValue(short shValue)
	{
		allocate(shValue);
	}
	
	public short getValue()
	{
		buffer.position(dataIndex);
		short v=buffer.getShort();
		buffer.clear();
		return v;
	}
	
	public Object getObjectValue()
	{
		return new Short(getValue());
	}
	
	@Override
	public Buffer getBuffer() {	
		return buffer;
	}
	
	private void allocate(short shValue){
		if(buffer==null){
			int len=dataIndex+databuffer;
			buffer=ByteBuffer.allocate(len);
			buffer.put((byte)FieldType.SHORT_TYPE);
		}
		buffer.position(dataIndex);
		buffer.putShort(shValue);
		buffer.flip();
	}		
	//公共方法,所有可提供外部使用的函数在此定义为public方法------------------------------------------------------
	@Override
	public void toXML(StringBuilder strbXML)
	{
		short shValue=getValue(); 
		strbXML.append("<SF N=\"").append(this.getName()).append("\"");
		strbXML.append(" V=\"").append(shValue).append("\"/>");
	}
	@Override
	public void toXMLWithIndent(int nIndentSize,StringBuilder strbXML)
	{
		short shValue=getValue(); 
		String strIndent=Utility.makeSameCharString('\t',nIndentSize);		

		strbXML.append(strIndent).append("<SF N=\"").append(this.getName()).append("\"");
		strbXML.append(" V=\"").append(shValue).append("\"/>\r\n");
	}
	@Override
	public String toJSON()
	{
		short shValue=getValue(); 
		StringBuffer str_JSON=new StringBuffer();
		str_JSON.append("\"").append(this.getName()).append("\"").append(":").append(shValue).append(",");
		return str_JSON.toString();
	}

	//构造函数,所有构造函数在此定义------------------------------------------------------------------------------
	public ShortField(String strFieldName){
		super(strFieldName);		
		setFieldType(type.SHORT);
		setValue((short)0);
	}

	public ShortField(String strFieldName,short shValue){
		super(strFieldName);		
		setFieldType(type.SHORT);		
		setValue(shValue);
	}
	
	public ShortField(short shValue){		
		setFieldType(type.SHORT);		
		setValue(shValue);
	}

	public ShortField(String strFieldName,ByteBuffer buffer){
		super(strFieldName);		
		setFieldType(type.SHORT);		
		this.buffer=buffer;
	}
}
