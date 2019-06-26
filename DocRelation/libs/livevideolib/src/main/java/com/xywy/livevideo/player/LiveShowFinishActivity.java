package com.xywy.livevideo.player;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.xywy.base.XywyBaseActivity;
import com.xywy.livevideo.LiveManager;
import com.xywy.livevideo.entity.FinishLiveBean;
import com.xywy.livevideo.utils.H5PageUtils;
import com.xywy.livevideolib.R;
import com.xywy.util.ImageLoaderUtils;

/**
 * Created by bailiangjin on 2017/3/1.
 */

public class LiveShowFinishActivity extends XywyBaseActivity implements View.OnClickListener{

    TextView tv_user_name;
    TextView tv_view_number;
    TextView tv_gift_number;

    ImageView iv_head;
    ImageView iv_close;

    Button btn_back_to_home;
    Button btn_view_gift;
    FinishLiveBean data;
    public static void start(Activity activity, FinishLiveBean data){
        Intent intent=new Intent(activity, LiveShowFinishActivity.class);
        Bundle params=new Bundle();
        params.putSerializable("data",data);
        intent.putExtras(params);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_show_finish);
        hideCommonBaseTitle();
        data= (FinishLiveBean) getIntent().getSerializableExtra("data");
        initView();
    }

    private void initView() {
        tv_user_name= (TextView) findViewById(R.id.tv_user_name);
        tv_view_number= (TextView) findViewById(R.id.tv_view_number);
        tv_gift_number= (TextView) findViewById(R.id.tv_gift_number);

        iv_head= (ImageView) findViewById(R.id.iv_head);
        iv_close= (ImageView) findViewById(R.id.iv_close);

        btn_back_to_home= (Button) findViewById(R.id.btn_back_to_home);
        btn_view_gift= (Button) findViewById(R.id.btn_view_gift);

        tv_user_name.setText(data.getName());
        tv_view_number.setText(data.getAmount()+"");
        tv_gift_number.setText(data.getGiftNum());
        iv_head.setOnClickListener(this);
        iv_close.setOnClickListener(this);
        btn_back_to_home.setOnClickListener(this);
        btn_view_gift.setOnClickListener(this);

        String headUrl=LiveManager.getInstance().getConfig().myHeadImageUrl;
        if(!TextUtils.isEmpty(headUrl)){
            //  加载头像图片
            ImageLoaderUtils.getInstance().displayImage(headUrl,iv_head);
        }
    }

    @Override
    public void onClick(View v) {

        if (R.id.iv_head==v.getId()){
            // TODO: 2017/3/1 是否跳转个人信息页？
            return;
        }

        if (R.id.iv_close==v.getId()){

            finish();
            return;
        }

        if (R.id.btn_back_to_home==v.getId()){
            //跳转首页
            finish();
            return;
        }

        if (R.id.btn_view_gift==v.getId()){
            //跳转礼物页
            H5PageUtils.toH5Page(LiveShowFinishActivity.this,"直播收益",H5PageUtils.LIVE_SHOW_INCOME_PAGE_URL);
            return;
        }


    }
}
