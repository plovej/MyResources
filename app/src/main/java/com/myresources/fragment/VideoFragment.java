package com.myresources.fragment;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.myresources.R;
import com.myresources.activity.VideoActivity;
import com.myresources.tuwenhunpai.AnalysisJSON;
import com.myresources.tuwenhunpai.Content;
import com.myresources.tuwenhunpai.ImgTxtAdapter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class VideoFragment extends Fragment{
	private View view;
	private TextView video_tv;
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		Log.d("---222-----",  "onCreateView");
//		view = inflater.inflate(R.layout.video_fragment, null);
//		video_tv = (TextView) view.findViewById(R.id.video_tv);
//		video_tv.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(getActivity(), VideoActivity.class);
//				startActivity(intent);
//			}
//		});
//		return view;
//	}

	private String path = "json/news.json";
	List<Content> list;
	private ListView img_txt_list;
	ImgTxtAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.video_fragment, null);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getAssetsData();
			getView().findViewById(R.id.video_tv).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getActivity(), VideoActivity.class);
					startActivity(intent);
				}
			});
		img_txt_list = (ListView)getView().findViewById(R.id.img_txt_list);
		adapter = new ImgTxtAdapter(getContext(), list);
		img_txt_list.setAdapter(adapter);
	}

	public List<Content> getAssetsData(){
		AssetManager am =  getActivity().getAssets();
		try {
			InputStream is = am.open(path);
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			byte[] bt = new byte[1024];
			int len = 0;
			while((len = is.read(bt)) != -1){
				outputStream.write(bt, 0, len);
			}

			outputStream.close();
			is.close();
			String json = outputStream.toString();
			list = AnalysisJSON.getProvinceCities(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
}
