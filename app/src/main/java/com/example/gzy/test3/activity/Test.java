package com.example.gzy.test3.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.gzy.test3.R;
import com.example.gzy.test3.model.SleepModel;
import com.example.gzy.test3.presenter.SleepPresenter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import cn.bmob.v3.Bmob;

/**
 * created by gzy on 2019/5/5.
 * Describle;
 */
public class Test extends AppCompatActivity {
    SimpleDateFormat df = new SimpleDateFormat("HH:mm");
    SleepPresenter sleepPresenter=new SleepPresenter();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this, "5a5dcb5264e14c4fa9886e8511707ac0");
        setContentView(R.layout.sleepactivity);
       //test1();
       sleepPresenter.findAll();
    }
    public void test1(){
        SleepModel sleepModel=new SleepModel();
        SleepPresenter sleepPresenter=new SleepPresenter();
        for(int i=0;i<1000;i++){
            sleepModel.setSleepdate(randomDate());
            sleepModel.setEndtime(radomtime());
            sleepModel.setStarttime(radomtime());
            sleepModel.setScore(radomscore());
            sleepModel.setTotalsleep(countTotalSleep(sleepModel.getStarttime(),sleepModel.getEndtime()));
            sleepPresenter.saveSleep(sleepModel);
        }
        Log.i("asa","asasa");
    }
    public String countTotalSleep(String strTime1,String strTime2){
        Date start = null;
        Date end=null;
        try {
            start = df.parse(strTime1);
            end=df.parse(strTime2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(start.getTime()<=end.getTime()){
            long l= Math.abs(start.getTime()-end.getTime());       //获取时间差
            long day=l/(24*60*60*1000);
            long hour=(l/(60*60*1000)+day*24);
            long min=((l/(60*1000))-day*24*60-hour*60);
            return hour+":"+min;
        }else {
            Calendar c=Calendar.getInstance();
            c.setTime(end);
            c.add(Calendar.DAY_OF_MONTH, 1);
            long l= (c.getTimeInMillis()-start.getTime());       //获取时间差
            long day=l/(24*60*60*1000);
            long hour=(l/(60*60*1000)+day*24);
            long min=((l/(60*1000))-day*24*60-hour*60);
            return hour+":"+min;
        }

    }
    public String radomtime(){
        Random rnd=new Random();
        return rnd.nextInt(24)+":"+rnd.nextInt(60);
    }
    public float radomscore(){
        Random rnd=new Random();
        return rnd.nextFloat()*100;
    }
    public String randomDate(){
        Random rndYear=new Random();
        int year=2019;  //生成[2000,2017]的整数；年
        Random rndMonth=new Random();
        int month=rndMonth.nextInt(12)+1;   //生成[1,12]的整数；月
        Random rndDay=new Random();
        int Day=rndDay.nextInt(30)+1;       //生成[1,30)的整数；日
        return year+"-"+month+"-"+Day;
    }
}
