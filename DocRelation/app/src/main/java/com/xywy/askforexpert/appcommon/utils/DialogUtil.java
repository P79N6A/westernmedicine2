package com.xywy.askforexpert.appcommon.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.model.api.BaseResultBean;
import com.xywy.askforexpert.model.doctor.CommentBean;
import com.xywy.askforexpert.model.doctor.RealNameItem;
import com.xywy.askforexpert.module.discovery.medicine.common.MyCallBack;
import com.xywy.askforexpert.module.docotorcirclenew.service.DoctorCircleService;
import com.xywy.askforexpert.module.my.account.LoginActivity;
import com.xywy.askforexpert.module.my.userinfo.ApproveInfoActivity;
import com.xywy.askforexpert.module.my.userinfo.CheckStateActivity;
import com.xywy.askforexpert.module.my.userinfo.PersonInfoActivity;
import com.xywy.util.T;

import rx.Subscriber;

/**
 * 对话框工具类 stone
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2016/11/1 11:15
 */

public class DialogUtil {

    /**
     * 显示完善个人资料
     */
    public static void showFinishInfoDialog(final Activity context) {
        String msgInfo = "";
        if (YMApplication.isDoctor()) {
            msgInfo = context.getString(R.string.doctor_not_all_infors);//需要完善姓名、职称、医院、科室等信息，才能发表实名动态
        } else {
            msgInfo = context.getString(R.string.doctor_student_not_all_infors);//需要完善姓名、学校、专业等信息，才能发表实名动态
        }
        showCompleteInfoDialog(null, context, msgInfo);
//        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
//
//        dialog.setMessage(msgInfo)
//                .setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {//"取消"
//
//                    @Override
//                    public void onClick(DialogInterface arg0, int arg1) {
//                        // TODO Auto-generated method stub
//                        arg0.dismiss();
//                    }
//                }).setPositiveButton("去完善", new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface arg0, int arg1) {
//                // TODO Auto-generated method stub
//                Intent inten = new Intent(YMApplication.getAppContext(), PersonInfoActivity.class);
//                inten.putExtra("doctorInfo", "doctorInfo");
//                context.startActivity(inten);
//                arg0.dismiss();
//            }
//        }).create().show();
    }

    public static void NewDialog(Context context, String str) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(
                context);
        dialog.setTitle("温馨提示");

        dialog.setMessage(str);

        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // ((DialogInterface) dialogs).dismiss();
                dialog.dismiss();
            }
        });
        dialog.create().show();

    }

    public static void LoginDialog_back(final Context context) {
        // dialog = new DialogWindow(context, "您还没有登录", "请登录以后使用", "取消", "去登录");
        // dialog.createDialogView(itemsOnClick);

        AlertDialog.Builder dialog = new AlertDialog.Builder(
                context);
        dialog.setTitle("您还没有登录");

        dialog.setMessage("请登录以后使用");

        dialog.setPositiveButton("去登录", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // ((DialogInterface) dialogs).dismiss();

                Intent intent = new Intent(context, LoginActivity.class);
                ((Activity) context).startActivityForResult(intent, YMOtherUtils.BACK_FINISH);
                dialog.dismiss();
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        dialog.create().show();

    }

    public static void LoginDialog(final Context context) {
        // dialog = new DialogWindow(context, "您还没有登录", "请登录以后使用", "取消", "去登录");
        // dialog.createDialogView(itemsOnClick);
        if (ClickUtil.isFastDoubleClick()) {
            return;
        }
        AlertDialog.Builder dialog = new AlertDialog.Builder(
                context, R.style.AlertDialogCustom);
        dialog.setTitle("您还没有登录");

        dialog.setMessage("请登录以后使用");

        dialog.setPositiveButton("去登录", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // ((DialogInterface) dialogs).dismiss();

                Intent intent = new Intent(context, LoginActivity.class);
                context.startActivity(intent);
                dialog.dismiss();
                YMApplication.getInstance().appExit();
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        dialog.create().show();

    }

    /**
     * 显示完善信息Dialog
     *
     * @param msgInfo
     */
    public static void showCompleteInfoDialog(Dialog mydialog, final Activity activity, String msgInfo) {
        if (mydialog == null) {
            mydialog = new AlertDialog.Builder(
                    activity).setMessage(msgInfo)
                    .setPositiveButton("去完善", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            Intent inten = new Intent(activity, PersonInfoActivity.class);
                            inten.putExtra("doctorInfo", "doctorInfo");
                            activity.startActivityForResult(inten, 1000);
                            arg0.dismiss();

                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            arg0.dismiss();

                        }
                    }).create();
        }
        if (!mydialog.isShowing()) {
            mydialog.show();
        }
    }

    /**
     * 删除评论
     */
    private void showDeleteDialog(Dialog mydialog, final Activity activity, final RealNameItem item, final CommentBean mCommentBean, final Subscriber<BaseResultBean> subscriber) {
        if (mydialog == null) {
            mydialog = new AlertDialog.Builder(activity)
                    .setTitle("是否要删除这条评论?")
                    .setPositiveButton("删除", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            arg0.dismiss();
                            DoctorCircleService.deleteComment(mCommentBean.user.userid, mCommentBean.id, subscriber);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            arg0.dismiss();
                        }
                    })
                    .create();
        }
        mydialog.show();
    }


    /**
     * 认证弹框统一处理 stone
     */
    public static void showUserCenterCertifyDialog(final Context context, MyCallBack positiveListener, T data, String serviceTitle) {

        //游客-->去登录
        if (YMUserService.isGuest()) {
            DialogUtil.LoginDialog(context);
            return;
        }

        /**认证状态
         *  -1 => '审核中', -2 => '驳回',
         *   0 => '待审核', 1 => '通过', 2 => '不通过',
         *   3 => '待跟踪', 4 => '暂不开通',
         */
        // TODO: 2018/3/16 测试stone
        final int type = YMApplication.DoctorApproveType();
//        final int type = -2;

        //认证通过-->做自己的跳转,不谈提示框
        if (type == 1) {
            if (positiveListener != null) {
                positiveListener.onClick(data);
            }
            return;
        }

        //非认证通过 用户-认证状态 --->弹框提示
        boolean hideNegativeBtn = false;
        String title = "";
        String content = "";
        String positive = "";
        String negative = "";

        switch (type) {
            case 0://待审核
                if (TextUtils.isEmpty(serviceTitle)) {
                    content = "请先通过专业认证";
                } else {
                    content = "开通" + serviceTitle + "，需通过专业认证";
                }
                title = "您还没有进行专业认证";
                hideNegativeBtn = false;
                positive = "去认证";
                negative = "取消";
                break;
            case -1://审核中
                if (TextUtils.isEmpty(serviceTitle)) {
                    content = "审核通过后才能使用此功能";
                } else {
                    content = "审核通过后才能开通";
                }
                hideNegativeBtn = true;
                title = "您的信息正在审核中";
                positive = "确定";
                break;
            case -2://驳回
                hideNegativeBtn = false;
                title = "您的认证信息被驳回";
                content = "请重新认证";
                positive = "去认证";
                negative = "取消";
                break;
            case 2://不通过
                if (TextUtils.isEmpty(serviceTitle)) {
                    content = "无法使用此功能";
                } else {
                    content = "无法开通此服务";
                }
                hideNegativeBtn = true;
                title = "您的信息未认证通过";
                positive = "确定";
                break;
        }

        //使用系统的dialog
        //stone 添加主题 解决崩溃(You need to use a Theme.AppCompat theme (or descendant) with this activity)
        //R.style.Theme_AppCompat_Light_Dialog_Alert / Theme_AppCompat_Dialog Theme_AppCompat_Light_Dialog_Alert / Theme_AppCompat_Light_Dialog
        AlertDialog.Builder dialog = new AlertDialog.Builder(context, R.style.AlertDialogCustom);
        dialog.setTitle(title);
        dialog.setMessage(content);

        dialog.setPositiveButton(positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //去开通
                if (type == 0) {
                    Intent intent = new Intent(context, ApproveInfoActivity.class);
                    context.startActivity(intent);
                } else if (type == -2) {
                    CheckStateActivity.startActivity(context, "check_reject", "被驳回");
                }
//                else if (type == -1) {
//                    CheckStateActivity.startActivity(context, "checking", "审核中");
//                }
                dialog.dismiss();
            }
        });


        if (!hideNegativeBtn) {
            dialog.setNegativeButton(negative, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }

        dialog.create().show();

    }

    /**
     * 弹框 stone
     *
     * @param context
     * @param title
     * @param content
     * @param btnOk
     * @param btnCancel
     * @param positiveListener
     */
    public static void showDialog(Context context, String title, String content, String btnOk, String btnCancel, final MyCallBack positiveListener) {

        if (TextUtils.isEmpty(title)) {
            title = "温馨提示";
        }
        if (TextUtils.isEmpty(btnOk)) {
            btnOk = "确定";
        }
        if (TextUtils.isEmpty(btnCancel)) {
            btnCancel = "取消";
        }

        AlertDialog.Builder dialog = new AlertDialog.Builder(context, R.style.AlertDialogCustom);
        dialog.setTitle(title);
        dialog.setMessage(content);

        dialog.setPositiveButton(btnOk, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (positiveListener != null) {
                    positiveListener.onClick(null);
                }
            }
        });

        dialog.setNegativeButton(btnCancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.create().show();
    }

    public static void showCustomDialog(Context context, int layout, String title, String content, String content2, String btnOk, String btnCancel, final MyCallBack positiveListener) {

        if (TextUtils.isEmpty(title)) {
            title = "";
        }
        if (TextUtils.isEmpty(btnOk)) {
            btnOk = "确定";
        }
        if (TextUtils.isEmpty(btnCancel)) {
            btnCancel = "取消";
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.dialogNoBg);
        View view = View.inflate(context, layout, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();

        TextView tv_title = (TextView) view.findViewById(R.id.title);//设置标题
        tv_title.setText(title);
        tv_title.setVisibility(TextUtils.isEmpty(title)?View.GONE:View.VISIBLE);
        TextView tv_msg= (TextView) view.findViewById(R.id.tv_msg);//设置内容一
        tv_msg.setText(content);
        tv_msg.setVisibility(TextUtils.isEmpty(content)?View.GONE:View.VISIBLE);
        TextView tv_fill= (TextView) view.findViewById(R.id.tv_fill);//设置内容二
        tv_fill.setText(content2);
        tv_fill.setVisibility(TextUtils.isEmpty(content2)?View.GONE:View.VISIBLE);
        builder.setCancelable(true);
        TextView btn_cancel = (TextView) view.findViewById(R.id.btn_cancel);
        btn_cancel.setText(btnCancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        TextView btn_ok = (TextView) view.findViewById(R.id.btn_ok);
        btn_ok.setText(btnOk);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (positiveListener != null) {
                    positiveListener.onClick(null);
                }
            }
        });
    }

    public static void showCustomDialogOnlyOneButton(Context context, int layout, String title, String content, String content2, String btnOk, String btnCancel, final MyCallBack positiveListener) {

        if (TextUtils.isEmpty(title)) {
            title = "";
        }
        if (TextUtils.isEmpty(btnOk)) {
            btnOk = "确定";
        }
        if (TextUtils.isEmpty(btnCancel)) {
            btnCancel = "";
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.dialogNoBg);
        View view = View.inflate(context, layout, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();

        TextView tv_title = (TextView) view.findViewById(R.id.title);//设置标题
        tv_title.setText(title);
        tv_title.setVisibility(TextUtils.isEmpty(title)?View.GONE:View.VISIBLE);
        TextView tv_msg= (TextView) view.findViewById(R.id.tv_msg);//设置内容一
        tv_msg.setText(content);
        tv_msg.setVisibility(TextUtils.isEmpty(content)?View.GONE:View.VISIBLE);
        TextView tv_fill= (TextView) view.findViewById(R.id.tv_fill);//设置内容二
        tv_fill.setText(content2);
        tv_fill.setVisibility(TextUtils.isEmpty(content2)?View.GONE:View.VISIBLE);
        builder.setCancelable(true);
        TextView btn_cancel = (TextView) view.findViewById(R.id.btn_cancel);
        btn_cancel.setText(btnCancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        if (TextUtils.isEmpty(btnCancel)) {
            btn_cancel.setVisibility(View.GONE);
        }

        TextView btn_ok = (TextView) view.findViewById(R.id.btn_ok);
        btn_ok.setText(btnOk);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (positiveListener != null) {
                    positiveListener.onClick(null);
                }
            }
        });
    }

    /**
     * 弹框 stone
     *
     * @param context
     * @param content
     * @param btnOk
     * @param btnCancel
     * @param positiveListener
     */
    public static void showDialogNoTitle(Context context, String content, String btnOk, String btnCancel, final MyCallBack positiveListener) {


        if (TextUtils.isEmpty(btnOk)) {
            btnOk = "确定";
        }

        if (TextUtils.isEmpty(btnCancel)) {
            btnCancel = "取消";
        }

        AlertDialog.Builder dialog = new AlertDialog.Builder(context, R.style.AlertDialogCustom);
        dialog.setMessage(content);

        dialog.setPositiveButton(btnOk, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (positiveListener != null) {
                    positiveListener.onClick(null);
                }
            }
        });

            dialog.setNegativeButton(btnCancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

        dialog.create().show();
    }

}
