package com.cdo.field;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.Map;

import com.cdo.google.GoogleCDO;
import com.cdo.pattern.Pattern;
import com.google.protobuf.ByteString;
/**
 * 重新构造
 * @author KenelLiu
 *
 */
public abstract class FieldImpl implements Field,Pattern
{


	//内部类,所有内部类在此声明----------------------------------------------------------------------------------

	//静态对象,所有static在此声明并初始化------------------------------------------------------------------------
	//内部对象,所有在本类中创建并使用的对象在此声明--------------------------------------------------------------

	private static final long serialVersionUID = 1324693182949266208L;
	//属性对象,所有在本类中创建，并允许外部访问的对象在此声明并提供get/set方法-----------------------------------
	private FieldType.type type;
	private String strName;
	protected ByteBuffer buffer=null;//需要谨慎操作，仅在内部字段里使用
	
	public void setFieldType(FieldType.type type)
	{
		this.type=type;
	}
	public FieldType.type getFieldType()
	{
		return type;
	}

	public void setName(String strName)
	{
		this.strName=strName;
	}
	public String getName()
	{
		return strName;
	}


	public FieldImpl(){
		this("");
	}

	public FieldImpl(String strFieldName){
		this.type=FieldType.type.NONE;
		strName=strFieldName;
	}
	@Override
	public String toHtmlJSON(){
		return toJSON();
	}	
	@Override
	public String toString() {	
		return toJSON();		
	}
		
	@Override
	public String toMixHtmlJSON(){
		return toJSON();
	}		
	@Override
	public void toAvro(String prefixField,Map<CharSequence,ByteBuffer> fieldMap){
		fieldMap.put(prefixField+this.getName(), buffer);		
	}

	@Override
	public void toProto(String prefixField,GoogleCDO.CDOProto.Builder cdoProto){
		GoogleCDO.CDOProto.Entry.Builder entry=GoogleCDO.CDOProto.Entry.newBuilder();
		entry.setName(prefixField+this.getName());
		entry.setValue(ByteString.copyFrom(buffer));
		buffer.flip();
		cdoProto.addFields(entry);
	}
	
	@Override
	public Buffer getBuffer() {		
		return buffer;
	}
	
	@Override
	public void release() {		
		 this.buffer=null;
	}
	
}
