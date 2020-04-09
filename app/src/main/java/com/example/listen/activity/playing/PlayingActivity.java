package com.example.listen.activity.playing;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.listen.R;
import com.example.listen.common.PlayBackStatusEnum;
import com.example.listen.constant.ActionConstant;
import com.example.listen.entity.Material;
import com.example.listen.lrc.LrcView;
import com.example.listen.player.MusicPlayer;
import com.example.listen.player.VoiceRecorder;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Objects;

public class PlayingActivity extends AppCompatActivity {

    private MusicPlayer player = MusicPlayer.getInstance();
    private VoiceRecorder recorder = VoiceRecorder.getInstance();

    private PlayStatusChangeReceiver receiver;

    private LrcView lrcView;
    private SeekBar seekBar;
    private ImageButton playButton;
    private TextView title;

    private PlayBackStatusEnum playBackStatus;
    private long startTime;
    private long endTime;

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (player.getIsPlaying()) {
                seekBar.setMax(player.getDuration());
                long time = player.getCurrentPosition();

                if (PlayBackStatusEnum.DISABLE.equals(playBackStatus)) {
                    lrcView.updateTime(time);
                    seekBar.setProgress((int) time);
                } else {
                    if (time >= endTime) {
                        player.pause();
                        playBackStatus = PlayBackStatusEnum.RECORDING;
                        recorder.start();
                    }
                }
            }

            handler.postDelayed(this, 300);
        }
    };

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

        playBackStatus = PlayBackStatusEnum.DISABLE;
        startTime = endTime = -1;
        initView();
        initBroadcast();
    }

    private void initBroadcast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ActionConstant.PLAY_STATUS_CHANGE);
        receiver = new PlayStatusChangeReceiver();
        registerReceiver(receiver, intentFilter);
    }

    private void initView() {
        lrcView = findViewById(R.id.lrc_view);
        seekBar = findViewById(R.id.progress_bar);
        ImageButton previousButton = findViewById(R.id.playing_previous_button);
        previousButton.setOnClickListener(v -> player.previous());
        ImageButton nextButton = findViewById(R.id.playing_next_button);
        nextButton.setOnClickListener(v -> player.next());
        playButton = findViewById(R.id.playing_play_button);
        playButton.setOnClickListener(v -> {
            player.buttonPlay();
            int id = player.getIsPlaying() ? R.drawable.ic_pause_black_24dp : R.drawable.ic_play_arrow_black_24dp;
            playButton.setImageResource(id);

        });

        handler.post(runnable);
        int id = player.getIsPlaying() ? R.drawable.ic_pause_black_24dp : R.drawable.ic_play_arrow_black_24dp;
        playButton.setImageResource(id);

        title = findViewById(R.id.playing_title);
        Material onPlay = player.getPlayingMaterial();
        if (ObjectUtils.isNotEmpty(onPlay)) {
            title.setText(onPlay.getName());
            lrcView.loadLrc(onPlay.getLyricsContent());
            if (player.getIsPlaying()) {
                seekBar.setMax(player.getDuration());
            }
        }

        lrcView.setDraggable(true, time -> {
            player.seekTo((int) time);
            if (!player.getIsPlaying()) {
                player.start();
                handler.post(runnable);
            }
            return true;
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                player.seekTo(seekBar.getProgress());
                lrcView.updateTime(seekBar.getProgress());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        handler.removeCallbacks(runnable);
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
                lrcView.loadLrc(onPlay.getLyricsContent());
                title.setText(onPlay.getName());
            }
        }
    }
}
