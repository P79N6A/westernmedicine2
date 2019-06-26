package com.xywy.askforexpert.module.doctorcircle.topic;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.DialogUtil;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.YMOtherUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.doctor.Messages;
import com.xywy.askforexpert.module.discovery.medicine.common.MyCallBack;
import com.xywy.askforexpert.module.my.userinfo.PersonInfoActivity;
import com.xywy.askforexpert.widget.TabPageIndicator;
import com.xywy.askforexpert.widget.UnderlinePageIndicatorEx;
import com.xywy.uilibrary.titlebar.ItemClickListener;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 更多话题/热门话题
 * Created by shihao on 16/4/21.
 */
public class MoreTopicActivity extends YMBaseActivity {

    private static final String TAG = "MoreTopicActivity";

    @Bind(R.id.slide_table_strip)
    UnderlinePageIndicatorEx slideTableStrip;
    @Bind(R.id.topic_viewpager)
    ViewPager topicViewpager;
    @Bind(R.id.tab_indicator)
    TabPageIndicator tabIndicator;

    private String[] titles = {"推荐", "热门", "最新", "我的"};

    private List<Fragment> mFragmentList;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_more_topic);
//
//        CommonUtils.initSystemBar(this);
//        ButterKnife.bind(this);
//        initData();
//        initView();
//    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_more_topic;
    }

    @Override
    protected void initView() {
        titleBarBuilder.setTitleText("热门话题");

        titleBarBuilder.addItem("",R.drawable.more_search_icon,new ItemClickListener() {
            @Override
            public void onClick() {
                StatisticalTools.eventCount(MoreTopicActivity.this, "yqTopicMoreSearch");
                Intent intent = new Intent();
                intent.setClass(MoreTopicActivity.this, TopicSearchActivity.class);
                intent.putExtra("from", 2);
                startActivity(intent);
            }
        }).build();
        topicViewpager.setOffscreenPageLimit(titles.length);
        topicViewpager.setAdapter(new MoreTopicPager(getSupportFragmentManager()));
        tabIndicator.setViewPager(topicViewpager);
        tabIndicator.setOnTabViewSelectedListener(new TabPageIndicator.OnTabViewSelectedListener() {
            @Override
            public void onTabViewSelected(int position) {
                DLog.i(TAG, "点击的位置" + position);
                switch (position) {
                    case 0:
                        StatisticalTools.eventCount(MoreTopicActivity.this, "yqTopicMoreRecom");
                        break;
                    case 1:
                        StatisticalTools.eventCount(MoreTopicActivity.this, "yqTopicMoreHot");
                        break;
                    case 2:
                        StatisticalTools.eventCount(MoreTopicActivity.this, "yqTopicMoreNew");
                        break;
                    case 3:
                        StatisticalTools.eventCount(MoreTopicActivity.this, "yqTopicMoreMine");
                        break;
                }
            }
        });
        slideTableStrip.setViewPager(topicViewpager);
        slideTableStrip.setFades(false);
        tabIndicator.setOnPageChangeListener(slideTableStrip);
    }

    @Override
    protected void initData() {
        mFragmentList = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            mFragmentList.add(new MoreTopicItemFragment(i));
        }
    }

//    private void initData() {
//        mFragmentList = new ArrayList<>();
//        for (int i = 0; i < titles.length; i++) {
//            mFragmentList.add(new MoreTopicItemFragment(i));
//        }
//    }

//    private void initView() {
//        topicViewpager.setOffscreenPageLimit(titles.length);
//        topicViewpager.setAdapter(new MoreTopicPager(getSupportFragmentManager()));
//        tabIndicator.setViewPager(topicViewpager);
//        tabIndicator.setOnTabViewSelectedListener(new TabPageIndicator.OnTabViewSelectedListener() {
//            @Override
//            public void onTabViewSelected(int position) {
//                DLog.i(TAG, "点击的位置" + position);
//                switch (position) {
//                    case 0:
//                        StatisticalTools.eventCount(MoreTopicActivity.this, "yqTopicMoreRecom");
//                        break;
//                    case 1:
//                        StatisticalTools.eventCount(MoreTopicActivity.this, "yqTopicMoreHot");
//                        break;
//                    case 2:
//                        StatisticalTools.eventCount(MoreTopicActivity.this, "yqTopicMoreNew");
//                        break;
//                    case 3:
//                        StatisticalTools.eventCount(MoreTopicActivity.this, "yqTopicMoreMine");
//                        break;
//                }
//            }
//        });
//        slideTableStrip.setViewPager(topicViewpager);
//        slideTableStrip.setFades(false);
//        tabIndicator.setOnPageChangeListener(slideTableStrip);
//    }

//    @OnClick({R.id.more_back_btn, R.id.search_btn, R.id.create_layout})
    @OnClick({ R.id.create_layout})
    public void onClick(View view) {
//        Intent intent = new Intent();
        switch (view.getId()) {
//            case R.id.more_back_btn:
//                finish();
//                break;
//            case R.id.search_btn:
//                StatisticalTools.eventCount(MoreTopicActivity.this, "yqTopicMoreSearch");
//                intent.setClass(MoreTopicActivity.this, TopicSearchActivity.class);
//                intent.putExtra("from", 2);
//                startActivity(intent);
//                break;
            case R.id.create_layout:
                StatisticalTools.eventCount(MoreTopicActivity.this, "yqTopicMoreCreate");
                if (!YMUserService.isGuest()) {
//                    String uuid = YMApplication.getLoginInfo().getData().getPid();
//                    checkAllInfos(uuid);

                    DialogUtil.showUserCenterCertifyDialog(MoreTopicActivity.this, new MyCallBack() {
                    @Override
                    public void onClick(Object object) {
                        Intent i = new Intent();
                        i.setClass(MoreTopicActivity.this, CreateEditTopicActivity.class);
                        i.putExtra(CreateEditTopicActivity.TOPIC_TYPE_KEY, Constants.CREATE_TOPIC);
                        startActivity(i);
                    }
                }, null, null);
                } else {
                    //go to login
                    DialogUtil.LoginDialog(new YMOtherUtils(MoreTopicActivity.this).context);
                }
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatisticalTools.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatisticalTools.onResume(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    /**
     * 检查用户信息是否完善
     */
    private void checkAllInfos(String uuid) {
        FinalHttp fh = new FinalHttp();

        String sign = MD5Util.MD5(uuid + "0" + "0" + Constants.MD5_KEY);

        AjaxParams params = new AjaxParams();
        params.put("a", "dynamic");
        params.put("m", "dynamic_new");
        params.put("userid", uuid);
        params.put("oldid", "0");
        params.put("noldid", "0");
        params.put("bind", uuid + "0" + "0");
        params.put("sign", sign);
        fh.post(CommonUrl.doctor_circo_url, params, new AjaxCallBack() {

            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t,errorNo,strMsg);
            }

            public void onSuccess(String t) {
                try {
                    JSONObject jsonObject = new JSONObject(t.toString());
                    String code = jsonObject.getString("code");
                    String msg = jsonObject.getString("msg");
                    JSONObject jsonMsg = jsonObject.getJSONObject("message");
                    Messages messages = new Messages();
                    messages.setIs_doctor(jsonMsg.getString("is_doctor"));
                    messages.setSnew(jsonMsg.getString("snew"));
                    messages.setNnew(jsonMsg.getString("nnew"));
                    messages.setNunread(jsonMsg.getString("nunread"));
                    messages.setSunread(jsonMsg.getString("sunread"));
                    if ("1".equals(messages.getIs_doctor())) {// 动态列表 1：正常 0：资料不全
                        Intent intent = new Intent();
                        intent.setClass(MoreTopicActivity.this, CreateEditTopicActivity.class);
                        intent.putExtra(CreateEditTopicActivity.TOPIC_TYPE_KEY, Constants.CREATE_TOPIC);
                        startActivity(intent);
                    } else {
                        showDailog();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    ToastUtils.shortToast("网络连接超时");
                    return;
                }

            }

        });

    }

    public void showDailog() {
        String msgInfo = "";
        if (YMApplication.isDoctor()) {
            msgInfo = getString(R.string.doctor_not_all_infors);// 需要完善姓名、职称、医院、科室等信息，才能发表实名动态
        } else {
            msgInfo = getString(R.string.doctor_student_not_all_infors);// 需要完善姓名、学校、专业等信息，才能发表实名动态
        }
        android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(this);

        dialog.setMessage(msgInfo);
        dialog.setNegativeButton(getString(R.string.cancel),
                new DialogInterface.OnClickListener() {// "取消"

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.dismiss();
                    }
                });

        dialog.setPositiveButton("去完善", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Intent intent = new Intent(MoreTopicActivity.this, PersonInfoActivity.class);
                intent.putExtra("doctorInfo", "doctorInfo");
                startActivity(intent);
                arg0.dismiss();
            }
        });
        dialog.create().show();

    }

    class MoreTopicPager extends FragmentPagerAdapter {

        public MoreTopicPager(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
}
