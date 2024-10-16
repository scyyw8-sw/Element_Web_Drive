package com.project.element_web_drive.annotation;

import org.springframework.web.bind.annotation.Mapping;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Mapping

@Target({ElementType.METHOD})
public @interface GlobalInterceptor {
}
