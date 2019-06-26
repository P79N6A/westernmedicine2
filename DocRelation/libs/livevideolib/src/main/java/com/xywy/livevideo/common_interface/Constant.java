package com.xywy.livevideo.common_interface;

/**
 * Created by zhangzheng on 2017/3/6.
 */
public class Constant {
    public static final String testApiUrl = "http://test.api.wws.xywy.com/api.php/";
    public static final String formalApiUrl = "http://api.wws.xywy.com/api.php/";
    public static final String testH5Url = "http://test.tv.xywy.com/";
    public static final String formalH5Url = "http://tv.xywy.com/";
    public static String PRIMARY_URL = formalApiUrl;
    public static String H5_URL = formalH5Url;
    //消费明细
    public static final String CONSUME_DETAIL = "gift-record/index";
    //关注
    public static final String METHOD_CONCERN = "live/concern/index";
    //进入直播房间
    public static final String METHOD_ENTER_LIVE_ROOM = "live/live/enterRoom";
    //获取房间信息
    public static final String METHOD_GET_ROOM_INFO = "live/room/getRoom";
    //获取礼物列表
    public static final String METHOD_GET_GIFT_LIST = "live/room/getGift";
    //赠送礼物
    public static final String METHOD_GIVE_GIFT = "live/live/giveGift";
    //离开直播间
    public static final String METHOD_LEAVE_LIVE_ROOM = "live/live/leaveRoom";
    //获取主播详情
    public static final String METHOD_GET_HOST_INFO = "live/room/liveList";
    //创建直播间
    public static final String METHOD_CREATE_LIVE_ROOM = "live/live/createRoom";

    public static final int CONSTANT_UN_CONCERN = 0;//未关注
    public static final int CONSTANT_CONCERN_ALREADY = 1; //已经关注
    public static final int CONSTANT_CONCERN_INTER = 2;//互相关注
    public static final int CONSTANT_LIVE_STATE_LIVE = 1;//直播
    public static final int CONSTANT_LIVE_STATE_END = 0;//直播结束，点播



}
