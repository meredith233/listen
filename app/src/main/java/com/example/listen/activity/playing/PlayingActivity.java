package com.example.listen.activity.playing;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.listen.R;
import com.example.listen.constant.ActionConstant;
import com.example.listen.entity.Material;
import com.example.listen.player.MusicPlayer;

import org.apache.commons.lang3.ObjectUtils;

public class PlayingActivity extends AppCompatActivity {

    private MusicPlayer player = MusicPlayer.getInstance();

    private PlayStatusChangeReceiver receiver;

    private ImageButton playButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);

        initView();
        initBroadcast();
    }

    private void initView() {
        playButton = findViewById(R.id.playing_play_button);
        playButton.setOnClickListener(v -> {
            player.buttonPlay();
            int id = player.getIsPlaying() ? R.drawable.ic_pause_black_24dp : R.drawable.ic_play_arrow_black_24dp;
            playButton.setImageResource(id);
        });

        int id = player.getIsPlaying() ? R.drawable.ic_pause_black_24dp : R.drawable.ic_play_arrow_black_24dp;
        playButton.setImageResource(id);
    }

    private void initBroadcast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ActionConstant.PLAY_STATUS_CHANGE);
        receiver = new PlayStatusChangeReceiver();
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    class PlayStatusChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int id = player.getIsPlaying() ? R.drawable.ic_pause_black_24dp : R.drawable.ic_play_arrow_black_24dp;
            playButton.setImageResource(id);

            Material onPlay = player.getPlayingMaterial();
            if (ObjectUtils.isNotEmpty(onPlay)) {

            }
        }
    }
}
