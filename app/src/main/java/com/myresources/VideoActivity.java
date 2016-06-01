package com.myresources;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.myresources.video.DisplayUtils;
import com.myresources.video.ImageRender;
import com.myresources.video.PlayDataBean;
import com.myresources.video.PlayDataType;
import com.myresources.video.PlaySetting2;
import com.myresources.video.PopupWindowAdapter;
import com.myresources.video.VideoPlayPupWindowSetting;
import com.myresources.video.VideoPlayView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：李靖靖 on 2016/5/24 17:46
 * 简介：
 */
public class VideoActivity extends PlaySetting2 {

    public final static String imageLoaderPath = "/fanXQ/image/cache/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_main_activity);
        // 初始化ImageLoader
        ImageRender.getInstance().init(this);


        //初始化数据
        initData();
        //初始化播放器
        intPlay(this, R.id.course_main_videoplayview, playDataBeanList);

        //初始化右侧弹出框
        initPopupWindow();

        videoPlayView.showStatus(VideoPlayView.Status.Init);
        videoPlayView.showStatus(VideoPlayView.Status.Loading);
        videoPlayView.showStatus(VideoPlayView.Status.Playing);
        videoPlayView.showStatus(VideoPlayView.Status.Pause);
        videoPlayView.showStatus(VideoPlayView.Status.Error);

        //关闭,已废除
        videoPlayView.showStatus(VideoPlayView.Status.Finished);
    }

    /**
     * 视频数据
     */
    private void initData() {
        // 视频集合
        playDataBeanList = new ArrayList<PlayDataBean>();

        /**********如果想让广告视频，在正式视频之前播放，需要在list集合里面排序，让广告视频的index大于正是视频的index*********/
        // 图片广告
        PlayDataBean playDataBean = new PlayDataBean(playDataBeanList.size() + 1, "视频广告", "",
                "http://img20.360buyimg.com/da/jfs/t2578/2/885808803/92072/b4f3c332/566fb362Nb4837357.jpg",
                PlayDataType.ImageAdvertisement, 0, 10, "http://img20.360buyimg.com/da/jfs/t2560/78/964846820/101913/93fc657c/5677c3f6N90290eca.jpg");
        playDataBeanList.add(playDataBean);

        // 视频广告
        playDataBean = new PlayDataBean(playDataBeanList.size() + 1, "视频广告", "http://video.sinosns.cn/fx7/2.mp4",
                "http://img20.360buyimg.com/da/jfs/t2578/2/885808803/92072/b4f3c332/566fb362Nb4837357.jpg",
                PlayDataType.VideoAdvertisement, 0, 0, "");
        playDataBeanList.add(playDataBean);

        // 视频
        playDataBean = new PlayDataBean(playDataBeanList.size() + 1, "测试1", "http://video.sinosns.cn/fx7/3.mp4",
                "http://img20.360buyimg.com/da/jfs/t2578/2/885808803/92072/b4f3c332/566fb362Nb4837357.jpg",
                PlayDataType.Video, 0, 0, "");
        playDataBeanList.add(playDataBean);
    }

    /**
     * 为videoPlayView设置popuwindow的显示
     */
    private void initPopupWindow() {
        mContentView = this.getLayoutInflater().inflate(R.layout.view_popupwindow_everydayvideo, null);
        final PopupWindow mPopupWindow = new PopupWindow(mContentView, DisplayUtils.dpToPx(this, 270),
                LinearLayout.LayoutParams.MATCH_PARENT);

        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                // videoPlayStatus = 1;
            }
        });
        videoPlayView
                .setVideoPlayPupupWindow(mIVideoPlayPupWindowSetting = new VideoPlayPupWindowSetting(mPopupWindow) {
                    @Override
                    public void popupWindonViewSetting() {

                        ListView popupListView;
                        PopupWindowAdapter adapter;

                        List<Map<Object, Object>> tjqS = new ArrayList<Map<Object, Object>>();
                        Map<Object, Object> map = new HashMap<Object, Object>();

                        map.put("img",
                                "http://img20.360buyimg.com/da/jfs/t2578/2/885808803/92072/b4f3c332/566fb362Nb4837357.jpg");
                        map.put("title", "测试1");
                        map.put("name", "测试1.1");
                        map.put("isTime", "1");
                        tjqS.add(map);

                        map.clear();
                        map.put("img",
                                "http://img20.360buyimg.com/da/jfs/t2578/2/885808803/92072/b4f3c332/566fb362Nb4837357.jpg");
                        map.put("title", "测试2");
                        map.put("name", "测试2.2");
                        map.put("isTime", "2");
                        tjqS.add(map);

                        map.clear();
                        map.put("img",
                                "http://img20.360buyimg.com/da/jfs/t2578/2/885808803/92072/b4f3c332/566fb362Nb4837357.jpg");
                        map.put("title", "测试3");
                        map.put("name", "测试3.3");
                        map.put("isTime", "1");
                        tjqS.add(map);

                        /**
                         * 初始化PopupWindow布局,根据自己的需要重写
                         */
                        mPopupWindow.setFocusable(true);
                        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
                        mPopupWindow.showAtLocation(videoPlayView.titleBar, Gravity.RIGHT, 0, 0);

                        videoPlayView.hidePlayControllBar();

                        /**
                         * 为PopupWindow布局填充数据，根据自己的需要重写
                         */
                        popupListView = (ListView) mContentView.findViewById(R.id.lv_popup);
                        adapter = new PopupWindowAdapter(VideoActivity.this, tjqS);
                        popupListView.setAdapter(adapter);
                        popupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                            }
                        });
                    }
                });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                finish();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
