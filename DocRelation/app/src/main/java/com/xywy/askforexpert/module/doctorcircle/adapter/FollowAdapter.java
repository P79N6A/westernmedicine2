package com.xywy.askforexpert.module.doctorcircle.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hyphenate.exceptions.HyphenateException;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.base.adapter.CommonBaseAdapter;
import com.xywy.askforexpert.appcommon.base.adapter.CommonViewHolder;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.DialogUtil;
import com.xywy.askforexpert.appcommon.utils.YMOtherUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.followList.FollowListData;
import com.xywy.askforexpert.model.followList.IsFollowData;
import com.xywy.easeWrapper.EMContactManager;
import com.xywy.easeWrapper.db.UserDao;
import com.xywy.easeWrapper.domain.EaseUser;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 关注列表 适配器
 * <p>
 * Compiler: Android Studio
 * Project: D_Platform
 * Author: Jack Fang
 * Email: fangqi@xywy.com
 * Date: 2015/12/23 18:50
 */
public class FollowAdapter extends CommonBaseAdapter<FollowListData> {
    private static final String LOG_TAG = "FollowAdapter";

    private static final String FROM_USER_CENTER = "0";
    private static final String FROM_PERSON_DETAIL = "1";
    private final DisplayImageOptions options;
    private final String uuid;
    private List<FollowListData> mDatas = new ArrayList<>();
    private Map<Integer, Integer> checkPositionList;
    private TextView followNum;
    private String type;

    public FollowAdapter(Context mContext, List<FollowListData> mDatas, int mItemLayoutId) {
        super(mContext, mDatas, mItemLayoutId);

        if (mDatas != null) {
            this.mDatas = mDatas;
        }

        checkPositionList = new HashMap<>();
        options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .showImageOnLoading(R.drawable.icon_photo_def)
                .showImageOnFail(R.drawable.icon_photo_def)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        if (mContext != null && mContext instanceof Activity) {
            type = ((Activity) mContext).getIntent().getStringExtra("type");
            uuid = ((Activity) mContext).getIntent().getStringExtra("userid");
        } else {
            type = FROM_USER_CENTER;
            uuid = YMApplication.getPID();
        }
    }

    public Map<Integer, Integer> getCheckPositionList() {
        return checkPositionList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(mItemLayoutId, parent, false);
        }

        ImageView followAvatar = CommonViewHolder.getView(convertView, R.id.follow_img);
        TextView followName = CommonViewHolder.getView(convertView, R.id.follow_name);
        TextView followTitle = CommonViewHolder.getView(convertView, R.id.follow_title);
        followNum = CommonViewHolder.getView(convertView, R.id.follow_num);
        CheckBox followState = CommonViewHolder.getView(convertView, R.id.follow_state);

        final FollowListData followListData = mDatas.get(position);
        mImageLoader.displayImage(followListData.getPhoto(), followAvatar, options);
        followName.setText(followListData.getNickname());
        followTitle.setText(followListData.getSubject());
        followNum.setText("已有" + followListData.getFanscount() + "人关注他");

        switch (type) {
            case FROM_USER_CENTER:
                followState.setVisibility(View.INVISIBLE);
                break;
            case FROM_PERSON_DETAIL:
                if (uuid.equals(YMApplication.getPID())) {
                    // 从自己的个人主页进入关注列表，隐藏关注按钮
                    followState.setVisibility(View.INVISIBLE);
                } else {
                    followState.setVisibility(View.VISIBLE);
                    String relation = followListData.getRelation();
                    // 0无关系   1我的粉丝     2我关注的   3互相关注   4 自己
                    boolean b = false;
                    if (relation != null) {
                        b = relation.equals("3") || relation.equals("2");
                    }
                    if (b) {
                        checkPositionList.put(position, position);
                    }

                    followState.setTag(position);

                    if (checkPositionList != null) {
                        followState.setChecked(checkPositionList.containsKey(position));
                        followState.setText(checkPositionList.containsKey(position) ? "已关注" : "关注");
                    } else {
                        followState.setChecked(false);
                        followState.setText("关注");
                    }

                    followState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (buttonView instanceof CheckBox) {
                                CheckBox checkBox = (CheckBox) buttonView;
                                if (isChecked) {
                                    if (NetworkUtil.isNetWorkConnected()) {
                                        if (!YMUserService.isGuest()) {
                                            if (!checkPositionList.containsKey(position)) {
                                                checkBox.setText("已关注");
                                                checkPositionList.put((int) checkBox.getTag(), (int) checkBox.getTag());
                                                addFollow(checkBox, YMApplication.getPID(), followListData.getId(), followListData);
                                            }
                                        } else {
                                            DialogUtil.LoginDialog(new YMOtherUtils(mContext).context);
                                        }
                                    } else {
                                        Toast.makeText(mContext, "网络不给力", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    if (NetworkUtil.isNetWorkConnected()) {
                                        if (!YMUserService.isGuest()) {
                                            if (checkPositionList.containsKey(checkBox.getTag())) {
                                                checkBox.setText("关注");
                                                checkPositionList.remove(checkBox.getTag());
                                                new Thread(new Runnable() {
                                                    public void run() {
                                                        try {
                                                            EMContactManager.getInstance().deleteContact("sid_" + followListData.getId());
                                                        } catch (HyphenateException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }).start();
                                                deleteRelation(followListData.getId());
                                                removeFollow(checkBox, YMApplication.getPID(), followListData.getId(), followListData);
                                            }
                                        } else {
                                            DialogUtil.LoginDialog(new YMOtherUtils(mContext).context);
                                        }
                                    } else {
                                        Toast.makeText(mContext, "网络不给力", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    });
                }
                break;

            default:
                break;
        }

        return convertView;
    }

    /**
     * 添加关注
     */
    private void addFollow(final CheckBox checkBox, String userid, String touserid, final FollowListData followListData) {
        String bind = userid + touserid;
        String sign = MD5Util.MD5(bind + Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();
        params.put("a", "dcFriend");
        params.put("m", "friend_add");
        params.put("userid", userid);
        params.put("touserid", touserid);
        params.put("bind", bind);
        params.put("sign", sign);

        DLog.d(LOG_TAG, "addFollow: " + CommonUrl.doctor_circo_url + "?" + params.toString());
        FinalHttp request = new FinalHttp();
        request.post(CommonUrl.doctor_circo_url, params, new AjaxCallBack() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);

                Gson gson = new Gson();
                IsFollowData data = gson.fromJson(s, IsFollowData.class);

                if (data != null) {
                    if (data.getCode().equals("0")) {
                        Toast.makeText(mContext, "关注成功", Toast.LENGTH_SHORT).show();
                        // 刷线页面显示的关注人数
                        followNum.setText("已有" + (Integer.parseInt(followListData.getFanscount()) + 1) + "人关注他");
                        followListData.setFanscount(String.valueOf(Integer.parseInt(followListData.getFanscount()) + 1));
                        followListData.setRelation("2");
                        notifyDataSetChanged();
                    } else {
                        Toast.makeText(mContext, data.getMsg(), Toast.LENGTH_SHORT).show();
                        // 失败，还原之前的状态
                        checkBox.setChecked(false);
                    }
                } else {
                    Toast.makeText(mContext, mContext.getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                    // 失败，还原之前的状态
                    checkBox.setChecked(false);
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);

                Toast.makeText(mContext, "关注失败", Toast.LENGTH_SHORT).show();
                // 失败，还原之前的状态
                checkBox.setChecked(false);
            }
        });
    }

    /**
     * 取消关注
     */
    private void removeFollow(final CheckBox checkBox, String userid, String touserid, final FollowListData followListData) {
        String bind = userid + touserid;
        String sign = MD5Util.MD5(bind + Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();
        params.put("a", "dcFriend");
        params.put("m", "friend_del");
        params.put("userid", userid);
        params.put("touserid", touserid);
        params.put("bind", bind);
        params.put("sign", sign);

        DLog.d(LOG_TAG, "removeFollow: " + CommonUrl.doctor_circo_url + "?" + params.toString());
        FinalHttp request = new FinalHttp();
        request.post(CommonUrl.doctor_circo_url, params, new AjaxCallBack() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);

                IsFollowData data = new Gson().fromJson(s, IsFollowData.class);

                if (data != null) {
                    if (data.getCode().equals("0")) {
                        Toast.makeText(mContext, "取消关注成功", Toast.LENGTH_SHORT).show();
                        // 刷新页面显示的关注人数
                        followNum.setText("已有" + (Integer.parseInt(followListData.getFanscount()) - 1) + "人关注他");
                        followListData.setFanscount(String.valueOf(Integer.parseInt(followListData.getFanscount()) - 1));
                        followListData.setRelation("0");
                        deleteContact(followListData.getId());
                        notifyDataSetChanged();
                    } else {
                        Toast.makeText(mContext, data.getMsg(), Toast.LENGTH_SHORT).show();
                        // 失败，还原之前的状态
                        checkBox.setChecked(true);
                    }
                } else {
                    Toast.makeText(mContext, mContext.getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                    // 失败，还原之前的状态
                    checkBox.setChecked(true);
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);

                Toast.makeText(mContext, "取消关注失败", Toast.LENGTH_SHORT).show();
                // 失败，还原之前的状态
                checkBox.setChecked(true);
            }
        });
    }

    /**
     * 删除关系
     */
    public void deleteRelation(String username) {
        String did = YMApplication.getLoginInfo().getData().getHuanxin_username();
        String bind = did + "sid_" + username;
        Long st = System.currentTimeMillis();

        String sign = MD5Util.MD5(st + bind + Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();

        params.put("timestamp", st + "");
        params.put("bind", bind);
        params.put("a", "chat");
        params.put("m", "delRelation");
        params.put("friend", "sid_" + username);
        params.put("owner", did);
        params.put("sign", sign);

        FinalHttp fh = new FinalHttp();
        fh.get(CommonUrl.Patient_Manager_Url, params,
                new AjaxCallBack() {
                });
    }

    /**
     * 删除联系人
     */
    public void deleteContact(final String username) {

        new Thread(new Runnable() {
            public void run() {
                try {
                    // 删除db和内存中此用户的数据
                    UserDao dao = new UserDao(YMApplication.getAppContext());
                    dao.deleteContact(username);
                    Map<String, EaseUser> userlist = YMApplication.getContactList();
                    userlist.remove(username);
                    YMApplication.getInstance().setContactList(userlist);
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
