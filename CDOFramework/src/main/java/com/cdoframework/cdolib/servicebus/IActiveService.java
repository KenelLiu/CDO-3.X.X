package com.cdoframework.cdolib.servicebus;
/**
 * @author Frank
 */
public interface IActiveService extends com.cdoframework.cdolib.base.IActiveObject,ITransService
{
	public boolean isClusterd();
	
	public void setClustered(boolean bIsClustered);
	/**
	 * 设置服务名
	 * @param strServiceName
	 */
	void setServiceName(String strServiceName);
	
	/**
	 * 取服务名
	 * @return
	 */
	String getServiceName();
}
