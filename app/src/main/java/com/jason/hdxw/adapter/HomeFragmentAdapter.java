package com.jason.hdxw.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * created by wang on 2018/11/10
 */
public class HomeFragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> list;
    private FragmentManager fm;

    public HomeFragmentAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.fm = fm;
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Fragment instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        fm.beginTransaction().show(fragment).commitAllowingStateLoss();
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Fragment fragment = list.get(position);
//        java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState
//        commit();
        fm.beginTransaction().hide(fragment).commitAllowingStateLoss();
    }


}
