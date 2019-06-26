package com.xywy.askforexpert.module.my.download;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.DownFileItemInfo;
import com.xywy.askforexpert.module.main.service.downFile.BaseDownFragment;
import com.xywy.askforexpert.module.main.service.downFile.ContentValue;

import net.tsz.afinal.FinalDb;
import net.tsz.afinal.FinalDb.DbUpdateListener;

import java.util.List;

/**
 * 下载中
 *
 * @author 王鹏
 * @2015-6-10上午11:33:51
 */
public class DownloadingFragment extends BaseDownFragment implements
        ContentValue {
    private static final String TAG = "DownloadingFragment";
    private ListView listView;
    private DownloadAdapter adapter;
    private List<DownFileItemInfo> file;
    private Class<DownFileItemInfo> clazz;
    private FinalDb fb;
    private View btn1;
    public TextView btn2, btn3;
    private Handler handler = new Handler();
    private TextView tv_cancle;
    private boolean isGo;
    private android.support.v7.app.AlertDialog.Builder dialog;
    private String type_commed;
    private String usid;
    private int time = 500;
    private Runnable runnable = new Runnable() {

        public void run() {
            usid = YMApplication.getLoginInfo().getData().getPid();
            file = getMyApp().getNewDownloadItems();
            for (int i = 0; i < file.size(); i++) {
                String filecommd = file.get(i).getCommed();
                int downstate = file.get(i).getDownloadState();
                String uid = file.get(i).getUserid();
                if (!type_commed.equals(filecommd) | !uid.equals(usid)) {
                    file.remove(i);
                }
            }
            if (file != null) {
                if (file.size() > 0) {
                    listView.setVisibility(View.VISIBLE);
                    if (adapter != null) {
                        adapter.setFile(file);
                        adapter.notifyDataSetChanged();
                    } else {
                        adapter = new DownloadAdapter(getActivity(), listView,
                                file, type_commed, usid);
                    }
                } else {
                    isGo = true;
                    listView.setVisibility(View.GONE);
                    btn3.setVisibility(View.GONE);
                    btn2.setVisibility(View.VISIBLE);
                    tv_cancle.setVisibility(View.GONE);
                    btn1.setVisibility(View.VISIBLE);
//					if(adapter!=null)
//					adapter.setEditState(false);// 设置为编辑状态
                }

            } else {
                isGo = true;
                listView.setVisibility(View.GONE);
                btn3.setVisibility(View.GONE);
                btn2.setVisibility(View.VISIBLE);
                tv_cancle.setVisibility(View.GONE);
                btn1.setVisibility(View.VISIBLE);
                if (adapter != null) {
                    adapter.setEditState(false);// 设置为编辑状态
                }
                adapter.notifyDataSetChanged();
            }

            if (!isGo) {
                handler.postDelayed(this, time);
                DLog.i(TAG, "下载刷新中。。。");
            }

        }

    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        Bundle budle = getArguments();
        type_commed = budle.getString("type_commed");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        return inflater.inflate(R.layout.downloading, container, false);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        // TODO Auto-generated method stub
        DLog.i(TAG, "隐藏状态。。。" + hidden);
        if (hidden) {
            isGo = true;
            if (runnable != null) {
                handler.removeCallbacks(runnable);
            }
        } else {
            handler.postDelayed(runnable, 100);
            isGo = false;
        }
        super.onHiddenChanged(hidden);
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        super.initView();
        listView = (ListView) getActivity().findViewById(R.id.list);
        btn1 =  getActivity().findViewById(R.id.btn1);
        btn2 = (TextView) getActivity().findViewById(R.id.btn2);
        btn3 = (TextView) getActivity().findViewById(R.id.btn3);
        tv_cancle = (TextView) getActivity().findViewById(R.id.tv_cancle);
        btn1.setOnClickListener(new onClick_back());
        btn2.setOnClickListener(new onClick_back());
        btn3.setOnClickListener(new onClick_back());
        tv_cancle.setOnClickListener(new onClick_back());
        listView.setDivider(null);
        clazz = DownFileItemInfo.class;
        // fb = FinalDb.create(getActivity(), "coupon.db");
        fb = FinalDb.create(getActivity(), "coupon.db", true, 2,
                new DbUpdateListener() {

                    @Override
                    public void onUpgrade(SQLiteDatabase arg0, int arg1,
                                          int arg2) {
                        // TODO Auto-generated method stub
                        DLog.i(TAG, "数据库版本" + arg1 + "新的" + arg2);
                        arg0.execSQL("ALTER TABLE downloadtask  ADD commed default '0'");

                    }
                });

        dialog = new android.support.v7.app.AlertDialog.Builder(getActivity());
        // file = fb.findAllByWhere(clazz, "downloadState<>" + 6 + "");

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        initView();
        if (getMyApp().getDownloadItems() != null
                && getMyApp().getDownloadItems().size() != 0) {
            // 如果Applaction里面有数据
        } else {
            // 从数据库中加载
            Intent i = getServerIntent();
            i.putExtra(SERVICE_TYPE_NAME, START_DOWNLOAD_LOADITEM);
            getActivity().startService(i);
        }
        // handler.postDelayed(runnable, 100);

        super.onActivityCreated(savedInstanceState);
    }

    class onClick_back implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            // TODO Auto-generated method stub
            switch (view.getId()) {
                case R.id.btn1:
                    getActivity().finish();
                    break;
                case R.id.btn2:
                    if (file != null & file.size() > 0) {
                        time = 1000;
                        btn2.setVisibility(View.GONE);
                        btn3.setVisibility(View.VISIBLE);
                        tv_cancle.setVisibility(View.VISIBLE);
                        btn1.setVisibility(View.GONE);
                        if (adapter != null) {
                            adapter.setEditState(true);// 设置为编辑状态
                            adapter.notifyDataSetChanged();
                        }

                    } else {
                        ToastUtils.shortToast( "没有正在下载的选项");
                    }
                    break;
                case R.id.btn3:

                    dialog.setTitle("删除下载");

                    dialog.setMessage("是否清空所有任务");

                    dialog.setPositiveButton("确定", new OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = getServerIntent();
                            i.putExtra(SERVICE_TYPE_NAME, DOWNLOAD_STATE_CLEAR);
                            getActivity().startService(i);
                            // 切换为编辑状态
                            btn3.setVisibility(View.GONE);
                            btn2.setVisibility(View.VISIBLE);
                            tv_cancle.setVisibility(View.GONE);
                            btn1.setVisibility(View.VISIBLE);
                            if (adapter != null) {
                                adapter.notifyDataSetChanged();
                            }
                        }
                    });
                    dialog.setNegativeButton("取消", new OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            btn3.setVisibility(View.GONE);
                            btn2.setVisibility(View.VISIBLE);
                            tv_cancle.setVisibility(View.GONE);
                            btn1.setVisibility(View.VISIBLE);
                            if (adapter != null) {
                                adapter.setEditState(false);// 设置为编辑状态
                                adapter.notifyDataSetChanged();
                            }

                            dialog.dismiss();
                        }
                    });
                    dialog.create().show();
                    // new AlertDialog.Builder(getActivity()).setTitle("提示")
                    // .setMessage("是否清空所有任务？") // 二次提示
                    // .setNegativeButton("确定", new OnClickListener()
                    // {
                    // public void onClick(DialogInterface dialog,
                    // int which)
                    // {
                    // Intent i = getServerIntent();
                    // i.putExtra(SERVICE_TYPE_NAME,
                    // DOWNLOAD_STATE_CLEAR);
                    // getActivity().startService(i);
                    // // 切换为编辑状态
                    // btn3.setVisibility(View.GONE);
                    // btn2.setVisibility(View.VISIBLE);
                    // tv_cancle.setVisibility(View.GONE);
                    // btn1.setVisibility(View.VISIBLE);
                    // if (adapter != null)
                    // adapter.notifyDataSetChanged();
                    // }
                    // }).setPositiveButton("取消", new OnClickListener()
                    // {
                    //
                    // public void onClick(DialogInterface dialog,
                    // int which)
                    // {
                    // btn3.setVisibility(View.GONE);
                    // btn2.setVisibility(View.VISIBLE);
                    // tv_cancle.setVisibility(View.GONE);
                    // btn1.setVisibility(View.VISIBLE);
                    // adapter.setEditState(false);// 设置为编辑状态
                    // adapter.notifyDataSetChanged();
                    // }
                    // }).show();
                    break;

                case R.id.tv_cancle:
                    time = 500;
                    btn3.setVisibility(View.GONE);
                    btn2.setVisibility(View.VISIBLE);
                    tv_cancle.setVisibility(View.GONE);
                    btn1.setVisibility(View.VISIBLE);
                    if (adapter != null) {
                        adapter.setEditState(false);// 设置为编辑状态
                        adapter.notifyDataSetChanged();
                    }

                    break;
                default:
                    break;
            }

        }

    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub

        isGo = true;
        if (runnable != null) {
            handler.removeCallbacks(runnable);
        }
        super.onDestroy();
    }

    public void onResume() {
        super.onResume();
        StatisticalTools.fragmentOnResume("DownloadingFragment");
    }

    public void onPause() {
        StatisticalTools.fragmentOnPause("DownloadingFragment");
        super.onPause();
    }
}
