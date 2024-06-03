package com.tencent.liteav.sdk.lebplay;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.tencent.live2.V2TXLiveDef;
import com.tencent.live2.V2TXLivePlayer;
import com.tencent.live2.V2TXLivePlayerObserver;
import com.tencent.live2.impl.V2TXLivePlayerImpl;
import com.tencent.mlvb.common.MLVBBaseActivity;
import com.tencent.mlvb.common.URLUtils;
import com.tencent.rtmp.ui.TXCloudVideoView;

import java.util.Random;

/**
 * Playback View
 * Features:
 * - Start playback {@link LebPlayActivity#startPlay()}
 * - Mute {@link LebPlayActivity#mute()}
 * For more information, please see the integration document {https://cloud.tencent.com/document/product/454/68195}.
 */
public class LebPlayActivity extends MLVBBaseActivity implements View.OnClickListener {
    private static final String TAG = "LebPlayActivity";

    private TXCloudVideoView mPlayRenderView;
    private V2TXLivePlayer   mLivePlayer;
    private boolean          mPlayFlag = false;
    private Button           mButtonMute;
    private TextView         mTextTitle;

    private String  mStreamId;
    private boolean mPlayAudioFlag = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lebplay_activity_leb_play);
        if (checkPermission()) {
            initIntentData();
            initView();
            startPlay();
        }
    }

    @Override
    protected void onPermissionGranted() {
        initView();
        startPlay();
    }

    private void initIntentData() {
        mStreamId = getIntent().getStringExtra("STREAM_ID");
    }


    private void initView() {
        mPlayRenderView = findViewById(R.id.play_tx_cloud_view);
        mButtonMute = findViewById(R.id.btn_mute);
        mTextTitle = findViewById(R.id.tv_title);

        mButtonMute.setOnClickListener(this);
        findViewById(R.id.iv_back).setOnClickListener(this);
        if (!TextUtils.isEmpty(mStreamId)) {
            mTextTitle.setText(mStreamId);
        }
    }

    private void startPlay() {
        mLivePlayer = new V2TXLivePlayerImpl(LebPlayActivity.this);
        mLivePlayer.setRenderView(mPlayRenderView);
        mLivePlayer.setObserver(new V2TXLivePlayerObserver() {

            @Override
            public void onError(V2TXLivePlayer player, int code, String msg, Bundle extraInfo) {
                Log.d(TAG,
                        "[Player] onError: player-" + player + " code-" + code + " msg-" + msg + " info-" + extraInfo);
            }

            @Override
            public void onVideoLoading(V2TXLivePlayer player, Bundle extraInfo) {
                Log.i(TAG, "[Player] onVideoLoading: player-" + player + ", extraInfo-" + extraInfo);
            }

            @Override
            public void onVideoPlaying(V2TXLivePlayer player, boolean firstPlay, Bundle extraInfo) {
                Log.i(TAG,
                        "[Player] onVideoPlaying: player-" + player + " firstPlay-" + firstPlay + " info-" + extraInfo);
            }

            @Override
            public void onVideoResolutionChanged(V2TXLivePlayer player, int width, int height) {
                Log.i(TAG, "[Player] onVideoResolutionChanged: player-" + player + " width-" + width + " height-"
                        + height);
            }

            @Override
            public void onWarning(V2TXLivePlayer v2TXLivePlayer, int i, String s, Bundle bundle) {
                Log.d(TAG, "[Player] Override: player-" + v2TXLivePlayer + ", i-" + i + ", s-" + s);
            }

            @Override
            public void onRenderVideoFrame(V2TXLivePlayer player, V2TXLiveDef.V2TXLiveVideoFrame v2TXLiveVideoFrame) {
                super.onRenderVideoFrame(player, v2TXLiveVideoFrame);
                Log.d(TAG,
                        "[Player] onRenderVideoFrame: player-" + player + ", v2TXLiveVideoFrame-" + v2TXLiveVideoFrame);
            }
        });

        String userId = String.valueOf(new Random().nextInt(10000));
        String playURL = URLUtils.generatePlayUrl(mStreamId, userId, 4);
        int result = mLivePlayer.startLivePlay(playURL);
        if (result == 0) {
            mPlayFlag = true;
        }
        Log.d(TAG, "startLivePlay : " + result);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLivePlayer != null) {
            if (mPlayFlag) {
                mLivePlayer.stopPlay();
            }
            mLivePlayer = null;
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.iv_back) {
            finish();
        } else if (id == R.id.btn_mute) {
            mute();
        }
    }

    private void mute() {
        if (mLivePlayer != null && mLivePlayer.isPlaying() == 1) {
            if (mPlayAudioFlag) {
                mLivePlayer.pauseAudio();
                mPlayAudioFlag = false;
                mButtonMute.setText(R.string.lebplay_cancel_mute);
            } else {
                mLivePlayer.resumeAudio();
                mPlayAudioFlag = true;
                mButtonMute.setText(R.string.lebplay_mute);
            }
        }
    }
}
