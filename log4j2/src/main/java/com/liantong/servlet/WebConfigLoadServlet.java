package com.liantong.servlet;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;


import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.LogManager;

public class WebConfigLoadServlet extends HttpServlet{

	private static final long serialVersionUID = -2660000154191149013L;
	private static  Logger logger=LogManager.getLogger(WebConfigLoadServlet.class);
	final String prjName="activitiAdmin";
	@Override
	public void init() throws ServletException {
		//FileInputStream stream=null;
		try {
		
			//stream=new FileInputStream("E:\\log4j2\\log4j2.xml");
			//设置log4j输出名
			//ConfigurationSource source = new ConfigurationSource(stream);
			//Configurator.initialize(null, source); 
			//logger=LogManager.getLogger(WebConfigLoadServlet.class);
			
		} catch (Exception e) {
			logger.fatal(prjName+e.getMessage(), e);
		}

		//if(logger.isInfoEnabled())
			logger.debug(prjName+" business service started debug-----------------");
			logger.info(prjName+" business service started info-----------------");
			logger.warn(prjName+" business service started warn-----------------");
			logger.error(prjName+" business service started error-----------------");
	}

}
