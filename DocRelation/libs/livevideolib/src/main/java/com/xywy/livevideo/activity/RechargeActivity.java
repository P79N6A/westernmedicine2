package com.xywy.livevideo.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.xywy.base.XywyBaseActivity;
import com.xywy.livevideo.LiveManager;
import com.xywy.livevideo.RechargeType;
import com.xywy.livevideo.adapter.RechargeAdapter;
import com.xywy.livevideo.entity.RechargeBean;
import com.xywy.livevideo.utils.H5PageUtils;
import com.xywy.livevideolib.R;
import com.xywy.uilibrary.recyclerview.adapter.DividerItemDecoration;
import com.xywy.util.IntentHelper;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

public class RechargeActivity extends XywyBaseActivity {
    TextView tvHealthCoin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        int type=getIntent().getIntExtra("type", RechargeType.recharge);
        if (type==RechargeType.recharge){
            titleBarBuilder.setTitleText("充值");
        }else {
            titleBarBuilder.setTitleText("健康币")
                    .setRignt("明细", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            H5PageUtils.toH5Page(RechargeActivity.this,"明细",H5PageUtils.CONSUME_DETAIL_PAGE_URL);
                        }
                    });
        }
        initRecycleView();
        tvHealthCoin= (TextView) findViewById(R.id.tv_health_coin);
        initContactPhone();
        setToolbarTransparent(0);
    }



    @Override
    protected void onResume() {
        super.onResume();
        tvHealthCoin.setText(LiveManager.getInstance().getConfig().healthIcon+"");
    }

    /**
     *
     * @param ctx
     * @param type  选择是进入充值页面还是我的健康币页面
     */
    public static void start(Context ctx,@RechargeType int type){
        Intent intent = new Intent(ctx, RechargeActivity.class);
        intent.putExtra("type",type);
        ctx.startActivity(intent);
    }

    private void initRecycleView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_recharge);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        final RechargeAdapter adapter=new RechargeAdapter(this);
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                RechargeBean data =adapter.getItem(position);
                Intent intent = new Intent();
                intent.putExtra("userId", LiveManager.getInstance().getConfig().userId);
                intent.putExtra("goodsId","还没确定");
                intent.putExtra("money",data.getRealMoney());
                intent.setAction("com.xywy.action.Recharge");

                IntentHelper.startImplicitActivity(RechargeActivity.this,intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        List<RechargeBean> datas=new ArrayList<>();
        datas.add(new RechargeBean(60,6));
        datas.add(new RechargeBean(300,30));
        datas.add(new RechargeBean(980,98));
        adapter.setData(datas);
        recyclerView.setAdapter(adapter);
    }

    private void initContactPhone() {
        String s1="充值如果有问题，请联系客服电话:";
        String s2="400-8591-200";
        SpannableStringBuilder builder = new SpannableStringBuilder(s1+s2);
//ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
        ForegroundColorSpan blackSpan = new ForegroundColorSpan(Color.parseColor("#999999"));
        ForegroundColorSpan blueSpan = new ForegroundColorSpan(Color.parseColor("#0dc3ce"));
        builder.setSpan(blackSpan, 0, s1.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        builder.setSpan(blueSpan, s1.length(), (s1+s2).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new MyClickableSpan(s2), s1.length(), (s1+s2).length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        TextView tv= (TextView) findViewById(R.id.tv_contact);
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        tv.setText(builder);
    }

    private static class MyClickableSpan extends ClickableSpan {
        public MyClickableSpan(String content) {
            this.content = content;
        }

        private String content;
        @Override
        public void onClick(View widget) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:"+content));
            widget.getContext().startActivity(intent);
        }
    }
}
