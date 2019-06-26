package com.xywy.askforexpert.module.my.download;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.artifex.mupdfdemo.MuPDFActivity;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.utils.ScreenUtils;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.DownFileItemInfo;
import com.xywy.askforexpert.module.my.download.DownloadListAdapter.OnItemDeleteClickListener;

import net.tsz.afinal.FinalDb;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 下载完成
 *
 * @author 王鹏
 * @2015-6-9上午11:42:03
 */
public class DownListFragment extends Fragment {
    private static final String TAG = "DownListFragment";
    private ListView listView;
    private ImageView btn1;
    private TextView btn2;
    private Class<DownFileItemInfo> clazz;
    private DownloadListAdapter adapter;
    private FinalDb fb;
    private boolean isDelete = false;
    private TextView tv_cancell;
    private String type_commed;
    private List<DownFileItemInfo> items = new ArrayList<DownFileItemInfo>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.download_list, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        Bundle budle = getArguments();
        type_commed = budle.getString("type_commed");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        ScreenUtils.initScreen(getActivity());
        listView = (ListView) getActivity().findViewById(R.id.list_view);
        tv_cancell = (TextView) getActivity().findViewById(R.id.tv_cancell);
        tv_cancell.setOnClickListener(new onClick_back());
        btn1 = (ImageView) getActivity().findViewById(R.id.btn_1);
        btn2 = (TextView) getActivity().findViewById(R.id.btn_2);
        btn1.setOnClickListener(new onClick_back());
        btn2.setOnClickListener(new onClick_back());

        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (isDelete) {
                    boolean ischeck = adapter.spar_map.get(arg2);
                    adapter.spar_map.put(arg2, !ischeck);
                    adapter.notifyDataSetChanged();
                } else {
                    try {
                        Uri uri = Uri.parse(items.get(arg2).getFilePath()
                                .toString().trim());
                        Intent intent = new Intent(getActivity(),
                                MuPDFActivity.class);
                        intent.setAction(Intent.ACTION_VIEW);
                        // intent.putExtra("path",
                        // items.get(arg2).getFilePath().toString().trim());
                        intent.setData(uri);
                        startActivity(intent);
                    } catch (Exception e) {
                        DLog.i(TAG, "PDF打开失败原因 " + e);
                        ToastUtils.shortToast( "pdf打开失败");
                    }

                    // T.shortToast( "pdf阅读 期待");
                }

                // startActivity(intent);

            }

        });

    }

    private OnItemDeleteClickListener listener = new OnItemDeleteClickListener() {

        @Override
        public void onRightClick(View v, int position) {

            // delete(askinfo.getData().get(position).getId(), position);
        }
    };

    class onClick_back implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            // TODO Auto-generated method stub
            switch (view.getId()) {
                case R.id.btn_1:
                    getActivity().finish();
                    break;
                case R.id.btn_2:
                    if (items != null & items.size() > 0) {

                        if ("批量操作".equals(btn2.getText())) {
                            adapter.isShow = true;
                            isDelete = true;
                            btn1.setVisibility(View.GONE);
                            btn2.setText("删除");
                            tv_cancell.setVisibility(View.VISIBLE);
                            adapter.notifyDataSetChanged();
                        } else if ("删除".equals(btn2.getText())) {
                            if (getCheck()) {
                                delete();
                                tv_cancell.setVisibility(View.GONE);
                                btn1.setVisibility(View.VISIBLE);
                                adapter.isShow = false;
                                btn2.setText("批量操作");
                                isDelete = false;
                                adapter.init();
                                adapter.notifyDataSetChanged();

                            } else {
                                ToastUtils.shortToast( "请至少选择一项");
                            }

                        }
                    } else

                    {
                        ToastUtils.shortToast( "没有下载完成的选项");
                    }
                    break;
                case R.id.tv_cancell:
                    adapter.isShow = false;
                    isDelete = false;
                    adapter.notifyDataSetChanged();
                    btn2.setText("批量操作");
                    tv_cancell.setVisibility(View.GONE);
                    btn1.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        }
    }

    public boolean getCheck() {
        boolean ischeck = false;
        for (int i = 0; i < items.size(); i++) {
            if (adapter.spar_map.get(i)) {
                ischeck = true;
                break;
            }
        }
        return ischeck;
    }

    public void delete() {
        StringBuilder sb = new StringBuilder();
        List<DownFileItemInfo> newItems = new ArrayList<DownFileItemInfo>();
        newItems = items;
        for (int i = newItems.size() - 1; i >= 0; i--) {
            if (adapter.spar_map.get(i)) {
                String str = items.get(i).getMovieName();
                // sb.append("'" + str + " ' " + "or");
                DLog.i(TAG, "删除的" + str);
                File file = new File(items.get(i).getFilePath());
                if (file.exists()) {
                    if (file.isFile()) {
                        file.delete();
                    }
                }
                fb.deleteByWhere(clazz, "movieName='" + str + "'");
                items.remove(items.get(i));

            }
        }
        // String ser = sb.substring(0, sb.lastIndexOf("or"));
        // YMApplication.Trace("sql" + ser);
        // // fb.deleteByWhere(clazz, "movieName=" + ser);

        // for (int i = 0; i < newItems.size(); i++)
        // {
        // if (adapter.spar_map.get(i))
        // {
        // YMApplication.Trace("移除的"+items.get(i).getMovieName());
        // items.remove(i);
        // }
        //
        // }
        if (items.size() > 0) {
            adapter.setList(items);
            adapter.notifyDataSetChanged();
        } else {
            listView.setVisibility(View.GONE);
        }

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            clazz = DownFileItemInfo.class;
            try {

                items = fb.findAllByWhere(clazz, "downloadState='" + 6
                        + "' and userid= '"
                        + YMApplication.getLoginInfo().getData().getPid()
                        + "' and commed= '" + type_commed + "'");
                if (items != null) {
                    getFile();
                }
            } catch (Exception e) {
                // TODO: handle exception
            }

            // if (items != null & items.size() > 0)
            // {
            // listView.setVisibility(View.VISIBLE);
            // if (adapter != null & items != null)
            // {
            // adapter.setList(items);
            // adapter.notifyDataSetChanged();
            // } else
            // {
            // adapter = new DownloadListAdapter(getActivity());
            // adapter.setList(items);
            // listView.setAdapter(adapter);
            // }
            //
            // } else
            // {
            // listView.setVisibility(View.GONE);
            // }
        }
    }

    public void getFile() {
        List<DownFileItemInfo> newItems = new ArrayList<DownFileItemInfo>();
        newItems = items;
        for (int i = newItems.size() - 1; i >= 0; i--) {
            String filepath = items.get(i).getFilePath().toString();
            String filename = items.get(i).getMovieName().toString();
            try {
                File f = new File(filepath);
                if (!f.exists()) {
                    DLog.i(TAG, "文件地址没有");
                    fb.deleteByWhere(clazz, "movieName='" + filename + "'");
                    items.remove(items.get(i));
                    // return false;
                }

            } catch (Exception e) {
                // TODO: handle exception
                DLog.i(TAG, "数据操作错误日志" + e);
                // return false;
            }
            // return true;
        }
        if (items.size() > 0) {
            listView.setVisibility(View.VISIBLE);
            if (adapter != null) {
                adapter.setList(items);
                adapter.notifyDataSetChanged();
            } else {
                adapter = new DownloadListAdapter(getActivity());
                adapter.setList(items);
                listView.setAdapter(adapter);
            }

        } else {
            listView.setVisibility(View.GONE);
        }

    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        StatisticalTools.fragmentOnResume("DownListFragment");

        clazz = DownFileItemInfo.class;
        try {
            // fb = FinalDb.create(getActivity(), "coupon.db");
            fb = FinalDb.create(getActivity(), "coupon.db", true, 2,
                    new FinalDb.DbUpdateListener() {

                        @Override
                        public void onUpgrade(SQLiteDatabase arg0, int arg1,
                                              int arg2) {
                            // TODO Auto-generated method stub
                            DLog.i(TAG, "数据库版本" + arg1 + "新的" + arg2);
                            arg0.execSQL("ALTER TABLE downloadtask  ADD commed default '0'");

                        }
                    });

            items = fb.findAllByWhere(clazz, "downloadState='" + 6
                    + "' and userid= '"
                    + YMApplication.getLoginInfo().getData().getPid()
                    + "' and commed= '" + type_commed + "'");
            if (items != null) {
                getFile();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        // if (items != null)
        // {
        // if (items.size() > 0)
        // {
        // listView.setVisibility(View.VISIBLE);
        // if (adapter != null & items != null)
        // {
        // adapter.setList(items);
        // adapter.notifyDataSetChanged();
        // } else
        // {
        // adapter = new DownloadListAdapter(getActivity());
        // adapter.setList(items);
        // listView.setAdapter(adapter);
        // }
        //
        // } else
        // {
        // listView.setVisibility(View.GONE);
        // }
        //
        // } else
        // {
        // listView.setVisibility(View.GONE);
        // }
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        StatisticalTools.fragmentOnPause("DownListFragment");
    }

}
