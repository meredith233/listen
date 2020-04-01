package com.example.listen.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.listen.R;
import com.example.listen.constant.ActionConstant;
import com.example.listen.entity.Material;
import com.example.listen.player.MusicPlayer;

import org.apache.commons.lang3.ObjectUtils;

public class MaterialTypeDetailActivity extends AppCompatActivity {

    private MusicPlayer player = MusicPlayer.getInstance();

    private PlayStatusChangeReceiver receiver;

    private ImageButton button;
    private TextView bottomTitle;
    private TextView bottomTypeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_type_detail);

        Intent intent = getIntent();
        Long typeId = intent.getLongExtra("typeId", 0);

        init();
    }

    private void init() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ActionConstant.PLAY_STATUS_CHANGE);
        receiver = new PlayStatusChangeReceiver();
        registerReceiver(receiver, intentFilter);

        button = findViewById(R.id.play_button_bottom);
        button.setOnClickListener(v -> {
            player.play(null);
            int id = player.getIsPlaying() ? R.drawable.ic_pause_black_24dp : R.drawable.ic_play_arrow_black_24dp;
            button.setImageResource(id);
        });
        bottomTitle = findViewById(R.id.material_title_bottom);
        bottomTypeName = findViewById(R.id.material_type_bottom);

        int id = player.getIsPlaying() ? R.drawable.ic_pause_black_24dp : R.drawable.ic_play_arrow_black_24dp;
        button.setImageResource(id);

        Material onPlay = player.getPlayingMaterial();
        if (ObjectUtils.isNotEmpty(onPlay)) {
            bottomTitle.setText(onPlay.getName());
            bottomTypeName.setText(onPlay.getTypeName());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    class PlayStatusChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int id = player.getIsPlaying() ? R.drawable.ic_pause_black_24dp : R.drawable.ic_play_arrow_black_24dp;
            button.setImageResource(id);

            Material onPlay = player.getPlayingMaterial();
            if (ObjectUtils.isNotEmpty(onPlay)) {
                bottomTitle.setText(onPlay.getName());
                bottomTypeName.setText(onPlay.getTypeName());
            }
        }
    }
}
