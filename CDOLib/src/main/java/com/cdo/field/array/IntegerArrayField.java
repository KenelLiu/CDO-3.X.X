package com.cdo.field.array;

import java.nio.Buffer;
import java.nio.ByteBuffer;

import com.cdo.field.FieldType;
import com.cdoframework.cdolib.data.cdo.DataBufferUtil;
import com.cdoframework.cdolib.util.Utility;

/**
 * 定义Integer数组字段
 * @author KenelLiu
 *
 */
public class IntegerArrayField extends ArrayFieldImpl{
	private static final long serialVersionUID = 308140565180695337L;
	//属性对象,所有在本类中创建，并允许外部访问的对象在此声明并提供get/set方法-----------------------------------
	private final int dataIndex=1;//数据保存的起始位置
	private final int databuffer=4;//数据占用字节
	
	public void setValue(int[] nsValue)
	{
		if(nsValue==null)
		{
			nsValue=new int[0];
		}
		allocate(nsValue);
	}
	public int[] getValue()
	{
		int len=getLength();
		int[] result=new int[len];
		buffer.position(dataIndex);
		for(int i=0;i<result.length;i++){			
			result[i]=buffer.getInt();
		}
		buffer.clear();
		return result;
	}

	public int getValueAt(int nIndex)
	{
		checkArrayIndex(nIndex);
		
		int pos=dataIndex+databuffer*nIndex;
		buffer.position(pos);
		int v=buffer.getInt();
		buffer.clear();
		return v;
	}

	public void setValueAt(int nIndex,int nValue)
	{
		checkArrayIndex(nIndex);
		
		int pos=dataIndex+databuffer*nIndex;
		buffer.position(pos);
		buffer.putInt(nValue);
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
		return new Integer(getValueAt(nIndex));
	}	
	
	@Override
	public Buffer getBuffer() {	
		return buffer;
	}

	private ByteBuffer allocate(int[] nsValue){
		buffer=DataBufferUtil.allocate(nsValue.length, FieldType.INTEGER_ARRAY_TYPE, buffer, dataIndex, databuffer);
		//设置起始位置  
		buffer.position(dataIndex);
		for(int i=0;i<nsValue.length;i++){
			buffer.putInt(nsValue[i]);
		}
		buffer.flip();
		return buffer;
	}		
	//公共方法,所有可提供外部使用的函数在此定义为public方法------------------------------------------------------
	@Override
	public void toXML(StringBuilder strbXML)
	{
		int[] nsValue=getValue();
		strbXML.append("<NAF N=\"").append(this.getName()).append("\"");;
		strbXML.append(" V=\"");
		for(int i=0;i<nsValue.length;i=i+1)
		{
			if(i>0)
			{
				strbXML.append(",");	
			}
			strbXML.append(nsValue[i]);	
		}
		strbXML.append("\"/>");
	}
	@Override
	public void toXMLWithIndent(int nIndentSize,StringBuilder strbXML)
	{
		int[] nsValue=getValue();
		String strIndent=Utility.makeSameCharString('\t',nIndentSize);

		strbXML.append(strIndent).append("<NAF N=\"").append(this.getName()).append("\"");
		strbXML.append(" V=\"");
		for(int i=0;i<nsValue.length;i=i+1)
		{		
			if(i>0)
			{
				strbXML.append(",");	
			}
			strbXML.append(nsValue[i]);				
		}
		strbXML.append("\"/>\r\n");
	}
	@Override
	public String toJSON()
	{
		int[] nsValue=getValue();
		StringBuffer str_JSON=new StringBuffer();
		str_JSON.append("\"").append(this.getName()).append("\"").append(":").append("[");
		int _length=nsValue.length;
		for(int i=0;i<nsValue.length;i=i+1)
		{
			String _sign=(i==_length-1)?"":",";
			str_JSON.append("").append(nsValue[i]).append(_sign);
		}
		str_JSON.append("],");
		return str_JSON.toString();
	}
	
	//构造函数,所有构造函数在此定义------------------------------------------------------------------------------
	public IntegerArrayField(String strFieldName){
		super(strFieldName);		
		setFieldType(type.INTEGER_ARRAY);
		setValue(new int[0]);
	}

	public IntegerArrayField(String strFieldName,int[] nsValue){
		super(strFieldName);		
		setFieldType(type.INTEGER_ARRAY);		
		if(nsValue==null)
		{
			nsValue=new int[0];
		}
		setValue(nsValue);
	}
	
	public IntegerArrayField(String strFieldName,ByteBuffer buffer){
		super(strFieldName);		
		setFieldType(type.INTEGER_ARRAY);		
		this.buffer=buffer;
	}

}
