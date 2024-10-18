package com.project.element_web_drive.service;

import com.project.element_web_drive.entity.po.EmailCode;
import com.project.element_web_drive.entity.query.EmailCodeQuery;
import com.project.element_web_drive.entity.vo.PaginationResultVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EmailCodeService {

    /**
     *根据条件查询列表
     */
    List<EmailCode> findListByParam(EmailCodeQuery query);

    /**
     *根据条件查询数量
     */
    Integer findCountByParam(EmailCodeQuery query);

    /**
     *分页查询
     */
    PaginationResultVO<EmailCode> findListByPage(EmailCodeQuery query);

    /**
     *新增
     */
    Integer add(EmailCode bean);

    /**
     *批量新增
     */
    Integer addBatch(List<EmailCode> listBean);

    /**
     *批量新增或修改
     */
    Integer addOrUpdateBatch(List<EmailCode> listBean);


    /**
     *根据EmailAndCode查询
     */
    EmailCode getEmailCodeByEmailAndCode(String email, String code);

    /**
     *根据EmailAndCode更新
     */
    Integer updateEmailCodeByEmailAndCode( EmailCode bean , String email, String code);

    /**
     *根据EmailAndCode删除
     */
    Integer deleteEmailCodeByEmailAndCode(String email, String code);

    void sendEmailCode(String email,Integer type);

    void checkCode(String email,String code);

}
