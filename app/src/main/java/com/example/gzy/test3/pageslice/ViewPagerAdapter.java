package com.example.gzy.test3.pageslice;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.gzy.test3.fragment.FragmentReport;

import java.util.ArrayList;
import java.util.List;

/**
 * created by gzy on 2019/4/15.
 * Describle;
 */
public class ViewPagerAdapter extends PagerAdapter {
    private List<GridView> gridList;
    public ViewPagerAdapter() {
        gridList = new ArrayList<>();
    }
    Callback callback;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }


    public static interface Callback {
        void callback(int data);
    }
    public void add(List<GridView> datas) {
        if (gridList.size() > 0) {
            gridList.clear();
        }
        gridList.addAll(datas);
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return gridList.size();
    }
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        container.addView(gridList.get(position));
        gridList.get(position).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
              callback.callback(position);
            }
        });
        return gridList.get(position);
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
