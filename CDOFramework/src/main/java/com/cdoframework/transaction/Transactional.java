package com.cdoframework.transaction;
/**
 * @author Kenel
 */
public interface Transactional {
	/**
	 * Support a current transaction, create a new one if none exists.
	 * description:
	 *   if there is an active transaction, then it creates a new one if nothing existed.
	 *   Otherwise, the business logic appends to the currently active transaction.
	 */
	byte PROPAGATION_REQUIRED=1;
	/**
	 * Support a current transaction, execute non-transactionally if none exists.
	 * description: 
	 * 	 first checks if an active transaction exists. 
	 * 	 If a transaction exists, then the existing transaction will be used.
	 *   If there isn't a transaction, it is executed non-transactional.
	 */
	byte PROPAGATION_SUPPORTS =2;
	/**
	 * Support a current transaction, throw an exception if none exists.
	 * description: 
	 *   if there is an active transaction, then it will be used. 
	 *   If there isn't an active transaction,then throws an exception
	 */
	byte PROPAGATION_MANDATORY=3;
	/**
	 * Execute non-transactionally, throw an exception if a transaction exists.
	 * description：
	 * 	  transactional logic with NEVER propagation,throws an exception if there's an active transaction
	 */
	byte PROPAGATION_NEVER=4;
	/**
	 * Execute non-transactionally, suspend the current transaction if one exists.
	 * description：
	 * 	 at first suspends the current transaction if it exists, 
	 *   then the business logic is executed without a transaction
	 */
	byte PROPAGATION_NOT_SUPPORTED=5;
	/**
	 * Create a new transaction, and suspend the current transaction if one exists.
	 * description：
	 * 	 suspends the current transaction if it exists and then creates a new one
	 */
	byte PROPAGATION_REQUIRES_NEW=6;
	/**
	 * Execute within a nested transaction if a current transaction exists, behave like REQUIRED otherwise.
	 * description：
	 * 	 checks if a transaction exists, then if yes, it marks a savepoint. 
	 * 	 This means if our business logic execution throws an exception, 
	 * 	 then transaction rollbacks to this savepoint. 
	 * 	 If there's no active transaction, it works like REQUIRED.
	 */
	byte PROPAGATION_NESTED=7;
}
