package com.cdoframework.transaction;

public class PropagationChainThreadLocal implements PropagationChain {

	private static final ThreadLocal<PropagationChain> propagationManager = new ThreadLocal<PropagationChain>(){
		
		protected PropagationChain initialValue() {
	       
	        return new PropagationChainImpl();
	     }
	};
	@Override
	public void addPropagation(Propagation propagation) {
		propagationManager.get().addPropagation(propagation);
	}
	
	@Override
	public void addPropagation(String strDataGroupId, Propagation propagation) {
		propagationManager.get().addPropagation(strDataGroupId, propagation);
	}
	
	@Override
	public void popPropagation() {
		propagationManager.get().popPropagation();
		
	}
	
	@Override
	public void popPropagation(String strDataGroupId) {
		propagationManager.get().popPropagation(strDataGroupId);		
	}
	
	@Override
	public Propagation getPropagation(String strDataGroupId) {
		
		return propagationManager.get().getPropagation(strDataGroupId);
	}





}
