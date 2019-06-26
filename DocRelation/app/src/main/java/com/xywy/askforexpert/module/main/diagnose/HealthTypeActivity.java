package com.xywy.askforexpert.module.main.diagnose;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xywy.askforexpert.R;

/**
 * 他的健康状态
 *
 * @author 王鹏
 * @2015-6-24上午9:05:44
 */
public class HealthTypeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health_type_main);
        ((TextView) findViewById(R.id.tv_title)).setText("TA的健康动态");
    }

    public void onClick_back(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                finish();
                break;

            default:
                break;
        }
    }
}
