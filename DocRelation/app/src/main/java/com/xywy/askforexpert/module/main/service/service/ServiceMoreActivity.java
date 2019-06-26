package com.xywy.askforexpert.module.main.service.service;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.model.subscribe.ServeEntity;
import com.xywy.askforexpert.module.main.subscribe.ChannelManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务窗更多页面
 *
 * @author SHR
 * @2015-5-5上午9:58:26
 */
public class ServiceMoreActivity extends Activity {

    private static final String TAG = "ServiceMoreActivity";
    //	private List<ChannelItem> channelLists;
    private List<ServeEntity> serveEntityList;
    private ImageView noneView;

    private ListView moreListView;

    private MoreServiceAdapter adapter;

    private SharedPreferences saveSp;

    private boolean isChanged = false;


    /**
     * 当前用户id
     */
    private String userid = "";

//	private Handler handeler = new Handler() {
//
//		@Override
//		public void handleMessage(Message msg) {
//			super.handleMessage(msg);
//			if (msg.what == 1) {
//				YMApplication.getInstance().setChannelItemList(
//						(ArrayList<ChannelItem>) DBManager.getManager()
//								.getUserChannelData());
//				if (serveEntityList.size() == 0) {
//					noneView.setVisibility(View.VISIBLE);
//				} else {
//					noneView.setVisibility(View.GONE);
//				}
//			}
//		}
//
//	};

    //列表中type为0是没添加的type为1是添加的type为2是固定的

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_more);

        CommonUtils.initSystemBar(this);
        noneView = (ImageView) findViewById(R.id.iv_have_none);
        moreListView = (ListView) findViewById(R.id.lv_add_service);

        if (serveEntityList == null) {
            serveEntityList = new ArrayList<>();
        }

        serveEntityList.clear();

        serveEntityList = ChannelManager.getManager().getServiceChannel();

        adapter = new MoreServiceAdapter();
        moreListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        if (!YMUserService.isGuest()) {
            userid = YMApplication.getLoginInfo().getData().getPid();
        } else {
            userid = "";
        }

        if (serveEntityList.size() == 0) {
            noneView.setVisibility(View.VISIBLE);
        } else {
            noneView.setVisibility(View.GONE);
        }

//==================================================================


//		saveSp = YMApplication.applicationContext.getSharedPreferences("saveChannel", Context.MODE_PRIVATE);
//
//		ServiceAPI.getInstance().getServiceData(userid, new AjaxCallBack() {
//			@Override
//			public void onSuccess(String s) {
//				super.onSuccess(s);
//
//				DLog.i(TAG,"服务定制数据"+s);
//
//				Gson gson = new Gson();
//
//				serviceTitleEntity = gson.fromJson(s, ServiceTitleEntity.class);
//
//				if (serviceTitleEntity.getCode()==0){
//					saveSp.edit().putString("channelData",s).apply();
//					saveSp.edit().putBoolean("changed", false).apply();
//					DLog.i(TAG, "解析数据订阅服务等" + serviceTitleEntity.getCode() + "服务数据" + (serviceTitleEntity.getSubscribe() == null));
//					if (serviceTitleEntity.getServe()!=null){
//						int size = serviceTitleEntity.getServe().size();
//
//						for (int i = 0;i<size;i++){
//							DLog.i(TAG, "服务数据" + serviceTitleEntity.getServe().get(i).getName());
//						}
//					}
//				}
//
//
//			}
//
//			@Override
//			public void onFailure(Throwable t, int errorNo, String strMsg) {
//				super.onFailure(t, errorNo, strMsg);
//			}
//
//			@Override
//			public void onLoading(long count, long current) {
//				super.onLoading(count, current);
//			}
//
//			@Override
//			public void onStart() {
//				super.onStart();
//			}
//		});
//
//
//		DLog.i(TAG, "频道列表===" + ChannelManager.getManager().getServiceDefautl());

//		if (ChannelManager.getManager().getUserChannel()!=null){
//			for (int i=0;i<ChannelManager.getManager().getUserChannel().size();i++){
//				DLog.i(TAG,"已添加"+ChannelManager.getManager().getUserChannel().get(i).getName());
//			}
//		}

        //==================================================================

    }

    private int[] more_pic = {R.drawable.service_more_1,
            R.drawable.service_more_2, R.drawable.service_more_3,
            R.drawable.service_more_4, R.drawable.service_more_5,
            R.drawable.service_more_6, R.drawable.service_more_7,
            R.drawable.service_more_8, R.drawable.service_more_9,
            R.drawable.service_more_10, R.drawable.service_more_11,
            R.drawable.service_more_1,
            R.drawable.service_more_2, R.drawable.service_more_3,
            R.drawable.service_more_4, R.drawable.service_more_5,
            R.drawable.service_more_6, R.drawable.service_more_7,};

    public void onMoreBackListener(View v) {
        switch (v.getId()) {
            case R.id.btn_setting_back:
                saveChannel();
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    private class MoreServiceAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return serveEntityList.size();
        }

        @Override
        public Object getItem(int position) {
            return serveEntityList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(ServiceMoreActivity.this)
                        .inflate(R.layout.more_service_item, null);
                holder.tvTitle = (TextView) convertView
                        .findViewById(R.id.tv_more_title);
                holder.ivMoreIcon = (ImageView) convertView
                        .findViewById(R.id.iv_more_icon);
                holder.state_btn = (Button) convertView
                        .findViewById(R.id.state_btn);
                holder.state_btn.setTag(position);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.tvTitle.setText(serveEntityList.get(position).getName());
//			holder.tvContent.setText(channelLists.get(position)
//					.getDescription());
            holder.ivMoreIcon.setImageResource(more_pic[serveEntityList.get(position).getId() - 1]);

            final int typeId = serveEntityList.get(position).getType();
            if (typeId == 2) {
                holder.state_btn.setText("已添加");
            } else if (typeId == 1) {
                holder.state_btn.setText("移除");
            } else {
                holder.state_btn.setText("添加到首页");
            }

            holder.state_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isChanged = true;
                    if (typeId == 1) {
                        holder.state_btn.setText("添加到首页");
                        serveEntityList.get(position).setType(0);
                    }
                    if (typeId == 0) {
                        holder.state_btn.setText("移除");
                        serveEntityList.get(position).setType(1);
                    }
                    notifyDataSetChanged();
                }
            });

            return convertView;
        }

        private class ViewHolder {
            TextView tvTitle;
            Button state_btn;
            ImageView ivMoreIcon;
        }

    }

    public void onResume() {
        super.onResume();
        StatisticalTools.onResume(this);
    }

    public void onPause() {

        StatisticalTools.onPause(this);
        super.onPause();
    }

    public void saveChannel() {
        if (isChanged) {
            ChannelManager.getManager().saveServiceChannel(serveEntityList);
            ChannelManager.getManager().saveDataToSp();
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            saveChannel();
        }
        return super.onKeyDown(keyCode, event);
    }
}
