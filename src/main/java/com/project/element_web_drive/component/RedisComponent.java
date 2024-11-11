package com.project.element_web_drive.component;

import com.project.element_web_drive.entity.dto.SysSettingsDto;
import com.project.element_web_drive.entity.dto.UserSpaceDto;
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

    public void saveUserSpaceUse(String userId, UserSpaceDto userSpaceDto){
        redisUtils.setex(Constants.redis_key_user_space_use+userId, userSpaceDto, Constants.REDIS_KEY_EXPIRES_DAY);

    }


    public UserSpaceDto getUserSpaceUse(String userId){
        UserSpaceDto spaceDto = (UserSpaceDto) redisUtils.get(Constants.redis_key_user_space_use+userId);
        if(spaceDto==null){
            spaceDto=new UserSpaceDto();
            //TODO 查询当前用户已经上传文件大小总和
            spaceDto.setUseSpace(0L);
            spaceDto.setTotalSpace(getSysSettingDto().getUserInitUseSpace() * Constants.MB);
            saveUserSpaceUse(userId,spaceDto);
        }
        return spaceDto;
    }
}
