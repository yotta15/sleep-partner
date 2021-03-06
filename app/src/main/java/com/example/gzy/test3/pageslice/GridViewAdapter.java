package com.example.gzy.test3.pageslice;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.gzy.test3.R;
import com.example.gzy.test3.activity.ShowBarchartActivity;
import com.example.gzy.test3.bardata.MyBarDataSet;
import com.example.gzy.test3.fragment.FragmentReport;
import com.example.gzy.test3.model.SleepstateBean;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.idlestar.ratingstar.RatingStarView;

import java.util.ArrayList;
import java.util.List;

/**
 * created by gzy on 2019/4/15.
 * Describle;
 */
public class GridViewAdapter extends BaseAdapter {
    private List<DataBean> dataList;
    private YAxis leftAxis;             //左侧Y轴
    private YAxis rightAxis;            //右侧Y轴
    private XAxis xAxis;                //X轴
    private Legend legend;              //图例

//    List<Integer> yValues = new ArrayList<>();
//    ;
//    List<String> xValues = new ArrayList<>();
//    ;
//
//    List<SleepstateBean> sleepstateBeans;

    public GridViewAdapter(List<DataBean> datas, int page, Context context) {
        dataList = new ArrayList<>();
        //start end分别代表要显示的数组在总数据List中的开始和结束位置
        int start = page * FragmentReport.item_grid_num;
        int end = start + FragmentReport.item_grid_num;
        while ((start < datas.size()) && (start < end)) {
            dataList.add(datas.get(start));
            start++;
        }
        DataUtil c = new DataUtil();
//        sleepstateBeans = c.returnlist(context);
//
//        for (SleepstateBean statebean : sleepstateBeans) {
//            xValues.add(statebean.getSleeptime());
//        }
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int i) {
        return dataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View itemView, final ViewGroup viewGroup) {
        ViewHolder mHolder;
        if (itemView == null) {
            mHolder = new ViewHolder();
            itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_gridview, viewGroup, false);
            mHolder.barChart = (BarChart) itemView.findViewById(R.id.item_barchart);
            LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(900, 600);
            tvParams.gravity = Gravity.CENTER_HORIZONTAL;
            tvParams.gravity = Gravity.CENTER_VERTICAL;
            mHolder.barChart.setLayoutParams(tvParams);

            mHolder.tv_score = (TextView) itemView.findViewById(R.id.tv_score);

            mHolder.tv_text = (TextView) itemView.findViewById(R.id.tv_text);
            mHolder.rsv_rating = (RatingStarView) itemView.findViewById(R.id.show_rsv_rating);
            mHolder.tv_starttime=(TextView) itemView.findViewById(R.id.tv_starttime);
            mHolder.tv_endtime=(TextView) itemView.findViewById(R.id.tv_endtime);
            mHolder.iv_starttime=(ImageView)itemView.findViewById(R.id.iv_starttime);
            mHolder.iv_endtime=(ImageView)itemView.findViewById(R.id.iv_endtime);
            itemView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) itemView.getTag();
        }
        final DataBean bean = dataList.get(i);

        if (bean != null) {

            initBarChart(mHolder.barChart);
            showBarChart(bean.sleepInfo.getSleepstate(), "", viewGroup.getContext(), mHolder.barChart);
            mHolder.tv_text.setText(bean.sleepInfo.getSleepdate());
            mHolder.tv_score.setText(String.valueOf(bean.sleepInfo.getScore()));
            //百分制转化为五分制
            mHolder.rsv_rating.setRating(bean.sleepInfo.getScore()/20.0f);
            String[] str1=bean.sleepInfo.getStarttime().split(":");
            String[] str2=bean.sleepInfo.getEndtime().split(":");

            mHolder.iv_starttime.setImageResource((Integer.parseInt(str1[0])>19||(Integer.parseInt(str1[0])<6))?
                    R.drawable.moon:R.drawable.sun);
            mHolder.iv_endtime.setImageResource((Integer.parseInt(str2[0])>19||(Integer.parseInt(str2[0])<6))?
                    R.drawable.moon:R.drawable.sun);
            mHolder.tv_starttime.setText("睡觉时间  "+bean.sleepInfo.getStarttime());
            mHolder.tv_endtime.setText("起床时间  "+bean.sleepInfo.getEndtime());

        }
        return itemView;
    }

    private class ViewHolder {
        private TextView tv_score;
        private ImageView iv_starttime;
        private ImageView iv_endtime;
        private BarChart barChart;
        private TextView tv_text;
        private RatingStarView rsv_rating;
        private TextView tv_starttime;
        private TextView tv_endtime;
    }

    /**
     * 初始化BarChart图表
     */
    private void initBarChart(BarChart barChart) {
        //背景颜色
        barChart.setBackgroundColor(Color.WHITE);
        //不显示图表网格
        barChart.setDrawGridBackground(true);
        //背景阴影
        barChart.setDrawBarShadow(false);
        barChart.setHighlightFullBarEnabled(false);

        barChart.setDoubleTapToZoomEnabled(false);
        //禁止拖拽
        barChart.setDragEnabled(false);
        //Y轴禁止缩放
        barChart.setScaleXEnabled(true);
        barChart.setScaleYEnabled(false);
        //   barChart.setScaleEnabled(false);
        //禁止所有事件
        barChart.setTouchEnabled(false);
        //不显示边框
        barChart.setDrawBorders(true);

        //不显示右下角描述内容
        Description description = new Description();
        description.setEnabled(false);
        barChart.setDescription(description);

        //设置动画效果
        barChart.animateY(1000, Easing.EasingOption.Linear);
        barChart.animateX(1000, Easing.EasingOption.Linear);

        /***XY轴的设置***/
        //X轴设置显示位置在底部
        xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);

        leftAxis = barChart.getAxisLeft();
        rightAxis = barChart.getAxisRight();

        xAxis.setAxisMinimum(0f);
        // xAxis.setAxisMaximum(xValues.size());
      //将X轴的值显示在中央
        xAxis.setCenterAxisLabels(true);
        //保证Y轴从0开始，不然会上移一点
    //        leftAxis.setAxisMinimum(0f);
    //        rightAxis.setAxisMinimum(0f);

        //不绘制X Y轴线条
        xAxis.setDrawAxisLine(false);
        leftAxis.setDrawAxisLine(false);
        rightAxis.setDrawAxisLine(false);
        //不显示左侧Y轴,右侧y轴
        leftAxis.setEnabled(false);
        rightAxis.setEnabled(false);
        //不显示X轴网格线
        xAxis.setDrawGridLines(false);
        //右侧Y轴网格线设置为虚线
        //rightAxis.enableGridDashedLine(10f, 10f, 0f);

        /***折线图例 标签 设置***/
        legend = barChart.getLegend();
        legend.setForm(Legend.LegendForm.LINE);
        legend.setTextSize(11f);
        //显示位置
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);

        //是否绘制在图表里面
        legend.setDrawInside(false);
    }

    //单条柱状图
    private void showBarChart(final List<SleepstateBean> sleepstateBeans, String name, Context context, BarChart barChart) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < sleepstateBeans.size(); i++) {
            /**
             * 此处还可传入Drawable对象 BarEntry(float x, float y, Drawable icon)
             * 即可设置柱状图顶部的 icon展示
             */
            BarEntry barEntry = new BarEntry(i, (float) sleepstateBeans.get(i).getFvalue());
            entries.add(barEntry);
        }
        // 每一个BarDataSet代表一类柱状图
        MyBarDataSet barDataSet = new MyBarDataSet(entries, name);
        initBarDataSet(barDataSet, context);
        //X轴自定义值,只显示 第一个和最后一个
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return "";
                // return xValues.get((int) Math.abs(value) % xValues.size());
                //return sleepstateBeans.get((int) value % sleepstateBeans.size()).getSleeptime();
            }
        });


        BarData data = new BarData(barDataSet);
        data.setBarWidth(1f);
        barChart.setData(data);
    }

    //未检测到 5红 /醒 8黄/浅睡 10 紫/ 深睡 15绿
    private void initBarDataSet(MyBarDataSet barDataSet, Context context) {
        int[] colors = new int[]{context.getResources().getColor(R.color.red),
                context.getResources().getColor(R.color.orange),
                context.getResources().getColor(R.color.cao_green),
                context.getResources().getColor(R.color.holo_blue_light)};
        barDataSet.setColors(colors);

        //    barDataSet.setColor(color);
        barDataSet.setFormLineWidth(1f);
        barDataSet.setFormSize(15.f);
        //不显示柱状图顶部值
        barDataSet.setDrawValues(false);
//        barDataSet.setValueTextSize(10f);
//        barDataSet.setValueTextColor(color);
    }

}
