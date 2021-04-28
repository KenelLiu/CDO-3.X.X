package com.liantong;

import java.io.FileInputStream;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

public class WebListener implements ServletContextListener {

	private static  Logger logger=null;
	public void contextInitialized(ServletContextEvent sce){
		FileInputStream stream=null;
		try {
		
			stream=new FileInputStream("E:\\log4j2\\log4j2.xml");
			//设置log4j输出名
			ConfigurationSource source = new ConfigurationSource(stream);
			Configurator.initialize(null, source); 
			logger=LogManager.getLogger(WebListener.class);
			
		} catch (Exception e) {
			logger.fatal(e.getMessage(), e);
		}

		//if(logger.isInfoEnabled())
			logger.debug(" business service started debug-----------------");
			logger.info(" business service started info-----------------");
			logger.warn(" business service started warn-----------------");
	} 
}
