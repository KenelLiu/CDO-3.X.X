package com.cdo.field;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.Map;

import com.cdo.google.GoogleCDO;
/**
 * 采用java.nio.Buffer进行管理CDO字段
 * @author KenelLiu
 */
public interface Field extends FieldType{

	/**
	 * 设置字段类型
	 * @param data
	 */
	public void setFieldType(FieldType.type data);
	/**
	 * 获取字段类型
	 * @return
	 */
	public FieldType.type getFieldType();	
	/**
	 * 设置字段名称
	 * @param fieldName
	 */
	public void setName(String fieldName);
	/**
	 * 获取字段名称
	 * @return
	 */
	public String getName();
	
	/**
	 * 返回字段内容值
	 * @return
	 */
	public  Object getObjectValue();
	/**
	 * 转换成json格式
	 * @return
	 */
	public  String toJSON();
	
	/**
	 * 输出string,为json格式.
	 * @return
	 */
	public  String toString();
	
	/**
	 * 转换成json格式,其中value进行html编码
	 * @return
	 */
	public  String toHtmlJSON();	
	/**
	 * 转换成json格式,其中value同时进行 json转义及html编码
	 * @return
	 */
	public  String toMixHtmlJSON();
	/**
	 * 转换成 xml文件格式
	 * @param strbXML
	 */
	public void toXML(StringBuilder strbXML);
	/**
	 * 转换成有缩进格式的 xml文件格式
	 * @param strbXML
	 */
	public void toXMLWithIndent(int nIndentSize,StringBuilder strbXML);
	/**
	 * 转换成 apache avro格式
	 * @param prefixField
	 * @param fieldMap
	 */
	public void toAvro(String prefixField,Map<CharSequence,ByteBuffer> fieldMap);
	/**
	 * 转换成 google protobuf格式
	 * @param prefixField
	 * @param fieldMap
	 */
	public void toProto(String prefixField,GoogleCDO.CDOProto.Builder cdoProto);
	/**
	 * 释放对象
	 */
	public void release();	
	/**
	 * [CDOField,CDOArrayField]字段  由CDO复杂结构构成.
	 * CDO保存的数据 是由以下基础数据字段组成
	 * I.普通类型	II.普通数组类型[不包含String数组] III.String数组类型  IV.文件类型
	 * 
	 * 数据存储采用java.nio.Buffer存储字节,能有效的转化成 xml,json,avro,proto等格式,
	 * 方便CDO复杂结构数据的 序列化,反序列化.
	 * I 普通类型,存储格式: 字段类型-数据
	  	 boolean,short,int,long,float,double,date,dataTime,time,String
	           第一个字节  字段类型参数	
	   	  第二个字节 至 末尾为数据
	 	  每个数据 内容  所占字节如下：
	 	 boolean  1个字节
	     byte  1个字节
		 short 2个字节
		 int   4个字节
		 long  8个字节
		 float 4个字节
		 doulbe 8个字节
		 date   采用时间数字long存储 ,8个字节 
		 dateTime 采用时间数字long存储 ,8个字节 
		 time 采用时间数字long存储 ,8个字节 
		 String utf8实际占用的字节长度
	  
	   II 普通数组类型,存储格式:字段类型-数据-数据-数据-数据-数据-数据......	   
	      byte数组,boolean数组,short数组,int数组,long数组,float数组,double数组,date数组,dataTime数组,time数组 
	             第一个字节  字段类型参数	       
	             数组中的每个数据所占字节参考I
	                    
	   III String数组类型,存储格式:字段类型-数组长度-数据长度-数据内容-数据长度-数据内容....--数据长度--数据内容-.....
	                        第一个字节  字段类型参数    
	                        数组长度     占2个字节 为short型,数组最多为 (Short.MAX_VALUE)
	                        每个数据长度   占4个字节 为int 型,表示  实际数据内容的字节长度,最多有(Integer.MAX_VALUE)个UTF8字节         
	                        每个数据内容   utf8实际占用的字节长度
	   IV 文件类型  考虑到文件数据量大,不在这儿序列化,在传输中做字节流特别处理,且仅对CDO 最外层的文件类型 做传输
	             处理.而对于CDO里嵌套CDO包含的文件对象，考虑复杂度,性能,实际用途很少,则忽略掉文件传输.	                         	                                                                                   
	 */
	public  Buffer getBuffer();

}
