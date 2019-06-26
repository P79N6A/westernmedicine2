package com.xywy.askforexpert.module.message.friend;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.xywy.askforexpert.BuildConfig;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.logictools.ResolveJson;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.easeWrapper.EMChatManager;
import com.xywy.easeWrapper.db.InviteMessgeDao;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.Map;

/**
 * 验证信息
 *
 * @author 王鹏
 * @2015-6-3下午6:24:55
 */
public class NewCardInfoActivity extends Activity {
    FinalBitmap fb;
    InviteMessgeDao inviteMessgeDao;
    private String head_img;
    private String sex;
    private String subject;
    private String reason;
    private String realname;
    private String hx_usernam;
    private String hospital;
    private String job;
    private ImageView image;
    private TextView tv_username, tv_sex, tv_ages, tv_info, tv_person_info;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_card_info);

        CommonUtils.initSystemBar(this);
        if (!YMUserService.isGuest()) {
            if (YMApplication.getLoginInfo() != null) {
                uid = YMApplication.getLoginInfo().getData().getPid();
            }

        }
        inviteMessgeDao = new InviteMessgeDao();
        fb = FinalBitmap.create(NewCardInfoActivity.this, false);
        head_img = getIntent().getStringExtra("head_img");
        sex = getIntent().getStringExtra("sex");
        subject = getIntent().getStringExtra("subject");
        reason = getIntent().getStringExtra("reason");
        hx_usernam = getIntent().getStringExtra("hx_usernam").toString();
        realname = getIntent().getStringExtra("realname");
        job = getIntent().getStringExtra("job");
        hospital = getIntent().getStringExtra("hospital");
        tv_ages = (TextView) findViewById(R.id.ages);
        image = (ImageView) findViewById(R.id.user_pic);
        tv_username = (TextView) findViewById(R.id.username);
        tv_sex = (TextView) findViewById(R.id.sex);
        tv_info = (TextView) findViewById(R.id.info);
        tv_person_info = (TextView) findViewById(R.id.person_info);

        fb.display(image, head_img);
        if (TextUtils.isEmpty(realname)) {
            realname = "用户" + hx_usernam.substring(hx_usernam.lastIndexOf("_") + 1, hx_usernam.length());
            tv_username.setText(realname + "  " + job);
        } else {
            realname = realname.replaceAll("did_", "");
            realname = realname.replaceAll("uid_", "");
            tv_username.setText(realname + "  " + job);
        }

        tv_sex.setText(subject);
        tv_info.setText(hospital);
        tv_person_info.setText(reason);
    }

    public void onClick_back(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_add_newfriend:
                String str = hx_usernam.substring(hx_usernam.indexOf("_") + 1,
                        hx_usernam.length());
                sendAddFriend(str);
                new Thread(new Runnable() {
                    public void run() {
                        // 调用sdk的同意方法
                        try {

                            EMChatManager.getInstance()
                                    .acceptInvitation(hx_usernam);

                            NewCardInfoActivity.this.runOnUiThread(new Runnable() {

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

                                    inviteMessgeDao.deleteMessage(hx_usernam);
                                    YMApplication.isrefresh = true;
                                    sendAddCardText(hx_usernam);
                                    finish();
                                }
                            });
                        } catch (final Exception e) {
                            NewCardInfoActivity.this.runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                    ToastUtils.shortToast(
                                            "异常");
                                }
                            });

                        }
                    }
                }).start();

                break;
            case R.id.btn_cancle_newfriend:
                new Thread(new Runnable() {
                    public void run() {

                        try {

                            EMChatManager.getInstance()
                                    .refuseInvitation(hx_usernam);

                            NewCardInfoActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    inviteMessgeDao.deleteMessage(hx_usernam);
                                    YMApplication.isrefresh = true;
                                    finish();
                                    ToastUtils.shortToast(
                                            "已拒绝");
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
                                    // T.shortToast( "");
                                }
                            });
                        } catch (final Exception e) {
                            NewCardInfoActivity.this.runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                    ToastUtils.shortToast(
                                            "异常");
                                }
                            });

                        }
                    }
                }).start();

                break;

            default:
                break;
        }
    }

    /**
     * 添加好友发送成功
     */
    public void sendAddCardText(String tochatname) {
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
            realname = hx_usernam.replaceAll("did_", "");
            realname = hx_usernam.replaceAll("uid_", "");
            message.setAttribute("toRealName", "用户" + realname);
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

    public void sendAddFriend(String touserid) {
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
                        Map<String, String> map = ResolveJson.R_Action(t
                                .toString());

                        // if (map.get("code").equals("0"))
                        // {
                        ToastUtils.shortToast("已接受");
                        //
                        // } else
                        // {
                        // T.showNoRepeatShort(NewCardInfoActivity.this,
                        // map.get("msg"));
                        //
                        // }

                        super.onSuccess(t);
                    }

                    @Override
                    public void onFailure(Throwable t, int errorNo,
                                          String strMsg) {
                        super.onFailure(t, errorNo, strMsg);
                    }
                });
    }

    @Override
    protected void onDestroy() {


        super.onDestroy();
    }
}
