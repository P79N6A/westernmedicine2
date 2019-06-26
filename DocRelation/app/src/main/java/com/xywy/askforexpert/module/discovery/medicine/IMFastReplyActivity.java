package com.xywy.askforexpert.module.discovery.medicine;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.GsonUtils;
import com.xywy.askforexpert.appcommon.utils.SPUtils;
import com.xywy.askforexpert.model.IdNameBean;
import com.xywy.askforexpert.module.discovery.adapter.StringListViewAdapter;
import com.xywy.askforexpert.module.discovery.medicine.common.MyCallBack;
import com.xywy.askforexpert.module.discovery.medicine.util.GsonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * IM快捷回复 stone
 */
public class IMFastReplyActivity extends YMBaseActivity {
    public static final int REQUEST_CODE_ADD = 100;
    public static final int REQUEST_CODE_EDIT = 200;

//    private SwipeMenuListView slvListView;
    private ListView slvListView;
    private StringListViewAdapter adapter;
    private List<String> mStringList;

    private android.support.v7.app.AlertDialog.Builder dialog;


    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (!IMFastReplyActivity.this.isFinishing()) {
                Intent intent = new Intent(IMFastReplyActivity.this,
                        IMAddEditFastReplyActvity.class);
                intent.putExtra(Constants.KEY_TYPE, 1);
                intent.putExtra(Constants.KEY_POS, msg.what);
                intent.putExtra(Constants.KEY_VALUE, mStringList.get(msg.what));
                startActivityForResult(intent, REQUEST_CODE_EDIT);
            }
        }
    };


    public void onClick_back(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.next_btn:
                intent = new Intent(IMFastReplyActivity.this,
                        IMAddEditFastReplyActvity.class);
                intent.putExtra(Constants.KEY_TYPE, 0);
                startActivityForResult(intent, REQUEST_CODE_ADD);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        SPUtils.getUser().putString(Constants.IM_FAST_REPLY_CONTENT, GsonUtil.GsonString(mStringList));
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD && resultCode == RESULT_OK) {
            if (data != null) {
                String str = data.getStringExtra(Constants.KEY_VALUE);
                mStringList.add(str);
                adapter.setList(mStringList);
                adapter.notifyDataSetChanged();
            }
        } else if (requestCode == REQUEST_CODE_EDIT && resultCode == RESULT_OK) {
            if (data != null) {
                String str = data.getStringExtra(Constants.KEY_VALUE);
                int pos = data.getIntExtra(Constants.KEY_POS, 0);
                mStringList.remove(pos);
                mStringList.add(pos, str);
                adapter.setList(mStringList);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void initView() {

        titleBarBuilder.setTitleText("快捷回复");


        dialog = new android.support.v7.app.AlertDialog.Builder(IMFastReplyActivity.this, R.style.AlertDialogCustom);

        slvListView = (ListView) findViewById(R.id.slv_list_view);
        View view = LayoutInflater.from(IMFastReplyActivity.this).inflate(
                R.layout.footview_im_fastreply, null);
        slvListView.addFooterView(view);

        slvListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Intent intent = new Intent();
                intent.putExtra(Constants.KEY_VALUE, mStringList.get(arg2));
                setResult(RESULT_OK, intent);
                finish();
            }
        });
//        slvListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//
//            @Override
//            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
//                                           final int position, long arg3) {
//
//                // dialog = new DialogWindow(AskPatientReplyActivity.this,
//                // "删除快捷回复", "是否确定删除？", "取消", "确定");
//                dialog.setTitle("删除");
//
//                dialog.setMessage("是否确定删除这条快捷回复？");
//                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        // TODO Auto-generated method stub
////                        adapter.initView(GlobalContent.viewMap4IM.get(mStringList.get(position)));
//                        // list.remove(contentString);
//                        // adapter.setList(list);
//                        // adapter.notifyDataSetChanged();
//
//                        mStringList.remove(position);
//                        adapter.setList(mStringList);
//                        adapter.notifyDataSetChanged();
//
//                    }
//                });
//                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//                dialog.create().show();
//                return true;
//            }
//        });

        // 创建左滑弹出的item
//        SwipeMenuCreator creator = new SwipeMenuCreator() {
//
//            @Override
//            public void create(SwipeMenu menu) {
//                // 创建Item
//                SwipeMenuItem openItem1 = new SwipeMenuItem(getApplicationContext());
//                // 设置item的背景颜色
//                openItem1.setBackground(new ColorDrawable(Color.parseColor("#b6b6b6")));
//                // 设置item的宽度
//                openItem1.setWidth(DensityUtils.dp2px(60));
//                // 设置item标题
//                openItem1.setTitle("编辑");
//                // 设置item字号
//                openItem1.setTitleSize(14);
//                // 设置item字体颜色
//                openItem1.setTitleColor(Color.WHITE);
////                openItem2.setIcon(R.drawable.ic_action_delete);
//                // 添加到ListView的Item布局当中
//                menu.addMenuItem(openItem1);
//
//                // 创建Item
//                SwipeMenuItem openItem2 = new SwipeMenuItem(getApplicationContext());
//                // 设置item的背景颜色
//                openItem2.setBackground(new ColorDrawable(Color.parseColor("#f4aa29")));
//                // 设置item的宽度
//                openItem2.setWidth(DensityUtils.dp2px(60));
//                // 设置item标题
//                openItem2.setTitle("删除");
//                // 设置item字号
//                openItem2.setTitleSize(14);
//                // 设置item字体颜色
//                openItem2.setTitleColor(Color.WHITE);
////                openItem.setIcon(R.drawable.ic_action_delete);
//                // 添加到ListView的Item布局当中
//                menu.addMenuItem(openItem2);
//            }
//        };
//        // set creator
//        slvListView.setMenuCreator(creator);
//
//        // 操作删除按钮的点击事件
//        slvListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
//                final String item = mStringList.get(position);
//                if (index == 1) {
//                    showDeleteDialog(position);
//                } else if (index == 0) {
//                    handler.sendEmptyMessageDelayed(position, 200);
//                }
//                return false;
//            }
//        });


        // 操作ListView左滑时的手势操作，这里用于处理上下左右滑动冲突：开始滑动时则禁止下拉刷新和上拉加载手势操作，结束滑动后恢复上下拉操作
//        swipeMenuListView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {
//            @Override
//            public void onSwipeStart(int position) {
//                refreshlistview.setPullRefreshEnabled(false);
//            }
//
//            @Override
//            public void onSwipeEnd(int position) {
//                refreshlistview.setPullRefreshEnabled(true);
//            }
//        });

//        swipeMenuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(SwipeListViewActivity.this, "点击了" + i, Toast.LENGTH_LONG).show();
//            }
//        });
    }

    @Override
    protected void initData() {
        String s = SPUtils.getUser().getString(Constants.IM_FAST_REPLY_CONTENT);
        if (!TextUtils.isEmpty(s)) {
            mStringList = GsonUtils.parsJsonArrayStr2List(s, String.class);
            if (mStringList == null) {
                mStringList = new ArrayList<>();
            }
        } else {
            mStringList = new ArrayList<>();
        }
        adapter = new StringListViewAdapter(mStringList, IMFastReplyActivity.this, new MyCallBack() {
            @Override
            public void onClick(Object data) {
                if (data != null) {
                    final IdNameBean idNameBean = (IdNameBean) data;
                    if (idNameBean != null) {
                        if (!TextUtils.isEmpty(idNameBean.id)
                                && idNameBean.id.equals(Constants.EDIT)
                                && !TextUtils.isEmpty(idNameBean.name)) {
                            handler.sendEmptyMessageDelayed(Integer.parseInt(idNameBean.name), 200);
                        } else if (!TextUtils.isEmpty(idNameBean.id)
                                && idNameBean.id.equals(Constants.DELETE)
                                && !TextUtils.isEmpty(idNameBean.name)) {
                            showDeleteDialog(Integer.parseInt(idNameBean.name));
                        }
                    }
                }

            }
        });
        slvListView.setAdapter(adapter);
    }

    private void showDeleteDialog(final int pos) {
        dialog.setTitle("删除");

        dialog.setMessage("是否确定删除这条快捷回复？");
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                mStringList.remove(pos);
                adapter.setList(mStringList);
                adapter.notifyDataSetChanged();

            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.create().show();
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.list_im_fast_reply;
    }


}
