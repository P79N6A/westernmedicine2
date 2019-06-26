package com.xywy.askforexpert.module.message.friend;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.DialogUtil;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.module.discovery.medicine.common.MyCallBack;
import com.xywy.askforexpert.module.main.media.MediaServiceListActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 添加新朋友
 *
 * @author 王鹏
 * @2015-6-1下午6:09:08 modify shr 2016-1-20
 * modified by Jack Fang @Date 2016/07/27
 */
@SuppressLint("NewApi")
public class AddNewCardHolderActivity extends Activity {
    public static boolean ismove = false;
    @Bind(R.id.btn1)
    View btn1;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.serch_layout)
    LinearLayout serchLayout;
    @Bind(R.id.re_selection)
    TextView reSelection;
    @Bind(R.id.re_hospital)
    TextView reHospital;
    @Bind(R.id.re_area)
    TextView reArea;
    @Bind(R.id.re_phone)
    TextView rePhone;
    @Bind(R.id.add_media)
    TextView addMedia;
    @Bind(R.id.add_service)
    TextView addService;
    private LinearLayout main;
    private float y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addcardholder);
        ButterKnife.bind(this);
        CommonUtils.initSystemBar(this);
        tvTitle.setText("添加好友");
        main = (LinearLayout) findViewById(R.id.main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ismove) {
            TranslateAnimation animation = new TranslateAnimation(0, 0, -y, 0);
            animation.setDuration(200);
            animation.setFillAfter(true);
            main.startAnimation(animation);
            ismove = false;
        }
        StatisticalTools.onResume(AddNewCardHolderActivity.this);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 & resultCode == RESULT_OK) {
            TranslateAnimation animation = new TranslateAnimation(0, 0, -y, 0);
            animation.setDuration(200);
            animation.setFillAfter(true);
            main.startAnimation(animation);
        }

    }

    @Override
    protected void onPause() {
        StatisticalTools.onPause(AddNewCardHolderActivity.this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    @OnClick({R.id.btn1, R.id.serch_layout, R.id.re_selection, R.id.re_hospital, R.id.re_area, R.id.re_phone, R.id.add_media, R.id.add_service})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btn1:
                this.finish();
                break;
            case R.id.serch_layout:
                StatisticalTools.eventCount(AddNewCardHolderActivity.this, "friendsearch");
                y = serchLayout.getY();
                intent = new Intent(AddNewCardHolderActivity.this, SerchCardActivity.class);
                startActivityForResult(intent, 1000);
                break;
            case R.id.re_selection:
                StatisticalTools.eventCount(AddNewCardHolderActivity.this, "department");
                //判断认证 stone
                DialogUtil.showUserCenterCertifyDialog(AddNewCardHolderActivity.this, new MyCallBack() {
                    @Override
                    public void onClick(Object data) {
                        Intent intent = new Intent(AddNewCardHolderActivity.this, SerchNewCardHolderActivity.class);
                        intent.putExtra("type", "subject");
                        startActivity(intent);
                    }
                }, null, null);

                break;
            case R.id.re_hospital:
                StatisticalTools.eventCount(AddNewCardHolderActivity.this, "hospital");
                //判断认证 stone
                DialogUtil.showUserCenterCertifyDialog(AddNewCardHolderActivity.this, new MyCallBack() {
                    @Override
                    public void onClick(Object data) {
                        Intent intent = new Intent(AddNewCardHolderActivity.this, SerchNewCardHolderActivity.class);
                        intent.putExtra("type", "hospital");
                        startActivity(intent);
                    }
                }, null, null);

                break;
            case R.id.re_area:
                StatisticalTools.eventCount(AddNewCardHolderActivity.this, "local");
                //判断认证 stone
                DialogUtil.showUserCenterCertifyDialog(AddNewCardHolderActivity.this, new MyCallBack() {
                    @Override
                    public void onClick(Object data) {
                        Intent intent = new Intent(AddNewCardHolderActivity.this, SerchNewCardHolderActivity.class);
                        intent.putExtra("type", "area");
                        startActivity(intent);
                    }
                }, null, null);

                break;
            case R.id.re_phone:
                StatisticalTools.eventCount(AddNewCardHolderActivity.this, "addresslist");
                //判断认证 stone
                DialogUtil.showUserCenterCertifyDialog(AddNewCardHolderActivity.this, new MyCallBack() {
                    @Override
                    public void onClick(Object data) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (ContextCompat.checkSelfPermission(AddNewCardHolderActivity.this, Manifest.permission.READ_CONTACTS)
                                    != PackageManager.PERMISSION_GRANTED) {
                                CommonUtils.permissionRequestDialog(AddNewCardHolderActivity.this, "无法获取手机通讯录，请授予联系人(Contacts)权限",
                                        333);
                            } else {
                                Intent intent = new Intent(AddNewCardHolderActivity.this, InviteNewFriendActivity.class);
                                intent.putExtra("type", "add");
                                startActivity(intent);
                            }
                        } else {
                            Intent intent = new Intent(AddNewCardHolderActivity.this, InviteNewFriendActivity.class);
                            intent.putExtra("type", "add");
                            startActivity(intent);
                        }
                    }
                }, null, null);


                break;
            case R.id.add_media:
                StatisticalTools.eventCount(this, "MediaNumber");
                intent = new Intent(AddNewCardHolderActivity.this, MediaServiceListActivity.class);
                intent.putExtra(MediaServiceListActivity.TYPE_INTENT_KEY, Constants.TYPE_MEDIA);
                startActivity(intent);
                break;
            case R.id.add_service:
                StatisticalTools.eventCount(this, "ServiceNumber");
                intent = new Intent(AddNewCardHolderActivity.this, MediaServiceListActivity.class);
                intent.putExtra(MediaServiceListActivity.TYPE_INTENT_KEY, Constants.TYPE_SERVICE);
                startActivity(intent);
                break;
        }
    }
}
