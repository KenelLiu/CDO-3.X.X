package com.cdo.field.array;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;

import com.cdo.field.FieldType;
import com.cdoframework.cdolib.data.cdo.DataBufferUtil;
import com.cdoframework.cdolib.util.Utility;

/**
 * 定义date数组字段
 * @author KenelLiu
 *
 */
public class DateArrayField extends ArrayFieldImpl{
	private static final long serialVersionUID = -6305770982050527951L;
	//属性对象,所有在本类中创建，并允许外部访问的对象在此声明并提供get/set方法-----------------------------------
	private final int dataIndex=1;//数据保存的起始位置
	private final int databuffer=8;//数据占用字节
	
	public void setValue(String[] strsValue){
		if(strsValue==null){
			strsValue=new String[0];
		}
		long[] lsValue=new long[strsValue.length];
		SimpleDateFormat sdf=new SimpleDateFormat(PATTERN_DATE);
		for(int i=0;i<strsValue.length;i++)
		{
			try{
				lsValue[i]=sdf.parse(strsValue[i]).getTime();
			}catch(Exception ex){
				throw new RuntimeException("arr index="+i+",["+strsValue[i]+"] Invalid date or Invalid date format,date format must is "+PATTERN_DATE);
			}
		}
		allocate(lsValue);
	}
	
	public void setLongValue(long[] lsValue){
		if(lsValue==null){
			lsValue=new long[0];
		}
		allocate(lsValue);
	}
	
	public String[] getValue()
	{
		int len=getLength();
		String[] result=new String[len];
		buffer.position(dataIndex);
		SimpleDateFormat sdf=new SimpleDateFormat(PATTERN_DATE);
		for(int i=0;i<result.length;i++){			
			result[i]=sdf.format(new java.util.Date(buffer.getLong()));
		}
		buffer.clear();
		return result;
		
	}
	public long[] getLongValue()
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


	public String getValueAt(int nIndex)
	{	
		long v=getLongValueAt(nIndex);
		SimpleDateFormat sdf=new SimpleDateFormat(PATTERN_DATE);
		return sdf.format(new java.util.Date(v));
	}

	public long getLongValueAt(int nIndex)
	{
		checkArrayIndex(nIndex);
		
		int pos=dataIndex+databuffer*nIndex;
		buffer.position(pos);
		long v=buffer.getLong();
		buffer.clear();
		return v;
	}
	
	public void setValueAt(int nIndex,String strValue)
	{		
		long v=0;
		try{		
			SimpleDateFormat sdf=new SimpleDateFormat(PATTERN_DATE);
			v=sdf.parse(strValue).getTime();			
		}catch(Exception ex){
			throw new RuntimeException("arr index="+nIndex+",["+strValue+"] Invalid date or Invalid date format,date format must is "+PATTERN_DATE);
		}			
		
		setLongValueAt(nIndex, v);
	}
	
	public void setLongValueAt(int nIndex,long lValue)
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
		return getValueAt(nIndex);
	}
	
	@Override
	public Buffer getBuffer() {	
		return buffer;
	}
	
	private void allocate(long[] lsValue){

		buffer=DataBufferUtil.allocate(lsValue.length, FieldType.DATE_ARRAY_TYPE, buffer, dataIndex, databuffer);
		//设置起始位置  
		buffer.position(dataIndex);
		for(int i=0;i<lsValue.length;i++){
			buffer.putLong(lsValue[i]);
		}
		buffer.flip();
	}		
	//公共方法,所有可提供外部使用的函数在此定义为public方法------------------------------------------------------
	@Override
	public void toXML(StringBuilder strbXML)
	{
		String[] strsValue=getValue();
		strbXML.append("<DAF N=\"").append(this.getName()).append("\"");;
		strbXML.append(" V=\"");
		for(int i=0;i<strsValue.length;i=i+1)
		{
			if(i>0)
			{
				strbXML.append(",");	
			}
			strbXML.append(strsValue[i]);	
		}
		strbXML.append("\"/>");
	}
	@Override
	public void toXMLWithIndent(int nIndentSize,StringBuilder strbXML)
	{
		String strIndent=Utility.makeSameCharString('\t',nIndentSize);
		
		String[] strsValue=getValue();
		strbXML.append(strIndent).append("<DAF N=\"").append(this.getName()).append("\"");
		strbXML.append(" V=\"");
		for(int i=0;i<strsValue.length;i=i+1)
		{		
			if(i>0)
			{
				strbXML.append(",");	
			}
			strbXML.append(strsValue[i]);				
		}
		strbXML.append("\"/>\r\n");
	}
	
	@Override
	public String toJSON()
	{
		String[] strsValue=getValue();
		StringBuffer str_JSON=new StringBuffer();
		str_JSON.append("\"").append(this.getName()).append("\"").append(":").append("[");
		int _length=strsValue.length;
		for(int i=0;i<_length;i=i+1)
		{
			String _sign=(i==_length-1)?"\"":"\",";
			str_JSON.append("\"").append(strsValue[i]).append(_sign);
		}
		str_JSON.append("],");
		return str_JSON.toString();
	}	

	//构造函数,所有构造函数在此定义------------------------------------------------------------------------------
	public DateArrayField(String strFieldName){
		super(strFieldName);		
		setFieldType(type.DATE_ARRAY);				
		setLongValue(new long[0]);
	}

	public DateArrayField(String strFieldName,String[] strsValue){
		super(strFieldName);		
		setFieldType(type.DATE_ARRAY);		
		setValue(strsValue);
	}

	public DateArrayField(String strFieldName,long[] lsValue){
		super(strFieldName);		
		setFieldType(type.DATE_ARRAY);		
		setLongValue(lsValue);
	}
	
	 public DateArrayField(String strFieldName,ByteBuffer buffer){
		super(strFieldName);		
		setFieldType(type.DATE_ARRAY);		
		this.buffer=buffer;
	}
	
}
