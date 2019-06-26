package com.xywy.askforexpert.module.consult.adapter.delegate;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.model.consultentity.ConsultChatEntity;
import com.xywy.askforexpert.model.consultentity.OnlineConsultChatEntity;
import com.xywy.component.uimodules.photoPicker.PhotoPreviewActivity;
import com.xywy.component.uimodules.photoPicker.model.PhotoInfo;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;

/**
 * Created by zhangzheng on 2017/4/28.
 */

public class OnlineConsultChatQuestionDescDelegate implements ItemViewDelegate<OnlineConsultChatEntity> {
    private Context context;
    private boolean isPatient;

    public OnlineConsultChatQuestionDescDelegate(Context context,boolean isPatient) {
        this.context = context;
        this.isPatient = isPatient;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_chat_question_desc;
    }

    @Override
    public boolean isForViewType(OnlineConsultChatEntity item, int position) {
        return item.getType() == ConsultChatEntity.TYPE_QUESTION_DESC;
    }

    @Override
    public void convert(ViewHolder holder, final OnlineConsultChatEntity consultChatEntity, final int position) {
        holder.setText(R.id.tv_chat_patient_info, consultChatEntity.getPatient_name()+
                ","+consultChatEntity.getPatient_sex()+
                ","+consultChatEntity.getPatient_age());
        if (consultChatEntity.getHospital().equals("0")){
            holder.setText(R.id.is_visit_tv, "是否就诊过：未就诊");
        }else{
            holder.setText(R.id.is_visit_tv, "是否就诊过：就诊过");
        }
        TextView price_tv = holder.getView(R.id.price_tv);
        price_tv.setText(consultChatEntity.getAmount());

        holder.setText(R.id.tv_chat_title, "标题："+consultChatEntity.getTop_title());
        holder.setText(R.id.description_tv, "描述："+consultChatEntity.getContent());
        if (isPatient){
            holder.setVisible(R.id.intent_tv,false);
        }else{
            holder.setText(R.id.intent_tv, "目的："+consultChatEntity.getIntent());
        }
        View pic_root = holder.getView(R.id.pic_root);
        if (null != consultChatEntity.getImg() && consultChatEntity.getImg().size() != 0){
            LinearLayout pic_ll = holder.getView(R.id.pic_ll);
            pic_ll.removeAllViews();
            final ArrayList<PhotoInfo> photoInfos = new ArrayList<>();
            for (final String picPath : consultChatEntity.getImg()){
                PhotoInfo info = new PhotoInfo();
                info.setNetWorkUrl(picPath);
                photoInfos.add(info);
                View picLayout = View.inflate(context,R.layout.online_im_title_pic_layout,null);
                ImageView im_title_pic = (ImageView) picLayout.findViewById(R.id.im_title_pic);
                im_title_pic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Intent intent = new Intent(context, PhotoInfoActivity.class);
//                        intent.putExtra("url",picPath);
//                        context.startActivity(intent);
                        PhotoPreviewActivity.startActivity(context, photoInfos, position,true);

                    }
                });
//                XYImageLoader.getInstance().displayImage(picPath, im_title_pic);
                pic_ll.addView(picLayout);
                Glide.with(context).load(picPath).into(im_title_pic);
            }
        }else{
            pic_root.setVisibility(View.GONE);
        }



    }
}
