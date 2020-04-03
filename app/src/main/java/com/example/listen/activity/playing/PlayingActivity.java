package com.example.listen.activity.playing;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.listen.R;
import com.example.listen.constant.ActionConstant;
import com.example.listen.entity.Material;
import com.example.listen.player.MusicPlayer;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Objects;

public class PlayingActivity extends AppCompatActivity {

    private MusicPlayer player = MusicPlayer.getInstance();

    private PlayStatusChangeReceiver receiver;

    private ImageButton playButton;
    private ImageButton nextButton;
    private ImageButton previousButton;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);
        Toolbar toolbar = findViewById(R.id.playing_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        initView();
        initBroadcast();
    }

    private void initView() {
        previousButton = findViewById(R.id.playing_previous_button);
        previousButton.setOnClickListener(v -> player.previous());
        nextButton = findViewById(R.id.playing_next_button);
        nextButton.setOnClickListener(v -> player.next());
        playButton = findViewById(R.id.playing_play_button);
        playButton.setOnClickListener(v -> {
            player.buttonPlay();
            int id = player.getIsPlaying() ? R.drawable.ic_pause_black_24dp : R.drawable.ic_play_arrow_black_24dp;
            playButton.setImageResource(id);
        });

        int id = player.getIsPlaying() ? R.drawable.ic_pause_black_24dp : R.drawable.ic_play_arrow_black_24dp;
        playButton.setImageResource(id);

        title = findViewById(R.id.playing_title);
        Material onPlay = player.getPlayingMaterial();
        if (ObjectUtils.isNotEmpty(onPlay)) {
            title.setText(onPlay.getName());
        }
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    class PlayStatusChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int id = player.getIsPlaying() ? R.drawable.ic_pause_black_24dp : R.drawable.ic_play_arrow_black_24dp;
            playButton.setImageResource(id);

            Material onPlay = player.getPlayingMaterial();
            if (ObjectUtils.isNotEmpty(onPlay)) {
                title.setText(onPlay.getName());
            }
        }
    }
}
