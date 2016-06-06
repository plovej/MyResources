package com.myresources.util;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

/**
 * 作者：范展鹏
 * 邮箱：fanzhanpeng@hskbj.com
 */
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(this);
    }
}
