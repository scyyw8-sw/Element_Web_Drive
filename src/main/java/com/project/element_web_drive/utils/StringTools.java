package com.project.element_web_drive.utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.codec.digest.DigestUtils;

import javax.management.relation.RelationNotFoundException;

public class  StringTools {

    // generate random number
    public static final String getRandomNumber(Integer count){
        return RandomStringUtils.random(count, false, true);

    }

    public static boolean isEmpty(String str){
        if(null==str || "".equals(str) || "null".equals(str) || "\u0000".equals(str)){
            return true;
        }else if("".equals(str.trim())){
            return true;
        }
        return false;
    }

    public static String encodeByMD5(String originString) {
        return StringTools.isEmpty(originString) ? null : DigestUtils.md5Hex(originString);
    }



    public static Boolean pathIsOk(String filePath){
        if (StringTools.isEmpty(filePath)){
            return true;
        }
        if(filePath.contains("../")||filePath.contains("..\\")){
            return false;
        }
        return true;
    }


}
