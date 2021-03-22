
package com.cdoframework.cdolib.data.cdo;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cdo.field.FieldType;
import com.cdo.field.array.ArrayFieldImpl;
import com.cdo.google.GoogleCDO;
import com.cdoframework.cdolib.util.Function;
import com.cdoframework.cdolib.util.Utility;
import com.google.protobuf.ByteString;

/**
 * 定义CDO数组字段
 * @author KenelLiu
 *
 */
public class CDOArrayField extends ArrayFieldImpl{
	private static final long serialVersionUID = -1583510869570826742L;
	//属性对象,所有在本类中创建，并允许外部访问的对象在此声明并提供get/set方法-----------------------------------
	private List<CDO> cdosValue;
	public void setValue(List<CDO> cdosValue)
	{
		if(cdosValue==null)
		{
			cdosValue=new ArrayList<CDO>();
		}

		this.cdosValue=cdosValue;
	}

	public List<CDO> getValue()
	{
		return this.cdosValue;
	}

	public CDO getValueAt(int nIndex)
	{
		return cdosValue.get(nIndex);
	}

	public void setValueAt(int nIndex,CDO cdoValue)
	{
		if(cdoValue==null)
		{
			cdoValue=new CDO();
		}		
		cdosValue.set(nIndex, cdoValue);
	}
	
	public int getLength()
	{
		return cdosValue.size();
	}
	//公共方法,所有可提供外部使用的函数在此定义为public方法------------------------------------------------------
	@Override
	public void toAvro(String prefixField,Map<CharSequence,ByteBuffer> fieldMap){
		if(this.cdosValue.size()==0){
			//表示无数据											
			ByteBuffer buffer=ByteBuffer.allocate(2);
			buffer.put((byte)FieldType.EMPTY_CDO_ARRAY_TYPE);
			buffer.flip();
			fieldMap.put(prefixField+this.getName()+"[-1].EMPTY",buffer);
			return;
		}	
		for(int i=0;i<this.cdosValue.size();i=i+1){
			String prefix=prefixField+this.getName()+"["+i+"].";
			this.cdosValue.get(i).toAvro(prefix,fieldMap);			
		}
		
	}	
	
	
	@Override
	public void toProto(String prefixField,GoogleCDO.CDOProto.Builder cdoProto){
		if(this.cdosValue.size()==0){
			//表示无数据								
			GoogleCDO.CDOProto.Entry.Builder entry=GoogleCDO.CDOProto.Entry.newBuilder();
			ByteBuffer buffer=ByteBuffer.allocate(2);
			buffer.put((byte)FieldType.EMPTY_CDO_ARRAY_TYPE);
			entry.setName(prefixField+this.getName()+"[-1].EMPTY");
			entry.setValue(ByteString.copyFrom(buffer));
			buffer.flip();
			cdoProto.addFields(entry);
			return;
		}		
		for(int i=0;i<this.cdosValue.size();i=i+1){
			String prefix=prefixField+this.getName()+"["+i+"].";
			this.cdosValue.get(i).toProto(prefix,cdoProto);
		}
	}
	
	@Override
	public void toXML(StringBuilder strbXML)
	{
		strbXML.append("<CDOAF N=\"").append(this.getName()).append("\">");
		for(int i=0;i<this.cdosValue.size();i=i+1)
		{
			this.cdosValue.get(i).toXML(strbXML);
		}
		strbXML.append("</CDOAF>");
	}
	
	@Override
	public void toXMLWithIndent(int nIndentSize,StringBuilder strbXML)
	{
		String strIndent=Utility.makeSameCharString('\t',nIndentSize);

		strbXML.append(strIndent).append("<CDOAF N=\"").append(this.getName()).append("\">\r\n");
		for(int i=0;i<this.cdosValue.size();i=i+1)
		{
			this.cdosValue.get(i).toXMLWithIndent(strIndent,strbXML);
		}
		strbXML.append(strIndent).append("</CDOAF>\r\n");
	}
	
	@Override
	public String toJSON()
	{
		StringBuffer str_JSON=new StringBuffer();
		str_JSON.append("\"").append(this.getName()).append("\"").append(":").append("[");
		int _length=this.cdosValue.size();
		for(int i=0;i<_length;i=i+1)
		{
			String _sign=(i==_length-1)?"":",";
			str_JSON.append("").append(this.cdosValue.get(i).toJSON()).append(_sign);
		}
		str_JSON.append("],");
		return str_JSON.toString();
	}	
	
	@Override
	public String toHtmlJSON()
	{
		StringBuffer str_JSON=new StringBuffer();
		str_JSON.append("\"").append(this.getName()).append("\"").append(":").append("[");
		int _length=this.cdosValue.size();
		for(int i=0;i<_length;i=i+1)
		{
			String _sign=(i==_length-1)?"":",";
			str_JSON.append("").append(this.cdosValue.get(i).toHtmlJSON()).append(_sign);
		}
		str_JSON.append("],");
		return str_JSON.toString();
	}		
	
	public String toMixHtmlJSON(){
		StringBuffer str_JSON=new StringBuffer();
		str_JSON.append("\"").append(this.getName()).append("\"").append(":").append("[");
		int _length=this.cdosValue.size();
		for(int i=0;i<_length;i=i+1)
		{
			String _sign=(i==_length-1)?"":",";
			str_JSON.append("").append(this.cdosValue.get(i).toMixHtmlJSON()).append(_sign);
		}
		str_JSON.append("],");
		return str_JSON.toString();
		
	}
	
	
	public Object getObjectValue()
	{
		return cdosValue;
	}

	public Object getObjectValueAt(int nIndex)
	{
		return cdosValue.get(nIndex);
	}

	@Override
	public void release(){	
		for(int i=0;i<this.cdosValue.size();i=i+1){			
			this.cdosValue.get(i).deepRelease();
		}		
	}
	
	//构造函数,所有构造函数在此定义------------------------------------------------------------------------------
	public CDOArrayField(){
		super();		
		setFieldType(type.CDO_ARRAY);		
		this.cdosValue	=new ArrayList<CDO>();
	}

	public CDOArrayField(String strFieldName){
		super(strFieldName);		
		setFieldType(type.CDO_ARRAY);		
		this.cdosValue	=new ArrayList<CDO>();
	}

	public CDOArrayField(String strFieldName,List<CDO> cdosValue){
		super(strFieldName);		
		setFieldType(type.CDO_ARRAY);		
		if(cdosValue==null){
			cdosValue=new ArrayList<CDO>();
		}
		setValue(cdosValue);
	}
	
	public CDOArrayField(List<CDO> cdosValue){	
		setFieldType(type.CDO_ARRAY);		
		if(cdosValue==null)
		{
			cdosValue=new ArrayList<CDO>();
		}
		setValue(cdosValue);
	}
	

}
