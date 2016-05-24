package com.myresources.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 作者：李靖靖 on 2016/5/24 14:48
 * 简介：网络工具类
 */
public class NetWorkUtil {
	public static final int NONE = 0;
	public static final int WIFI = 1;
	public static final int MOBILE = 2;

	/**
	 *  判断有无网络
	 */
	public static boolean getNetWork(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netinfo = cm.getActiveNetworkInfo();
		return null == netinfo;
	}

	/**
	 * 判断WIFI还是流量
	 */
	public static int getNetWorkInfoType(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(cm!=null){
			NetworkInfo info = cm.getActiveNetworkInfo();
			if(info!=null&&info.isConnected()&&info.isAvailable()){
				if(info.getType()==ConnectivityManager.TYPE_WIFI){
					return WIFI;
				}else if(info.getType()==ConnectivityManager.TYPE_MOBILE){
					return MOBILE;
				}
			}else{
				return NONE;
			}
		}else{
			return NONE;
		}
		return NONE;
	}
}