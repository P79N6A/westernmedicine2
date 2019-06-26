package com.xywy.askforexpert.module.docotorcirclenew.model;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.DialogUtil;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.model.topics.TopicDetailData;
import com.xywy.askforexpert.module.main.service.person.PersonDetailActivity;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2016/12/3 15:26
 */

public class TopicItemClickListener extends RealNameItemClickListener {
    TopicDetailData.ListBean headerData;

    public TopicItemClickListener(Activity context, IRecycleViewModel recycleViewModel) {
        super(context, recycleViewModel);
    }

    /**
     * 话题关注/取消关注
     */
    private void follow() {
        String userID = YMApplication.getUUid();
        String topicId = (String) recycleViewModel.getExtra("topicId");
        String sign = MD5Util.MD5(userID + topicId + Constants.MD5_KEY);
        String bind = userID + topicId;
        FinalHttp request = new FinalHttp();
        AjaxParams params = new AjaxParams();
        params.put("a", "theme");
        params.put("bind", bind);
        params.put("sign", sign);
        params.put("userid", userID);
        params.put("m", "theme_fans");
        params.put("themeid", String.valueOf(topicId));
        request.post(CommonUrl.FOLLOW_LIST, params, new AjaxCallBack() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);


                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (jsonObject == null) {
                    Toast.makeText(context,
                            context.getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                } else {
                    int code = jsonObject.optInt("code");
                    String msg = jsonObject.optString("msg");
                    String oMsg = "";
                    JSONObject object = jsonObject.optJSONObject("list");
                    if (object != null) {
                        int oCode = object.optInt("code");
                        oMsg = object.optString("msg");
                    }

                    if (code == 0) {
                        Toast.makeText(context, oMsg, Toast.LENGTH_SHORT).show();
                        // 刷新数据
//                            currentPage = DEFAULT_PAGE;
                        recycleViewModel.onRefresh();
                    } else {
                        String message;
                        if (headerData.isFocused()) {
                            message = "关注失败";
                        } else {
                            message = "取消关注失败";
                        }
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);

                String message;
                if (headerData.isFocused()) {
                    message = "关注失败";
                } else {
                    message = "取消关注失败";
                }
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClick(View v) {
        headerData = (TopicDetailData.ListBean) recycleViewModel.getExtra("head");
        switch (v.getId()) {
            case R.id.topic_detail_follow:
                if (YMUserService.isGuest()) {
                    DialogUtil.LoginDialog(v.getContext());
                } else {
                    headerData.setFocused(!headerData.isFocused());
                    follow();
                }
                break;
            case R.id.topic_detail_topic_host:
                Intent intent = new Intent(context, PersonDetailActivity.class);
                intent.putExtra("uuid", headerData.getUserid());
                context.startActivity(intent);
                break;
            default:
                super.onClick(v);
        }
    }
}
