package com.xywy.askforexpert.module.doctorcircle;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.model.QuestionSquareMsgItem;
import com.xywy.askforexpert.module.doctorcircle.adapter.FastReplyAdapter;
import com.xywy.askforexpert.module.main.service.que.QueDetailActivity;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：D_Platform 类名称：FastReplyActivity 类描述：快捷回复模块 创建人：shihao 创建时间：2015-5-25
 * 上午9:52:04 修改备注：
 */
public class FastReplyActivity extends AppCompatActivity {

    private ListView listView;
    private FastReplyAdapter adapter;

    private List<QuestionSquareMsgItem> mLists;

    private ImageButton ibBack, ibEdit;

    private Builder builder;

    private int currentPosition;

    private RelativeLayout rlNodata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fast_reply_activity);
        listView = (ListView) findViewById(R.id.lv_fast_reply);
        ibBack = (ImageButton) findViewById(R.id.btn_reply_back);
        ibEdit = (ImageButton) findViewById(R.id.ib_edit);

        builder = new Builder(this);

        rlNodata = (RelativeLayout) findViewById(R.id.rl_no_data);

        adapter = new FastReplyAdapter(FastReplyActivity.this);
        initDatas();

        ibBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ibEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FastReplyActivity.this,
                        EditActivity.class);
                startActivityForResult(intent, 3000);
            }
        });
    }

    private void initDatas() {
        mLists = new ArrayList<QuestionSquareMsgItem>();
        // 请求接口
        AjaxParams params = new AjaxParams();
        params.put("userid", YMApplication.getLoginInfo().getData().getPid());
        params.put(
                "sign",
                MD5Util.MD5(YMApplication.getLoginInfo().getData().getPid()
                        + Constants.MD5_KEY));
        FinalHttp fh = new FinalHttp();

        fh.post(CommonUrl.QUE_EDIT, params, new AjaxCallBack() {

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                // TODO Auto-generated method stub
                super.onFailure(t, errorNo, strMsg);
            }

            @Override
            public void onLoading(long count, long current) {
                // TODO Auto-generated method stub
                super.onLoading(count, current);
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    JSONObject jsonObject = new JSONObject(t);
                    int code = jsonObject.getInt("code");
                    String msg = jsonObject.getString("msg");
                    if (code == 0) {
                        JSONArray array = jsonObject.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonElement = array.getJSONObject(i);
                            QuestionSquareMsgItem data = new QuestionSquareMsgItem();
                            data.setId(jsonElement.getInt("id"));
                            data.setTitle(jsonElement.getString("content"));
                            mLists.add(data);
                        }

                        if (mLists.size() == 0) {
                            rlNodata.setVisibility(View.VISIBLE);
                        } else {
                            rlNodata.setVisibility(View.GONE);
                            adapter.bindData(mLists);
                            listView.setAdapter(adapter);

                            listView.setOnItemClickListener(new OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView<?> parent,
                                                        View view, int position, long id) {
                                    Intent intent = new Intent(
                                            FastReplyActivity.this,
                                            QueDetailActivity.class);
                                    intent.putExtra("content",
                                            mLists.get(position).getTitle());
                                    setResult(2015, intent);
                                    finish();
                                }
                            });

                            listView.setOnItemLongClickListener(new OnItemLongClickListener() {

                                @Override
                                public boolean onItemLongClick(
                                        AdapterView<?> parent, View view,
                                        int position, long id) {
                                    currentPosition = position;
                                    builder.setTitle("温馨提示").setMessage("是否删除该条信息?")
                                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    editContent(currentPosition);
                                                }
                                            })
                                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // TODO Auto-generated method stub

                                                }
                                            }).create().show();

                                    return true;
                                }
                            });
                        }

                    } else {
                        ToastUtils.shortToast( msg);
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
    }

    public void editContent(final int position) {
        // boolean flag = false;
        // 请求接口
        AjaxParams params = new AjaxParams();
        params.put("userid", YMApplication.getLoginInfo().getData().getPid());
        params.put("mid", mLists.get(position).getId() + "");
        params.put(
                "sign",
                MD5Util.MD5(YMApplication.getLoginInfo().getData().getPid()
                        + mLists.get(position).getId()
                        + Constants.MD5_KEY));
        FinalHttp fh = new FinalHttp();

        fh.post(CommonUrl.QUE_EDIT_DELETE, params, new AjaxCallBack() {
            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                // TODO Auto-generated method stub
                super.onFailure(t, errorNo, strMsg);
            }

            @Override
            public void onLoading(long count, long current) {
                // TODO Auto-generated method stub
                super.onLoading(count, current);
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    JSONObject jsonObject = new JSONObject(t);
                    int code = jsonObject.getInt("code");
                    String msg = jsonObject.getString("msg");
                    if (code == 0) {
                        ToastUtils.shortToast( msg);
                        initDatas();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
        // return flag;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 3000) {
            if (resultCode == 3001) {
                initDatas();
            }
        }
    }

    public void onResume() {
        super.onResume();
        StatisticalTools.onResume(this);
    }

    public void onPause() {
        super.onPause();
        StatisticalTools.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
