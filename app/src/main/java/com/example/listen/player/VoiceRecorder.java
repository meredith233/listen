package com.example.listen.player;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;

import java.io.File;

public class VoiceRecorder {

    private static final VoiceRecorder recorder = new VoiceRecorder();

    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private String filePath;

    private Context context;

    private VoiceRecorder() {
        mediaRecorder = new MediaRecorder();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(MediaPlayer::start);
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public static VoiceRecorder getInstance() {
        return recorder;
    }

    public void start() {
        try {
            String fileName = "cache.m4a";
            File destDir = new File(Environment.getExternalStorageDirectory() + "/test/");
            if (!destDir.exists()) {
                destDir.mkdirs();
            }
            filePath = Environment.getExternalStorageDirectory() + "/cache/" + fileName;
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mediaRecorder.setOutputFile(filePath);
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void playBack() {
        try {
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isEnd() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            return true;
        }
        return false;
    }

    public void stopRecord() {
        mediaRecorder.stop();
        mediaRecorder.release();
    }
}
