package com.example.gzy.test3.activity;

/**
 * created by gzy on 2019/4/1.
 * Describle;
 * 用以实现单条柱状图，每条颜色不一样
 */

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.gzy.test3.R;
import com.example.gzy.test3.datasource.Barchart;
import com.example.gzy.test3.datasource.Barchart.StFinDateBean.VtDateValueBean;
import com.example.gzy.test3.datasource.Barchart.StFinDateBean.VtDateValueAvgBean;

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
 * Created by Administrator on 2017/5/24 0024.
 */
public class BarChartActivity extends AppCompatActivity {

    private BarChart barChart;
    private YAxis leftAxis;             //左侧Y轴
    private YAxis rightAxis;            //右侧Y轴
    private XAxis xAxis;                //X轴
    private Legend legend;              //图例
    private LimitLine limitLine;        //限制线
    TextView dataSet1TextView;
    TextView dataSet2TextView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chart);
        dataSet1TextView =(TextView) findViewById(R.id.dataSet1TextView);
        dataSet2TextView =(TextView) findViewById(R.id.dataSet2TextView);


        barChart = (BarChart) findViewById(R.id.bar_chart);
        initBarChart(barChart);
        Barchart barChartBean = LocalJsonAnalyzeUtil.JsonToObject(this,
                "test.json",  Barchart.class);

        //处理数据 时 记得判断每条柱状图对应的数据集合 长度是否一致
        LinkedHashMap<String, List<Float>> chartDataMap = new LinkedHashMap<>();
        List<String> xValues = new ArrayList<>();
        List<Float> yValue1 = new ArrayList<>();
        List<Float> yValue2 = new ArrayList<>();
        List<Integer> colors = Arrays.asList(
                getResources().getColor(R.color.blue), getResources().getColor(R.color.orange)
        );

        List<VtDateValueBean> valueList = barChartBean.getStFinDate().getVtDateValue();
        List<VtDateValueAvgBean> avgValueList = barChartBean.getStFinDate().getVtDateValueAvg();
        Collections.reverse(valueList);//将集合 逆序排列，转换成需要的顺序

        for (VtDateValueBean valueBean : valueList) {
            xValues.add(valueBean.getSYearMonth());
            yValue1.add((float) valueBean.getFValue());
        }
        for (VtDateValueAvgBean valueAvgBean : avgValueList) {
            yValue2.add((float) valueAvgBean.getFValue());
        }
        chartDataMap.put("净资产收益率（%）", yValue1);
        chartDataMap.put("行业平均值（%）", yValue2);

        showBarChart(xValues, chartDataMap, colors);

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
    }

    /**
     * 初始化BarChart图表
     */
    private void initBarChart(BarChart barChart) {
        /***图表设置***/
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


    /**
     * 柱状图始化设置 一个BarDataSet 代表一列柱状图
     * 深睡、浅睡、未检测到、醒  四列柱状图
     * @param barDataSet 柱状图
     * @param color      柱状图颜色
     */
    private void initBarDataSet(MyBarDataSet barDataSet, int color) {
        barDataSet.setColors(new int[]{getResources().getColor(R.color.green),
                getResources().getColor(R.color.orange),
                getResources().getColor(R.color.red)});
//        ArrayList<BarDataSet> dataSets = new ArrayList<>();
//        dataSets.add(set);
//
//        BarData data = new BarData(xVals, dataSets);

    //    barDataSet.setColor(color);
        barDataSet.setFormLineWidth(1f);
        barDataSet.setFormSize(15.f);
        //不显示柱状图顶部值
        barDataSet.setDrawValues(false);
//        barDataSet.setValueTextSize(10f);
//        barDataSet.setValueTextColor(color);
    }

    /**
     * @param xValues   X轴的值
     * @param dataLists LinkedHashMap<String, List<Float>>
     *                  key对应柱状图名字  List<Float> 对应每类柱状图的Y值
     * @param colors
     */
    public void showBarChart(final List<String> xValues, LinkedHashMap<String, List<Float>> dataLists,
                             @ColorRes List<Integer> colors) {

        List<IBarDataSet> dataSets = new ArrayList<>();
        int currentPosition = 0;//用于柱状图颜色集合的index

        for (LinkedHashMap.Entry<String, List<Float>> entry : dataLists.entrySet()) {
            String name = entry.getKey();
            List<Float> yValueList = entry.getValue();

            List<BarEntry> entries = new ArrayList<>();

            for (int i = 0; i < yValueList.size(); i++) {
                /**
                 *  如果需要添加TAG标志 可使用以下构造方法
                 *  BarEntry(float x, float y, Object data)
                 *  e.getData()
                 */
                entries.add(new BarEntry(i, yValueList.get(i)));
            }
            // 每一个BarDataSet代表一类柱状图
            MyBarDataSet barDataSet = new MyBarDataSet(entries, name);

            initBarDataSet(barDataSet, colors.get(currentPosition));
            dataSets.add(barDataSet);

            currentPosition++;
        }

//X轴自定义值
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xValues.get((int) Math.abs(value) % xValues.size());
            }
        });
        //右侧Y轴自定义值
//        rightAxis.setValueFormatter(new IAxisValueFormatter() {
//            @Override
//            public String getFormattedValue(float value, AxisBase axis) {
//                return (int) (value) + "%";
//            }
//        });

        BarData data = new BarData(dataSets);

        /**
         * float groupSpace = 0.3f;   //柱状图组之间的间距
         * float barSpace =  0.05f;  //每条柱状图之间的间距  一组两个柱状图
         * float barWidth = 0.3f;    //每条柱状图的宽度     一组两个柱状图
         * (barWidth + barSpace) * 2 + groupSpace = (0.3 + 0.05) * 2 + 0.3 = 1.00
         * 3个数值 加起来 必须等于 1 即100% 按照百分比来计算 组间距 柱状图间距 柱状图宽度
         */
        int barAmount = dataLists.size(); //需要显示柱状图的类别 数量
        //设置组间距占比0% 每条柱状图宽度占比 100% /barAmount  柱状图间距占比 0%
        float groupSpace = 0f; //柱状图组之间的间距
        float barWidth = (1f - groupSpace) / barAmount;
        float barSpace = 0f;
        //设置柱状图宽度
        data.setBarWidth(barWidth);
        //(起始点、柱状图组间距、柱状图之间间距)
        data.groupBars(0f, groupSpace, barSpace);
        barChart.setData(data);

        xAxis.setAxisMinimum(0f);
        xAxis.setAxisMaximum(xValues.size());
        //将X轴的值显示在中央
        xAxis.setCenterAxisLabels(true);
    }

    private void showBarChart(final List<VtDateValueBean> dateValueList, String name, int color) {
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
        initBarDataSet(barDataSet, color);
        //X轴自定义值
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return dateValueList.get((int) value % dateValueList.size()).getSYearMonth();
            }
        });
        //右侧Y轴自定义值
        rightAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return (int) (value) + "%";
            }
        });

//        // 添加多个BarDataSet时
//        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
//        dataSets.add(barDataSet);
//        BarData data = new BarData(dataSets);

        BarData data = new BarData(barDataSet);
        barChart.setData(data);
    }
}

