package com.xywy.askforexpert.module.consult.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.model.consultentity.ConsultPagerTabBean;
import com.xywy.askforexpert.model.consultentity.ConsultPagerTabItemBean;
import com.xywy.askforexpert.model.consultentity.IMQuestionBean;
import com.xywy.askforexpert.model.consultentity.QuestionInHandleRspEntity;
import com.xywy.askforexpert.module.consult.ConsultConstants;
import com.xywy.askforexpert.module.consult.ServiceProvider;
import com.xywy.askforexpert.module.consult.activity.ConsultChatActivity;
import com.xywy.uilibrary.fragment.pagerfragment.adapter.XywyFragmentPagerAdapter;
import com.xywy.uilibrary.fragment.pagerfragment.fragment.XYWYTabPagerFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import rx.Subscriber;

/**
 * 在线咨询页 切换我的咨询和问题库 stone
 * Created by zhangzheng on 2017/4/26.
 */

public class ConsultPagerFragment extends XYWYTabPagerFragment<ConsultPagerTabBean> implements ConsultItemFragment.IModifyMsgTip {

    private XywyFragmentPagerAdapter<ConsultPagerTabBean> pagerAdapter;
    private List<Fragment> fragments;
    private List<ConsultPagerTabBean> consultPagerTabBeanList;
    private static final String STR_MY_UNANSWERED = "未回复";
    private static final String STR_MY_CONSULT = "处理中";
    private static final String STR_QUESTIONS = "问题库";
    private static final String NOT_SUMUP = "未总结";
    private static final String ALL = "已关闭";

    private View[] tabViewArray;
    private ITip iTip;
    private String doctorId = YMApplication.getUUid();
    private ConsultItemFragment itemFragment1;
    private ConsultItemFragment itemFragment2;


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
        consultPagerTabBeanList = new ArrayList<>();
        ConsultPagerTabItemBean itemBean = new ConsultPagerTabItemBean(ConsultPagerTabItemBean.TYPE_UNANSWERED, STR_MY_UNANSWERED);
        consultPagerTabBeanList.add(new ConsultPagerTabBean(STR_MY_UNANSWERED, itemBean));

        itemBean = new ConsultPagerTabItemBean(ConsultPagerTabItemBean.TYPE_MY_CONSULT, STR_MY_CONSULT);
        consultPagerTabBeanList.add(new ConsultPagerTabBean(STR_MY_CONSULT, itemBean));
//        itemBean = new ConsultPagerTabItemBean(ConsultPagerTabItemBean.TYPE_QUESTIONS, STR_QUESTIONS);
//        consultPagerTabBeanList.add(new ConsultPagerTabBean(STR_QUESTIONS, itemBean));
        //问题库注释
//        fragments.add(ConsultItemFragment.newInstance(consultPagerTabBeanList.get(1), this, true,0));
        itemBean = new ConsultPagerTabItemBean(ConsultPagerTabItemBean.TYPE_ANSWERED, ALL);
        consultPagerTabBeanList.add(new ConsultPagerTabBean(NOT_SUMUP, itemBean));
        itemBean = new ConsultPagerTabItemBean(ConsultPagerTabItemBean.TYPE_ANSWERED, ALL);
        consultPagerTabBeanList.add(new ConsultPagerTabBean(ALL, itemBean));

        fragments = new ArrayList<>();
        fragments.add(ConsultItemFragment.newInstance(consultPagerTabBeanList.get(0), this, false,0));
        itemFragment1 = (ConsultItemFragment) fragments.get(0);

        fragments.add(ConsultItemFragment.newInstance(consultPagerTabBeanList.get(1), this, false,0));
        itemFragment2 = (ConsultItemFragment) fragments.get(1);

        fragments.add(ConsultItemFragment.newInstance(consultPagerTabBeanList.get(2), this, true,1));
        fragments.add(ConsultItemFragment.newInstance(consultPagerTabBeanList.get(3), this, true,2));

        addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (iTip != null) {
                    switch (position) {
                        case 0:
                        case 1:
                            iTip.showTip(getResources().getString(R.string.consult_bottom_tip));
                            break;
                        case 2:
                            iTip.showTip(getResources().getString(R.string.question_close_tip));
                            break;
                        case 3:
                            iTip.showTip("");
                            break;
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setOffscreenPageLimit(fragments.size());
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData(){
        StatisticalTools.eventCount(YMApplication.getAppContext(), ConsultConstants.EVENT_MY_CONSULT);
        ServiceProvider.getQuestionsInHandle(doctorId, new Subscriber<QuestionInHandleRspEntity>() {
            @Override
            public void onCompleted() {
//                itemFragment1.processingData(null);
//                itemFragment2.processingData(null);
            }

            @Override
            public void onError(Throwable e) {
                itemFragment1.processingData(null,1);
                itemFragment2.processingData(null,2);
            }

            @Override
            public void onNext(QuestionInHandleRspEntity entry) {
                if (null!=entry) {
                    ArrayList<IMQuestionBean> unReplyList = new ArrayList<IMQuestionBean>();
                    QuestionInHandleRspEntity unReplyEntry = new QuestionInHandleRspEntity();
                    unReplyEntry.setData(new QuestionInHandleRspEntity.DataBean());
                    if (null != entry.getData() && null != entry.getData().getList()) {
                        unReplyEntry.getData().setNo_read_total(entry.getData().getNo_read_total());
                        unReplyEntry.getData().setTotal(entry.getData().getTotal());
                        unReplyEntry.getData().setList(new ArrayList<IMQuestionBean>());
                        for (IMQuestionBean data : entry.getData().getList()) {
                            if (!TextUtils.isEmpty(data.getIs_new_reply())) {
                                if (data.getIs_new_reply().equals("0")) {
                                    unReplyList.add(data);
                                    unReplyEntry.getData().setList(unReplyList);
                                }
                            }
                        }
                    }
                    itemFragment2.processingData(entry, 2);
                    itemFragment1.processingData(unReplyEntry, 1);

                }else {
//                    Toast.makeText(getActivity(),"数据返回异常",Toast.LENGTH_SHORT).show();
                    Log.e("dataError","数据返回异常");
                }

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
    }

    @Override
    public void updateMsgCount(int tab, String count) {
        if (tabViewArray == null || tabViewArray[tab] == null) {
            return;
        }
        TextView tvCount = (TextView) tabViewArray[tab].findViewById(R.id.tv_msg_number);
        if (tvCount != null) {
            if (count.equals("0")) {
                tvCount.setVisibility(View.INVISIBLE);
            } else {
                tvCount.setVisibility(View.VISIBLE);
                tvCount.setText(count);
            }
        }
    }

    @Override
    public void loadProcessingData() {
        loadData();
    }



}
