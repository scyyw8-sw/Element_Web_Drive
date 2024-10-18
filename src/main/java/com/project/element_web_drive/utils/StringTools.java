package com.project.element_web_drive.utils;

import org.apache.commons.lang3.RandomStringUtils;

import javax.management.relation.RelationNotFoundException;

public class StringTools {

    // generate random number
    public static final String getRandomNumber(Integer count){
        return RandomStringUtils.random(count, false, true);

    }
}
