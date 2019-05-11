package com.example.gzy.test3.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gzy.test3.R;
import com.example.gzy.test3.presenter.SleepPresenter;
import com.example.gzy.test3.presenter.UserPresenterImpl;
import com.example.gzy.test3.util.LocalJsonAnalyzeUtil;
import com.githang.statusbar.StatusBarCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.bmob.v3.Bmob;

/**
 * created by gzy on 2019/5/11.
 * Describle;
 */
public class ShowSleepInfoActivity extends AppCompatActivity{
    UserPresenterImpl userPresenter=new UserPresenterImpl();
    TextView mTvname,mTvweek,mTvmonth,mTvyear;
    TextView mTVshowweek,mTVshowmonth,mTVshowyear;
    //格式化日期
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this, "5a5dcb5264e14c4fa9886e8511707ac0");
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.white));
        setContentView(R.layout.activity_showsleepinfo);
        mTvname=(TextView) findViewById(R.id.Sleepinfo_username);
        mTvname.setText(userPresenter.<String>queryData("username"));

        mTvweek=(TextView) findViewById(R.id.tv_weekinfo);
        mTvweek.setText("本周:"+getTimeInterval());
        mTvmonth=(TextView)findViewById(R.id.tv_monthinfo);
        mTvmonth.setText("本月:"+getTimeMonth());
        mTvyear=(TextView)findViewById(R.id.tv_yearinfo);
        mTvyear.setText("本年:"+getTimeYear());
        mTVshowweek=(TextView)findViewById(R.id.tv_show_weekinfo);
        mTVshowweek.setText("你一共睡了"+
                LocalJsonAnalyzeUtil.getUserStandard(getApplicationContext(),"week-m-00000"));
        mTVshowmonth=(TextView)findViewById(R.id.tv_show_monthinfo);
        mTVshowmonth.setText("你一共睡了"+
                LocalJsonAnalyzeUtil.getUserStandard(getApplicationContext(),"month-m-00000"));
        mTVshowyear=(TextView)findViewById(R.id.tv_show_yearinfo);
        mTVshowyear.setText("你一共睡了"+
                LocalJsonAnalyzeUtil.getUserStandard(getApplicationContext(),"year-m-00000"));
        ImageView iv_return=(ImageView) findViewById(R.id.iv_sleepinfo_return);
        iv_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), ContentActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    public String getTimeInterval() {
        Calendar cal = Calendar.getInstance();
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // 获得当前日期是一个星期的第几天
        int day = cal.get(Calendar.DAY_OF_WEEK);
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
        String imptimeBegin = sdf.format(cal.getTime());

        // System.out.println("所在周星期日的日期：" + imptimeEnd);
        return imptimeBegin + "-" + sdf.format(new Date());
    }
    public String getTimeMonth(){
        Calendar cale;
        cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, 0);
        cale.set(Calendar.DAY_OF_MONTH, 1);
        String firstday = sdf.format(cale.getTime());
       return  firstday+"-"+sdf.format(new Date());
    }
    public String getTimeYear(){
        Calendar cale= Calendar.getInstance();
        cale.set(cale.get(Calendar.YEAR),Calendar.JANUARY, 1);
        String firstday = sdf.format(cale.getTime());
        return  firstday+"-"+sdf.format(new Date());
    }
}
