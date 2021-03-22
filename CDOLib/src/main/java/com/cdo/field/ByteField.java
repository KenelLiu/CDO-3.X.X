
package com.cdo.field;

import java.nio.ByteBuffer;
import java.util.Map;

import com.cdo.google.GoogleCDO;
import com.cdoframework.cdolib.base.DataType;
import com.cdoframework.cdolib.base.Utility;
/**
 * 重新构造
 * @author KenelLiu
 *
 */
public class ByteField extends FieldImpl
{

	//内部类,所有内部类在此声明----------------------------------------------------------------------------------

	//静态对象,所有static在此声明并初始化------------------------------------------------------------------------

	//内部对象,所有在本类中创建并使用的对象在此声明--------------------------------------------------------------

	/**
	 * 
	 */
	private static final long serialVersionUID = 1440959538120435270L;
	//属性对象,所有在本类中创建，并允许外部访问的对象在此声明并提供get/set方法-----------------------------------
	private byte byValue;
	public void setValue(byte byValue)
	{
		this.byValue=byValue;
	}
	public byte getValue()
	{
		return this.byValue;
	}

	private void allocate(){
		int len=1+1;//字段类型所占字节+数据所占字节
		buffer=ByteBuffer.allocate(len);
		buffer.put((byte)DataType.BYTE_TYPE);
		buffer.put(this.byValue);
		buffer.flip();
	}
	//引用对象,所有在外部创建并传入使用的对象在此声明并提供set方法-----------------------------------------------

	//内部方法,所有仅在本类或派生类中使用的函数在此定义为protected方法-------------------------------------------

	//公共方法,所有可提供外部使用的函数在此定义为public方法------------------------------------------------------
	@Override
	public void toAvro(String prefixField,Map<CharSequence,ByteBuffer> fieldMap){
		allocate();		
		super.toAvro(prefixField, fieldMap);
	}	
	@Override
	public void toProto(String prefixField,GoogleCDO.CDOProto.Builder cdoProto){
		allocate();	
		super.toProto(prefixField, cdoProto);
	}
	@Override
	public void toXML(StringBuilder strbXML)
	{		
		strbXML.append("<BYF N=\"").append(this.getName()).append("\"");
		strbXML.append(" V=\"").append(this.byValue).append("\"/>");
	}
	@Override
	public void toXMLWithIndent(int nIndentSize,StringBuilder strbXML)
	{
		String strIndent=Utility.makeSameCharString('\t',nIndentSize);

		strbXML.append(strIndent).append("<BYF N=\"").append(this.getName()).append("\"");
		strbXML.append(" V=\"").append(this.byValue).append("\"/>\r\n");
	}

	@Override
	public String toJSON()
	{
		StringBuffer str_JSON=new StringBuffer();
		str_JSON.append("\"").append(this.getName()).append("\"").append(":").append(this.byValue).append(",");
		return str_JSON.toString();
	}	
	
	@Override
	public Object getObjectValue()
	{
		return new Byte(byValue);
	}


	//接口实现,所有实现接口函数的实现在此定义--------------------------------------------------------------------

	//事件处理,所有重载派生类的事件类方法(一般为on...ed)在此定义-------------------------------------------------

	//事件定义,所有在本类中定义并调用，由派生类实现或重载的事件类方法(一般为on...ed)在此定义---------------------

	//构造函数,所有构造函数在此定义------------------------------------------------------------------------------

	public ByteField(String strFieldName)
	{

		//请在此加入初始化代码,内部对象和属性对象负责创建或赋初值,引用对象初始化为null，初始化完成后在设置各对象之间的关系
		setType(Data.BYTE);
		
		this.byValue	=0;
	}

	public ByteField(String strFieldName,byte byValue)
	{

		//请在此加入初始化代码,内部对象和属性对象负责创建或赋初值,引用对象初始化为null，初始化完成后在设置各对象之间的关系
		super(strFieldName);
		
		setType(Data.BYTE);
		
		this.byValue	=byValue;
	}
	
	public ByteField(byte byValue)
	{

		//请在此加入初始化代码,内部对象和属性对象负责创建或赋初值,引用对象初始化为null，初始化完成后在设置各对象之间的关系				
		setType(Data.BYTE);
		
		this.byValue	=byValue;
	}


}
