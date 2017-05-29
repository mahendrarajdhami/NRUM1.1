package org.nrum.nrum;

/**
 * Created by rajdhami on 5/23/2017.
 */
public class Constant {
    public static final String SERVER = "http://192.168.0.100/bs.dev/nrum";
//    public static final String SERVER = "http://192.168.100.2/bs.dev/nrum";
    public static final String API_END = SERVER + "/dataProvider";
    public static final String NEWS_API = API_END + "/newsApi/lists";
    public static final String BANNER_API = API_END + "/bannerApi/lists";
    public static final String NOTICE_API = API_END + "/noticeApi/lists";

    public static final String UPLOAD_PATH = SERVER + "/uploads";
    public static final String UPLOAD_PATH_BANNER = UPLOAD_PATH + "/company_1/banner";
    public static final String UPLOAD_PATH_NEWS = UPLOAD_PATH + "/company_1/news";


    public static final int BANNER_TRANSITION_DURATION = 5000;
    public static final int NOTICE_BUTTON_BLINK_DURATION = 5000;
    public static final int BANNER_FETCH_LIMIT = 3;

    public static final String  CURRENT_LANG_ID_2 = "2";
    public static final String  CURRENT_LANG_ID_3 = "3";
}
