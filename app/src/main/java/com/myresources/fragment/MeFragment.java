package com.myresources.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.myresources.R;
import com.myresources.activity.SetLocationActivity;

/**
 * 百度地图定位
 */
public class MeFragment extends Fragment implements View.OnClickListener {
	private TextView LocationResult;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void onStart() {
		super.onStart();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.me_fragment, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		LocationResult = (TextView) getView().findViewById(R.id.textView1);
		LocationResult.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
	switch (v.getId()){
		case R.id.textView1:
			Intent intent = new Intent(getActivity(), SetLocationActivity.class);
			startActivity(intent);
			break;
		default:
			break;
	}
	}
}
