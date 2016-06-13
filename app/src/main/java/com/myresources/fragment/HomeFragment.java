package com.myresources.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.myresources.R;

public class HomeFragment extends Fragment implements OnGetGeoCoderResultListener {
	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = new MyLocationListener();
	private LocationClientOption mOption,DIYoption;
	GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
	boolean isFirstLoc = true; // 是否首次定位
	BitmapDescriptor mCurrentMarker;
	private MyLocationConfiguration.LocationMode mCurrentMode;
	//获取地图控件引用
	MapView mMapView = null;
	BaiduMap mBaiduMap;
	View view;
	private TextView main_tv;
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		Log.d("---111-----", "onAttach");
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.d("---111-----", "onCreate");

	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d("---111-----", "onCreateView");
		View view = inflater.inflate(R.layout.main_fragment,null);
		mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
		mMapView = (MapView) view.findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);

		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(15));
		// 初始化搜索模块，注册事件监听
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(this);
		return view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		/**
		 * 发起搜索
		 *
		 * @param v
		 */
		  getView().findViewById(R.id.patient_query_ic).setOnClickListener(new View.OnClickListener() {
			 @Override
			 public void onClick(View v) {
				 EditText editGeoCodeKey = (EditText) getView().findViewById(R.id.patient_search);
				 // Geo搜索
				 mSearch.geocode(new GeoCodeOption().city("").address(editGeoCodeKey.getText().toString()));
			 }
		 });
		LocationClientOption mOption = getDefaultLocationClientOption();
		mOption.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);
		mOption.setCoorType("bd09ll");
		mLocationClient = new LocationClient(getContext());     //声明LocationClient类
		mLocationClient.setLocOption(mOption);
		mLocationClient.registerLocationListener( myListener );    //注册监听函数
		mLocationClient.start();
		Log.d("---111-----", "onActivityCreated");
	}


	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.d("---111-----",  "onStart");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
		mMapView.onResume();
		EditText editGeoCodeKey = (EditText) getView().findViewById(R.id.patient_search);
		editGeoCodeKey.setText("");
		Log.d("---111-----",  "onResume");
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
		mMapView.onPause();
		Log.d("---111-----",  "onPause");
	}
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.d("---111-----",  "onStop");
	}
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		Log.d("---111-----",  "onDestroyView");
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mLocationClient.registerLocationListener(myListener);
		mLocationClient.stop();
		//在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
		mMapView.onDestroy();
		Log.d("---111-----",  "onDestroy");
	}
	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		Log.d("---111-----",  "onDetach");
	}


	/***
	 * 定位结果回调，在此方法中处理定位结果
	 */
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation bdLocation) {
			if (bdLocation != null && (bdLocation.getLocType() == 161 || bdLocation.getLocType() == 66)) {
				Message locMsg = locHander.obtainMessage();
				Bundle locData = new Bundle();

//				locData = Algorithm(bdLocation);
//				if (locData != null) {
				locData.putInt("iscalculate", 0);
				locData.putParcelable("loc", bdLocation);
				locMsg.setData(locData);
				locHander.sendMessage(locMsg);
//				}
			}
		}
	}
	BDLocation location =null;
	/***
	 * 接收定位结果消息，并显示在地图上
	 */
	private Handler locHander = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			 location = msg.getData().getParcelable("loc");
//			if (location != null) {
//				LatLng point = new LatLng(location.getLatitude(), location.getLongitude());
//				BitmapDescriptor bitmap = null;
//				bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_openmap_focuse_mark);
//				// 构建MarkerOption，用于在地图上添加Marker
//				OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
//				// 在地图上添加Marker，并显示
//				mBaiduMap.addOverlay(option);
//				mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(point));
//			}
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
							// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
//			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng point = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatus.Builder builder = new MapStatus.Builder();
				builder.target(point).zoom(18.0f);
				mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
//                mCurrentMarker = BitmapDescriptorFactory
//                        .fromResource(R.drawable.icon_openmap_focuse_mark);
				mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
						mCurrentMode, true, mCurrentMarker));
//			}
		}
	};
	public LocationClientOption getDefaultLocationClientOption(){
		if(mOption == null){
			mOption = new LocationClientOption();
			mOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
			mOption.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
			mOption.setScanSpan(0);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
			mOption.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
			mOption.setIsNeedLocationDescribe(true);//可选，设置是否需要地址描述
			mOption.setNeedDeviceDirect(false);//可选，设置是否需要设备方向结果
			mOption.setLocationNotify(false);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
			mOption.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
			mOption.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
			mOption.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
			mOption.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集

		}
		return mOption;
	}

	@Override
	public void onGetGeoCodeResult(GeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(getContext(), "抱歉，未能找到结果", Toast.LENGTH_LONG)
					.show();
			return;
		}
		mBaiduMap.clear();
		mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.icon_gcoding)));
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
				.getLocation()));
		String strInfo = String.format("纬度：%f 经度：%f",
				result.getLocation().latitude, result.getLocation().longitude);
		Toast.makeText(getContext(), strInfo, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(getContext(), "抱歉，未能找到结果", Toast.LENGTH_LONG)
					.show();
			return;
		}
		mBaiduMap.clear();
		mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.icon_gcoding)));
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
				.getLocation()));
		Toast.makeText(getContext(), result.getAddress(),
				Toast.LENGTH_LONG).show();

	}
}
