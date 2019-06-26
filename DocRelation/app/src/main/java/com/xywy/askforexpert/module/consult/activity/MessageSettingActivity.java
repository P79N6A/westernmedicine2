package com.xywy.askforexpert.module.consult.activity;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YmRxBus;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.ClickUtil;
import com.xywy.askforexpert.appcommon.utils.DateUtils;
import com.xywy.askforexpert.appcommon.utils.DialogUtil;
import com.xywy.askforexpert.appcommon.utils.KeyBoardUtils;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.YMOtherUtils;
import com.xywy.askforexpert.model.certification.MessageBoardBean;
import com.xywy.askforexpert.module.discovery.medicine.CertificationAboutRequest;
import com.xywy.askforexpert.module.discovery.medicine.common.MyCallBack;
import com.xywy.retrofit.demo.BaseRetrofitResponse;
import com.xywy.retrofit.net.BaseData;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

import static com.xywy.askforexpert.appcommon.old.Constants.CLOSEED;
import static com.xywy.askforexpert.appcommon.old.Constants.NIGHT;
import static com.xywy.askforexpert.appcommon.old.Constants.OFFLINE;
import static com.xywy.askforexpert.appcommon.old.Constants.OPENED;

/**
 * 留言设置 stone
 */
public class MessageSettingActivity extends YMBaseActivity implements View.OnClickListener {

    @Bind(R.id.et_offline)
    EditText et_offline;

    @Bind(R.id.tv_offline_time)
    TextView tv_offline_time;

    @Bind(R.id.tv_offline_button)
    TextView tv_offline_button;

    @Bind(R.id.tv_offline_edit)
    TextView tv_offline_edit;

    @Bind(R.id.et_night)
    EditText et_night;

    @Bind(R.id.tv_night_button)
    TextView tv_night_button;


    @Bind(R.id.tv_night_edit)
    TextView tv_night_edit;

    private boolean mOfflineOpened, mNightOpend;

    private boolean mOfflineEditable, mNightEditable;

    private long mOfflineTime;

    private CountDownTimer mOfflineTimer;

    //字数限制 汉字
    private int mTextLimitMax = 50;
    private int mTextLimitMin = 5;


    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, MessageSettingActivity.class));
    }

    @Override
    protected void initView() {
        initTitleBar();
        //默认都是不可编辑状态
        et_night.setFocusable(false);
        et_night.setFocusableInTouchMode(false);

        et_offline.setFocusable(false);
        et_offline.setFocusableInTouchMode(false);
    }

    private void handleNight() {
        if (mNightOpend) {
            tv_night_button.setBackgroundResource(R.drawable.selector_round_ff9459_88ff9459);
            tv_night_button.setText("关闭夜间模式");
            tv_night_edit.setVisibility(View.GONE);
        } else {
            tv_night_button.setBackgroundResource(R.drawable.selector_round_a9bacd_88a9bacd);
            tv_night_button.setText("开启夜间模式");
            tv_night_edit.setVisibility(View.VISIBLE);
        }
    }

    private void handleOffline() {
        if (mOfflineTimer != null) {
            mOfflineTimer.cancel();
        }
        if (mOfflineTime == 0) {
            tv_offline_button.setBackgroundResource(R.drawable.selector_round_a9bacd_88a9bacd);
            mOfflineOpened = false;
            tv_offline_edit.setVisibility(View.VISIBLE);
            tv_offline_time.setVisibility(View.GONE);
            tv_offline_button.setText("开启离线模式");
        } else {
            tv_offline_button.setBackgroundResource(R.drawable.selector_round_ff9459_88ff9459);
            mOfflineOpened = true;
            tv_offline_edit.setVisibility(View.GONE);
            tv_offline_time.setVisibility(View.VISIBLE);
            tv_offline_button.setText("关闭离线模式");
            /** 倒计时mOfflineTime，一次1秒 */
            mOfflineTimer = new CountDownTimer(mOfflineTime, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    if (mOfflineOpened) {
                        tv_offline_time.setText(DateUtils.formatTimeForMessageSetting(millisUntilFinished) + "后\n系统将自动关闭离线留言功能");
                    } else {
                        mOfflineTimer.cancel();
                    }
                }

                @Override
                public void onFinish() {
                    tv_offline_time.setVisibility(View.GONE);
                    mOfflineTime = 0;
                    handleOffline();
                }
            }.start();
        }
    }

    @Override
    protected void initData() {
        //获取留言板内容
        showLoadDataDialog();
        CertificationAboutRequest.getInstance().getMessageBoard(YMApplication.getUUid()).subscribe(new BaseRetrofitResponse<BaseData<List<MessageBoardBean>>>() {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                hideProgressDialog();
                finish();
            }

            @Override
            public void onNext(BaseData<List<MessageBoardBean>> listBaseData) {
                super.onNext(listBaseData);
                hideProgressDialog();
                if (listBaseData != null
                        && listBaseData.getData() != null
                        && listBaseData.getData().size() == 2) {
                    handleSetMessageBoard(listBaseData.getData().get(0));
                    handleSetMessageBoard(listBaseData.getData().get(1));
                } else {
                    finish();
                }
            }
        });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_message_setting;
    }


    private void initTitleBar() {
        titleBarBuilder.setTitleText("留言设置");
    }

    @OnClick({R.id.tv_night_button, R.id.tv_night_edit, R.id.tv_offline_button, R.id.tv_offline_edit})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_night_button:
                if (ClickUtil.isFastDoubleClick()) {
                    return;
                }
                if (mNightEditable) {
                    DialogUtil.showDialog(this, null, "保存正在编辑的夜间留言么", null, null, new MyCallBack() {
                        @Override
                        public void onClick(Object data) {

                            if (checkTextLimit(et_night.getText().toString())) {
                                return;
                            }

                            //保存夜间留言内容
                            requestSetMessageBoard(YMApplication.getUUid(), NIGHT, CLOSEED, et_night.getText().toString(), new MyCallBack() {
                                @Override
                                public void onClick(Object data) {
                                    handleSetMessageBoard((MessageBoardBean) data);
                                    initNightEditStatus(false);
                                }
                            });
                        }
                    });
                    return;
                }

                if (mOfflineEditable) {
                    DialogUtil.showDialog(this, null, "保存正在编辑的离线留言么", null, null, new MyCallBack() {
                        @Override
                        public void onClick(Object data) {

                            if (checkTextLimit(et_offline.getText().toString())) {
                                return;
                            }

                            //保存离线留言内容
                            requestSetMessageBoard(YMApplication.getUUid(), OFFLINE, CLOSEED, et_offline.getText().toString(), new MyCallBack() {
                                @Override
                                public void onClick(Object data) {
                                    handleSetMessageBoard((MessageBoardBean) data);
                                    initOfflineEditStatus(false);
                                }
                            });
                        }
                    });
                    return;
                }

                //开启夜间留言 stone
                if (!mNightOpend) {
                    StatisticalTools.eventCount(this, Constants.OPENNIGHTMESSAGE);
                }

                //修改开启关闭状态
                requestSetMessageBoard(YMApplication.getUUid(), NIGHT, !mNightOpend ? OPENED : CLOSEED, et_night.getText().toString(), new MyCallBack() {
                    @Override
                    public void onClick(Object data) {
                        //全部设置数据
                        handleSetMessageBoard((MessageBoardBean) data);
                    }
                });
                break;
            case R.id.tv_night_edit:
                if (ClickUtil.isFastDoubleClick()) {
                    return;
                }
                if (mOfflineEditable) {
                    DialogUtil.showDialog(this, null, "保存正在编辑的离线留言么", null, null, new MyCallBack() {
                        @Override
                        public void onClick(Object data) {

                            if (checkTextLimit(et_offline.getText().toString())) {
                                return;
                            }

                            //保存离线留言内容
                            requestSetMessageBoard(YMApplication.getUUid(), OFFLINE, CLOSEED, et_offline.getText().toString(), new MyCallBack() {
                                @Override
                                public void onClick(Object data) {
                                    initOfflineEditStatus(false);

                                    mNightEditable = !mNightEditable;
                                    initNightEditStatus(mNightEditable);
                                }
                            });
                        }
                    });
                    return;
                }

                if (mNightEditable) {

                    if (checkTextLimit(et_night.getText().toString())) {
                        return;
                    }

                    //保存夜间留言内容
                    requestSetMessageBoard(YMApplication.getUUid(), NIGHT, CLOSEED, et_night.getText().toString(), new MyCallBack() {
                        @Override
                        public void onClick(Object data) {
                            handleSetMessageBoard((MessageBoardBean) data);
                            initNightEditStatus(false);
                        }
                    });
                } else {
                    initNightEditStatus(true);
                }


                break;
            case R.id.tv_offline_button:
                if (ClickUtil.isFastDoubleClick()) {
                    return;
                }
                if (mOfflineEditable) {
                    DialogUtil.showDialog(this, null, "保存正在编辑的离线留言么", null, null, new MyCallBack() {
                        @Override
                        public void onClick(Object data) {

                            if (checkTextLimit(et_offline.getText().toString())) {
                                return;
                            }

                            //保存离线留言内容
                            requestSetMessageBoard(YMApplication.getUUid(), OFFLINE, CLOSEED, et_offline.getText().toString(), new MyCallBack() {
                                @Override
                                public void onClick(Object data) {
                                    handleSetMessageBoard((MessageBoardBean) data);
                                    initOfflineEditStatus(false);
                                }
                            });
                        }
                    });
                    return;
                }

                if (mNightEditable) {
                    DialogUtil.showDialog(this, null, "保存正在编辑的夜间留言么", null, null, new MyCallBack() {
                        @Override
                        public void onClick(Object data) {

                            if (checkTextLimit(et_night.getText().toString())) {
                                return;
                            }

                            //保存夜间留言内容
                            requestSetMessageBoard(YMApplication.getUUid(), NIGHT, CLOSEED, et_night.getText().toString(), new MyCallBack() {
                                @Override
                                public void onClick(Object data) {
                                    handleSetMessageBoard((MessageBoardBean) data);
                                    initNightEditStatus(false);
                                }
                            });
                        }
                    });
                    return;
                }

                //离线开启 stone
                if (!mOfflineOpened) {
                    StatisticalTools.eventCount(this, Constants.OPENOFFLINEMESSAGE);
                }

                //修改开启关闭状态
                if (!checkTextLimit(et_offline.getText().toString())) {
                    requestSetMessageBoard(YMApplication.getUUid(), OFFLINE, !mOfflineOpened ? OPENED : CLOSEED, et_offline.getText().toString(), new MyCallBack() {
                        @Override
                        public void onClick(Object data) {
                            //全部设置数据
                            handleSetMessageBoard((MessageBoardBean) data);
                        }
                    });
                }

                break;
            case R.id.tv_offline_edit:
                //修改 stone
                StatisticalTools.eventCount(this, Constants.MODIFYTHEMESSAGE);

                if (ClickUtil.isFastDoubleClick()) {
                    return;
                }
                if (mNightEditable) {
                    DialogUtil.showDialog(this, null, "保存正在编辑的夜间留言么", null, null, new MyCallBack() {
                        @Override
                        public void onClick(Object data) {

                            if (checkTextLimit(et_night.getText().toString())) {
                                return;
                            }


                            //保存夜间留言内容
                            requestSetMessageBoard(YMApplication.getUUid(), NIGHT, CLOSEED, et_night.getText().toString(), new MyCallBack() {
                                @Override
                                public void onClick(Object data) {
                                    initNightEditStatus(false);

                                    mOfflineEditable = !mOfflineEditable;
                                    initOfflineEditStatus(mOfflineEditable);
                                }
                            });
                        }
                    });
                    return;
                }

                if (mOfflineEditable) {

                    if (checkTextLimit(et_offline.getText().toString())) {
                        return;
                    }

                    //保存离线留言内容
                    requestSetMessageBoard(YMApplication.getUUid(), OFFLINE, CLOSEED, et_offline.getText().toString(), new MyCallBack() {
                        @Override
                        public void onClick(Object data) {
                            handleSetMessageBoard((MessageBoardBean) data);
                            initOfflineEditStatus(false);
                        }
                    });
                } else {
                    initOfflineEditStatus(true);
                }
                break;
            default:
                break;
        }
    }

    private boolean checkTextLimit(String s) {
        if (YMOtherUtils.getChineseCount(s) < mTextLimitMin || YMOtherUtils.getChineseCount(s) > mTextLimitMax) {
            Toast.makeText(this,"字数要求5-50字",Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }

    //初始化夜间编辑状态
    private void initNightEditStatus(boolean canEdit) {
        if (canEdit) {
            mNightEditable = true;
            tv_night_edit.setText("完成");
            tv_night_edit.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.messagesetting_wancheng), null, null, null);
            et_night.setFocusable(true);
            et_night.setFocusableInTouchMode(true);
            uiHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!isFinishing()) {
                        et_night.requestFocus();
                        et_night.setSelection(et_night.getText().toString().length());

                        //弹出键盘
                        KeyBoardUtils.openKeybord(et_night);
                    }
                }
            }, 50);
        } else {
            mNightEditable = false;
            tv_night_edit.setText("修改");
            tv_night_edit.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.messagesetting_xiugai), null, null, null);
            et_night.setFocusable(false);
            et_night.setFocusableInTouchMode(false);
        }
    }

    //初始化离线编辑状态
    private void initOfflineEditStatus(boolean canEdit) {
        if (canEdit) {
            mOfflineEditable = true;
            tv_offline_edit.setText("完成");
            tv_offline_edit.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.messagesetting_wancheng), null, null, null);
            et_offline.setFocusable(true);
            et_offline.setFocusableInTouchMode(true);
            uiHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!isFinishing()) {
                        et_offline.requestFocus();
                        et_offline.setSelection(et_offline.getText().toString().length());

                        //弹出键盘
                        KeyBoardUtils.openKeybord(et_offline);
                    }
                }
            }, 50);
        } else {
            mOfflineEditable = false;
            tv_offline_edit.setText("修改");
            tv_offline_edit.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.messagesetting_xiugai), null, null, null);
            et_offline.setFocusable(false);
            et_offline.setFocusableInTouchMode(false);
        }
    }

    //处理设置留言板
    private void handleSetMessageBoard(MessageBoardBean bean) {
        if (bean != null) {
            //1 离线 2 夜间
            if (OFFLINE.equals(bean.getType())) {
                mOfflineOpened = OPENED.equals(bean.getIsopen());
                mOfflineTime = Long.parseLong(bean.getLeft_time() + "000");
                et_offline.setText(bean.getMessage());
                handleOffline();
            } else if (NIGHT.equals(bean.getType())) {
                mNightOpend = OPENED.equals(bean.getIsopen());
                et_night.setText(bean.getMessage());
                handleNight();
                //通知
                YmRxBus.notifyNightModeChanged(mNightOpend);
            }
        }
    }

    //请求设置留言板
    private void requestSetMessageBoard(String did, String type, String isopen, String message, final MyCallBack callBack) {
        showLoadDataDialog();
        //修改 stone
        StatisticalTools.eventCount(this, Constants.MODIFYTHEMESSAGE);

        CertificationAboutRequest.getInstance().getSetMessageBoard(did, type, isopen, message).subscribe(new BaseRetrofitResponse<BaseData<MessageBoardBean>>() {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                hideProgressDialog();
                ToastUtils.shortToast("请求失败");
            }

            @Override
            public void onNext(BaseData<MessageBoardBean> listBaseData) {
                super.onNext(listBaseData);
                hideProgressDialog();
                if (listBaseData != null
                        && listBaseData.getData() != null) {

                    handleSetMessageBoard(listBaseData.getData());

                    if (callBack != null) {
                        callBack.onClick(listBaseData.getData());
                    }
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mOfflineTimer != null) {
            mOfflineTimer.cancel();
        }
    }
}
