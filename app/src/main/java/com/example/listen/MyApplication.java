package com.example.listen;

import android.app.Application;

import lombok.Data;

@Data
public class MyApplication extends Application {

    private Boolean isPlaying;

    @Override
    public void onCreate() {
        isPlaying = false;
        super.onCreate();
    }
}
