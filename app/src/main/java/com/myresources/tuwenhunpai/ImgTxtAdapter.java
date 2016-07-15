package com.myresources.tuwenhunpai;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.myresources.R;

import java.util.HashMap;
import java.util.List;

public class ImgTxtAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inf;
	private List<Content> list;

	public ImgTxtAdapter(Context context, List<Content> list){
		this.context = context;
		this.list = list;

		inf = LayoutInflater.from(this.context);
	}

	@Override
	public int getCount() {
		return list != null ? list.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

//	@Override
//	public boolean areAllItemsEnabled() {
//		return false;
//	} 

	@Override
	public boolean isEnabled(int position) { //����listView��ÿ��item�ĸ���Ч��
		return false;
	}
	public HashMap<Integer,View> lmap = new HashMap<Integer,View>();
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MyView mv;
		if(lmap.get(position) == null){
			mv = new MyView();
			convertView = inf.inflate(R.layout.img_txt_item, null);
			mv.img = (ImageButton)convertView.findViewById(R.id.img);
			mv.txt = (TextView)convertView.findViewById(R.id.txt);
			mv.course_main_videoplayview = convertView.findViewById(R.id.course_main_videoplayview);
			lmap.put(position, convertView);
			convertView.setTag(mv);

		} else {
			convertView = lmap.get(position);
			mv = (MyView) convertView.getTag();
		}

		Content content = list.get(position);
		if(content.isImg()){
			mv.img.setBackgroundResource(R.drawable.note_pic_loading);
			mv.txt.setVisibility(View.GONE);
			mv.course_main_videoplayview.setVisibility(View.GONE);
		} else if(content.isVideo_()){
			mv.img.setVisibility(View.GONE);
			mv.txt.setVisibility(View.GONE);
		}else {lmap.put(position, convertView);
			mv.txt.setText(content.getDetails());
			mv.img.setVisibility(View.GONE);
			mv.course_main_videoplayview.setVisibility(View.GONE);
		}
		return convertView;
	}

	class MyView {
		ImageButton img;
		TextView txt;
		View course_main_videoplayview;
	}
}
