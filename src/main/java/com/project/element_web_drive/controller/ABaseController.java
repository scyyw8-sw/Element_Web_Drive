package com.project.element_web_drive.controller;

import com.project.element_web_drive.entity.constants.Constants;
import com.project.element_web_drive.entity.dto.SessionWebUserDto;
import com.project.element_web_drive.enums.ResponseCodeEnum;

import com.project.element_web_drive.entity.vo.ResponseVO;
import com.project.element_web_drive.utils.StringTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;


public class ABaseController {

    protected static final String STATUC_SUCCESS="success";
    protected static final String STATUC_ERROR="error";
    private static final Logger logger = LoggerFactory.getLogger(ABaseController.class);


    protected <T> ResponseVO getSuccessResponseVO(T t) {
        ResponseVO<T> responseVO = new ResponseVO<>();
        responseVO.setStatus(STATUC_SUCCESS);
        responseVO.setCode(ResponseCodeEnum.CODE_200.getCode());
        responseVO.setInfo(ResponseCodeEnum.CODE_200.getMsg());
        responseVO.setData(t);
        return responseVO;
    }

    protected void readFile(HttpServletResponse response, String filePath){
        if(!StringTools.pathIsOk(filePath)){
            return;
        }
        OutputStream out=null;
        FileInputStream input=null;
        try {
            File file=new File(filePath);
            if(!file.exists()){
                return;
            }
            input=new FileInputStream(file);
            byte[] byteData=new byte[1024];
            out=response.getOutputStream();
            int len=0;
            while((len=input.read(byteData))!=-1){
                out.write(byteData,0,len);
            }
            out.flush();
        }catch (Exception e){
            logger.error("读取文件异常",e);
        }finally {
            if(out!=null){
                try {
                    out.close();
                }catch (IOException e){
                    logger.error("IO异常",e);
                }
            }
            if(input!=null){
                try {
                    input.close();
                }catch (IOException e){
                    logger.error("IO异常",e);
                }
            }
        }
    }


    protected SessionWebUserDto getUserInfoFromSession(HttpSession session){
        SessionWebUserDto sessionWebUserDto=(SessionWebUserDto) session.getAttribute(Constants.session_key);
        return sessionWebUserDto;
    }


}
