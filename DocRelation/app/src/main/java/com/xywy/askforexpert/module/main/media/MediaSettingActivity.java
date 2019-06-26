package com.xywy.askforexpert.module.main.media;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.appcommon.utils.others.DLog;

/**
 * 媒体介绍 页面
 */
public class MediaSettingActivity extends AppCompatActivity {
    private static final String LOG_TAG = MediaSettingActivity.class.getSimpleName();

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.activity_media_setting);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        CommonUtils.setToolbar(this, toolbar);

        CheckBox msgTop = (CheckBox) findViewById(R.id.msg_top);

        sp = getSharedPreferences("msg_manager", MODE_PRIVATE);
        boolean isMsgTop = sp.getBoolean("mediaSetting", false);
        DLog.d(LOG_TAG, "is msg top = " + isMsgTop);
        msgTop.setChecked(isMsgTop);

        msgTop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView instanceof CheckBox) {
                    sp.edit().putBoolean("mediaSetting", isChecked).apply();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}
