package com.project.element_web_drive.annotation;

import com.project.element_web_drive.enums.VerifyRegexEnum;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME )
public @interface verifyParam {
    int min() default -1;
    int max() default -1;
    boolean required() default false;
    //定义正则 默认不校验
    VerifyRegexEnum regex() default VerifyRegexEnum.NO;

}
