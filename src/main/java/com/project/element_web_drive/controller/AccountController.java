package com.project.element_web_drive.controller;

import com.project.element_web_drive.entity.constants.Constants;
import com.project.element_web_drive.entity.dto.CreateImageCode;
import com.project.element_web_drive.entity.vo.ResponseVO;
import com.project.element_web_drive.exception.BusinessException;
import com.project.element_web_drive.service.EmailCodeService;
import com.project.element_web_drive.service.UserInfoService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@RestController()
public class AccountController {

    @Resource
    private UserInfoService userInfoService;


    @Resource
    private EmailCodeService emailCodeService;

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
        } else {
            session.setAttribute(Constants.CHECK_CODE_KEY_EMAIL, code);
        }
        vCode.write(response.getOutputStream());
    }


    // Interface2: send email code

    @RequestMapping("/sendEmailCode")
    public ResponseVO sendEmailCode(HttpSession session,String email, String checkCode, Integer type) throws BusinessException {
        try {
            if(!checkCode.equalsIgnoreCase((String)session.getAttribute(Constants.CHECK_CODE_KEY_EMAIL))){
                throw new BusinessException("图片验证码不正确");
            }
            emailCodeService.sendEmailCode(email,type);
            //this line
            // return getSuccessResponseVO(null);
            return null;
        }finally {
            session.removeAttribute(Constants.CHECK_CODE_KEY_EMAIL);
        }

    }


}