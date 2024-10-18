package com.project.element_web_drive.component;

import com.project.element_web_drive.entity.dto.SysSettingsDto;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import com.project.element_web_drive.entity.constants.Constants;

@Component("RedisComponent")
public class RedisComponent {
    @Resource
    private RedisUtils redisUtils;
    public SysSettingsDto getSysSettingDto(){
        SysSettingsDto sysSettingsDto=(SysSettingsDto)redisUtils.get(Constants.redis_key_sys_setting);
        if(null == sysSettingsDto){
            sysSettingsDto= new SysSettingsDto();
            redisUtils.set( Constants.redis_key_sys_setting,sysSettingsDto);
        }
        return sysSettingsDto;

    }
}
