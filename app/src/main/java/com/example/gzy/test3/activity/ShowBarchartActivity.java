package com.example.gzy.test3.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.gzy.test3.R;
import com.example.gzy.test3.bardata.MyBarDataSet;
import com.example.gzy.test3.model.SleepInfo;
import com.example.gzy.test3.model.SleepstateBean;
import com.example.gzy.test3.util.LocalJsonAnalyzeUtil;
import com.githang.statusbar.StatusBarCompat;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * created by gzy on 2019/5/8.
 * Describle;
 */
public class ShowBarchartActivity extends AppCompatActivity {
    SleepInfo sleepInfo;
    private YAxis leftAxis;             //左侧Y轴
    private YAxis rightAxis;            //右侧Y轴
    private XAxis xAxis;                //X轴
    private Legend legend;              //图例
    BarChart barChart;
    TextView tv_score, tv_starttime, tv_endtime, tv_totaltime, tv_noise;
    ImageView iv_starttime, iv_endtime;
    RatingStarView rsv_rating;
    SimpleDateFormat df = new SimpleDateFormat("HH:mm");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.white));
        setContentView(R.layout.activity_showbarchart);
        Intent intent = getIntent();

        Bundle bundle = intent.getExtras();
        assert bundle != null;
        sleepInfo = (SleepInfo) bundle.getSerializable("sleepinfo");
        barChart = (BarChart) findViewById(R.id.activity_barchart);
        tv_score = (TextView) findViewById(R.id.tv_show_score);
        LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 600);
        tvParams.gravity = Gravity.CENTER_HORIZONTAL;
        tvParams.gravity = Gravity.CENTER_VERTICAL;
        barChart.setLayoutParams(tvParams);

        rsv_rating = (RatingStarView) findViewById(R.id.show_rsv_rating);
        tv_starttime = (TextView) findViewById(R.id.tv_show_starttime);
        iv_starttime = (ImageView) findViewById(R.id.iv_startStandard);
        tv_endtime = (TextView) findViewById(R.id.tv_show_endtime);
        iv_endtime = (ImageView) findViewById(R.id.iv_endStandard);

        tv_totaltime = (TextView) findViewById(R.id.tv_show_totaltime);
        tv_noise = (TextView) findViewById(R.id.tv_show_noise);

        initBarChart(barChart);
        showBarChart(sleepInfo.getSleepstate(), "", getApplicationContext(), barChart);

        tv_score.setText(String.valueOf(sleepInfo.getScore()));
        //百分制转化为五分制
        rsv_rating.setRating(sleepInfo.getScore() / 20.0f);
        String[] str1 = sleepInfo.getStarttime().split(":");
        String[] str2 = sleepInfo.getEndtime().split(":");

        tv_noise.setText(sleepInfo.getNoise());
        tv_starttime.setText(sleepInfo.getStarttime());
        tv_endtime.setText(sleepInfo.getEndtime());
        tv_totaltime.setText(countTotalSleep(sleepInfo.getStarttime(), sleepInfo.getEndtime()));

        String startStandard = LocalJsonAnalyzeUtil.getStandard(getApplicationContext(), "StarttimeSleep");
        String tipStartTime = tipStandard(startStandard);
        iv_starttime.setImageResource(tipStartTime.equals("normal") ? R.drawable.standard : tipStartTime.equals("up")
                ? R.drawable.uparrow : R.drawable.downarrow);

        String endStandard = LocalJsonAnalyzeUtil.getStandard(getApplicationContext(), "EndtimeSleep");
        String tipsEndTime = tipStandard(endStandard);
        iv_endtime.setImageResource(tipsEndTime.equals("normal") ? R.drawable.standard : tipsEndTime.equals("up")
                ? R.drawable.uparrow : R.drawable.downarrow);
        ImageView iv_return=(ImageView)findViewById(R.id.iv_show_return);
        iv_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), ContentActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    public String tipStandard(String standard) {
        String[] str = standard.split("-");
        String tipStandard = "";
        try {
            Date date = df.parse(sleepInfo.getStarttime());
            if (date.getTime() <= df.parse(str[1]).getTime() && date.getTime() >= df.parse(str[0]).getTime()) {
                tipStandard = "normal";
            } else if (date.getTime() <= df.parse(str[0]).getTime()) {
                tipStandard = "up";
            } else if (date.getTime() >= df.parse(str[1]).getTime()) {
                tipStandard = "down";
            }
        } catch (ParseException e) {
            tipStandard = "";
            e.printStackTrace();
        }
        return tipStandard;
    }

    public String countTotalSleep(String strTime1, String strTime2) {
        Date start = null;
        Date end = null;
        try {
            start = df.parse(strTime1);
            end = df.parse(strTime2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (start.getTime() < end.getTime()) {
            long l = Math.abs(start.getTime() - end.getTime());       //获取时间差
            long day = l / (24 * 60 * 60 * 1000);
            long hour = (l / (60 * 60 * 1000) + day * 24);
            long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
            return hour + ":" + min;
        } else {
            Calendar c = Calendar.getInstance();
            c.setTime(end);
            c.add(Calendar.DAY_OF_MONTH, 1);
            long l = (c.getTimeInMillis() - start.getTime());       //获取时间差
            long day = l / (24 * 60 * 60 * 1000);
            long hour = (l / (60 * 60 * 1000) + day * 24);
            long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
            return hour + ":" + min;
        }

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
