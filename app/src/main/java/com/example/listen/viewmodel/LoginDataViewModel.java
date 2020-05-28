package com.example.listen.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class LoginDataViewModel extends AndroidViewModel {

    public LoginDataViewModel(@NonNull Application application) {
        super(application);
    }

    public boolean isLogin() {

        return false;
    }
}
