package com.cdoframework.transaction;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.apache.log4j.Logger;

import com.cdoframework.cdolib.base.Return;
/**
 * 
 * @author Kenel
 *
 */
public class TransactionChainImpl implements TransactionChain {
	private Map<String,Stack<Boolean>> autoStartTransMap=new HashMap<String,Stack<Boolean>>();
	
	public boolean getCurrentAutoStartTransaction(String strDataGroupId){		
		if (isEmptyStack(strDataGroupId)){
			autoStartTransMap.remove(strDataGroupId);
			return false;
        }		
		Stack<Boolean> stack=autoStartTransMap.get(strDataGroupId);
		return stack.peek().booleanValue();
	}

	public void pushAutoStartTransaction(String strDataGroupId,boolean autoStartTransaction){
		Stack<Boolean> stack=autoStartTransMap.get(strDataGroupId);
		if(stack==null){
			stack=new Stack<Boolean>();
		}
		stack.push(autoStartTransaction);
		autoStartTransMap.put(strDataGroupId, stack);
	}	
	
	public void popAutoStartTransaction(String strDataGroupId){
		if(!isEmptyStack(strDataGroupId)){
			Stack<Boolean> stack=autoStartTransMap.get(strDataGroupId);
			stack.pop();
			if(stack.isEmpty()){
				autoStartTransMap.remove(strDataGroupId);
			}
		}		
	 }
	
   public boolean isEmptyStack(String strDataGroupId){
		Stack<Boolean> stack=autoStartTransMap.get(strDataGroupId);
		if (stack==null || stack.isEmpty()){
			return true;
        }	
        return false;
    }
   
   
   public boolean isEmpty(){
	 return   autoStartTransMap.isEmpty(); 
   }
}
