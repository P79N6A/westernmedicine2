package com.xywy.askforexpert.module.main.service;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.module.main.service.service.MyServicesGridAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页 我的服务 第二页
 *
 * @author Jack Fang
 */
public class MyServicesSecondFragment extends Fragment {
    private static final String ARG_KEY = "MyServicesSecondFragment";

    /**
     * 服务id
     */
    private List<Integer> ids;

    public MyServicesSecondFragment() {
        // Required empty public constructor
    }

    public static MyServicesSecondFragment newInstance(List<Integer> ids) {
        MyServicesSecondFragment fragment = new MyServicesSecondFragment();
        Bundle args = new Bundle();
        args.putIntegerArrayList(ARG_KEY, (ArrayList<Integer>) ids);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayList<Integer> list = this.getArguments().getIntegerArrayList(ARG_KEY);
        if (list != null && list.size() > 0) {
            ids = list;
        } else {
            ids = new ArrayList<>();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_services_sencond, container, false);

        DLog.d(ARG_KEY, "second ids size = " + ids.size());

        GridView serviceGrid = (GridView) view.findViewById(R.id.myServices_grid);
        MyServicesGridAdapter adapter = new MyServicesGridAdapter(getActivity(), ids, R.layout.my_services_grid_layout);
        serviceGrid.setAdapter(adapter);

        serviceGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CommonUtils.gotoService(getActivity(), ids.get(position));
            }
        });

        return view;
    }

}
