package com.xywy.oauth.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xywy.oauth.R;


/**
 * Created by DongJr on 2016/3/21.
 */
public class AboutActivity extends BaseActivity {


    private RelativeLayout Lback;
    private TextView titleName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initID();
        initView();
    }

    private void initID() {
        Lback = findView(R.id.Lback);
        titleName = findView(R.id.title_name);
    }

    protected void initView() {
        Lback.setVisibility(View.VISIBLE);
        titleName.setText(R.string.me_about);

        Lback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
