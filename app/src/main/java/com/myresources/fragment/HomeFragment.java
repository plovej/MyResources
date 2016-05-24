package com.myresources.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.myresources.R;

public class HomeFragment extends Fragment{
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
		view = inflater.inflate(R.layout.main_fragment, null);
		main_tv = (TextView) view.findViewById(R.id.main_tv);
		main_tv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				showDialog(getActivity(), "提示", "是否取消支付？",
//						"是",R.color.w_c14, "否",R.color.w_c14,true,new DialogOnClickListener() {
//							@Override
//							public void onSure(View v) {
//								Toast.makeText(getActivity(), "确定", 0).show();
//							}
//							@Override
//							public void onCancle(View v) {
//								Toast.makeText(getActivity(), "取消", 0).show();
//							}
//						});
			}
		});
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
		Log.d("---111-----",  "onResume");
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
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
		Log.d("---111-----",  "onDestroy");
	}
	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		Log.d("---111-----",  "onDetach");
	}
}
