package com.example.listen.repository;

import androidx.lifecycle.MutableLiveData;

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

public class MaterialRepository {

    private MaterialInterface request;

    public MaterialRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GlobalConst.NET) // 设置网络请求的公共Url地址
                .addConverterFactory(GsonConverterFactory.create()) // 设置数据解析器
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // 支持RxJava平台
                .build();
        // 创建 网络请求接口 的实例
        request = retrofit.create(MaterialInterface.class);

    }

    public void get(MutableLiveData<List<Material>> materials) {
        Call<MyResponse<List<Material>>> call = request.getIndexMaterial();
        call.enqueue(new Callback<MyResponse<List<Material>>>() {
            @Override
            public void onResponse(Call<MyResponse<List<Material>>> call, Response<MyResponse<List<Material>>> response) {
                MyResponse<List<Material>> myResponse = response.body();
                materials.postValue(myResponse.getObject());
            }

            @Override
            public void onFailure(Call<MyResponse<List<Material>>> call, Throwable t) {

            }
        });
    }
}
