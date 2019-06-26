package com.xywy.askforexpert.module.main.subscribe;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 首页 订阅
 *
 * @author Jack Fang
 */
public class SubscribeListActivity extends AppCompatActivity {
    private static final String TAG = "SubscribeListActivity";

    @Bind(R.id.subscribe_list_toolbar)
    Toolbar subscribeListToolbar;

    private boolean showAnim;

    SubscribeListFragment subscribeListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe_list);
        ButterKnife.bind(this);

        CommonUtils.initSystemBar(this);
        CommonUtils.setToolbar(this, subscribeListToolbar);

        showAnim = getIntent().getBooleanExtra("isShouldShowAnim", false);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        subscribeListFragment = SubscribeListFragment.newInstance(0);
        transaction.replace(R.id.rl_fragment_container, subscribeListFragment).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            if (showAnim) {
                overridePendingTransition(R.anim.stable, R.anim.bottom_out);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
            if (showAnim) {
                overridePendingTransition(R.anim.stable, R.anim.bottom_out);
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }


    @OnClick(R.id.tv_finish)
    public void onClick() {
        subscribeListFragment.saveChannel();
        finish();

    }
}
