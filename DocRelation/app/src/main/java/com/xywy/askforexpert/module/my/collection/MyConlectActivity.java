package com.xywy.askforexpert.module.my.collection;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.module.discovery.answer.AnswerMainActivity;
import com.xywy.askforexpert.module.discovery.answer.activity.AnsweredPaperListActivity;

/**
 * 我的收藏
 *
 * @author LG
 */
public class MyConlectActivity extends YMBaseActivity implements OnClickListener {

//    private ImageView iv_back;
    private RelativeLayout re_tabinder, re_yaodian, re_shouce, re_zixun, re_docment, rl_answer, rl_answered;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        CommonUtils.initSystemBar(this);
//        setContentView(R.layout.activity_myconlect);
//
//        initView();
//        setLinser();
//
//    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_myconlect;
    }

//    private void initView() {
//        iv_back = (ImageView) findViewById(R.id.iv_back);
//        re_zixun = (RelativeLayout) findViewById(R.id.re_zixun);
//        re_shouce = (RelativeLayout) findViewById(R.id.re_shouce);
//        re_yaodian = (RelativeLayout) findViewById(R.id.re_yaodian);
//        re_docment = (RelativeLayout) findViewById(R.id.re_docment);
//        re_tabinder = (RelativeLayout) findViewById(R.id.re_tabinder);
//        rl_answer = (RelativeLayout) findViewById(R.id.rl_answer);
//        rl_answered = (RelativeLayout) findViewById(R.id.rl_answered);
//
//    }

    @Override
    protected void initView() {
//        iv_back = (ImageView) findViewById(R.id.iv_back);
        titleBarBuilder.setTitleText("我的收藏");
        re_zixun = (RelativeLayout) findViewById(R.id.re_zixun);
        re_shouce = (RelativeLayout) findViewById(R.id.re_shouce);
        re_yaodian = (RelativeLayout) findViewById(R.id.re_yaodian);
        re_docment = (RelativeLayout) findViewById(R.id.re_docment);
        re_tabinder = (RelativeLayout) findViewById(R.id.re_tabinder);
        rl_answer = (RelativeLayout) findViewById(R.id.rl_answer);
        rl_answered = (RelativeLayout) findViewById(R.id.rl_answered);
        setLinser();
    }

    @Override
    protected void initData() {
    }


    private void setLinser() {
        // TODO Auto-generated method stub
//        iv_back.setOnClickListener(this);
        re_zixun.setOnClickListener(this);
        re_shouce.setOnClickListener(this);
        re_docment.setOnClickListener(this);
        re_yaodian.setOnClickListener(this);
        re_tabinder.setOnClickListener(this);
        re_tabinder.setOnClickListener(this);
        rl_answer.setOnClickListener(this);
        rl_answered.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent conlectIntent = new Intent(this, MyConlectListActivity.class);
        switch (v.getId()) {
//            case R.id.iv_back:
//                this.finish();
//                break;

            case R.id.re_tabinder:// 临床指南
                conlectIntent.putExtra("channel", "3");
                conlectIntent.putExtra("name", "临床指南");
                startActivity(conlectIntent);
                break;

            case R.id.re_yaodian:// 药典
                conlectIntent.putExtra("channel", "2");
                conlectIntent.putExtra("name", "药典");
                startActivity(conlectIntent);
                break;

            case R.id.re_shouce:// 手册
                conlectIntent.putExtra("channel", "1");
                conlectIntent.putExtra("name", "检查手册");
                startActivity(conlectIntent);
                break;

            case R.id.re_zixun:// 资讯
                conlectIntent.putExtra("channel", "-1");
                conlectIntent.putExtra("name", "医学资讯");
                startActivity(conlectIntent);
                break;
            case R.id.re_docment:// 医学文献
                conlectIntent.putExtra("channel", "100");
                conlectIntent.putExtra("name", "医学文献");
                startActivity(conlectIntent);
                break;
            case R.id.rl_answer:// 医学试题
                AnswerMainActivity.start(MyConlectActivity.this);
                break;
            case R.id.rl_answered:// 已答医学试题
                AnsweredPaperListActivity.start(MyConlectActivity.this);
                break;

        }


    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // getNotReadMessage(type);
        StatisticalTools.onResume(this);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        StatisticalTools.onPause(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub

        super.onDestroy();
    }
}
