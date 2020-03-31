package com.example.listen.player;

import android.media.MediaPlayer;

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

    public void play() {
        try {
            if (!isPlaying) {
                mediaPlayer.setDataSource("http://music.163.com/song/media/outer/url?id=562598065.mp3");
                mediaPlayer.prepare();
                mediaPlayer.start();
                isPlaying = true;
            } else {
                mediaPlayer.pause();
                isPlaying = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        // TODO broadcast 提醒底部按钮修改
    }

    public Boolean getIsPlaying() {
        return isPlaying;
    }
}
