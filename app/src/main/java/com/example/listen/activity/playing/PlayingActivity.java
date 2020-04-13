package com.example.listen.activity.playing;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.listen.R;
import com.example.listen.common.PlayBackStatusEnum;
import com.example.listen.common.PlayModeEnum;
import com.example.listen.constant.ActionConstant;
import com.example.listen.entity.Material;
import com.example.listen.lrc.LrcView;
import com.example.listen.player.MusicPlayer;
import com.example.listen.player.VoiceRecorder;

import org.apache.commons.lang3.ObjectUtils;

import java.time.LocalTime;
import java.util.Objects;

import lombok.SneakyThrows;

public class PlayingActivity extends AppCompatActivity {

    private MusicPlayer player = MusicPlayer.getInstance();
    private VoiceRecorder recorder = VoiceRecorder.getInstance();

    private PlayStatusChangeReceiver receiver;

    private Material onPlayMaterial = null;

    private LrcView lrcView;
    private SeekBar seekBar;
    private ImageButton playButton;
    private TextView title;
    private RelativeLayout playPanel;

    private RelativeLayout controlPanel;
    private ImageButton controlRecord;
    private ImageButton controlToPlay;
    private ImageButton controlToPlayBack;
    private LocalTime recordEndTime;

    private PlayBackStatusEnum playBackStatus;
    private long startTime;
    private long endTime;
    private int line;
    private int maxLine;

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @SneakyThrows
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void run() {
            if (player.getIsPlaying()) {
                seekBar.setMax(player.getDuration());
                long time = player.getCurrentPosition();
                if (playBackStatus.equals(PlayBackStatusEnum.DISABLE)) {
                    lrcView.updateTime(time);
                    seekBar.setProgress((int) time);
                }
                if (playBackStatus == PlayBackStatusEnum.PLAYING) {
                    resetControlButton();
                    if (time >= endTime) {
                        player.pause();
                        playBackStatus = PlayBackStatusEnum.RECORDING;
                        recorder.start();
                        recordEndTime = LocalTime.now().plusSeconds(30);
                        controlToPlay.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                        controlRecord.setImageResource(R.drawable.ic_fiber_manual_record_red_24dp);
                        Toast.makeText(getApplicationContext(), "录音中...", Toast.LENGTH_LONG).show();
                    }

                }
            } else {
                switch (playBackStatus) {
                    case RECORDING:
                        if (LocalTime.now().isAfter(recordEndTime)) {
                            recorder.stopRecord();
                            recorder.playBack();
                            playBackStatus = PlayBackStatusEnum.PLAYBACK;
                            controlRecord.setImageResource(R.drawable.ic_fiber_manual_record_black_24dp);
                            controlToPlayBack.setImageResource(R.drawable.ic_play_arrow_red_24dp);
                            Toast.makeText(getApplicationContext(), "回放中...", Toast.LENGTH_LONG).show();
                        }
                        break;
                    case PLAYBACK:
                        if (recorder.isEnd()) {
                            player.seekTo((int) startTime);
                            player.start();
                            resetControlButton();
                            playBackStatus = PlayBackStatusEnum.PLAYING;
                        }
                        break;
                    default:
                        break;
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
        playPanel = findViewById(R.id.play_panel);
        ImageButton singleLinePlayButton = findViewById(R.id.playing_single_line);
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
            onPlayMaterial = onPlay;
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

        controlPanel = findViewById(R.id.control_panel);
        controlPanel.setVisibility(View.INVISIBLE);
        controlToPlay = findViewById(R.id.control_to_play);
        controlToPlay.setOnClickListener(v -> {
            player.seekTo((int) startTime);
            resetControlButton();
        });
        ImageButton controlToPreviousLine = findViewById(R.id.control_to_previous_line);
        controlToPreviousLine.setOnClickListener(v -> {
            if (line - 1 >= 0) {
                line--;
            }
            startTime = lrcView.getLineStartTime(line);
            endTime = lrcView.getLineStartTime(line + 1);
            player.seekTo((int) startTime);
            lrcView.updateTime(startTime);
            resetControlButton();
            playBackStatus = PlayBackStatusEnum.PLAYING;
        });
        controlRecord = findViewById(R.id.control_record);
        controlRecord.setOnClickListener(v -> {
            if (PlayBackStatusEnum.RECORDING.equals(playBackStatus)) {
                recorder.stopRecord();
                recorder.playBack();
                playBackStatus = PlayBackStatusEnum.PLAYBACK;
                controlRecord.setImageResource(R.drawable.ic_fiber_manual_record_black_24dp);
                controlToPlayBack.setImageResource(R.drawable.ic_play_arrow_red_24dp);
                Toast.makeText(getApplicationContext(), "回放中...", Toast.LENGTH_LONG).show();
            }
        });
        ImageButton controlToNextLine = findViewById(R.id.control_to_next_line);
        controlToNextLine.setOnClickListener(v -> {
            if (line + 1 < maxLine) {
                line++;
            }
            startTime = lrcView.getLineStartTime(line);
            endTime = lrcView.getLineStartTime(line + 1);
            player.seekTo((int) startTime);
            lrcView.updateTime(startTime);
            resetControlButton();
            playBackStatus = PlayBackStatusEnum.PLAYING;
        });
        controlToPlayBack = findViewById(R.id.control_to_play_back);
        controlToPlayBack.setOnClickListener(v -> {

        });
        ImageButton controlExit = findViewById(R.id.control_record_exit);
        controlExit.setOnClickListener(v -> {
            playPanel.setVisibility(View.VISIBLE);
            controlPanel.setVisibility(View.INVISIBLE);
            recorder.exit();
            playBackStatus = PlayBackStatusEnum.DISABLE;
            startTime = endTime = -1;
            player.setPlayMode(PlayModeEnum.LIST_LOOP);
            player.start();
        });

        singleLinePlayButton.setOnClickListener(v -> {
            player.setPlayMode(PlayModeEnum.SINGLE_LINE);
            playPanel.setVisibility(View.INVISIBLE);
            controlPanel.setVisibility(View.VISIBLE);
            playBackStatus = PlayBackStatusEnum.PLAYING;
            line = lrcView.getCenterLineNum();
            maxLine = lrcView.getMaxLineNum();
            startTime = lrcView.getLineStartTime(line);
            endTime = lrcView.getLineStartTime(line + 1);
            lrcView.updateTime(startTime);
            player.seekTo((int) startTime);
            resetControlButton();
        });
    }

    private void resetControlButton() {
        controlToPlay.setImageResource(R.drawable.ic_play_arrow_red_24dp);
        controlRecord.setImageResource(R.drawable.ic_fiber_manual_record_black_24dp);
        controlToPlayBack.setImageResource(R.drawable.ic_play_arrow_black_24dp);
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
            if (ObjectUtils.isNotEmpty(onPlay) && !onPlay.equals(onPlayMaterial)) {
                lrcView.loadLrc(onPlay.getLyricsContent());
                title.setText(onPlay.getName());
            }
        }
    }
}
