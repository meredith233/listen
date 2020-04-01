package com.example.listen.ui.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.listen.R;
import com.example.listen.adapter.MaterialListAdapter;
import com.example.listen.adapter.MaterialTypeListAdapter;
import com.example.listen.constant.ActionConstant;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private MaterialListAdapter materialListAdapter;
    private PlayStatusChangeReceiver receiver;
    private Context context;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        context = root.getContext();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ActionConstant.PLAY_STATUS_CHANGE);
        receiver = new PlayStatusChangeReceiver();
        context.registerReceiver(receiver, intentFilter);

        RefreshLayout refreshLayout = root.findViewById(R.id.refresh_main);
        refreshLayout.setRefreshHeader(new ClassicsHeader(root.getContext()));
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(refreshLayout1 -> {
            homeViewModel.refreshMaterial();
            refreshLayout1.finishRefresh(2000/*,false*/);//传入false表示刷新失败
        });

        RecyclerView recyclerView = root.findViewById(R.id.recycler_view_main);
        LinearLayoutManager manager = new LinearLayoutManager(root.getContext());
        recyclerView.setLayoutManager(manager);
        materialListAdapter = new MaterialListAdapter(root.getContext());
        recyclerView.setAdapter(materialListAdapter);
        homeViewModel.getMaterial().observe(getViewLifecycleOwner(), materialListAdapter::setMaterials);

        RecyclerView materialTypeRecyclerView = root.findViewById(R.id.recycler_view_main_material_type);
        LinearLayoutManager materialTypeManager = new LinearLayoutManager(root.getContext());
        materialTypeManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        materialTypeRecyclerView.setLayoutManager(materialTypeManager);
        final MaterialTypeListAdapter materialTypeListAdapter = new MaterialTypeListAdapter(root.getContext());
        materialTypeRecyclerView.setAdapter(materialTypeListAdapter);
        homeViewModel.getMaterialType().observe(getViewLifecycleOwner(), materialTypeListAdapter::setMaterials);

        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        context.unregisterReceiver(receiver);
    }

    class PlayStatusChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            materialListAdapter.notifyDataSetChanged();
        }
    }
}