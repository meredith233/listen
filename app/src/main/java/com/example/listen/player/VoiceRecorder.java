package com.example.listen.player;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;

import java.io.File;

public class VoiceRecorder {

    private static final VoiceRecorder recorder = new VoiceRecorder();

    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private String filePath;

    private VoiceRecorder() {
        String fileName = "cache.m4a";
        File destDir = new File(Environment.getExternalStorageDirectory() + "/test/");
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        filePath = Environment.getExternalStorageDirectory() + "/cache/" + fileName;
        mediaRecorder = new MediaRecorder();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(MediaPlayer::start);
    }

    public static VoiceRecorder getInstance() {
        return recorder;
    }

    public void start() {
        try {
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
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isEnd() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            return true;
        }
        return false;
    }

    public void stopRecord() {
        mediaRecorder.stop();
        mediaRecorder.reset();
    }

    public void exit() {
        mediaPlayer.release();
        mediaRecorder.release();
    }

    public void stopPlay() {
        mediaPlayer.stop();
    }
}
