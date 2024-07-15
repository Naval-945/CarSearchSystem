package com.dingwei.wifi.api.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LocationViewModel extends ViewModel {

    private final MutableLiveData<LocationInfo> locationInfo = new MutableLiveData<>();

    public LiveData<LocationInfo> getLocationInfo() {
        return locationInfo;
    }

    //在此处locationInfo发生变化 在MainActivity中监听
    public void setLocationInfo(LocationInfo locationinfo) {
        locationInfo.setValue(locationinfo);
    }

    // Singleton pattern for LocationViewModel
    private static LocationViewModel locationViewModelInstance;

    public static synchronized LocationViewModel getInstance() {
        if (locationViewModelInstance == null) {
            locationViewModelInstance = new LocationViewModel();
        }
        return locationViewModelInstance;
    }

}
