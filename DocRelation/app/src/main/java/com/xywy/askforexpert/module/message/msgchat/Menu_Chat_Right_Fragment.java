package com.xywy.askforexpert.module.message.msgchat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.logictools.ResolveJson;
import com.xywy.askforexpert.module.main.diagnose.Patient_Group_ManagerActivity;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.Map;

/**
 * 聊天
 *
 * @author 王鹏
 * @2015-6-3下午2:35:58
 */
public class Menu_Chat_Right_Fragment extends Fragment {

    /**
     * 患者管理
     */
    private LinearLayout lin_mangere;
    private LinearLayout lin_add_new_ptient;
    private String type;
    private String uid;
    private String lastgid;
    private String hx_uid;
    private SharedPreferences sp;
    private boolean isFistin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        sp = getActivity().getSharedPreferences("save_gid", Context.MODE_PRIVATE);
        isFistin = sp.getBoolean(YMApplication.getLoginInfo().getData().getPid(), true);
        if (isFistin) {
            setGroupList(false);
        }
        Bundle bundle = getArguments();
        type = bundle.getString("type");
        if (type.equals("uid")) {

            lastgid = bundle.getString("lastgid");
            hx_uid = bundle.getString("userId");
            uid = hx_uid.substring(
                    hx_uid.indexOf("_") + 1, hx_uid.length());
        }

    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        StatisticalTools.fragmentOnResume("Menu_Chat_Right_Fragment");
        if (!TextUtils.isEmpty(hx_uid)) {
            lastgid = sp.getString(hx_uid, "0");
        }
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_right_chat, container,
                false);
        lin_mangere = (LinearLayout) v.findViewById(R.id.lin_mangere);
        lin_mangere.setOnClickListener(new MyOnclick());
        lin_add_new_ptient = (LinearLayout) v
                .findViewById(R.id.lin_change_subject);
        lin_add_new_ptient.setOnClickListener(new MyOnclick());
        if (type.equals("uid")) {
            lin_mangere.setVisibility(View.VISIBLE);
        } else if (type.equals("qid")) {
//			lin_add_new_ptient.setVisibility(View.VISIBLE);
        }
        return v;
    }

    class MyOnclick implements OnClickListener

    {

        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()) {
                case R.id.lin_mangere:
                    isFistin = sp.getBoolean(YMApplication.getLoginInfo().getData().getPid(), true);
                    if (isFistin) {
                        setGroupList(true);
                    } else {
                        intent = new Intent(getActivity(),
                                Patient_Group_ManagerActivity.class);
                        intent.putExtra("type", "set_group");
                        intent.putExtra("uid", uid);
                        intent.putExtra("hx_userid", hx_uid);
                        intent.putExtra("lastgid", lastgid);
                        startActivity(intent);
                    }
                    break;
                case R.id.lin_change_subject:
                    ToastUtils.shortToast("纠正科室");

                    break;

                default:
                    break;
            }

        }
    }

    public void onPause() {
        super.onPause();
        StatisticalTools.fragmentOnPause("Menu_Chat_Right_Fragment");
    }

    public void setGroupList(final boolean intent_2_patient_group_manageractivity) {
        String did = YMApplication.getLoginInfo().getData().getPid();
        String bind = did;
        Long st = System.currentTimeMillis();
        String sign = MD5Util.MD5(st + bind + Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();
        params.put("a", "chat");
        params.put("m", "groupInit");
        params.put("timestamp", st + "");
        params.put("bind", did);
        params.put("did", did);
        params.put("sign", sign);

        FinalHttp fh = new FinalHttp();
        fh.get(CommonUrl.Patient_Manager_Url, params,
                new AjaxCallBack() {
                    @Override
                    public void onSuccess(String t) {
                        Map<String, String> map = ResolveJson.R_Action(t.toString());
                        if (map.get("code").equals("0")) {
                            sp.edit()
                                    .putBoolean(
                                            YMApplication.getLoginInfo().getData()
                                                    .getPid(), false).commit();
                            if(intent_2_patient_group_manageractivity){
                                Intent intent = new Intent(getActivity(),
                                        Patient_Group_ManagerActivity.class);
                                intent.putExtra("type", "set_group");
                                intent.putExtra("uid", uid);
                                intent.putExtra("hx_userid", hx_uid);
                                intent.putExtra("lastgid", lastgid);
                                startActivity(intent);
                            }

                        }
                        super.onSuccess(t);
                    }

                });
    }

}
