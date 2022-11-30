package com.lp.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author LuoPing
 * @date 2022/10/14 16:10
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {
    /**
     * 日志内容
     */
    String title() default "";

    /**
     * 功能 业务类型的分类，便于条件筛选
     */
    String businessType() default "" ;

}
