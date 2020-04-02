package com.example.listen.player;

import android.content.Context;
import android.media.MediaRecorder;

public class VoiceRecorder {

    private static final VoiceRecorder recorder = new VoiceRecorder();

    private MediaRecorder mediaRecorder;

    private Context context;

    private VoiceRecorder() {
        mediaRecorder = new MediaRecorder();
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public static VoiceRecorder getInstance() {
        return recorder;
    }
}
