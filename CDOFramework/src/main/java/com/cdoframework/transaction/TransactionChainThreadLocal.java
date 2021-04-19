package com.cdoframework.transaction;

/**
 * 
 * @author Kenel
 * 考虑 threadLocal 里value 内存泄露问题
 */
public class TransactionChainThreadLocal implements TransactionChain {
	private static final ThreadLocal<TransactionChainImpl> transChain = new ThreadLocal<TransactionChainImpl>(){
		
		protected TransactionChainImpl initialValue() {
	       
	        return new TransactionChainImpl();
	     }
	};
	
	@Override
	public boolean getCurrentAutoStartTransaction(String strDataGroupId){
		TransactionChainImpl trans=transChain.get();
		try{
			return trans.getCurrentAutoStartTransaction(strDataGroupId);
		}finally{
			if(trans.isEmpty()){
				transChain.remove();
			}				
		}		
	}
	
	@Override
	public void pushAutoStartTransaction(String strDataGroupId,boolean autoStartTransaction){
		TransactionChainImpl trans=transChain.get();
		try{
			trans.pushAutoStartTransaction(strDataGroupId,autoStartTransaction);
		}finally{
			if(trans.isEmpty()){
				transChain.remove();
			}				
		}	
	}
	
	public void popAutoStartTransaction(String strDataGroupId){
		TransactionChainImpl trans=transChain.get();
		try{
			trans.popAutoStartTransaction(strDataGroupId);
		}finally{
			if(trans.isEmpty()){
				transChain.remove();				
			}				
		}	
	}
	
	public ThreadLocal<TransactionChainImpl> getThreadLocal(){
		return transChain;
	}
}
