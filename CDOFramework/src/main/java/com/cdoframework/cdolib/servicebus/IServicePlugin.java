
package com.cdoframework.cdolib.servicebus;

import com.cdoframework.cdolib.base.Return;
import com.cdoframework.cdolib.data.cdo.CDO;

/**
 * @author Frank
 * 
 * 本接口定义插件对象的行为
 */
public interface IServicePlugin{

	String getPluginName();
		
	Return handleTrans(CDO cdoRequest,CDO cdoResponse);
	
	Return handleDataTrans(CDO cdoRequest,CDO cdoResponse);
	
}
