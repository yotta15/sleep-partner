package com.example.gzy.test3.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.JsonWriter;

import com.example.gzy.test3.R;
import com.example.gzy.test3.model.SleepInfo;
import com.example.gzy.test3.model.SleepstateBean;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * created by gzy on 2019/4/3.
 * Describle;
 * !!!!! assets 目录下只能读，不能写！！！！！！！
 */
public class WriteDataService extends Service {
    SleepInfo sleepInfo=new SleepInfo();
    List<SleepstateBean> sleepstates=new ArrayList<>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //写json文件
    @Override
    public void onCreate() {
        super.onCreate();

    }

    //单例模式，没有<T>就不知道T是从何而来的
    public  <T> void toJson(String  key, T value) {
//        String sleepdate = "2018-01-03";
//        String starttime = "22:05:30";
//        String endtime = "07:20:30";
//        int turnover = 5;
//        int dreamtalk = 2;
//        String beforesleep = "excited";
//        String noise = "25db";
//        String score = "75";

        switch (key){
            case "sleepdate":
                sleepInfo.setSleepdate((String)value);
                break;
            case "starttime":
                sleepInfo.setStarttime((String) value);
                break;
            case "endtime":
                sleepInfo.setEndtime((String)value);
                break;
            case "turnover":
                sleepInfo.setTurnover((Integer) value);
                break;
            case "dreamtalk":
                sleepInfo.setDreamtalk((Integer)value);
                break;
            case "beforesleep":
                sleepInfo.setBeforesleep((String)value);
                break;
            case "noise":
                sleepInfo.setNoise((String) value);
                break;
            case "score":
                sleepInfo.setScore((Integer) value);
                break;
            case"sleepstate":
                SleepstateBean sleepstateBean=new SleepstateBean();
                sleepstateBean.setFvalue((Integer) value);
                Calendar c=Calendar.getInstance();
                sleepstateBean.setSleeptime(c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND));
                sleepstates.add(sleepstateBean);
                break;
        }


    }
    //结束记录,service 销毁
    public  void endRecording(){
        //格式化，不忽略空字段
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

    public File createFile(String jsonstr) throws IOException {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH/mm/ss");// HH:mm:ss
//获取当前时间
        Date date = new Date(System.currentTimeMillis());
        String filename = "data_" + simpleDateFormat.format(date) + ".json";
        File saveFile = new File(Environment.getExternalStorageDirectory()
                + "/" + getApplicationContext().getResources().getString(R.string.default_file_name), filename);
        if (!saveFile.exists()) {
            saveFile.mkdirs();
        }
        FileWriter fileWritter = new FileWriter(saveFile);
        fileWritter.write(jsonstr);
        fileWritter.close();
        return saveFile;
    }

}
