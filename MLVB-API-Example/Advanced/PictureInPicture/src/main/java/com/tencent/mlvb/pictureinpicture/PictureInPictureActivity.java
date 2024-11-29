package com.tencent.mlvb.pictureinpicture;

import android.app.PictureInPictureParams;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Rational;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.tencent.mlvb.pictureinpicture.R;
import com.tencent.live2.V2TXLivePlayer;
import com.tencent.live2.V2TXLivePlayerObserver;
import com.tencent.live2.impl.V2TXLivePlayerImpl;
import com.tencent.mlvb.common.MLVBBaseActivity;
import com.tencent.rtmp.ui.TXCloudVideoView;

public class PictureInPictureActivity extends MLVBBaseActivity {

    private static final String TAG = PictureInPictureActivity.class.getSimpleName();
    private static final String PLAY_URL = "http://liteavapp.qcloud.com/live/liteavdemoplayerstreamid.flv";
    private TXCloudVideoView mVideoView;
    private V2TXLivePlayer mLivePlayer;
    private ImageView mButtonBack;
    private Button mButtonEnablePictureInPicture;
    private Button mButtonPause;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_in_picture);
        if (checkPermission()) {
            initView();
            initPlayer();
            startPlay();
        }
    }

    @Override
    protected void onPermissionGranted() {
        initView();
        initPlayer();
        startPlay();
    }

    private void initView() {
        mVideoView = findViewById(R.id.video_view);
        mButtonBack = findViewById(R.id.iv_back);
        mButtonEnablePictureInPicture = findViewById(R.id.btn_enable_picture_in_picture);
        mButtonPause = findViewById(R.id.btn_first_pause);

        mButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mButtonEnablePictureInPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPictureInPicture();
            }
        });

        mButtonPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLivePlayer.isPlaying() == 1) {
                    stopPlay();
                    mButtonPause.setText(R.string.resume);
                } else {
                    startPlay();
                    mButtonPause.setText(R.string.pause);
                }
            }
        });
    }

    private void initPlayer() {
        mLivePlayer = new V2TXLivePlayerImpl(this);
        mLivePlayer.setRenderView(mVideoView);
    }

    private void startPictureInPicture() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            PictureInPictureParams.Builder pictureInPictureBuilder = new PictureInPictureParams.Builder();
            Rational aspectRatio = new Rational(mVideoView.getWidth(), mVideoView.getHeight());
            pictureInPictureBuilder.setAspectRatio(aspectRatio);
            enterPictureInPictureMode(pictureInPictureBuilder.build());
        } else {
            Toast.makeText(this, R.string.picture_in_picture_not_supported, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode, Configuration configuration) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, configuration);
        if (isInPictureInPictureMode) {
            mButtonEnablePictureInPicture.setVisibility(View.GONE);
            mButtonPause.setVisibility(View.GONE);
        } else {
            mButtonEnablePictureInPicture.setVisibility(View.VISIBLE);
            mButtonPause.setVisibility(View.VISIBLE);
        }
    }

    private void stopPlay() {
        mLivePlayer.stopPlay();
    }

    private void startPlay() {
        int ret = mLivePlayer.startLivePlay(PLAY_URL);
        Log.i(TAG, "startPlay return: " + ret);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopPlay();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}