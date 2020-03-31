package com.example.listen.player;

import android.media.MediaPlayer;

import java.io.IOException;

public class MusicPlayer {

    private static MusicPlayer player = new MusicPlayer();

    private static Boolean isPlaying;

    private static String playId;

    private static MediaPlayer mediaPlayer;

    private MusicPlayer() {
        mediaPlayer = new MediaPlayer();
    }

    public static MusicPlayer getInstance() {
        return player;
    }

    public static void play(String materialId) throws IOException {
        if (isPlaying) {
            mediaPlayer.pause();
            isPlaying = false;
        } else {

        }
        mediaPlayer.setDataSource(materialId);
        mediaPlayer.prepare();
        mediaPlayer.start();
        isPlaying = true;
        playId = materialId;

        // TODO broadcast 提醒底部按钮修改
    }


}
