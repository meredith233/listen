package com.example.listen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.listen.activity.BaseActivity;
import com.example.listen.activity.playing.PlayingActivity;
import com.example.listen.activity.user.InfoActivity;
import com.example.listen.adapter.MaterialListAdapter;
import com.example.listen.adapter.MaterialTypeListAdapter;
import com.example.listen.common.ActivityController;
import com.example.listen.constant.ActionConstant;
import com.example.listen.entity.Material;
import com.example.listen.player.MusicPlayer;
import com.example.listen.utils.PermissionUtils;
import com.example.listen.viewmodel.HomeViewModel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import org.apache.commons.lang3.ObjectUtils;

public class MainActivity extends BaseActivity {

    private AppBarConfiguration mAppBarConfiguration;

    private static Long duration;

    private MusicPlayer player = MusicPlayer.getInstance();

    private PlayStatusChangeReceiver receiver;

    private ImageButton button;
    private TextView bottomTitle;
    private TextView bottomTypeName;

    private HomeViewModel homeViewModel;
    private MaterialListAdapter materialListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        player.setContext(this);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ActionConstant.PLAY_STATUS_CHANGE);
        receiver = new PlayStatusChangeReceiver();
        registerReceiver(receiver, intentFilter);

        LinearLayout linearLayout = findViewById(R.id.bottom_sheet);
        linearLayout.setOnClickListener(v -> {
            if (bottomTitle.getText().equals(getResources().getString(R.string.bottom_title_placeholder))) {

            } else {
                Intent intent = new Intent();
                intent.setClass(this, PlayingActivity.class);
                startActivity(intent);
            }
        });

        button = findViewById(R.id.play_button_bottom);
        button.setOnClickListener(v -> {
            player.buttonPlay();
            int id = player.getIsPlaying() ? R.drawable.ic_pause_black_24dp : R.drawable.ic_play_arrow_black_24dp;
            button.setImageResource(id);
        });

        bottomTitle = findViewById(R.id.material_title_bottom);
        bottomTypeName = findViewById(R.id.material_type_bottom);

        PermissionUtils requestPermission = new PermissionUtils();
        requestPermission.RequestPermission(this);

        initView();
    }

    private void initView() {
        RefreshLayout refreshLayout = findViewById(R.id.refresh_main);
        refreshLayout.setRefreshHeader(new ClassicsHeader(getBaseContext()));
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(refreshLayout1 -> {
            homeViewModel.refreshMaterial();
            refreshLayout1.finishRefresh(2000/*,false*/);//传入false表示刷新失败
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_view_main);
        LinearLayoutManager manager = new LinearLayoutManager(getBaseContext());
        recyclerView.setLayoutManager(manager);
        materialListAdapter = new MaterialListAdapter(getBaseContext());
        recyclerView.setAdapter(materialListAdapter);
        homeViewModel.getMaterial().observe(this, materialListAdapter::setMaterials);

        RecyclerView materialTypeRecyclerView = findViewById(R.id.recycler_view_main_material_type);
        LinearLayoutManager materialTypeManager = new LinearLayoutManager(getBaseContext());
        materialTypeManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        materialTypeRecyclerView.setLayoutManager(materialTypeManager);
        final MaterialTypeListAdapter materialTypeListAdapter = new MaterialTypeListAdapter(getBaseContext());
        materialTypeRecyclerView.setAdapter(materialTypeListAdapter);
        homeViewModel.getMaterialType().observe(this, materialTypeListAdapter::setMaterials);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_setting) {
            Toast.makeText(this, "这里是菜单1", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getBaseContext(), InfoActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (duration == null || Math.abs(duration - System.currentTimeMillis()) > 1000) {
                Toast.makeText(getApplicationContext(), "再点击一次退出", Toast.LENGTH_SHORT).show();
                duration = System.currentTimeMillis();
                return false;
            } else {
                ActivityController.finishAll();
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    class PlayStatusChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            materialListAdapter.notifyDataSetChanged();
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
