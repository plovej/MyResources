package com.myresources.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.myresources.R;

public class VideoFragment extends Fragment{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d("---222-----",  "onCreateView");
		return inflater.inflate(R.layout.video_fragment, null);
	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		Log.d("---222-----",  "onAttach");
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.d("---222-----",  "onCreate");
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		Log.d("---222-----",  "onActivityCreated");
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.d("---222-----",  "onStart");
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d("---222-----",  "onResume");
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.d("---222-----",  "onPause");
	}
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.d("---222-----",  "onStop");
	}
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		Log.d("---222-----",  "onDestroyView");
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d("---222-----",  "onDestroy");
	}
	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		Log.d("---222-----",  "onDetach");
	}
}
