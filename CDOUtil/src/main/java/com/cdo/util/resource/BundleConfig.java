package com.cdo.util.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import com.cdoframework.cdolib.base.Resources;
/**
 * @author KenelLiu
 */
public class BundleConfig{

  private Properties prop = null;

  private Logger logger = Logger.getLogger(getClass());

  /**
   * 默认从class path路径下读取配置文件数据
   * @param strConfig
   * @throws IOException
   */
  public BundleConfig(String strConfig) throws IOException{
	  this(strConfig,true);    
  }
  /**
   * 
   * @param strConfig
   * @param isClassPath  true 表示从class路径下读取配置文件,false 表示从指定路径strConfig 读取
   * @throws IOException
   */
  public BundleConfig(String strConfig,boolean isClassPath) throws IOException{
	  if(logger.isInfoEnabled())
		  logger.info(strConfig+",scan classpath path?"+isClassPath);		  
	  this.prop = new Properties();
	  if(isClassPath){
	      for (String file : listResource(strConfig)) {
	    	  this.loadConfigFromResource(file,true);
	      } 
	  }else{
		  this.loadConfigFromResource(strConfig, false);
	  }
  }
	
  void loadConfigFromResource(String strResource,boolean isClassPath){
		InputStream stream=null;		
		try{
			if(isClassPath)
				stream=Resources.getResourceAsStream(strResource);
			else
				stream=new FileInputStream(strResource);
			prop.load(stream);						
		}catch(Exception e){
			logger.error(e.getMessage(),e);
		}finally{
			try{stream.close();}catch(Exception e){}
		}
	}
  

  public String getString(String key){
    return getString(key, null);
  }

  public String getString(String key, String defaultValue){   
    if(this.prop != null){
      return this.prop.getProperty(key, defaultValue);
    }
    return defaultValue;
  }

  public int getInt(String key){
    return getInt(key,0);
  }

  public int getInt(String key, int defaultValue){
    String stringValue = getString(key);
    if (stringValue != null) {
      try{
    	  return Integer.parseInt(stringValue.trim());
      	 }catch (NumberFormatException ex) {}
    }
    return defaultValue;
  }

  public long getLong(String key){
    return getLong(key, 0L);
  }

  public long getLong(String key, long defaultValue){    
    String stringValue = getString(key);
    if (stringValue != null) {
      try{
        return Long.parseLong(stringValue.trim());
      }catch (NumberFormatException ex) {}
    }
    return defaultValue;
  }

  public boolean getBoolean(String key) {
    return getBoolean(key, false);
  }

  public boolean getBoolean(String key, boolean defaultValue)
  {
    String stringValue =getString(key);
    if (stringValue != null) {  
        try{
            return Boolean.parseBoolean(stringValue.trim());
          }catch (Exception ex) {}      
    }
    return defaultValue;
  }

  
  String[] listResource(String fileReg)throws IOException{
    String currentPath = Resources.getResourceURL("").getFile().replaceAll("%20"," ");
    String[] files = new File(currentPath).list(new ResourcesFilter(fileReg));
    return files;
  }
	  
  private static class ResourcesFilter implements FilenameFilter{
    private String nameReg = null;

    public ResourcesFilter(String nameReg) {
      this.nameReg = nameReg;
    }
    public boolean accept(File dir, String name){
      return Pattern.matches(this.nameReg, name);
    }
  }
  
}