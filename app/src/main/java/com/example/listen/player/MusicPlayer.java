package com.example.listen.player;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.listen.constant.ActionConstant;
import com.example.listen.entity.Material;

import org.apache.commons.lang3.ObjectUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class MusicPlayer {

    private static final MusicPlayer player = new MusicPlayer();

    private static MediaPlayer mediaPlayer;

    private static Material playingMaterial = null;

    private Context context;

    private List<String> testMusic;

    private MusicPlayer() {
        mediaPlayer = new MediaPlayer();
        testMusic = new ArrayList<>();
        testMusic.add("http://m10.music.126.net/20200402215431/932953734851108c4abb166b9215d908/ymusic/26cb/5385/4501/270298cf596d4291c9d1b324f76e6b4d.mp3");
        testMusic.add("http://m10.music.126.net/20200402221144/726e247bbfbef48c18af5f85bbabd7df/ymusic/520f/5108/075e/ee7b522b30fb3344253304ffd17fdbfd.mp3");
        testMusic.add("http://m10.music.126.net/20200402221251/9bc7cba8dc2bf46d6a4abbf0aa4cdfaf/ymusic/589c/3cbb/b0ec/93061daa615b4d0984cc72e5d40be405.mp3");
    }

    /**
     * 尽在测试中使用， 随机返回一首音乐url
     *
     * @return url
     */
    @Deprecated
    private String getOneMusic() {
        int length = testMusic.size();
        Random random = new Random();
        String get = testMusic.get(random.nextInt(length));
        Log.i(TAG, "getOneMusic: " + get);
        return get;
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
            sendPlayStatusChangeBroadcast();
        } else {
            this.playOneNewMusic(onPlay);
        }
    }

    public void intentPlay(Material onPlay) {
        if (ObjectUtils.isNotEmpty(onPlay) && !onPlay.equals(playingMaterial)) {
            this.playOneNewMusic(onPlay);
        }
    }

    private void playOneNewMusic(Material onPlay) {
        try {
            mediaPlayer.reset();
//                mediaPlayer.setDataSource(onPlay.getFileUrl());
            mediaPlayer.setDataSource(getOneMusic());
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(mp -> {
                mp.start();
                playingMaterial = onPlay;
                sendPlayStatusChangeBroadcast();
            });
            mediaPlayer.setOnErrorListener((mp, what, extra) -> {
                Toast.makeText(context, "加载失败", Toast.LENGTH_SHORT).show();
                sendPlayStatusChangeBroadcast();
                return false;
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendPlayStatusChangeBroadcast() {
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
