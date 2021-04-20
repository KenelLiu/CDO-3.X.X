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
	//======当自动开启事务为true[即autoStartTransaction=true],存在多个数据源时,可以指定对某个数据库开启事务,默认会为所有数据源开启事务=========//
	//======当自动开启事务为false[即autoStartTransaction=false],该参数无意义,忽略=========//
	String dataGroupId() default ""; 
}
