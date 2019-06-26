package com.xywy.askforexpert.module.message.friend;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.DialogUtil;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.YMOtherUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.logictools.ResolveJson;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.zxing.activity.CaptureActivity;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.Map;

/**
 * 名片夹 功能菜单
 *
 * @author 王鹏
 * @2015-5-14下午2:53:31
 */
public class Menu_CardHoder_Right_Fragment extends Fragment {
    private static final String TAG = "Menu_CardHoder_Right_Fragment";
    Map<String, String> map;
    private LinearLayout lin_add_friend, lin_invite_friend, lin_sweep, mycard_ll;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_right_cardhoder, container,
                false);
        lin_add_friend = (LinearLayout) v.findViewById(R.id.lin_add_friend);
        lin_invite_friend = (LinearLayout) v
                .findViewById(R.id.lin_invite_friend);

        lin_sweep = (LinearLayout) v.findViewById(R.id.lin_sweep);

        mycard_ll = (LinearLayout) v.findViewById(R.id.mycard_ll);
        lin_sweep.setOnClickListener(new MyOnclick());
        lin_add_friend.setOnClickListener(new MyOnclick());
        lin_invite_friend.setOnClickListener(new MyOnclick());
        mycard_ll.setOnClickListener(new MyOnclick());
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            sendData(scanResult);
            // T.shortToast(scanResult );
        }
    }

    public void sendData(final String str) {
        String did = YMApplication.getLoginInfo().getData().getPid();
        String bind = did + str;
        Long st = System.currentTimeMillis();

        String sign = MD5Util.MD5(st + bind + Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();

        params.put("timestamp", st + "");
        params.put("bind", bind);
        params.put("a", "chat");
        params.put("m", "qrcodeScan");
        params.put("url", str);
        params.put("did", did);
        params.put("sign", sign);

        FinalHttp fh = new FinalHttp();
        fh.get(CommonUrl.Patient_Manager_Url, params,
                new AjaxCallBack() {
                    @Override
                    public void onSuccess(String t) {
                        // TODO Auto-generated method stub
                        DLog.i(TAG, "扫面返回的结果" + t.toString());
                        Gson gson = new Gson();
                        map = ResolveJson.R_Action_twos(t.toString());
                        if (map.get("code").equals("0")) {
                            if (map.get("isxywy").equals("1")) {
                                Intent intenAdd = new Intent(getActivity(),
                                        AddCardHoldVerifyActiviy.class);
                                intenAdd.putExtra("toAddUsername",
                                        "did_" + map.get("did"));
                                startActivity(intenAdd);
                            } else {
                                if (str.contains("http")) {
                                    Intent intent = new Intent();
                                    intent.setAction("android.intent.action.VIEW");
                                    Uri content_url = Uri.parse(str);
                                    intent.setData(content_url);
                                    startActivity(intent);
                                } else {
                                    Dialog(getActivity(), str);
                                }

                            }
                        } else {
                            ToastUtils.shortToast( map.get("msg"));
                            if (str.contains("http")) {
                                Intent intent = new Intent();
                                intent.setAction("android.intent.action.VIEW");
                                Uri content_url = Uri.parse(str);
                                intent.setData(content_url);
                                startActivity(intent);
                            } else {
                                Dialog(getActivity(), str);
                            }
                        }
                        super.onSuccess(t);
                    }
                });

    }

    public void Dialog(Context context, String str) {
        LinearLayout layout = (LinearLayout) LayoutInflater.from(context)
                .inflate(R.layout.myclic_dialog, null);
        final Dialog dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        TextView content = (TextView) layout.findViewById(R.id.tv_content);
        content.setText(str);
        RelativeLayout re_ok = (RelativeLayout) layout.findViewById(R.id.rl_ok);
        re_ok.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();

            }
        });

    }

    public void onResume() {
        super.onResume();
        StatisticalTools.fragmentOnResume("Menu_CardHoder_Right_Fragment");
    }

    public void onPause() {
        super.onPause();
        StatisticalTools.fragmentOnPause("Menu_CardHoder_Right_Fragment");
    }

    class MyOnclick implements OnClickListener

    {

        @Override
        public void onClick(View arg0) {
            Intent intent;
            switch (arg0.getId()) {
                case R.id.lin_sweep:
                    StatisticalTools.eventCount(getActivity(), "RichScan");
                    if (YMUserService.isGuest()) {
                        DialogUtil.LoginDialog(new YMOtherUtils(getActivity()).context);
                    } else {
                        Intent openCameraIntent = new Intent(getActivity(),
                                CaptureActivity.class);
                        startActivityForResult(openCameraIntent, 0);
                    }

                    break;
                case R.id.lin_add_friend:
                    StatisticalTools.eventCount(getActivity(), "addfriends");
                    if (YMUserService.isGuest()) {
                        DialogUtil.LoginDialog(new YMOtherUtils(getActivity()).context);
                    } else {
                        intent = new Intent(getActivity(),
                                AddNewCardHolderActivity.class);
                        startActivity(intent);
                    }
                    break;
                case R.id.lin_invite_friend:
                    StatisticalTools.eventCount(getActivity(), "Invite");
                    if (YMUserService.isGuest()) {
                        DialogUtil.LoginDialog(new YMOtherUtils(getActivity()).context);
                    } else {
                        intent = new Intent(getActivity(),
                                InviteNewFriendMainActivity.class);
                        startActivity(intent);
                    }
                    break;

                case R.id.mycard_ll:
                    if (YMUserService.isGuest()) {
                        DialogUtil.LoginDialog(new YMOtherUtils(getActivity()).context);
                    } else {
                        intent = new Intent(getActivity(),
                                MyIdCardActivity.class);
                        startActivity(intent);
                    }
                    break;
                default:
                    break;
            }

        }

    }

}
