package com.dingwei.wifi.ui;

import android.graphics.PointF;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.dingwei.wifi.LocationMarkerView;
import com.dingwei.wifi.NavigationView;
import com.dingwei.wifi.R;
import com.dingwei.wifi.api.model.LocationViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private LocationMarkerView locationMarkerView;
    private LocationViewModel locationViewModel;
    private NavigationView navigationView;

    public String currentSpot;


    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationMarkerView = findViewById(R.id.locationMarkerView);
//        locationViewModel = new ViewModelProvider(this).get(LocationViewModel.class);
        locationViewModel = LocationViewModel.getInstance();
        navigationView = findViewById(R.id.navigationView);
//        navigationView.initOrangePoints();

        //直到这里都是LocationInfo（带有目前停车位信息）
        locationViewModel.getLocationInfo().observe(this, locationInfo -> {
            if (locationInfo != null) {
                Log.i(TAG, "locationInfo changed!!");
                //就修改定位标记位置
                updateMarkerLocation(locationInfo.getX(), locationInfo.getY());
                currentSpot = locationInfo.getParkingSpot();
                Log.d(TAG, "currentSpot is " + currentSpot);
            }
        });

        findViewById(R.id.button1).setOnClickListener(v -> navigateToSpot("A1"));
        findViewById(R.id.button2).setOnClickListener(v -> navigateToSpot("A2"));
        findViewById(R.id.button3).setOnClickListener(v -> navigateToSpot("A3"));
        findViewById(R.id.button4).setOnClickListener(v -> navigateToSpot("A4"));
        findViewById(R.id.buttonG).setOnClickListener(v -> navigateToSpot("GATE"));


    }

    // 更新定位标记的位置
    public void updateMarkerLocation(float x, float y) {
        locationMarkerView.updateLocation(x, y);
    }

    private void navigateToSpot(String spot) {

        PointF currentLocation = locationMarkerView.getLocation();

        if(currentSpot.equals("R")){
            PointF targetOrangePoint = navigationView.getNearestOrangePoint(spot);
            Log.i(TAG, "Two points: " + currentLocation + " " + targetOrangePoint);
            navigationView.clearRoutes();
            navigationView.addRoute(currentLocation, targetOrangePoint);
        }else{
            PointF currentOrangePoint = navigationView.getNearestOrangePoint(currentSpot);
            PointF targetOrangePoint = navigationView.getNearestOrangePoint(spot);
            Log.i(TAG, "Three points: " + currentLocation + " " + currentOrangePoint + " " + targetOrangePoint);
            navigationView.clearRoutes(); // 清除现有路线
            navigationView.addRoute(currentLocation, currentOrangePoint, targetOrangePoint);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy()");
    }
}