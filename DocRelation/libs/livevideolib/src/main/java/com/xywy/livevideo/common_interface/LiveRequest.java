package com.xywy.livevideo.common_interface;

import com.google.gson.reflect.TypeToken;
import com.xywy.livevideo.LiveManager;
import com.xywy.livevideo.entity.BaseData;
import com.xywy.livevideo.entity.CommonResponseEntity;
import com.xywy.livevideo.entity.CreateLiveRoomRespEntity;
import com.xywy.livevideo.entity.EnterLiveRoomRespEntity;
import com.xywy.livevideo.entity.FinishLiveBean;
import com.xywy.livevideo.entity.GetRoomInfoRespEntity;
import com.xywy.livevideo.entity.GiftListRespEntity;
import com.xywy.livevideo.entity.GiveGiftRespEntity;
import com.xywy.livevideo.entity.HostInfoNoListRespEntity;
import com.xywy.livevideo.entity.HostInfoWithListRespEntity;
import com.xywy.livevideo.entity.ImageBean;
import com.xywy.livevideo.entity.LeaveRoomRespEntity;

import java.io.File;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bobby on 17/2/24.
 */

public class LiveRequest {

    public static void finishLive(Map<String, String> postParms, IDataResponse<BaseData<FinishLiveBean>> resp) {
        Map<String, String> getParamMap = LiveManager.getInstance().getRequest().getCommonGetParams("1563");
        Type type = new TypeToken<BaseData<FinishLiveBean>>() {
        }.getType();
        LiveManager.getInstance().getRequest().post(Constant.PRIMARY_URL, "live/live/endRoom", getParamMap, postParms, resp, type, null, false);
    }

    public static void uploadPic(File file, IDataResponse<BaseData<ImageBean>> resp) {
        Map<String, String> getParamMap = LiveManager.getInstance().getRequest().getCommonGetParams("1248");
        getParamMap.put("version", "1.1");
        Map<String, String> postParams = new HashMap<>();
        postParams.put("thumb", "2");
        Map<String, File> fileMap = new HashMap<String, File>();
        if (file.exists()) {
            fileMap.put("mypic", file);
        }
        Type type = new TypeToken<BaseData<ImageBean>>() {
        }.getType();
        LiveManager.getInstance().getRequest().upload(Constant.PRIMARY_URL, "common/uploadImg/index", fileMap, getParamMap, postParams, resp, type, null, false);
    }

    public void cancel(Object cancelObject) {
        LiveManager.getInstance().getRequest().cancel(cancelObject);
    }

    public static final int CONCERN_TYPE_DOC = 1;
    public static final int CONCERN_TYPE_NORMAL_USER = 2;
    public static final int CONCERN_ACTION = 1;
    public static final int CONCERN_ACTION_CANCEL = 2;

    /**
     * 关注，取消关注
     *
     * @param userId        用户ID
     * @param toUserId      主播ID
     * @param type          用户类型，1，医生，2，普通用户
     * @param action        1，关注，2，取消关注
     * @param iDataResponse
     */
    public static void concern(String userId, String toUserId, int type, int action, IDataResponse<CommonResponseEntity> iDataResponse) {
        Map<String, String> getParams = LiveManager.getInstance().getRequest().getCommonGetParams("1551");
        getParams.put("version", "1.0");
        Map<String, String> postParams = new HashMap<>();
        postParams.put("user_id", userId);
        postParams.put("touserid", toUserId);
        postParams.put("type", type + "");
        postParams.put("action", action + "");
        LiveManager.getInstance().getRequest().post(Constant.PRIMARY_URL, Constant.METHOD_CONCERN,
                getParams, postParams, iDataResponse, CommonResponseEntity.class, "flag", false);
    }

    /**
     * 进入直播间
     *
     * @param roomId        房间号
     * @param userId        用户ID
     * @param iDataResponse
     */
    public static void enterLiveRoom(String roomId, String userId, IDataResponse<EnterLiveRoomRespEntity> iDataResponse) {
        Map<String, String> getParams = LiveManager.getInstance().getRequest().getCommonGetParams("1557");
        getParams.put("version", "1.0");
        Map<String, String> postParams = new HashMap<>();
        postParams.put("rid", roomId);
        postParams.put("user_id", userId);
        LiveManager.getInstance().getRequest().post(Constant.PRIMARY_URL, Constant.METHOD_ENTER_LIVE_ROOM,
                getParams, postParams, iDataResponse, EnterLiveRoomRespEntity.class, "flag", false);
    }

    /**
     * 获取房间信息
     *
     * @param roomId        房间号
     * @param iDataResponse
     */
    public static void getRoomInfo(String roomId, IDataResponse<GetRoomInfoRespEntity> iDataResponse) {
        Map<String, String> getParams = LiveManager.getInstance().getRequest().getCommonGetParams("1560");
        getParams.put("version", "1.0");
        getParams.put("id", roomId);
        getParams.put("api", "1560");
        Map<String, String> postParams = new HashMap<>();

        LiveManager.getInstance().getRequest().get(Constant.PRIMARY_URL, Constant.METHOD_GET_ROOM_INFO,
                getParams, postParams, iDataResponse, GetRoomInfoRespEntity.class, "flag", false);
    }

    /**
     * 获取礼物列表
     *
     * @param page          页码
     * @param iDataResponse
     */
    public static void getGiftList(int page, final IDataResponse<GiftListRespEntity> iDataResponse) {
        GiftListRespEntity giftList = LiveManager.getInstance().getConfig().giftList;
        if (giftList!=null&&giftList.getCode() == 10000){
            if (iDataResponse!=null)
                iDataResponse.onReceived(giftList);
            return;
        }
        Map<String, String> getParams = LiveManager.getInstance().getRequest().getCommonGetParams("1561");
        getParams.put("version", "1.0");
        getParams.put("page", page + "");
        Map<String, String> postParams = new HashMap<>();

        LiveManager.getInstance().getRequest().get(Constant.PRIMARY_URL, Constant.METHOD_GET_GIFT_LIST,
                getParams, postParams, new
                        IDataResponse<GiftListRespEntity>() {
                            @Override
                            public void onReceived(GiftListRespEntity gifts) {
                                if (gifts !=null&& gifts.getCode() == 10000){
                                    LiveManager.getInstance().getConfig().giftList= gifts;
                                    if (iDataResponse!=null)
                                        iDataResponse.onReceived(gifts);
                                    return;
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                if (iDataResponse!=null)
                                    iDataResponse.onError(e);
                            }
                        }, GiftListRespEntity.class, "flag", false);
    }

    /**
     * 赠送礼物
     *
     * @param roomId        房间号
     * @param giftId        礼物ID
     * @param userId        用户ID
     * @param toUserId      主播ID
     * @param iDataResponse
     */
    public static void giveGift(String roomId, String giftId, String userId, String toUserId, IDataResponse<GiveGiftRespEntity> iDataResponse) {
        Map<String, String> getParams = LiveManager.getInstance().getRequest().getCommonGetParams("1559");
        getParams.put("version", "1.0");
        Map<String, String> postParams = new HashMap<>();
        postParams.put("rid", roomId);
        postParams.put("gid", giftId);
        postParams.put("user_id", userId);
        postParams.put("touserid", toUserId);

        LiveManager.getInstance().getRequest().post(Constant.PRIMARY_URL, Constant.METHOD_GIVE_GIFT,
                getParams, postParams, iDataResponse, GiveGiftRespEntity.class, "flag", false);
    }

    /**
     * 离开直播间
     *
     * @param roomId        房间ID
     * @param userId        用户ID
     * @param iDataResponse
     */
    public static void leaveLiveRoom(String roomId, String userId, IDataResponse<LeaveRoomRespEntity> iDataResponse) {
        Map<String, String> getParams = LiveManager.getInstance().getRequest().getCommonGetParams("1558");
        getParams.put("version", "1.0");
        Map<String, String> postParams = new HashMap<>();
        postParams.put("rid", roomId);
        postParams.put("user_id", userId);
        LiveManager.getInstance().getRequest().post(Constant.PRIMARY_URL, Constant.METHOD_LEAVE_LIVE_ROOM,
                getParams, postParams, iDataResponse, LeaveRoomRespEntity.class, "flag", false);
    }

    /**
     * 获取主播信息,不带list
     *
     * @param userId        主播ID
     * @param page          页码
     * @param iDataResponse
     */
    public static void getHostInfo(String id, String userId, int page, IDataResponse<HostInfoNoListRespEntity> iDataResponse) {
        Map<String, String> getParams = LiveManager.getInstance().getRequest().getCommonGetParams("1568");
        getParams.put("version", "1.0");
        getParams.put("id", id);
        getParams.put("page", page + "");
        if (userId != null && !"".equals(userId)) {
            getParams.put("user_id", userId);
        }

        Map<String, String> postParams = new HashMap<>();

        LiveManager.getInstance().getRequest().get(Constant.PRIMARY_URL, Constant.METHOD_GET_HOST_INFO,
                getParams, postParams, iDataResponse, HostInfoNoListRespEntity.class, "flag", false);
    }

    /**
     * 获取主播信息,带list
     *
     * @param userId        主播ID
     * @param page          页码
     * @param isList        1
     * @param iDataResponse
     */
    public static void getHostInfoList(String id, String userId, int page, int isList, IDataResponse<HostInfoWithListRespEntity> iDataResponse) {
        Map<String, String> getParams = LiveManager.getInstance().getRequest().getCommonGetParams("1568");
        getParams.put("version", "1.0");
        getParams.put("id", id);
        getParams.put("page", page + "");
        getParams.put("is_list", "" + isList);
        Map<String, String> postParams = new HashMap<>();
        if (userId != null && !"".equals(userId)) {
            getParams.put("user_id", userId);
        }

        LiveManager.getInstance().getRequest().get(Constant.PRIMARY_URL, Constant.METHOD_GET_HOST_INFO,
                getParams, postParams, iDataResponse, HostInfoWithListRespEntity.class, "flag", false);
    }

    /**
     * 创建直播
     *
     * @param userId        用户ID
     * @param coverUrl      封面URL
     * @param cateId        直播类别
     * @param name          直播名称
     * @param iDataResponse
     */
    public static void createLiveRoom(String userId, String coverUrl, String cateId, String name, int userType, IDataResponse<CreateLiveRoomRespEntity> iDataResponse) {
        Map<String, String> getParams = LiveManager.getInstance().getRequest().getCommonGetParams("1556");
        getParams.put("version", "1.0");
        Map<String, String> postParams = new HashMap<>();
        postParams.put("user_id", userId);
        postParams.put("cover", coverUrl);
        postParams.put("cate_id", cateId);
        postParams.put("name", name);
        postParams.put("type", userType + "");

        LiveManager.getInstance().getRequest().post(Constant.PRIMARY_URL, Constant.METHOD_CREATE_LIVE_ROOM,
                getParams, postParams, iDataResponse, CreateLiveRoomRespEntity.class, "flag", false);

    }
}
