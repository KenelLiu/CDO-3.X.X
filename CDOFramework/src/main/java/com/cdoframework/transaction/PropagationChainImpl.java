package com.cdoframework.transaction;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.apache.log4j.Logger;
import com.cdoframework.cdolib.database.DBPoolManager;

public class PropagationChainImpl implements PropagationChain {
	//================保存每个库的事务传播链===============//
	private Logger logger=Logger.getLogger(TransactionManagerImpl.class);
	private Map<String,Stack<Propagation>> propagationMap=new HashMap<String,Stack<Propagation>>();
	
	@Override
	public void addPropagation(Propagation propagation) {
        try {
        	 Set<String>  DataGroupId=DBPoolManager.getInstances().getHmDBPool().keySet();
        	 for (String strDataGroupId : DataGroupId) {
             	Stack<Propagation> stack=propagationMap.get(strDataGroupId);
                if(stack==null){
                	stack=new Stack<Propagation>();
                }    
                stack.push(propagation);
                propagationMap.put(strDataGroupId, stack); 
			}
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        } 
	}

	@Override
	public Propagation getPropagation(String strDataGroupId) {
        Stack<Propagation> stack=propagationMap.get(strDataGroupId);
        if(stack==null){
        	stack=new Stack<Propagation>();
        } 
        if (stack.isEmpty()) {
        	stack.push(Propagation.REQUIRED);
        }
        return stack.pop();
	}

}
