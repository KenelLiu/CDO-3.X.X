package com.cdo.field.array;

import java.nio.Buffer;
import java.nio.ByteBuffer;

import com.cdo.field.FieldType;
import com.cdoframework.cdolib.data.cdo.DataBufferUtil;
import com.cdoframework.cdolib.util.Utility;

/**
 * 定义Long数组字段
 * @author KenelLiu
 *
 */
public class LongArrayField extends ArrayFieldImpl{

	private static final long serialVersionUID = -4517167042801694678L;
	//属性对象,所有在本类中创建，并允许外部访问的对象在此声明并提供get/set方法-----------------------------------
	private final int dataIndex=1;//数据保存的起始位置
	private final int databuffer=8;//数据占用字节
	public void setValue(long[] lsValue)
	{
		if(lsValue==null)
		{
			lsValue=new long[0];
		}
		allocate(lsValue);
	}
	public long[] getValue()
	{				
		int len=getLength();
		long[] result=new long[len];
		buffer.position(dataIndex);
		for(int i=0;i<result.length;i++){			
			result[i]=buffer.getLong();
		}
		buffer.clear();
		return result;
	}

	public long getValueAt(int nIndex)
	{
		checkArrayIndex(nIndex);
		
		int pos=dataIndex+databuffer*nIndex;
		buffer.position(pos);
		long v=buffer.getLong();
		buffer.clear();
		return v;
	}

	public void setValueAt(int nIndex,long lValue)
	{
		checkArrayIndex(nIndex);
		
		int pos=dataIndex+databuffer*nIndex;
		buffer.position(pos);
		buffer.putLong(lValue);		
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
		return new Long(getValueAt(nIndex));
	}
	@Override
	public Buffer getBuffer() {	
		return buffer;
	}

	private ByteBuffer allocate(long[] nsValue){
		buffer=DataBufferUtil.allocate(nsValue.length, FieldType.LONG_ARRAY_TYPE, buffer, dataIndex, databuffer);
		//设置起始位置  
		buffer.position(dataIndex);
		for(int i=0;i<nsValue.length;i++){
			buffer.putLong(nsValue[i]);
		}
		buffer.flip();
		return buffer;
	}
	
	//公共方法,所有可提供外部使用的函数在此定义为public方法------------------------------------------------------
	@Override
	public void toXML(StringBuilder strbXML)
	{
		long[] lsValue=getValue();
		strbXML.append("<LAF N=\"").append(this.getName()).append("\"");;
		strbXML.append(" V=\"");
		for(int i=0;i<lsValue.length;i=i+1)
		{
			if(i>0)
			{
				strbXML.append(",");	
			}
			strbXML.append(lsValue[i]);	
		}
		strbXML.append("\"/>");
	}
	@Override
	public void toXMLWithIndent(int nIndentSize,StringBuilder strbXML)
	{
		long[] lsValue=getValue();
		String strIndent=Utility.makeSameCharString('\t',nIndentSize);

		strbXML.append(strIndent).append("<LAF N=\"").append(this.getName()).append("\"");
		strbXML.append(" V=\"");
		for(int i=0;i<lsValue.length;i=i+1)
		{		
			if(i>0)
			{
				strbXML.append(",");	
			}
			strbXML.append(lsValue[i]);				
		}
		strbXML.append("\"/>\r\n");
	}	
	@Override
	public String toJSON()
	{
		long[] lsValue=getValue();
		StringBuffer str_JSON=new StringBuffer();
		str_JSON.append("\"").append(this.getName()).append("\"").append(":").append("[");
		int _length=lsValue.length;
		for(int i=0;i<lsValue.length;i=i+1)
		{
			String _sign=(i==_length-1)?"":",";
			str_JSON.append("").append(lsValue[i]).append(_sign);
		}
		str_JSON.append("],");
		return str_JSON.toString();
	}

	//构造函数,所有构造函数在此定义------------------------------------------------------------------------------
	public LongArrayField(String strFieldName){
		super(strFieldName);		
		setFieldType(type.LONG_ARRAY);
		setValue(new long[0]);
	}

	public LongArrayField(String strFieldName,long[] lsValue){
		super(strFieldName);		
		setFieldType(type.LONG_ARRAY);		
		if(lsValue==null)
		{
			lsValue=new long[0];
		}
		setValue(lsValue);
	}

	public LongArrayField(String strFieldName,ByteBuffer buffer){
		super(strFieldName);		
		setFieldType(type.LONG_ARRAY);		
		this.buffer=buffer;
	}
}
