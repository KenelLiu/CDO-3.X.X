
package com.cdo.field;

import java.nio.ByteBuffer;
import java.util.Map;

import com.cdo.google.GoogleCDO;
import com.cdoframework.cdolib.util.Utility;
/**
 * 定义byte字段
 * @author KenelLiu
 *
 */
public class ByteField extends FieldImpl{

	private static final long serialVersionUID = 1440959538120435270L;
	//属性对象,所有在本类中创建，并允许外部访问的对象在此声明并提供get/set方法-----------------------------------
	private byte byValue;
	public void setValue(byte byValue){
		this.byValue=byValue;
	}
	public byte getValue(){
		return this.byValue;
	}

	private void allocate(){
		int len=1+1;//字段类型所占字节+数据所占字节
		buffer=ByteBuffer.allocate(len);
		buffer.put((byte)FieldType.BYTE_TYPE);
		buffer.put(this.byValue);
		buffer.flip();
	}
	
	//公共方法,所有可提供外部使用的函数在此定义为public方法------------------------------------------------------
	@Override
	public void toAvro(String prefixField,Map<CharSequence,ByteBuffer> fieldMap){
		allocate();		
		super.toAvro(prefixField, fieldMap);
	}	
	@Override
	public void toProto(String prefixField,GoogleCDO.CDOProto.Builder cdoProto){
		allocate();	
		super.toProto(prefixField, cdoProto);
	}
	@Override
	public void toXML(StringBuilder strbXML){		
		strbXML.append("<BYF N=\"").append(this.getName()).append("\"");
		strbXML.append(" V=\"").append(this.byValue).append("\"/>");
	}
	@Override
	public void toXMLWithIndent(int nIndentSize,StringBuilder strbXML){
		String strIndent=Utility.makeSameCharString('\t',nIndentSize);

		strbXML.append(strIndent).append("<BYF N=\"").append(this.getName()).append("\"");
		strbXML.append(" V=\"").append(this.byValue).append("\"/>\r\n");
	}

	@Override
	public String toJSON(){
		StringBuffer str_JSON=new StringBuffer();
		str_JSON.append("\"").append(this.getName()).append("\"").append(":").append(this.byValue).append(",");
		return str_JSON.toString();
	}	
	
	@Override
	public Object getObjectValue(){
		return new Byte(byValue);
	}

	//构造函数,所有构造函数在此定义------------------------------------------------------------------------------
	public ByteField(String strFieldName){
		setFieldType(type.BYTE);		
		this.byValue=0;
	}

	public ByteField(String strFieldName,byte byValue){
		super(strFieldName);		
		setFieldType(type.BYTE);		
		this.byValue=byValue;
	}
	
	public ByteField(byte byValue){
		setFieldType(type.BYTE);		
		this.byValue	=byValue;
	}


}
