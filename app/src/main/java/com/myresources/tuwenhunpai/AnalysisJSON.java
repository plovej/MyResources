package com.myresources.tuwenhunpai;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AnalysisJSON {

	public static List<Content> getProvinceCities(String json){
		List<Content> list = new ArrayList<Content>();
		
		try {
			JSONArray jsonArray = new JSONArray(json);
			int count = jsonArray.length();
			for(int i=0; i < count; i++){
				JSONObject object = jsonArray.getJSONObject(i);
				String detail = object.getString("detail");
				boolean img = false;
				boolean video = false;
				if(detail.indexOf(".jpg") != -1){
					img = true;
				}
				if(detail.indexOf(".mp4") != -1){
					video = true;
				}
				Content content = new Content(detail, img, video);
				
				list.add(content);
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		} 
		
		return list;
	}
}
