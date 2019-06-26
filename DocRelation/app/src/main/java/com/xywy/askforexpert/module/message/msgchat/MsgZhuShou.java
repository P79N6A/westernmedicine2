package com.xywy.askforexpert.module.message.msgchat;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.model.Message;
import com.xywy.askforexpert.module.message.MsgInfoAdapter;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：D_Platform 类名称：MsgZhuShou 类描述：消息小助手 创建人：shihao 创建时间：2015-6-11 上午11:52:29
 * 修改备注：
 * 2018-6-4 重构
 */
public class MsgZhuShou extends YMBaseActivity {

    private ListView lvMsg;

    private List<Message> msgLists;
    private Context context;

    private MsgInfoAdapter adapter;

    private RelativeLayout noDataView;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        this.context = this;
//
//        setContentView(R.layout.activity_zhushou);
//        CommonUtils.initSystemBar(this);
//        lvMsg = (ListView) findViewById(R.id.lv_msg);
//        noDataView = (RelativeLayout) findViewById(R.id.rl_no_data);
//        adapter = new MsgInfoAdapter(this);
//
//        if (!YMUserService.isGuest() && YMApplication.getLoginInfo().getData().getIsjob().equals("1")) {
//            noDataView.setVisibility(View.GONE);
//            msgXiaoZhu();
//        } else {
//            noDataView.setVisibility(View.VISIBLE);
//        }
//
//        lvMsg.setOnItemClickListener(new OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                // 跳转到问答消息列表
//                Intent intent = new Intent(context, MsgQueList.class);
//                intent.putExtra("title", msgLists.get(position).getTitle());
//                intent.putExtra("type", msgLists.get(position).getType() + "");
//                startActivityForResult(intent, 6000);
//            }
//        });
//    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_zhushou;
    }

    @Override
    protected void initView() {
        this.context = this;
        CommonUtils.initSystemBar(this);
        titleBarBuilder.setTitleText("消息小助手");
        lvMsg = (ListView) findViewById(R.id.lv_msg);
        noDataView = (RelativeLayout) findViewById(R.id.rl_no_data);
        adapter = new MsgInfoAdapter(this);

        if (!YMUserService.isGuest() && YMApplication.getLoginInfo().getData().getIsjob().equals("1")) {
            noDataView.setVisibility(View.GONE);
            msgXiaoZhu();
        } else {
            noDataView.setVisibility(View.VISIBLE);
        }

        lvMsg.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // 跳转到问答消息列表
                Intent intent = new Intent(context, MsgQueList.class);
                intent.putExtra("title", msgLists.get(position).getTitle());
                intent.putExtra("type", msgLists.get(position).getType() + "");
                startActivityForResult(intent, 6000);
            }
        });
    }

    @Override
    protected void initData() {
    }

    /**
     * 请求网络获取数据
     */
    private void msgXiaoZhu() {
        AjaxParams params = new AjaxParams();
        String userid = YMApplication.getLoginInfo().getData().getPid();
        params.put("userid", userid);
        params.put("sign",
                MD5Util.MD5(userid + Constants.MD5_KEY));
        FinalHttp fh = new FinalHttp();

        fh.post(CommonUrl.MSG_ZHU_SHOU, params, new AjaxCallBack() {

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
                msgLists = new ArrayList<Message>();
                try {
                    JSONObject jsonObject = new JSONObject(t);
                    int code = jsonObject.getInt("code");
                    String msg = jsonObject.getString("msg");
                    JSONObject jsonChild = jsonObject.getJSONObject("data");
                    String count = jsonChild.getString("count");
                    String msgTotals = jsonChild.getString("msgTotal");
                    String msg_detail = jsonChild.getString("detail");
                    String msg_createtime = jsonChild.getString("createtime");
                    YMApplication.msgnum = count;
                    YMApplication.msgdetail = msg_detail;
                    YMApplication.msgcreatetime = msg_createtime;
                    YMApplication.msgtotal = msgTotals;
                    JSONArray array = jsonChild.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonElement = array.getJSONObject(i);
                        Message message = new Message();
                        message.setType(jsonElement.getInt("type"));
                        message.setTitle(jsonElement.getString("title"));
                        message.setDetail(jsonElement.getString("detail"));
                        message.setCreateTime(jsonElement
                                .getString("createtime"));
                        message.setMsgNum(jsonElement.getString("msgnum"));
                        msgLists.add(message);
                        YMApplication.msgLists = msgLists;
//						YMApplication.msgnum=msgLists.get(0).getMsgNum();
                    }
                    if (msgLists.size() == 0) {
                        noDataView.setVisibility(View.VISIBLE);
                    } else {
                        noDataView.setVisibility(View.GONE);
                        adapter.bindData(msgLists);
                        lvMsg.setAdapter(adapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

//    public void onMsgBackListener(View v) {
//        switch (v.getId()) {
//            case R.id.btn_msg_back:
//                finish();
//                break;
//
//            default:
//                break;
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 6000) {
            if (resultCode == 6000) {
                msgXiaoZhu();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
