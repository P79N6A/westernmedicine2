package com.xywy.askforexpert.module.my.download;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;

public class MyDownActivity extends YMBaseActivity implements OnClickListener {

//    private ImageView iv_back;
    private RelativeLayout rl_zhinan, rl_docments;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        CommonUtils.initSystemBar(this);
//
//        setContentView(R.layout.mydown);
//        intiView();
//        intilinser();
//
//    }
//
//    private void intiView() {
//
//        iv_back = (ImageView) findViewById(R.id.iv_back1);
//        rl_zhinan = (RelativeLayout) findViewById(R.id.rl_zhinan);
//        rl_docments = (RelativeLayout) findViewById(R.id.rl_docments);
//    }

    @Override
    protected int getLayoutResId() {
        return R.layout.mydown;
    }

    @Override
    protected void initView() {
        titleBarBuilder.setTitleText("我的下载");
        rl_zhinan = (RelativeLayout) findViewById(R.id.rl_zhinan);
        rl_docments = (RelativeLayout) findViewById(R.id.rl_docments);
        intilinser();
    }

    @Override
    protected void initData() {

    }

    private void intilinser() {

//        iv_back.setOnClickListener(this);
        rl_docments.setOnClickListener(this);
        rl_zhinan.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;

        switch (v.getId()) {
//            case R.id.iv_back1:
//                MyDownActivity.this.finish();
//
//                break;

            case R.id.rl_zhinan://临床指南
                intent = new Intent(this, DownloadActivity.class);//
                intent.putExtra("type_commed", "0");//0指南1文献
                startActivity(intent);
                break;
            case R.id.rl_docments://文献
                intent = new Intent(this, DownloadActivity.class);
                intent.putExtra("type_commed", "1");
                startActivity(intent);
                break;
            default:
                break;
        }

    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}
