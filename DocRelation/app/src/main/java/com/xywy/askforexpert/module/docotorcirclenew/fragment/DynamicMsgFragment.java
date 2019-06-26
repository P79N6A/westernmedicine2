package com.xywy.askforexpert.module.docotorcirclenew.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.YmRxBus;
import com.xywy.askforexpert.appcommon.base.fragment.CommonListFragment;
import com.xywy.askforexpert.appcommon.utils.DensityUtils;
import com.xywy.askforexpert.model.doctor.Messages;
import com.xywy.askforexpert.module.docotorcirclenew.model.PublishType;
import com.xywy.askforexpert.module.docotorcirclenew.service.DCMiddlewareService;
import com.xywy.retrofit.rxbus.Event;
import com.xywy.retrofit.rxbus.EventSubscriber;

import static com.xywy.askforexpert.module.docotorcirclenew.model.PublishType.Realname;

/**
 * 医圈 共用列表 Fragment
 * Created by bailiangjin on 2016/10/27.
 */

public class DynamicMsgFragment extends CommonListFragment {

    @PublishType
    String publishType;

    public  static DynamicMsgFragment newInstance(@PublishType String publishType){
        DynamicMsgFragment fragment = new DynamicMsgFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", publishType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void beforeViewBind() {
        publishType=getArguments().getString("type");
    }

    @Override
    protected void initView() {
        super.initView();
        vsHead.setLayoutResource(R.layout.unread_msg_count);
        new MyStubView(vsHead.inflate());
    }

    public class MyStubView implements View.OnClickListener{
        TextView tvUnreadMessageNum;
        RelativeLayout rlUnreadMesaages;

        public MyStubView(View view) {
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtils.dp2px(40));
            view.setLayoutParams(params);
            view.requestLayout();
            view.invalidate();
            tvUnreadMessageNum= (TextView) view.findViewById(R.id.tv_unread_message_num);
            rlUnreadMesaages= (RelativeLayout) view.findViewById(R.id.rl_unreadMesaages);
            rlUnreadMesaages.setOnClickListener(this);
            YmRxBus.registerMessageCountChanged(new EventSubscriber<Messages>() {
                @Override
                public void onNext(Event<Messages> messagesEvent) {
                    updateMessageCount(messagesEvent.getData());
                }
            }, getActivity());
        }
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.rl_unreadMesaages:
                    DCMiddlewareService.circleMsg(getActivity(), (Messages) rlUnreadMesaages.getTag(),publishType);
                    break;
            }
        }
        public void updateMessageCount(Messages msgs) {
            if (msgs != null) {
                rlUnreadMesaages.setTag(msgs);
                String count = null;
                String msgCount = Realname.equals(publishType) ? msgs.getSunread() : msgs.getNunread();
                if (Integer.parseInt(msgCount) > 0) {
                    count = Integer.parseInt(msgCount) > 99 ? "99+" : msgCount;
                    count = "有" + count + "条新的提醒，点击查看 >>";
                }
                if (!TextUtils.isEmpty(count)) {
                    rlUnreadMesaages.setVisibility(View.VISIBLE);
                    tvUnreadMessageNum.setText(count);
                } else {
                    rlUnreadMesaages.setVisibility(View.GONE);
                }
            }
        }
    }
}

