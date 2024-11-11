package com.project.element_web_drive.entity.constants;

public class Constants {
    public static final String CHECK_CODE_KEY = "check_code_key";

    public static final String CHECK_CODE_KEY_EMAIL = "check_code_key_email";

    public static final String FILE_FOLDER_FILE = "/file/";
    public static final String FILE_FOLDER_AVATAR_NAME= "avatar/";
    public static final String AVATAR_SUFFIX= ".jpg";
    public static final String AVATAR_DEFAULT= "default_avatar.jpg";


    public static final String session_key= "session_key";;


    public static final Integer LENGTH_5  = 5;
    public static final Integer LENGTH_10 = 10;
    public static final Integer LENGTH_15 = 15;
    public static final Integer LENGTH_20 = 20;
    public static final Integer Zero = 0;

    public static final Long MB = 1024*1024L;

    public static final String SESSION_KEY = "session_key";

    public static final Integer REDIS_KEY_EXPIRES_ONE_MIN = 60;
    public static final Integer REDIS_KEY_EXPIRES_DAY = REDIS_KEY_EXPIRES_ONE_MIN * 24 * 60;

    public static String redis_key_sys_setting = "element_web_drive:sys:setting:";  // initialization
    public static String redis_key_user_space_use = "element_web_drive:user:space:use:";

    public static String VIEW_OBJ_RESULT_KEY = "result";
}

