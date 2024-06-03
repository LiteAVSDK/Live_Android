package com.tencent.liteav.sdk.lebplay;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.tencent.mlvb.common.MLVBBaseActivity;

/**
 * WebRTC Playback Entrance View
 * - For the playback view, see {@link LebPlayActivity}.
 */
public class LebPlayEnterActivity extends MLVBBaseActivity {

    private EditText mEditStreamId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lebplay_activity_leb_play_enter);
        initView();
    }

    private void initView() {
        mEditStreamId = findViewById(R.id.et_stream_id);

        mEditStreamId.setText(generateStreamId());
        findViewById(R.id.btn_play_webrtc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPlay();
            }
        });
    }

    private void startPlay() {
        String streamId = mEditStreamId.getText().toString();
        if (TextUtils.isEmpty(streamId)) {
            Toast.makeText(LebPlayEnterActivity.this, getString(R.string.lebplay_please_input_streamid),
                    Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(LebPlayEnterActivity.this, LebPlayActivity.class);
            intent.putExtra("STREAM_ID", streamId);
            startActivity(intent);
        }
    }

    @Override
    protected void onPermissionGranted() {

    }
}
