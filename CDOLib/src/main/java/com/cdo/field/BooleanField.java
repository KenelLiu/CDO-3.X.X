
package com.cdo.field;

import java.nio.Buffer;
import java.nio.ByteBuffer;

import com.cdoframework.cdolib.util.Utility;
/**
 * 定义boolean字段
 * @author KenelLiu
 */
public class BooleanField extends FieldImpl{

	private static final long serialVersionUID = 3388005366612717848L;
	//属性对象,所有在本类中创建，并允许外部访问的对象在此声明并提供get/set方法-----------------------------------
	private final int dataIndex=1;//数据保存的起始位置
	private final int databuffer=1;//数据占用字节
	
	public void setValue(boolean bValue){
		allocate(bValue);
	}

	public boolean getValue(){
		buffer.position(dataIndex);
		byte b=buffer.get();	
		buffer.clear();
		if(b==1)
			return true;
		return false;	
	}
	
	public Object getObjectValue(){
		return new Boolean(getValue());
	}
	
	@Override
	public Buffer getBuffer() {	
		return buffer;
	}
	
	private void allocate(boolean bValue){
		if(buffer==null){
			int len=dataIndex+databuffer;
			buffer=ByteBuffer.allocate(len);
			buffer.put((byte)FieldType.BOOLEAN_TYPE);
		}
		buffer.position(dataIndex);
		if(bValue)
			buffer.put((byte)1);
		else
			buffer.put((byte)0);
		buffer.flip();
	}

	//公共方法,所有可提供外部使用的函数在此定义为public方法------------------------------------------------------

	public void toXML(StringBuilder strbXML){		
		strbXML.append("<BF N=\"").append(this.getName()).append("\"");
		strbXML.append(" V=\"").append(getValue()).append("\"/>");
	}

	public void toXMLWithIndent(int nIndentSize,StringBuilder strbXML){
		String strIndent=Utility.makeSameCharString('\t',nIndentSize);

		strbXML.append(strIndent).append("<BF N=\"").append(this.getName()).append("\"");
		strbXML.append(" V=\"").append(getValue()).append("\"/>\r\n");
	}	
	

	public String toJSON(){
		StringBuffer str_JSON=new StringBuffer();
		str_JSON.append("\"").append(this.getName()).append("\"").append(":").append(getValue()).append(",");
		return str_JSON.toString();
	}	

	//构造函数,所有构造函数在此定义------------------------------------------------------------------------------
	public BooleanField(String strFieldName){
		setFieldType(type.BOOLEAN);
		allocate(false);		
	}

	public BooleanField(String strFieldName,boolean bValue){
		super(strFieldName);		
		setFieldType(type.BOOLEAN);		
		allocate(bValue);
	}
	
	public BooleanField(boolean bValue){
		setFieldType(type.BOOLEAN);		
		allocate(bValue);
	}
		
	public BooleanField(String strFieldName,ByteBuffer buffer){
		//在反序列化使用
		super(strFieldName);		
		setFieldType(type.BOOLEAN);		
		this.buffer=buffer;
	}
}
