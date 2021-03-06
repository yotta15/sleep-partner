package com.example.gzy.test3.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

//alarmmanager
public class AlarmService extends Service {

    private static final String TAG = "test";
    private Calendar mCalendar;
    private AlarmManager mAlarmManager;
    MediaPlayer mediaPlayer;
    PendingIntent pendingIntent;
    public AlarmService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        Log.i("闹钟","ring");
//        Toast.makeText(getApplicationContext(),"闹钟响了",Toast.LENGTH_LONG).show();


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        mCalendar = (Calendar) intent.getSerializableExtra("calendar");
        //int noteId = intent.getIntExtra("noteId", 0);
        Intent intent1=new Intent();
        intent1.setAction("com.gzy.android.RING");
        pendingIntent = PendingIntent.getBroadcast(AlarmService.this, 0, intent1, 0);
        //根据不同的版本使用不同的设置方法,将在calendar对应的时间启动paddingintent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mAlarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mAlarmManager.setExact(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), pendingIntent);
        } else {
            mAlarmManager.set(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), pendingIntent);
        }
//        Intent intent1=new Intent();
//        intent1.setAction("com.gzy.android.RING");
//        sendBroadcast(intent1);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: 服务被杀死");
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
