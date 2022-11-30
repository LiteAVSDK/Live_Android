package com.tencent.mlvb.timeshift;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.tencent.live2.V2TXLiveDef;
import com.tencent.live2.V2TXLivePlayer;
import com.tencent.live2.V2TXLivePlayerObserver;
import com.tencent.live2.impl.V2TXLivePlayerImpl;
import com.tencent.mlvb.common.MLVBBaseActivity;
import com.tencent.mlvb.timeshift.R;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.tencent.mlvb.timeshift.TimeShiftHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/*
 时移功能
 MLVB APP 时移代码示例：
 本文件展示如何通过移动直播SDK实现时移功能
 1、首先在官网了解时移基本概念和使用，并开启时移功能。https://cloud.tencent.com/document/product/267/32742
 2、根据文档规则拼接时移的播放链接 String timeShiftUrl = "http://[Domain]/timeshift/[AppName]/[StreamName]/timeshift.m3u8?delay=90"。(delay ，默认最小90秒)
 3、停止当前正在播放的直播流 API: mLivePlayer.stopPlay();
 4、开始播放时移流 API: mLivePlayer.startLivePlay(timeShiftUrl);

 恢复直播流
 1、停止当前正在播放的时移流 API: mLivePlayer.stopPlay();
 2、开始播放直播流 API: mLivePlayer.startLivePlay(liveUrl);

 */

public class TimeShiftActivity extends MLVBBaseActivity {
    private static final String TAG = TimeShiftActivity.class.getSimpleName();
    /// 时移功能演示，示例拉流地址。
    private static final String DEFAULT_PLAY_URL = "http://liteavapp.qcloud.com/live/liteavdemoplayerstreamid.flv";
    private static final String DEFAULT_TIME_SHIFT_DOMAIN = "liteavapp.timeshift.qcloud.com";

    // 时移区间可配置 https://cloud.tencent.com/document/product/267/32742
    private static final int kMaxFallbackSeconds = 600;
    private static final int kMinFallbackSeconds = 90;

    private TXCloudVideoView                            mVideoView;
    private TextView                                    mDateView;
    private SeekBar                                     mSeekBar;
    private Timer                                       mTimer;
    private TimeShiftHelper                             mTimeShiftHelper;
    private V2TXLivePlayer                              mLivePlayer;
    private String                                      mPlayUrl     = DEFAULT_PLAY_URL;
    private ArrayList<V2TXLiveDef.V2TXLiveStreamInfo>   mPlayUrlList = new ArrayList();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_shift);
        mTimeShiftHelper = new TimeShiftHelper(DEFAULT_TIME_SHIFT_DOMAIN);
        if (checkPermission()) {
            initView();
            startPlay();
            startTimer();
        }
    }

    @Override
    protected void onPermissionGranted() {
        initView();
    }

    private void initView() {
        mVideoView = findViewById(R.id.tx_cloud_view);
        findViewById(R.id.timeshift_resume_live).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resumeLive();
            }
        });

        mDateView = findViewById(R.id.timeshift_date);
        mSeekBar = findViewById(R.id.timeshift_seekbar);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (seekBar.getProgress() > 99) {
                    resumeLive();
                } else {
                    startTimeShift();
                }
                updateProgress();
            }
        });
    }

    private void startTimer() {
        mTimer = new Timer(true);
        TimerTask task = new TimerTask() {
            public void run() {
                updateProgress();
            }
        };
        mTimer.schedule(task, 0, 500);
    }

    private void stopTimer() {
        if (mTimer != null) {
            mTimer.cancel();
        }
    }

    private long getCurrentDelay() {
        double delay = 0.f;
        double progress = mSeekBar.getProgress() / 100.f;
        if (progress  < 0.99) {
            delay = kMinFallbackSeconds + (kMaxFallbackSeconds - kMinFallbackSeconds) * (1.0 - progress);
        }
        return Math.round(delay);
    }

    private void updateProgress() {
        long delay = getCurrentDelay();
        long nowms = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date(nowms - delay * 1000);
        mDateView.setText(simpleDateFormat.format(date));
    }

    private void stopPlay() {
        if (mLivePlayer != null) {
            if (mLivePlayer.isPlaying() == 1) {
                mLivePlayer.stopPlay();
            }
        }
        mLivePlayer = null;
    }

    private void startPlay() {
        if (mLivePlayer == null) {
            mLivePlayer = new V2TXLivePlayerImpl(this);
            mLivePlayer.setObserver(new V2TXLivePlayerObserver() {
                @Override
                public void onError(V2TXLivePlayer player, int code, String msg, Bundle extraInfo) {
                    super.onError(player, code, msg, extraInfo);
                }

                @Override
                public void onWarning(V2TXLivePlayer player, int code, String msg, Bundle extraInfo) {
                    super.onWarning(player, code, msg, extraInfo);
                }

                @Override
                public void onConnected(V2TXLivePlayer player, Bundle extraInfo) {
                    Log.d(TAG, "extraInfo:" + extraInfo.toString());
                    mPlayUrlList.clear();
                    ArrayList arrayList = mLivePlayer.getStreamList();
                    if (arrayList != null && arrayList.size() > 0) {
                        mPlayUrlList.addAll(arrayList);
                    }
                }

                @Override
                public void onVideoResolutionChanged(V2TXLivePlayer player, final int width, final int height) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(TimeShiftActivity.this,
                                    "resolution:" + width + "*" + height, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
        mLivePlayer.setProperty("clearLastImage", false);
        mLivePlayer.setRenderView(mVideoView);
        mLivePlayer.setRenderFillMode(V2TXLiveDef.V2TXLiveFillMode.V2TXLiveFillModeFit);
        int ret = mLivePlayer.startLivePlay(mPlayUrl);
        Log.i(TAG, "startPlay return: " + ret);
    }

    private void resumeLive() {
        mSeekBar.setProgress(100);
        mLivePlayer.stopPlay();
        mLivePlayer.startLivePlay(mPlayUrl);
    }

    private void startTimeShift() {
        String timeShiftUrl = mTimeShiftHelper.getTimeShiftUrl(DEFAULT_PLAY_URL, getCurrentDelay());
        mLivePlayer.stopPlay();
        mLivePlayer.startLivePlay(timeShiftUrl);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopPlay();
        stopTimer();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
