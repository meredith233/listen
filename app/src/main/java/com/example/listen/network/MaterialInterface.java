package com.example.listen.network;

import com.example.listen.common.MyResponse;
import com.example.listen.entity.Material;
import com.example.listen.entity.MaterialType;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MaterialInterface {

    /**
     * 随机获取6个听力材料
     *
     * @return 听力材料列表
     */
    @GET("/materials")
    Call<MyResponse<List<Material>>> getIndexMaterial();

    @GET("/material/type/list")
    Call<MyResponse<List<MaterialType>>> getIndexMaterialType();

    @GET("/material/type/{id}")
    Call<MyResponse<MaterialType>> getTypeById(@Path("id") Long id);

    @GET("/materials/type/{id}")
    Call<MyResponse<List<Material>>> listMaterialByTypeId(@Path("id") Long id);
}
