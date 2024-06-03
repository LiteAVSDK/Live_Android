package com.tencent.mlvb.apiexample;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.tencent.liteav.sdk.lebplay.LebPlayEnterActivity;
import com.tencent.mlvb.customvideocapture.CustomVideoCaptureActivity;
import com.tencent.mlvb.demo.R;
import com.tencent.mlvb.hlsautobitrate.HlsAutoBitrateActivity;
import com.tencent.mlvb.lebautobitrate.LebAutoBitrateActivity;
import com.tencent.mlvb.linkpk.LivePKEnterActivity;
import com.tencent.mlvb.livelink.LiveLinkEnterActivity;
import com.tencent.mlvb.liveplay.LivePlayEnterActivity;
import com.tencent.mlvb.livepushcamera.LivePushCameraEnterActivity;
import com.tencent.mlvb.livepushscreen.LivePushScreenEnterActivity;
import com.tencent.mlvb.newtimeshiftspriite.NewTimeShiftSpriteActivity;
import com.tencent.mlvb.rtcpushandplay.RTCPushAndPlayEnterActivity;
import com.tencent.mlvb.switchrenderview.SwitchRenderViewActivity;
import com.tencent.mlvb.thirdbeauty.ThirdBeautyFaceUnityActivity;
import com.tencent.mlvb.thirdbeauty.ThirdBeautyEntranceActivity;
import com.tencent.mlvb.timeshift.TimeShiftActivity;

/**
 * MLVB API-Example Main View
 *
 * Features
 * Basic features:
 * - Publishing from camera {@link LivePushCameraEnterActivity}
 * - Publishing from screen {@link LivePushScreenEnterActivity}
 * - Playback {@link LivePlayEnterActivity}
 * - Co-anchoring {@link LiveLinkEnterActivity}
 * - Competition {@link LivePKEnterActivity}
 *
 * Advanced features:
 * - Dynamically switching rendering controls {@link SwitchRenderViewActivity}
 * - Custom video capturing {@link CustomVideoCaptureActivity}
 * - Third-party beauty filters {@link ThirdBeautyFaceUnityActivity}
 * - RTC co-anchoring + ultra-low-latency playback {@link RTCPushAndPlayEnterActivity}
 *
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.launch_view).setVisibility(View.GONE);
            }
        }, 1000);

        findViewById(R.id.ll_push_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LivePushCameraEnterActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.ll_push_screen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LivePushScreenEnterActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.ll_play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LivePlayEnterActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.ll_leb_play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LebPlayEnterActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.ll_link).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LiveLinkEnterActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.ll_pk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LivePKEnterActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.ll_switch_render_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SwitchRenderViewActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.ll_custom_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CustomVideoCaptureActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.ll_third_beauty).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ThirdBeautyEntranceActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.ll_cloud_transcoding).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RTCPushAndPlayEnterActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.ll_leb_auto_play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LebAutoBitrateActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.ll_hls_auto_play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, HlsAutoBitrateActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.ll_time_shift).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TimeShiftActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.ll_new_time_shift_sprite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewTimeShiftSpriteActivity.class);
                startActivity(intent);
            }
        });
    }

}
