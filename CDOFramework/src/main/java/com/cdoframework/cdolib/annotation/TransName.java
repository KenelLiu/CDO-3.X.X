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
	//====禁止进入该方法前,自动启动事务,创建数据库连接=======//
	boolean denyAutoStartTransaction() default false;
}
