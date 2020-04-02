package com.example.listen.activity.type;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.listen.R;
import com.example.listen.adapter.MaterialListAdapter;
import com.example.listen.constant.ActionConstant;
import com.example.listen.entity.Material;
import com.example.listen.player.MusicPlayer;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Objects;

public class MaterialTypeDetailActivity extends AppCompatActivity {

    private MusicPlayer player = MusicPlayer.getInstance();

    private PlayStatusChangeReceiver receiver;
    MaterialTypeDetailViewModel viewModel;

    private ImageButton button;
    private TextView bottomTitle;
    private TextView bottomTypeName;
    MaterialListAdapter materialListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_type_detail);
        viewModel = new ViewModelProvider(this).get(MaterialTypeDetailViewModel.class);

        Toolbar toolbar = findViewById(R.id.material_type_detail_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setDisplayShowTitleEnabled(false);

        Intent intent = getIntent();
        Long typeId = intent.getLongExtra("typeId", 0);

        initBroadCast();
        initView();
    }

    private void initView() {
        RefreshLayout refreshLayout = findViewById(R.id.refresh_material_type_detail);
        refreshLayout.setRefreshHeader(new ClassicsHeader(getApplicationContext()));
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(refreshLayout1 -> {
            viewModel.refreshMaterial();
            refreshLayout1.finishRefresh(2000/*,false*/);//传入false表示刷新失败
        });

        RecyclerView recyclerView = findViewById(R.id.material_type_detail_list);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(manager);
        materialListAdapter = new MaterialListAdapter(getApplicationContext());
        recyclerView.setAdapter(materialListAdapter);
        viewModel.getMaterial().observe(this, materialListAdapter::setMaterials);
    }

    private void initBroadCast() {
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
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
            materialListAdapter.notifyDataSetChanged();
        }
    }
}
