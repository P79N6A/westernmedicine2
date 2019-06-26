package com.xywy.askforexpert.module.my.smallstation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.GsonUtils;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.logictools.ResolveJson;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.MySmallActionInfo;
import com.xywy.askforexpert.module.my.smallstation.adapter.BaseMySmallActionAdapter;
import com.xywy.askforexpert.widget.ReboundScrollView;
import com.xywy.askforexpert.widget.view.MyListView;
import com.xywy.askforexpert.widget.view.RoundAngleImageView;
import com.xywy.base.view.ProgressDialog;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

/**
 * 我的小站
 *
 * @author 王鹏
 * @2015-5-23下午1:54:45
 *
 * 2018-03-21 这个类，根本未使用
 */
public class MySmallStationFragment extends Fragment implements OnTouchListener {
    private static final String TAG = "MySmallStationFragment";
    private RoundAngleImageView img_head;
    private TextView tv_name;
    private TextView tv_job, tv_id, tv_hostipal, tv_help_number;
    private TextView tv_zixun_number, tv_add_number, tv_phodoctor_number,
            tv_homdoctor_moth, tv_homdoctor_number, tv_speciality;
    private RatingBar ratingbar;
    private TextView tv_points;
    private FinalBitmap fb;
    private MySmallActionInfo actionInfo;
//    private BaseMySmallActionAdapter adpter;
    private TextView tv_h_number;
    private MyListView list;
    private LinearLayout help_layout;
    private RelativeLayout re_free_zx, re_home_doctor, re_phone_doctor,
            re_add_number;

    private RelativeLayout rel_title;
    private ReboundScrollView scroll;
    private TextView tv_list_title;

    private TextView tv_title_fee, tv_title_phone, tv_title_add, tv_title_fam;
    private TextView tv_content_fee, tv_content_phone, tv_content_add,
            tv_content_fam;
    private ImageView img_free, img_add, img_phone, img_fam;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_smallmation_main, container,
                false);
        view.setOnTouchListener(this);
        fb = FinalBitmap.create(getActivity(), true);
        scroll = (ReboundScrollView) view.findViewById(R.id.scroll);
        rel_title = (RelativeLayout) view.findViewById(R.id.rel_title);
        rel_title.setFocusable(true);
        rel_title.setFocusableInTouchMode(true);
        rel_title.requestFocus();
        img_free = (ImageView) view.findViewById(R.id.img_free);
        img_add = (ImageView) view.findViewById(R.id.img_free_1);
        img_phone = (ImageView) view.findViewById(R.id.img_free_3);
        img_fam = (ImageView) view.findViewById(R.id.img_free_4);
        tv_title_fee = (TextView) view.findViewById(R.id.tv_title_free);
        tv_title_phone = (TextView) view.findViewById(R.id.tv_title_phone);
        tv_title_add = (TextView) view.findViewById(R.id.tv_title_add);
        tv_title_fam = (TextView) view.findViewById(R.id.tv_title_fam);
        tv_content_fee = (TextView) view.findViewById(R.id.tv_content_free);
        tv_content_phone = (TextView) view.findViewById(R.id.tv_content_phone);
        tv_content_add = (TextView) view.findViewById(R.id.tv_content_add);
        tv_content_fam = (TextView) view.findViewById(R.id.tv_content_fam);

        tv_title_fee = (TextView) view.findViewById(R.id.tv_title_free);

        tv_h_number = (TextView) view.findViewById(R.id.tv_h_number);
        help_layout = (LinearLayout) view.findViewById(R.id.help_layout);
        img_head = (RoundAngleImageView) view.findViewById(R.id.img_head);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        tv_job = (TextView) view.findViewById(R.id.tv_job);
        tv_id = (TextView) view.findViewById(R.id.tv_id);
        tv_hostipal = (TextView) view.findViewById(R.id.tv_hostipal);
//        tv_help_number = (TextView) view.findViewById(R.id.tv_help_number);
        ratingbar = (RatingBar) view.findViewById(R.id.ratingbar);
        tv_points = (TextView) view.findViewById(R.id.tv_points);
        tv_zixun_number = (TextView) view.findViewById(R.id.tv_zixun_number);
        tv_add_number = (TextView) view.findViewById(R.id.tv_add_number);
        tv_phodoctor_number = (TextView) view
                .findViewById(R.id.tv_phodoctor_number);
        tv_homdoctor_moth = (TextView) view
                .findViewById(R.id.tv_homdoctor_moth);
        tv_homdoctor_number = (TextView) view
                .findViewById(R.id.tv_homdoctor_number);
        re_free_zx = (RelativeLayout) view.findViewById(R.id.re_free_zx);
        re_add_number = (RelativeLayout) view.findViewById(R.id.re_add_number);
        re_phone_doctor = (RelativeLayout) view
                .findViewById(R.id.re_phone_doctor);
        re_home_doctor = (RelativeLayout) view
                .findViewById(R.id.re_home_doctor);
        tv_speciality = (TextView) view.findViewById(R.id.tv_speciality);
        list = (MyListView) view.findViewById(R.id.list);
        tv_list_title = (TextView) view.findViewById(R.id.tv_list_title);

        scroll.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                scroll.fullScroll(View.FOCUS_UP);

            }
        });
        if (YMApplication.DoctorType() == 2) {
            help_layout.setVisibility(View.GONE);
        }

        ininview();

        if (NetworkUtil.isNetWorkConnected()) {
            getData();
        } else {
            ToastUtils.shortToast( "网络连接失败");
        }
        return view;
    }

    public void ininview() {
        DLog.d("h_num", YMApplication.getLoginInfo().getData().getH_num()
                .trim());
        tv_h_number.setText(YMApplication.getLoginInfo().getData().getH_num()
                .trim());
        fb.display(img_head, YMApplication.getLoginInfo().getData().getPhoto());
        fb.configLoadfailImage(R.drawable.icon_photo_def);
        tv_name.setText(YMApplication.getLoginInfo().getData().getRealname());
        tv_job.setText(YMApplication.getLoginInfo().getData().getJob());
        if ("".equals(YMApplication.getLoginInfo().getData().getSubjectName())) {
            tv_id.setText("未填写");
        } else {
            tv_id.setText(YMApplication.getLoginInfo().getData()
                    .getSubjectName());
        }
        // tv_id.setText(YMApplication.getLoginInfo().getData().getPid());
        tv_hostipal.setText(YMApplication.getLoginInfo().getData()
                .getHospital());
//        tv_help_number.setText(YMApplication.getLoginInfo().getData().getH_num());
        ratingbar.setClickable(false);
        if (!YMApplication.getLoginInfo().getData().getStat().equals("")) {
            float star = Float.parseFloat(YMApplication.getLoginInfo()
                    .getData().getStat());
            ratingbar.setRating(star);
        }
        tv_points.setText(YMApplication.getLoginInfo().getData().getStat()
                + " 分");
    }

    public void init2() {
//        tv_speciality.setText(actionInfo.getData().getSynopsis());
//        if (actionInfo.getData().getClinic().get(0).getIsclicl().equals("1")) {
//            re_free_zx.setVisibility(View.VISIBLE);
//            tv_zixun_number.setText("已有帮助"
//                    + actionInfo.getData().getClinic().get(0).getNumber()
//                    + "名患者");
//            if (this.isAdded()) {
//                tv_title_fee.setTextColor(getResources().getColor(
//                        R.color.my_textcolor));
//                tv_content_fee.setTextColor(getResources().getColor(
//                        R.color.my_textcolor));
//            }
//            img_free.setBackgroundResource(R.drawable.free_zx);
//
//        } else {
//            // re_free_zx.setVisibility(View.GONE);
//        }
//        if (actionInfo.getData().getClinic().get(1).getIsclicl().equals("1")) {
//            re_add_number.setVisibility(View.VISIBLE);
//            tv_add_number.setText("已有"
//                    + actionInfo.getData().getClinic().get(1).getNumber()
//                    + "人就诊");
//            if (this.isAdded()) {
//                tv_title_add.setTextColor(getResources().getColor(
//                        R.color.my_textcolor));
//                tv_content_add.setTextColor(getResources().getColor(
//                        R.color.my_textcolor));
//            }
//            img_add.setBackgroundResource(R.drawable.add_num);
//        } else {
//            // re_add_number.setVisibility(View.GONE);
//        }
//        if (actionInfo.getData().getClinic().get(2).getIsclicl().equals("1")) {
//            re_phone_doctor.setVisibility(View.VISIBLE);
//            tv_phodoctor_number.setText("已有"
//                    + actionInfo.getData().getClinic().get(2).getNumber()
//                    + "人签约");
//            if (this.isAdded()) {
//                tv_title_phone.setTextColor(getResources().getColor(
//                        R.color.my_textcolor));
//                tv_content_phone.setTextColor(getResources().getColor(
//                        R.color.my_textcolor));
//            }
//            img_phone.setBackgroundResource(R.drawable.phone_doctor);
//        } else {
//            // re_phone_doctor.setVisibility(View.GONE);
//        }
//
//        if (actionInfo.getData().getClinic().get(3).getIsclicl().equals("1")) {
//            re_home_doctor.setVisibility(View.VISIBLE);
//            // tv_homdoctor_moth.setText(actionInfo.getData().getClinic().get(3)
//            // .getPrice());
//            tv_homdoctor_number.setText("已有"
//                    + actionInfo.getData().getClinic().get(3).getNumber()
//                    + "人签约");
//            if (this.isAdded()) {
//                tv_title_fam.setTextColor(getResources().getColor(
//                        R.color.my_textcolor));
//                tv_content_fam.setTextColor(getResources().getColor(
//                        R.color.my_textcolor));
//            }
//            img_fam.setBackgroundResource(R.drawable.home_doctor);
//        } else {
//            // re_home_doctor.setVisibility(View.GONE);
//        }
//        if (actionInfo != null) {
//            if (actionInfo.getData().getGrade().size() > 0) {
//                tv_list_title.setVisibility(View.VISIBLE);
//            }
//            adpter = new BaseMySmallActionAdapter(getActivity(), actionInfo
//                    .getData().getGrade());
//            adpter.init();
//            list.setAdapter(adpter);
//        }

    }

    public void getData() {
        final ProgressDialog dialog = new ProgressDialog(getActivity(),
                getString(R.string.loading_now));

        dialog.setCanceledOnTouchOutside(false);
        dialog.showProgersssDialog();
        AjaxParams params = new AjaxParams();
        String userid = YMApplication.getLoginInfo().getData().getPid();
        String sign = MD5Util.MD5(userid + Constants.MD5_KEY);
        params.put("command", "getInfo");
        params.put("userid", userid);
        params.put("sign", sign);

        FinalHttp fh = new FinalHttp();
        fh.post(CommonUrl.DP_COMMON, params, new AjaxCallBack() {
            @Override
            public void onSuccess(String t) {
                DLog.i(TAG, "小站返回数据" + t.toString());
//                actionInfo = ResolveJson.R_mysmallaction(t.toString());
//                GsonUtils.toJson()
//                if (actionInfo == null) {
//                    dialog.dismiss();
//                    return;
//                }
//                if (actionInfo.getCode().equals("0")) {
//                    init2();
//                }
                dialog.dismiss();
                super.onSuccess(t);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                // TODO Auto-generated method stub
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    @Override
    public boolean onTouch(View arg0, MotionEvent arg1) {
        // TODO Auto-generated method stub
        return true;
    }

    public void onResume() {
        super.onResume();
        StatisticalTools.fragmentOnResume("MySmallStationFragment");
    }

    public void onPause() {
        super.onPause();
        StatisticalTools.fragmentOnPause("MySmallStationFragment");
    }
}
