package com.xywy.askforexpert.widget.view;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.utils.WebViewUtils2;

/**
 * 带进度条的WebView stone
 *
 * @author 王鹏
 * @2015-9-18上午9:37:42
 */
@SuppressWarnings("deprecation")
public class ProgressWebView extends WebView {

    private ProgressBar progressbar;

    public ProgressWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        progressbar = new ProgressBar(context, null,
                android.R.attr.progressBarStyleHorizontal);
        progressbar.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
                10, 0, 0));
        progressbar.setIndeterminateDrawable(getResources().getDrawable(R.color.color_00c8aa));
        progressbar.setMax(100);
        addView(progressbar);
        setWebChromeClient(new WebChromeClient());
        WebViewUtils2.safeEnhance(this);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        LayoutParams lp = (LayoutParams) progressbar.getLayoutParams();
        lp.x = l;
        lp.y = t;
        progressbar.setLayoutParams(lp);
        super.onScrollChanged(l, t, oldl, oldt);
    }

    public class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);

            if (newProgress >= 100) {
                progressbar.setVisibility(GONE);
            } else {
                if (progressbar.getVisibility() == GONE) {
                    progressbar.setVisibility(VISIBLE);
                }
                progressbar.setProgress(newProgress);
            }
        }
    }

}