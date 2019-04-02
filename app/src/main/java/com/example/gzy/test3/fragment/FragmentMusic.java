package com.example.gzy.test3.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.gzy.test3.R;
import com.example.gzy.test3.adapter.FragmentAdapter;

import java.util.ArrayList;
import java.util.List;

public class FragmentMusic extends Fragment {

    protected ViewPager viewPager;
    protected TabLayout tabLayout;
    protected View view;
    protected List<Fragment> fragmentList;
    protected List<String> titleList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentList = new ArrayList<>();
        titleList = new ArrayList<>();
        fragmentList.add(new BzyFragment());
        fragmentList.add(new MusicFragment());
        fragmentList.add(new StoryFragment());
        titleList.add("白噪音");
        titleList.add("音乐");
        titleList.add("睡前故事");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.secondfragment, container, false);
        tabLayout= (TabLayout) view.findViewById(R.id.tabLayout);
        tabLayout.setBackgroundColor(Color.parseColor("#ff3344"));
        //设置tabLayout的属性
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setBackgroundColor(Color.parseColor("#ffffff"));
        tabLayout.setTabTextColors(Color.parseColor("#000000"), Color.parseColor("#0ddcff"));//设置tab上文字的颜色，第一个参数表示没有选中状态下的文字颜色，第二个参数表示选中后的文字颜色
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#0ddcff"));//设置tab选中的底部的指示条的颜色

        viewPager = (ViewPager) view.findViewById(R.id.fg1viewPager);
        //给Fragment1里面的ViewPager添加自定义的适配器。
        viewPager.setAdapter(new FragmentAdapter(getChildFragmentManager(),fragmentList,titleList));
        //然后让TabLayout和ViewPager关联
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

}
