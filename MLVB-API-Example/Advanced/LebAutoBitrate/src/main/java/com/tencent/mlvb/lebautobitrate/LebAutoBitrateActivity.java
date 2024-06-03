package com.tencent.mlvb.lebautobitrate;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.tencent.live2.V2TXLivePlayer;
import com.tencent.live2.V2TXLivePlayerObserver;
import com.tencent.live2.impl.V2TXLivePlayerImpl;
import com.tencent.mlvb.common.MLVBBaseActivity;
import com.tencent.rtmp.ui.TXCloudVideoView;

/**
 * Webrtc Auto Bitrate
 * MLVB APP Webrtc Auto Bitrate
 * - 1、Set Render View API:{@link V2TXLivePlayer#setRenderView(TXCloudVideoView)}
 * - 2、Start Play API: {@link V2TXLivePlayer#startLivePlay(String)}
 * Documentation: https://cloud.tencent.com/document/product/454/81212
 * After the adaptive bitrate playback is started, seamless streaming cannot be performed.
 * If you enter the adaptive bit rate in the playback state,
 * Need to stop current playback before starting adaptive playback
 */
public class LebAutoBitrateActivity extends MLVBBaseActivity {
    private static final String TAG = LebAutoBitrateActivity.class.getSimpleName();
    private static final String NORMAL_PLAY_URL_1080 = "webrtc://liteavapp.qcloud.com/live/liteavdemoplayerstreamid?"
            + "tabr_bitrates=demo1080p,demo720p,demo540p&tabr_start_bitrate=demo1080p";
    private static final String NORMAL_PLAY_URL_720  = "webrtc://liteavapp.qcloud.com/live/liteavdemoplayerstreamid?"
            + "tabr_bitrates=demo1080p,demo720p,demo540p&tabr_start_bitrate=demo720p";
    private static final String NORMAL_PLAY_URL_540  = "webrtc://liteavapp.qcloud.com/live/liteavdemoplayerstreamid?"
            + "tabr_bitrates=demo1080p,demo720p,demo540p&tabr_start_bitrate=demo540p";
    private static final String AUTO_BITRATE_SUFFIX  = "&tabr_control=auto";

    private TXCloudVideoView mVideoView;
    private V2TXLivePlayer   mLivePlayer;
    private boolean          mAutoBitrate = false;
    private String           mPlayUrl = NORMAL_PLAY_URL_720;
    private Button           mButton1080P;
    private Button           mButton720P;
    private Button           mButton540P;
    private Button           mButtonSwitch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lebautobitrate_activity_leb_auto_bitrate);
        if (checkPermission()) {
            initView();
            startPlay();
        }
    }

    @Override
    protected void onPermissionGranted() {
        initView();
    }


    private void initView() {
        mVideoView = findViewById(R.id.tx_cloud_view);
        mButtonSwitch = findViewById(R.id.btn_switch);
        mButton1080P = findViewById(R.id.btn_switch_1080);
        mButton720P = findViewById(R.id.btn_switch_720);
        mButton540P = findViewById(R.id.btn_switch_540);
        mButtonSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAutoBitrate = !mAutoBitrate;
                switchAutoBitrate(mAutoBitrate);
            }
        });

        mButton1080P.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchBitrate(1080);
            }
        });
        mButton720P.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchBitrate(720);
            }
        });
        mButton540P.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchBitrate(540);
            }
        });
    }

    private void switchBitrate(int bitrate) {
        if (mAutoBitrate) {
            return;
        }
        switch (bitrate) {
            case 1080:
                mPlayUrl = NORMAL_PLAY_URL_1080;
                break;
            case 720:
                mPlayUrl = NORMAL_PLAY_URL_720;
                break;
            case 540:
                mPlayUrl = NORMAL_PLAY_URL_540;
                break;
            default:
                mPlayUrl = NORMAL_PLAY_URL_720;
                break;
        }
        if (mAutoBitrate) {
            mLivePlayer.switchStream(mPlayUrl + AUTO_BITRATE_SUFFIX);
        } else {
            mLivePlayer.switchStream(mPlayUrl);
        }
    }

    private void switchAutoBitrate(boolean mAutoBitrate) {
        mLivePlayer.stopPlay();
        if (mAutoBitrate) {
            mPlayUrl = NORMAL_PLAY_URL_540;
            mLivePlayer.startLivePlay(mPlayUrl + AUTO_BITRATE_SUFFIX);
            mButton1080P.setBackgroundResource(R.drawable.common_button_grey_bg);
            mButton720P.setBackgroundResource(R.drawable.common_button_grey_bg);
            mButton540P.setBackgroundResource(R.drawable.common_button_grey_bg);
            mButtonSwitch.setText(R.string.lebautobitrate_switch_stop);
        } else {
            mLivePlayer.startLivePlay(mPlayUrl);
            mButton1080P.setBackgroundResource(R.drawable.common_button_bg);
            mButton720P.setBackgroundResource(R.drawable.common_button_bg);
            mButton540P.setBackgroundResource(R.drawable.common_button_bg);
            mButtonSwitch.setText(R.string.lebautobitrate_switch_start);
        }
    }

    private void stopPlay() {
        if (mLivePlayer != null) {
            if (mLivePlayer != null && mLivePlayer.isPlaying() == 1) {
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
                public void onVideoResolutionChanged(V2TXLivePlayer player, final int width, final int height) {
                     runOnUiThread(new Runnable() {
                         @Override
                         public void run() {
                             Toast.makeText(LebAutoBitrateActivity.this,
                                     "resolution:" + width + "*" + height, Toast.LENGTH_SHORT).show();
                         }
                     });
                }
            });
        }
        mLivePlayer.setRenderView(mVideoView);
        int ret = mLivePlayer.startLivePlay(mPlayUrl);
        Log.i(TAG, "startPlay return: " + ret);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopPlay();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
