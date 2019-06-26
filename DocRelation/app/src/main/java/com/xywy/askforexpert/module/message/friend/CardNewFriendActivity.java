package com.xywy.askforexpert.module.message.friend;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.model.NewCardInfo;
import com.xywy.askforexpert.module.message.friend.adapter.BaseNewFriendAdapter;
import com.xywy.base.view.ProgressDialog;
import com.xywy.easeWrapper.db.InviteMessage;
import com.xywy.easeWrapper.db.InviteMessgeDao;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.List;

/**
 * 新朋友 名片夹
 *
 * @author 王鹏
 * @2015-5-30下午4:33:26
 */
public class CardNewFriendActivity extends Activity {
    ListView listView;
    InviteMessgeDao inviteMessgeDao;
    List<InviteMessage> msgs;
    NewCardInfo newinfo;
    private BaseNewFriendAdapter adapter;
    private LinearLayout no_data;
    private TextView tv_nodata_title;
    private ImageView img_nodata;
    private String uid = "";
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 100:
                    if (newinfo == null) {
                        no_data.setVisibility(View.VISIBLE);

                        return;
                    }
                    if (newinfo.getCode().equals("0") & newinfo.getData().size() > 0) {
                        no_data.setVisibility(View.GONE);
                        YMApplication.addFriendNum = msgs.size() + "";
                        adapter.setData(msgs, newinfo.getData());
                        listView.setAdapter(adapter);
                        no_data.setVisibility(View.GONE);
                    } else {
                        no_data.setVisibility(View.VISIBLE);
                    }
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.patient_server_gone);

        CommonUtils.initSystemBar(this);
        no_data = (LinearLayout) findViewById(R.id.lin_nodata);
        tv_nodata_title = (TextView) findViewById(R.id.tv_nodata_title);
        tv_nodata_title.setText("暂无新朋友");
        img_nodata = (ImageView) findViewById(R.id.img_nodate);
        img_nodata.setBackgroundResource(R.drawable.nofriend);
        listView = (ListView) findViewById(R.id.list_server_gone);
        View title_layout = findViewById(R.id.title_layout);
        title_layout.setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.tv_title)).setText("新朋友");
        if (!NetworkUtil.isNetWorkConnected()) {
            no_data.setVisibility(View.VISIBLE);
            tv_nodata_title.setText("网络连接失败");
        }
        inviteMessgeDao = new InviteMessgeDao();

        adapter = new BaseNewFriendAdapter(CardNewFriendActivity.this);

        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Intent intent = new Intent(CardNewFriendActivity.this,
                        NewCardInfoActivity.class);
                intent.putExtra("head_img", newinfo.getData().get(arg2)
                        .getPhoto());
                String sex;
                if ("0".equals(newinfo.getData().get(arg2).getSex())) {
                    sex = "女";
                } else {
                    sex = "男";
                }

                intent.putExtra("sex", sex);
                intent.putExtra("subject", newinfo.getData().get(arg2)
                        .getSubject());
                intent.putExtra("reason", msgs.get(arg2).getReason());
                intent.putExtra("hx_usernam", msgs.get(arg2).getFrom());
                intent.putExtra("realname", newinfo.getData().get(arg2)
                        .getNickname());
                intent.putExtra("hospital", newinfo.getData().get(arg2).getHospital());

                intent.putExtra("job", newinfo.getData().get(arg2).getJob());

                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        if (!YMUserService.isGuest()) {
            if (YMApplication.getLoginInfo() != null) {
                uid = YMApplication.getLoginInfo().getData().getPid();
            }

        }
        msgs = inviteMessgeDao.getMessagesList();
        if (msgs != null) {
            if (msgs.size() > 0) {
                if (!getStr().equals("")) {

                    getData(getStr());
                }
            } else {
                no_data.setVisibility(View.VISIBLE);
                if (newinfo != null) {
                    newinfo.getData().clear();
                    adapter.setData(msgs, newinfo.getData());
                    adapter.notifyDataSetChanged();
                    if (newinfo.getData().size() == 0) {
                        no_data.setVisibility(View.VISIBLE);
                    }
                }

            }
        } else

        {
            no_data.setVisibility(View.VISIBLE);
        }
        StatisticalTools.onResume(CardNewFriendActivity.this);

    }

    public String getStr() {
        String strs = "";
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < msgs.size(); i++) {
            String toAddUsername = msgs.get(i).getFrom();
            String str = toAddUsername.substring(
                    toAddUsername.indexOf("_") + 1, toAddUsername.length());
            sb = sb.append(str + ",");
        }
        if (sb.length() > 0) {
            strs = sb.substring(0, sb.lastIndexOf(","));

        }
        return strs;
    }

    public void getData(String userid) {
        // String userid = YMApplication.getLoginInfo().getData().getPid();
        final ProgressDialog dialog = new ProgressDialog(this, "正在加载中...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.showProgersssDialog();
        String bind = userid;
        String sign = MD5Util.MD5(bind + Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();
        params.put("a", "doctor");
        params.put("m", "doctor_list");
        params.put("userid", userid);
        params.put("bind", bind);
        params.put("sign", sign);

        FinalHttp fh = new FinalHttp();
        fh.post(CommonUrl.Patient_Manager_Url, params,
                new AjaxCallBack() {
                    @Override
                    public void onSuccess(String t) {
                        dialog.dismiss();
                        Gson gson = new Gson();
                        newinfo = gson.fromJson(t.toString(), NewCardInfo.class);

                        // T.showNoRepeatShort(AddCardHoldVerifyActiviy.this,
                        // map.get("msg"));

                        handler.sendEmptyMessage(100);


                        super.onSuccess(t);
                    }

                    @Override
                    public void onFailure(Throwable t, int errorNo,
                                          String strMsg) {
                        dialog.dismiss();
                        no_data.setVisibility(View.VISIBLE);
                        super.onFailure(t, errorNo, strMsg);
                    }
                });

    }

    public void onClick_back(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                finish();
                break;

            default:
                break;
        }
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub

        StatisticalTools.onPause(CardNewFriendActivity.this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {

        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
