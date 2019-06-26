package com.xywy.askforexpert.module.main.subscribe;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.net.ServiceAPI;
import com.xywy.askforexpert.appcommon.utils.GsonUtils;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.ChannelItem;
import com.xywy.askforexpert.model.main.SubscribeTitleListBean;
import com.xywy.askforexpert.model.subscribe.ServeEntity;
import com.xywy.askforexpert.model.subscribe.ServiceTitleEntity;
import com.xywy.askforexpert.model.subscribe.SubscribeEntity;
import com.xywy.askforexpert.model.subscribe.SubscribeMediaBean;
import com.xywy.askforexpert.module.main.home.HomePageCacheUtils;

import net.tsz.afinal.http.AjaxCallBack;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述:
 * 创建人: shihao on 16/1/5 15:59.
 *
 * @modified by Jack Fang
 */
public final class ChannelManager {


    private static final String TAG = "ChannelManager";
    private static ChannelManager channelManager = new ChannelManager();
    /**
     * 默认的用户选择频道列表
     */
    public List<ChannelItem> defaultUserChannels;
    /**
     * 默认的其他频道列表
     */
    public List<ChannelItem> defaultOtherChannels;
    /**
     * 默认定制服务
     */
    public List<ServeEntity> serveEntities;

    public List<ChannelItem> defaultUserMedia;

    public List<ChannelItem> defaultOtherMedia;

    private SharedPreferences saveSp;

    private ServiceTitleEntity serviceTitleEntity;

    private Gson gson;

    //列表中type为0是没添加的type为1是添加的type为2是固定的
//    static {
//        defaultUserChannels = new ArrayList<ChannelItem>();
//        defaultOtherChannels = new ArrayList<ChannelItem>();
//
//        defaultUserChannels.add(new ChannelItem(1,"要闻头条",1,1));//
//        defaultUserChannels.add(new ChannelItem(2,"医者故事",2,1));
//
//        defaultOtherChannels.add(new ChannelItem(3,"医学进展",3,0));
//        defaultOtherChannels.add(new ChannelItem(4,"内科",4,0));
//    }

    private ChannelManager() {
        saveSp = YMApplication.getAppContext().getSharedPreferences("saveChannel", Context.MODE_PRIVATE);
        gson = new Gson();
    }

    //要闻头条  、   医者故事

    public static ChannelManager getManager() {
        return channelManager;
    }

    /**
     * 获取已添加的频道
     * 目前没有排序
     *
     * @return 已经添加的频道
     */
    public List<ChannelItem> getUserChannel() {
        String jsonData = getChannelListJsonStrCache();


        if (defaultUserChannels == null) {
            defaultUserChannels = new ArrayList<>();
        }

        defaultUserChannels.clear();
        //stone 判空 崩溃bug
        if (!TextUtils.isEmpty(jsonData)) {
            serviceTitleEntity = gson.fromJson(jsonData, ServiceTitleEntity.class);

            int size = serviceTitleEntity.getSubscribe().size();

            for (int i = 0; i < size; i++) {

                if (serviceTitleEntity.getSubscribe().get(i).getType() == 2) {
                    ChannelItem channelItem = new ChannelItem();
                    channelItem.setId(serviceTitleEntity.getSubscribe().get(i).getId());
                    channelItem.setName(serviceTitleEntity.getSubscribe().get(i).getName());
                    channelItem.setSelected(serviceTitleEntity.getSubscribe().get(i).getType());
                    defaultUserChannels.add(channelItem);
                }

                if (serviceTitleEntity.getSubscribe().get(i).getType() == 1) {
                    ChannelItem channelItem = new ChannelItem();
                    channelItem.setId(serviceTitleEntity.getSubscribe().get(i).getId());
                    channelItem.setName(serviceTitleEntity.getSubscribe().get(i).getName());
                    channelItem.setSelected(serviceTitleEntity.getSubscribe().get(i).getType());
                    defaultUserChannels.add(channelItem);
                }

            }
        }

        return defaultUserChannels;
    }

    /**
     * 获取未添加的频道
     *
     * @return 已经添加的频道
     */
    public List<ChannelItem> getOtherChannel() {
        String jsonData = getChannelListJsonStrCache();

        if (defaultOtherChannels == null) {
            defaultOtherChannels = new ArrayList<>();
        }

        defaultOtherChannels.clear();
        //stone 判空 崩溃bug
        if (!TextUtils.isEmpty(jsonData)) {

            serviceTitleEntity = gson.fromJson(jsonData, ServiceTitleEntity.class);

            int size = serviceTitleEntity.getSubscribe().size();

            for (int i = 0; i < size; i++) {

                if (serviceTitleEntity.getSubscribe().get(i).getType() == 0) {
                    ChannelItem channelItem = new ChannelItem();
                    channelItem.setId(serviceTitleEntity.getSubscribe().get(i).getId());
                    channelItem.setName(serviceTitleEntity.getSubscribe().get(i).getName());
                    channelItem.setSelected(serviceTitleEntity.getSubscribe().get(i).getType());
                    defaultOtherChannels.add(channelItem);
                }

            }
        }

        return defaultOtherChannels;
    }

    @Nullable
    private String getChannelListJsonStrCache() {
        String jsonData = null;
        SubscribeTitleListBean subscribeTitleListBean = HomePageCacheUtils.getSubscribeTitleListBean(YMUserService.getCurUserId());
        if (null != subscribeTitleListBean) {
            jsonData = GsonUtils.toJson(subscribeTitleListBean);
        }
        return jsonData;
    }

    /**
     * 清除所有的频道
     */
    public void deleteAllChannel() {
        if (serviceTitleEntity != null && serviceTitleEntity.getSubscribe() != null) {
            serviceTitleEntity.getSubscribe().clear();
        }
    }

    public void deleteAllMedia() {
        if (serviceTitleEntity != null && serviceTitleEntity.getMedia() != null) {
            serviceTitleEntity.getMedia().clear();
        }
    }

    /**
     * 获取用户已订阅的媒体号
     */
    public List<ChannelItem> getUserMedia() {
        String mediaJson = saveSp.getString("channelData", "");

        if (defaultUserMedia == null) {
            defaultUserMedia = new ArrayList<>();
        }

        if (!defaultUserMedia.isEmpty()) {
            defaultUserMedia.clear();
        }

        if (!mediaJson.equals("")) {
            serviceTitleEntity = gson.fromJson(mediaJson, ServiceTitleEntity.class);

            List<SubscribeMediaBean> mediaBeen = serviceTitleEntity.getMedia();
            if (mediaBeen != null && !mediaBeen.isEmpty()) {
                for (SubscribeMediaBean bean : mediaBeen) {
                    if (bean.getType() == 1) {
                        ChannelItem channelItem = new ChannelItem();
                        channelItem.setId(bean.getId());
                        channelItem.setName(bean.getName());
                        channelItem.setSelected(bean.getType());
                        defaultUserMedia.add(channelItem);
                    }
                }
            }
        }

        return defaultUserMedia;
    }

    public List<ChannelItem> getOtherMedia() {
        String mediaJson = saveSp.getString("channelData", "");

        if (defaultOtherMedia == null) {
            defaultOtherMedia = new ArrayList<>();
        }

        if (!defaultOtherMedia.isEmpty()) {
            defaultOtherMedia.clear();
        }

        if (!mediaJson.equals("")) {
            serviceTitleEntity = gson.fromJson(mediaJson, ServiceTitleEntity.class);

            List<SubscribeMediaBean> mediaBeen = serviceTitleEntity.getMedia();
            if (mediaBeen != null && !mediaBeen.isEmpty()) {
                for (SubscribeMediaBean bean : mediaBeen) {
                    if (bean.getType() == 0) {
                        ChannelItem channelItem = new ChannelItem();
                        channelItem.setId(bean.getId());
                        channelItem.setName(bean.getName());
                        channelItem.setSelected(bean.getType());
                        defaultOtherMedia.add(channelItem);
                    }
                }
            }
        }

        return defaultOtherMedia;
    }

//    /**
//     * 初始化内存中数据
//     */
//    private void initDefaultChannel() {
//        DLog.d("deleteAll", "deleteAll");
//        deleteAllChannel();
//        saveUserChannel(defaultUserChannels);
//        saveOtherChannel(defaultOtherChannels);
//
//        //存入sp中
//        if (serviceTitleEntity!=null&&serviceTitleEntity.getSubscribe().size()>0);
//    }

    /**
     * 保存用户已订阅到Sp
     *
     * @param userList
     */
    public void saveUserChannel(List<ChannelItem> userList) {
        List<SubscribeEntity> subscribeEntities = new ArrayList<>();
        StringBuilder orderId = new StringBuilder();
        DLog.d(TAG, "user channel list size = " + userList.size() + ", " + userList.toString());
        for (int i = 0; i < userList.size(); i++) {
            SubscribeEntity subscribeEntity = new SubscribeEntity();
            subscribeEntity.setId(userList.get(i).getId());
            subscribeEntity.setName(userList.get(i).getName());
            DLog.i(TAG, "订阅数据名字" + userList.get(i).getName());
            if (i == 0/* || i == 1 || i == 2*/) {
                subscribeEntity.setType(2);
            } else {
                subscribeEntity.setType(1);
            }

            orderId.append(userList.get(i).getId()).append(",");
            subscribeEntities.add(subscribeEntity);
            //异步发送排序到服务器
        }
        pushOrderToServer(orderId.substring(0, orderId.length() - 1));
        serviceTitleEntity.setSubscribe(subscribeEntities);
    }


    private void pushOrderToServer(String idOrderedStr) {

        String userId = YMUserService.getCurUserId();
        DLog.d(TAG, "order id = " + idOrderedStr);
        ServiceAPI.getInstance().modifySub(userId, idOrderedStr, new AjaxCallBack() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                DLog.i(TAG, "修改数据结果" + s);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                ToastUtils.shortToast("网络未连接 保存更改失败");
            }

            @Override
            public void onLoading(long count, long current) {
                super.onLoading(count, current);
            }

            @Override
            public void onStart() {
                super.onStart();
            }
        });
    }


    /**
     * 保存未订阅到sp
     *
     * @param otherList
     */
    public void saveOtherChannel(List<ChannelItem> otherList) {
        for (int i = 0; i < otherList.size(); i++) {
            SubscribeEntity subscribeEntity = new SubscribeEntity();
            subscribeEntity.setId(otherList.get(i).getId());
            subscribeEntity.setName(otherList.get(i).getName());
            subscribeEntity.setType(0);
            serviceTitleEntity.getSubscribe().add(subscribeEntity);
        }

        serviceTitleEntity.setCode(10000);
        serviceTitleEntity.setMsg("success");
    }

    public void saveOtherMedia(List<ChannelItem> otherList) {
        if (otherList != null && !otherList.isEmpty()) {
            DLog.d(TAG, "other media list = " + otherList.toString());
            for (ChannelItem item : otherList) {
                SubscribeMediaBean bean = new SubscribeMediaBean();
                DLog.d(TAG, "other media = " + item.toString());
                bean.setId(item.getId());
                bean.setName(item.getName());
                bean.setType(0);
                DLog.d(TAG, "other media bean = " + bean.toString());
                serviceTitleEntity.getMedia().add(bean);
            }
            DLog.d(TAG, "entity media list = " + serviceTitleEntity.getMedia().toString());
        }

        serviceTitleEntity.setCode(10000);
        serviceTitleEntity.setMsg("success");
    }

    //默认
    public String getServiceDefautl() {

        serviceTitleEntity = new ServiceTitleEntity();
        serviceTitleEntity.setCode(10000);
        serviceTitleEntity.setMsg("success");

        List<ServeEntity> serve = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            ServeEntity serveEntity = new ServeEntity();
            if (i == 1) {
                serveEntity.setId(i);
                serveEntity.setName("问题广场");
                serveEntity.setType(2);
            }
            if (i == 2) {
                serveEntity.setId(i);
                serveEntity.setName("文献检索");
                serveEntity.setType(2);
            }
            if (i == 3) {
                serveEntity.setId(i);
                serveEntity.setName("管理患者");
                serveEntity.setType(2);
            }
            if (i == 4) {
                serveEntity.setId(i);
                serveEntity.setName("检查手册");
                serveEntity.setType(2);
            }
            if (i == 5) {
                serveEntity.setId(i);
                serveEntity.setName("家庭医生");
                serveEntity.setType(0);
            }
            if (i == 6) {
                serveEntity.setId(i);
                serveEntity.setName("药典");
                serveEntity.setType(0);
            }
            if (i == 7) {
                serveEntity.setId(i);
                serveEntity.setName("电话医生");
                serveEntity.setType(0);
            }
            if (i == 8) {
                serveEntity.setId(i);
                serveEntity.setName("临床指南");
                serveEntity.setType(0);
            }
            if (i == 9) {
                serveEntity.setId(i);
                serveEntity.setName("招聘");
                serveEntity.setType(0);
            }
            if (i == 10) {
                serveEntity.setId(i);
                serveEntity.setName("预约转诊");
                serveEntity.setType(0);
            }

            serve.add(serveEntity);
        }

        serviceTitleEntity.setServe(serve);

        List<SubscribeEntity> subscribe = new ArrayList<>();


        SubscribeEntity subscribeEntity0 = new SubscribeEntity();
        subscribeEntity0.setId(0);
        subscribeEntity0.setName("推荐");
        subscribeEntity0.setType(2);
        subscribe.add(subscribeEntity0);


        serviceTitleEntity.setSubscribe(subscribe);

        return gson.toJson(serviceTitleEntity);
    }

    /**
     * 保存定制服务数据到sp
     */
    public void saveDataToSp() {
        DLog.i(TAG, "保存数据到sp");
        if (serviceTitleEntity != null/* && serviceTitleEntity.getSubscribe().size() > 0 && serviceTitleEntity.getServe().size() > 0*/) {
            DLog.d(TAG, "当前数据 " + serviceTitleEntity.toString());
            DLog.i(TAG, "当前订阅大小" + serviceTitleEntity.getSubscribe().size());
//            DLog.d(TAG, "current media " + serviceTitleEntity.getMedia().toString());
            if (YMUserService.isGuest()) {
                saveSp.edit().putString("channelDataGuest", gson.toJson(serviceTitleEntity)).apply();
                saveSp.edit().putBoolean("changedGuest", true).apply();
            } else {
                DLog.d(TAG, "save channel data = " + gson.toJson(serviceTitleEntity));
                saveSp.edit().putString("channelData", gson.toJson(serviceTitleEntity)).apply();
                saveSp.edit().putBoolean("changed", true).apply();
            }
        }
    }

    public List<ServeEntity> getServiceChannel() {
        String jsonData;
        if (YMUserService.isGuest()) {
            jsonData = saveSp.getString("channelDataGuest", getServiceDefautl());
        } else {
            jsonData = saveSp.getString("channelData", getServiceDefautl());
        }

        if (serveEntities == null) {
            serveEntities = new ArrayList<>();
        }

        serveEntities.clear();

        serviceTitleEntity = gson.fromJson(jsonData, ServiceTitleEntity.class);

        return serviceTitleEntity.getServe();
    }

    public void saveServiceChannel(List<ServeEntity> serveEntityList) {

        if (serveEntityList == null) {
            return;
        }

        StringBuffer stringBuffer = new StringBuffer();

        for (int i = 0; i < serveEntityList.size(); i++) {
            if (serveEntityList.get(i).getType() == 2) {
                stringBuffer.append(serveEntityList.get(i).getId() + ",");
            }
            if (serveEntityList.get(i).getType() == 1) {
                stringBuffer.append(serveEntityList.get(i).getId() + ",");
            }

        }
        pushSerOrderToServer(YMApplication.getLoginInfo().getData().getPid() + "", stringBuffer.substring(0, stringBuffer.length() - 1));

        serviceTitleEntity.setServe(serveEntityList);
    }

    private void pushSerOrderToServer(String userid, String orderId) {
        ServiceAPI.getInstance().modifyService(userid, orderId, new AjaxCallBack() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                DLog.i(TAG, "修改数据结果" + s);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }

            @Override
            public void onLoading(long count, long current) {
                super.onLoading(count, current);
            }

            @Override
            public void onStart() {
                super.onStart();
            }
        });
    }

    public void refreshSpData() {
        saveSp.edit().putBoolean("changed", true).apply();
        saveSp.edit().putBoolean("changedGuest", true).apply();
        saveSp.edit().putBoolean("islogout", true).apply();
    }
}
