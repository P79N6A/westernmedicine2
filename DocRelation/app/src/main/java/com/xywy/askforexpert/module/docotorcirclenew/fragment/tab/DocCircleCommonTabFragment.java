package com.xywy.askforexpert.module.docotorcirclenew.fragment.tab;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.YmRxBus;
import com.xywy.askforexpert.appcommon.base.YMBaseFragment;
import com.xywy.askforexpert.appcommon.utils.DialogUtil;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.model.doctor.Messages;
import com.xywy.askforexpert.module.docotorcirclenew.interfaze.IViewRefresh;
import com.xywy.askforexpert.module.docotorcirclenew.model.PublishType;
import com.xywy.askforexpert.widget.DocCircleViewPager;
import com.xywy.retrofit.rxbus.Event;
import com.xywy.retrofit.rxbus.EventSubscriber;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2016/12/19 14:07
 */
public class DocCircleCommonTabFragment extends YMBaseFragment implements View.OnClickListener {
    protected List<Fragment> subFragmets = new ArrayList<>();

     DocCircleViewPager mContent;
     RelativeLayout mRealName;
     RelativeLayout mNotRealName;
     TextView mTv_realname;
     TextView mTv_Noname;
     TextView tv_realName_update;// 实名更新提醒
     TextView tv_UnName_update;// 匿名更新提醒
     Messages message;
     View noname;
     View realname;
     RelativeLayout re_right;
     RelativeLayout left;



    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_doc_circle;
    }

    @Override
    protected void initView() {
        noname = rootView.findViewById(R.id.noname);
        realname = rootView.findViewById(R.id.realname);

        mRealName = (RelativeLayout) rootView.findViewById(R.id.rl_realname);
        mNotRealName = (RelativeLayout) rootView.findViewById(R.id.rl_not_reaname);
        mContent = (DocCircleViewPager) rootView.findViewById(R.id.fl_doctor_content);
        mContent.setOffscreenPageLimit(1);
        mTv_realname = (TextView) rootView.findViewById(R.id.tv_realname);
        mTv_Noname = (TextView) rootView.findViewById(R.id.tv_noname);
        tv_realName_update = (TextView) rootView.findViewById(R.id.id_BadgeView);
        tv_UnName_update = (TextView) rootView.findViewById(R.id.tv_UnName_update);
        switchToFragment(0);
        re_right = (RelativeLayout) rootView.findViewById(R.id.re_right);
        left = (RelativeLayout) rootView.findViewById(R.id.ll_left);
        if (YMUserService.isGuest()) {
            mContent.setDialogShow(true);
            mContent.setPageAllowScroll(false);
        }
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        initSubFragments();
        mContent.setAdapter(new FragmentPagerAdapter(
                getActivity().getSupportFragmentManager()) {

            @Override
            public int getCount() {
                return subFragmets.size();
            }

            @Override
            public Fragment getItem(int arg0) {
                return subFragmets.get(arg0);
            }

            public Object instantiateItem(ViewGroup container, final int position) {
                return super.instantiateItem(container, position);
            }
        });
    }

    protected void initSubFragments() {

    }

    private void updateMessageCount(Messages msg) {
        message = msg;
        if (message != null) {
            if (Integer.parseInt(message.sunread) > 99) {
                tv_realName_update.setVisibility(View.VISIBLE);
                tv_realName_update.setText("99+");
                left.setVisibility(View.VISIBLE);
                realname.setVisibility(View.INVISIBLE);
            } else {
                if (Integer.parseInt(message.sunread) > 0) {
                    tv_realName_update.setVisibility(View.VISIBLE);
                    tv_realName_update.setText(message.sunread + "");
                    left.setVisibility(View.VISIBLE);
                    realname.setVisibility(View.INVISIBLE);
                } else {
                    if (Integer.parseInt(message.snew) > 0) {
                        realname.setVisibility(View.VISIBLE);
                        tv_realName_update.setVisibility(View.GONE);
                        left.setVisibility(View.GONE);
                    } else {
                        tv_realName_update.setVisibility(View.GONE);
                        realname.setVisibility(View.GONE);
                        left.setVisibility(View.GONE);
                    }

                }
            }

        }

        if (message != null) {

            if (Integer.parseInt(message.nunread) > 99) {
                re_right.setVisibility(View.VISIBLE);
                tv_UnName_update.setVisibility(View.VISIBLE);
                tv_UnName_update.setText("99+");
                re_right.setVisibility(View.VISIBLE);
                noname.setVisibility(View.INVISIBLE);
            } else {
                if (Integer.parseInt(message.nunread) > 0) {
                    noname.setVisibility(View.INVISIBLE);
                    re_right.setVisibility(View.VISIBLE);
                    tv_UnName_update.setText(message.nunread + "");
                    tv_UnName_update.setVisibility(View.VISIBLE);
                } else {
                    if (Integer.parseInt(message.nnew) > 0) {
                        noname.setVisibility(View.VISIBLE);
                        re_right.setVisibility(View.GONE);
                    } else {
                        noname.setVisibility(View.GONE);
                        re_right.setVisibility(View.GONE);
                    }

                    tv_UnName_update.setVisibility(View.GONE);
                }
            }
        }
    }

    protected void initListener() {
        mRealName.setOnClickListener(this);
        mNotRealName.setOnClickListener(this);

        mContent.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                switchToFragment(arg0);
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }

            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }
        });
         YmRxBus.registerMessageCountChanged(new EventSubscriber<Messages>() {
            @Override
            public void onNext(Event<Messages> messagesEvent) {
                updateMessageCount(messagesEvent.getData());
            }
        }, getActivity());
    }

    @Override
    public String getStatisticalPageName() {
        return "DocCircleCommonTabFragment";
    }

    public void refresh() {
        ((IViewRefresh)subFragmets.get(mContent.getCurrentItem())).refresh();
    }

    public void switchFragment(@PublishType String type) {
        if (type.equals(PublishType.Anonymous)){
            switchToFragment(1);
        }else{
            switchToFragment(0);
        }
    }

    protected  boolean dismisisDiaglog(){
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.rl_not_reaname:// 匿名动态
                StatisticalTools.eventCount(getActivity(), "realnamedynamic");
                if (dismisisDiaglog())
                    return;
                if (YMUserService.isGuest()) {
                    //go to login
                    DialogUtil.LoginDialog(getActivity());
                    return;
                }
                //doctorRealNameFragment doctorNotNameFragment
                if (mContent.getCurrentItem() == 1) {
                    ((IViewRefresh)subFragmets.get(1)).refresh();
                    return;
                }

                switchToFragment(1);

                break;

            case R.id.rl_realname:// 实名动态
                StatisticalTools.eventCount(YMApplication.getAppContext(), "Anonymousdynamic");
                if (dismisisDiaglog())
                    return;
                if (mContent.getCurrentItem() == 0) {
                    ((IViewRefresh)subFragmets.get(0)).refresh();
//                    doctorRealNameFragment.getData();
                    return;
                }

                switchToFragment(0);
                break;

        }
    }

    protected void switchToFragment(int page) {
        if (page == 0) {
            mTv_realname.setTextColor(getResources().getColor(R.color.white));
            mRealName.setBackgroundResource(R.drawable.shape_while_bg_left);
            mTv_Noname.setTextColor(getResources().getColor(R.color.c333));
            mNotRealName.setBackgroundResource(R.drawable.shape_while_bg_rigte);
            mContent.setCurrentItem(0);
        } else if (page == 1) {
            mTv_realname.setTextColor(getResources().getColor(R.color.c333));
            mRealName.setBackgroundResource(R.drawable.shape_while_bg_left_normal);
            mTv_Noname.setTextColor(getResources().getColor(R.color.white));
            mNotRealName.setBackgroundResource(R.drawable.shape_while_bg_right_selected);
            mContent.setCurrentItem(1);
        }
    }


}
