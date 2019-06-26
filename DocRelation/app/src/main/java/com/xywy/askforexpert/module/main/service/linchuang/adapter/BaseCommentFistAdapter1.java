package com.xywy.askforexpert.module.main.service.linchuang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.DialogUtil;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.YMOtherUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.logictools.ResolveJson;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.CommentFistInfo;
import com.xywy.askforexpert.module.main.service.linchuang.CommentInfoActivity1;
import com.xywy.askforexpert.widget.view.MyListView;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 4.1 资讯评论
 *
 * @author 王鹏
 * @2015-8-3下午3:10:14
 */
public class BaseCommentFistAdapter1 extends BaseAdapter {

    public Map<String, String> map;
    private Context context;
    private LayoutInflater inflater;
    private List<CommentFistInfo> list;
    private FinalBitmap fb;
    private String tag;
    private List<HashMap<String, Object>> secondlist = new ArrayList<>();
    private String Url;

    public BaseCommentFistAdapter1(Context context, String tag) {
        // TODO Auto-generated constructor stub
        this.context = context;
        inflater = LayoutInflater.from(context);

        fb = FinalBitmap.create(context, false);
        this.tag = tag;
        // this.onClickListener = onClickListener;
        // this.onItemClickListener = onItemClickListener;
    }

    // 显示虚拟键盘
    public static void ShowKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);

    }

    public void setData(List<CommentFistInfo> list) {
        this.list = list;
        if (list != null) {
            init();
        }

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.comment_list_item, parent, false);
            holder.tv_content = (TextView) convertView
                    .findViewById(R.id.tv_content);
            holder.tv_commt = (TextView) convertView
                    .findViewById(R.id.tv_reply);
            holder.tv_praise = (TextView) convertView
                    .findViewById(R.id.tv_praise);
            holder.tv_docname = (TextView) convertView
                    .findViewById(R.id.tv_docname);
            holder.list_second = (MyListView) convertView
                    .findViewById(R.id.list_second);
            holder.tv_redate = (TextView) convertView
                    .findViewById(R.id.tv_redate);
            holder.head_img = (ImageView) convertView
                    .findViewById(R.id.head_img);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // if (list.get(position).getSecond_list() != null
        // && list.get(position).getSecond_list().size() > 0)
        // {
        // holder.list_second.setVisibility(View.VISIBLE);
        // } else
        // {
        // holder.list_second.setVisibility(View.GONE);
        //
        // }
        if (list != null) {
            DLog.d("=========", "==== " + list.get(position).getContent());
            holder.tv_content.setText(list.get(position).getContent());
            holder.tv_docname.setText(list.get(position).getDoc().getName());
            holder.tv_redate.setText(list.get(position).getNomaldate());
            fb.display(holder.head_img, list.get(position).getDoc().getPhoto());
            fb.configLoadfailImage(R.drawable.icon_photo_def);
            fb.configLoadingImage(R.drawable.icon_photo_def);
            String praisenum = list.get(position).getPraiseNum();
            String commnum = list.get(position).getCommentNum();
            holder.tv_praise.setText("赞(" + praisenum + ")");
            holder.tv_commt.setText("回复(" + commnum + ")");
        }

        // convertView.setTag(position, list.get(position).getSecond_list());
        if (list != null) {
            if (list.get(position).getSecond_list() != null
                    && list.get(position).getSecond_list().size() > 0) {

                holder.list_second.setVisibility(View.VISIBLE);
                holder.adapter = new BaseCommentSecondAdapter(context, list.get(
                        position).getSecond_list());
                holder.list_second.setAdapter(holder.adapter);

                holder.list_second
                        .setOnItemClickListener(new MyItemOnclik(position));
                // holder.adapter.notifyDataSetChanged();
            } else {
                holder.list_second.setVisibility(View.GONE);
            }
        }

        holder.tv_praise.setOnClickListener(new MyOnclick(position, "praise"
        ));
        holder.tv_commt.setOnClickListener(new MyOnclick(position, "commt"
        ));

        return convertView;
    }

    public void setData(AjaxParams params, final int position) {
        FinalHttp ft = new FinalHttp();
        ft.post(Url, params, new AjaxCallBack() {
            @Override
            public void onSuccess(String t) {
                // TODO Auto-generated method stub
                map = ResolveJson.R_Action(t.toString());
                // handler.sendEmptyMessage(200);
                if ("0".equals(map != null ? map.get("code") : null)) {
                    int num = Integer
                            .valueOf(list.get(position).getPraiseNum());
                    num = num + 1;
                    list.get(position).setPraiseNum(num + "");
                    notifyDataSetChanged();
                }
                if ("1".equals(map != null ? map.get("code") : null)) {
                    int num = Integer
                            .valueOf(list.get(position).getPraiseNum());
                    num = num + 1;
                    list.get(position).setPraiseNum(num + "");
                    notifyDataSetChanged();
                }
                ToastUtils.shortToast( map != null ? map.get("msg") : null);
                super.onSuccess(t);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                // TODO Auto-generated method stub
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }

    public void init() {

        for (int i = 0; i < list.size(); i++) {
            HashMap<String, Object> map = new HashMap<>();
            map.put(i + "", list.get(i).getSecond_list());
            secondlist.add(map);
        }
    }

    private class MyOnclick implements OnClickListener {
        private int position;
        private String type;

        public MyOnclick(int position, String type) {
            this.position = position;
            this.type = type;
        }

        @Override
        public void onClick(View arg0) {
            if ("commt".equals(type)) {
                CommentInfoActivity1.et_sendmmot.setHint("回复"
                        + list.get(position).getDoc().getName() + "的评论");

                CommentInfoActivity1.rl_bottom.setVisibility(View.VISIBLE);
                CommentInfoActivity1.rl_menu.setVisibility(View.GONE);
                CommentInfoActivity1.rl_bottom_tiez.setVisibility(View.GONE);
                CommentInfoActivity1.et_sendmmot.requestFocus();
                CommentInfoActivity1.et_sendmmot.setFocusableInTouchMode(true);
                CommentInfoActivity1.et_sendmmot.setFocusable(true);
                ShowKeyboard(CommentInfoActivity1.et_sendmmot);
                // String sign =
                // MD5Util.MD5(CommentInfoActivity1.et_sendmmot.getText()
                // .toString() + YMApplication.md5Key);

                map = new HashMap<>();
                map.put("c", "comment");
                map.put("a", "comment");
                if (YMUserService.isGuest()) {
                    DialogUtil.LoginDialog(new YMOtherUtils(context).context);
                } else {
                    map.put("userid", YMApplication.getLoginInfo().getData().getPid());

                }
                map.put("toUserid", list.get(position).getDoc().getId());
                map.put("pid", list.get(position).getId());
                // map.put("sign", sign);
                CommentInfoActivity1.isTiez = false;
                CommentInfoActivity1.et_sendmmot.setText("");
            } else if ("praise".equals(type)) {
                if ("consult".equals(tag)) {
                    if (YMUserService.isGuest()) {
                        DialogUtil.LoginDialog(new YMOtherUtils(context).context);
                    } else {
                        Url = CommonUrl.Consulting_Url;
                        String userid = YMApplication.getLoginInfo().getData().getPid();
                        String sign = MD5Util.MD5(userid
                                + list.get(position).getId()
                                + Constants.MD5_KEY);
                        AjaxParams params = new AjaxParams();
                        params.put("a", "actionAdd");
                        params.put("id", list.get(position).getId());
                        params.put("userid", userid);
                        params.put("type", "2");
                        params.put("c", "praise");
                        params.put("sign", sign);
                        setData(params, position);
                    }
                } else if ("guide".equals(tag)) {
                    if (YMUserService.isGuest()) {
                        DialogUtil.LoginDialog(new YMOtherUtils(context).context);
                    } else {
                        Url = CommonUrl.Codex_Url;
                        String userid = YMApplication.getLoginInfo().getData().getPid();
                        String sign = MD5Util
                                .MD5(userid + Constants.MD5_KEY);
                        AjaxParams params = new AjaxParams();
                        params.put("a", "praise");
                        params.put("id", list.get(position).getId());
                        params.put("userid", userid);
                        params.put("type", "2");
                        params.put("c", "comment");
                        params.put("sign", sign);
                        setData(params, position);
                    }
                }

            }
        }

    }



    private class MyItemOnclik implements OnItemClickListener {
        private int position;

        public MyItemOnclik(int position) {
            this.position = position;
        }

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
            CommentInfoActivity1.et_sendmmot.setHint("回复"
                    + list.get(position).getSecond_list().get(arg2).getDoc()
                    .getName() + "的评论");

            // String sign
            CommentInfoActivity1.rl_bottom.setVisibility(View.VISIBLE);
            CommentInfoActivity1.rl_menu.setVisibility(View.GONE);
            CommentInfoActivity1.rl_bottom_tiez.setVisibility(View.GONE);
            CommentInfoActivity1.et_sendmmot.requestFocus();
            CommentInfoActivity1.et_sendmmot.setFocusableInTouchMode(true);
            CommentInfoActivity1.et_sendmmot.setFocusable(true);
            ShowKeyboard(CommentInfoActivity1.et_sendmmot);

            map = new HashMap<>();
            map.put("c", "comment");
            map.put("a", "comment");
            if (YMUserService.isGuest()) {
                DialogUtil.LoginDialog(new YMOtherUtils(context).context);
            } else {
                map.put("userid", YMApplication.getLoginInfo().getData().getPid());
            }
            map.put("toUserid", list.get(position).getSecond_list().get(arg2)
                    .getDoc().getId());
            map.put("pid", list.get(position).getId());
            // map.put("themeid", "35511");
            // map.put("sign", sign);
            CommentInfoActivity1.isTiez = false;
            CommentInfoActivity1.et_sendmmot.setText("");
        }

    }

    private class ViewHolder {
        TextView tv_content;
        ImageView head_img;
        TextView tv_redate;
        TextView tv_docname;
        MyListView list_second;

        TextView tv_praise;
        TextView tv_commt;
        BaseCommentSecondAdapter adapter;
    }

}
