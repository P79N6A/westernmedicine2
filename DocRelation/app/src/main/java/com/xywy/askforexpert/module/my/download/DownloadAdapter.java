package com.xywy.askforexpert.module.my.download;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.DownFileItemInfo;
import com.xywy.askforexpert.module.main.service.downFile.ContentValue;
import com.xywy.askforexpert.module.main.service.downFile.DownloadService;

import java.util.List;

/**
 * 下载中列表
 *
 * @author 王鹏
 * @2015-6-10下午1:59:18
 */
public class DownloadAdapter extends BaseAdapter implements ContentValue {

    private static final String TAG = "DownloadAdapter";
    private Context mContext;
    private ListView downloListView;
    private List<DownFileItemInfo> file;
    private YMApplication myApp;
    private boolean isEditState;
    private android.support.v7.app.AlertDialog.Builder dialog;
    private String type_commed;
    private String usid;
    private int size;

    /**
     * Title: Description:
     */
    public DownloadAdapter(Context mContext, ListView downloadListView,
                           List<DownFileItemInfo> file, String type_commed, String usid) {
        this.mContext = mContext;
        this.downloListView = downloadListView;
        this.file = file;
        this.downloListView.setAdapter(this);
        this.type_commed = type_commed;
        this.usid = usid;
        if (mContext != null) {
            myApp = (YMApplication) mContext.getApplicationContext();
            dialog = new android.support.v7.app.AlertDialog.Builder(mContext);
        }
        size = file.size();

    }

    @Override
    public int getCount() {
        return file.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        DownFileItemInfo d = file.get(position);
        View view = null;
        ViewHolder holder = null;
        if (convertView != null) {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        } else {
            view = View.inflate(mContext, R.layout.downloading_item, null);
            holder = new ViewHolder();
            holder.current_progress = (TextView) view
                    .findViewById(R.id.current_progress);
            holder.download_progressBar = (ProgressBar) view
                    .findViewById(R.id.download_progressBar);
            // holder.movie_file_size = (TextView) view
            // .findViewById(R.id.movie_file_size);
            holder.file_name_item = (TextView) view
                    .findViewById(R.id.tv_file_name);
            holder.stop_download_bt = (TextView) view
                    .findViewById(R.id.tv_download_stype);
            holder.delete_file = (ImageView) view.findViewById(R.id.img_delete);
            view.setTag(holder);
        }
        // YMApplication.Trace("下载状态。。" + d.getDownloadState());

        if (d != null) {
            // System.out.println(d.getMovieName()+"的下载状态："+d.getDownloadState());
            switch (d.getDownloadState()) {
                case DOWNLOAD_STATE_SUCCESS:

//                    update(position);
                    holder.stop_download_bt.setVisibility(View.VISIBLE);
                    holder.current_progress.setText(d.getPercentage());
                    holder.stop_download_bt.setText("下载完成");
                    holder.current_progress.setTextColor(Color
                            .parseColor("#23b5bc"));

                    break;
                case DOWNLOAD_STATE_DOWNLOADING:
                    // 如果下载中,可以停止

                    holder.stop_download_bt.setVisibility(View.VISIBLE);
                    holder.stop_download_bt.setText("暂停");
                    holder.current_progress.setText(d.getPercentage());
                    holder.current_progress.setTextColor(Color
                            .parseColor("#23b5bc"));
                    break;
                case DOWNLOAD_STATE_SUSPEND:
                    // 如果已经停止,可以开始
                    holder.stop_download_bt.setVisibility(View.VISIBLE);
                    holder.stop_download_bt.setText("开始");
                    holder.current_progress.setText(d.getPercentage());
                    holder.current_progress.setTextColor(Color
                            .parseColor("#23b5bc"));
                    break;
                case DOWNLOAD_STATE_EXCLOUDDOWNLOAD:
                    // 如果不在当前下载队列之内
                    holder.stop_download_bt.setText("等待中");
                    holder.stop_download_bt.setVisibility(View.INVISIBLE);
                    holder.current_progress.setText(d.getPercentage());
                    holder.current_progress.setTextColor(Color
                            .parseColor("#23b5bc"));
                    break;
                case DOWNLOAD_STATE_FAIL:
                    // 如果是下载失败状态
                    holder.stop_download_bt.setText("重试");
                    holder.stop_download_bt.setTextColor(Color
                            .parseColor("#333333"));
                    holder.current_progress.setTextColor(Color
                            .parseColor("#f39801"));
                    holder.current_progress.setText("下载失败");
                    break;
                default:
                    break;
            }
            DLog.i(TAG, "点击isEditState" + isEditState);
            if (isEditState) {
                // 如果是编辑状态,显示删除按钮
                holder.delete_file.setVisibility(View.VISIBLE);
                // 为删除按钮设置点击事件
                holder.delete_file.setOnClickListener(new MyOnClick(holder, d,
                        true, position));
                // 设置点击事件
            } else {
                holder.delete_file.setOnClickListener(null);
                holder.delete_file.setVisibility(View.GONE);
            }
            int count = d.getProgressCount().intValue();
            int currentPress = d.getCurrentProgress().intValue();
            holder.download_progressBar.setMax(count);
            holder.download_progressBar.setProgress(currentPress);
            holder.file_name_item.setText(d.getMovieName());
            // String imagePath = new
            // File(mContext.getCacheDir(),d.getMovieId()+".png").getAbsolutePath();
            holder.stop_download_bt.setOnClickListener(new MyOnClick(holder, d,
                    false, position));
        }
        return view;
    }

    public boolean isEditState() {
        return isEditState;
    }

    public void setEditState(boolean isEditState) {
        this.isEditState = isEditState;
    }

    public List<DownFileItemInfo> getFile() {
        return file;
    }

    public void setFile(List<DownFileItemInfo> file) {
        this.file = file;

    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * 更新
     *
     * @param position
     */
    public void update(int position) {
//        if (!type_commed.equals(file.get(position).getCommed())
//                | file.get(position).getDownloadState() == 6
//                | !file.get(position)
//                .getUserid()
//                .equals(usid))
//        {
        file.remove(position);
        setFile(file);
        notifyDataSetChanged();
//        }


    }

    static class ViewHolder {
        TextView file_name_item;
        // TextView movie_file_size;
        ProgressBar download_progressBar;
        TextView current_progress;
        TextView stop_download_bt;
        ImageView delete_file;
    }

    class MyOnClick implements OnClickListener {

        private ViewHolder holder;
        private DownFileItemInfo dmi;
        private boolean isDeleteMovie;
        private int position;

        /**
         * Title: Description:
         */
        public MyOnClick(ViewHolder holder, DownFileItemInfo dmi,
                         boolean isDeleteMovie, int position) {
            this.holder = holder;
            this.dmi = dmi;
            this.isDeleteMovie = isDeleteMovie;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            DLog.i(TAG, "点击了。");
            if (position <= file.size()) {
                dmi = file.get(position);
                final Intent i = new Intent(mContext, DownloadService.class);
                if (isDeleteMovie) {
                    dialog.setTitle("要删除下载文件吗？");

                    dialog.setMessage("此操作将会永久删除文件");

                    dialog.setPositiveButton(
                            "确定",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    if (file != null)

                                    {
                                        if (size != file.size()) {
                                            if (dialog != null) {
                                                ToastUtils.shortToast("列表已变更，请重新选择");
                                                size = file.size();
                                                dialog.dismiss();

                                            }

                                        } else {
                                            i.putExtra(SERVICE_TYPE_NAME,
                                                    DOWNLOAD_STATE_DELETE);
                                            myApp.setStopOrStartDownloadfileItem(dmi);

                                            mContext.startService(i);
                                            file.remove(position);
                                            notifyDataSetChanged();
                                            size = file.size();
                                        }
                                    }


                                }
                            });
                    dialog.setNegativeButton(
                            "取消",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss();
                                }
                            });
                    dialog.create().show();
                    // new AlertDialog.Builder(mContext)
                    // .setTitle("要删除下载文件吗？")
                    // .setMessage("此操作将会永久删除文件")
                    // // 二次提示
                    // .setNegativeButton("确定",
                    // new DialogInterface.OnClickListener()
                    // {
                    // public void onClick(
                    // DialogInterface dialog,
                    // int which)
                    // {
                    // i.putExtra(SERVICE_TYPE_NAME,
                    // DOWNLOAD_STATE_DELETE);
                    // myApp.setStopOrStartDownloadfileItem(dmi);
                    // mContext.startService(i);
                    // notifyDataSetChanged();
                    // }
                    // })
                    // .setPositiveButton("取消",
                    // new DialogInterface.OnClickListener()
                    // {
                    //
                    // public void onClick(
                    // DialogInterface dialog,
                    // int which)
                    // {
                    // // btn3.setVisibility(View.GONE);
                    // // btn2.setVisibility(View.VISIBLE);
                    // // adapter.setEditState(false);//
                    // // 设置为编辑状态
                    // // adapter.notifyDataSetChanged();
                    // }
                    // }).show();
                } else {

                    if (dmi.getDownloadState() == DOWNLOAD_STATE_SUCCESS) {
                        // 在这里添加下载完成时的代码
                    } else {
                        if (size != file.size()) {
                            ToastUtils.shortToast("列表已经变更，请重新选择");
                            size = file.size();
                        } else {


                            int code = dmi.getDownloadState();
                            switch (code) {
                                case DOWNLOAD_STATE_SUSPEND:
                                    // 如果是停止状态,设置为开始
                                    holder.stop_download_bt.setText("暂停");
                                    // 停止这个下载任务
                                    i.putExtra(SERVICE_TYPE_NAME, DOWNLOAD_STATE_START);
                                    dmi.setDownloadState(DOWNLOAD_STATE_WATTING);
                                    break;
                                case DOWNLOAD_STATE_DOWNLOADING:
                                    // 暂停这个下载任务
                                    holder.stop_download_bt.setText("开始");
                                    dmi.setDownloadState(DOWNLOAD_STATE_SUSPEND);
                                    i.putExtra(SERVICE_TYPE_NAME,
                                            DOWNLOAD_STATE_SUSPEND);
                                    break;
                                case DOWNLOAD_STATE_FAIL:
                                    // 如果下载失败,重新下载
                                    // 开始这个下载任务
                                    dmi.setDownloadState(DOWNLOAD_STATE_WATTING);
                                    i.putExtra(SERVICE_TYPE_NAME, DOWNLOAD_STATE_START);
                                    ToastUtils.shortToast(
                                            "重新下载：" + dmi.getMovieName());
                                    break;

                                default:
                                    break;
                            }
                        }

                    }
                }
                myApp.setStopOrStartDownloadfileItem(dmi);
                mContext.startService(i);
            } else {
                // 数组角标越界
            }
        }
    }
}
