package com.cdo.field;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Map;

import com.cdo.google.GoogleCDO;
import com.cdoframework.cdolib.util.Function;
import com.cdoframework.cdolib.util.Utility;

/**
 * 定义shring字段
 * @author KenelLiu
 *
 */
public class NullField extends FieldImpl{

	private static final long serialVersionUID = 5971309765452721210L;
	//属性对象,所有在本类中创建，并允许外部访问的对象在此声明并提供get/set方法-----------------------------------
	//=========NULL 分配一个字节,表示类型即可.==============//	
	public void setValue()
	{
		allocateBuffer();
	}
	


	public Object getObjectValue()
	{
		return null;
	}	
	@Override
	public Buffer getBuffer() {	
		return buffer;
	}

	private  void allocateBuffer(){
	
		buffer=ByteBuffer.allocate(1);
		buffer.put((byte)FieldType.NULL_TYPE);
			
	}
	
	@Override
	public void toAvro(String prefixField,Map<CharSequence,ByteBuffer> fieldMap){
		buffer=ByteBuffer.allocate(1);//必须分配2个字节,是为了兼容protoCDO
		buffer.put((byte)FieldType.NULL_TYPE);	
		super.toAvro(prefixField, fieldMap);
	}	
	@Override
	public void toProto(String prefixField,GoogleCDO.CDOProto.Builder cdoProto){
		buffer=ByteBuffer.allocate(2);//必须分配2个字节,是为了兼容protoCDO反序列化
		buffer.put((byte)FieldType.NULL_TYPE);	
		super.toProto(prefixField, cdoProto);
	}
	//公共方法,所有可提供外部使用的函数在此定义为public方法------------------------------------------------------
	@Override
	public void toXML(StringBuilder strbXML)
	{
		strbXML.append("<NullF N=\"").append(this.getName()).append("\"/>");
		
	}
	@Override
	public void toXMLWithIndent(int nIndentSize,StringBuilder strbXML)
	{

		String strIndent=Utility.makeSameCharString('\t',nIndentSize);		
		strbXML.append(strIndent).append("<NullF N=\"").append(this.getName()).append("\"/>\r\n");
		
	}

	@Override
	public String toJSON()
	{

		return "";
	}
	@Override
	public String toHtmlJSON()
	{

		return "";
	}	
	
	public String toMixHtmlJSON(){

		return "";
		
	}
	
	@Override
	public String toString()
	{		
		StringBuilder strJSON=new StringBuilder();
		strJSON.append("\"").append(this.getName()).append("\"").append(":\"").append("NULL TYPE Field").append("\",");
		return strJSON.toString();	
	}

	//构造函数,所有构造函数在此定义------------------------------------------------------------------------------
	public NullField(String strFieldName){	
		super(strFieldName);		
		setFieldType(type.NULL);						
		setValue();		
	}

}
