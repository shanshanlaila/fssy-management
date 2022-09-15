/**
 * 
 */
package com.fssy.shareholder.management.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于标识方法是否记录auditlog</br>
 * 运行时有效，作用于方法
 * 
 * @author Solomon
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RequiredLog
{
	/**
	 * 操作名称
	 */
	String value() default "";
}
