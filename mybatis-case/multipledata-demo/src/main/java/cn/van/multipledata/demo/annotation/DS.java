package cn.van.multipledata.demo.annotation;

import cn.van.multipledata.demo.enums.DynamicDSEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DS {
    DynamicDSEnum value() default DynamicDSEnum.MASTER;

    boolean clear() default true;
}
