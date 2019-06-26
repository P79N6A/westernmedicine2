package com.xywy.askforexpert.module.message;

import android.view.View;
import android.widget.ImageView;

import com.xywy.askforexpert.R;

/**
 * Compiler: Android Studio
 * Project: DocRelation
 * Author: Jack Fang
 * Email: fangqi@xywy.com
 * Date: 2016/8/1 10:23
 */
public class MsgTutorialFragment extends BaseCustomViewDialogFragment implements View.OnClickListener {

    private ImageView tutorial0;
    private ImageView tutorial1;
    private ImageView tutorialBtn;

    public MsgTutorialFragment() {
    }

    public static MsgTutorialFragment newInstance(int contentViewLayoutId) {
        MsgTutorialFragment fragment = new MsgTutorialFragment();
        setArgs(fragment, contentViewLayoutId);

        return fragment;
    }

    protected void initViews(View contentView) {
        if (contentView != null) {
            tutorial0 = (ImageView) contentView.findViewById(R.id.msg_tutorial_0);
            tutorial1 = (ImageView) contentView.findViewById(R.id.msg_tutorial_1);
            tutorialBtn = (ImageView) contentView.findViewById(R.id.msg_tutorial_next);

            tutorial1.setVisibility(View.GONE);
            tutorialBtn.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.msg_tutorial_next:
                if (tutorial1.getVisibility() == View.GONE) {
                    tutorial0.setVisibility(View.GONE);
                    tutorial1.setVisibility(View.VISIBLE);
                    tutorialBtn.setImageResource(R.drawable.msg_tutorial_dismiss);
                } else {
                    getDialog().dismiss();
                }
                break;

            default:
                break;
        }
    }
}
