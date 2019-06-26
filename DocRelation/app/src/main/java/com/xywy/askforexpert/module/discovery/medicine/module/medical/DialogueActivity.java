package com.xywy.askforexpert.module.discovery.medicine.module.medical;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.widget.TextView;

import com.xywy.askforexpert.R;

/**
 * 对话框Activity 发送处方的弹框
 * stone
 * 2017/11/1 下午4:02
 */
public class DialogueActivity extends Activity implements OnClickListener {
    private final String DIALOGUE_CONSTANTS = "com.xywy.medicine_super_market_dialogueActivity";
    private final String REQUEST_CODE = "request_code";
    private final String MSG = "msg";
    private TextView tv_msg,tv_title;
    private TextView btn_ok,btn_cancel;
    Bundle b;
    private String msg;
    private int mRequestCode = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialogue);
        if(null != getIntent()){
            b = getIntent().getExtras();
            msg = b.getString(MSG);
            mRequestCode = b.getInt(REQUEST_CODE);
        }
        int version = Integer.valueOf(Build.VERSION.SDK_INT);
        if(version>=11){
            DialogueActivity.this.setFinishOnTouchOutside(false);
        }
        initViews();
        initListeners();
    }


    public void initViews() {
        btn_ok = (TextView)findViewById(R.id.btn_ok);
        btn_cancel = (TextView)findViewById(R.id.btn_cancel);
        tv_msg = (TextView)findViewById(R.id.tv_msg);
        tv_title = (TextView)findViewById(R.id.title);
        tv_title.setVisibility(View.INVISIBLE);
        if(TextUtils.isEmpty(msg)){
            msg = "";
        }
        tv_msg.setText(msg);
    }

    public void initListeners() {
        // TODO Auto-generated method stub
        btn_ok.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
    }

    public void initData() {
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);}

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && isOutOfBounds(this, event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    private boolean isOutOfBounds(Activity context, MotionEvent event) {
        final int x = (int) event.getX();
        final int y = (int) event.getY();
        final int slop = ViewConfiguration.get(context).getScaledWindowTouchSlop();
        final View decorView = context.getWindow().getDecorView();
        return (x < -slop) || (y < -slop)|| (x > (decorView.getWidth() + slop))|| (y > (decorView.getHeight() + slop));
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                DialogueActivity.this.finish();
                break;
            case R.id.btn_ok:
                if(null != b){
                    Intent intent = new Intent(DIALOGUE_CONSTANTS);
                    intent.putExtra(REQUEST_CODE,mRequestCode);
                    sendBroadcast(intent);
                    DialogueActivity.this.finish();
                }
                break;
            default:
                break;
        }
    }
}
