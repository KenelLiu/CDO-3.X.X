package com.cdoframework.cdolib.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StringReader;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import com.cdo.field.FieldType;
import com.cdo.pattern.Pattern;
import com.cdoframework.cdolib.base.Resources;

public class Utility{

	static protected DecimalFormat		decFormat       =new DecimalFormat();
	private static Logger logger=Logger.getLogger(Utility.class);
		
	/**
	 * 将指定字符串的每一行格式化成一个字符串
	 * 结果串中不包含回车符和换行符
	 * @param str
	 * @return
	 */
	public static String[] readLine(String str){		
		List<String> list = new ArrayList<String>(10);
		BufferedReader reader = new BufferedReader(new StringReader(str));
		String strContent = null;
		try{
			while ((strContent=reader.readLine()) !=null){
				list.add(strContent);
			}
		}catch(Exception e){
			return null;
		}finally{
			if(reader!=null){try{reader.close();}catch(Exception ex){}}				
		}
		return list.toArray(new String[list.size()]);		
	}
	/**
	 * 读取文本文件的内容
	 * @param strFile
	 * @param strCoding
	 * @return
	 */
	public static String readTextFile(String strFile){
		return readTextFile(strFile, null);
	}
	/**
	 * 读取文本文件的内容
	 * @param strFile
	 * @param strCoding
	 * @return
	 */
	public static String readTextFile(String strFile,String strCoding){
		FileInputStream stream=null;
		InputStreamReader reader=null;
		try
		{
			stream=new java.io.FileInputStream(strFile);
			if(strCoding!=null)
				reader=new InputStreamReader(stream,strCoding);
			else
				reader=new InputStreamReader(stream);
			
			char[] chsData=new char[(int)stream.getChannel().size()];
			int nReadSize=reader.read(chsData,0,chsData.length);	
			
			return new String(chsData,0,nReadSize);
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			return null;
		}finally{
			if(reader!=null){try{reader.close();}catch(Exception ex){}}
			if(stream!=null){try{stream.close();}catch(Exception ex){}}	
		}
	}
	/**
	 * 读取文件
	 * @param strFile
	 * @return
	 */
	public static String readTextResource(String strFile){
		return readTextResource(strFile, null);
	}	
	/**
	 * 读取文本Source的内容
	 * @param strFile 文件
	 * @param strCoding 编码
	 * @return
	 */
	public static String readTextResource(String strFile,String strCoding){
		return readTextResource(strFile, strCoding, true);
	}

	/**
	 * 读取文本Source的内容
	 * @param strFile
	 * @param strCoding
	 * @return
	 */
	public static String readTextResource(String strFile,String strCoding,boolean isClassPath)
	{
		InputStream stream=null;
		InputStreamReader reader=null;
		try
		{
			if(isClassPath){
				stream=Resources.getResourceAsStream(strFile);
			}else{
				stream=new FileInputStream(strFile);
			}
			if(strCoding!=null)
				reader=new InputStreamReader(stream,strCoding);
			else
				reader=new InputStreamReader(stream);

			StringBuilder strbContent=new StringBuilder();
			char[] chsData=new char[1024];
			while(true)
			{
				int nReadSize=reader.read(chsData,0,chsData.length);
				if(nReadSize<=0)
				{
					break;
				}
				strbContent.append(new String(chsData,0,nReadSize));
			}
			
			return strbContent.toString();
		}
		catch(Exception e){
			logger.error("strFile:"+strFile+",strCoding:"+strCoding+";"+e.getMessage());			
			return null;
		}finally{
			if(reader!=null){try{reader.close();}catch(Exception ex){}}
			if(stream!=null){try{stream.close();}catch(Exception ex){}}			
		}
	}
	

	
	/**
	 * 字符串编码转换
	 * @param strText
	 * @param strFromCoding
	 * @param strToCoding
	 * @return
	 */
	public static String encodingText(String strText,String strFromCoding,String strToCoding)
	{
		if(strFromCoding.equalsIgnoreCase(strToCoding)){
			return strText;
		}
		try{
			return new String(strText.getBytes(strFromCoding),strToCoding);
		}catch(Exception e){
			return null;
		}
	}
	/**
	 * 检查一个对象是否是一个类或接口的实例
	 * @param obj
	 * @param strClassName
	 * @return
	 */
	public static boolean IsInstanceOf(Object obj,String strClassName)
	{
		try{
			return Class.forName(strClassName).isInstance(obj);
		}catch(Exception e){
			return false;
		}
	}
	/**
	 * 
	 * @param ch
	 * @param nLength
	 * @return
	 */
	public static String makeSameCharString(char ch,int nLength)
	{
		char[] chsOutput=new char[nLength];
		for(int i=0;i<nLength;i++)
		{
			chsOutput[i]=ch;
		}
		
		return new String(chsOutput);
	}
	/**
	 * 查找到 nIndex 位置的字符的匹配字符
	 * @param nIndex
	 * @param strText
	 * @return
	 */
	public static int findMatchedChar(int nIndex,String strText){
		if(nIndex<0)
		{
			return -1;
		}		
		char[] chsText=strText.toCharArray();
		return findMatchedChar(nIndex, chsText);
	}
	/**
	 * 查找到 nIndex 位置的字符的匹配字符
	 * @param nIndex
	 * @param strText
	 * @return
	 */
	public static int findMatchedChar(int nIndex,char[] chsText)
	{
		if(nIndex<0)
		{
			return -1;
		}
		
		char chChar=chsText[nIndex];
		int nCount=0;
		int nStartIndex=-1;
		int nEndIndex=-1;

		char chFind=' ';
		switch(chChar)
		{
			case '(':
				chFind=')';
				break;
			case '{':
				chFind='}';
				break;
			case '[':
				chFind=']';
				break;
			case ')':
				chFind='(';
				break;
			case '}':
				chFind='{';
				break;
			case ']':
				chFind='[';
				break;
			default:
				return -1;
		}

		int nLength=chsText.length;
		switch(chChar)
		{
			case '(':
			case '{':
			case '[':
				for(int i=nIndex+1;i<nLength;i++)
				{
					char ch=chsText[i];
					
					if(ch==chChar)
					{
						nCount++;
					}
					else if(ch==chFind)
					{
						if(nCount==0)
						{
							nEndIndex=i;
							break;
						}
						else
						{
							nCount--;
						}
					}
				}
				return nEndIndex;
			case ')':
			case '}':
			case ']':
				for(int i=nIndex-1;i>=0;i--)
				{
					char ch=chsText[i];
					
					if(ch==chChar)
					{
						nCount++;
					}
					else if(ch==chFind)
					{
						if(nCount==0)
						{
							nStartIndex=i;
							break;
						}
						else
						{
							nCount--;
						}
					}
				}
				return nStartIndex;
			default:
				return -1;
		}
	}
	/**
	 * 
	 * @param obj
	 * @return
	 */
	public static Serializable deepClone(Serializable obj){
		Serializable objOutput=null;
		byte[] bysObject=null;

		//将对象输出到byte[]
		ObjectOutputStream out =null; 
		java.io.ByteArrayOutputStream streamOutput=new ByteArrayOutputStream();
		try
		{ 
			out = new ObjectOutputStream(streamOutput); 
			out.writeObject(obj); 
			bysObject=streamOutput.toByteArray();
		} catch (Exception e){
			logger.error(e.getMessage(),e);
			return null;
		}finally{
			try{out.close();}catch(Exception e){}
			try{streamOutput.close();}catch(Exception ex){}
		}

		//根据byte[]生成InputStream
		java.io.ByteArrayInputStream streamInput=new ByteArrayInputStream(bysObject);		
		ObjectInputStream in =null;; 
		try{ 
			in = new ObjectInputStream(streamInput); 
			objOutput = (Serializable) in.readObject(); 
		} catch (Exception e){
			return null;
		}finally{
			try{in.close();}catch(Exception ex){}
			try{streamInput.close();}catch(Exception ex){}
		}
		return objOutput;
	}
	
	/**
	 * 
	 * @param fileType
	 * @param source
	 * @return
	 */
	public static Object parseObjectValue(FieldType.type fileType,Object source)
	{
		if(source==null)
		{
			return null;
		}		
		switch (fileType.getType())
		{
			case FieldType.STRING_TYPE:
			{
				return source.toString();
			}
			case FieldType.INTEGER_TYPE:
			{
				return parseIntegerValue(source);
			}
			case FieldType.LONG_TYPE:
			{
				return parseLongValue(source);
			}
			case  FieldType.DATETIME_TYPE:
			{
				return parseDateTimeValue(source);
			}
			case  FieldType.DATE_TYPE:
			{
				return parseDateValue(source);
			}
			case  FieldType.TIME_TYPE:
			{
				return parseTimeValue(source);
			}
			case  FieldType.FLOAT_TYPE:
			{
				return parseFloatValue(source);
			}
			case  FieldType.DOUBLE_TYPE:
			{
				return parseDoubleValue(source);
			}
			case  FieldType.BOOLEAN_TYPE:
			{
				return parseBooleanValue(source);
			}
			case  FieldType.BYTE_TYPE:
			{
				return parseByteValue(source);
			}
			case  FieldType.SHORT_TYPE:
			{
				return parseShortValue(source);
			}
			case  FieldType.BYTE_ARRAY_TYPE:
			{
				return parseByteArrayValue(source);
			}
			case  FieldType.INTEGER_ARRAY_TYPE:
				{
					return parseIntegerArrayValue(source);
				}
			case  FieldType.LONG_ARRAY_TYPE:
				{
					return parseLongArrayValue(source);
				}
			case  FieldType.BOOLEAN_ARRAY_TYPE:
				{
					return parseBooleanArrayValue(source);
				}
			case  FieldType.SHORT_ARRAY_TYPE:
				{
					return parseShortArrayValue(source);
				}
			case  FieldType.STRING_ARRAY_TYPE:
				{
					return parseStringArrayValue(source);
				}				
			default:
			{
				throw new RuntimeException("invalid type "+fileType.getName());
			}
		}
	}

	public static String parseStingValue(Object source)
	{
		if(source==null)
		{
			return null;
		}
		return source.toString();
	}
	public static String[] parseStringArrayValue(Object source)
	{
		if(source==null)
		{
			return null;
		}
		if(source instanceof String[])
		{
			return (String[])source;
		}
		if(source instanceof String)
		{
			String[] ss = ((String)source).split(",");
			return ss;
		}
		return new String[]{source.toString()};
	}
	public static Integer parseIntegerValue(Object source)
	{
		if(source==null)
		{
			return null;
		}
		if(source instanceof Integer)
		{
			return (Integer)source;
		}
		return Double.valueOf(source.toString()).intValue();
			
	}
	public static int[] parseIntegerArrayValue(Object source)
	{
		if(source==null)
		{
			return null;
		}
		if(source instanceof int[])
		{
			return (int[])source;
		}
		if(source instanceof Object[])
		{
			Object[] objs=(Object[])source;
			int[] values=new int[objs.length];
			for(int i=0;i<objs.length;i++)
			{
				values[i]=Double.valueOf(objs[i].toString()).intValue();
			}
			return values;
		}
		if(source instanceof String)
		{
			String[] ss = ((String)source).split(",");
			int[] bsArr=new int[ss.length];
			for(int i=0;i<ss.length;i++)
			{
				bsArr[i] = Double.valueOf(ss[i]).intValue();
			}
			return bsArr;
		}		
		return new int[]{Double.valueOf(source.toString()).intValue()};		
	}	
	public static Long parseLongValue(Object source)
	{
		if(source==null)
		{
			return null;
		}

		if(source instanceof Long)
		{
			return (Long)source;
		}
		return Double.valueOf(source.toString()).longValue();
			
	}
	public static long[] parseLongArrayValue(Object source)
	{
		if(source==null)
		{
			return null;
		}
		if(source instanceof long[])
		{
			return (long[])source;
		}
		if(source instanceof int[])
		{
			int[] nsSource = (int[]) source;
			long[] lsResult = new long[nsSource.length];
			for(int i=0;i<nsSource.length;i++)
			{
				lsResult[i] = nsSource[i];
			}
			return lsResult;
		}
		if(source instanceof Object[])
		{
			Object[] objs=(Object[])source;
			long[] values=new long[objs.length];
			for(int i=0;i<objs.length;i++)
			{
				values[i]=new Double(objs[i].toString()).longValue();
			}
			return values;
		}
		if(source instanceof String)
		{
			String[] ss = ((String)source).split(",");
			long[] bsArr=new long[ss.length];
			for(int i=0;i<ss.length;i++)
			{
				bsArr[i] = new Double(ss[i]).longValue();
			}
			return bsArr;
		}		
		return new long[]{new Double(source.toString()).longValue()};		
	}		

	
	public static String parseDateTimeValue(Object source){
		if(source==null){
			return null;
		}
		if(source instanceof String){		
			String strValue = source.toString();
			if(strValue.length()==10){
				//======如果是date,自动补充======//
				strValue=strValue+" 00:00:00";
			}			
			if(strValue.length()==19){				
				try{
					SimpleDateFormat sdf=new SimpleDateFormat(Pattern.PATTERN_DATETIME);
					java.util.Date date=sdf.parse(strValue);
					return sdf.format(date);
				}catch(Exception ex){
					throw new RuntimeException("Invalid Date format "+source);
				}
			}else{
				throw new RuntimeException("Invalid Date format "+source);
			}
		}else if(source instanceof java.util.Date){
			java.util.Date temp = (java.util.Date)source;			
			SimpleDateFormat sdf=new SimpleDateFormat(Pattern.PATTERN_DATETIME);
			return sdf.format(temp);
		}else{
			throw new RuntimeException("Invalid date format "+source);
		}
	}
	
	public static String parseDateValue(Object source){
		if(source==null){
			return null;
		}
		
		if(source instanceof String){
			String strValue = source.toString();
			
			if(strValue.length()==19){
				//======如果是dateTime,自动裁剪date======//
				strValue=strValue.substring(0,10);
			}				
			if(strValue.length()==10){				
				try{
					SimpleDateFormat sdf=new SimpleDateFormat(Pattern.PATTERN_DATE);
					java.util.Date date=sdf.parse(strValue);
					return sdf.format(date);
				}catch(Exception ex){
					throw new RuntimeException("Invalid Date format "+source);
				}
			}else{
				throw new RuntimeException("Invalid Date format "+source);
			}
		}else if(source instanceof java.util.Date){
			java.util.Date temp = (java.util.Date)source;
			SimpleDateFormat sdf=new SimpleDateFormat(Pattern.PATTERN_DATE);
			return sdf.format(temp);
		}else{
			throw new RuntimeException("Invalid date format "+source);
		}		
	}
	public static String parseTimeValue(Object source)
	{
		if(source==null)
		{
			return null;
		}
		if(source instanceof String)
		{
			String strValue = source.toString();
			
			if(strValue.length()==19){
				//======如果是dateTime,自动裁剪date======//
				strValue=strValue.substring(11);
			}	
			if(strValue.length()==10){				
				try{
					SimpleDateFormat sdf=new SimpleDateFormat(Pattern.PATTERN_TIME);
					java.util.Date date=sdf.parse(strValue);
					return sdf.format(date);
				}catch(Exception ex){
					throw new RuntimeException("Invalid Date format "+source);
				}
			}else{
				throw new RuntimeException("Invalid Date format "+source);
			}
		}else if(source instanceof java.util.Date){
			java.util.Date temp = (java.util.Date)source;
			SimpleDateFormat sdf=new SimpleDateFormat(Pattern.PATTERN_TIME);
			return sdf.format(temp);
		}else{
			throw new RuntimeException("Invalid date format "+source);
		}
	}
	
	public static Float parseFloatValue(Object source)
	{
		if(source==null)
		{
			return null;
		}
		if(source instanceof Float)
		{
			return (Float)source;
		}
		return Double.valueOf(source.toString()).floatValue();
	}
	
	public static float[] parseFloatArrayValue(Object source)
	{
		if(source==null)
		{
			return null;
		}
		if(source instanceof float[])
		{
			return (float[])source;
		}
		if(source instanceof Object[])
		{
			Object[] objs=(Object[])source;
			float[] values=new float[objs.length];
			for(int i=0;i<objs.length;i++)
			{
				values[i]=Double.valueOf(objs[i].toString()).floatValue();
			}
			return values;
		}
		if(source instanceof String)
		{
			String[] ss = ((String)source).split(",");
			float[] bsArr=new float[ss.length];
			for(int i=0;i<ss.length;i++)
			{
				bsArr[i] = Float.parseFloat(ss[i]);
			}
			return bsArr;
		}
		return new float[]{Double.valueOf(source.toString()).floatValue()};		
	}
	
	public static Double parseDoubleValue(Object source)
	{
		if(source==null)
		{
			return null;
		}
		if(source instanceof Double)
		{
			return (Double)source;
		}
		return Double.valueOf(source.toString());
			
	}
	public static double[] parseDoubleArrayValue(Object source)
	{
		if(source==null)
		{
			return null;
		}
		if(source instanceof double[])
		{
			return (double[])source;
		}
		if(source instanceof Object[])
		{
			Object[] objs=(Object[])source;
			double[] values=new double[objs.length];
			for(int i=0;i<objs.length;i++)
			{
				values[i]=Double.valueOf(objs[i].toString()).doubleValue();
			}
			return values;
		}
		if(source instanceof String)
		{
			String[] ss = ((String)source).split(",");
			double[] bsArr=new double[ss.length];
			for(int i=0;i<ss.length;i++)
			{
				bsArr[i] = Double.parseDouble(ss[i]);
			}
			return bsArr;
		}		
		return new double[]{Double.valueOf(source.toString()).doubleValue()};		
	}
	
	public static Boolean parseBooleanValue(Object source)
	{
		if(source==null)
		{
			return null;
		}
		if(source instanceof Boolean)
		{
			return (Boolean)source;
		}
		String strValue = source.toString();
		if("true".equalsIgnoreCase(strValue))
		{
			return Boolean.TRUE;
		}
		if("false".equalsIgnoreCase(strValue))
		{
			return Boolean.FALSE;
		}
		if(Long.parseLong(strValue)==0)
		{
			return Boolean.FALSE;
		}
		else
		{
			return Boolean.TRUE;
		}			
	}
	public static boolean[] parseBooleanArrayValue(Object source)
	{
		if(source==null)
		{
			return null;
		}
		if(source instanceof boolean[])
		{
			return (boolean[])source;
		}
		if(source instanceof Boolean[])
		{
			Boolean[] bs=(Boolean[])source;
			boolean[] bsArr=new boolean[bs.length];
			for(int i=0;i<bs.length;i++)
			{
				bsArr[i]=bs[i].booleanValue();
			}
			return bsArr;
		}
		if(source instanceof Object[])
		{
			Object[] objs=(Object[])source;
			boolean[] values=new boolean[objs.length];
			for(int i=0;i<objs.length;i++)
			{
				values[i]=Boolean.valueOf(objs[i].toString());
			}
			return values;
		}		
		if(source instanceof String)
		{
			String[] ss = ((String)source).split(",");
			boolean[] bsArr=new boolean[ss.length];
			for(int i=0;i<ss.length;i++)
			{
				if("true".equalsIgnoreCase(ss[i]))
				{
					bsArr[i]=true;
				}
				else
				{
					bsArr[i]=false;
				}
			}
			return bsArr;
		}
		return null;			
	}	
	public static Boolean[] parseBooleanObjectArrayValue(Object source)
	{
		if(source==null)
		{
			return null;
		}
		if(source instanceof Boolean[])
		{
			return (Boolean[])source;
		}
		if(source instanceof boolean[])
		{
			boolean[] bs=(boolean[])source;
			Boolean[] bsArr=new Boolean[bs.length];
			for(int i=0;i<bs.length;i++)
			{
				bsArr[i]=new Boolean(bs[i]);
			}
			return bsArr;
		}
		if(source instanceof String)
		{
			String[] ss = ((String)source).split(",");
			Boolean[] bsArr=new Boolean[ss.length];
			for(int i=0;i<ss.length;i++)
			{
				if("true".equalsIgnoreCase(ss[i]))
				{
					bsArr[i]=Boolean.TRUE;
				}
				else
				{
					bsArr[i]=Boolean.FALSE;
				}
			}
			return bsArr;
		}
		return null;			
	}
	
	public static Short parseShortValue(Object source)
	{
		if(source==null)
		{
			return null;
		}
		if(source instanceof Byte)
		{
			return (Short)source;
		}
		return Double.valueOf(source.toString()).shortValue();
		
	}
	
	public static short[] parseShortArrayValue(Object source)
	{
		if(source==null)
		{
			return null;
		}
		if(source instanceof short[])
		{
			return (short[])source;
		}
		if(source instanceof Object[])
		{
			Object[] objs=(Object[])source;
			short[] values=new short[objs.length];
			for(int i=0;i<objs.length;i++)
			{
				values[i]=Double.valueOf(objs[i].toString()).shortValue();
			}
			return values;
		}
		if(source instanceof String)
		{
			String[] ss = ((String)source).split(",");
			short[] bsArr=new short[ss.length];
			for(int i=0;i<ss.length;i++)
			{
				bsArr[i] = Short.parseShort(ss[i]);
			}
			return bsArr;
		}		
		return new short[]{Long.valueOf(source.toString()).shortValue()};		
	}
		
	public static Byte parseByteValue(Object source)
	{
		if(source==null)
		{
			return null;
		}
		if(source instanceof Byte)
		{
			return (Byte)source;
		}
		return Long.valueOf(source.toString()).byteValue();
			
	}
	
	public static byte[] parseByteArrayValue(Object source) {
		if(source==null)
		{
			return null;
		}
		if(source instanceof byte[])
		{
			return (byte[])source;
		}
		if(source instanceof Object[])
		{
			Object[] objs=(Object[])source;
			byte[] values=new byte[objs.length];
			for(int i=0;i<objs.length;i++)
			{
				values[i]=Byte.parseByte(objs[i].toString());
			}
			return values;
		}		
		if(source instanceof String)
		{
			String[] ss = ((String)source).split(",");
			byte[] bsArr=new byte[ss.length];
			for(int i=0;i<ss.length;i++)
			{
				bsArr[i] = Byte.parseByte(ss[i]);
			}
			return bsArr;
		}		
		return source.toString().getBytes();
	}	
}