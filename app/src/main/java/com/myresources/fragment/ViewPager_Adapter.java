package com.myresources.fragment;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

public class ViewPager_Adapter extends FragmentStatePagerAdapter{
	
	List<Fragment> list;

	public ViewPager_Adapter(FragmentManager fm, List<Fragment> list) {
		super(fm);
		this.list = list;
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public void destroyItem(View container, int position, Object object) {
		// TODO Auto-generated method stub
		super.destroyItem(container, position, object);
	}
}
