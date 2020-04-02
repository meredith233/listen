package com.example.listen.activity.type;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.listen.entity.Material;
import com.example.listen.entity.MaterialType;
import com.example.listen.repository.MaterialRepository;

import java.util.List;

public class MaterialTypeDetailViewModel extends AndroidViewModel {

    private MutableLiveData<List<Material>> mMaterial;

    private MutableLiveData<MaterialType> mMaterialType;

    private MaterialRepository materialRepository;

    private Long typeId;

    public MaterialTypeDetailViewModel(@NonNull Application application) {
        super(application);
        materialRepository = new MaterialRepository();
        mMaterial = new MutableLiveData<>();
        mMaterialType = new MutableLiveData<>();
    }

    void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public LiveData<List<Material>> getMaterial() {
        return mMaterial;
    }

    LiveData<MaterialType> getMaterialType() {
        return mMaterialType;
    }

    void refreshMaterialType() {
        materialRepository.listMaterialByTypeId(mMaterial, typeId);
        materialRepository.getMaterialTypeById(mMaterialType, typeId);
    }
}
