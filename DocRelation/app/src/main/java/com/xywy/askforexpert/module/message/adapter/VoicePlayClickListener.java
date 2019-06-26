package com.xywy.askforexpert.module.message.adapter;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessage.ChatType;
import com.hyphenate.chat.EMVoiceMessageBody;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.module.message.msgchat.ChatFragment;
import com.xywy.easeWrapper.EMChatManager;
import com.xywy.easeWrapper.controller.EaseUI;

import java.io.File;

public class VoicePlayClickListener implements View.OnClickListener {

    public static boolean isPlaying = false;
    public static VoicePlayClickListener currentPlayListener = null;
    EMMessage message;
    EMVoiceMessageBody voiceBody;
    ImageView voiceIconView;
    MediaPlayer mediaPlayer = null;
    ImageView iv_read_status;
    Fragment fragment;
    private AnimationDrawable voiceAnimation = null;
    private ChatType chatType;
    private BaseAdapter adapter;

    /**
     * @param message
     * @param v
     * @param iv_read_status
     */
    public VoicePlayClickListener(EMMessage message, ImageView v, ImageView iv_read_status, BaseAdapter adapter, Fragment fragment,
                                  String username) {
        this.message = message;
        voiceBody = (EMVoiceMessageBody) message.getBody();
        this.iv_read_status = iv_read_status;
        this.adapter = adapter;
        voiceIconView = v;
        this.fragment = fragment;
        this.chatType = message.getChatType();
    }

    public void stopPlayVoice() {
        voiceAnimation.stop();
        if (message.direct() == EMMessage.Direct.RECEIVE) {
            voiceIconView.setImageResource(R.drawable.chatfrom_voice_playing_f3);
        } else {
//			voiceIconView.setImageResource(R.drawable.chatto_voice_playing);
        }
        // stop play voice
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        isPlaying = false;
        ((ChatFragment) fragment).playMsgId = null;
        adapter.notifyDataSetChanged();
    }

    public void playVoice(String filePath) {
        if (!(new File(filePath).exists())) {
            return;
        }
        ((ChatFragment) fragment).playMsgId = message.getMsgId();
        AudioManager audioManager = (AudioManager) fragment.getActivity().getSystemService(Context.AUDIO_SERVICE);

		mediaPlayer = new MediaPlayer();
		if (EaseUI.getInstance().getSettingsProvider().isSpeakerOpened()) {
			audioManager.setMode(AudioManager.MODE_NORMAL);
			audioManager.setSpeakerphoneOn(true);
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
		} else {
			audioManager.setSpeakerphoneOn(false);// 关闭扬声器
			// 把声音设定成Earpiece（听筒）出来，设定为正在通话中
			audioManager.setMode(AudioManager.MODE_IN_CALL);
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
		}
		try {
			mediaPlayer.setDataSource(filePath);
			mediaPlayer.prepare();
			mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    // TODO Auto-generated method stub
                    mediaPlayer.release();
                    mediaPlayer = null;
                    stopPlayVoice(); // stop animation
                }

            });
            isPlaying = true;
            currentPlayListener = this;
            mediaPlayer.start();
            showAnimation();

            // 如果是接收的消息
            if (message.direct() == EMMessage.Direct.RECEIVE) {
                try {
                    if (!message.isAcked()) {
                        message.setAcked(true);
                        // 告知对方已读这条消息
                        if (chatType != ChatType.GroupChat) {
                            EMChatManager.getInstance().ackMessageRead(message.getFrom(), message.getMsgId());
                        }
                    }
                } catch (Exception e) {
                    message.setAcked(false);
                }
                if (!message.isListened() && iv_read_status != null && iv_read_status.getVisibility() == View.VISIBLE) {
                    // 隐藏自己未播放这条语音消息的标志
                    iv_read_status.setVisibility(View.INVISIBLE);
                    EMChatManager.getInstance().setMessageListened(message);
                }

            }

        } catch (Exception e) {
        }
    }

    // show the voice playing animation
    private void showAnimation() {
        // play voice, and start animation
        if (message.direct() == EMMessage.Direct.RECEIVE) {
            voiceIconView.setImageResource(R.drawable.voice_from_icon);
        } else {
            voiceIconView.setImageResource(R.drawable.voice_to_icon);
        }
        voiceAnimation = (AnimationDrawable) voiceIconView.getDrawable();
        voiceAnimation.start();
    }

    @Override
    public void onClick(View v) {
        String st = fragment.getActivity().getResources().getString(R.string.Is_download_voice_click_later);
        if (isPlaying) {
            if (((ChatFragment) fragment).playMsgId != null && ((ChatFragment) fragment).playMsgId.equals(message.getMsgId())) {
                currentPlayListener.stopPlayVoice();
                return;
            }
            currentPlayListener.stopPlayVoice();
        }

        if (message.direct() == EMMessage.Direct.SEND) {
            // for sent msg, we will try to play the voice file directly
            playVoice(voiceBody.getLocalUrl());
        } else {
            if (message.status() == EMMessage.Status.SUCCESS) {
                File file = new File(voiceBody.getLocalUrl());
                if (file.exists() && file.isFile()) {
                    playVoice(voiceBody.getLocalUrl());
                } else {
                    System.err.println("file not exist");
                }

            } else if (message.status() == EMMessage.Status.INPROGRESS) {
                Toast.makeText(fragment.getActivity(), st, Toast.LENGTH_SHORT).show();
            } else if (message.status() == EMMessage.Status.FAIL) {
                Toast.makeText(fragment.getActivity(), st, Toast.LENGTH_SHORT).show();
                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {
                        EMChatManager.getInstance().asyncFetchMessage(message);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        super.onPostExecute(result);
                        adapter.notifyDataSetChanged();
                    }

                }.execute();

            }

        }
    }
}