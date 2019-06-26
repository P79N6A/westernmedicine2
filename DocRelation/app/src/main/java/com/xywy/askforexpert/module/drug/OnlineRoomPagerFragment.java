package com.xywy.askforexpert.module.drug;

import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.utils.AppUtils;
import com.xywy.askforexpert.model.consultentity.ConsultPagerTabBean;
import com.xywy.askforexpert.model.consultentity.ConsultPagerTabItemBean;
import com.xywy.askforexpert.widget.view.SelectBasePopupWindow;
import com.xywy.uilibrary.fragment.pagerfragment.adapter.XywyFragmentPagerAdapter;
import com.xywy.uilibrary.fragment.pagerfragment.fragment.XYWYTabPagerFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;


/**
 * 在线诊室切换 stone
 */
public class OnlineRoomPagerFragment extends XYWYTabPagerFragment<ConsultPagerTabBean> implements OnlineRoomItemFragment.IModifyMsgTip {

    private View lay1;
    private View lay2;
    private View popRoot;

    private SelectBasePopupWindow mPopupWindow;

    private TextView tv_title;
    private ImageView iv_arrow;

    private XywyFragmentPagerAdapter<ConsultPagerTabBean> pagerAdapter;
    private List<Fragment> fragments;
    private List<ConsultPagerTabBean> consultPagerTabBeanList;
    private static final String STR_MY_CONSULT = "已接诊";
    private static final String STR_QUESTIONS = "我的待接诊";
    private static final String STR_PRESCRIPTION = "处方记录";

    private View[] tabViewArray;
    private ITip iTip;


    @Override
    protected void beforeViewBind() {
    }

    @Override
    protected void bindView(View view) {
        ButterKnife.bind(this, view);
    }

    @Override
    protected void unBindView() {
        ButterKnife.unbind(this);
    }

    @Override
    protected void initListener() {

    }

    @Override
    public XywyFragmentPagerAdapter<ConsultPagerTabBean> getAdapter() {
        if (pagerAdapter == null) {
            initData();
            pagerAdapter = new XywyFragmentPagerAdapter<ConsultPagerTabBean>(getChildFragmentManager(), consultPagerTabBeanList) {
                @Override
                protected Fragment getItemFragment(int position, ConsultPagerTabBean tabData) {
                    return fragments.get(position);
                }
            };
        }
        return pagerAdapter;
    }

    private void initData() {

        viewPager.setOffscreenPageLimit(3);
        consultPagerTabBeanList = new ArrayList<>();
        ConsultPagerTabItemBean itemBean = new ConsultPagerTabItemBean(ConsultPagerTabItemBean.TYPE_MY_CONSULT, STR_MY_CONSULT);
        consultPagerTabBeanList.add(new ConsultPagerTabBean(STR_MY_CONSULT, itemBean));
        itemBean = new ConsultPagerTabItemBean(ConsultPagerTabItemBean.TYPE_QUESTIONS, STR_QUESTIONS);
        consultPagerTabBeanList.add(new ConsultPagerTabBean(STR_QUESTIONS, itemBean));

        itemBean = new ConsultPagerTabItemBean(ConsultPagerTabItemBean.TYPE_ANSWERED, STR_PRESCRIPTION);
        consultPagerTabBeanList.add(new ConsultPagerTabBean(STR_PRESCRIPTION, itemBean));

        fragments = new ArrayList<>();
        fragments.add(OnlineRoomItemFragment.newInstance(1,0, this));
        fragments.add(OnlineRoomItemFragment.newInstance(3,0, this));
        fragments.add(OnlineRoomItemFragment.newInstance(4,1, this));
//        fragments.add(ConsultItemFragment.newInstance(consultPagerTabBeanList.get(0), this, false,0));
//        fragments.add(ConsultItemFragment.newInstance(consultPagerTabBeanList.get(1), this, true,0));
        addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (iTip != null) {
                    switch (position) {
                        case 0:
                            iTip.showTip(getResources().getString(R.string.consult_bottom_tip));
                            break;
                        case 1:
                            iTip.showTip(getResources().getString(R.string.question_pool_tip));
                            break;
                        case 2:
                            iTip.showTip(getResources().getString(R.string.question_pool_tip));
                            break;
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    public static interface ITip {
        void showTip(String text);
    }

    public ITip getiTip() {
        return iTip;
    }

    public void setiTip(ITip iTip) {
        this.iTip = iTip;
    }

    /**
     * tablayout mode
     *
     * @return
     */
    protected int getTabMode() {
        return TabLayout.MODE_FIXED;
    }

    @Override
    protected int getTabLayoutResId() {
        return R.layout.item_tab_online_consult;
    }

    @Override
    protected int getTabTitleTextViewResId() {
        return R.id.tv_title;
    }

    @Override
    protected int getSelectedTabIndicatorColor() {
        return R.color.color_00c8aa;
    }

    @Override
    protected void onTabInit(View[] tabViewArray) {
        super.onTabInit(tabViewArray);
        this.tabViewArray = tabViewArray;
        viewPager.setCurrentItem(getArguments().getInt("pageItem",0));
    }

    @Override
    public void updateMsgCount(int tab, String count) {
        if (tabViewArray == null || tabViewArray[tab] == null) {
            return;
        }
        TextView tv_msg_number =  (TextView) tabViewArray[tab].findViewById(R.id.tv_msg_number1);
        tv_msg_number.setVisibility(View.VISIBLE);
        tv_msg_number.setBackgroundColor(Color.TRANSPARENT);
        if (TextUtils.isEmpty(count)||count.equals("0")){
            tv_msg_number.setTextColor(Color.GRAY);
            tv_msg_number.setText("(0)");
        }else {
            tv_msg_number.setTextColor(Color.RED);
            if (Integer.parseInt(count)>=100){
                tv_msg_number.setText("("+"99+"+")");
            }else{
                tv_msg_number.setText("("+count+")");
            }

        }
//        if (tab == 1) {
//            iv_arrow = (ImageView) tabViewArray[tab].findViewById(R.id.iv_arrow);
//            iv_arrow.setVisibility(View.VISIBLE);
//            iv_arrow.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    showPop();
//                }
//            });
//            tv_title = (TextView) tabViewArray[tab].findViewById(R.id.tv_title);
//        }
    }

    private void showPop() {
        if (mPopupWindow == null) {
            mPopupWindow = new SelectBasePopupWindow(true, getActivity());
            popRoot = View.inflate(getActivity(), R.layout.pop_layout_onlineroom1, null);
            lay1 = popRoot.findViewById(R.id.lay1);
            lay2 = popRoot.findViewById(R.id.lay2);
            lay1.setOnClickListener(mPopOnClickListener);
            lay2.setOnClickListener(mPopOnClickListener);

            mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    iv_arrow.setImageResource(R.drawable.onlineroom_arrow_down);
                }
            });
        }
        if (!mPopupWindow.isShowing()) {
//            mPopupWindow.init(popRoot).showAtLocation(this.getWindow().getDecorView(), Gravity.TOP | Gravity.RIGHT, (AppUtils.dpToPx(48, getResources()) - 24) / 2 + AppUtils.dpToPx(5, getResources()) - 30 - 27, AppUtils.dpToPx(48, getResources()) + YMApplication.getStatusBarHeight() - 30);
            mPopupWindow.init(popRoot).showAtLocation(getActivity().getWindow().getDecorView(), Gravity.TOP | Gravity.RIGHT, AppUtils.dpToPx(5, getResources()), AppUtils.dpToPx(48 + 40, getResources()) + YMApplication.getStatusBarHeight());
        }
        iv_arrow.setImageResource(R.drawable.onlineroom_arrow_up);
    }


    /**
     * pop监听器
     */
    private View.OnClickListener mPopOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mPopupWindow.dismiss();
            OnlineRoomItemFragment fragment = (OnlineRoomItemFragment) fragments.get(1);
            switch (view.getId()) {
                case R.id.lay1:
                    tv_title.setText("我的待接诊");
                    fragment.setType(3);
                    break;
                case R.id.lay2:
                    tv_title.setText("未指定待接诊");
                    fragment.setType( 4);
                    break;
                default:
                    break;

            }
            fragment.setModifyMsgTip(OnlineRoomPagerFragment.this);
            fragment.loadData(State.ONREFRESH.getFlag());
        }

    };

    @Override
    public void onStart() {

        super.onStart();
    }


    private enum State {
        ONREFRESH(1), LOADMORE(2);
        private int flag;

        private State(int flag) {
            this.flag = flag;
        }

        public int getFlag() {
            return this.flag;
        }
    }
}
