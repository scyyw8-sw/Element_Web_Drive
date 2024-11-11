package com.project.element_web_drive.entity.po;
import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import com.project.element_web_drive.enums.DateTimePatternEnum;
import com.project.element_web_drive.utils.DateUtils;

public class UserInfo implements Serializable {
/**
 * @Description: 
 * @Author: element_web_drive
 * @Date: 2024/10/18
*/
/**
 *用户ID
*/
	private String userId;

/**
 *用户信息
*/
	private String nickName;

/**
 *邮箱
*/
	private String email;

/**
 *qqOpenId
*/
	private String qqOpenId;

/**
 *qq头像
*/
	private String qqAvatar;

/**
 *密码
*/
	private String password;

/**
 *加入时间
*/
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date joinTime;

/**
 *最后登录时间
*/
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date lastLoginTime;

/**
 *0:禁用
1:启用
*/
	private Integer status;

/**
 *使用空间单位byte
*/
	private Long useSpace;

/**
 *总空间
*/
// change String to Long
	private Long totalSpace;

	 public void setUserId(String userId){
		this.userId=userId;
	 }
	 public String getUserId(){
		return this.userId;
	 }
	 public void setNickName(String nickName){
		this.nickName=nickName;
	 }
	 public String getNickName(){
		return this.nickName;
	 }
	 public void setEmail(String email){
		this.email=email;
	 }
	 public String getEmail(){
		return this.email;
	 }
	 public void setQqOpenId(String qqOpenId){
		this.qqOpenId=qqOpenId;
	 }
	 public String getQqOpenId(){
		return this.qqOpenId;
	 }
	 public void setQqAvatar(String qqAvatar){
		this.qqAvatar=qqAvatar;
	 }
	 public String getQqAvatar(){
		return this.qqAvatar;
	 }
	 public void setPassword(String password){
		this.password=password;
	 }
	 public String getPassword(){
		return this.password;
	 }
	 public void setJoinTime(Date joinTime){
		this.joinTime=joinTime;
	 }
	 public Date getJoinTime(){
		return this.joinTime;
	 }
	 public void setLastLoginTime(Date lastLoginTime){
		this.lastLoginTime=lastLoginTime;
	 }
	 public Date getLastLoginTime(){
		return this.lastLoginTime;
	 }
	 public void setStatus(Integer status){
		this.status=status;
	 }
	 public Integer getStatus(){
		return this.status;
	 }
	 public void setUseSpace(Long useSpace){
		this.useSpace=useSpace;
	 }
	 public Long getUseSpace(){
		return this.useSpace;
	 }
	 public void setTotalSpace(Long totalSpace){
		this.totalSpace=totalSpace;
	 }
	 public Long getTotalSpace(){
		return this.totalSpace;
	 }
	@Override
	 public String toString(){
		return "user_id:"+(userId == null ? "空" : userId)+",nick_name:"+(nickName == null ? "空" : nickName)+",email:"+(email == null ? "空" : email)+",qq_open_id:"+(qqOpenId == null ? "空" : qqOpenId)+",qq_avatar:"+(qqAvatar == null ? "空" : qqAvatar)+",password:"+(password == null ? "空" : password)+",join_time:"+(DateUtils.format(joinTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()) == null ? "空" : joinTime)+",last_login_time:"+(DateUtils.format(lastLoginTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()) == null ? "空" : lastLoginTime)+",status:"+(status == null ? "空" : status)+",use_space:"+(useSpace == null ? "空" : useSpace)+",total_space:"+(totalSpace == null ? "空" : totalSpace);
	}
}