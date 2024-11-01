package com.project.element_web_drive.enums;

public enum VerifyRegexEnum {
    NO("","不校验"),
    EMAIL("^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$","邮箱"),
    PASSWORD("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,}$","只能是数字字母特殊字符8-18位");

    private String regex;
    private String desc;

    VerifyRegexEnum(String regex,String desc){
        this.regex=regex;
        this.desc=desc;

    }
    public String getRegex(){
        return regex;
    }

    public String getDesc() {
        return desc;
    }
}
