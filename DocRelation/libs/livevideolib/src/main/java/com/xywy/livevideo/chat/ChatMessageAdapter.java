package com.xywy.livevideo.chat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xywy.livevideo.chat.model.LiveChatContent;
import com.xywy.livevideolib.R;
import com.xywy.uilibrary.recyclerview.adapter.XYWYRVMultiTypeBaseAdapter;
import com.xywy.util.ImageLoaderUtils;
import com.xywy.util.L;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2017/2/24 10:28
 */
public class ChatMessageAdapter extends XYWYRVMultiTypeBaseAdapter<LiveChatContent> {
    public ChatMessageAdapter(Context context) {
        super(context);
        addItemViewDelegate(new ChatMessageItemDelegate());
    }
}

class ChatMessageItemDelegate implements ItemViewDelegate<LiveChatContent> {
    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_chat_msg;
    }

    @Override
    public boolean isForViewType(LiveChatContent item, int position) {
        return true;
    }

    @Override
    public void convert(final ViewHolder holder, final LiveChatContent msg, final int position) {
        holder.getConvertView().setTag(msg);
        String from = msg.name + ":";
        CharSequence content = msg.content;
        final SpannableStringBuilder builder = new SpannableStringBuilder(from + content);

//ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
        ForegroundColorSpan whiteSpan = new ForegroundColorSpan(Color.WHITE);
        ForegroundColorSpan blueSpan = new ForegroundColorSpan(Color.parseColor("#0dc3ce"));

        builder.setSpan(blueSpan, 0, from.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        builder.setSpan(whiteSpan, from.length(), (from + content).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (msg.imageurl != null) {

            ImageLoaderUtils.getInstance().loadImage(msg.imageurl, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    L.e("onLoadingStarted",imageUri);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    L.e("onLoadingFailed",failReason.getCause().getMessage());
                    Drawable drawable = holder.getConvertView().getContext().getResources().getDrawable(R.drawable.fix_bitrate);
                    BitmapDrawable bd = (BitmapDrawable) drawable;
                    Bitmap bm = bd.getBitmap();
                    display(bm);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    if (holder.getConvertView().getTag()!=msg){
                        return;
                    }
                    display(loadedImage);

                }

                private void display(Bitmap loadedImage) {
                    int oldLength = builder.length();
                    String icon = "icon";
                    builder.append(icon);
                    ImageSpan imgSpan = new ImageSpan(loadedImage);
                    builder.setSpan(imgSpan, oldLength, oldLength + icon.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    TextView tv = holder.getView(R.id.tv_msg);
                    tv.setText(builder);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    L.e("onLoadingCancelled",imageUri);
                }
            });

        }
            TextView tv = holder.getView(R.id.tv_msg);
            tv.setText(builder);
    }
}
