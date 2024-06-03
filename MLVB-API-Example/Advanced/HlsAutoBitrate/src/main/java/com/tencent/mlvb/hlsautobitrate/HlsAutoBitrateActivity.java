package com.tencent.mlvb.hlsautobitrate;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.tencent.live2.V2TXLiveDef;
import com.tencent.live2.V2TXLivePlayer;
import com.tencent.live2.V2TXLivePlayerObserver;
import com.tencent.live2.impl.V2TXLivePlayerImpl;
import com.tencent.mlvb.common.MLVBBaseActivity;
import com.tencent.mlvb.hlsautobitrate.R;
import com.tencent.rtmp.ui.TXCloudVideoView;

import java.util.ArrayList;

/**
 * HLS Auto Bitrate
 *  MLVB APP HLS Auto Bitrate
 *  1、Set Render View API:[self.livePlayer setRenderView:self.view];
 *  2、Start Play API: [self.livePlayer startLivePlay:url];
 * Documentation: https://cloud.tencent.com/document/product/454/81211
 */
public class HlsAutoBitrateActivity extends MLVBBaseActivity {
    private static final String TAG = HlsAutoBitrateActivity.class.getSimpleName();
    private static final String DEFAULT_PLAY_URL_HLS = "http://liteavapp.qcloud.com/live/"
            + "liteavdemoplayerstreamid_autoAdjust.m3u8";

    private TXCloudVideoView                            mVideoView;
    private V2TXLivePlayer                              mLivePlayer;
    private String                                      mPlayUrl     = DEFAULT_PLAY_URL_HLS;
    private ArrayList<V2TXLiveDef.V2TXLiveStreamInfo>   mPlayUrlList = new ArrayList();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hlsautobitrate_activity_hls_auto_bitrate);
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
        findViewById(R.id.btn_switch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchAutoBitrate();
            }
        });

        findViewById(R.id.btn_switch_1080).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchBitrate(1080);
            }
        });
        findViewById(R.id.btn_switch_720).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchBitrate(720);
            }
        });
        findViewById(R.id.btn_switch_540).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchBitrate(540);
            }
        });
    }

    private void switchBitrate(int bitrate) {
        if (mPlayUrlList == null || mPlayUrlList.size() == 0) {
            Log.d(TAG, "liveplayer getStreamList return empty");
            return;
        }
        for (V2TXLiveDef.V2TXLiveStreamInfo streamInfo : mPlayUrlList) {
            if (streamInfo != null && streamInfo.height != 0 && streamInfo.width != 0
                    && !TextUtils.isEmpty(streamInfo.url)
                    && min(streamInfo.height, streamInfo.width) == bitrate) {
                mPlayUrl = streamInfo.url;
                mLivePlayer.switchStream(mPlayUrl);
            }
        }
    }

    private void switchAutoBitrate() {
        mPlayUrl = DEFAULT_PLAY_URL_HLS;
        mLivePlayer.switchStream(mPlayUrl);
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
                            Toast.makeText(HlsAutoBitrateActivity.this,
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

    public int min(int value1, int value2) {
        if (value1 > value2) {
            return value2;
        }
        return value1;
    }
}
