package com.myresources.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.myresources.R;
import com.myresources.VideoActivity;

public class VideoFragment extends Fragment{
	private View view;
	private TextView video_tv;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d("---222-----",  "onCreateView");
		view = inflater.inflate(R.layout.video_fragment, null);
		video_tv = (TextView) view.findViewById(R.id.video_tv);
		video_tv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), VideoActivity.class);
				startActivity(intent);
			}
		});
		return view;
	}
}
