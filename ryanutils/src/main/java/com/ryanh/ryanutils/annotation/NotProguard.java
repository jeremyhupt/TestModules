package com.ryanh.ryanutils.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author:胡仲俊
 * Date: 2017 - 01 - 03
 * Des: NotProguard, Means not proguard something, like class, method, field
 * FIXME
 * Todo
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.FIELD})
public @interface NotProguard {

}