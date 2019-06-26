package com.xywy.askforexpert.module.discovery.medicine.module.medical;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.widget.TextView;

import com.xywy.askforexpert.R;


public class DialogueDecreaseActivity extends Activity implements OnClickListener {
    private final String CONSTATNS = "com.xywy.medicine_super_market_dialogueDecreaseActivity";
    private TextView tv_msg;
    private TextView btn_ok,btn_cancel;
    Bundle b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descrease_dialogue);
        if(null != getIntent()){
            b = getIntent().getExtras();
        }
        int version = Integer.valueOf(Build.VERSION.SDK_INT);
        if(version>=11){
            DialogueDecreaseActivity.this.setFinishOnTouchOutside(false);
        }
        initViews();
        initListeners();
    }


    public void initViews() {
        btn_ok = (TextView)findViewById(R.id.btn_ok);
        btn_cancel = (TextView)findViewById(R.id.btn_cancel);
        tv_msg = (TextView)findViewById(R.id.tv_msg);
//        tv_msg.setText(msg);
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
                DialogueDecreaseActivity.this.finish();
                break;
            case R.id.btn_ok:
                if(null != b){
                    Intent intent = new Intent(CONSTATNS);
                    intent.putExtras(b);
                    sendBroadcast(intent);
                    DialogueDecreaseActivity.this.finish();
                }
                break;
            default:
                break;
        }
    }
}
