
package com.cdoframework.cdolib.data.cdo;

import java.nio.ByteBuffer;
import java.util.Map;

import com.cdo.field.FieldImpl;
import com.cdo.google.GoogleCDO;
import com.cdoframework.cdolib.util.Utility;
/**
 * 定义CDO字段
 * @author KenelLiu
 */
public class CDOField extends FieldImpl{

	private static final long serialVersionUID = 1633490038117631870L;
	//属性对象,所有在本类中创建，并允许外部访问的对象在此声明并提供get/set方法-----------------------------------
	private CDO cdoValue;
	public void setValue(CDO cdoValue)
	{
		this.cdoValue=cdoValue;
	}
	public CDO getValue()
	{
		return this.cdoValue;
	}
	//公共方法,所有可提供外部使用的函数在此定义为public方法------------------------------------------------------	
	@Override
	public void toAvro(String prefixField,Map<CharSequence,ByteBuffer> fieldMap){				
		String prefix=prefixField+this.getName()+".";
		this.cdoValue.toAvro(prefix,fieldMap);		
	}

	@Override
	public void toProto(String prefixField,GoogleCDO.CDOProto.Builder cdoProto){
		String prefix=prefixField+this.getName()+".";
		this.cdoValue.toProto(prefix,cdoProto);	
	}
	
	@Override
	public void toXML(StringBuilder strbXML)
	{
		strbXML.append("<CDOF N=\"").append(this.getName()).append("\">");
		this.cdoValue.toXML(strbXML);
		strbXML.append("</CDOF>");
	}
	
	@Override
	public void toXMLWithIndent(int nIndentSize,StringBuilder strbXML)
	{
		String strIndent=Utility.makeSameCharString('\t',nIndentSize);

		strbXML.append(strIndent).append("<CDOF N=\"").append(this.getName()).append("\">\r\n");
		this.cdoValue.toXMLWithIndent("\t",strbXML);
		strbXML.append(strIndent).append("</CDOF>\r\n");
	}
	
	@Override
	public String toJSON()
	{
		StringBuffer str_JSON=new StringBuffer();
		str_JSON.append("\"").append(this.getName()).append("\"").append(":").append(this.cdoValue.toJSON())
						.append(",");
		return str_JSON.toString();
	}

	public Object getObjectValue()
	{
		return cdoValue;
	}

	
	
	@Override
	public void release(){				
		this.cdoValue.deepRelease();
	}

	//构造函数,所有构造函数在此定义------------------------------------------------------------------------------
	public CDOField(String strFieldName){
		super(strFieldName);		
		setFieldType(type.CDO);		
		this.cdoValue	=null;
	}

	public CDOField(String strFieldName,CDO cdoValue){
		super(strFieldName);		
		setFieldType(type.CDO);		
		this.cdoValue	=cdoValue;
	}


	public CDOField(CDO cdoValue){
		setFieldType(type.CDO);		
		this.cdoValue=cdoValue;
	}



}
