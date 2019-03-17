package com.example.gzy.test3.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.gzy.test3.UI.Toastview;

/**
 * created by gzy on 2019/3/17.
 * Describle;
 */
public class SensorService extends Service implements SensorEventListener{
    SensorManager manager;
    Toastview toastview;
    private MyBinder myBinder=new MyBinder();
    //检测的时间间隔
    private static final int UPDATE_INTERVAL = 100;
    //摇晃检测阈值，决定了对摇晃的敏感程度，越小越敏感
    private int shakeThreshold = 10;
    //,x轴默认为0,y轴默认0,z轴默认9.81
    float mLastX=0,mLastY=0,mLastZ=(float)9.81;
    long mLastUpdateTime;

    String tag1="x";
    String tag2="y";
    String tag3="z";
    @Override
    public void onCreate() {
        super.onCreate();
        //获取系统的传感器管理服务，并注册监听加速度传感器
         manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
         manager.registerListener(this,manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),manager.SENSOR_DELAY_UI);
        // toastview.showToast(getApplication(),"Sensor已初始化");
        Log.i("11","Sensor已初始化");
    }
    public class MyBinder extends Binder{
        public void stopitself(){
            //TODO
        }
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return myBinder;
    }



    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float[] values= sensorEvent.values;
        long currentTime = System.currentTimeMillis();
        long diffTime = currentTime - mLastUpdateTime;
        if (diffTime < UPDATE_INTERVAL) {
            return;
        }
        mLastUpdateTime = currentTime;


        float x = values[0];
        float y = values[1];
        float z = values[2];
        float deltaX = x - mLastX;
        float deltaY = y - mLastY;
        float deltaZ = z - mLastZ;
        mLastX = x;
        mLastY = y;
        mLastZ = z;
        //只取x,z
        float delta = (float) (Math.sqrt(deltaX * deltaX  + deltaZ * deltaZ) / diffTime * 10000);
        // 当检测到一个摇晃（加速度的差值大于指定阈值）,差不多是这了，
        if (delta > shakeThreshold ) {
            Log.i("SensorChange","change");
            //TODO
            // dosomething();
        }


        //toastview.showToast(this,"");

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    //取消注册
    public void onStop(){
        manager.unregisterListener(this);
    }
}
