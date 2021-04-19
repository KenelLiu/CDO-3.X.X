package com.cdoframework.transaction;

/**
 * 
 * @author KenelLiu
 *
 */
public interface TransactionChain {
	 /**
	  * 获取当前TransName定义的方法上 autoStartTransaction属性值,
	  * 即为stack栈顶元素,且元素不出栈
	  * @param strDataGroupId
	  * @return
	  */
	 boolean getCurrentAutoStartTransaction(String strDataGroupId);
	 /**
	  * 在进入 transName方法前调用
	  * 将transName定义方法上的autoStartTransaction属性值入栈
	  * 1 若stack不存在,首次创建,并把内容入栈
	  * 2 若stack已存在,直接内容入栈
	  * @param strDataGroupId
	  */
	 void pushAutoStartTransaction(String strDataGroupId,boolean autoStartTransaction);	 
	 /**
	  * 在退出 transName方法后调用
	  * 将transName定义方法上的autoStartTransaction属性值出栈
	  * @param strDataGroupId
	  * @throws SQLException 
	  */
	 void popAutoStartTransaction(String strDataGroupId);	 
}
