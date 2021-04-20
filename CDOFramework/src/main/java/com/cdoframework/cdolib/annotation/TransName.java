package com.cdoframework.cdolib.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TransName {
	//=======TransName的名字，1 用户定义了名称,则使用用户指定名称.2 当为空串时,则会获取java方法名作为名称=========//
	String name() default ""; 
	//======是否自动开启事务,默认自动开启事务,且事务得传播属性值为Propagation.REQUIRED======================//
	boolean autoStartTransaction() default true;
	//======当自动开启事务为true[即autoStartTransaction=true],存在多个数据源时,可以指定对某个数据库开启事务====//
	//======默认空串会为所有数据源开启事务,即会占用每个数据源一个连接,直到退出方法=================================//
	//======当自动开启事务为false[即autoStartTransaction=false],该参数无意义,忽略====================//
	String dataGroupId() default ""; 
}
