package com.cdoframework.cdolib.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TransName {
	//=======TransName的名字，默认是空串=========//
	String name() default ""; 
	//====是否在进入TransName定义的方法前,自动开启事务,默认自动开启事务,且传播属性值为Propagation.REQUIRED=======//
	boolean autoStartTransaction() default true;
}
