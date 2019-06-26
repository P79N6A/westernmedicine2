package com.xywy.askforexpert.module.main.prelaunch.follow;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.xywy.askforexpert.R;
import com.xywy.uilibrary.titlebar.ItemClickListener;

import java.util.LinkedList;
import java.util.List;

/**
 * 关注医院 ViewPager 适配器
 * Created by bailiangjin on 16/10/26.
 */
public class FollowPagerAdapter extends PagerAdapter {


    //显示的数据

    private List<View> mViewCache = null;

    private LayoutInflater mLayoutInflater = null;
    private Context context;

    private Handler handler;
    ItemClickListener btnNextClick;
    ItemClickListener btnJoinClick;


    public FollowPagerAdapter(Context context, Handler handler, ItemClickListener btnNextClick, ItemClickListener btnJoinClick) {
        this.context = context;
        this.handler = handler;
        this.btnNextClick = btnNextClick;
        this.btnJoinClick = btnJoinClick;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mViewCache = new LinkedList<>();
    }


    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View contentView = (View) object;
        container.removeView(contentView);
        this.mViewCache.add(contentView);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View convertView;

        convertView = this.mLayoutInflater.inflate(position == 0 ? R.layout.dialog_follow_first : R.layout.dialog_follow_second, null, false);

        if (0 == position) {
            Button btn_next = (Button) convertView.findViewById(R.id.btn_next);
            btn_next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnNextClick.onClick();
                }
            });
        } else {
            Button btn_join = (Button) convertView.findViewById(R.id.btn_join);
            btn_join.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnJoinClick.onClick();
                }
            });
        }
        container.addView(convertView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        return convertView;
    }


}
