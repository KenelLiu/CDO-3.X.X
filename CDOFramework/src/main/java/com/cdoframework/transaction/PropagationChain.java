package com.cdoframework.transaction;

public interface PropagationChain {
	/**
	 * 将【事务传播属性】增加到所有数据库的传播链上
	 * 进入TransName方法时 使用
	 * @param propagation
	 */
	public void addPropagation(Propagation propagation);
	
	/**
	 * 将事务传播属性增加到指定数据库的传播链上
	 * @param strDataGroupId 对应的数据库Id
	 * @param propagation
	 */
	public void addPropagation(String strDataGroupId,Propagation propagation);
	
	/**
	 * 将所有数据库的传播链上的栈顶【事务传播属性】移除
	 * 退出 TransName方法时,将栈顶的事务传播属性从每个数据库上弹出
	 */
	public void popPropagation();
	
	/**
	 * 将指定数据库的传播链上的栈顶【事务传播属性】移除
	 * @param strDataGroupId 对应的数据库Id
	 * @return
	 */
	public void popPropagation(String strDataGroupId);
		
	/**
	 * huo'qu指定数据库的传播链上的栈顶【事务传播属性】
	 * @param strDataGroupId 对应的数据库
	 * @return
	 */
	public Propagation getPropagation(String strDataGroupId);
	


}
