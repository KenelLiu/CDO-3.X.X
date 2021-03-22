package com.cdo.field;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import com.cdoframework.cdolib.util.Function;
import com.cdoframework.cdolib.util.Utility;

/**
 * 定义shring字段
 * @author KenelLiu
 *
 */
public class StringField extends FieldImpl{

	private static final long serialVersionUID = 5971309765452721210L;
	//属性对象,所有在本类中创建，并允许外部访问的对象在此声明并提供get/set方法-----------------------------------
	private final int dataIndex=1;//数据保存的起始位置
	private int databuffer=0;//数据占用字节   随字符串UTF8长度 而改变
	private int preDataBuffer=0;//数据未改变之前 所占用字节
	
	public void setValue(String strValue)
	{
		if(strValue==null)
		{
			strValue="";
		}
		allocate(strValue);
	}
	
	public String getValue()
	{
		buffer.position(dataIndex);
		ByteBuffer slice = buffer.slice();
		byte[] dst=new byte[slice.capacity()];
		slice.get(dst);
		buffer.clear();
		return new String(dst,Charset.forName("UTF-8"));
	}

	public Object getObjectValue()
	{
		return getValue();
	}	
	@Override
	public Buffer getBuffer() {	
		return buffer;
	}

	private  void allocateBuffer(){
		int len=dataIndex+databuffer;
		buffer=ByteBuffer.allocate(len);
		buffer.put((byte)FieldType.STRING_TYPE);
			
	}
	
	private void allocate(String strValue){
		byte[] strByte=strValue.getBytes(Charset.forName("UTF-8"));
		databuffer=strByte.length;
		if(buffer==null){
			allocateBuffer();
		}else{			
			if(preDataBuffer<databuffer){
				//最新字符串 长度大于之前的长度
				allocateBuffer();
			}else if(preDataBuffer>databuffer){
				//最新字符串 长度小于原来字符串长度  截取原长度的一部分 作为本次数据存放,不需要重新分配内存
				int len=dataIndex+databuffer;
				buffer.position(0);
				buffer.limit(len);
				buffer=buffer.slice();
			}
			//若相等，数据进行覆盖即可;
		}		
		buffer.position(dataIndex);
		buffer.put(strByte);
		buffer.flip();
		//设置字符串长度，当字符串改变 可以进行比较
		preDataBuffer=databuffer;
	}	
	//公共方法,所有可提供外部使用的函数在此定义为public方法------------------------------------------------------
	@Override
	public void toXML(StringBuilder strbXML)
	{
		String strValue=getValue();
		strbXML.append("<STRF N=\"").append(this.getName()).append("\"");
		strbXML.append(" V=\"").append(Function.FormatTextForXML(strValue)).append("\"/>");
	}
	@Override
	public void toXMLWithIndent(int nIndentSize,StringBuilder strbXML)
	{
		String strValue=getValue();
		String strIndent=Utility.makeSameCharString('\t',nIndentSize);
		
		strbXML.append(strIndent).append("<STRF N=\"").append(this.getName()).append("\"");
		strbXML.append(" V=\"").append(Function.FormatTextForXML(strValue)).append("\"/>\r\n");
	}

	@Override
	public String toJSON()
	{
		String strValue=getValue();
		StringBuffer str_JSON=new StringBuffer();
		str_JSON.append("\"").append(this.getName()).append("\"").append(":\"").append(Function.FormatTextForJson(strValue)).append("\",");
		return str_JSON.toString();
	}
	@Override
	public String toHtmlJSON()
	{
		String strValue=getValue();
		StringBuffer str_JSON=new StringBuffer();
		strValue=Function.FormatTextForHTML(strValue);
		str_JSON.append("\"").append(this.getName()).append("\"").append(":\"").append(strValue).append("\",");
		return str_JSON.toString();
	}	
	
	public String toMixHtmlJSON(){
		String strValue=getValue();
		StringBuffer str_JSON=new StringBuffer();
		strValue=Function.FormatTextForMixHTMLJSON(strValue);
		str_JSON.append("\"").append(this.getName()).append("\"").append(":\"").append(strValue).append("\",");
		return str_JSON.toString();
		
	}
	
	@Override
	public String toString()
	{		
		String strValue=getValue();
		if(strValue.length()>50){
			strValue=strValue.substring(0, 50)+"......";
		}
		StringBuffer str_JSON=new StringBuffer();
		str_JSON.append("\"").append(this.getName()).append("\"").append(":\"").append(Function.FormatTextForJson(strValue)).append("\",");
		return str_JSON.toString();	
	}

	//构造函数,所有构造函数在此定义------------------------------------------------------------------------------
	public StringField(String strFieldName){	
		super(strFieldName);		
		setFieldType(type.STRING);						
		setValue("");		
	}

	public StringField(String strFieldName,String strValue){
		super(strFieldName);		
		setFieldType(type.STRING);		
		if(strValue==null)
		{
			strValue="";
		}			
		setValue(strValue);
	}
	
	 public StringField(String strFieldName,ByteBuffer buffer){
		super(strFieldName);		
		setFieldType(type.STRING);		
		this.buffer=buffer;
	}
}
