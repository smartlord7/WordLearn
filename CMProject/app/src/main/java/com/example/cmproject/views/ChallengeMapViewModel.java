package com.example.cmproject.views;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ChallengeMapViewModel extends ViewModel {
    private MutableLiveData<String> data = new MutableLiveData<>();

    public void setData(String value) {
        data.setValue(value);
    }

    public MutableLiveData<String> getData() {
        return data;
    }
}