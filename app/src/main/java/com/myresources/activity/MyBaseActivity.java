package com.myresources.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.myresources.R;
import com.myresources.util.DialogOfSetting;
import com.myresources.util.NetWorkUtil;
import com.myresources.util.ProgressDialogUtil;
import com.myresources.util.SystemBarTintManager;

/**
 * 作者：李靖靖 on 2016/5/24 14:35
 * 简介：父activity
 */
public class MyBaseActivity extends FragmentActivity implements View.OnClickListener {
    private View mBaseActivityContainer;
    private FrameLayout mTemplateContainer;
    /** 加载失败界面*/
    protected RelativeLayout mTemplateNoData;

    private Toast toast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //调用状态栏方法
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            initStatusBar(R.color.colorPrimaryDark);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_main);

        mTemplateContainer = (FrameLayout) super.findViewById(R.id.view_mainBody);
        mTemplateContainer.setBackgroundColor(Color.WHITE);
        mTemplateNoData = (RelativeLayout) super.findViewById(R.id.noData);
        mTemplateNoData.setOnClickListener(this);
        if(NetWorkUtil.getNetWorkInfoType(getApplicationContext())==NetWorkUtil.NONE){
            loadError(true);
        }
    }

    /**
     * 初始化状态栏
     * @param color 状态栏颜色
     */
    private void initStatusBar(int color) {
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setStatusBarTintColor(getResources().getColor(color));
    }


    /**
     * 数据加载失败
     * @param isError true是显示加载失败,false隐藏加载失败
     */
    public void loadError(boolean isError){
        if(isError){
            hideViewForGone(mTemplateContainer);
            showView(mTemplateNoData);
        }else{
            showView(mTemplateContainer);
            hideViewForGone(mTemplateNoData);
        }
    }

    /**
     * 隐藏View方法,此方法隐藏不保留View的大小
     * @param view 需要隐藏的View
     */
    public void hideViewForGone(View... view){
        if(view.length>0){
            for(View viewParams : view){
                viewParams.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 显示View方法
     * @param view 需要显示的View
     */
    public void showView(View... view){
        if(view.length>0){
            for(View viewParams : view){
                viewParams.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        if (layoutResID == R.layout.activity_base_main) {
            mBaseActivityContainer = LayoutInflater.from(this).inflate(
                    layoutResID, null);
            super.setContentView(mBaseActivityContainer);
        } else {
            mTemplateContainer.removeAllViews();
            View inflate = this.getLayoutInflater().inflate(layoutResID, null);
            inflate.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            mTemplateContainer.addView(inflate);
        }
    }

    @Override
    public void setContentView(View view) {
        setContentView(view, null);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        if (mTemplateContainer != null) {
            mTemplateContainer.removeAllViews();
            if (params != null) {
                mTemplateContainer.addView(view, params);
            } else {
                mTemplateContainer.addView(view);
            }
        } else {
            super.setContentView(view, params);
        }
    }

    @Override
    public void onClick(View v) {
        ProgressDialogUtil.showDefaultProgerss(this,null);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                    handler.sendEmptyMessage(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
    private Handler  handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(NetWorkUtil.getNetWorkInfoType(getApplicationContext())==NetWorkUtil.NONE){
                ProgressDialogUtil.closeDefalutProgerss();
                Log.e("网络连接", "1ture");
                loadError(true);
                showToast("无网络");
            }else{
                ProgressDialogUtil.closeDefalutProgerss();
                Log.e("网络连接", "2false");
                loadError(false);
            }
        }
    };

    /**
     * intent封装 ,不支持参数,无参跳转
     * @param toClsActivity 需要跳转到的activity
     */
    public void goIntent(Class<?> toClsActivity) {
        goIntent(toClsActivity, null);
    }

    /**
     * intent封装 ,支持参数携带
     * @param toClsActivity 需要跳转到的activity
     * @param bundle 参数封装
     */
    public void goIntent(Class<?> toClsActivity, Bundle bundle) {
        Intent intent = new Intent(this, toClsActivity);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * 显示一个toast
     * @param message toast的内容
     */
    public void showToast(String message) {
        showToast(message, Toast.LENGTH_SHORT);
    }

    /**
     * 显示一个Toast
     * @param message 吐司的内容
     * @param time 吐司停留的时间
            */
            public void showToast(String message,int time) {
        if (message == null) {
            return;
        }
        if(toast==null){
            toast = Toast.makeText(getApplicationContext(), message, time);
        }
        toast.setText(message);
        toast.show();
    }


    /** 显示Dialog 提示框,无需调用dialog.dismiss方法
     * @param message dialog的提示消息
     * @param sureText dialog确定按键的text文本
     * @param cancelText dialog取消按键的text文本
     * @param listener dialog的监听
     */
    public void showDialog(Context context,String title, String message,String sureText, int sureColor, String cancelText,int cancelColor,boolean isShowCancel ,final DialogOnClickListener listener){
        if(message==null){
            throw new NullPointerException("提示框消息不能为空");
        }
        if(sureText==null||sureText.equals("")){
            sureText = "确定";
        }
        if(cancelText==null||cancelText.equals("")){
            cancelText = "取消";
        }
        final DialogOfSetting mDialog = new DialogOfSetting(context);
        mDialog.setmMessage(message);
        mDialog.setmTitle(title);
        mDialog.setShowBtn(isShowCancel);
        if(!isShowCancel){
            mDialog.mConfirm.setText(sureText);
            mDialog.mConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null){
                        listener.onSure(view);
                    }
                    mDialog.dismiss();
                }
            });
        }
        mDialog.mCancle.setText(cancelText);
        mDialog.mCancle.setTextColor(getResources().getColor(cancelColor));
        mDialog.mSure.setText(sureText);
        mDialog.mSure.setTextColor(getResources().getColor(sureColor));
//        mDialog.mSure.setBackgroundResource(sureBackColor);
//        mDialog.mSure.setBackgroundColor(sureBackColor);
        mDialog.mCancle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.onCancle(v);
                }
                mDialog.dismiss();
            }
        });
        mDialog.mSure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.onSure(v);
                }
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }


    /**
     * dialog的点击监听
     */
    public interface DialogOnClickListener{
        /** 确定回调
         */
        void onSure(View v);

        /**
         * 取消回调
         */
        void onCancle(View v);
    }

    /**
     * 网络刷新监听事件变量
     */
    public ClickRefresh onClickRefresh;
    /**
     * 网络点击刷新方法
     */
    public interface  ClickRefresh{
        void onRefresh();
    }

    /**
     * 设置点击事件
     * @param l
     */
    public void setOnClickRefresh(ClickRefresh l){
        onClickRefresh = l ;
    }
}