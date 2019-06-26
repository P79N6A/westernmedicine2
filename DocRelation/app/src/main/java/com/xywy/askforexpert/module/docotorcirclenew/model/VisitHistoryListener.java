package com.xywy.askforexpert.module.docotorcirclenew.model;

import android.app.Activity;
import android.view.View;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.YMDebugOnClickListener;
import com.xywy.askforexpert.model.DoctorUnReadMessageBean;
import com.xywy.askforexpert.module.docotorcirclenew.activity.NewTopicDetailActivity;
import com.xywy.askforexpert.module.docotorcirclenew.activity.detailpage.NoNameDetailActivity;
import com.xywy.askforexpert.module.docotorcirclenew.activity.detailpage.RealNameDetailActivity;
import com.xywy.askforexpert.module.doctorcircle.DiscussDetailActivity;
import com.xywy.askforexpert.module.doctorcircle.InterestPersonActivity;

/**
* Author: zhangshupeng
* Email: zhangshupeng@xywy.com
* Date: 2016/12/21 14:46
*/
public class VisitHistoryListener extends YMDebugOnClickListener {
   private final Activity context;
   private final String type;

   public VisitHistoryListener(Activity context, String type) {
       this.context = context;
       this.type = type;
   }

   @Override
   public void onClick(View v) {
       super.onClick(v);
       final DoctorUnReadMessageBean.ListItem itme = (DoctorUnReadMessageBean.ListItem) v.getTag();
       switch (v.getId()) {
           case R.id.ll_visit_history:
               if (!NetworkUtil.isNetWorkConnected()) {
                   ToastUtils.shortToast( "网络异常，请检查网络连接");
                   return;
               }

               if ("8".equals(itme.type)) {//跳转至感兴趣的人
                   InterestPersonActivity.startActivity(context, "2");
                   return;
               }

               if ("17".equals(itme.type) || "18".equals(itme.type) ||
                       "19".equals(itme.type) || "20".equals(itme.type)) {
                   NewTopicDetailActivity.startActivity(context,itme.dynamicid == null ? 0 : Integer.valueOf(itme.dynamicid));
                   return;
               }

               if ("1".equals(type)) { //动态详情
                   if (itme.source != null && itme.source.equals("4")) {
                       DiscussDetailActivity.startActivity(context, itme.dynamicid, type);
                   } else {
                       RealNameDetailActivity.start(context,itme.dynamicid);
                   }
               } else {
                   NoNameDetailActivity.start(context,itme.dynamicid);
               }
               break;
   }
}
}
