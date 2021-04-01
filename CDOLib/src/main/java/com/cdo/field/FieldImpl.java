package com.cdo.field;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.Map;

import com.cdo.google.GoogleCDO;
import com.cdo.pattern.Pattern;
import com.google.protobuf.ByteString;
/**
 * @author KenelLiu
 */
public abstract class FieldImpl implements Field,Pattern{

	private static final long serialVersionUID = 1324693182949266208L;
	//=========字段类型==============//
	private FieldType.type type;
	//=========字段名称==============//
	private String fieldName;
	
	//=====管理字段字节内容分配,需要谨慎操作，仅在内部字段里使用=======//
	protected ByteBuffer buffer=null;
	
	public void setFieldType(FieldType.type type)
	{
		this.type=type;
	}
	public FieldType.type getFieldType()
	{
		return type;
	}

	public void setName(String fieldName)
	{
		this.fieldName=fieldName;
	}
	public String getName()
	{
		return fieldName;
	}


	public FieldImpl(){
		this("");
	}

	public FieldImpl(String strFieldName){
		this.type=FieldType.type.NONE;
		this.fieldName=strFieldName;
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
