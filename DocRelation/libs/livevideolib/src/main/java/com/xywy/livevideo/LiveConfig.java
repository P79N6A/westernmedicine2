package com.xywy.livevideo;

import com.xywy.livevideo.common_interface.IDataRequest;
import com.xywy.livevideo.entity.GiftListRespEntity;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created by bobby on 17/2/24.
 */

public class LiveConfig {
    //主播封面url
    public  String anchorUrl;
    //健康币
    public  int healthIcon;
    /**
     * 当前用户头像图片url
     */
    public String myHeadImageUrl;

    public final Set<String> chatRoomIds=new CopyOnWriteArraySet<>();

    public final IDataRequest request;

    public final String userId;

    public final String nickName;

    public final String hxUserName;

    public final String hxPassword;

    public  GiftListRespEntity  giftList;//礼物列表

    public final int userType;//用户类型，1，医生，2，普通用户



    LiveConfig(Builder builder) {
        this.anchorUrl = builder.anchorUrl;
        this.healthIcon = builder.healthIcon;
        this.myHeadImageUrl = builder.myHeadImageUrl;
        this.request = builder.request;
        this.userId = builder.userId;
        this.nickName = builder.nickName;
        this.hxUserName = builder.hxUserName;
        this.hxPassword = builder.hxPassword;
        this.userType=builder.userType;
    }

    public static class Builder {
        private String anchorUrl;
        private int healthIcon;
        private IDataRequest request;
        private String userId;
        private String nickName;
        private String myHeadImageUrl;
        private String hxUserName;
        private String hxPassword;
        private int userType;

        public Builder setAnchorUrl(String anchorUrl) {
            this.anchorUrl = anchorUrl;
            return this;
        }

        public Builder setHealthIcon(int healthIcon) {
            this.healthIcon = healthIcon;
            return this;
        }

        public Builder setRequest(IDataRequest request) {
            this.request = request;
            return this;
        }

        public Builder setUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder setNickName(String nickName) {
            this.nickName = nickName;
            return this;
        }

        public Builder setHxUserName(String hxUserName) {
            this.hxUserName = hxUserName;
            return this;
        }

        public Builder setHxPassword(String hxPassword) {
            this.hxPassword = hxPassword;
            return this;
        }

        public Builder setUserType(int userType) {
            this.userType=userType;
            return this;
        }

        public Builder setMyHeadImageUrl(String myHeadImageUrl) {
            this.myHeadImageUrl=myHeadImageUrl;
            return this;
        }

        public LiveConfig createLiveConfig(){
           return new LiveConfig(this);
        }

    }
}
