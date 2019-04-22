package com.example.gzy.test3.service;

import android.os.Handler;
import android.support.annotation.IntegerRes;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * created by gzy on 2019/4/19.
 * Describle;
 * 振动是第一要素，语音识别第二要素，音量回调是第三要素,睡觉时间是第四要素
 */
public class WriteDataUtil {
    private static float aweakThreshold = 40;
    private static float lightSleepThreshold = 10;
    private static float deepSleepThreshold = 4;
    private static float staticThreshold = 0.5f;
    HashMap<String,Integer> hashMap=new HashMap<>();
    Handler  handler=new Handler();
    Runnable runnable;
    private  int[] count=new int[4];

//todo  手机加速度传感器会停止活动
    public void oncreate(){
        Calendar c=Calendar.getInstance();
      String starttime=c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND);
        runnable = new Runnable() {
            public void run() {
                // TODO Auto-generated method stub  
                handler.postDelayed(runnable, 10000);
                countMap(hashMap);
            }
        };

        handler.postDelayed(runnable, 2000);

    }
    public void countMap(HashMap<String,Integer> hashMap){
        hashMap.put("awake",count[0]);
        hashMap.put("lightSleep",count[1]);
        hashMap.put("deepSleep",count[2]);
        hashMap.put("static",count[3]);
        findmode(hashMap);
           // hashMap.clear();
    }
    public void findmode(HashMap<String,Integer> hashMap){
        int k=0;
        String s= "";
        Iterator iter = hashMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Integer value= (Integer) entry.getValue();
            if(k < value){
                k=value;
                s=(String) entry.getKey();
            }
        }
        for  (int i=0;i<4;i++){
            count[i]=0;
        }
        //最多的 value值，根据value找key
        Log.i("findmode",""+k +"   "+s);

    }


    public void SensorData(String delta) {
//        if(int i=0){
//            oncreate();
//        }
        float data = Float.parseFloat(delta);
        if (data >= aweakThreshold) {
            Log.i("s    hakeaweak 醒/梦", "" + delta);
           count[0]++;
        } else if (data >= lightSleepThreshold && data < aweakThreshold) {
            Log.i("shakeThreshold", "" + delta);
            count[1]++;
        } else if (data < deepSleepThreshold && data >= staticThreshold) {
            Log.i("深睡", "" + delta);
            count[2]++;
        } else if (data < staticThreshold) {
            count[3]++;
            Log.i("静止属于浅睡", "" + delta);

        }

        //计算总值，计算众数，数组清零，

    }
    public void onpause(){
        handler.removeCallbacks(runnable);
    }
}
