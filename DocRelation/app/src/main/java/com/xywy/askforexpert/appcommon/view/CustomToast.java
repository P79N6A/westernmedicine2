package com.xywy.askforexpert.appcommon.view;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.xywy.askforexpert.R;

import static com.letv.adlib.model.services.CommonAdDataService.getApplicationContext;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2017/1/3 13:06
 */

public class CustomToast {
    private static Toast toast = null;
    private static TextView title=null;
    public  static  void showSignName(Activity context, String content,int leftDrawable){
        Drawable drawable=initView(context,content,leftDrawable);
        title.setCompoundDrawables(drawable,null,null,null);
        toast.show();
    }

    @NonNull
    private static Drawable initView(Activity context, String content,int topDrawble) {
        if (toast==null){
            LayoutInflater inflater = context.getLayoutInflater();
            View layout = inflater.inflate(R.layout.custom_toast,
                    null);
            title = (TextView) layout.findViewById(R.id.tv_toast);
            toast = new Toast(getApplicationContext());
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
        }
        title.setText(content);
        Drawable drawable=context.getResources().getDrawable(topDrawble);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        return drawable;
    }

    public  static  void showAlreadySigned(Activity context, String content,int topDrawble){
        Drawable drawable=initView(context,content,topDrawble);
        title.setCompoundDrawables(null,drawable,null,null);
        toast.show();
    }
}
