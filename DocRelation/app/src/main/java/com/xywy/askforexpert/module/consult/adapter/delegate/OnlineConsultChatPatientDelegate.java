package com.xywy.askforexpert.module.consult.adapter.delegate;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

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

public class OnlineConsultChatPatientDelegate implements ItemViewDelegate<OnlineConsultChatEntity> {
    private Context context;

    public OnlineConsultChatPatientDelegate(Context context) {
        this.context = context;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_chat_patient_title;
    }

    @Override
    public boolean isForViewType(OnlineConsultChatEntity item, int position) {
        return item.getType() == ConsultChatEntity.TYPE_PATIENT_TITLE;
    }

    @Override
    public void convert(ViewHolder holder, final OnlineConsultChatEntity consultChatEntity, final int position) {
        holder.setText(R.id.tv_chat_patient_info, consultChatEntity.getPatient_name()+
                " "+consultChatEntity.getPatient_sex()+
                " "+consultChatEntity.getPatient_age());
        holder.setText(R.id.condition_tv, consultChatEntity.getContent());
        holder.setText(R.id.visit_time_tv, consultChatEntity.getVisit_time());
        holder.setText(R.id.description_tv, consultChatEntity.getAdvice());
        holder.setText(R.id.intent_tv, consultChatEntity.getRevisit_time());
        View pic_root = holder.getView(R.id.pic_root);
        if (null != consultChatEntity.getImg() && consultChatEntity.getImg().size() != 0){
            LinearLayout pic_ll = holder.getView(R.id.pic_ll);
            LinearLayout pic_ll2 = holder.getView(R.id.pic_ll2);
            pic_ll.removeAllViews();
            pic_ll2.removeAllViews();
            if (consultChatEntity.getImg().size()>5){
                final ArrayList<PhotoInfo> photoInfos = new ArrayList<>();
                final ArrayList<PhotoInfo> photoInfos2 = new ArrayList<>();
                for (int i=0;i<5;i++){
                    PhotoInfo info = new PhotoInfo();
                    info.setNetWorkUrl(consultChatEntity.getImg().get(i));
                    photoInfos.add(info);
                    View picLayout = View.inflate(context,R.layout.online_im_title_pic_layout,null);
                    ImageView im_title_pic = (ImageView) picLayout.findViewById(R.id.im_title_pic);
                    im_title_pic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                        Intent intent = new Intent(context, PhotoInfoActivity.class);
//                        intent.putExtra("url",picPath);
//                        context.startActivity(intent);
                            PhotoPreviewActivity.startActivity(context, photoInfos, position);

                        }
                    });
                    pic_ll.addView(picLayout);
                    Glide.with(context).load(consultChatEntity.getImg().get(i)).into(im_title_pic);
                }
                for(int i=5;i<consultChatEntity.getImg().size();i++){
                    PhotoInfo info = new PhotoInfo();
                    info.setNetWorkUrl(consultChatEntity.getImg().get(i));
                    photoInfos2.add(info);
                    View picLayout = View.inflate(context,R.layout.online_im_title_pic_layout,null);
                    ImageView im_title_pic = (ImageView) picLayout.findViewById(R.id.im_title_pic);
                    im_title_pic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                        Intent intent = new Intent(context, PhotoInfoActivity.class);
//                        intent.putExtra("url",picPath);
//                        context.startActivity(intent);
                            PhotoPreviewActivity.startActivity(context, photoInfos2, position);

                        }
                    });
                    pic_ll2.addView(picLayout);
                    Glide.with(context).load(consultChatEntity.getImg().get(i)).into(im_title_pic);
                }
            }

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
                        PhotoPreviewActivity.startActivity(context, photoInfos, position);

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
