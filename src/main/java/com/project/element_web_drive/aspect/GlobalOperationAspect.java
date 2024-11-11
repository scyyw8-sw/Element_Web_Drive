package com.project.element_web_drive.aspect;

import com.project.element_web_drive.annotation.GlobalInterceptor;
import com.project.element_web_drive.annotation.verifyParam;
import com.project.element_web_drive.entity.constants.Constants;
import com.project.element_web_drive.entity.dto.SessionWebUserDto;
import com.project.element_web_drive.enums.ResponseCodeEnum;
import com.project.element_web_drive.exception.BusinessException;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.project.element_web_drive.utils.VerifyUtils;
import com.project.element_web_drive.utils.StringTools;

import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Parameter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;



@Component("globalOperationAspect")//表面spring管理
@Aspect
public class GlobalOperationAspect  {

    private static final Logger logger= LoggerFactory.getLogger(GlobalOperationAspect.class);
    private static final String TYPE_INTEGER="java.lang.Integer";
    private static final String TYPE_STRING="java.lang.String";
    private static final String TYPE_LONG="java.lang.Long";
    //请求拦截 切面拦截annotation里的
     @Pointcut("@annotation(com.project.element_web_drive.annotation.GlobalInterceptor)")
     private void requestInterceptor(){
     }


    @Before("requestInterceptor()")
    public void interceptorDo(JoinPoint joinPoint) throws BusinessException {
        try {
            Object target=joinPoint.getTarget();
            Object[] arguments =joinPoint.getArgs();
            String methodName=joinPoint.getSignature().getName();
            Class<?>[] parameterTypes=((MethodSignature) joinPoint.getSignature()).getMethod().getParameterTypes();
            Method method=target.getClass().getMethod(methodName,parameterTypes);
            GlobalInterceptor interceptor=method.getAnnotation(GlobalInterceptor.class);

            //校验参数
            if(null == interceptor){
                return;
            }

            // 校验登录
            if(interceptor.checkLogin()|| interceptor.checkAdmin()){
                checkLogin(interceptor.checkAdmin());
            }

            if(interceptor.checkParams()){
                validateParams(method,arguments);
            }
        }catch (BusinessException e){
            logger.error("全局拦截异常",e);
            throw e;
        }catch (Exception e){
            logger.error("全局拦截异常",e);
            throw new BusinessException(ResponseCodeEnum.CODE_500);
        }catch (Throwable e){
            logger.error("全局拦截异常",e);
            throw new BusinessException(ResponseCodeEnum.CODE_500);
        }
    }
    private void validateParams(Method method,Object[] arguments) throws BusinessException{
        Parameter[] parameters= method.getParameters();
        for(int i=0;i<parameters.length;i++){
            Parameter parameter=parameters[i];
            Object value=arguments[i];
            verifyParam verifyParam=parameter.getAnnotation(com.project.element_web_drive.annotation.verifyParam.class);
            if(verifyParam==null){
                continue;
            }
            //基本数据类型
            if(TYPE_STRING.equals(parameter.getParameterizedType().getTypeName())||TYPE_LONG.equals(parameter.getParameterizedType().getTypeName())||TYPE_INTEGER.equals(parameter.getParameterizedType().getTypeName())){
                checkValue(value,verifyParam);
            }else {
                checkObjValue(parameter,value);
            }
        }
    }
    private void checkObjValue(Parameter parameter,Object value) throws BusinessException {
        try {
            String typeName=parameter.getParameterizedType().getTypeName();
            Class classz=Class.forName(typeName);
            Field[] fields=classz.getDeclaredFields();
            for (Field field:fields){
                verifyParam verifyParam=field.getAnnotation(com.project.element_web_drive.annotation.verifyParam.class);
                if(verifyParam==null){
                    continue;
                }
                field.setAccessible(true);
                Object resultValue=field.get(value);
                checkValue(resultValue,verifyParam);
            }
        }catch (BusinessException e){
            logger.error("校验参数失败",e);
            throw e;
        }catch (Exception e){
            logger.error("校验参数失败",e);
            try {
                throw new BusinessException(ResponseCodeEnum.CODE_600);
            } catch (BusinessException ex) {
                ex.printStackTrace();
            }
        }
    }
    private void checkValue(Object value, verifyParam verifyParam) throws BusinessException {
        Boolean isEmpty =value==null|| StringTools.isEmpty(value.toString());
        Integer length=value==null?0:value.toString().length();

        //校验空
        if(isEmpty && verifyParam.required()){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        //校验长度
        if(!isEmpty && (verifyParam.max()!=-1 && verifyParam.max()<length||verifyParam.min()!=-1&&verifyParam.min()>length)){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        //校验正则
        if(!isEmpty && !StringTools.isEmpty(verifyParam.regex().getRegex())&&!VerifyUtils.verify(verifyParam.regex(),String.valueOf(value))){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
    }


    private void checkLogin(Boolean checkAdmin){
        //取得request对象
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session=request.getSession();
        SessionWebUserDto userDto=(SessionWebUserDto) session.getAttribute(Constants.session_key);

        if(null== userDto){
            try {
                throw new BusinessException(ResponseCodeEnum.CODE_901);
            } catch (BusinessException e) {
                e.printStackTrace();
            }
        }

        if(checkAdmin && !userDto.getAdmin()){
            try {
                throw new BusinessException(ResponseCodeEnum.CODE_404);
            } catch (BusinessException e) {
                e.printStackTrace();
            }
        }
    }
 }

