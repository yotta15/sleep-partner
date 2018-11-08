package com.example.gzy.test3.adapter;

import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by w on 2018/11/7.
 */
public class FragmentAdapter extends FragmentPagerAdapter {
    private List<android.support.v4.app.Fragment> list;
    private List<String> titleList;//用来存储每个Fragment的title
    public FragmentAdapter(android.support.v4.app.FragmentManager fm, List<android.support.v4.app.Fragment> list, List<String> titleList) {
        super(fm);
        this.list = list;
        this.titleList = titleList;
    }
    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    //返回标题
    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }

}
