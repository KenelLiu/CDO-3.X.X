package com.cdoframework.transaction;

public interface PropagationChain {
	/**
	 * 将事务传播属性保存在每个数据库的上
	 * @param propagation
	 */
	public void addPropagation(Propagation propagation);
	/**
	 * 
	 * @param strDataGroupId 对应的数据库
	 * @return
	 */
	public Propagation getPropagation(String strDataGroupId);
	
}
