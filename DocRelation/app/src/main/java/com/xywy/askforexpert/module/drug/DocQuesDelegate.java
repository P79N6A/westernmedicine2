package com.xywy.askforexpert.module.drug;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.module.drug.bean.DocQues;
import com.xywy.util.ImageLoaderUtils;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by xugan on 2018/7/5.
 */

public class DocQuesDelegate implements ItemViewDelegate<DocQues> {
    private int type;
    private Context context;
    private OnlineRoomItemFragment onlineRoomItemFragment;
    public DocQuesDelegate(OnlineRoomItemFragment onlineRoomItemFragment, int type, Context context){
        this.type = type;
        this.context = context;
        this.onlineRoomItemFragment = onlineRoomItemFragment;
    }
    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_onlineroom_list;
    }

    @Override
    public boolean isForViewType(DocQues item, int position) {
        return true;
    }

    @Override
    public void convert(ViewHolder holder, final DocQues entity, final int position) {
        if (entity != null) {
            ((TextView) holder.getView(R.id.name)).setText(entity.patient_name);
            if("2".equals(entity.patient_sex)){
                ((TextView) holder.getView(R.id.sex)).setText("女");
            }else if("1".equals(entity.patient_sex)){
                ((TextView) holder.getView(R.id.sex)).setText("男");
            }
            StringBuffer age = new StringBuffer();
            if (!entity.patient_age.equals("0")){
                age.append(entity.patient_age+"岁");
            }
            if (!entity.patient_age_month.equals("0")){
                age.append(entity.patient_age_month+"月");
            }
            if (!entity.patient_age_day.equals("0")){
                age.append(entity.patient_age_day+"天");
            }
            ((TextView) holder.getView(R.id.age)).setText(age);
            ((TextView) holder.getView(R.id.price)).setText(entity.amount + "元");
            ((TextView) holder.getView(R.id.question)).setText(entity.content);
//            ((TextView) holder.getView(R.id.answer)).setText(entity.getSpecification());
            ImageLoaderUtils.getInstance().displayImage(entity.patient_photo, (ImageView) holder.getView(R.id.img));
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss");
            String msgTime = sdf.format(new Date(Long.parseLong(entity.last_time)*1000));
            ((TextView) holder.getView(R.id.time)).setText(msgTime);

            TextView go = holder.getView(R.id.reply);

            if (1==type) {
                go.setText("回复");
            }else if(3==type || type == 4){
                go.setText("接诊");
            } else if(2 == type){
                if("4".equals(entity.status)){
                    go.setText("退款待审核");
                }else if("5".equals(entity.status)){
                    go.setText("退款审核通过");
                }else if("6".equals(entity.status)){
                    go.setText("退款审核未通过");
                }else if("7".equals(entity.status)){
                    go.setText("退款完成");
                }else if("8".equals(entity.status)){
                    go.setText("已完成");
                }else if("11".equals(entity.status)){
                    go.setText("申请退款成功");
                }else if("12".equals(entity.status)){
                    go.setText("申请退款失败");
                }
            }

            View read_view = holder.getView(R.id.read_view);
            read_view.setVisibility(View.VISIBLE);
            if (type==1){
                read_view.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(entity.is_read)&&entity.is_read.equals("0")){
                    read_view.setBackgroundResource(R.drawable.read_view_bg);
                }else{
                    read_view.setBackgroundResource(R.drawable.answer_select_circle_bg_normal);
                }
            }else{
                read_view.setVisibility(View.GONE);
            }
            holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //跳转到聊天页面
                    if (null!=onlineRoomItemFragment) {
                        OnlineChatDetailActivity.startActivity(context, entity.qid, entity.uid, entity.patient_name, false, onlineRoomItemFragment.getType());
                    }
                }
            });

            holder.getView(R.id.opt).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //跳转到聊天页面
                    if (null!=onlineRoomItemFragment) {
                        OnlineChatDetailActivity.startActivity(context, entity.qid, entity.uid, entity.patient_name, false, onlineRoomItemFragment.getType());
                    }
                }
            });

        }
    }
}
