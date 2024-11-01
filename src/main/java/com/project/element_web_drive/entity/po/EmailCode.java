package com.project.element_web_drive.entity.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.element_web_drive.enums.DateTimePatternEnum;
import com.project.element_web_drive.utils.DateUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class EmailCode {
    private String email;
    private String code;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private Integer status;


    public String getEmail() {
        return this.email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setCreateTime(Date createTime){
        this.createTime=createTime;
    }
    public Date getCreateTime(){
        return this.createTime;
    }
    public void setStatus(Integer status){
        this.status=status;
    }
    public Integer getStatus(){
        return this.status;
    }
    @Override
    public String toString(){
        return "email:"+(email == null ? "空" : email)+",code:"+(code == null ? "空" : code)+",create_time:"+(DateUtils.format(createTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()) == null ? "空" : createTime)+",status:"+(status == null ? "空" : status);
    }
}