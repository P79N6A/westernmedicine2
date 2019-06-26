package com.xywy.livevideo.player;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.rtmp.ITXLivePlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.xywy.livevideolib.R;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by zhangzheng on 2017/2/16.
 */
public class XYMediaPlayerView extends RelativeLayout implements SeekBar.OnSeekBarChangeListener, View.OnClickListener, ITXLivePlayListener {

    private Context context;

    private String url;

    private TXMediaPlayer mediaPlayer;

    private RelativeLayout rlBottom;

    private SeekBar sbProgress;

    private Button btnPlay;

    private TextView tvProgress;//当前进度

    private TextView tvDuration;//视频时间总长

    private CommonLoadingView clvLoading;

    private int playType;

    private boolean isDraging = false;

    private boolean isUpdateProgress = true;

    private SimpleDateFormat simpleDateFormat;
    private ImageView ivLeave;

    public static final int PLAYER_TYPE_REALTIME = 1;   //直播
    public static final int PLAYER_TYPE_REPLAY = 0;  //点播

    private List<String> vodList;

    private int currentIndex = 0;

    public XYMediaPlayerView(Context context) {
        super(context);
        initView(context);
    }

    public XYMediaPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public XYMediaPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    //实现播放接口，初始化界面，添加播放的view
    private void initView(Context context) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.live_player_view, this);
        mediaPlayer = new TXMediaPlayer(context);
        mediaPlayer.setPlayListener(this);
        this.addView(mediaPlayer.getView(), 0);
        rlBottom = (RelativeLayout) findViewById(R.id.rl_bottom);
        sbProgress = (SeekBar) findViewById(R.id.sb_progress);
        sbProgress.setOnSeekBarChangeListener(this);
        btnPlay = (Button) findViewById(R.id.btn_play);
        btnPlay.setOnClickListener(this);
        tvProgress = (TextView) findViewById(R.id.tv_play_view_current_progress);
        tvDuration = (TextView) findViewById(R.id.tv_play_view_total_duration);
        clvLoading = (CommonLoadingView) findViewById(R.id.clv_loading);
        ivLeave = (ImageView) findViewById(R.id.iv_host_leave);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (playType == PLAYER_TYPE_REALTIME) {
            mediaPlayer.start(url);
        } else if (playType == PLAYER_TYPE_REPLAY) {
            if (vodList != null && !vodList.isEmpty()) {
                mediaPlayer.start(vodList.get(currentIndex));
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onDetachedFromWindow() {
        mediaPlayer.stop();
        super.onDetachedFromWindow();
    }

    /**
     * 重新播放
     */
    public void restart() {
        mediaPlayer.restart(url);
    }

    /**
     * 初始化播放链接和类型
     *
     * @param url  播放链接
     * @param type 区分直播或者点播
     */
    public void init(String url, int type, List<String> vodList) {
        this.url = url;
        initPlayerType(type);
        this.playType = type;
        this.vodList = vodList;
    }

    public void init(String url) {
        init(url, PLAYER_TYPE_REALTIME, null);
    }

    private void initPlayerType(int type) {
        switch (type) {
            case PLAYER_TYPE_REALTIME:
                rlBottom.setVisibility(GONE);
                break;
            case PLAYER_TYPE_REPLAY:
                rlBottom.setVisibility(VISIBLE);
                break;
        }
        mediaPlayer.setPlayMode(type);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        isUpdateProgress = false;
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mediaPlayer.seekProgress(seekBar.getProgress());
        isUpdateProgress = true;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_play) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                btnPlay.setBackgroundResource(R.drawable.replay_play_icon);
            } else {
                mediaPlayer.resume();
                btnPlay.setBackgroundResource(R.drawable.replay_pause_icon);
            }
        }
    }

    @Override
    public void onPlayEvent(int i, Bundle bundle) {
        switch (i) {
            case TXLiveConstants.PLAY_EVT_PLAY_BEGIN:
                //开始播放，隐藏loading
                mediaPlayer.getView().setVisibility(VISIBLE);
                // clvLoading.setVisibility(GONE);
                ivLeave.setVisibility(GONE);
                break;
            case TXLiveConstants.PLAY_EVT_PLAY_END:
                //如果是回放，结束后自动播放下一个
                if (playType == PLAYER_TYPE_REPLAY) {
                    sbProgress.setProgress(sbProgress.getMax());
                    tvProgress.setText(tvProgress.getText());
                    currentIndex++;
                    if (currentIndex < vodList.size()) {
                        mediaPlayer.start(vodList.get(currentIndex));

                    } else {
                        Toast.makeText(context, "点播结束", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case TXLiveConstants.PLAY_EVT_PLAY_LOADING:
                //加载，显示loading
                mediaPlayer.getView().setVisibility(INVISIBLE);
                //clvLoading.setVisibility(VISIBLE);
                ivLeave.setVisibility(VISIBLE);
                break;
            case TXLiveConstants.PLAY_ERR_NET_DISCONNECT:
                mediaPlayer.getView().setVisibility(INVISIBLE);
                ivLeave.setVisibility(VISIBLE);
                break;
            case TXLiveConstants.PLAY_EVT_PLAY_PROGRESS:
                //更新播放进度
                if (playType == PLAYER_TYPE_REPLAY) {
                    int progress = bundle.getInt(TXLiveConstants.EVT_PLAY_PROGRESS);
                    int duration = bundle.getInt(TXLiveConstants.EVT_PLAY_DURATION);
                    updateProgress(progress, duration);
                }
                break;
        }
    }

    @Override
    public void onNetStatus(Bundle bundle) {

    }

    private void updateProgress(int progress, int duration) {
        if (isUpdateProgress) {
            sbProgress.setMax(duration);
            sbProgress.setProgress(progress);
            tvProgress.setText(String.format("%02d:%02d", progress / 60, progress % 60));
            tvDuration.setText(String.format("%02d:%02d", duration / 60, duration % 60));
        }
    }
}
