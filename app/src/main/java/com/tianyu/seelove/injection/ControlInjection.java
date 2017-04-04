package com.tianyu.seelove.injection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 控件注入注解，所有的有此注解的属性都会被自动注入
 * @author shisheng.zhao
 * @date 2015-08-31 15:48:43
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ControlInjection {
    public int value() default 0;
}
