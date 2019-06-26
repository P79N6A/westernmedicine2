package com.xywy.askforexpert.module.main.service;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.appcommon.utils.others.DLog;

import java.util.List;
import java.util.Map;

/**
 * 首页 我的服务 第一页
 *
 * @author Jack Fang
 */
public class MyServicesFirstFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_KEY = "MyServicesFirstFragment";

    private RelativeLayout questions;
    private RelativeLayout docsSearch;
    private RelativeLayout patientControl;
    private RelativeLayout guide;
    private RelativeLayout firstPageLastItem;
    private TextView lastItemText;
    private TextView questionsLayout;
    private boolean isPush;
    private int typeId;
    private SharedPreferences sp_save_user;
    private String userid;

    /**
     * 定制的服务id
     */
    private int ids;
    private View view;
    private TextView docSearch;
    private TextView patientManage;
    private TextView guideLayout;
    private List<Integer> serviceIds;

    public MyServicesFirstFragment() {
        // Required empty public constructor
    }

    public void setIds(int ids) {
        this.ids = ids;
    }

    public void setServiceIds(List<Integer> serviceIds) {
        this.serviceIds = serviceIds;
    }

    public void setIsPush(boolean isPush) {
        this.isPush = isPush;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false)) {
            return;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_services_first, container, false);

        sp_save_user = getActivity().getSharedPreferences("save_user", Context.MODE_PRIVATE);
        if (YMUserService.isGuest()) {
            userid = "";
        } else {
            userid = YMApplication.getLoginInfo().getData().getPid();
            // getSettingData();
//            getNewsData();
        }

        initView(view);
        setItems();
        setLastItem();
        registerListener();

        if (isPush) {
            if (typeId == 3) {
                CommonUtils.getBackNum(getActivity());
            } else {
                CommonUtils.gotoQuestions(getActivity(), isPush, typeId);
            }
        }

        return view;
    }

    private void registerListener() {
        questions.setOnClickListener(this);
        docsSearch.setOnClickListener(this);
        patientControl.setOnClickListener(this);
        guide.setOnClickListener(this);
        firstPageLastItem.setOnClickListener(this);
    }

    /**
     * 设置最后一个item
     */
    public void setLastItem() {
        Map<String, Object> serviceInfo = CommonUtils.getServiceInfo(ids);
        if (serviceInfo != null) {
            DLog.d(ARG_KEY, "bg = " + serviceInfo.get("bgColor"));
            DLog.d(ARG_KEY, "view == null ? " + (view == null));
            if (view != null) {
                firstPageLastItem.setBackgroundColor(Color.parseColor((String) serviceInfo.get("bgColor")));
                lastItemText.setText((String) serviceInfo.get("name"));
                if (isAdded()) {
                    lastItemText.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable((Integer) serviceInfo.get("icon")),
                            null, null, null);
                }
            }
        }
    }

    public void setItems() {
        if (serviceIds != null && !serviceIds.isEmpty() && serviceIds.get(0) == 11) {
            serviceIds.remove(Integer.valueOf(11));
            serviceIds.add(0, 998);
        }
        List<Map<String, Object>> serviceInfos = CommonUtils.getServiceInfos(serviceIds);
        if (serviceIds != null && !serviceIds.isEmpty() && serviceIds.get(0) == 998) {
            serviceIds.remove(Integer.valueOf(998));
            serviceIds.add(0, 11);
        }
        if (serviceInfos != null && !serviceInfos.isEmpty() && serviceInfos.size() == 4) {
            // 第一个item
            questions.setBackgroundColor(Color.parseColor((String) serviceInfos.get(0).get("bgColor")));
            questionsLayout.setText((String) serviceInfos.get(0).get("name"));
            if (isAdded()) {
                questionsLayout.setCompoundDrawablesWithIntrinsicBounds(null,
                        getResources().getDrawable((Integer) serviceInfos.get(0).get("icon")),
                        null, null);
            }

            // 第二个item
            docsSearch.setBackgroundColor(Color.parseColor((String) serviceInfos.get(1).get("bgColor")));
            docSearch.setText((String) serviceInfos.get(1).get("name"));
            if (isAdded()) {
                docSearch.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable((Integer) serviceInfos.get(1).get("icon")),
                        null, null, null);
            }

            // 第三个item
            patientControl.setBackgroundColor(Color.parseColor((String) serviceInfos.get(2).get("bgColor")));
            patientManage.setText((String) serviceInfos.get(2).get("name"));
            if (isAdded()) {
                patientManage.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable((Integer) serviceInfos.get(2).get("icon")),
                        null, null, null);
            }

            // 第四个item
            guide.setBackgroundColor(Color.parseColor((String) serviceInfos.get(3).get("bgColor")));
            guideLayout.setText((String) serviceInfos.get(3).get("name"));
            if (isAdded()) {
                guideLayout.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable((Integer) serviceInfos.get(3).get("icon")),
                        null, null, null);
            }
        }
    }

    private void initView(View view) {
        if (view != null) {
            questions = (RelativeLayout) view.findViewById(R.id.questions);
            questionsLayout = (TextView) view.findViewById(R.id.question_text);
            docsSearch = (RelativeLayout) view.findViewById(R.id.docs_search);
            docSearch = (TextView) view.findViewById(R.id.docs_search_text);
            patientControl = (RelativeLayout) view.findViewById(R.id.patient_control);
            patientManage = (TextView) view.findViewById(R.id.patient_control_text);
            guide = (RelativeLayout) view.findViewById(R.id.guide);
            guideLayout = (TextView) view.findViewById(R.id.guide_ll);

            firstPageLastItem = (RelativeLayout) view.findViewById(R.id.first_page_last_item);
            lastItemText = (TextView) view.findViewById(R.id.first_page_last_item_text);
        }
    }

    @Override
    public void onClick(View v) {
        try {
            Integer id;
            switch (v.getId()) {
                // item 1
                case R.id.questions:
                    id = serviceIds.get(0);
                    if (id == 11) { // 签约居民(大)
                        StatisticalTools.eventCount(getActivity(), "BigSignResident");
                    }
                    CommonUtils.gotoService(getActivity(), id);
                    break;

                // item 2
                case R.id.docs_search:
                    id = serviceIds.get(1);
                    if (id == 11) { // 签约居民(小)
                        StatisticalTools.eventCount(getActivity(), "SmallSignResident");
                    }
                    CommonUtils.gotoService(getActivity(), id);
                    break;

                // item 3
                case R.id.patient_control:
                    id = serviceIds.get(2);
                    CommonUtils.gotoService(getActivity(), id);
                    break;

                // item 4
                case R.id.guide:
                    id = serviceIds.get(3);
                    CommonUtils.gotoService(getActivity(), id);
                    break;

                // 最后一个item
                case R.id.first_page_last_item:
                    CommonUtils.gotoService(getActivity(), ids);
                    break;

                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
