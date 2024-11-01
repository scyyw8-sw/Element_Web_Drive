package com.project.element_web_drive.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SysSettingsDto implements Serializable {
    private String registerEMailTitle="Email Verification Code";
    private String registerEmailContent="Hello, Your Email Verification Code is :%s, valid for 15 minutes.";
    private Integer userInitUseSpace=5;
    public String getRegisterEMailTitle() {
        return registerEMailTitle;
    }

    public void setRegisterEMailTitle(String registerEMailTitle) {
        this.registerEMailTitle = registerEMailTitle;
    }

    public String getRegisterEmailContent() {
        return registerEmailContent;
    }

    public void setRegisterEmailContent(String registerEmailContent) {
        this.registerEmailContent = registerEmailContent;
    }

    public Integer getUserInitUseSpace() {
        return userInitUseSpace;
    }

    public void setUserInitUseSpace(Integer userInitUseSpace) {
        this.userInitUseSpace = userInitUseSpace;
    }
}
