package com.xywy.askforexpert.appcommon.net.retrofitWrapper;


import com.xywy.askforexpert.BuildConfig;

/**
 * Created by bobby on 16/6/17.
 */
public class ApiConstants {

    /**
     * 服务端API版本号
     */
    public static final String API_VERSION = BuildConfig.SERVER_API_VERSION;


    public static final String COMMON_URL = "app/" + API_VERSION + "/index.interface.php";

    public static final String CLUB_URL = "app/" + API_VERSION + "/club/doctorApp.interface.php";

    public static final String ZIXUN_URL = "app/" + API_VERSION + "/zixun/index.interface.php";

    public static final String OTHER_HOST = "app/other/index.interface.php";

    public static final String FORCE_NETWORK = "Cache-Control: no-cache";
    public static final String bookingMethod = "api.php/jtys/userOrders/index";
    public static final String loginMethod = CLUB_URL;
    public static final String contactMethod = COMMON_URL ;

    public static final String answerMethod = COMMON_URL ;

    public static final String promotionInfoMethod = CLUB_URL;

    public static final String doctorCircleMethod = COMMON_URL ;
    public static final String FOLLOW_LIST = COMMON_URL;





}
