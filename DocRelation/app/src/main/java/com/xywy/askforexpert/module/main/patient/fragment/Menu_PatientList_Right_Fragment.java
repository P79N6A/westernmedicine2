package com.xywy.askforexpert.module.main.patient.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.utils.DialogUtil;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.YMOtherUtils;
import com.xywy.askforexpert.module.main.diagnose.Patient_Group_ManagerActivity;
import com.xywy.askforexpert.module.main.patient.activity.AddNewPatientActivity;

/**
 * 患者管理 功能菜单
 *
 * @author 王鹏
 * @2015-5-21上午10:14:31
 */
public class Menu_PatientList_Right_Fragment extends Fragment {

    /**
     * 患者管理
     */
    private LinearLayout lin_mangere;
    private LinearLayout lin_add_new_ptient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_right_patient, container,
                false);
        lin_mangere = (LinearLayout) v.findViewById(R.id.lin_mangere);
        lin_mangere.setOnClickListener(new MyOnclick());
        lin_add_new_ptient = (LinearLayout) v
                .findViewById(R.id.lin_add_new_ptient);
        lin_add_new_ptient.setOnClickListener(new MyOnclick());
        return v;
    }

    public void onResume() {
        super.onResume();
        StatisticalTools.fragmentOnResume("Menu_PatientList_Right_Fragment");
    }

    public void onPause() {
        super.onPause();
        StatisticalTools.fragmentOnPause("Menu_PatientList_Right_Fragment");
    }

    class MyOnclick implements OnClickListener

    {

        @Override
        public void onClick(View arg0) {
            Intent intent;
            switch (arg0.getId()) {
                case R.id.lin_mangere:
                    if (YMUserService.isGuest()) {
                        DialogUtil.LoginDialog(new YMOtherUtils(getActivity()).context);
                    } else {
                        intent = new Intent(getActivity(),
                                Patient_Group_ManagerActivity.class);
                        intent.putExtra("type", "gruoup");
                        startActivity(intent);
                    }
                    break;
                case R.id.lin_add_new_ptient:
                    StatisticalTools.eventCount(getActivity(), "Addpatient");
                    if (YMUserService.isGuest()) {
                        DialogUtil.LoginDialog(new YMOtherUtils(getActivity()).context);
                    } else {
                        intent = new Intent(getActivity(), AddNewPatientActivity.class);
                        startActivity(intent);
                    }
                    break;

                default:
                    break;
            }

        }

    }

}
