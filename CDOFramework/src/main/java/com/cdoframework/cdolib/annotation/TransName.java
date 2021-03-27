package com.cdoframework.cdolib.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.cdoframework.transaction.Propagation;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TransName {
	String name() default ""; // TransName的名字，默认是空串
	
	Propagation propagation() default Propagation.REQUIRED;
}
