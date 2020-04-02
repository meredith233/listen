package com.example.listen.player;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.widget.Toast;

import com.example.listen.constant.ActionConstant;
import com.example.listen.entity.Material;

import java.io.IOException;
import java.util.List;

public class MusicPlayer {

    private static final MusicPlayer player = new MusicPlayer();

    private static MediaPlayer mediaPlayer;

    private Context context;

    private List<Material> playList;

    private Integer position;

    private MusicPlayer() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(mp -> {
            next();
        });
        mediaPlayer.setOnPreparedListener(mp -> {
            mp.start();
            sendPlayStatusChangeBroadcast();
        });
        mediaPlayer.setOnErrorListener((mp, what, extra) -> {
            Toast.makeText(context, "加载失败", Toast.LENGTH_SHORT).show();
            sendPlayStatusChangeBroadcast();
            return false;
        });

        position = 0;
    }

    public static MusicPlayer getInstance() {
        return player;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    // 该方法用于底部按钮
    public void buttonPlay() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        } else {
            mediaPlayer.start();
        }
        sendPlayStatusChangeBroadcast();
    }

    // 该方法用于list内按钮
    public void buttonPlay(Material onPlay, List<Material> playList) {
        if (onPlay.equals(getPlayingMaterial())) {
            if (this.getIsPlaying()) {
                mediaPlayer.pause();
            } else {
                mediaPlayer.start();
            }
        } else {
            this.play(onPlay, playList);
        }
    }

    // 该方法用于直接点击list
    public void play(Material onPlay, List<Material> playList) {
        if (!onPlay.equals(getPlayingMaterial())) {
            this.playOneNewMusic(onPlay);
        } else if (!getIsPlaying()) {
            mediaPlayer.start();
        }
        refreshPlayList(onPlay, playList);
    }

    private void playOneNewMusic(Material onPlay) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(onPlay.getFileUrl());
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
        sendPlayStatusChangeBroadcast();
    }

    private void sendPlayStatusChangeBroadcast() {
        Intent intent = new Intent(ActionConstant.PLAY_STATUS_CHANGE);
        context.sendBroadcast(intent);
    }

    public Boolean getIsPlaying() {
        return mediaPlayer.isPlaying();
    }

    public Material getPlayingMaterial() {
        try {
            return playList.get(position);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void playByPosition() {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(playList.get(position).getFileUrl());
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
        sendPlayStatusChangeBroadcast();
    }

    public void next() {
        position = (position + 1) % playList.size();
        playByPosition();
    }

    public void previous() {
        position = (position - 1 + playList.size()) % playList.size();
        playByPosition();
    }

    private void refreshPlayList(Material onPlay, List<Material> newPlayList) {
        this.playList = newPlayList;
        this.position = newPlayList.indexOf(onPlay);
    }

}
