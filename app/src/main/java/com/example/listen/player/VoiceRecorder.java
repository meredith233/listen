package com.example.listen.player;

public class VoiceRecorder {

    private static final VoiceRecorder recorder = new VoiceRecorder();

    private VoiceRecorder() {

    }

    public static VoiceRecorder getInstance() {
        return recorder;
    }
}
