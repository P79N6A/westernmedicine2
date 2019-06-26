package com.xywy.askforexpert.module.message.msgchat;

import android.content.Context;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.SoundPool;
import android.os.Bundle;

import com.hyphenate.chat.EMCallStateChangeListener;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.xywy.askforexpert.BuildConfig;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.old.BaseActivity;
import com.xywy.easeWrapper.EMChatManager;
import com.xywy.easeWrapper.EaseConstant;

public class CallVoidActivity extends BaseActivity {

    protected boolean isInComingCall;
    protected String username;
    protected CallingState callingState = CallingState.CANCED;
    protected String callDruationText;
    protected String msgid;
    protected AudioManager audioManager;
    protected SoundPool soundPool;
    protected Ringtone ringtone;
    protected int outgoing;
    protected EMCallStateChangeListener callStateListener;

    public String toRealname = "";
    public String toHeadImge = "";

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (soundPool != null) {
            soundPool.release();
        }
        if (ringtone != null && ringtone.isPlaying()) {
            ringtone.stop();
        }
        audioManager.setMode(AudioManager.MODE_NORMAL);
        audioManager.setMicrophoneMute(false);

        if (callStateListener != null) {
            EMChatManager.getInstance().removeCallStateChangeListener(callStateListener);
        }

    }

    /**
     * 播放拨号响铃
     */
    protected int playMakeCallSounds() {
        try {
            // 最大音量
            float audioMaxVolumn = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
            // 当前音量
            float audioCurrentVolumn = audioManager.getStreamVolume(AudioManager.STREAM_RING);
            float volumnRatio = audioCurrentVolumn / audioMaxVolumn;

            audioManager.setMode(AudioManager.MODE_RINGTONE);
            audioManager.setSpeakerphoneOn(false);

            // 播放
            int id = soundPool.play(outgoing, // 声音资源
                    0.3f, // 左声道
                    0.3f, // 右声道
                    1, // 优先级，0最低
                    -1, // 循环次数，0是不循环，-1是永远循环
                    1); // 回放速度，0.5-2.0之间。1为正常速度
            return id;
        } catch (Exception e) {
            return -1;
        }
    }

    // 打开扬声器
    protected void openSpeakerOn() {
        try {
            if (!audioManager.isSpeakerphoneOn()) {
                audioManager.setSpeakerphoneOn(true);
            }
            audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 关闭扬声器
    protected void closeSpeakerOn() {

        try {
            if (audioManager != null) {
                // int curVolume =
                // audioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);
                if (audioManager.isSpeakerphoneOn()) {
                    audioManager.setSpeakerphoneOn(false);
                }
                audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
                // audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,
                // curVolume, AudioManager.STREAM_VOICE_CALL);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存通话消息记录
     *
     * @param type 0：音频，1：视频
     */
    protected void saveCallRecord(int type) {
        EMMessage message = null;
        EMTextMessageBody txtBody = null;
        if (!isInComingCall) { // 打出去的通话
            message = EMMessage.createSendMessage(EMMessage.Type.TXT);
            message.setReceipt(username);
        } else {
            return;
//            message = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
//            message.setFrom(username);
        }

        String st1 = getResources().getString(R.string.call_duration);
        String st2 = getResources().getString(R.string.Refused);
        String st3 = getResources().getString(R.string.The_other_party_has_refused_to);
        String st4 = getResources().getString(R.string.The_other_is_not_online);
        String st5 = getResources().getString(R.string.The_other_is_on_the_phone);
        String st6 = getResources().getString(R.string.The_other_party_did_not_answer);
        String st7 = getResources().getString(R.string.did_not_answer);
        String st8 = getResources().getString(R.string.Has_been_cancelled);
        switch (callingState) {
            case NORMAL:
//            txtBody = new EMTextMessageBody(st1 + callDruationText)
                // ;
                txtBody = new EMTextMessageBody(st1);
                break;
            case REFUESD:
                txtBody = new EMTextMessageBody(st2);
                break;
            case BEREFUESD:
                txtBody = new EMTextMessageBody(st3);
                break;
            case OFFLINE:
                txtBody = new EMTextMessageBody(st4);
                break;
            case BUSY:
                txtBody = new EMTextMessageBody(st5);
                break;
            case NORESPONSE:
                txtBody = new EMTextMessageBody(st6);
                break;
            case UNANSWERED:
                txtBody = new EMTextMessageBody(st7);
                break;
            case VERSION_NOT_SAME:
                txtBody = new EMTextMessageBody(getString(R.string.call_version_inconsistent));
            default:
                txtBody = new EMTextMessageBody(st8);
//            message.status = EMMessage.Status.SUCCESS;

                break;
        }
        // 设置扩展属性
        if (type == 0) {
            message.setAttribute(EaseConstant.MESSAGE_ATTR_IS_VOICE_CALL, true);
        } else {
            message.setAttribute(EaseConstant.MESSAGE_ATTR_IS_VIDEO_CALL, true);
        }

//        final long start = System.currentTimeMillis();
        message.setAttribute("fromRealName", YMApplication.getLoginInfo()
                .getData().getRealname());
        message.setAttribute("fromAvatar", YMApplication.getLoginInfo()
                .getData().getPhoto());

        message.setAttribute("toRealName", toRealname);
        message.setAttribute("toAvatar", toHeadImge);
        message.setAttribute("newMsgType", "callVideo");
//        message.setAttribute("callingState",callingState+"");
        //stone 新添加的扩展字段区别是医脉搏还是用药助手
        if (YMApplication.APPLICATIONID.equals(BuildConfig.APPLICATION_ID)) {
            message.setAttribute("source", "selldrug");
        }

        message.setStatus(EMMessage.Status.SUCCESS);

        // 设置消息body
        message.addBody(txtBody);
        message.setMsgId(msgid);

        // 保存
        EMChatManager.getInstance().saveMessage(message, false);
    }

    enum CallingState {
        CANCED, NORMAL, REFUESD, BEREFUESD, UNANSWERED, OFFLINE, NORESPONSE, BUSY, VERSION_NOT_SAME
    }
}
