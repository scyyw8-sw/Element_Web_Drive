package com.project.element_web_drive.annotation;

import org.springframework.web.bind.annotation.Mapping;

import java.lang.annotation.*;



@Mapping

@Target({ElementType.METHOD})//annotation will be used in the method(it can be used in package,method,field)
@Retention(RetentionPolicy.RUNTIME)// lifetime.  The compilation process starts when execution begins
@Documented
public @interface GlobalInterceptor {
    //校验参数
    boolean checkParams() default false;

    //校验登录
    boolean checkLogin() default true;

    //校验超级管理员
    boolean checkAdmin() default false;
}
