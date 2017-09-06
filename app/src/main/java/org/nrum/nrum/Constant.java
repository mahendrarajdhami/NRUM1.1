package org.nrum.nrum;

/**
 * Created by rajdhami on 5/23/2017.
 */
public class Constant {

    public static final String NRUM_EMAIL = "info.nrum@gmail.com";
    public static final String SERVER = "http://www.nrum.org";
    public static final String API_ID_ACCESS_TOKEN_STRING = "?app_id=NRUM-ANDROID-APP&access_token=qwertyuiopasdfghjklzxcv";
    //public static final String SERVER = "http://192.168.0.100/bs.dev/nrum";
//    public static final String SERVER = "http://192.168.100.2/bs.dev/nrum";
    public static final String API_END = SERVER + "/dataProvider";
    public static final String NEWS_API = API_END + "/newsApi/lists"+API_ID_ACCESS_TOKEN_STRING;
    public static final String BANNER_API = API_END + "/bannerApi/lists"+API_ID_ACCESS_TOKEN_STRING;
    public static final String NOTICE_API = API_END + "/noticeApi/lists"+API_ID_ACCESS_TOKEN_STRING;
    public static final String PAGE_API = API_END + "/pageApi/lists"+API_ID_ACCESS_TOKEN_STRING;
    public static final String POST_API = API_END + "/postApi/lists"+API_ID_ACCESS_TOKEN_STRING;
    public static final String FAQ_API = API_END + "/faqApi/lists"+API_ID_ACCESS_TOKEN_STRING;

    public static final String UPLOAD_PATH = SERVER + "/uploads";
    public static final String UPLOAD_PATH_BANNER = UPLOAD_PATH + "/company_1/banner";
    public static final String UPLOAD_PATH_NEWS = UPLOAD_PATH + "/company_1/news";
    public static final String UPLOAD_PATH_PAGE = UPLOAD_PATH + "/company_1/page";
    public static final String UPLOAD_PATH_POST = UPLOAD_PATH + "/company_1/posts";
    public static final String UPLOAD_PATH_MEMBER = UPLOAD_PATH + "/company_1/member";

    public static final int BANNER_TRANSITION_DURATION = 5000;
    public static final int NOTICE_BUTTON_BLINK_DURATION = 5000;
    public static final int LIMIT_BANNER_FETCH = 3;
    public static final int LIMIT_MAIN_NEWS_FETCH =4;

    public static final String  CURRENT_LANG_ID_2 = "2";
    public static final String  CURRENT_LANG_ID_3 = "3";
    //page_id
    public static final int  PAGE_ID_ABOUT = 1;
}
