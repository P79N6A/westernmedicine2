package com.xywy.askforexpert.module.main.service.linchuang.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.model.CodexSecondInfo;
import com.xywy.askforexpert.model.DownFileItemInfo;
import com.xywy.askforexpert.module.main.service.downFile.ContentValue;
import com.xywy.askforexpert.module.main.service.downFile.DownloadService;

import net.tsz.afinal.FinalDb;

import java.util.ArrayList;
import java.util.List;

/**
 * 学校城市 适配器
 *
 * @author 王鹏
 * @2015-5-7下午5:48:59
 */
@SuppressLint("ResourceAsColor")
public class BaseGuideAdapter extends BaseAdapter implements ContentValue {

    public SparseBooleanArray selectionMap;
    List<CodexSecondInfo> list;
    private Class<DownFileItemInfo> clazz;
    private Context context;
    private LayoutInflater inflater;
    private FinalDb fb;
    private YMApplication myApp;
    private List<DownFileItemInfo> items = new ArrayList<DownFileItemInfo>();

    public BaseGuideAdapter(Context context) {
        this.context = context;

        inflater = LayoutInflater.from(context);
        clazz = DownFileItemInfo.class;
        myApp = (YMApplication) context.getApplicationContext();
    }

    public FinalDb getFb() {
        return fb;
    }

    public void setFb(FinalDb fb) {
        this.fb = fb;
    }

    public void setData(List<CodexSecondInfo> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.guide_item, null);
            holder.textView = (TextView) convertView.findViewById(R.id.tv_text);
            holder.textView2 = (TextView) convertView
                    .findViewById(R.id.tv_text2);
            holder.re_item = (RelativeLayout) convertView
                    .findViewById(R.id.re_item);
            holder.btn_down = (TextView) convertView
                    .findViewById(R.id.btn_down);
            // holder.download_progressBar = (ProgressBar) convertView
            // .findViewById(R.id.download_progressBar);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String title = list.get(position).getTitle().toString().trim();
        if (list != null) {
            holder.textView.setText(title);
            holder.textView2.setText(list.get(position).getAuthor());
        }

//		items = fb.findAllByWhere(clazz, "movieName='" + title + "'");
        if (items.size() > 0) {
            switch (items.get(0).getDownloadState()) {
                case DOWNLOAD_STATE_DOWNLOADING:
                    holder.btn_down.setText("正在下载");
                    break;
                case DOWNLOAD_STATE_SUSPEND:
                    holder.btn_down.setText("暂停");
                    break;
                case DOWNLOAD_STATE_WATTING:
                    holder.btn_down.setText("等待");
                    break;
                case DOWNLOAD_STATE_FAIL:
                    holder.btn_down.setText("下载失败");
                    break;
                case DOWNLOAD_STATE_SUCCESS:
                    holder.btn_down.setText("已下载");
                    break;
                case DOWNLOAD_STATE_DELETE:
                    holder.btn_down.setText("gg");
                    break;
                case DOWNLOAD_STATE_NONE:
                    holder.btn_down.setText("下载");
                    break;
                case DOWNLOAD_STATE_START:
                    holder.btn_down.setText("等待");
                    break;
                case DOWNLOAD_STATE_EXCLOUDDOWNLOAD:
                    holder.btn_down.setText("等待中");
                    break;

                default:
                    break;
            }
            // int count = items.get(0).getProgressCount().intValue();
            // int currentPress = items.get(0).getCurrentProgress().intValue();
            // holder.download_progressBar.setMax(count);
            // holder.download_progressBar.setProgress(currentPress);
        } else {

            holder.btn_down.setText("下载");
            holder.btn_down.setOnClickListener(new MyOnclick(position,
                    holder.btn_down));

        }

        return convertView;
    }

    public void init() {
        for (int i = 0; i < list.size(); i++) {
            selectionMap = new SparseBooleanArray();
            selectionMap.put(i, false);
        }
    }

    private class ViewHolder {
        TextView textView;
        TextView textView2;
        RelativeLayout re_item;
        TextView btn_down;
        ProgressBar download_progressBar;
    }

    class MyOnclick implements OnClickListener {

        int position;
        TextView tv_text;

        public MyOnclick(int position, TextView tv_text) {
            this.position = position;
            this.tv_text = tv_text;
        }

        @Override
        public void onClick(View arg0) {
            tv_text.setText("正在下载");
            items = fb.findAllByWhere(clazz, "movieName='"
                    + list.get(position).getTitle().toString().trim() + "'");
            if (items != null & items.size() > 0) {
                ToastUtils.shortToast( "别戳了！再戳就破啦...");
            } else {
                Intent i = new Intent(context, DownloadService.class);
                DownFileItemInfo d = new DownFileItemInfo();
                d.setDownloadUrl(list.get(position).getDownloadurl());
                d.setFileSize(list.get(position).getFilesize());
                d.setMovieName(list.get(position).getTitle().toString().trim());
                d.setDownloadState(DOWNLOAD_STATE_WATTING);
                i.putExtra(SERVICE_TYPE_NAME, START_DOWNLOAD_MOVIE);
                // i.putExtra(DOWNLOAD_TAG_BY_INTENT, d);
                i.putExtra(DOWNLOAD_TAG_BY_INTENT, d);
                context.startService(i);

            }

        }

    }
}
