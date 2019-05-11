package com.example.gzy.test3.service;

import android.annotation.SuppressLint;
import android.os.Environment;

import com.example.gzy.test3.model.SleepInfo;
import com.example.gzy.test3.model.SleepModel;
import com.example.gzy.test3.model.SleepstateBean;
import com.example.gzy.test3.presenter.SleepPresenter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * created by gzy on 2019/4/3.
 * Describle;
 * !!!!! assets 目录下只能读，不能写！！！！！！！
 */
public class ConvertJsonUtil {
    SleepInfo sleepInfo = new SleepInfo();
    List<SleepstateBean> sleepstates = new ArrayList<>();
    SleepPresenter sleepPresenter=new SleepPresenter();
    SimpleDateFormat df = new SimpleDateFormat("HH:mm");

    //单例模式，没有<T>就不知道T是从何而来的
    public <T> void praseJson(String key, T value) {
//        String sleepdate = "2018-01-03";
//        String starttime = "22:05:30";
//        String endtime = "07:20:30";
//        int turnover = 5;
//        int dreamtalk = 2;
//        String beforesleep = "excited";
//        String noise = "25db";
//        String score = "75";

        switch (key) {
            case "sleepdate":
                sleepInfo.setSleepdate((String) value);
                break;
            case "starttime":
                sleepInfo.setStarttime((String) value);
                break;
            case "endtime":
                sleepInfo.setEndtime((String) value);
                break;
            case "turnover":
                sleepInfo.setTurnover((Integer) value);
                break;
            case "dreamtalk":
                sleepInfo.setDreamtalk((Integer) value);
                break;
            case "beforesleep":
                sleepInfo.setBeforesleep((String) value);
                break;
            case "noise":
                sleepInfo.setNoise((String) value);
                break;
            case "score":
                sleepInfo.setScore((Integer) value);
                break;
            case "sleepstate":
                SleepstateBean sleepstateBean = new SleepstateBean();
                sleepstateBean.setFvalue((Integer) value);
                Calendar c = Calendar.getInstance();
                sleepstateBean.setSleeptime(c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND));
                sleepstates.add(sleepstateBean);
                //sleepInfo.getSleepstate().add(sleepstateBean);
                break;
        }


    }

    /**
     * 计算各占比
     * 入睡快 、睡眠深 、无起夜、起床快
     *
     * @return
     */
    public int countScore() {
        int  count1 = 0, count2 = 0,count3=0;
        float score;
        for (int i = 0; i < sleepstates.size(); i++) {
            if (sleepstates.get(i).getFvalue() == SleepConstant.SLEEP_LIGHT||
                    sleepstates.get(i).getFvalue() == SleepConstant.SLEEP_DEEP) {
                count3=i;
            } else {
                break;
            }
        }
        // 越少愈好
        for (int i = sleepstates.size()-1; i >= 0; i--) {
            if (sleepstates.get(i).getFvalue() == SleepConstant.SLEEP_AWAKE) {
                count1++;
            } else {
                break;
            }
        }
        //count3 越小越好 ,count2 越大越好
        for (int i = 0; i < sleepstates.size(); i++) {
            if( sleepstates.get(i).getFvalue() == SleepConstant.SLEEP_DEEP){
                count2++;
            }
        }
        float size=(float) sleepstates.size();
        score=(count1/size+count3/size)*30f+count2/size*70f;
        return (int)score;
    }

    //结束记录,service 销毁
    public void endRecording() {
        //格式化，不忽略空字段
        //todo 自动检测空字段
        sleepInfo.setScore(countScore());
        sleepInfo.setSleepstate(sleepstates);
        //todo  存储在云数据库
        SleepModel sleepModel = new SleepModel();
        sleepModel.setSleepdate(sleepInfo.getSleepdate());
        sleepModel.setEndtime(sleepInfo.getEndtime());
        sleepModel.setStarttime(sleepInfo.getStarttime());
        sleepModel.setScore(countScore());
        sleepModel.setTotalsleep(countTotalSleep(sleepInfo.getStarttime(),sleepInfo.getEndtime()));

        sleepPresenter.saveSleep(sleepModel);

        Gson gson = new GsonBuilder().serializeNulls().create();
        String jsonStr = gson.toJson(sleepInfo);
        try {
            createFile(jsonStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //TODO 开启一个新线程延时中止该服务
        // getApplicationContext().stopService
    }
    public String countTotalSleep(String strTime1,String strTime2){
        Date start = null;
        Date end=null;
        Date oneday=null;
        try {
            start = df.parse(strTime1);
            end=df.parse(strTime2);
            oneday=df.parse("24:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(start.getTime()<end.getTime()){
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
    public File createFile(String jsonstr) throws IOException {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");// HH:mm:ss
//获取当前时间
        Date date = new Date(System.currentTimeMillis());
        String filename = "sleepdata " + simpleDateFormat.format(date) + ".json";
        File saveFile = new File(Environment.getExternalStorageDirectory()
                + "/" + "SleepPartner", filename);
        if (!saveFile.exists()) {
            saveFile.createNewFile();
        }
        FileWriter fileWritter = new FileWriter(saveFile);
        fileWritter.write(jsonstr);
        fileWritter.close();
        return saveFile;
    }

    public SleepInfo getSleepInfo() {
        return sleepInfo;
    }
}
