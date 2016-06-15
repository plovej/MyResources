package com.myresources.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.navisdk.adapter.BNOuterTTSPlayerCallback;
import com.baidu.navisdk.adapter.BNaviSettingManager;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.baidu.navisdk.adapter.BaiduNaviManager.NaviInitListener;
import com.myresources.R;
import com.myresources.baidu.PathplanningActivity;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class HomeFragment extends Fragment implements OnGetGeoCoderResultListener {
	//获取地图控件引用
	MapView mMapView = null;
	BaiduMap mBaiduMap;
	public LocationClient mLocationClient = null;
	private UiSettings mUiSettings;//设置控制功能
	private MyLocationConfiguration.LocationMode mCurrentMode;//设置定位显示样式
	GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
	public BDLocationListener myListener = new MyLocationListener(); //注册监听函数
	private LocationClientOption mOption;//配置定位SDK参数
	public static List<Activity> activityList = new LinkedList<Activity>();
	//应用程序文件夹名称
	private static final String APP_FOLDER_NAME = "BNSDKSimpleDemo";
	public static final String ROUTE_PLAN_NODE = "routePlanNode";
	public static final String SHOW_CUSTOM_ITEM = "showCustomItem";
	public static final String RESET_END_NODE = "resetEndNode";
	public static final String VOID_MODE = "voidMode";

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.d("---111-----", "onAttach");
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("---111-----", "onCreate");
		if (initDirs()) {
			initNavi();
		}
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d("---111-----", "onCreateView");
		return inflater.inflate(R.layout.main_fragment,null);
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		this.setUpView();
		Log.d("---111-----", "onActivityCreated");
	}

	/**
	 * 初始化百度地图参数
	 */
	private  void  setUpView(){
		mCurrentMode = MyLocationConfiguration.LocationMode.FOLLOWING;//跟随样式 COMPASS 罗盘 FOLLOWING 跟随 NORMAL 普通
		mMapView = (MapView) getView().findViewById(R.id.bmapView);

		mBaiduMap = mMapView.getMap();
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		//是否启用旋转手势
		mUiSettings = mBaiduMap.getUiSettings();
		mUiSettings.setRotateGesturesEnabled(false);

		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(15));
		// 初始化搜索模块，注册事件监听
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(this);
		mUiSettings = mBaiduMap.getUiSettings();
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
		mLocationClient.registerLocationListener(myListener);    //注册监听函数
		mLocationClient.start();

		//路线的点击事件
		getView().findViewById(R.id.head_img).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), PathplanningActivity.class);
				startActivity(intent);
			}
		});

	}

	@Override
	public void onStart() {
		super.onStart();
		Log.d("---111-----",  "onStart");
	}

	@Override
	public void onResume() {
		super.onResume();
		//在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
		mMapView.onResume();
		EditText editGeoCodeKey = (EditText) getView().findViewById(R.id.patient_search);
		editGeoCodeKey.setText("");
		Log.d("---111-----",  "onResume");
	}

	@Override
	public void onPause() {
		super.onPause();
		//在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
		mMapView.onPause();
		Log.d("---111-----",  "onPause");
	}
	@Override
	public void onStop() {
		super.onStop();
		Log.d("---111-----",  "onStop");
	}
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Log.d("---111-----",  "onDestroyView");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mLocationClient.registerLocationListener(myListener);
		mLocationClient.stop();
		//在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
		mMapView.onDestroy();
		Log.d("---111-----",  "onDestroy");
	}
	@Override
	public void onDetach() {
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
	BitmapDescriptor mCurrentMarker;
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
				LatLng point = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatus.Builder builder = new MapStatus.Builder();
				builder.target(point).zoom(18.0f);
				mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
//                mCurrentMarker = BitmapDescriptorFactory
//                        .fromResource(R.drawable.icon_openmap_focuse_mark);
				mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
						mCurrentMode, true, mCurrentMarker));
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
	//根据输入的信息来查询所在的位置
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

	//根据输入的坐标来查询所在的位置
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


	private String mSDCardPath = null;
	private boolean initDirs() {
		mSDCardPath = getSdcardDir();
		if (mSDCardPath == null) {
			return false;
		}
		File f = new File(mSDCardPath, APP_FOLDER_NAME);
		if (!f.exists()) {
			try {
				f.mkdir();
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
	private String getSdcardDir() {
		if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
			return Environment.getExternalStorageDirectory().toString();
		}
		return null;
	}

	String authinfo = null;
	private void initNavi() {

		BNOuterTTSPlayerCallback ttsCallback = null;

		BaiduNaviManager.getInstance().init(getActivity(), mSDCardPath, APP_FOLDER_NAME, new NaviInitListener() {
			@Override
			public void onAuthResult(int status, String msg) {
				if (0 == status) {
					authinfo = "key校验成功!";
				} else {
					authinfo = "key校验失败, " + msg;
				}
				getActivity().runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(getActivity(), authinfo, Toast.LENGTH_LONG).show();
					}
				});
			}

			public void initSuccess() {
				Toast.makeText(getActivity(), "百度导航引擎初始化成功", Toast.LENGTH_SHORT).show();
				initSetting();
			}

			public void initStart() {
				Toast.makeText(getActivity(), "百度导航引擎初始化开始", Toast.LENGTH_SHORT).show();
			}

			public void initFailed() {
				Toast.makeText(getActivity(), "百度导航引擎初始化失败", Toast.LENGTH_SHORT).show();
			}


		},  null, ttsHandler, null);

	}
	//导航中部分设置项的设置
	private void initSetting(){
		BNaviSettingManager.setDayNightMode(BNaviSettingManager.DayNightMode.DAY_NIGHT_MODE_DAY);
		BNaviSettingManager.setShowTotalRoadConditionBar(BNaviSettingManager.PreViewRoadCondition.ROAD_CONDITION_BAR_SHOW_ON);
		BNaviSettingManager.setVoiceMode(BNaviSettingManager.VoiceMode.Veteran);
		BNaviSettingManager.setPowerSaveMode(BNaviSettingManager.PowerSaveMode.DISABLE_MODE);
		BNaviSettingManager.setRealRoadCondition(BNaviSettingManager.RealRoadCondition.NAVI_ITS_ON);
	}

	/**
	 * 内部TTS播报状态回传handler
	 */
	private Handler ttsHandler = new Handler() {
		public void handleMessage(Message msg) {
			int type = msg.what;
			switch (type) {
				case BaiduNaviManager.TTSPlayMsgType.PLAY_START_MSG: {
					showToastMsg("Handler : TTS play start");
					break;
				}
				case BaiduNaviManager.TTSPlayMsgType.PLAY_END_MSG: {
					showToastMsg("Handler : TTS play end");
					break;
				}
				default :
					break;
			}
		}
	};

	/**
	 * 内部TTS播报状态回调接口
	 */
	private BaiduNaviManager.TTSPlayStateListener ttsPlayStateListener = new BaiduNaviManager.TTSPlayStateListener() {

		@Override
		public void playEnd() {
//	            showToastMsg("TTSPlayStateListener : TTS play end");
		}

		@Override
		public void playStart() {
//	            showToastMsg("TTSPlayStateListener : TTS play start");
		}
	};

	public void showToastMsg(final String msg) {
		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
			}
		});
	}

}
