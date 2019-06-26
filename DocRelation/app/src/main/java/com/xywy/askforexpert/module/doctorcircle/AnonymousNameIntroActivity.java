package com.xywy.askforexpert.module.doctorcircle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;

public class AnonymousNameIntroActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private WebView anonymousNameWeb;
    private Toolbar toolbar;
    private String anonymousName;
    private int type = 1;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);

        setContentView(R.layout.activity_anonymous_name_intro);

        initView();
        CommonUtils.setToolbar(this, toolbar);
        getParams();
        CommonUtils.setWebView(anonymousNameWeb);
        anonymousNameWeb.getSettings().setJavaScriptEnabled(false);
        loadData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }

    private void getParams() {
        anonymousName = getIntent().getStringExtra("anonymousName");
        type = getIntent().getIntExtra("type", 1);
        url = getIntent().getStringExtra("url");
    }

    private void loadData() {
        anonymousNameWeb.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress < 100) {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(newProgress);
                } else {
                    progressBar.setVisibility(View.GONE);
                }
            }

        });
        if (type == 0) {
            anonymousNameWeb.loadUrl(url);
        } else {
            anonymousNameWeb.loadUrl(CommonUrl.ANONYMOUS_INTRO + anonymousName);
        }
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        anonymousNameWeb = (WebView) findViewById(R.id.anonymous_name_web);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
