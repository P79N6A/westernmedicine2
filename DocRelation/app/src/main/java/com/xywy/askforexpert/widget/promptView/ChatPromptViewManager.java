package com.xywy.askforexpert.widget.promptView;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.widget.CustomPromptView;

/**
 * Created by chenpengfei on 2016/11/2.
 */
public class ChatPromptViewManager extends PromptViewHelper.PromptViewManager {

    public ChatPromptViewManager(Activity activity, String[] dataArray, Location location) {
        super(activity, dataArray, location);
    }

    public ChatPromptViewManager(Activity activity, Location location) {
        this(activity, new String[]{"复制"}, location);
    }

    @Override
    public View inflateView() {
//      return new PromptView(activity);
        return new CustomPromptView(activity);
    }

    @Override
    public void bindData(View view, String[] dataArray) {
//        if (view instanceof PromptView) {
//            PromptView promptView = (PromptView) view;
//            promptView.setContentArray(dataArray);
//            promptView.setOnItemClickListener(new PromptView.OnItemClickListener() {
//                @Override
//                public void onItemClick(int position) {
//                    if (onItemClickListener != null) onItemClickListener.onItemClick(position);
//                }
//            });
//        }
        ((TextView) view.findViewById(R.id.tv_copy)).setText(dataArray[0]);
        view.findViewById(R.id.tv_copy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) onItemClickListener.onItemClick(0);
            }
        });
    }
}
