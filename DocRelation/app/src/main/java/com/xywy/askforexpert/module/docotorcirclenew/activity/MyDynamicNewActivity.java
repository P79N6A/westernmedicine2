package com.xywy.askforexpert.module.docotorcirclenew.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.base.fragment.CommonListFragment;
import com.xywy.askforexpert.module.docotorcirclenew.adapter.AdapterFactory;
import com.xywy.askforexpert.module.docotorcirclenew.adapter.BaseUltimateRecycleAdapter;
import com.xywy.askforexpert.module.docotorcirclenew.fragment.tab.MyCircleDynamicMsgTabFragment;
import com.xywy.askforexpert.module.docotorcirclenew.model.DoctorCircleModelFactory;
import com.xywy.askforexpert.module.docotorcirclenew.model.IRecycleViewModel;
import com.xywy.askforexpert.module.docotorcirclenew.model.PublishType;

/**
 * 我的动态
 *
 * @author LG
 */
public class MyDynamicNewActivity extends YMBaseActivity {
    private String name;
    private String visitedUserId;
    private
    @PublishType
    String type = "1";

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_new_mydynamic;
    }

    public static void startActivity(Context context, String realName, @PublishType String type, String visitedUserId) {
        Intent intent = new Intent(context, MyDynamicNewActivity.class);
        intent.putExtra("name", realName);
        intent.putExtra("visitedUserId", visitedUserId);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    protected void beforeViewBind() {
        name = getIntent().getStringExtra("name");
        type = getIntent().getStringExtra("type");
        visitedUserId = getIntent().getStringExtra("visitedUserId");
    }


    @Override
    protected void initView() {
//        if (PublishType.Realname.equals(type)) {
//            if (TextUtils.isEmpty(name)) {
//                titleBarBuilder.setTitleText("我的实名动态");
//            } else {
//                titleBarBuilder.setTitleText(name + "的实名动态");
//            }
//        } else {
//            titleBarBuilder.setTitleText("我的匿名动态");
//        }

        if (TextUtils.isEmpty(name)) {
                titleBarBuilder.setTitleText("我的动态");
            } else {
                titleBarBuilder.setTitleText(name + "的动态");
            }
        Fragment frag;
        if (visitedUserId != null){
            CommonListFragment commonListFragment =new CommonListFragment();
            IRecycleViewModel recycleViewModel = DoctorCircleModelFactory.newInstance(commonListFragment, this, type, visitedUserId);
            commonListFragment.setRecycleViewModel(recycleViewModel);
            BaseUltimateRecycleAdapter adapter = AdapterFactory.newDynanicMsgAdapter(this, type);
            commonListFragment.setAdapter(adapter);
            frag=commonListFragment;
        }else {
            frag = MyCircleDynamicMsgTabFragment.newInstance(visitedUserId != null ? visitedUserId : YMApplication.getUUid());
        }
        getSupportFragmentManager().beginTransaction().add(R.id.baseContainer, frag).commit();
    }

    @Override
    protected void initData() {

    }
}
