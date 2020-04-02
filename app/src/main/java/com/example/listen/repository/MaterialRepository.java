package com.example.listen.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.listen.common.MyResponse;
import com.example.listen.constant.GlobalConst;
import com.example.listen.entity.Material;
import com.example.listen.entity.MaterialType;
import com.example.listen.network.MaterialInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static androidx.constraintlayout.widget.Constraints.TAG;

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

    public void getMaterialForMainPage(MutableLiveData<List<Material>> materials) {
        Call<MyResponse<List<Material>>> call = request.getIndexMaterial();
        call.enqueue(new Callback<MyResponse<List<Material>>>() {
            @Override
            public void onResponse(@NonNull Call<MyResponse<List<Material>>> call, Response<MyResponse<List<Material>>> response) {
                MyResponse<List<Material>> myResponse = response.body();
                materials.postValue(myResponse.getObject());
            }

            @Override
            public void onFailure(@NonNull Call<MyResponse<List<Material>>> call, Throwable t) {
                Log.i(TAG, "onFailure: getMaterial", t);
            }
        });
    }

    public void getMaterialTypeForMainPage(MutableLiveData<List<MaterialType>> materialTypes) {
        Call<MyResponse<List<MaterialType>>> call = request.getIndexMaterialType();
        call.enqueue(new Callback<MyResponse<List<MaterialType>>>() {
            @Override
            public void onResponse(Call<MyResponse<List<MaterialType>>> call, Response<MyResponse<List<MaterialType>>> response) {
                MyResponse<List<MaterialType>> myResponse = response.body();
                materialTypes.postValue(myResponse.getObject());
            }

            @Override
            public void onFailure(Call<MyResponse<List<MaterialType>>> call, Throwable t) {
                Log.i(TAG, "onFailure: getMaterialType", t);
            }
        });
    }

    public void getMaterialTypeById(MutableLiveData<MaterialType> materialType, Long typeId) {
        Call<MyResponse<MaterialType>> call = request.getTypeById(typeId);
        call.enqueue(new Callback<MyResponse<MaterialType>>() {
            @Override
            public void onResponse(@NonNull Call<MyResponse<MaterialType>> call, Response<MyResponse<MaterialType>> response) {
                MyResponse<MaterialType> myResponse = response.body();
                materialType.postValue(myResponse.getObject());
            }

            @Override
            public void onFailure(@NonNull Call<MyResponse<MaterialType>> call, Throwable t) {
                Log.i(TAG, "onFailure: getMaterial", t);
            }
        });
    }

    public void listMaterialByTypeId(MutableLiveData<List<Material>> materials, Long typeId) {
        Call<MyResponse<List<Material>>> call = request.listMaterialByTypeId(typeId);
        call.enqueue(new Callback<MyResponse<List<Material>>>() {
            @Override
            public void onResponse(@NonNull Call<MyResponse<List<Material>>> call, Response<MyResponse<List<Material>>> response) {
                MyResponse<List<Material>> myResponse = response.body();
                materials.postValue(myResponse.getObject());
            }

            @Override
            public void onFailure(@NonNull Call<MyResponse<List<Material>>> call, Throwable t) {
                Log.i(TAG, "onFailure: getMaterial", t);
            }
        });
    }
}
