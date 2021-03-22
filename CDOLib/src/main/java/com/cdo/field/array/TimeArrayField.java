package com.cdo.field.array;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;

import com.cdoframework.cdolib.base.DataType;
import com.cdoframework.cdolib.base.Utility;
import com.cdoframework.cdolib.data.cdo.DataBufferUtil;

/**
 * 重新构造
 * @author KenelLiu
 *
 */
public class TimeArrayField extends ArrayFieldImpl
{

	//内部类,所有内部类在此声明----------------------------------------------------------------------------------

	//静态对象,所有static在此声明并初始化------------------------------------------------------------------------

	//内部对象,所有在本类中创建并使用的对象在此声明--------------------------------------------------------------

	private static final long serialVersionUID = -9060053020629106038L;
	//属性对象,所有在本类中创建，并允许外部访问的对象在此声明并提供get/set方法-----------------------------------
	private final int dataIndex=1;//数据保存的起始位置
	private final int databuffer=8;//数据占用字节

	public void setValue(String[] strsValue)
	{
		if(strsValue==null)
		{
			strsValue=new String[0];
		}
		long[] lsValue=new long[strsValue.length];
		for(int i=0;i<strsValue.length;i++)
		{
			try{
				lsValue[i]=java.sql.Time.valueOf(strsValue[i]).getTime();
			}catch(Exception ex){
				throw new RuntimeException("arr index="+i+",["+strsValue[i]+"] Invalid date or Invalid date format,date format must is "+PATTERN_TIME);
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
		SimpleDateFormat sdf=new SimpleDateFormat(PATTERN_TIME);
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
		SimpleDateFormat sdf=new SimpleDateFormat(PATTERN_TIME);
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
			v=java.sql.Time.valueOf(strValue).getTime();
		}catch(Exception ex){
			throw new RuntimeException("arr index="+nIndex+",["+strValue+"] Invalid date or Invalid date format,date format must is "+PATTERN_TIME);
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

		buffer=DataBufferUtil.allocate(lsValue.length, DataType.TIME_ARRAY_TYPE, buffer, dataIndex, databuffer);
		//设置起始位置  
		buffer.position(dataIndex);
		for(int i=0;i<lsValue.length;i++){
			buffer.putLong(lsValue[i]);
		}
		buffer.flip();
	}	
	//引用对象,所有在外部创建并传入使用的对象在此声明并提供set方法-----------------------------------------------

	//内部方法,所有仅在本类或派生类中使用的函数在此定义为protected方法-------------------------------------------

	//公共方法,所有可提供外部使用的函数在此定义为public方法------------------------------------------------------
	@Override
	public void toXML(StringBuilder strbXML)
	{
		String[] strsValue=getValue();
		strbXML.append("<TAF N=\"").append(this.getName()).append("\"");;
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
		String[] strsValue=getValue();
		String strIndent=Utility.makeSameCharString('\t',nIndentSize);

		strbXML.append(strIndent).append("<TAF N=\"").append(this.getName()).append("\"");
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
		for(int i=0;i<strsValue.length;i=i+1)
		{
			String _sign=(i==_length-1)?"\"":"\",";
			str_JSON.append("\"").append(strsValue[i]).append(_sign);
		}
		str_JSON.append("],");
		return str_JSON.toString();
	}

	//接口实现,所有实现接口函数的实现在此定义--------------------------------------------------------------------

	//事件处理,所有重载派生类的事件类方法(一般为on...ed)在此定义-------------------------------------------------

	//事件定义,所有在本类中定义并调用，由派生类实现或重载的事件类方法(一般为on...ed)在此定义---------------------

	//构造函数,所有构造函数在此定义------------------------------------------------------------------------------

	public TimeArrayField(String strFieldName)
	{

		//请在此加入初始化代码,内部对象和属性对象负责创建或赋初值,引用对象初始化为null，初始化完成后在设置各对象之间的关系
		super(strFieldName);
		
		setType(Data.TIME_ARRAY);
		
		setLongValue(new long[0]);
	}

	public TimeArrayField(String strFieldName,String[] strsValue)
	{

		//请在此加入初始化代码,内部对象和属性对象负责创建或赋初值,引用对象初始化为null，初始化完成后在设置各对象之间的关系
		super(strFieldName);
		
		setType(Data.TIME_ARRAY);
		
		setValue(strsValue);
	}
	public TimeArrayField(String strFieldName,long[] lsValue)
	{

		//请在此加入初始化代码,内部对象和属性对象负责创建或赋初值,引用对象初始化为null，初始化完成后在设置各对象之间的关系
		super(strFieldName);
		
		setType(Data.TIME_ARRAY);
		
		setLongValue(lsValue);
	}
	
	 public TimeArrayField(String strFieldName,ByteBuffer buffer)
	 {
			super(strFieldName);
			
			setType(Data.TIME_ARRAY);
			
			this.buffer=buffer;
	}
}
