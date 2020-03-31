package com.example.listen.player;

import android.media.MediaPlayer;

import androidx.annotation.Nullable;

import com.example.listen.entity.Material;

import org.apache.commons.lang3.ObjectUtils;

import java.io.IOException;

public class MusicPlayer {

    private static final MusicPlayer player = new MusicPlayer();

    private static MediaPlayer mediaPlayer;

    private static Material playingMaterial = null;

    private MusicPlayer() {
        mediaPlayer = new MediaPlayer();
    }

    public static MusicPlayer getInstance() {
        return player;
    }

    public void play(@Nullable Material onPlay) {
        if (ObjectUtils.isEmpty(onPlay) || onPlay.equals(playingMaterial)) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            } else {
                mediaPlayer.start();
            }
        } else {
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource("http://music.163.com/song/media/outer/url?id=562598065.mp3");
                mediaPlayer.prepare();
                mediaPlayer.start();
                playingMaterial = onPlay;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



        // TODO broadcast 提醒底部按钮修改
    }

    public Boolean getIsPlaying() {
        return mediaPlayer.isPlaying();
    }
}
