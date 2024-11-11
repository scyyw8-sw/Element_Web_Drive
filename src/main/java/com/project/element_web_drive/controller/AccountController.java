package com.project.element_web_drive.controller;

import com.project.element_web_drive.annotation.GlobalInterceptor;
import com.project.element_web_drive.annotation.verifyParam;
import com.project.element_web_drive.component.RedisComponent;
import com.project.element_web_drive.entity.config.AppConfig;
import com.project.element_web_drive.entity.constants.Constants;
import com.project.element_web_drive.entity.dto.CreateImageCode;
import com.project.element_web_drive.entity.dto.SessionWebUserDto;
import com.project.element_web_drive.entity.dto.UserSpaceDto;
import com.project.element_web_drive.entity.po.UserInfo;
import com.project.element_web_drive.entity.vo.ResponseVO;
import com.project.element_web_drive.enums.VerifyRegexEnum;
import com.project.element_web_drive.exception.BusinessException;
import com.project.element_web_drive.service.EmailCodeService;
import com.project.element_web_drive.service.UserInfoService;
import com.project.element_web_drive.utils.StringTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;


@RestController()
public class AccountController extends ABaseController{

	private static Logger logger = LoggerFactory.getLogger(AccountController.class);

	@Resource
	private UserInfoService userInfoService;
	private static final String CONTENT_TYPE="Content-Type";
	private static final String CONTENT_TYPE_VALUE="application/json;charset=UTF-8";

	@Resource
	private EmailCodeService emailCodeService;

	@Resource
	private AppConfig appConfig;

	@Resource
	private RedisComponent redisComponent;



	/**
	 * Verification code is returned based on the request type and stored in session
	 *
	 * @param type 0:log in or sign up  1:send email verification code  default:0
	 */
	// Interface1: generate/acquire verification code
	@GetMapping("/checkCode")
	public void checkCode(HttpServletResponse response, HttpSession session
			, @RequestParam(value = "type", required = false) Integer type) throws IOException {
		CreateImageCode vCode = new CreateImageCode(130, 38, 5, 10);

		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache"); //响应消息不能缓存
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");

		String code = vCode.getCode();
		if (type == null || type == 0) {
			session.setAttribute(Constants.CHECK_CODE_KEY, code);
		}else{
			session.setAttribute(Constants.CHECK_CODE_KEY_EMAIL, code);
		}
		vCode.write(response.getOutputStream());
	}


	// Interface2: send email code

	@RequestMapping("/sendEmailCode")
	@GlobalInterceptor(checkParams = true, checkLogin = false)
	public ResponseVO sendEmailCode(HttpSession session,
									@verifyParam(required = true, regex = VerifyRegexEnum.EMAIL, max = 150) String email,
									@verifyParam(required = true) String checkCode,
									@verifyParam(required = true) Integer type) throws BusinessException {
		try {
			if(!checkCode.equalsIgnoreCase((String)session.getAttribute(Constants.CHECK_CODE_KEY_EMAIL))){
				throw new BusinessException("图片验证码不正确");
			}
			emailCodeService.sendEmailCode(email,type);
			//this line
			return getSuccessResponseVO(null);
		}finally {
			session.removeAttribute(Constants.CHECK_CODE_KEY_EMAIL);
		}
	}

	@RequestMapping("/register")
	@GlobalInterceptor(checkParams = true,checkLogin = false)
	public ResponseVO register(HttpSession session,
							   @verifyParam(required = true,regex = VerifyRegexEnum.EMAIL,max = 150) String email,
							   @verifyParam(required = true) String nickName,
							   @verifyParam(required = true,regex = VerifyRegexEnum.PASSWORD,min = 8,max = 18) String password,
							   @verifyParam(required = true) String emailCode,
							   @verifyParam(required = true) String checkCode) throws BusinessException {
		try {
			if(!checkCode.equalsIgnoreCase((String)session.getAttribute(Constants.CHECK_CODE_KEY))){
				throw new BusinessException("图片验证码不正确");
			}
			userInfoService.register(email,nickName,password,emailCode);
			return getSuccessResponseVO(null);
		}finally {
			session.removeAttribute(Constants.CHECK_CODE_KEY);
		}
	}

	@RequestMapping("/login")
	@GlobalInterceptor(checkParams = true, checkLogin = false)
	public ResponseVO login(HttpSession session,
							   @verifyParam(required = true) String email,
							   @verifyParam(required = true) String password,
							   @verifyParam(required = true) String checkCode) throws BusinessException {
		try {
			if(!checkCode.equalsIgnoreCase((String)session.getAttribute(Constants.CHECK_CODE_KEY))){
				throw new BusinessException("图片验证码不正确");
			}
			SessionWebUserDto sessionWebUserDto = userInfoService.login(email,password);
			session.setAttribute(Constants.SESSION_KEY,sessionWebUserDto);
			return getSuccessResponseVO(sessionWebUserDto);
		}finally {
			session.removeAttribute(Constants.CHECK_CODE_KEY);
		}
	}

	//未登录更改密码
	@RequestMapping("/resetPwd")
	@GlobalInterceptor(checkParams = true,checkLogin = false)
	public ResponseVO resetPwd(HttpSession session,
							   @verifyParam(required = true,regex = VerifyRegexEnum.EMAIL,max = 150) String email,
							   @verifyParam(required = true,regex = VerifyRegexEnum.PASSWORD,min = 8,max = 18) String password,
							   @verifyParam(required = true) String emailCode,
							   @verifyParam(required = true) String checkCode) throws BusinessException {
		try {
			if(!checkCode.equalsIgnoreCase((String)session.getAttribute(Constants.CHECK_CODE_KEY))){
				throw new BusinessException("图片验证码不正确");
			}
			userInfoService.resetPassword(email,password,emailCode);
			return getSuccessResponseVO(null);
		}finally {
			session.removeAttribute(Constants.CHECK_CODE_KEY);
		}
	}


	@RequestMapping("/getAvatar/{userId}")
	@GlobalInterceptor(checkParams = true, checkLogin = false)
	public void getAvatar(HttpServletResponse response, @verifyParam(required = true) @PathVariable("userId") String userId) {
		String avatarFolderName = Constants.FILE_FOLDER_FILE + Constants.FILE_FOLDER_AVATAR_NAME;
		File folder = new File(appConfig.getProjectFolder() + avatarFolderName);
		if(!folder.exists()){
			folder.mkdirs();
		}
		String avatarPath = appConfig.getProjectFolder() + avatarFolderName + userId + Constants.AVATAR_SUFFIX;
		File file = new File(avatarPath);
		if(!file.exists()){
			if(!new File(appConfig.getProjectFolder() + avatarFolderName + Constants.AVATAR_DEFAULT).exists()){
				printNoDefaultImage(response);
			}
			avatarPath = appConfig.getProjectFolder() + avatarFolderName + Constants.AVATAR_DEFAULT;
		}
		response.setContentType("image/jpeg");
		readFile(response,avatarPath);
	}


	private void printNoDefaultImage(HttpServletResponse response){
		response.setHeader(CONTENT_TYPE,CONTENT_TYPE_VALUE);
		response.setStatus(HttpStatus.OK.value());
		PrintWriter writer=null;
		try{
			writer=response.getWriter();
			writer.print("请在头像目录下放置默认头像default_avatar.jpg");
		}catch (Exception e){
			logger.error("输出无默认图片失败",e);
		}finally {
			writer.close();
		}
	}


	@RequestMapping("/getUserInfo")
	@GlobalInterceptor(checkParams = true)
	public ResponseVO getUserInfo(HttpSession session) {
		SessionWebUserDto sessionWebUserDto = getUserInfoFromSession(session);
		return getSuccessResponseVO(sessionWebUserDto);
	}

	@RequestMapping("/getUserSpace")
	@GlobalInterceptor(checkParams = true)
	public ResponseVO getUserSpace(HttpSession session) {
		SessionWebUserDto sessionWebUserDto = getUserInfoFromSession(session);
		UserSpaceDto spaceDto = redisComponent.getUserSpaceUse(sessionWebUserDto.getUserId());
		return getSuccessResponseVO(spaceDto);
	}


	@RequestMapping("/logout")
	public ResponseVO logout(HttpSession session) {
		session.invalidate();
		return getSuccessResponseVO(null);
	}


	@RequestMapping("/updateUserAvatar")
	@GlobalInterceptor(checkParams = true)
	public ResponseVO updateUserAvatar(HttpSession session, MultipartFile avatar)  {
		SessionWebUserDto webUserDto=getUserInfoFromSession(session);
		String baseFolder= appConfig.getProjectFolder()+Constants.FILE_FOLDER_FILE;
		File targetFileFolder=new File(baseFolder+Constants.FILE_FOLDER_AVATAR_NAME);
		if(!targetFileFolder.exists()){
			targetFileFolder.mkdirs();
		}
		File targetFile=new File(targetFileFolder.getPath()+"/"+webUserDto.getUserId()+Constants.AVATAR_SUFFIX);
		try {
			avatar.transferTo(targetFile);
		}catch (Exception e){
			logger.error("上传头像失败",e);
		}
		UserInfo userInfo=new UserInfo();
		userInfo.setQqAvatar("");
		userInfoService.updateUserInfoByUserId(userInfo,webUserDto.getUserId());
		webUserDto.setUserId(null);
		session.setAttribute(Constants.session_key,webUserDto);
		return getSuccessResponseVO(null);
	}



	// 已登录后更改密码
	@GlobalInterceptor(checkParams = true)
	public ResponseVO updatePassword(HttpSession session,
									 @verifyParam(required = true,regex = VerifyRegexEnum.PASSWORD,min = 8,max = 18) String password)
	{
		SessionWebUserDto webUserDto=getUserInfoFromSession(session);
		UserInfo userInfo=new UserInfo();
		userInfo.setPassword(StringTools.encodeByMD5(password));
		userInfoService.updateUserInfoByUserId(userInfo,webUserDto.getUserId());
		return getSuccessResponseVO(null);
	}












}