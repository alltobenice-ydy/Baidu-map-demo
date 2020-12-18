package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;


public class MainActivity extends AppCompatActivity {
    private MapView mMapView = null;
    private BaiduMap mBaiduMap = null;
    private LocationClient mLocationClient = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        SDKInitializer.setCoordType(CoordType.BD09LL);
        setContentView(R.layout.activity_main);

        mMapView = (MapView) findViewById(R.id.bmapView);
        mMapView.removeViewAt(1);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, null));

        //定位初始化
        mLocationClient = new LocationClient(this);

        //通过LocationClientOption设置LocationClient相关参数
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);

        //设置locationClientOption
        mLocationClient.setLocOption(option);

        //注册LocationListener监听器
        MyLocationListener myLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myLocationListener);

        //开启地图定位图层
        mLocationClient.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        mLocationClient.stop();
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
    }

    private class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //mapView 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }

            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(location.getDirection())
                    .latitude(39.90)   //location.getLatitude()
                    .longitude(116.4) //location.getLongitude()
                    .build();

            mBaiduMap.setMyLocationData(locData);
        }
    }
}


