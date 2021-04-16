package com.cdoframework.cdolib.data.cdo;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nanoxml.XMLElement;

import com.cdo.field.BooleanField;
import com.cdo.field.ByteField;
import com.cdo.field.DateField;
import com.cdo.field.DateTimeField;
import com.cdo.field.DoubleField;
import com.cdo.field.FileField;
import com.cdo.field.FloatField;
import com.cdo.field.IntegerField;
import com.cdo.field.LongField;
import com.cdo.field.NullField;
import com.cdo.field.ShortField;
import com.cdo.field.StringField;
import com.cdo.field.TimeField;
import com.cdo.field.array.BooleanArrayField;
import com.cdo.field.array.ByteArrayField;
import com.cdo.field.array.DateArrayField;
import com.cdo.field.array.DateTimeArrayField;
import com.cdo.field.array.DoubleArrayField;
import com.cdo.field.array.FloatArrayField;
import com.cdo.field.array.IntegerArrayField;
import com.cdo.field.array.LongArrayField;
import com.cdo.field.array.ShortArrayField;
import com.cdo.field.array.StringArrayField;
import com.cdo.field.array.TimeArrayField;
import com.cdoframework.cdolib.data.cdo.CDO;

/**
 * xml2CDO 
 * @author KenelLiu
 *
 */
public class ParseXmlCDO {
	
	
	protected static void xml2CDO(CDO cdo,XMLElement nodeCDO,boolean isRootNode)
	{
		Iterator<?> enumNodes=nodeCDO.enumerateChildren();

		while(enumNodes.hasNext())
		{
			XMLElement node=(XMLElement)enumNodes.next();

			String strTag=node.getName();
			String strName	=node.getStringAttribute("N");
			Object v=node.getAttribute("V");
			String strValue=null;
			if(v!=null)
				strValue=(String)v;
			switch(strTag){
				case "BF"://BooleanField
					boolean bValue=false;
					if(strValue.equalsIgnoreCase("true"))
					{
						bValue=true;
					}
					cdo.putItem(strName,new BooleanField(strName,bValue));
				break;
				case "BYF"://ByteField			
					 cdo.putItem(strName,new ByteField(strName,Byte.parseByte(strValue)));
					break;
				case "SF"://ShortField
					 cdo.putItem(strName,new ShortField(strName, Short.parseShort(strValue)));
					break;
				case "NF"://IntegerField
					 cdo.putItem(strName,new IntegerField(strName, Integer.parseInt(strValue)));
					break;
				case "LF"://LongField
					 cdo.putItem(strName,new LongField(strName, Long.parseLong(strValue)));					
					break;
				case "FF"://FloatField
					 cdo.putItem(strName,new FloatField(strName, Float.parseFloat(strValue)));					
					break;
				case "DBLF"://DoubleField	
					cdo.putItem(strName,new DoubleField(strName, Double.parseDouble(strValue)));
					break;
				case "STRF"://StringField
					cdo.putItem(strName,new StringField(strName, strValue));					
					break;
				case "DF"://DateField
					cdo.putItem(strName,new DateField(strName, strValue));					
					break;
				case "TF"://TimeField
					cdo.putItem(strName,new TimeField(strName, strValue));
					break;
				case "DTF":	//DateTimeField
					cdo.putItem(strName,new DateTimeField(strName, strValue));
					break;
				case "CDOF"://CDOField
					CDO cdoValue=new CDO();
					xml2CDO(cdoValue,(XMLElement)node.getChildren().get(0),false);
					cdo.putItem(strName,new CDOField(strName, cdoValue));
					break;
				case "FILE":				
					cdo.putItem(strName,new FileField(strName, new File(strValue)));
				   //仅对最顶级CDO里的file 做字节流传输,嵌套里的文件在网络传输中不处理  忽略掉
					if(isRootNode)
						cdo.fileCount++;
					break;
				case "BAF"://BooleanArrayField
					String[] strValueArr=null;
					if(strValue.length()==0){
						strValueArr=new String[0];
					}else{
						strValueArr=strValue.split(",");
					}
					boolean[] bsValue=new boolean[strValueArr.length];
					for(int i=0;i<strValueArr.length;i++){
						if(strValueArr[i].equalsIgnoreCase("true")){
							bsValue[i]=true;	
						}
					}
					cdo.putItem(strName,new BooleanArrayField(strName, bsValue));
					break;
				case "BYAF":
					
					if(strValue.length()==0){
						strValueArr=new String[0];
					}else{
						strValueArr=strValue.split(",");
					}
					//bytes数据比较多，采用分成四等份，循环1/4 即可完成原来遍历所有数据处理。
					byte[] bysValue=new byte[strValueArr.length];
					int length=bysValue.length;				
					int mid=length/2;
					int quarter=mid/2;
					int j=quarter+1;
					int m=mid+1;
					int quarter3=mid+quarter;
					int n=mid+quarter+1;
								
					for(int i=0;i<=quarter ;i++){
						try{
							bysValue[i]=Byte.parseByte(strValueArr[i]);				
							if(j<=mid){
								bysValue[j]=Byte.parseByte(strValueArr[j]);
								j++;
							}
							if(m<=quarter3){
								bysValue[m]=Byte.parseByte(strValueArr[m]);
								m++;
							}
							if(n<length){
								bysValue[n]=Byte.parseByte(strValueArr[n]);
								n++;
							}				
						}catch(Exception ex)
						{
							throw new RuntimeException("Parse xml error: unexpected short value "+strValueArr[i]+" under "+strTag);
						}					
					}			
					cdo.putItem(strName,new ByteArrayField(strName, bysValue));					
					break;
				case "SAF":
					if(strValue.length()==0){
						strValueArr=new String[0];
					}else{
						strValueArr=strValue.split(",");
					}
					short[] shsValue=new short[strValueArr.length];
					for(int i=0;i<strValueArr.length;i++)
					{
						try{
							shsValue[i]=Short.parseShort(strValueArr[i]);
						}catch(Exception ex)
						{
							throw new RuntimeException("Parse xml error: unexpected short value "+strValueArr[i]+" under "+strTag);
						}
					}			
					cdo.putItem(strName,new ShortArrayField(strName,shsValue));					
					break;
				case "NAF":
					if(strValue.length()==0){
						strValueArr=new String[0];
					}else{
						strValueArr=strValue.split(",");
					}
					int[] nsValue=new int[strValueArr.length];
					for(int i=0;i<strValueArr.length;i++)
					{
						try{
							nsValue[i]=Integer.parseInt(strValueArr[i]);
						}catch(Exception ex)
						{
							throw new RuntimeException("Parse xml error: unexpected int value "+strValueArr[i]+" under "+strTag);
						}
					}			
					cdo.putItem(strName,new IntegerArrayField(strName, nsValue));					
					break;
				case "LAF":
					if(strValue.length()==0){
						strValueArr=new String[0];
					}else{
						strValueArr=strValue.split(",");
					}
					long[] lsValue=new long[strValueArr.length];
					for(int i=0;i<strValueArr.length;i++)
					{
						try{
							lsValue[i]=Long.parseLong(strValueArr[i]);
						}catch(Exception ex)
						{
							throw new RuntimeException("Parse xml error: unexpected long value "+strValueArr[i]+" under "+strTag);
						}
					}			
					cdo.putItem(strName,new LongArrayField(strName, lsValue));					
					break;
				case "FAF":
					if(strValue.length()==0){
						strValueArr=new String[0];
					}else{
						strValueArr=strValue.split(",");
					}
					float[] fsValue=new float[strValueArr.length];
					for(int i=0;i<strValueArr.length;i++)
					{
						try{
							fsValue[i]=Float.parseFloat(strValueArr[i]);
						}catch(Exception ex)
						{
							throw new RuntimeException("Parse xml error: unexpected float value "+strValueArr[i]+" under "+strTag);
						}
					}			
					cdo.putItem(strName,new FloatArrayField(strName,fsValue));					
					break;
				case "DBLAF":
					if(strValue.length()==0){
						strValueArr=new String[0];
					}else{
						strValueArr=strValue.split(",");
					}
					double[] dblsValue=new double[strValueArr.length];
					for(int i=0;i<strValueArr.length;i++)
					{
						try{
							dblsValue[i]=Double.parseDouble(strValueArr[i]);
						}catch(Exception ex)
						{
							throw new RuntimeException("Parse xml error: unexpected float value "+strValueArr[i]+" under "+strTag);
						}
					}			
					cdo.putItem(strName,new DoubleArrayField(strName, dblsValue));					
					break;
				case "STRAF":
					Iterator<?> enumItems	=node.enumerateChildren();

					String[] strsValue=new String[node.countChildren()];
					int nIndex=0;
					while(enumItems.hasNext())
					{
						XMLElement subNode=(XMLElement)enumItems.next();
						String strSubNodeTag=subNode.getName();
						if(strSubNodeTag.equals("STR")==false){
							throw new RuntimeException("Parse xml error: unexpected Tag name "+strSubNodeTag+" under "+strTag);
						}
						strsValue[nIndex]=subNode.getContent();
						nIndex++;
					}
					cdo.putItem(strName,new StringArrayField(strName, strsValue));					
					break;
				case "DAF":
					if(strValue.length()==0){
						strValueArr=new String[0];
					}else{
						strValueArr=strValue.split(",");
					}
					cdo.putItem(strName,new DateArrayField(strName, strValueArr));
					break;	
				case "TAF":
					if(strValue.length()==0){
						strsValue=new String[0];
					}else{
						strsValue=strValue.split(",");
					}
					cdo.putItem(strName,new TimeArrayField(strName, strsValue));
					break;
				case "DTAF":
					if(strValue.length()==0){
						strsValue=new String[0];
					}else{
						strsValue=strValue.split(",");
					}
					cdo.putItem(strName,new DateTimeArrayField(strName, strsValue) );					
					break;
				case "CDOAF":
					enumItems=node.enumerateChildren();
					List<CDO> cdosValue=new ArrayList<CDO>(node.countChildren());
					while(enumItems.hasNext())
					{
						XMLElement subNode=(XMLElement)enumItems.next();
						String strSubNodeTag=subNode.getName();
						if(strSubNodeTag.equals("CDO")==false)
						{
							throw new RuntimeException("Parse xml error: unexpected Tag name "+strSubNodeTag+" under "+strTag);
						}
						CDO tmpCDO=new CDO();
						xml2CDO(tmpCDO,subNode,false);
						cdosValue.add(tmpCDO);					
					}
					cdo.putItem(strName,new CDOArrayField(strName, cdosValue));
					break;
				case "NullF":
					cdo.putItem(strName,new NullField(strName));
					break;
				default:
					throw new RuntimeException("Parse xml error: unexpected Tag name ["+strTag+"]");
			}
		}
	}
}
