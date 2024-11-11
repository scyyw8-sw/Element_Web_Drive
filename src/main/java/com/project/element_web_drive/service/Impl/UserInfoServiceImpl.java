package com.project.element_web_drive.service.Impl;
import com.project.element_web_drive.component.RedisComponent;
import com.project.element_web_drive.entity.config.AppConfig;
import com.project.element_web_drive.entity.constants.Constants;
import com.project.element_web_drive.entity.dto.QQInfoDto;
import com.project.element_web_drive.entity.dto.SessionWebUserDto;
import com.project.element_web_drive.entity.dto.SysSettingsDto;
import com.project.element_web_drive.entity.dto.UserSpaceDto;
import com.project.element_web_drive.entity.vo.PaginationResultVO;
import com.project.element_web_drive.entity.query.SimplePage;
import com.project.element_web_drive.enums.PageSize;
import com.project.element_web_drive.entity.po.UserInfo;
import com.project.element_web_drive.entity.query.UserInfoQuery;
import javax.annotation.Resource;
import javax.mail.Session;

import com.project.element_web_drive.enums.UserStatusEnum;
import com.project.element_web_drive.exception.BusinessException;
import com.project.element_web_drive.mappers.UserInfoMapper;
import com.project.element_web_drive.service.EmailCodeService;
import com.project.element_web_drive.service.UserInfoService;
import com.project.element_web_drive.utils.JsonUtils;
import com.project.element_web_drive.utils.OKHttpUtils;
import com.project.element_web_drive.utils.StringTools;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.reflection.ArrayUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: ServiceImpl
 * @Author: element_web_drive
 * @Date: 2024/10/18
*/
@Service("userInfoService")
public class UserInfoServiceImpl implements UserInfoService {

	private static final Logger logger = LoggerFactory.getLogger(UserInfoServiceImpl.class);

	@Resource
	private UserInfoMapper<UserInfo,UserInfoQuery> userInfoMapper;

    @Resource
    private EmailCodeService emailCodeService;

	@Resource
	private RedisComponent redisComponent;


	@Resource
	private AppConfig appConfig;



	/**
 *根据条件查询列表
*/
	public List<UserInfo> findListByParam(UserInfoQuery query) {
		return this.userInfoMapper.selectList(query);
	}
/**
 *根据条件查询数量
*/
	public Integer findCountByParam(UserInfoQuery query) {
		return this.userInfoMapper.selectCount(query);
	}
/**
 *分页查询
*/
	public PaginationResultVO<UserInfo> findListByPage(UserInfoQuery query) {
		Integer count=this.findCountByParam(query);
		Integer pageSize=query.getPageSize()==null?PageSize.SIZE15.getSize():query.getPageSize();
		SimplePage page=new SimplePage(query.getPageNo(),count,pageSize);
		query.setSimplePage(page);
		List<UserInfo> list=this.findListByParam(query);
		PaginationResultVO<UserInfo> result=new PaginationResultVO(count,page.getPageSize(),page.getPageNo(),page.getPageTotal(),list);
		return result;
	}
/**
 *新增
*/
	public Integer add(UserInfo bean) {
		return this.userInfoMapper.insert(bean);
	}
/**
 *批量新增
*/
	public Integer addBatch(List<UserInfo> listBean) {
		if (listBean==null || listBean.isEmpty()) {
			return 0;
		}
		return this.userInfoMapper.insertBatch(listBean);
	}
/**
 *批量新增或修改
*/
	public Integer addOrUpdateBatch(List<UserInfo> listBean) {
		if (listBean==null || listBean.isEmpty()) {
			return 0;
		}
		return this.userInfoMapper.insertOrUpdateBatch(listBean);
	}

/**
 *根据UserId查询
*/
	public UserInfo getUserInfoByUserId(String userId) {
		return this.userInfoMapper.selectByUserId(userId);
	}
/**
 *根据UserId更新
*/
	public Integer updateUserInfoByUserId( UserInfo bean , String userId) {
		return this.userInfoMapper.updateByUserId(bean,userId);
	}

/**
 *根据UserId删除
*/
	public Integer deleteUserInfoByUserId(String userId) {
		return this.userInfoMapper.deleteByUserId(userId);
	}

/**
 *根据Email查询
*/
	public UserInfo getUserInfoByEmail(String email) {
		return this.userInfoMapper.selectByEmail(email);
	}
/**
 *根据Email更新
*/
	public Integer updateUserInfoByEmail( UserInfo bean , String email) {
		return this.userInfoMapper.updateByEmail(bean,email);
	}

/**
 *根据Email删除
*/
	public Integer deleteUserInfoByEmail(String email) {
		return this.userInfoMapper.deleteByEmail(email);
	}

/**
 *根据QqOpenId查询
*/
	public UserInfo getUserInfoByQqOpenId(String qqOpenId) {
		return this.userInfoMapper.selectByQqOpenId(qqOpenId);
	}
/**
 *根据QqOpenId更新
*/
	public Integer updateUserInfoByQqOpenId( UserInfo bean , String qqOpenId) {
		return this.userInfoMapper.updateByQqOpenId(bean,qqOpenId);
	}

/**
 *根据QqOpenId删除
*/
	public Integer deleteUserInfoByQqOpenId(String qqOpenId) {
		return this.userInfoMapper.deleteByQqOpenId(qqOpenId);
	}

/**
 *根据NickName查询
*/
	public UserInfo getUserInfoByNickName(String nickName) {
		return this.userInfoMapper.selectByNickName(nickName);
	}
/**
 *根据NickName更新
*/
	public Integer updateUserInfoByNickName( UserInfo bean , String nickName) {
		return this.userInfoMapper.updateByNickName(bean,nickName);
	}

/**
 *根据NickName删除
*/
	public Integer deleteUserInfoByNickName(String nickName) {
		return this.userInfoMapper.deleteByNickName(nickName);
	}


	@Transactional(rollbackFor = Exception.class)
	public void register(String email, String nickName, String password, String emailCode){
		UserInfo userInfo = this.userInfoMapper.selectByEmail(email);
		if(userInfo!=null){
            try {
                throw new BusinessException("邮箱已存在");
            } catch (BusinessException e) {
                throw new RuntimeException(e);
            }
        }
		UserInfo nickNameUser = this.userInfoMapper.selectByNickName(nickName);
		if(null != nickNameUser){
			try {
				throw new BusinessException("昵称已存在");
			} catch (BusinessException e) {
				throw new RuntimeException(e);
			}
		}

		//校验邮箱验证码
		emailCodeService.checkCode(email, emailCode);
		String userId = StringTools.getRandomNumber(Constants.LENGTH_10);
		userInfo = new UserInfo();
		userInfo.setUserId(userId);
		userInfo.setNickName(nickName);
		userInfo.setEmail(email);
		userInfo.setPassword(StringTools.encodeByMD5(password));
		userInfo.setJoinTime(new Date());
		userInfo.setStatus(UserStatusEnum.ENABLE.getStatus());
		userInfo.setUseSpace(0L);
		SysSettingsDto sysSettingsDto = redisComponent.getSysSettingDto();
		userInfo.setTotalSpace(sysSettingsDto.getUserInitUseSpace() * Constants.MB);
		this.userInfoMapper.insert(userInfo);

	}

	public SessionWebUserDto login(String email, String password) {
		UserInfo userInfo = this.userInfoMapper.selectByEmail(email);
		if(userInfo==null || !userInfo.getPassword().equals(password)){
            try {
                throw new BusinessException("账号或者密码错误");
            } catch (BusinessException e) {
                throw new RuntimeException(e);
            }
        }

		if(UserStatusEnum.DISABLE.equals(userInfo.getStatus())){
            try {
                throw new BusinessException("账号已禁用");
            } catch (BusinessException e) {
                throw new RuntimeException(e);
            }
        }
		UserInfo updateInfo = new UserInfo();
		updateInfo.setLastLoginTime(new Date());
		this.userInfoMapper.updateByUserId(updateInfo,userInfo.getUserId());

		SessionWebUserDto sessionWebUserDto = new SessionWebUserDto();
		sessionWebUserDto.setNickName(userInfo.getNickName());
		sessionWebUserDto.setUserId(userInfo.getUserId());
		if(ArrayUtils.contains(appConfig.getAdminEmails().split(","), email)){
			sessionWebUserDto.setAdmin(true);
		}else{
			sessionWebUserDto.setAdmin(false);
		}

		UserSpaceDto userSpaceDto = new UserSpaceDto();
		//TODO 查询当前用户已经上传文件大小总和
		// userSpaceDto.setUseSpace(userInfo.getUseSpace());
		userSpaceDto.setTotalSpace(userInfo.getTotalSpace());
		redisComponent.saveUserSpaceUse(userInfo.getUserId(), userSpaceDto);

		return sessionWebUserDto;
	}


	@Override
	@Transactional(rollbackFor = Exception.class)
	public void resetPassword(String email, String password, String emailCode) {
		UserInfo userInfo = this.userInfoMapper.selectByEmail(email);
		if(userInfo==null){
			try {
				throw new BusinessException("邮箱账号不存在");
			} catch (BusinessException e) {
				throw new RuntimeException(e);
			}
		}
		emailCodeService.checkCode(email, emailCode);
		UserInfo updateInfo = new UserInfo();
		updateInfo.setPassword(StringTools.encodeByMD5(password));
		this.userInfoMapper.updateByEmail(updateInfo,email);

	}


	@Override
	// 如果你的用户通过社交账号登录，例如微信登录，微信作为身份提供商会颁发自己的 Access Token
	// 你的应用可以利用 Access Token 调用微信相关的 API。
	public SessionWebUserDto qqLogin(String code){
		//自动注册
		//第一步 通过回调code,获取accessToken
		String accessToken = getQQAccessToken(code);
		//第二步 获取qq OpenId
		String openId = getQQOpenId(accessToken);
		UserInfo user = this.userInfoMapper.selectByQqOpenId(openId);
		String avatar = null;

		if(user == null){
			//第三步 获取qq用户信息
			QQInfoDto qqInfoDto=getQQUserInfo(accessToken,openId);
			user=new UserInfo();
			String nickName=qqInfoDto.getNickName();

			nickName=nickName.length()>Constants.LENGTH_20?nickName.substring(0,Constants.LENGTH_20):nickName;
			avatar=StringUtils.isEmpty(qqInfoDto.getFigureurl_qq_2())?qqInfoDto.getFigureurl_qq_1():qqInfoDto.getFigureurl_qq_2();

			Date curDate=new Date();

			user.setQqOpenId(openId);
			user.setJoinTime(curDate);
			user.setNickName(nickName);
			user.setQqAvatar(avatar);
			user.setUserId(StringTools.getRandomNumber(Constants.LENGTH_10));
			user.setLastLoginTime(curDate);
			user.setStatus(UserStatusEnum.ENABLE.getStatus());
			user.setUseSpace(0L);
			user.setTotalSpace(redisComponent.getSysSettingDto().getUserInitUseSpace()*Constants.MB);
			this.userInfoMapper.insert(user);
			user=userInfoMapper.selectByQqOpenId(openId);
		}else{
			UserInfo updateInfo=new UserInfo();
			updateInfo.setLastLoginTime(new Date());
			avatar= user.getQqAvatar();
			this.userInfoMapper.updateByQqOpenId(updateInfo,openId);
		}

		SessionWebUserDto sessionWebUserDto = new SessionWebUserDto();
		sessionWebUserDto.setUserId(user.getUserId());
		sessionWebUserDto.setNickName(user.getNickName());
		sessionWebUserDto.setAvatar(avatar);

		if(ArrayUtils.contains(appConfig.getAdminEmails().split(","), user.getEmail()==null?"":user.getEmail())){
			sessionWebUserDto.setAdmin(true);
		}else {
			sessionWebUserDto.setAdmin(false);
		}

		UserSpaceDto userSpaceDto = new UserSpaceDto();

		//TODO 获取用户已使用的空间
		// Long useSpace=fileInfoMapper.selectUseSpace(user.getUserId());
		userSpaceDto.setUseSpace(0L);
		userSpaceDto.setTotalSpace(user.getTotalSpace());
		redisComponent.saveUserSpaceUse(user.getUserId(), userSpaceDto);

		//第三步 获取用户的qq基本信息
		return sessionWebUserDto;
	}


	private String getQQAccessToken(String code){
		String accessToken=null;
		String url=null;
		try{
			url=String.format(appConfig.getQqUrlAccessToken(),appConfig.getQqAppId(),appConfig.getQqAppKey(),code, URLEncoder.encode(appConfig.getQqUrlRedirect(),"utf-8"));

		}catch (UnsupportedEncodingException e){
			logger.error("encode失败",e);
		}

        String tokenResult= null;
        try {
            tokenResult = OKHttpUtils.getRequest(url);
        } catch (BusinessException e) {
            throw new RuntimeException(e);
        }
        if(tokenResult==null || tokenResult.indexOf(Constants.VIEW_OBJ_RESULT_KEY) != -1){
			logger.error("获取qqtoken失败：{}",tokenResult);
            try {
                throw new BusinessException("获取qqToken失败");
            } catch (BusinessException e) {
                throw new RuntimeException(e);
            }
        }
		String[] params=tokenResult.split("&");
		if(params != null && params.length > 0){
			for(String p:params){
				if(p.indexOf("access_token") != -1){
					accessToken=p.split("=")[-1]; // [1]
					break;
				}
			}
		}
		return accessToken;
	}


	private String getQQOpenId(String accessToken){
		String url=String.format(appConfig.getQqUrlOpenid(),accessToken);
        String openIdResult= null;
        try {
            openIdResult = OKHttpUtils.getRequest(url);
        } catch (BusinessException e) {
            throw new RuntimeException(e);
        }
        String tmpJson=this.getQQResp(openIdResult);
		if(tmpJson==null){
			logger.error("调取qq接口获取openid失败：tempJson{}",tmpJson);
            try {
                throw new BusinessException("调qq接口获取openid失败");
            } catch (BusinessException e) {
                throw new RuntimeException(e);
            }
        }
		Map jsonData= JsonUtils.convertJson2Obj(tmpJson,Map.class);
		if(jsonData==null || jsonData.containsKey(Constants.VIEW_OBJ_RESULT_KEY)){
			logger.error("调qq接口获取openid失败:{}",jsonData);
            try {
                throw new BusinessException("调qq接口获取openid失败");
            } catch (BusinessException e) {
                throw new RuntimeException(e);
            }
        }
		return String.valueOf(jsonData.get("openid"));
	}


	private String getQQResp(String result){
		if(org.apache.commons.lang3.StringUtils.isNotBlank(result)){
			int pos=result.indexOf("callback"); // 解析获取openid
			if(pos!=-1){
				int start=result.indexOf("(");
				int end=result.lastIndexOf(")");
				String jsonStr=result.substring(start+1,end-1);
				return jsonStr;
			}
		}
		return null;
	}


	private QQInfoDto getQQUserInfo(String accessToken, String qqopenId){
		String url=String.format(appConfig.getQqUrlUserInfo(),accessToken,appConfig.getQqAppId(),qqopenId);
        String response= null;
        try {
            response = OKHttpUtils.getRequest(url);
        } catch (BusinessException e) {
            throw new RuntimeException(e);
        }
        if(StringUtils.isNotBlank(response)){
			QQInfoDto qqInfoDto=JsonUtils.convertJson2Obj(response,QQInfoDto.class);
			if(qqInfoDto.getRet() != 0){
				logger.error("qqInfo:{}",response);
                try {
                    throw new BusinessException("调qq接口获取用户信息异常");
                } catch (BusinessException e) {
                    throw new RuntimeException(e);
                }
            }
			return qqInfoDto;
		}
        try {
            throw new BusinessException("调qq接口获取用户信息异常");
        } catch (BusinessException e) {
            throw new RuntimeException(e);
        }
    }

}
