package com.cdoframework.cdolib.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import com.cdo.field.FieldType;
import com.cdo.pattern.Pattern;
import com.cdoframework.cdolib.base.Resources;
import com.cdoframework.cdolib.base.Return;


public class Utility{

	static protected DecimalFormat		decFormat       =new DecimalFormat();
	private static Logger logger=Logger.getLogger(Utility.class);
	
	/**
	 * 使用分隔符将字符串分隔
	 * @param strSource
	 * @param chSeperator
	 * @return
	 */
	public static String[] splitString(String strSource,char chSeperator)
	{
		ArrayList<String> alOutput=new ArrayList<String>();
		
		StringBuilder strbString=new StringBuilder();
		for(int i=0;i<strSource.length();i++)
		{
			char chChar=strSource.charAt(i);
			if(chChar==chSeperator)
			{
				alOutput.add(strbString.toString());

				strbString=new StringBuilder();
			}
			else
			{
				strbString.append(chChar);
			}
		}
		alOutput.add(strbString.toString());
		
		return alOutput.toArray(new String[alOutput.size()]);
	}
	
	/**
	 * 使用分隔符将字符串分隔
	 * @param strSource
	 * @param chSeperator
	 * @param bRepeated 是否重复分隔符当作一个
	 * @return
	 */
	public static String[] splitString(String strSource,char chSeperator,boolean bRepeated)
	{
		ArrayList<String> alOutput=new ArrayList<String>();
		
		StringBuilder strbString=new StringBuilder();
		char chLastChar='\0';
		for(int i=0;i<strSource.length();i++)
		{
			char chChar=strSource.charAt(i);
			if(chChar==chSeperator)
			{
				if(bRepeated==true)
				{
					if(chChar!=chLastChar)
					{
						alOutput.add(strbString.toString());
	
						strbString=new StringBuilder();
					}
				}
				else
				{
					alOutput.add(strbString.toString());

					strbString=new StringBuilder();
				}
			}
			else
			{
				strbString.append(chChar);
			}
			chLastChar=chChar;
		}
		alOutput.add(strbString.toString());

		return  alOutput.toArray(new String[alOutput.size()]);
	}

	/**
	 * 将指定字符串的每一行格式化成一个字符串
	 * 结果串中不包含回车符和换行符
	 * @param str
	 * @return
	 */
	public static String[] readLine(String str)
	{		
		List<String> list = new ArrayList<String>(10);
		BufferedReader reader = new BufferedReader(new StringReader(str));
		String strContent = null;
		try
		{
			while ((strContent=reader.readLine()) !=null)
			{
				list.add(strContent);
			}
		}
		catch(Exception e)
		{
			return null;
		}	
		finally
		{
			try
			{
				reader.close();
			}
			catch(IOException e)
			{
			}
		}

		return list.toArray(new String[list.size()]);		
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
		if(strFromCoding.equalsIgnoreCase(strToCoding))
		{
			return strText;
		}
		try
		{
			return new String(strText.getBytes(strFromCoding),strToCoding);
		}
		catch(Exception e)
		{
			return null;
		}
	}

	/**
	 * 读取文本文件的内容
	 * @param strFile
	 * @param strCoding
	 * @return
	 */
	public static String readTextFile(String strFile)
	{
		FileInputStream stream=null;
		InputStreamReader reader=null;
		try
		{
			stream=new java.io.FileInputStream(strFile);
			reader=new InputStreamReader(stream);

			char[] chsData=new char[(int)stream.getChannel().size()];
			int nReadSize=reader.read(chsData,0,chsData.length);
			
			return new String(chsData,0,nReadSize);
		}
		catch(Exception e)
		{
			return null;
		}
		finally
		{
			try
			{
				if(reader!=null)
				{
					reader.close();
				}
				if(stream!=null)
				{
				stream.close();
				}
			}
			catch(Exception e)
			{
				
			}
		}
	}

	/**
	 * 读取文本文件的内容
	 * @param strFile
	 * @param strCoding
	 * @return
	 */
	public static String readTextFile(String strFile,String strCoding)
	{
		FileInputStream stream=null;
		InputStreamReader reader=null;
		try
		{
			stream=new java.io.FileInputStream(strFile);
			reader=new InputStreamReader(stream,strCoding);
		
			char[] chsData=new char[(int)stream.getChannel().size()];
			int nReadSize=reader.read(chsData,0,chsData.length);	
			
			return new String(chsData,0,nReadSize);
		}
		catch(Exception e)
		{
			return null;
		}
		finally
		{
			try
			{
				if(reader!=null)
				{
					reader.close();
				}
				if(stream!=null)
				{
				stream.close();
				}
			}
			catch(Exception e)
			{
				
			}
		}
	}

	/**
	 * 读取文本Source的内容
	 * @param strFile
	 * @param strCoding
	 * @return
	 */
	public static String readTextResource(String strFile,String strCoding)
	{
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
			reader=new InputStreamReader(stream,strCoding);

			StringBuilder strbContent=new StringBuilder();
			char[] chsData=new char[1024];// Aaron change 10240 to 1024 on 2010-08-17. 10240 might cause OOM
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
		catch(Exception e)
		{
			logger.error("strFile:"+strFile+",strCoding:"+strCoding+";"+e.getMessage());			
			return null;
		}
		finally
		{
			try
			{
				if(reader!=null)
				{
					reader.close();
				}
			}
			catch(Exception e)
			{
			}
			if(stream!=null)
			{
				try
				{
					stream.close();
				}
				catch(Exception e)
				{
					
				}
			}
		}
	}
	
	public static String readTextResource(String strFile)
	{
		InputStream stream=null;
		InputStreamReader reader=null;
		try
		{
			stream=Resources.getResourceAsStream(strFile);
			reader=new InputStreamReader(stream);

			StringBuilder strbContent=new StringBuilder();
			char[] chsData=new char[10240];// Aaron change 10240 to 1024 on 2010-08-17. 10240 might cause OOM
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
		catch(Exception e)
		{
			return null;
		}
		finally
		{
			try
			{
				if(reader!=null)
				{
					reader.close();
				}
			}
			catch(Exception e)
			{
			}
			if(stream!=null)
			{
				try
				{
					stream.close();
				}
				catch(Exception e)
				{
					
				}
			}
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
		try
		{
			return Class.forName(strClassName).isInstance(obj);
		}
		catch(Exception e)
		{
			return false;
		}
	}

	/**
	 * 读取文件所有数据
	 * @param strFile
	 * @return
	 */
	public static byte[] readFile(String strFile)
	{
		FileInputStream stream=null;
		try
		{
			stream=new java.io.FileInputStream(strFile);
			
			byte[] bysData=new byte[(int)stream.getChannel().size()];
			stream.read(bysData);
			
			return bysData;
		}
		catch(Exception e)
		{
			return null;
		}
		finally
		{
			try
			{
				if(stream!=null)
				{
					stream.close();
				}
			}
			catch(Exception e)
			{
			}
		}
	}

	/**
	 * 取本机IP
	 * <br>注意:本方法可能返回127.0.0.1
	 * @return
	 */
	static public String getIPAddress()
	{
        String strIPAddress=null;

        try
        {
	        InetAddress netAddress = InetAddress.getLocalHost();
	        strIPAddress	=netAddress.getHostAddress();
        }
        catch(Exception e)
        {
        	
        }
        return strIPAddress;
	}
	/**
	 * 取本机IP
	 * <br>用InetAddress类有可能会取到127的地址,所以当取到127的地址后使用NetworkInterface类
	 * <br>在非windows操作系统中,推荐使用本方法,而不使用本类中的<getIPAddress()>方法
	 * 
	 * @return 失败返回NULL,成功返回IPV4地址
	 * 
	 */
	static public String getLocalIp()
	{
		String strIp = null;
		
		//首先偿试用InetAddress
		try
		{
			InetAddress netAddress=InetAddress.getLocalHost();
			strIp	=netAddress.getHostAddress();
			if(!strIp.startsWith("127"))
			{
				return strIp;	
			}			
		}
		catch(UnknownHostException e1)
		{			
		}
		
		//未找到非127的地址
	    Enumeration<NetworkInterface> netInterfaces = null;
		try
		{
			netInterfaces=NetworkInterface.getNetworkInterfaces();
		}
		catch(SocketException e)
		{	//使用NetworkInterface失败,只能返回127.0.0.1的地址了	       
			return strIp;
		}
	   InetAddress ip = null;
	   while(netInterfaces.hasMoreElements())
	   {
			NetworkInterface ni=(NetworkInterface)netInterfaces.nextElement();	
			Enumeration<InetAddress> enumInetAddress = ni.getInetAddresses();
			while(enumInetAddress.hasMoreElements())
			{
			    ip=(InetAddress) enumInetAddress.nextElement();
			    strIp = ip.getHostAddress();
			    
			    //过虑掉127和localhost
			    if( strIp.startsWith("127")||strIp.startsWith("l")||strIp.startsWith("L"))
			    {
			    	continue;
			    }	    
			    return strIp;
			}
	   }
	   
	   //还没有取到,返回空或127.0.0.1
	   return strIp;	   
	}


	public static byte[] hexStringToBytes(String strHexString)
	{
		String strDigital="0123456789ABCDEF";
	    
		byte[] bytes= new byte[strHexString.length()/2];
	    int temp;
	    for(int i=0;i<bytes.length;i++){
	      temp=strDigital.indexOf(strHexString.substring(2*i,2*i+1))*16;
	      temp+=strDigital.indexOf(strHexString.substring(2*i+1,2*i+2));
	      bytes[i]=(byte)(temp&0xff);
	    }

	    return bytes;
	}

	public static String bytesToHexString(byte[] bysBytes)
	{
		String strDigital="0123456789ABCDEF";
		
	    StringBuilder sb=new StringBuilder("");
	    byte[] bs = bysBytes;
	    int bit;
	    
	    for(int i=0;i<bs.length;i++)
	    {
	      bit=(bs[i]&0x0f0)>>4;
	      sb.append(strDigital.substring(bit,bit+1));
	      bit=bs[i]&0x0f;
	      sb.append(strDigital.substring(bit,bit+1));
	    }

	    return sb.toString();
	}

	public static void closeReader(Reader reader)
	{
		if(reader==null)
		{
			return;
		}
		try
		{
			reader.close();
		}
		catch(Exception e)
		{
		}
	}



	public static String makeSameCharString(char ch,int nLength)
	{
		char[] chsOutput=new char[nLength];
		for(int i=0;i<nLength;i++)
		{
			chsOutput[i]=ch;
		}
		
		return new String(chsOutput);
	}

	public static String format(Object obj,String strFormat) throws Exception
	{
		if(Utility.IsInstanceOf(obj,"java.lang.String")==true)
		{//String Format,Format is like "Length|FillChar|0 or 1",最后一个值0表示在前面补,1表示在后面补
			String strObj=(String)obj;
			if(strFormat==null || strFormat.length()==0)
			{
				return strObj;
			}
			
			//分析得到格式
			String[] strsFormatItem=Utility.splitString(strFormat,',');
			int nLength=0;
			char chFill=' ';
			int nFillAt=0;
			if(strsFormatItem.length>=1)
			{
				nLength=Integer.parseInt(strsFormatItem[0]);
			}
			if(strsFormatItem.length>=2)
			{
				if(strsFormatItem[1].length()!=1)
				{
					throw new Exception("Unsupported format: "+strFormat);
				}
				chFill=strsFormatItem[1].charAt(0);
			}
			if(strsFormatItem.length>=3)
			{
				nFillAt=Integer.parseInt(strsFormatItem[2]);
				if(nFillAt!=0 && nFillAt!=1)
				{
					throw new Exception("Unsupported format: "+strFormat);
				}
			}
			else
			{
				throw new Exception("Unsupported format: "+strFormat);
			}
			
			//生成输出
			if(strObj.length()>=nLength)
			{
				return strObj;
			}
			
			String strOutput="";
			if(nFillAt==0)
			{
				strOutput=makeSameCharString(chFill,nLength-strObj.length())+strObj;
			}
			else
			{
				strOutput=strObj+makeSameCharString(chFill,nLength-strObj.length());
			}
			
			return strOutput;
		}
		else if(Utility.IsInstanceOf(obj,"java.lang.Byte")==true || Utility.IsInstanceOf(obj,"java.lang.Integer")==true
			|| Utility.IsInstanceOf(obj,"java.lang.Long")==true)
		{//Integer Format,Format is like "Length|FillChar"
			String strObj=obj.toString();
			if(strFormat==null || strFormat.length()==0)
			{
				return strObj;
			}

			//分析得到格式
			String[] strsFormatItem=Utility.splitString(strFormat,',');
			int nLength=0;
			char chFill=' ';
			int nFillAt=0;
			if(strsFormatItem.length>=1)
			{
				nLength=Integer.parseInt(strsFormatItem[0]);
			}
			if(strsFormatItem.length>=2)
			{
				if(strsFormatItem[1].length()!=1)
				{
					throw new Exception("Unsupported format: "+strFormat);
				}
				chFill=strsFormatItem[1].charAt(0);
			}
			if(strsFormatItem.length>=3)
			{
				nFillAt=Integer.parseInt(strsFormatItem[2]);
				if(nFillAt!=0 && nFillAt!=1)
				{
					throw new Exception("Unsupported format: "+strFormat);
				}
			}
			else
			{
				throw new Exception("Unsupported format: "+strFormat);
			}
			
			//生成输出
			if(strObj.length()>=nLength)
			{
				return strObj;
			}
			
			String strOutput="";
			if(nFillAt==0)
			{
				strOutput=makeSameCharString(chFill,nLength-strObj.length())+strObj;
			}
			else
			{
				strOutput=strObj+makeSameCharString(chFill,nLength-strObj.length());
			}
			
			return strOutput;
		}
		else
		{//Unsupported
			throw new Exception("Unsupported format: "+strFormat);
		}
	}

	public static String formatArray(Object objArray,int nIndex,String strFormat) throws Exception
	{
		if(objArray.getClass().isArray()==false)
		{
			throw new Exception("Object not an array");
		}
		
		Object obj=null;
		if(Utility.IsInstanceOf(objArray,"[B")==true)
		{
			byte[] bysObj=(byte[])objArray;
			obj=new Byte(bysObj[nIndex]);
		}
		else if(Utility.IsInstanceOf(objArray,"[I")==true)
		{
			int[] nsObj=(int[])objArray;
			obj=new Integer(nsObj[nIndex]);
		}
		else if(Utility.IsInstanceOf(objArray,"[J")==true)
		{
			long[] lsObj=(long[])objArray;
			obj=new Long(lsObj[nIndex]);
		}
		else if(Utility.IsInstanceOf(objArray,"[Ljava.lang.String")==true)
		{
			String[] strsObj=(String[])objArray;
			obj=strsObj[nIndex];
		}
		
		else
		{
			throw new Exception("Unsupported array: "+objArray.getClass().getName());
		}
		
		if(Utility.IsInstanceOf(obj,"java.lang.String")==true)
		{//String Format,Format is like "Length|FillChar|0 or 1"
			String strObj=(String)obj;
			if(strFormat==null || strFormat.length()==0)
			{
				return strObj;
			}
			
			//分析得到格式
			String[] strsFormatItem=Utility.splitString(strFormat,',');
			int nLength=0;
			char chFill=' ';
			int nFillAt=0;
			if(strsFormatItem.length>=1)
			{
				nLength=Integer.parseInt(strsFormatItem[0]);
			}
			if(strsFormatItem.length>=2)
			{
				if(strsFormatItem[1].length()!=1)
				{
					throw new Exception("Unsupported format: "+strFormat);
				}
				chFill=strsFormatItem[1].charAt(0);
			}
			if(strsFormatItem.length>=3)
			{
				nFillAt=Integer.parseInt(strsFormatItem[2]);
				if(nFillAt!=0 && nFillAt!=1)
				{
					throw new Exception("Unsupported format: "+strFormat);
				}
			}
			else
			{
				throw new Exception("Unsupported format: "+strFormat);
			}
			
			//生成输出
			if(strObj.length()>=nLength)
			{
				return strObj;
			}
			
			String strOutput="";
			if(nFillAt==0)
			{
				strOutput=makeSameCharString(chFill,nLength-strObj.length())+strObj;
			}
			else
			{
				strOutput=strObj+makeSameCharString(chFill,nLength-strObj.length());
			}
			
			return strOutput;
		}
		else if(Utility.IsInstanceOf(obj,"java.lang.Byte")==true || Utility.IsInstanceOf(obj,"java.lang.Integer")==true
			|| Utility.IsInstanceOf(obj,"java.lang.Long")==true)
		{//Integer Format,Format is like "Length|FillChar"
			String strObj=obj.toString();
			if(strFormat==null || strFormat.length()==0)
			{
				return strObj;
			}

			//分析得到格式
			String[] strsFormatItem=Utility.splitString(strFormat,',');
			int nLength=0;
			char chFill=' ';
			int nFillAt=0;
			if(strsFormatItem.length>=1)
			{
				nLength=Integer.parseInt(strsFormatItem[0]);
			}
			if(strsFormatItem.length>=2)
			{
				if(strsFormatItem[1].length()!=1)
				{
					throw new Exception("Unsupported format: "+strFormat);
				}
				chFill=strsFormatItem[1].charAt(0);
			}
			if(strsFormatItem.length>=3)
			{
				nFillAt=Integer.parseInt(strsFormatItem[2]);
				if(nFillAt!=0 && nFillAt!=1)
				{
					throw new Exception("Unsupported format: "+strFormat);
				}
			}
			else
			{
				throw new Exception("Unsupported format: "+strFormat);
			}
			
			//生成输出
			if(strObj.length()>=nLength)
			{
				return strObj;
			}
			
			String strOutput="";
			if(nFillAt==0)
			{
				strOutput=makeSameCharString(chFill,nLength-strObj.length())+strObj;
			}
			else
			{
				strOutput=strObj+makeSameCharString(chFill,nLength-strObj.length());
			}
			
			return strOutput;
		}
		
		else
		{//Unsupported
			throw new Exception("Unsupported format: "+strFormat);
		}
	}

	

	public static boolean isLeapYear(int nYear)
	{
		if(nYear%100==0)
		{
			if(nYear%400==0)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			if(nYear%4==0)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
	}


	/**
	 * 查找到 nIndex 位置的字符的匹配字符
	 * @param nIndex
	 * @param strText
	 * @return
	 */
	public static int findMatchedChar(int nIndex,String strText)
	{
		if(nIndex<0)
		{
			return -1;
		}
		
		char[] chsText=strText.toCharArray();
		
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
	

	public static Object parseObjectValue(int nType,Object source)
	{
		if(source==null)
		{
			return null;
		}
		switch (nType)
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
				throw new RuntimeException("invalid type "+nType);
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
	/**
	 * 检查一个字符串是否为数字字符串，即全部由0－9组成
	 * @param strText 待检查的字符串
	 * @return 是:true,否:false
	 */
	static public boolean isIntText(String strText)
	{
		strText	=strText.trim();
		if(strText.length()==0)
		{
			return false;
		}

		for(int i=0;i<strText.length();i++)
		{
			if(strText.charAt(i)<'0' || strText.charAt(i)>'9')
			{
				return false;
			}
		}

		return true;
	}
	
	public static boolean checkDate(String strValue)
	{
		int nLength=strValue.length();
		if(nLength==0)
		{
			return true;
		}
		if(nLength!=10)
		{
			return false;
		}

		if(strValue.charAt(4)!='-' && strValue.charAt(7)!='-')
		{
			return false;
		}
		String strYear=strValue.substring(0,4);
		String strMonth=strValue.substring(5,7);
		String strDay=strValue.substring(8);
		if(Utility.isIntText(strYear)==false || Utility.isIntText(strMonth)==false || Utility.isIntText(strDay)==false)
		{
			return false;
		}
		int nMonth=Integer.parseInt(strMonth);
		if(nMonth<1 || nMonth>12)
		{
			return false;
		}
		int nDay=Integer.parseInt(strDay);
		if(nDay<1 || nDay>31)
		{
			return false;
		}
		if(nMonth==1 || nMonth==3 || nMonth==5 || nMonth==7 || nMonth==8 || nMonth==10 || nMonth==12)			
		{//大月
			return true;
		}
		if((nMonth==4 || nMonth==6 || nMonth==9 || nMonth==11))
		{//小月
			if(nDay>30)
			{
				return false;
			}
			else
			{
				return true;
			}
		}

		int nYear=Integer.parseInt(strYear);
		if(Utility.isLeapYear(nYear)==false && nDay>28)
		{
			return false;
		}
		if(Utility.isLeapYear(nYear) && nDay>29)
		{
			return false;
		}
		return true;
	}

	public static boolean checkTime(String strValue)
	{
		int nLength=strValue.length();
		if(nLength==0)
		{
			return true;
		}
		if(nLength!=8)
		{
			return false;
		}

		if(strValue.charAt(2)!=':' && strValue.charAt(5)!=':')
		{
			return false;
		}
		String strHour=strValue.substring(0,2);
		String strMinute=strValue.substring(3,5);
		String strSecond=strValue.substring(6);
		if(Utility.isIntText(strHour)==false || Utility.isIntText(strMinute)==false || Utility.isIntText(strSecond)==false)
		{
			return false;
		}
		int nHour=Integer.parseInt(strHour);
		if(nHour<0 || nHour>23)
		{
			return false;
		}
		int nMinute=Integer.parseInt(strMinute);
		if(nMinute<0 || nMinute>59)
		{
			return false;
		}
		int nSecond=Integer.parseInt(strSecond);
		if(nSecond<0 || nSecond>59)
		{
			return false;
		}

		return true;
	}

	public static boolean checkDateTime(String strValue)
	{
		if(strValue.length()!=19)
		{
			return false;
		}
		if(strValue.charAt(10)!=' ')
		{
			return false;
		}
		
		String strDate=strValue.substring(0,10);
		if(checkDate(strDate)==false)
		{
			return false;
		}
		String strTime=strValue.substring(11);
		if(checkTime(strTime)==false)
		{
			return false;
		}
		
		return true;
	}

	public static String parseDateTimeValue(Object source)
	{
		if(source==null)
		{
			return null;
		}
		if(source instanceof String)
		{		
			String strValue = source.toString();
			if(Utility.checkDateTime(strValue)==true)
			{
				return strValue;
			}
			else if(Utility.checkDate(strValue)==true)
			{
				return strValue+" 00:00:00";
			}
			else
			{
				throw new RuntimeException("Invalid date format "+source);
			}
		}
		else if(source instanceof java.util.Date)			
		{
			java.util.Date temp = (java.util.Date)source;			
			SimpleDateFormat sdf=new SimpleDateFormat(Pattern.PATTERN_DATETIME);
			return sdf.format(temp);
		}
		
		else
		{
			throw new RuntimeException("Invalid date format "+source);
		}
		
			
	}
	public static String parseDateValue(Object source)
	{
		if(source==null)
		{
			return null;
		}
		if(source instanceof String)
		{
			String strValue = source.toString();
			if(Utility.checkDate(strValue)==true)
			{
				return strValue;
			}
			if(Utility.checkDateTime(strValue))
			{
				return strValue.substring(0,10);
			}
			else
			{
				throw new RuntimeException("Invalid date format");
			}
		}
		else if(source instanceof java.util.Date)			
		{
			java.util.Date temp = (java.util.Date)source;
			SimpleDateFormat sdf=new SimpleDateFormat(Pattern.PATTERN_DATE);
			return sdf.format(temp);
		}		
		else
		{
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
			if(Utility.checkTime(strValue)==true)
			{
				return strValue;
			}
			if(Utility.checkDateTime(strValue))
			{
				return strValue.substring(11);
			}
			throw new RuntimeException("Invalid date format");
		}
		else if(source instanceof java.util.Date)			
		{
			java.util.Date temp = (java.util.Date)source;
			SimpleDateFormat sdf=new SimpleDateFormat(Pattern.PATTERN_TIME);
			return sdf.format(temp);
		}		
		else
		{
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