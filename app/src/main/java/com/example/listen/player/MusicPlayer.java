package com.example.listen.player;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;

import androidx.annotation.Nullable;

import com.example.listen.constant.ActionConstant;
import com.example.listen.entity.Material;

import org.apache.commons.lang3.ObjectUtils;

import java.io.IOException;

public class MusicPlayer {

    private static final MusicPlayer player = new MusicPlayer();

    private static MediaPlayer mediaPlayer;

    private static Material playingMaterial = null;

    private Context context;

    private MusicPlayer() {
        mediaPlayer = new MediaPlayer();
    }

    public static MusicPlayer getInstance() {
        return player;
    }

    public void setContext(Context context) {
        this.context = context;
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
//                mediaPlayer.setDataSource(onPlay.getFileUrl());
                mediaPlayer.setDataSource("http://music.163.com/song/media/outer/url?id=562598065.mp3");
                mediaPlayer.prepare();
                mediaPlayer.start();
                playingMaterial = onPlay;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        Intent intent = new Intent(ActionConstant.PLAY_STATUS_CHANGE);
        context.sendBroadcast(intent);
    }

    public Boolean getIsPlaying() {
        return mediaPlayer.isPlaying();
    }

    public Material getPlayingMaterial() {
        return playingMaterial;
    }

}
