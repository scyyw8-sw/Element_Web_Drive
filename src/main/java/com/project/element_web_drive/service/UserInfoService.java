package com.project.element_web_drive.service;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import com.project.element_web_drive.enums.DateTimePatternEnum;
import com.project.element_web_drive.utils.DateUtils;

import com.project.element_web_drive.entity.vo.PaginationResultVO;
import com.project.element_web_drive.entity.po.UserInfo;
import com.project.element_web_drive.entity.query.UserInfoQuery;
import java.util.List;

/**
 * @Description: Service
 * @Author: element_web_drive
 * @Date: 2024/10/18
*/
public interface UserInfoService{

/**
 *根据条件查询列表
*/
	List<UserInfo> findListByParam(UserInfoQuery query);

/**
 *根据条件查询数量
*/
	Integer findCountByParam(UserInfoQuery query);

/**
 *分页查询
*/
	PaginationResultVO<UserInfo> findListByPage(UserInfoQuery query);

/**
 *新增
*/
	Integer add(UserInfo bean);

/**
 *批量新增
*/
	Integer addBatch(List<UserInfo> listBean);

/**
 *批量新增或修改
*/
	Integer addOrUpdateBatch(List<UserInfo> listBean);


/**
 *根据UserId查询
*/
	UserInfo getUserInfoByUserId(String userId);

/**
 *根据UserId更新
*/
	Integer updateUserInfoByUserId( UserInfo bean , String userId);

/**
 *根据UserId删除
*/
	Integer deleteUserInfoByUserId(String userId);

/**
 *根据Email查询
*/
	UserInfo getUserInfoByEmail(String email);

/**
 *根据Email更新
*/
	Integer updateUserInfoByEmail( UserInfo bean , String email);

/**
 *根据Email删除
*/
	Integer deleteUserInfoByEmail(String email);

/**
 *根据QqOpenId查询
*/
	UserInfo getUserInfoByQqOpenId(String qqOpenId);

/**
 *根据QqOpenId更新
*/
	Integer updateUserInfoByQqOpenId( UserInfo bean , String qqOpenId);

/**
 *根据QqOpenId删除
*/
	Integer deleteUserInfoByQqOpenId(String qqOpenId);

/**
 *根据NickName查询
*/
	UserInfo getUserInfoByNickName(String nickName);

/**
 *根据NickName更新
*/
	Integer updateUserInfoByNickName( UserInfo bean , String nickName);

/**
 *根据NickName删除
*/
	Integer deleteUserInfoByNickName(String nickName);
}
