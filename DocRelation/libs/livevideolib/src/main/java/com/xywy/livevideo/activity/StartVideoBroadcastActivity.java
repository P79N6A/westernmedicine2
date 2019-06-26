package com.xywy.livevideo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.xywy.base.XywyBaseActivity;
import com.xywy.livevideolib.R;


/**
 * Created by bailiangjin on 2017/2/20.
 */

public class StartVideoBroadcastActivity extends XywyBaseActivity {

    Spinner sp_type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_video_broadcast);

        sp_type = (Spinner) findViewById(R.id.sp_type);

        sp_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] languages = getResources().getStringArray(R.array.live_video_show_type);
                Toast.makeText(StartVideoBroadcastActivity.this, "选择了" + position + ":" + languages[position], Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(StartVideoBroadcastActivity.this, "未选择任何项目", Toast.LENGTH_SHORT).show();

            }
        });

    }
}
