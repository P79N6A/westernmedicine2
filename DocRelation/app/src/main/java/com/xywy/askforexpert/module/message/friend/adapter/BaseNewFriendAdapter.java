package com.xywy.askforexpert.module.message.friend.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.xywy.askforexpert.BuildConfig;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.ACache;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.logictools.ResolveJson;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.AddressBook;
import com.xywy.askforexpert.model.NewCardInfo;
import com.xywy.easeWrapper.EMChatManager;
import com.xywy.easeWrapper.db.InviteMessage;
import com.xywy.easeWrapper.db.InviteMessgeDao;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 添加好友
 *
 * @author 王鹏
 * @2015-5-30下午4:37:02
 */
@SuppressLint("ResourceAsColor")
public class BaseNewFriendAdapter extends BaseAdapter {

    public SparseBooleanArray selectionMap;
    List<InviteMessage> list;
    List<NewCardInfo> newlist;
    InviteMessgeDao inviteMessgeDao;
    FinalBitmap fb;
    String uid = "";
    ImageLoader instance;
    DisplayImageOptions options;
    private Context context;
    private LayoutInflater inflater;
    private AddressBook pListInfo;
    private ACache mCache;

    public BaseNewFriendAdapter(Context context) {
        this.context = context;
        mCache = ACache.get(context);
        inflater = LayoutInflater.from(context);
        inviteMessgeDao = new InviteMessgeDao();
        fb = FinalBitmap.create(context, false);
        if (!YMUserService.isGuest()) {
            if (YMApplication.getLoginInfo() != null) {
                uid = YMApplication.getLoginInfo().getData().getPid();
            }

        }
        options = new DisplayImageOptions.Builder()
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .showStubImage(R.drawable.icon_photo_def)
                .showImageForEmptyUri(R.drawable.icon_photo_def)
                .showImageOnFail(R.drawable.icon_photo_def).cacheInMemory(true)
                .cacheOnDisc(true).build();
        instance = ImageLoader.getInstance();
    }

    public void setData(List<InviteMessage> list, List<NewCardInfo> newlist) {
        this.list = list;
        this.newlist = newlist;
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
            convertView = inflater.inflate(R.layout.newfriend_item, parent, false);
            holder.textView = (TextView) convertView.findViewById(R.id.tv_name);
            // holder.re_item = (RelativeLayout) convertView
            // .findViewById(R.id.re_item);
            holder.btn_add = (TextView) convertView.findViewById(R.id.btn_add);
            holder.tv_reason = (TextView) convertView
                    .findViewById(R.id.tv_reason);
            holder.btn_cancle = (TextView) convertView
                    .findViewById(R.id.btn_cancle);
            holder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.btn_add.setOnClickListener(new MyOnclick(position));
        holder.btn_cancle.setOnClickListener(new MyOnclick(position));
        if (newlist != null && newlist.size() > 0 && position >= 0 && position < newlist.size()) {
            String username = newlist.get(position).getNickname();

            if (TextUtils.isEmpty(username)) {
                String tochatname = list.get(position).getFrom();
                tochatname = tochatname.replaceAll("did_", "");
                tochatname = tochatname.replaceAll("uid_", "");
                username = "用户" + tochatname;
            }
            holder.textView.setText(username + "   "
                    + newlist.get(position).getJob());
            String sex;
            if ("0".equals(newlist.get(position).getSex())) {
                sex = "女";
            } else {
                sex = "男";
            }
            holder.tv_reason.setText(newlist.get(position).getSubject() + " "
                    + newlist.get(position).getHospital());
            // fb.display(holder.avatar, newlist.get(position).getPhoto());
            instance.displayImage(newlist.get(position).getPhoto(),
                    holder.avatar, options);
        }
        return convertView;
    }

    private class ViewHolder {
        TextView textView;
        RelativeLayout re_item;
        TextView tv_reason;
        TextView btn_add;
        TextView btn_cancle;
        ImageView avatar;
    }

    class MyOnclick implements OnClickListener {
        int position;

        public MyOnclick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            switch (arg0.getId()) {
                case R.id.btn_add:
                    String str = list
                            .get(position)
                            .getFrom()
                            .substring(
                                    list.get(position).getFrom().indexOf("_") + 1,
                                    list.get(position).getFrom().length());
                    sendAddFriend(str, position);
                    // EMChatManager.getInstance().acceptInvitation(
                    // list.get(position).getFrom());
                    // inviteMessgeDao.deleteMessage(list.get(position).getFrom());
                    notifyDataSetChanged();

                    break;
                case R.id.btn_cancle:

                    new Thread(new Runnable() {
                        public void run() {

                            try {

                                EMChatManager.getInstance().refuseInvitation(
                                        list.get(position).getFrom());

                                ((Activity) context).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        inviteMessgeDao.deleteMessage(list.get(
                                                position).getFrom());
                                        newlist.remove(position);
                                        setData(inviteMessgeDao.getMessagesList(),
                                                newlist);
                                        notifyDataSetChanged();
                                        YMApplication.isrefresh = true;
                                        // list.get(position).setStatus(
                                        // InviteMesageStatus.REFUSED);
                                        // // 更新db
                                        // ContentValues values = new
                                        // ContentValues();
                                        // values.put(
                                        // InviteMessgeDao.COLUMN_NAME_STATUS,
                                        // list.get(position).getStatus()
                                        // .ordinal());
                                        // inviteMessgeDao.updateMessage(
                                        // list.get(position).getId(), values);
                                        // T.showNoRepeatShort(context, "");
                                    }
                                });
                            } catch (final Exception e) {
                                ((Activity) context).runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {

                                        ToastUtils.shortToast("异常");
                                    }
                                });

                            }
                        }
                    }).start();
                    notifyDataSetChanged();
                    break;

                default:
                    break;
            }
        }

    }

    public void Doit(final int position) {
        new Thread(new Runnable() {
            public void run() {
                // 调用sdk的同意方法
                try {

                    EMChatManager.getInstance().acceptInvitation(
                            list.get(position).getFrom());

                    ((Activity) context).runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            // list.get(position).setStatus(
                            // InviteMesageStatus.AGREED);
                            // // 更新db
                            // ContentValues values = new
                            // ContentValues();
                            // values.put(
                            // InviteMessgeDao.COLUMN_NAME_STATUS,
                            // list.get(position).getStatus()
                            // .ordinal());
                            // inviteMessgeDao.updateMessage(
                            // list.get(position).getId(), values);

                            if (newlist != null && newlist.size() > 0
                                    && list != null && list.size() > 0
                                    && position >= 0 && position < list.size() && position < newlist.size()) {
                                inviteMessgeDao.deleteMessage(list.get(position).getFrom());
                                newlist.remove(position);
                            }
                            setData(inviteMessgeDao.getMessagesList(), newlist);
                            notifyDataSetChanged();
                            YMApplication.isrefresh = true;
                        }
                    });
                } catch (final Exception e) {
                    ((Activity) context).runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            ToastUtils.shortToast("异常");
                        }
                    });

                }
            }
        }).start();

    }

    /**
     * 添加好友发送成功
     */
    public void sendAddCardText(String tochatname, String realname,
                                String head_img) {
        EMConversation conversation = EMChatManager.getInstance()
                .getConversation(tochatname);
        EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
        // 如果是群聊，设置chattype,默认是单聊
        // if (chatType == CHATTYPE_GROUP)
        // message.setChatType(ChatType.GroupChat);
        EMTextMessageBody txtBody = new EMTextMessageBody("我们是好友了，可以聊天了");
        // 设置消息body
        message.addBody(txtBody);
        // 设置要发给谁,用户username或者群聊groupid
        message.setReceipt(tochatname);
        // 把messgage加到conversation中
        conversation.insertMessage(message);
        // 通知adapter有消息变动，adapter会根据加入的这条message显示消息和调用sdk的发送方法

        final long start = System.currentTimeMillis();
        message.setAttribute("fromRealName", YMApplication.getLoginInfo().getData()
                .getRealname());
        message.setAttribute("fromAvatar", YMApplication.getLoginInfo().getData()
                .getPhoto());

        if (TextUtils.isEmpty(realname)) {
            tochatname = tochatname.replaceAll("did_", "");
            tochatname = tochatname.replaceAll("uid_", "");
            message.setAttribute("toRealName", "用户" + tochatname);
        } else {
            message.setAttribute("toRealName", realname);
        }
        message.setAttribute("toAvatar", head_img);

        //stone 新添加的扩展字段区别是医脉搏还是用药助手
        if (YMApplication.APPLICATIONID.equals(BuildConfig.APPLICATION_ID)) {
            message.setAttribute("source", "selldrug");
        }
        // 调用sdk发送异步发送方法
        EMChatManager.getInstance().sendMessage(message, new EMCallBack() {

            @Override
            public void onSuccess() {
                // umeng自定义事件，
                // sendEvent2Umeng(message, start);
                //
                // updateSendedView(message, holder);
            }

            @Override
            public void onError(int code, String error) {
                // sendEvent2Umeng(message, start);
                //
                // updateSendedView(message, holder);
            }

            @Override
            public void onProgress(int progress, String status) {
            }

        });

    }

    public void sendAddFriend(final String touserid, final int position) {
        Doit(position);
        if (list != null && list.size() > 0 && position >= 0 && position < list.size() && position < newlist.size()) {
            sendAddCardText(list.get(position).getFrom(), newlist.get(position).getNickname(), newlist.get(position).getPhoto());
        }
        String userid = YMApplication.getLoginInfo().getData().getPid();
        String bind = userid + touserid;
        String sign = MD5Util.MD5(bind + Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();
        params.put("a", "dcFriend");
        params.put("m", "friend_add");
        params.put("userid", userid);
        params.put("touserid", touserid);
        params.put("bind", bind);
        params.put("sign", sign);

        FinalHttp fh = new FinalHttp();
        fh.post(CommonUrl.Patient_Manager_Url, params,
                new AjaxCallBack() {
                    @Override
                    public void onSuccess(String t) {
                        DLog.i("addfriend", "添加好友" + t);
                        Map<String, String> map = ResolveJson.R_Action(t
                                .toString());
                        // 获取所有会话，包括陌生人
                        Map<String, EMConversation> conversations = EMChatManager
                                .getInstance().getAllConversations();

                        try {
                            synchronized (conversations) {


                                Iterator<Hashtable.Entry<String, EMConversation>> iterator = conversations.entrySet().iterator();

                                while (iterator.hasNext()) {
                                    Hashtable.Entry<String, EMConversation> emConversationEntry = iterator.next();
                                    EMConversation emConversation = emConversationEntry.getValue();
                                    //if (emConversation.getUserName().substring(emConversation.getUserName().lastIndexOf("_") + 1, emConversation.getUserName().length()).equals(touserid)) {
                                    //    EMChatManager.getInstance().deleteConversation(emConversation.getUserName(), true, true);
                                    //}

                                    //added by shijiazi
                                    if (emConversation.getUserName().equals("新朋友")) {
                                        List<EMMessage> messages = emConversation.getAllMessages();
                                        for (EMMessage message : messages) {
                                            if (message.getStringAttribute("toRealName", "").contains(touserid)) {
                                                messages.remove(message);
                                                break;
                                            }
                                        }

                                        if (messages.size() == 0) {
                                            EMChatManager.getInstance().deleteConversation("新朋友", true, true);
                                            break;
                                        }
                                    }
                                }

                            }

//                            getData();
                            ToastUtils.shortToast("已接受");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        super.onSuccess(t);
                    }

                    @Override
                    public void onFailure(Throwable t, int errorNo,
                                          String strMsg) {
                        ToastUtils.shortToast("网络异常");
                        super.onFailure(t, errorNo, strMsg);
                    }
                });
    }

    public void init() {
        for (int i = 0; i < list.size(); i++) {
            selectionMap = new SparseBooleanArray();
            selectionMap.put(i, false);
        }
    }

}
