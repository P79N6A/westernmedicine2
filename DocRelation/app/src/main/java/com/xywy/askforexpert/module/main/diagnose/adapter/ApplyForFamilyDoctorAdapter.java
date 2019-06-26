package com.xywy.askforexpert.module.main.diagnose.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xywy.askforexpert.R;

import net.tsz.afinal.FinalBitmap;

import java.util.List;


public class ApplyForFamilyDoctorAdapter extends RecyclerView.Adapter<ApplyForFamilyDoctorAdapter.ViewHolder> {
    public boolean isAddShow = true;
    // public SparseBooleanArray selectionMap;
    private int maxsize;
    private FinalBitmap fb;
    private Context context;
//    private SDCardImageLoader loader;

    private List<String> mDatas;

    private LayoutInflater mInflater;

    protected OnItemClickListener mOnItemClickListener;


    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
        }

        ImageView imageView;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return mDatas.size() == maxsize ? mDatas.size()
                : mDatas.size() + 1;
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = mInflater.inflate(R.layout.photo_grid_item,
                viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);

        viewHolder.imageView = (ImageView) view
                .findViewById(R.id.idcard_gridView_item_photo);

        setListener(viewGroup, viewHolder, viewType, view);
        return viewHolder;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        if (position == mDatas.size()) {
            if (isAddShow) {
                viewHolder.imageView.setImageBitmap(BitmapFactory.decodeResource(
                        context.getResources(), R.drawable.card_add));
                if (position == maxsize) {
                    viewHolder.imageView.setVisibility(View.GONE);
                }
            } else {
                viewHolder.imageView.setVisibility(View.GONE);
            }

        } else {
            fb.display(viewHolder.imageView, mDatas.get(position));

        }
    }


    public void setData(List<String> imagePathList) {
        this.mDatas = imagePathList;
    }

    public ApplyForFamilyDoctorAdapter(Context context, List<String> datas, int maxSize) {
        this.maxsize = maxSize;
        fb = FinalBitmap.create(context, false);
        mInflater = LayoutInflater.from(context);
        mDatas = datas;
        this.context = context;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, RecyclerView.ViewHolder holder, int position);

        boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    protected void setListener(final ViewGroup parent, final RecyclerView.ViewHolder viewHolder, int viewType, View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    mOnItemClickListener.onItemClick(v, viewHolder, position);
                }
            }
        });

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    return mOnItemClickListener.onItemLongClick(v, viewHolder, position);
                }
                return false;
            }
        });
    }

}