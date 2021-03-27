package com.cdoframework.transaction;

public enum Propagation{
	/**
	 * if there is an active transaction, then it creates a new one if nothing existed.
	 *  Otherwise, the business logic appends to the currently active transaction
	 */
	REQUIRED(Transaction.PROPAGATION_REQUIRED),
	/**
	 * first checks if an active transaction exists. 
	 * If a transaction exists, then the existing transaction will be used.
	 * If there isn't a transaction, it is executed non-transactional.
	 */
	SUPPORTS(Transaction.PROPAGATION_SUPPORTS),
	/**
	 * if there is an active transaction, then it will be used. 
	 * If there isn't an active transaction,then throws an exception
	 */
	MANDATORY(Transaction.PROPAGATION_MANDATORY),
	/**
	 * transactional logic with NEVER propagation, throws an exception if there's an active transaction
	 */
	NEVER(Transaction.PROPAGATION_NEVER),
	/**
	 * at first suspends the current transaction if it exists, 
	 * then the business logic is executed without a transaction
	 */
	NOT_SUPPORTED(Transaction.PROPAGATION_NOT_SUPPORTED),
	/**
	 * suspends the current transaction if it exists and then creates a new one
	 */
	REQUIRES_NEW(Transaction.PROPAGATION_REQUIRES_NEW),
	/**
	 * checks if a transaction exists, then if yes, it marks a savepoint. 
	 * This means if our business logic execution throws an exception, 
	 * then transaction rollbacks to this savepoint. 
	 * If there's no active transaction, it works like REQUIRED.
	 */
	NESTED(Transaction.PROPAGATION_NESTED);
	
	private final byte value;

	Propagation(byte value) { this.value = value; }

	public byte value() { return this.value; }
	
}
