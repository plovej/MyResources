package com.myresources.video;

import android.app.Activity;

import java.util.List;

/**
 * Created by hzb on 15/12/22.
 */
public interface IPlaySetting {

    void intPlay(Activity mActivity, int videoPlayViewID, List<PlayDataBean> playDataBeanList);
    void initVideoView();
    PlayDataBean getNextBoughtCourse(List<PlayDataBean> videos, int id);
    boolean playCourse(PlayDataBean playDataBean);
    void showPortraitScreen();
    void showPortraitScreenFunction();
    void showLandscapeFullScreen();
    void showLandscapeFullScreenFunction();
    void onTitleLeftImageBackOnClick();
    void onTitleRightTextOnClick();
    void updateHadByTime(int bytime);
    void onStopOrPause();
    void onFirstPlay();
    Activity getActivity();
    void onPlayNextVideo();
    boolean onVideoPlayOver();
    boolean onImagePlayOver();
}
