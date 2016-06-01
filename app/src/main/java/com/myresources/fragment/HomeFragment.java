package com.myresources.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.myresources.R;

public class HomeFragment extends Fragment{
	//获取地图控件引用
	MapView mMapView = null;
	BaiduMap mBaiduMap;
	View view;
	private TextView main_tv;
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		Log.d("---111-----",  "onAttach");
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.d("---111-----",  "onCreate");
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d("---111-----",  "onCreateView");
		View view = inflater.inflate(R.layout.main_fragment,null);
		mMapView = (MapView) view.findViewById(R.id.bmapView);
		return view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		Log.d("---111-----",  "onActivityCreated");
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
}
