package com.xywy.askforexpert.module.doctorcircle.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.model.doctor.CommentBean;
import com.xywy.askforexpert.model.doctor.RealNameItem;
import com.xywy.askforexpert.model.doctor.Touser;
import com.xywy.askforexpert.model.doctor.User;
import com.xywy.askforexpert.module.main.service.person.PersonDetailActivity;

import java.util.List;

/**
 * 评论适配器
 *
 * @author LG
 */
public class CommlistAdapter extends BaseAdapter {

    String TAG = "CommlistAdapter";
    ViewHolder mHolder = null;
    private Context context;
    private RealNameItem commlist;
    private MyOnclick myOnclick = new MyOnclick();
    private EditText et_contenct;
    private List<CommentBean> commItemlist;
    private PopupWindow pp;

    public CommlistAdapter() {

    }

    public CommlistAdapter(Context con, RealNameItem RealNameItem) {
        this.context = con;
        this.commlist = RealNameItem;
        commItemlist = commlist.commlist;
    }

    public CommlistAdapter(Context con, List<CommentBean> commentBeen) {
        this.context = con;
        this.commItemlist = commentBeen;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return commItemlist.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return commItemlist.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public View getView(int position, View connView, ViewGroup arg2) {
        if (connView == null) {
            mHolder = new ViewHolder();
            connView = View.inflate(context, R.layout.item_commlist, null);
            mHolder.ll_view = (LinearLayout) connView
                    .findViewById(R.id.ll_view);
            mHolder.tv_user = (TextView) connView.findViewById(R.id.tv_user);
            mHolder.tv_to = (TextView) connView.findViewById(R.id.tv_to);
            mHolder.tv_touser = (TextView) connView.findViewById(R.id.tv_touser);
            mHolder.tv_tocontent = (TextView) connView
                    .findViewById(R.id.tv_tocontent);
            connView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) connView.getTag();
        }
        CommentBean comItem = commItemlist.get(position);


        if (comItem != null) {
            User user = comItem.user;
            Touser touser = comItem.touser;
            if (touser != null) {
                if (TextUtils.isEmpty(touser.userid)) {
                    mHolder.tv_to.setVisibility(View.GONE);
                    mHolder.tv_touser.setVisibility(View.GONE);
                } else {
                    if (!TextUtils.isEmpty(touser.nickname)) {
                        mHolder.tv_touser.setText(touser.nickname + " : ");
                        mHolder.tv_to.setVisibility(View.VISIBLE);
                        mHolder.tv_touser.setVisibility(View.VISIBLE);
                    }
                }

            }
            if (user == null) {
                mHolder.ll_view.setVisibility(View.GONE);
            } else {
                mHolder.ll_view.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(touser.userid)) {
                    mHolder.tv_user.setText(user.nickname + " ");
                    String trsStr = user.nickname + " "
                            + mHolder.tv_to.getText().toString()
                            + touser.nickname + " : ";
                    String contet = trsStr + comItem.content;
                    SpannableString ss = new SpannableString(contet);
                    ss.setSpan(new ForegroundColorSpan(Color.TRANSPARENT),
                            0, trsStr.length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    mHolder.tv_tocontent.setText(ss);

                } else {
                    mHolder.tv_user.setText(user.nickname + " : ");
                    String trsStrUser = user.nickname + " : ";
                    String content2 = trsStrUser + comItem.content;
                    SpannableString ss = new SpannableString(content2);
                    ss.setSpan(new ForegroundColorSpan(Color.TRANSPARENT),
                            0, trsStrUser.length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    mHolder.tv_tocontent.setText(ss);
                }

            }

        }
        mHolder.tv_user.setTag(position);
        mHolder.tv_touser.setTag(position);
        mHolder.tv_touser.setOnClickListener(myOnclick);
        mHolder.tv_user.setOnClickListener(myOnclick);

        return connView;
    }

    class ViewHolder {
        TextView tv_user, tv_to, tv_touser, tv_tocontent;
        LinearLayout ll_view;
    }

    class MyOnclick implements OnClickListener {

        @Override
        public void onClick(View v) {
            CommentBean commListuser = null;
            StatisticalTools.eventCount(context, "yqListCommentName");
            Intent itemIntent = new Intent(context, PersonDetailActivity.class);
            switch (v.getId()) {
                case R.id.tv_user:

                    int userPosition = (Integer) v.getTag();
                    commListuser = commItemlist.get(userPosition);
                    itemIntent.putExtra("uuid", commListuser.user.userid);
                    break;
                case R.id.tv_touser:
                    int toUserPosition = (Integer) v.getTag();
                    commListuser = commItemlist.get(toUserPosition);
                    itemIntent.putExtra("uuid", commListuser.touser.userid);
                    break;

            }

            context.startActivity(itemIntent);
        }

    }

}
