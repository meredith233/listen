package com.example.listen.ui.home;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.listen.entity.Material;
import com.example.listen.repository.MaterialRepository;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {

    private MutableLiveData<List<Material>> mMaterial;

    private MaterialRepository materialRepository;

    public HomeViewModel(Application application) {
        super(application);
        materialRepository = new MaterialRepository();
        mMaterial = new MutableLiveData<>();
        materialRepository.get(mMaterial);
    }


    public LiveData<List<Material>> getMaterial() {
        return mMaterial;
    }

    public void refreshMaterial() {
        materialRepository.get(mMaterial);
    }

}