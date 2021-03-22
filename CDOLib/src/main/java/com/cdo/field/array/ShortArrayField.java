package com.cdo.field.array;

import java.nio.Buffer;
import java.nio.ByteBuffer;

import com.cdo.field.FieldType;
import com.cdoframework.cdolib.data.cdo.DataBufferUtil;
import com.cdoframework.cdolib.util.Utility;

/**
 * 定义short数组字段
 * @author KenelLiu
 *
 */
public class ShortArrayField extends ArrayFieldImpl{

	private static final long serialVersionUID = 2227539383025705302L;
	//属性对象,所有在本类中创建，并允许外部访问的对象在此声明并提供get/set方法-----------------------------------
	private final int dataIndex=1;//数据保存的起始位置
	private final int databuffer=2;//数据占用字节
	
	public void setValue(short[] shsValue)
	{
		if(shsValue==null)
		{
			shsValue=new short[0];
		}
		allocate(shsValue);
	}
	public short[] getValue()
	{				
		int len=getLength();
		short[] result=new short[len];
		buffer.position(dataIndex);
		for(int i=0;i<result.length;i++){			
			result[i]=buffer.getShort();
		}
		buffer.clear();
		return result;
	}

	public short getValueAt(int nIndex)
	{
		checkArrayIndex(nIndex);
		
		int pos=dataIndex+databuffer*nIndex;
		buffer.position(pos);
		short v=buffer.getShort();
		buffer.clear();
		return v;
	}

	public void setValueAt(int nIndex,short shValue)
	{
		checkArrayIndex(nIndex);
		
		int pos=dataIndex+databuffer*nIndex;
		buffer.position(pos);
		buffer.putShort(shValue);
		buffer.clear();
	}
	
	public int getLength()
	{
		return (buffer.capacity()-dataIndex)/databuffer;
	}

	public Object getObjectValue()
	{
		return getValue();
	}

	public Object getObjectValueAt(int nIndex)
	{
		return new Short(getValueAt(nIndex));
	}
	
	@Override
	public Buffer getBuffer() {	
		return buffer;
	}

	private ByteBuffer allocate(short[] shsValue){
		buffer=DataBufferUtil.allocate(shsValue.length, FieldType.SHORT_ARRAY_TYPE, buffer, dataIndex, databuffer);
		//设置起始位置  
		buffer.position(dataIndex);
		for(int i=0;i<shsValue.length;i++){
			buffer.putShort(shsValue[i]);
		}
		buffer.flip();
		return buffer;
	}	
	//公共方法,所有可提供外部使用的函数在此定义为public方法------------------------------------------------------
	@Override
	public void toXML(StringBuilder strbXML)
	{
		short[] shsValue=getValue();
		strbXML.append("<SAF N=\"").append(this.getName()).append("\"");;
		strbXML.append(" V=\"");
		for(int i=0;i<shsValue.length;i=i+1)
		{
			if(i>0)
			{
				strbXML.append(",");	
			}
			strbXML.append(shsValue[i]);	
		}
		strbXML.append("\"/>");
	}
	@Override
	public void toXMLWithIndent(int nIndentSize,StringBuilder strbXML)
	{
		short[] shsValue=getValue();
		String strIndent=Utility.makeSameCharString('\t',nIndentSize);

		strbXML.append(strIndent).append("<SAF N=\"").append(this.getName()).append("\"");
		strbXML.append(" V=\"");
		for(int i=0;i<shsValue.length;i=i+1)
		{		
			if(i>0)
			{
				strbXML.append(",");	
			}
			strbXML.append(shsValue[i]);				
		}
		strbXML.append("\"/>\r\n");
	}	
	@Override
	public String toJSON()
	{
		short[] shsValue=getValue();
		StringBuffer str_JSON=new StringBuffer();
		str_JSON.append("\"").append(this.getName()).append("\"").append(":").append("[");
		int _length=shsValue.length;
		for(int i=0;i<shsValue.length;i=i+1)
		{
			String _sign=(i==_length-1)?"":",";
			str_JSON.append("").append(shsValue[i]).append(_sign);
		}
		str_JSON.append("],");
		return str_JSON.toString();
	}
	//构造函数,所有构造函数在此定义------------------------------------------------------------------------------
	public ShortArrayField(String strFieldName){
		super(strFieldName);		
		setFieldType(type.SHORT_ARRAY);
		setValue(new short[0]);
	}

	public ShortArrayField(String strFieldName,short[] shsValue){
		super(strFieldName);		
		setFieldType(type.SHORT_ARRAY);		
		if(shsValue==null)
		{
			shsValue=new short[0];
		}
		setValue(shsValue);
	}

	public ShortArrayField(String strFieldName,ByteBuffer buffer){
		super(strFieldName);		
		setFieldType(type.SHORT_ARRAY);		
		this.buffer=buffer;
	}

}
