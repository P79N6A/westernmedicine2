package com.xywy.develop_settings;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.github.pedrovgs.lynx.LynxActivity;
import com.github.pedrovgs.lynx.LynxConfig;
import com.github.pedrovgs.lynx.model.TraceLevel;
import com.jakewharton.processphoenix.ProcessPhoenix;

public class DeveloperSettingsActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    LynxConfig lynxConfig;
    DeveloperSettingsPresenter presenter;

    TextView buildDateTextView;

    TextView buildVersionCodeTextView;

    TextView buildVersionNameTextView;

    Switch stethoSwitch;

    Switch leakCanarySwitch;

    Switch tinyDancerSwitch;

    Switch blockCanarySwitch;

    Button showLog;

    Button restartApp;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_developer_settings);
        initView();
        initListener();
        lynxConfig = new LynxConfig();
        lynxConfig.setFilterTraceLevel(TraceLevel.ERROR);
        lynxConfig.setMaxNumberOfTracesToShow(20000);
        presenter = new DeveloperSettingsPresenter(DevelopSettingManager.getDeveloperSettingModel());
        presenter.bindView(this);
    }

    private void initListener() {
        stethoSwitch.setOnCheckedChangeListener(this);
        leakCanarySwitch.setOnCheckedChangeListener(this);
        tinyDancerSwitch.setOnCheckedChangeListener(this);
        blockCanarySwitch.setOnCheckedChangeListener(this);
        showLog.setOnClickListener(this);
        restartApp.setOnClickListener(this);
    }

    private void initView() {
        buildDateTextView = (TextView) findViewById(R.id.developer_settings_build_date_text_view);

        buildVersionCodeTextView = (TextView) findViewById(R.id.developer_settings_build_version_code_text_view);

        buildVersionNameTextView = (TextView) findViewById(R.id.developer_settings_build_version_name_text_view);

        stethoSwitch = (Switch) findViewById(R.id.developer_settings_stetho_switch);

        leakCanarySwitch = (Switch) findViewById(R.id.developer_settings_leak_canary_switch);

        tinyDancerSwitch = (Switch) findViewById(R.id.developer_settings_tiny_dancer_switch);

        blockCanarySwitch= (Switch) findViewById(R.id.developer_settings_block_canary_switch);

        showLog = (Button) findViewById(R.id.b_show_log);

        restartApp = (Button) findViewById(R.id.developer_settings_restart_app_button);

    }

    void onStethoSwitchCheckedChanged(boolean checked) {
        presenter.changeStethoState(checked);
    }

    void onTinyDancerSwitchCheckedChanged(boolean checked) {
        presenter.changeTinyDancerState(checked);
    }

    void onRestartAppClick() {
        ProcessPhoenix.triggerRebirth(this, new Intent(this, this.getClass()));
    }


    public void changeBuildDate(@NonNull final String date) {
        runOnUiThreadIfFragmentAlive(new Runnable() {
            @Override
            public void run() {
                {
                    assert buildDateTextView != null;
                    buildDateTextView.setText(date);
                }
            }
        });
    }

    public void changeBuildVersionCode(@NonNull final String versionCode) {
        runOnUiThreadIfFragmentAlive(new Runnable() {
            @Override
            public void run() {
                {
                    assert buildVersionCodeTextView != null;
                    buildVersionCodeTextView.setText(versionCode);
                }
            }
        });
    }

    public void changeBuildVersionName(@NonNull final String versionName) {
        runOnUiThreadIfFragmentAlive(new Runnable() {
            @Override
            public void run() {
                {
                    assert buildVersionNameTextView != null;
                    buildVersionNameTextView.setText(versionName);
                }
            }
        });
    }

    public void changeStethoState(final boolean enabled) {
        runOnUiThreadIfFragmentAlive(new Runnable() {
            @Override
            public void run() {
                {
                    assert stethoSwitch != null;
                    stethoSwitch.setChecked(enabled);
                }
            }
        });
    }

    public void changeLeakCanaryState(final boolean enabled) {
        runOnUiThreadIfFragmentAlive(new Runnable() {
            @Override
            public void run() {
                {
                    assert leakCanarySwitch != null;
                    leakCanarySwitch.setChecked(enabled);
                }
            }
        });
    }

    public void changeBlockCanaryState(final boolean enabled) {
        runOnUiThreadIfFragmentAlive(new Runnable() {
            @Override
            public void run() {
                {
                    assert blockCanarySwitch != null;
                    blockCanarySwitch.setChecked(enabled);
                }
            }
        });
    }

    public void changeTinyDancerState(final boolean enabled) {
        runOnUiThreadIfFragmentAlive(new Runnable() {
            @Override
            public void run() {
                {
                    assert tinyDancerSwitch != null;
                    tinyDancerSwitch.setChecked(enabled);
                }
            }
        });
    }

    @SuppressLint("ShowToast")
    // Yeah, Lambdas and Lint are not good friends…
    public void showMessage(@NonNull final String message) {
        runOnUiThreadIfFragmentAlive(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(DeveloperSettingsActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    void onLeakCanarySwitchCheckedChanged(boolean checked) {
        presenter.changeLeakCanaryState(checked);
    }

    void onBlockCanarySwitchCheckedChanged(boolean checked) {
        presenter.changeBlockCanaryState(checked);
    }

    @SuppressLint("ShowToast")
    // Yeah, Lambdas and Lint are not good friends…
    public void showAppNeedsToBeRestarted() {
        runOnUiThreadIfFragmentAlive(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(DeveloperSettingsActivity.this, "To apply new settings app needs to be restarted", Toast.LENGTH_LONG).show();
            }
        })
        ;
    }

    void showLog() {
        startActivity(LynxActivity.getIntent(DeveloperSettingsActivity.this, lynxConfig));
    }

    protected void runOnUiThreadIfFragmentAlive(@NonNull final Runnable runnable) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run();
        } else {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    runnable.run();
                }
            });
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.developer_settings_stetho_switch)
            onStethoSwitchCheckedChanged(isChecked);
        else if (buttonView.getId() == R.id.developer_settings_tiny_dancer_switch)
            onTinyDancerSwitchCheckedChanged(isChecked);
        else if (buttonView.getId() == R.id.developer_settings_leak_canary_switch)
            onLeakCanarySwitchCheckedChanged(isChecked);
        else if (buttonView.getId() == R.id.developer_settings_block_canary_switch)
            onBlockCanarySwitchCheckedChanged(isChecked);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.b_show_log)
            showLog();
        else if (v.getId() == R.id.developer_settings_restart_app_button)
            onRestartAppClick();
    }
}
