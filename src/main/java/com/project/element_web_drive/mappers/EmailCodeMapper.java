package com.project.element_web_drive.mappers;

import io.lettuce.core.dynamic.annotation.Param;

public interface EmailCodeMapper<T,P> extends BaseMapper<T,P> {

    /**
     * 根据EmailAndCode更新
     */
    Integer updateByEmailAndCode(@Param("bean") T t, @Param("email") String email, @Param("code") String code);


    /**
     * 根据EmailAndCode删除
     */
    Integer deleteByEmailAndCode(@Param("email") String email, @Param("code") String code);


    /**
     * 根据EmailAndCode获取对象
     */
    T selectByEmailAndCode(@Param("email") String email, @Param("code") String code);

    void disableEmailCode(@Param("email") String email);


}
