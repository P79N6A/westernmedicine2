package com.xywy.askforexpert.module.docotorcirclenew.model;

import android.app.Activity;

import com.google.gson.Gson;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.net.DoctorAPI;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.model.DoctorUnReadMessageBean;
import com.xywy.askforexpert.module.docotorcirclenew.interfaze.IViewRender;
import com.xywy.retrofit.net.RetrofitCache;

import net.tsz.afinal.http.AjaxCallBack;

import static com.xywy.askforexpert.module.doctorcircle.adapter.DitaileNoNameCommlistAdapter.uuid;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2016/11/9 16:34
 */

public class RecycleViewVisitHistoryModel extends BaseRecycleViewModel<DoctorUnReadMessageBean.ListItem> {
    private String type;
    public RecycleViewVisitHistoryModel(IViewRender view, final Activity context, final String type) {
        super(view);
        this.type = type;
        setItemClickListener(new VisitHistoryListener(context, type));
    }
    protected void request(final boolean loadmore) {
        uuid = YMApplication.getUUid();
        String sign = MD5Util.MD5(uuid + type + Constants.MD5_KEY);
        DoctorAPI.getUnReadHistoryIpi(computePage(loadmore) + "", uuid, type + "", sign,
                new AjaxCallBack() {
                    @Override
                    public void onFailure(Throwable t, int errorNo, String strMsg) {
                        super.onFailure(t, errorNo, strMsg);
                        handleError();
                    }

                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onSuccess(String t) {
                        super.onSuccess(t);
                        DoctorUnReadMessageBean messageBean = new Gson().fromJson(t.toString(), DoctorUnReadMessageBean.class);
                        handleSuccess(messageBean.list,loadmore, RetrofitCache.isResponseFromCache(messageBean));
                    }

                });
    }

}

