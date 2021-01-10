package com.example.flybuy.Interface;

import com.example.flybuy.Model.SliderImageModel;

import java.util.List;

public interface IFirebaseLoad {
    void onFirebaseLoadSuccess(List<SliderImageModel> sliderList);
    void onFirebaseLoadFailed(String message);
}
