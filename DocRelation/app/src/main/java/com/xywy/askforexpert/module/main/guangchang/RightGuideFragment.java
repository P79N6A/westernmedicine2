package com.xywy.askforexpert.module.main.guangchang;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.old.BaseFragment;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.module.main.service.HistoryReplyActivity;
import com.xywy.askforexpert.module.main.service.que.QueFoundActivity;
import com.xywy.askforexpert.module.main.service.que.QueMyReplyActivity;
import com.xywy.askforexpert.module.main.service.que.QueSettingActivity;

/**
 * 右侧向导界面
 *
 * @author shihao 2015-5-9
 */
public class RightGuideFragment extends BaseFragment implements OnClickListener {

    private LinearLayout llFound, llSetting;

    private RelativeLayout rlReply, rlRed;

    private int backNum;

    private TextView tvRedNum;

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.rl_my_reply:
                StatisticalTools.eventCount(getActivity(), "myreply");
//                intent.setClass(getActivity(), QueMyReplyActivity.class);
//                intent.putExtra("backNum", backNum);
                intent.setClass(getActivity(), HistoryReplyActivity.class);
                intent.putExtra("backNum", backNum);
                break;
            case R.id.ll_found:
                StatisticalTools.eventCount(getActivity(), "discovery");
                intent.setClass(getActivity(), QueFoundActivity.class);
//			getActivity().finish();
                break;
//		case R.id.ll_switch:
//			intent.setClass(getActivity(), QueSwitchDpart.class);
//			break;
            case R.id.ll_setting:
                intent.setClass(getActivity(), QueSettingActivity.class);
                break;
        }
        getActivity().startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        Bundle budle = getArguments();
        backNum = budle.getInt("backNum");
    }

    public void onResume() {
        super.onResume();
        StatisticalTools.fragmentOnResume("RightGuideFragment");
    }

    public void onPause() {
        super.onPause();
        StatisticalTools.fragmentOnPause("RightGuideFragment");
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.service_question_rguide,
                container, false);
        String isJob = YMApplication.getLoginInfo().getData().getIsjob();

        rlReply = (RelativeLayout) view.findViewById(R.id.rl_my_reply);
        llFound = (LinearLayout) view.findViewById(R.id.ll_found);
//		llSwitch = (LinearLayout) view.findViewById(R.id.ll_switch);
        llSetting = (LinearLayout) view.findViewById(R.id.ll_setting);
        rlRed = (RelativeLayout) view.findViewById(R.id.rl_red_point);
        tvRedNum = (TextView) view.findViewById(R.id.tv_num_red);

        changeNum(backNum);

//        if (isJob.equals("2")) {
//            llFound.setVisibility(View.GONE);
//        } else {
//            llFound.setVisibility(View.VISIBLE);
//            llFound.setOnClickListener(this);
//        }

        rlReply.setOnClickListener(this);
        llFound.setOnClickListener(this);
        llSetting.setOnClickListener(this);

        return view;
    }

    public void changeNum(int num) {
//        if (num > 0) {
//            rlRed.setVisibility(View.VISIBLE);
//            tvRedNum.setText("" + num);
//        } else {
//            rlRed.setVisibility(View.GONE);
//        }
        rlRed.setVisibility(View.GONE);
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }
}
