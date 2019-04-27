package com.example.gzy.test3.service;

import android.annotation.TargetApi;
import android.app.AlarmManager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.gzy.test3.R;
import com.example.gzy.test3.UI.Toastview;
import com.example.gzy.test3.activity.ContentActivity;
import com.example.gzy.test3.broadcastReceiver.NotificationReceiver;

import static android.app.Notification.VISIBILITY_SECRET;

/**
 * created by gzy on 2019/3/17.
 * Describle;
 */
public class SensorService extends Service implements SensorEventListener {
    SensorManager sensorManager;
    Callback callback;
    PowerManager pm;
    PowerManager.WakeLock wl;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }


    //检测的时间间隔
    private static final int UPDATE_INTERVAL = 1000;
    //摇晃检测阈值，决定了对摇晃的敏感程度，越小越敏感
    private int shakeThreshold = 10;
    private int shakeaweak = 40;
    //,x轴默认为0,y轴默认0,z轴默认9.81
    float mLastX = 0, mLastY = 0, mLastZ = (float) 9.81;
    long mLastUpdateTime;

    String tag1 = "x";
    String tag2 = "y";
    String tag3 = "z";

    @Override
    public void onCreate() {
        super.onCreate();
        //获取系统的传感器管理服务，并注册监听加速度传感器
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        keepcpuawake();

    }

    private void keepcpuawake() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
            mBuilder.setPriority(Notification.PRIORITY_MIN);// 设置该通知优先级
            //  mBuilder.setSmallIcon(R.drawable.gpsblue);
            Notification notification = mBuilder.build();
//        startForeground(1, notification);

            //点击通知栏跳转到相应的应用里面
            Intent intent1 = new Intent(this, ContentActivity.class);
            //这一句加不加没什么影响
// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            notification.contentIntent = PendingIntent.getActivity(this, 1, intent1, 0);

            //这里的id不能是0
            startForeground(1, notification);
        }
        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "My Tag");
        wl.acquire();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    public void start() {
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), sensorManager.SENSOR_DELAY_UI);

    }

    public class MyBinder extends Binder {
        public SensorService getService() {
            return SensorService.this;
        }

        public void startRecord() {
            start();
        }

        public void stopitself() {
            stop();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    public static interface Callback {
        void onDataChange(String data);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float[] values = sensorEvent.values;
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
        float deltaZ = z - mLastZ;//正值
     //   Log.i("deltaz",deltaZ+"");
        mLastX = x;
        mLastY = y;
        mLastZ = z;
        //只取x,z
        float delta = (float) (Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ) / diffTime * 10000);
        // 当检测到一个摇晃（加速度的差值大于指定阈值）,差不多是这了，
//        if(delta>shakeaweak){
//            Log.i("s    hakeaweak 醒/梦",""+delta) ;
//        }else if (delta > 4 && delta<shakeaweak) {
//            //  callback.onDataChange("" + delta);
//            //  Log.i("SensorChan                    ge","change");
//            Log.i("shakeThreshold",""+delta) ;
//        }else if(delta<5 && delta > 4){
//            Log.i("深睡",""+delta) ;
//        }else if(delta < 0.5){
        //Log.i("test", "" + delta);
//        }
        //TODO
        callback.onDataChange(String.valueOf(delta));


        //toastview.showToast(this,"");

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    //取消注册
    public void stop() {
        sensorManager.unregisterListener(this);
          wl.release();
    }

    @Override
    public boolean onUnbind(Intent intent) {
      //  sensorManager.unregisterListener(this);
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
