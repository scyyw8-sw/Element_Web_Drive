package com.project.element_web_drive.service.Impl;

import com.project.element_web_drive.entity.constants.Constants;
import com.project.element_web_drive.entity.po.EmailCode;
import com.project.element_web_drive.entity.po.UserInfo;
import com.project.element_web_drive.entity.query.EmailCodeQuery;
import com.project.element_web_drive.entity.query.UserInfoQuery;
import com.project.element_web_drive.entity.vo.PaginationResultVO;
import com.project.element_web_drive.exception.BusinessException;
import com.project.element_web_drive.mappers.EmailCodeMapper;
import com.project.element_web_drive.mappers.UserInfoMapper;
import com.project.element_web_drive.service.EmailCodeService;
import com.project.element_web_drive.utils.StringTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class EmailCodeServiceImpl implements EmailCodeService {


    private static final Logger logger= LoggerFactory.getLogger(EmailCodeServiceImpl.class);
    @Resource
    private UserInfoMapper<UserInfo, UserInfoQuery> userInfoMapper;
    @Resource
    private EmailCodeMapper<EmailCode, EmailCodeQuery> emailCodeMapper;

    @Resource
    private JavaMailSender javaMailSender;


    /**
     *根据条件查询列表
     */
    public List<EmailCode> findListByParam(EmailCodeQuery query) {
        return this.emailCodeMapper.selectList(query);
    }
    /**
     *根据条件查询数量
     */
    public Integer findCountByParam(EmailCodeQuery query) {
        return this.emailCodeMapper.selectCount(query);
    }

    @Override
    public PaginationResultVO<EmailCode> findListByPage(EmailCodeQuery query) {
        return null;
    }

    /**
     *新增
     */
    public Integer add(EmailCode bean) {
        return this.emailCodeMapper.insert(bean);
    }
    /**
     *批量新增
     */
    public Integer addBatch(List<EmailCode> listBean) {
        if (listBean==null || listBean.isEmpty()) {
            return 0;
        }
        return this.emailCodeMapper.insertBatch(listBean);
    }
    /**
     *批量新增或修改
     */
    public Integer addOrUpdateBatch(List<EmailCode> listBean) {
        if (listBean==null || listBean.isEmpty()) {
            return 0;
        }
        return this.emailCodeMapper.insertOrUpdateBatch(listBean);
    }

    /**
     *根据EmailAndCode查询
     */
    public EmailCode getEmailCodeByEmailAndCode(String email, String code) {
        return this.emailCodeMapper.selectByEmailAndCode(email, code);
    }
    /**
     *根据EmailAndCode更新
     */
    public Integer updateEmailCodeByEmailAndCode( EmailCode bean , String email, String code) {
        return this.emailCodeMapper.updateByEmailAndCode(bean,email, code);
    }

    @Override
    public Integer deleteEmailCodeByEmailAndCode(String email, String code) {
        return this.emailCodeMapper.deleteByEmailAndCode(email, code);
    }


    @Override
    public void sendEmailCode(String email, Integer type) {
        if (type == Constants.Zero) {
            UserInfo userInfo = userInfoMapper.selectByEmail(email);
            if (userInfo != null) {
                try {
                    throw new BusinessException("Email has existed");
                } catch (BusinessException e) {
                    e.printStackTrace();
                }
            }
        }


        String code = StringTools.getRandomNumber(Constants.LENGTH_5);

        //TODO sent email verification

        // set the previous verification code to invalid
        EmailCode emailCode = new EmailCode();
        emailCode.setCode(code);

        //将之前的验证码置为无效
        emailCodeMapper.disableEmailCode(email);

        emailCode.setCode(code);
        emailCode.setEmail(email);
        emailCode.setStatus(Constants.Zero);
        emailCode.setCreateTime(new Date());
        emailCodeMapper.insert(emailCode);
    }

    @Override
    public void checkCode(String email, String code) {

    }
}
