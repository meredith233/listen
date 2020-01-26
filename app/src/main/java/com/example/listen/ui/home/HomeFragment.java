package com.example.listen.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.listen.R;
import com.example.listen.adapter.MaterialListAdapter;
import com.example.listen.common.MyResponse;
import com.example.listen.constant.GlobalConst;
import com.example.listen.entity.Material;
import com.example.listen.network.MaterialInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, textView::setText);

        RecyclerView recyclerView = root.findViewById(R.id.recycler_view_main);
        LinearLayoutManager manager = new LinearLayoutManager(root.getContext());
        recyclerView.setLayoutManager(manager);
        final MaterialListAdapter adapter = new MaterialListAdapter(root.getContext());
        recyclerView.setAdapter(adapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GlobalConst.NET) // 设置网络请求的公共Url地址
                .addConverterFactory(GsonConverterFactory.create()) // 设置数据解析器
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // 支持RxJava平台
                .build();
        MaterialInterface request = retrofit.create(MaterialInterface.class);
        Call<MyResponse<List<Material>>> call = request.getIndexMaterial();
        call.enqueue(new Callback<MyResponse<List<Material>>>() {
            @Override
            public void onResponse(Call<MyResponse<List<Material>>> call, Response<MyResponse<List<Material>>> response) {
                MyResponse<List<Material>> myResponse = response.body();
                List<Material> materials = myResponse.getObject();
                adapter.setWords(materials);
            }

            @Override
            public void onFailure(Call<MyResponse<List<Material>>> call, Throwable t) {
                //do nothing
            }
        });

        return root;
    }
}