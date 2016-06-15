package com.myresources.activity;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;

import com.myresources.R;
import com.myresources.fragment.FaXianFragment;
import com.myresources.fragment.HomeFragment;
import com.myresources.fragment.MeFragment;
import com.myresources.fragment.VideoFragment;
import com.myresources.fragment.ViewPager_Adapter;
import com.myresources.util.ViewPagerCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * 2016-5-24 14:00
 * lijingjing
 */
public class MainActivity extends MyBaseActivity {

    private Button bt1,bt2,bt4,bt5;
    private ViewPagerCompat viewpager;
    private FragmentManager fm;
    List<Fragment> list_fragment;
    Drawable d;
    private Resources res;

    private long lastScrollTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init(){
        bt1 = (Button) findViewById(R.id.btn_tab1);
        bt2 = (Button) findViewById(R.id.btn_tab2);
        bt4 = (Button) findViewById(R.id.btn_tab4);
        bt5 = (Button) findViewById(R.id.btn_tab5);
        viewpager = (ViewPagerCompat) findViewById(R.id.fragment_viewpager);

        bt1.setOnClickListener(this);
        bt2.setOnClickListener(this);
        bt4.setOnClickListener(this);
        bt5.setOnClickListener(this);

        /**
         * 监听软件盘
         */
//        bt1.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                int heightDiff = bt1.getRootView().getHeight() - bt1.getHeight();
//                if(heightDiff > 300){
//                    //软件盘弹出
//                    bt1.setVisibility(View.VISIBLE);
//                }else{
//                    //软键盘收起
//                    bt1.setVisibility(View.GONE);
//                }
//            }
//        });
        list_fragment = new ArrayList<Fragment>();
        list_fragment.add(new HomeFragment());
        list_fragment.add(new VideoFragment());
        list_fragment.add(new FaXianFragment());
        list_fragment.add(new MeFragment());

        fm = getSupportFragmentManager();

        viewpager.setOffscreenPageLimit(0);
        viewpager.setAdapter(new ViewPager_Adapter(fm, list_fragment));
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                // TODO Auto-generated method stub
                getColorChange(arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });

        res = getResources();
        d = res.getDrawable(R.mipmap.home_bottom_home_selected);
        d.setBounds(0, 0, d.getMinimumWidth(), d.getMinimumHeight());
        bt1.setCompoundDrawables(null, d, null, null);
        bt1.setTextColor(getResources().getColorStateList(R.color.color_red));
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_tab1:
                viewpager.setCurrentItem(0);
                getColorChange(0);
                break;
            case R.id.btn_tab2:
                viewpager.setCurrentItem(1);
                getColorChange(1);
                break;
            case R.id.btn_tab4:
                viewpager.setCurrentItem(2);
                getColorChange(2);
                break;
            case R.id.btn_tab5:
                viewpager.setCurrentItem(3);
                getColorChange(3);
                break;

            default:
                break;
        }
    }

    // 关闭Fragment
    public void popAllFragmentsExceptTheBottomOne() {
        for (int i = 0, count = fm.getBackStackEntryCount(); i < count; i++) {
            fm.popBackStack();
        }
    }

    /**
     * Drawable img_on, img_off;
     * Resources res = getResources();
     * img_off = res.getDrawable(R.drawable.btn_strip_mark_off);
     * 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
     * img_off.setBounds(0, 0, img_off.getMinimumWidth(), img_off.getMinimumHeight());
     * btn.setCompoundDrawables(img_off, null, null, null); //设置左图标
     *
     * 设置底部按钮的颜色、图片的变化方法
     */

    private void getSetting(Button bt, int drawable, int color){

        d = res.getDrawable(drawable);
        d.setBounds(0, 0, d.getMinimumWidth(), d.getMinimumHeight());
        bt.setCompoundDrawables(null, d, null, null);
        bt.setTextColor(getResources().getColorStateList(color));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (lastScrollTime == 0) {
                    showToast("再按一次退出程序");
                    lastScrollTime = System.currentTimeMillis();
                    return true;
                } else if (System.currentTimeMillis() - lastScrollTime < 3000) {
                    lastScrollTime = 0;
                    finish();
                } else if(System.currentTimeMillis() - lastScrollTime > 3000){
                    showToast("再按一次退出程序");
                    lastScrollTime = System.currentTimeMillis();
                    return true;
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
    public void  getColorChange(int number){
        switch (number){
            case 0:
                getSetting(bt1, R.mipmap.home_bottom_home_selected, R.color.color_red);
                getSetting(bt2, R.mipmap.home_bottom_video_normal, R.color.color_black);
                getSetting(bt4, R.mipmap.home_bottom_find_normal, R.color.color_black);
                getSetting(bt5, R.mipmap.home_bottom_me_normal, R.color.color_black);
                break;
            case 1:
                getSetting(bt2, R.mipmap.home_bottom_video_selected, R.color.color_red);
                getSetting(bt1, R.mipmap.home_bottom_home_normal, R.color.color_black);
                getSetting(bt4, R.mipmap.home_bottom_find_normal, R.color.color_black);
                getSetting(bt5, R.mipmap.home_bottom_me_normal, R.color.color_black);
                break;
            case 2:
                getSetting(bt4, R.mipmap.home_bottom_find_selected, R.color.color_red);
                getSetting(bt1, R.mipmap.home_bottom_home_normal, R.color.color_black);
                getSetting(bt2, R.mipmap.home_bottom_video_normal, R.color.color_black);
                getSetting(bt5, R.mipmap.home_bottom_me_normal, R.color.color_black);
                break;
            case 3:
                getSetting(bt5, R.mipmap.home_bottom_me_selected, R.color.color_red);
                getSetting(bt1, R.mipmap.home_bottom_home_normal, R.color.color_black);
                getSetting(bt2, R.mipmap.home_bottom_video_normal, R.color.color_black);
                getSetting(bt4, R.mipmap.home_bottom_find_normal, R.color.color_black);
                break;

            default:
                break;
        }
    }
}
