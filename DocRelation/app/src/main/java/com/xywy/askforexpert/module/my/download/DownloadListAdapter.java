package com.xywy.askforexpert.module.my.download;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.DownFileItemInfo;

import net.tsz.afinal.FinalDb;

import java.io.File;
import java.util.List;

/**
 * @author 王鹏
 * @2015-6-10下午2:40:43
 */
@SuppressLint("ResourceAsColor")
public class DownloadListAdapter extends BaseAdapter {
    private static final String TAG = "DownloadListAdapter";
    public OnItemDeleteClickListener listener;
    public boolean isShow = false;
    public SparseBooleanArray spar_map;
    private List<DownFileItemInfo> list;
    private Context context;
    private int mRightWidth = 0;
    private FinalDb fb;
    private Class<DownFileItemInfo> clazz;

    public DownloadListAdapter(Context context) {
        this.context = context;
        try {
            clazz = DownFileItemInfo.class;
            fb = FinalDb.create(context, "coupon.db", true, 2,
                    new FinalDb.DbUpdateListener() {

                        @Override
                        public void onUpgrade(SQLiteDatabase arg0, int arg1,
                                              int arg2) {
                            // TODO Auto-generated method stub
                            DLog.i(TAG, "数据库版本" + arg1 + "新的" + arg2);
                            arg0.execSQL("ALTER TABLE downloadtask  ADD commed default '0'");

                        }
                    });
        } catch (Exception e) {
            DLog.i(TAG, "数据报错");
        }

    }

    public void initView(View view) {
        view.scrollTo(0, 0);
    }

    public void setList(List<DownFileItemInfo> list) {
        this.list = list;
        if (null != list) {
            init();
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.downlist_item, null);
            holder.tvTextView = (TextView) convertView
                    .findViewById(R.id.tv_text);
            holder.cb = (CheckBox) convertView.findViewById(R.id.cb);
            // holder.relRightDelete = (RelativeLayout)
            // convertView.findViewById(R.id.rel_right_delete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
//		String filepath = "";
//		String filename = "";
//		if (list != null)
//		{
//			if (list.size() > 0)
//			{
//				filepath = list.get(position).getFilePath().toString();
//				filename = list.get(position).getMovieName().toString();
//			}
//
//		}
//
//		if (getFile(filename, filepath, position))
//		{
        holder.tvTextView.setText(list.get(position).getMovieName());
//		}
        // if (getFile(filename, filepath, position))
        // {
        // }

        if (isShow) {
            holder.cb.setVisibility(View.VISIBLE);
            boolean ischeck = spar_map.get(position);
            holder.cb.setChecked(ischeck);

        } else {
            holder.cb.setVisibility(View.GONE);
        }
        return convertView;
    }

    public void init() {
        spar_map = new SparseBooleanArray();
        for (int i = 0; i < list.size(); i++) {
            spar_map.put(i, false);
        }
    }

    public boolean getFile(String title, String url, int position) {
        try {
            File f = new File(url);
            if (!f.exists()) {
                DLog.i(TAG, "文件地址没有");
                fb.deleteByWhere(clazz, "movieName='" + title + "'");
                list.remove(position);
                setList(list);
                notifyDataSetChanged();
                return false;
            }

        } catch (Exception e) {
            // TODO: handle exception
            DLog.i(TAG, "数据操作错误日志" + e);
            return false;
        }
        return true;
    }

    public interface OnItemDeleteClickListener {
        void onRightClick(View v, int position);
    }

    class ViewHolder {
        public TextView tvTextView;
        public RelativeLayout relRightDelete;
        public CheckBox cb;
    }

}
