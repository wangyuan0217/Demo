package com.judd.trump.app;

/**
 * Created by Administrator on 2017/12/6.
 */

public class Config {
    //App名字
    public static final String APP_NAME_CH = "测试";
    public static final String APP_NAME_EN = "demo";
    //分页
    public static final int PAGE_FROM = 1;
    //下拉刷新的延时时间
    public static final int REFRESH_DELAY = 500;
    //是否显示列表加载更多结束的footer
    public static final boolean HIDE_LIST_END_TIP = true;

    //okHttp
    public static final int HTTP_TIMEOUT_READ = 20;
    public static final int HTTP_TIMEOUT_WTITE = 20;
    public static final int HTTP_TIMEOUT_CONNECTION = 15;

    public static final String BASE_URL = "";

    //pref
    public static final String PREF_KEY_COOKIE = "cookie";
}
