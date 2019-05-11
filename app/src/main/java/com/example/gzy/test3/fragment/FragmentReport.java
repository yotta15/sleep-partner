package com.example.gzy.test3.fragment;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.gzy.test3.R;
import com.example.gzy.test3.activity.ShowBarchartActivity;
import com.example.gzy.test3.model.SleepInfo;
import com.example.gzy.test3.model.SleepstateBean;
import com.example.gzy.test3.pageslice.DataBean;
import com.example.gzy.test3.pageslice.DataUtil;
import com.example.gzy.test3.pageslice.GridViewAdapter;
import com.example.gzy.test3.pageslice.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * created by gzy on 2019/4/1.
 * Describle;
 * 浅睡 、深睡 、醒着、未检测到
 */
public class FragmentReport extends Fragment{
    public static int item_grid_num = 1;//每一页中GridView中item的数量
    public static int number_columns = 1;//gridview一行展示的数目
    private ViewPager view_pager;
    private ViewPagerAdapter mAdapter;
    private List<DataBean> dataList;
    private List<GridView> gridList = new ArrayList<>();
    GridView gridView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.report_fragment, container, false);
        view_pager = (ViewPager) view.findViewById(R.id.view_pager);
        mAdapter = new ViewPagerAdapter();
        view_pager.setAdapter(mAdapter);
        dataList = new ArrayList<>();
        initDatas();
        return view;

    }


    private void initDatas() {
        if (dataList.size() > 0) {
            dataList.clear();
        }
        if (gridList.size() > 0) {
            gridList.clear();
        }
        DataUtil dataUtil = new DataUtil();
        List<List<SleepstateBean>> allSleepstateBean = dataUtil.GetSleepList(getContext());
        List<SleepInfo> allSleepInfo = dataUtil.GetAllSleepInfo(getContext());
        //初始化数据
        for (int i = 0; i < allSleepstateBean.size(); i++) {
            DataBean bean = new DataBean();
            bean.name = "第" + (i + 1) + "条数据";
            bean.sleepInfo = allSleepInfo.get(i);
            dataList.add(bean);
        }
        //计算viewpager一共显示几页
        int pageSize = dataList.size() % item_grid_num == 0
                ? dataList.size() / item_grid_num
                : dataList.size() / item_grid_num + 1;
        for (int i = 0; i < pageSize; i++) {

            gridView = new GridView(getContext());
            GridViewAdapter adapter = new GridViewAdapter(dataList, i, getContext());

            gridView.setNumColumns(number_columns);
            gridView.setAdapter(adapter);
            gridList.add(gridView);
        }
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//            }
//        });
        mAdapter.setCallback(new ViewPagerAdapter.Callback() {
            @Override
            public void callback(int data) {
                Intent intent = new Intent(getActivity(), ShowBarchartActivity.class);
                intent.putExtra("sleepinfo", dataList.get(data).getSleepInfo());
                getActivity().startActivity(intent);
                Log.i("barchart", "onclick"+data);
            }
        });
        mAdapter.add(gridList);
    }


//    @Override
//    public void callback(int data) {
//        Intent intent = new Intent(getActivity(), ShowBarchartActivity.class);
//        intent.putExtra("sleepinfo", dataList.get(data).getSleepInfo());
//        getActivity().startActivity(intent);
//        Log.i("barchart", "onclick");
//
//    }
}
