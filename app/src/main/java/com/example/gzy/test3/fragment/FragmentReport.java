package com.example.gzy.test3.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.example.gzy.test3.R;
import com.example.gzy.test3.bardata.LocalJsonAnalyzeUtil;
import com.example.gzy.test3.bardata.MyBarDataSet;
import com.example.gzy.test3.model.SleepInfo;
import com.example.gzy.test3.model.SleepstateBean;
import com.example.gzy.test3.pageslice.DataBean;
import com.example.gzy.test3.pageslice.DataUtil;
import com.example.gzy.test3.pageslice.GridViewAdapter;
import com.example.gzy.test3.pageslice.ViewPagerAdapter;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.List;

/**
 * created by gzy on 2019/4/1.
 * Describle;
 * 浅睡 、深睡 、醒着、未检测到
 */
public class FragmentReport extends Fragment {
    public static int item_grid_num = 1;//每一页中GridView中item的数量
    public static int number_columns = 1;//gridview一行展示的数目
    private ViewPager view_pager;
    private ViewPagerAdapter mAdapter;
    private List<DataBean> dataList;
    private List<GridView> gridList = new ArrayList<>();
//    private CirclePageIndicator indicator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.madetest,container,false);


        view_pager = (ViewPager)view.findViewById(R.id.view_pager);
        mAdapter = new ViewPagerAdapter();
        view_pager.setAdapter(mAdapter);
        dataList = new ArrayList<>();
        initDatas();
        return  view;

    }



    private void initViews() {
        //初始化ViewPager

        //圆点指示器
//        indicator = (CirclePageIndicator) findViewById(R.id.indicator);
//        indicator.setVisibility(View.VISIBLE);
//        indicator.setViewPager(view_pager);
    }

    private void initDatas() {
        if (dataList.size() > 0) {
            dataList.clear();
        }
        if (gridList.size() > 0) {
            gridList.clear();
        }
        DataUtil dataUtil=new DataUtil();
        List<List<SleepstateBean>> allSleepstateBean= dataUtil.GetSleepList(getContext());
        //初始化数据
        for (int i = 0; i < allSleepstateBean.size(); i++) {
            DataBean bean = new DataBean();
            bean.name = "第" + (i + 1) + "条数据";
            bean.sleepstateBean=allSleepstateBean.get(i);
            dataList.add(bean);
        }
        //计算viewpager一共显示几页
        int pageSize = dataList.size() % item_grid_num == 0
                ? dataList.size() / item_grid_num
                : dataList.size() / item_grid_num + 1;
        for (int i = 0; i < pageSize; i++) {
            GridView gridView = new GridView(getContext());
            GridViewAdapter adapter = new GridViewAdapter(dataList, i,getContext());
            gridView.setNumColumns(number_columns);
            gridView.setAdapter(adapter);
            gridList.add(gridView);
        }
        mAdapter.add(gridList);
    }
}
