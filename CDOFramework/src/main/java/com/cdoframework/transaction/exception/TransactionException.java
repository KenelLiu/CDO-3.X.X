package com.cdoframework.transaction.exception;

import com.cdoframework.cdolib.base.Return;

public class TransactionException extends RuntimeException {

	private static final long serialVersionUID = -4827973186515205031L;
	private Return ret;
	public TransactionException(Return ret,String message) {
        super(message);
        this.ret=ret;
    }
 
    public TransactionException(Return ret,Throwable cause) {
        super(cause);
        this.ret=ret;
    }
    public TransactionException(Return ret,String message,Throwable cause) {
        super(message,cause);
        this.ret=ret;
    }

	public Return getRet() {
		return ret;
	}
    
}
