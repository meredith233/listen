package com.example.listen.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.listen.R;
import com.example.listen.adapter.MaterialListAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

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
        final MaterialListAdapter adapter = new MaterialListAdapter(root.getContext());
        recyclerView.setAdapter(adapter);
        homeViewModel.getMaterial().observe(getViewLifecycleOwner(), adapter::setMaterials);

        return root;
    }
}