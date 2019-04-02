package com.example.gzy.test3.fragment;

import android.graphics.Color;
import android.support.annotation.ColorRes;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gzy.test3.R;
import com.example.gzy.test3.datasource.BarChartBean;
import com.example.gzy.test3.datasource.LocalJsonAnalyzeUtil;
import com.example.gzy.test3.datasource.MyBarDataSet;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * created by gzy on 2019/4/1.
 * Describle;
 *  浅睡 、深睡 、醒着、未检测到
 */
public class FragmentReport extends Fragment {
    private BarChart barChart;
    private YAxis leftAxis;             //左侧Y轴
    private YAxis rightAxis;            //右侧Y轴
    private XAxis xAxis;                //X轴
    private Legend legend;              //图例
    private LimitLine limitLine;        //限制线
    TextView dataSet1TextView;
    TextView dataSet2TextView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_bar_chart, container, false);
        dataSet1TextView =(TextView) view.findViewById(R.id.dataSet1TextView);
        dataSet2TextView =(TextView) view.findViewById(R.id.dataSet2TextView);


        barChart = (BarChart) view.findViewById(R.id.bar_chart);
        initBarChart(barChart);
        BarChartBean barChartBean = LocalJsonAnalyzeUtil.JsonToObject(getActivity(),
                "bardata.json", BarChartBean.class);

        List<BarChartBean.StFinDateBean.VtDateValueBean> dateValueList = barChartBean.getStFinDate().getVtDateValue();
        Collections.reverse(dateValueList);//将集合 逆序排列，转换成需要的顺序

        showBarChart(dateValueList, "净资产收益率（%）", getResources().getColor(R.color.blue));




        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                //得到包含此柱状图的 数据集
                BarDataSet dataSets = (BarDataSet) barChart.getBarData().getDataSetForEntry(e);
                dataSet1TextView.setText("被点击的柱状图名称：\n" + dataSets.getLabel() + "X轴：" + (int) e.getX() + "    Y轴" + e.getX());

                StringBuffer allBarChart = new StringBuffer();
                allBarChart.append("所有柱状图：\n");
                for (IBarDataSet dataSet : barChart.getBarData().getDataSets()) {
                    BarEntry entry = dataSet.getEntryForIndex((int) e.getX());
                    allBarChart.append(dataSet.getLabel())
                            .append("X轴：")
                            .append((int) entry.getX())
                            .append("    Y轴")
                            .append(entry.getY())
                            .append("\n");
                }
                dataSet2TextView.setText(allBarChart.toString());
            }

            @Override
            public void onNothingSelected() {
            }
        });
        return view;
    }
    /**
     * 初始化BarChart图表
     */
    private void initBarChart(BarChart barChart) {
        //背景颜色
        barChart.setBackgroundColor(Color.WHITE);
        //不显示图表网格
        barChart.setDrawGridBackground(false);
        //背景阴影
        barChart.setDrawBarShadow(false);
        barChart.setHighlightFullBarEnabled(false);

        barChart.setDoubleTapToZoomEnabled(false);
        //禁止拖拽
        barChart.setDragEnabled(false);
        //X轴或Y轴禁止缩放
        barChart.setScaleXEnabled(false);
        barChart.setScaleYEnabled(false);
        barChart.setScaleEnabled(false);
        //禁止所有事件
//        barChart.setTouchEnabled(false);
        //不显示边框
        barChart.setDrawBorders(false);

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
//        xAxis.setAxisMinimum(0f);
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
    private void showBarChart(final List<BarChartBean.StFinDateBean.VtDateValueBean> dateValueList, String name, int color) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < dateValueList.size(); i++) {
            /**
             * 此处还可传入Drawable对象 BarEntry(float x, float y, Drawable icon)
             * 即可设置柱状图顶部的 icon展示
             */
            BarEntry barEntry = new BarEntry(i, (float) dateValueList.get(i).getFValue());
            entries.add(barEntry);
        }
        // 每一个BarDataSet代表一类柱状图
        MyBarDataSet barDataSet = new MyBarDataSet(entries, name);
        initBarDataSet(barDataSet);
        //X轴自定义值
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return dateValueList.get((int) value % dateValueList.size()).getSYearMonth();
            }
        });


        BarData data = new BarData(barDataSet);
        data.setBarWidth(1f);
        barChart.setData(data);
    }
    private void initBarDataSet(MyBarDataSet barDataSet) {
        int[] colors=new int[]{getResources().getColor(R.color.green),
                getResources().getColor(R.color.orange),
                getResources().getColor(R.color.red)};
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
