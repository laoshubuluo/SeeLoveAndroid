package com.tianyu.seelove.ui.activity.message;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.tianyu.seelove.R;
import com.tianyu.seelove.ui.activity.base.BaseActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;

public class PositionActivity extends BaseActivity implements OnGetGeoCoderResultListener {
    private MapView mMapView;
    BaiduMap mBaiduMap;
    BitmapDescriptor mCurrentMarker;
    // 定位相关
    LocationClient mLocClient;
    private LocationMode mCurrentMode;
    GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
    private double lat, lng;
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        lng = getIntent().getDoubleExtra("lng", 0);
        lat = getIntent().getDoubleExtra("lat", 0);
        address = getIntent().getStringExtra("address");
        initView();
    }

    private void initView() {
        TextView titleView = (TextView) findViewById(R.id.titleView);
        titleView.setText(getString(R.string.location_information));
        ImageView backView = (ImageView) findViewById(R.id.leftBtn);
        backView.setVisibility(View.VISIBLE);
        backView.setOnClickListener(this);
        mMapView = (MapView) findViewById(R.id.mMapView);
        mBaiduMap = mMapView.getMap();
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(17f);
        mBaiduMap.setMapStatus(msu);
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(this);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();
        // 罗盘模式：LocationMode.COMPASS;普通模式：LocationMode.NORMAL;跟随模式：LocationMode.FOLLOWING
        // mCurrentMarker：传入null则，恢复默认图标;出入图片则进行自定义marker
        // mCurrentMarker = BitmapDescriptorFactory
        // .fromResource(R.drawable.ic_launcher);
        mCurrentMode = LocationMode.COMPASS;
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
                mCurrentMode, true, mCurrentMarker));
        // 初始化搜索模块，注册事件监听
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);
        LatLng ll = new LatLng(lat, lng);
        // 反Geo搜索
        mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(ll));
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
        mBaiduMap.animateMapStatus(u);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.leftBtn:
                finish();
                break;
        }
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult arg0) {
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(PositionActivity.this, getString(R.string.search_no_result), Toast.LENGTH_LONG)
                    .show();
            return;
        }
        mBaiduMap.clear();
        MarkerOptions ooA = new MarkerOptions().position(result.getLocation())
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.position_icon)).zIndex(9)
                .draggable(true);
        // 掉下动画
//        ooA.animateType(MarkerAnimateType.drop);
        // 自定义其他弹出覆盖物
        Button button = new Button(getApplicationContext());
        button.setBackgroundResource(R.mipmap.popup);
        button.setText(address);
        button.setPadding(10, 0, 10, 10);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2, 30);
        button.setLayoutParams(params);
        // 定义用于显示该InfoWindow的坐标点
        LatLng pt = result.getLocation();
        // 创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
        InfoWindow mInfoWindow = new InfoWindow(button, pt, -47);
        // 显示InfoWindow
        mBaiduMap.showInfoWindow(mInfoWindow);
        mBaiduMap.addOverlay(ooA);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
                .getLocation()));
    }

    @Override
    protected void onResume() {
        // 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        mSearch.destroy();
        super.onDestroy();
    }
}
