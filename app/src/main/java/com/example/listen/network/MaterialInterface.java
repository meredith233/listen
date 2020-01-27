package com.example.listen.network;

import com.example.listen.common.MyResponse;
import com.example.listen.entity.Material;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MaterialInterface {

    @GET("/materials")
    Call<MyResponse<List<Material>>> getIndexMaterial();
}
