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
	public Propagation getPropagation(String strDataGroupId) {
		
		return propagationManager.get().getPropagation(strDataGroupId);
	}

}
