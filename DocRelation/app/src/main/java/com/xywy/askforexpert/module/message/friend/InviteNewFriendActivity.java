package com.xywy.askforexpert.module.message.friend;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.model.InviteNewFriendInfo;
import com.xywy.askforexpert.module.message.friend.adapter.BaseInviteNewFriendAdapter;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.ArrayList;
import java.util.List;

/**
 * 手机通讯录好友 邀请好友 stone
 *
 * @author 王鹏
 * @2015-6-11下午5:15:02
 */
public class InviteNewFriendActivity extends YMBaseActivity {
    private static final String[] PHONES_PROJECTION = new String[]{
            Phone.DISPLAY_NAME, Phone.NUMBER, Photo.PHOTO_ID, Phone.CONTACT_ID};
    private static final int PHONES_DISPLAY_NAME_INDEX = 0;
    private static final int PHONES_NUMBER_INDEX = 1;
    private static final int PHONES_PHOTO_ID_INDEX = 2;
    private static final int PHONES_CONTACT_ID_INDEX = 3;
    InviteNewFriendInfo inviteinfo;
    BaseInviteNewFriendAdapter adapter;
    String type;
    private List<String> mContactsName = new ArrayList<String>();
    private List<String> mContactsNumber = new ArrayList<String>();
    private List<Bitmap> mContactsPhonto = new ArrayList<Bitmap>();
    private ListView listView;
    private ImageView iv_no;
    private TextView tv_nodata_title;
    private LinearLayout lin_nodata;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 100:
                    if (inviteinfo.getCode().equals("0")) {
                        adapter = new BaseInviteNewFriendAdapter(InviteNewFriendActivity.this);
                        adapter.setData(inviteinfo.getData());
                        listView.setAdapter(adapter);
                        if (inviteinfo.getData().size() == 0) {
                            lin_nodata.setVisibility(View.VISIBLE);
                            iv_no.setBackgroundResource(R.drawable.nofriend);
                            tv_nodata_title.setText("暂无通讯录");
                        } else {
                            lin_nodata.setVisibility(View.GONE);
                            iv_no.setVisibility(View.GONE);
                        }

                    } else {
                        lin_nodata.setVisibility(View.VISIBLE);
                        iv_no.setBackgroundResource(R.drawable.nofriend);
                        tv_nodata_title.setText("暂无通讯录");
                    }
                    break;

                default:
                    break;
            }
        }
    };


//    public void onClick_back(View view) {
//        switch (view.getId()) {
//            case R.id.btn1:
//                finish();
//                break;
//
//            default:
//                break;
//        }
//    }


    protected void onResume() {
        super.onResume();
        if (mContactsName != null) {
            if (mContactsName.size() > 0) {
                sendContentlist();
                //iv_no.setVisibility(View.INVISIBLE);
            } else {
                tv_nodata_title.setText("暂无通讯录");
                lin_nodata.setVisibility(View.VISIBLE);
                iv_no.setBackgroundResource(R.drawable.nofriend);
            }

        }

    }

    @Override
    protected void initView() {
        type = getIntent().getStringExtra("type");
        titleBarBuilder.setTitleText("手机通讯录");
        listView = (ListView) findViewById(R.id.list_server_gone);
        iv_no = (ImageView) findViewById(R.id.img_nodate);
        tv_nodata_title = (TextView) findViewById(R.id.tv_nodata_title);
        lin_nodata = (LinearLayout) findViewById(R.id.lin_nodata);
        PackageManager pkm = getPackageManager();
        boolean has_permission = (PackageManager.PERMISSION_GRANTED == pkm.checkPermission("android.permission.READ_CONTACTS", "com.xywy.askforexpert"));

        if (has_permission) {
            getSIMContacts();
            getPhoneContacts();

        } else {
            lin_nodata.setVisibility(View.VISIBLE);
            tv_nodata_title.setText("未授权");
            iv_no.setBackgroundResource(R.drawable.ico_suo);
            iv_no.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.SETTINGS");
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    protected void initData() {

    }

    public void sendContentlist() {
        if (!NetworkUtil.isNetWorkConnected()) {
            tv_nodata_title.setText("网络连接失败");
            lin_nodata.setVisibility(View.VISIBLE);
            iv_no.setBackgroundResource(R.drawable.nofriend);
            return;
        }
        String did = YMApplication.getLoginInfo().getData().getPid();
        String bind = did;
        Long st = System.currentTimeMillis();

        String sign = MD5Util.MD5(st + bind + Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();

        params.put("timestamp", st + "");
        params.put("bind", bind);
        params.put("a", "chat");
        params.put("m", "contactStatus");
        params.put("type", type);
        params.put("contacts", getStr_Content());
        params.put("did", did);
        params.put("sign", sign);

        FinalHttp fh = new FinalHttp();
        fh.post(CommonUrl.Patient_Manager_Url, params,
                new AjaxCallBack() {

                    @Override
                    public void onFailure(Throwable t, int errorNo,
                                          String strMsg) {
                        // TODO Auto-generated method stub
                        super.onFailure(t, errorNo, strMsg);
                        tv_nodata_title.setText("获取数据失败");
                        lin_nodata.setVisibility(View.VISIBLE);
                        iv_no.setBackgroundResource(R.drawable.nofriend);
                    }

                    @Override
                    public void onSuccess(String t) {
                        // TODO Auto-generated method stub
                        Gson gson = new Gson();
                        inviteinfo = gson.fromJson(t.toString(), InviteNewFriendInfo.class);
                        handler.sendEmptyMessage(100);

                        super.onSuccess(t);
                    }
                });
    }

    public String getStr_Content() {
        String str = "";
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < mContactsName.size(); i++) {
            sb = sb.append(mContactsName.get(i).toString().trim() + "\t"
                    + mContactsNumber.get(i).toString().trim() + "\n");
        }
        if (sb.toString().length() > 0) {
            str = sb.substring(0, sb.lastIndexOf("\n"));
        }
        return str;
    }

    /**
     * 得到手机通讯录联系人信息
     **/
    private void getPhoneContacts() {
        ContentResolver resolver = InviteNewFriendActivity.this
                .getContentResolver();

        // 获取手机联系人
        Cursor phoneCursor = resolver.query(Phone.CONTENT_URI,
                PHONES_PROJECTION, null, null, null);

        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {

                // 得到手机号码
                String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
                // 当手机号码为空的或者为空字段 跳过当前循环
                if (TextUtils.isEmpty(phoneNumber)) {
                    continue;
                }

                // 得到联系人名称
                String contactName = phoneCursor
                        .getString(PHONES_DISPLAY_NAME_INDEX);

                // 得到联系人ID
                Long contactid = phoneCursor.getLong(PHONES_CONTACT_ID_INDEX);

                // 得到联系人头像ID
                // Long photoid = phoneCursor.getLong(PHONES_PHOTO_ID_INDEX);

                // 得到联系人头像Bitamp
                // Bitmap contactPhoto = null;
                //
                // // photoid 大于0 表示联系人有头像 如果没有给此人设置头像则给他一个默认的
                // if (photoid > 0) {
                // Uri uri = ContentUris.withAppendedId(
                // ContactsContract.Contacts.CONTENT_URI, contactid);
                // InputStream input = ContactsContract.Contacts
                // .openContactPhotoInputStream(resolver, uri);
                // contactPhoto = BitmapFactory.decodeStream(input);
                // } else {
                // contactPhoto = BitmapFactory.decodeResource(getResources(),
                // R.drawable.contact_photo);
                // }

                mContactsName.add(contactName);
                phoneNumber = phoneNumber.replaceAll(" ", "");
                phoneNumber = phoneNumber.replaceAll("-", "");
                mContactsNumber.add(phoneNumber);
                // mContactsPhonto.add(contactPhoto);
            }

            phoneCursor.close();
        }
    }

    /**
     * 得到手机SIM卡联系人人信息
     **/
    private void getSIMContacts() {
        ContentResolver resolver = InviteNewFriendActivity.this
                .getContentResolver();
        // 获取Sims卡联系人
        Uri uri = Uri.parse("content://icc/adn");
        Cursor phoneCursor = resolver.query(uri, PHONES_PROJECTION, null, null,
                null);

        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {

                // 得到手机号码
                String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
                // 当手机号码为空的或者为空字段 跳过当前循环
                if (TextUtils.isEmpty(phoneNumber)) {
                    continue;
                }
                // 得到联系人名称
                String contactName = phoneCursor
                        .getString(PHONES_DISPLAY_NAME_INDEX);

                // Sim卡中没有联系人头像

                mContactsName.add(contactName);
                phoneNumber = phoneNumber.replaceAll(" ", "");
                phoneNumber = phoneNumber.replaceAll("-", "");
                mContactsNumber.add(phoneNumber);
            }
            phoneCursor.close();
        } else {
            ToastUtils.shortToast("SIM");
        }
    }

    @Override
    protected void onDestroy() {

        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.patient_server_gone;
    }
}
