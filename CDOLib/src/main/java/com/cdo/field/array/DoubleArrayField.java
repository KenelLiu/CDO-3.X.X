
package com.cdo.field.array;

import java.nio.Buffer;
import java.nio.ByteBuffer;

import com.cdo.field.FieldType;
import com.cdoframework.cdolib.data.cdo.DataBufferUtil;
import com.cdoframework.cdolib.util.Utility;
/**
 * 定义double数组字段
 * @author KenelLiu
 *
 */
public class DoubleArrayField extends ArrayFieldImpl{

	private static final long serialVersionUID = -2827948642997814796L;
	//属性对象,所有在本类中创建，并允许外部访问的对象在此声明并提供get/set方法-----------------------------------
	private final int dataIndex=1;//数据保存的起始位置
	private final int databuffer=8;//数据占用字节
	
	public void setValue(double[] dblValue)
	{
		if(dblValue==null)
		{
			dblValue=new double[0];
		}
		allocate(dblValue);
	}
	public double[] getValue()
	{
		buffer.position(1);
		int len=getLength();
		double[] result=new double[len];
		for(int i=0;i<result.length;i++){			
			result[i]=buffer.getDouble();
		}
		buffer.clear();
		return result;
	}

	public double getValueAt(int nIndex)
	{
		checkArrayIndex(nIndex);
		
		int pos=dataIndex+databuffer*nIndex;
		buffer.position(pos);
		double d=buffer.getDouble();
		buffer.clear();
		return d;
	}

	public void setValueAt(int nIndex,double dblValue)
	{	
		checkArrayIndex(nIndex);
		int pos=dataIndex+databuffer*nIndex;
		buffer.position(pos);
		buffer.putDouble(dblValue);
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
		return new Double(getValueAt(nIndex));
	}
	
	@Override
	public Buffer getBuffer() {	
		return buffer;
	}

	private ByteBuffer allocate(double[] dblsValue){
		buffer=DataBufferUtil.allocate(dblsValue.length, FieldType.DOUBLE_ARRAY_TYPE, buffer, dataIndex, databuffer);
		//设置起始位置  
		buffer.position(dataIndex);
		for(int i=0;i<dblsValue.length;i++){
			buffer.putDouble(dblsValue[i]);
		}
		buffer.flip();
		return buffer;
	}
	//公共方法,所有可提供外部使用的函数在此定义为public方法------------------------------------------------------		
	@Override
	public void toXML(StringBuilder strbXML)
	{
		double[] dblsValue=getValue();
		strbXML.append("<DBLAF N=\"").append(this.getName()).append("\"");;
		strbXML.append(" V=\"");
		for(int i=0;i<dblsValue.length;i=i+1)
		{
			if(i>0)
			{
				strbXML.append(",");	
			}
			strbXML.append(dblsValue[i]);	
		}
		strbXML.append("\"/>");
	}
	@Override
	public void toXMLWithIndent(int nIndentSize,StringBuilder strbXML)
	{
		double[] dblsValue=getValue();
		String strIndent=Utility.makeSameCharString('\t',nIndentSize);

		strbXML.append(strIndent).append("<DBLAF N=\"").append(this.getName()).append("\"");
		strbXML.append(" V=\"");
		for(int i=0;i<dblsValue.length;i=i+1)
		{		
			if(i>0)
			{
				strbXML.append(",");	
			}
			strbXML.append(dblsValue[i]);				
		}
		strbXML.append("\"/>\r\n");
	}	
	@Override
	public String toJSON()
	{
		double[] dblsValue=getValue();
		StringBuffer str_JSON=new StringBuffer();
		str_JSON.append("\"").append(this.getName()).append("\"").append(":").append("[");
		int _length=dblsValue.length;
		for(int i=0;i<dblsValue.length;i=i+1)
		{
			String _sign=(i==_length-1)?"":",";
			str_JSON.append("").append(dblsValue[i]).append(_sign);
		}
		str_JSON.append("],");
		return str_JSON.toString();
	}
	
	//构造函数,所有构造函数在此定义------------------------------------------------------------------------------
	public DoubleArrayField(String strFieldName){
		super(strFieldName);		
		setFieldType(type.DOUBLE_ARRAY);
		setValue(new double[0]);
	}

	public DoubleArrayField(String strFieldName,double[] dblValue){
		super(strFieldName);		
		setFieldType(type.DOUBLE_ARRAY);	
		if(dblValue==null)
		{
			dblValue=new double[0];
		}
		setValue(dblValue);
	}
	 
	public DoubleArrayField(String strFieldName,ByteBuffer buffer){
			super(strFieldName);			
			setFieldType(type.DOUBLE_ARRAY);
			this.buffer=buffer;
		}
}
