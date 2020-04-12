package com.example.listen.player;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.text.format.DateFormat;

import java.io.File;
import java.util.Calendar;
import java.util.Locale;

public class VoiceRecorder {

    private static final VoiceRecorder recorder = new VoiceRecorder();

    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;

    private Context context;

    private VoiceRecorder() {
        mediaRecorder = new MediaRecorder();
//        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
//        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

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
            String fileName = DateFormat.format("yyyyMMdd_HHmmss", Calendar.getInstance(Locale.CHINA)) + ".m4a";
            File destDir = new File(Environment.getExternalStorageDirectory() + "/test/");
            if (!destDir.exists()) {
                destDir.mkdirs();
            }
            String filePath = Environment.getExternalStorageDirectory() + "/test/" + fileName;

            mediaRecorder.setOutputFile(filePath);
            mediaPlayer.setDataSource(filePath);
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
