package com.example.gzy.test3.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Calendar;

//alarmmanager
public class AlarmService extends Service {

    private static final String TAG = "test";
    private Calendar mCalendar;
    private AlarmManager mAlarmManager;

    public AlarmService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        mCalendar = (Calendar) intent.getSerializableExtra("calendar");
        //int noteId = intent.getIntExtra("noteId", 0);

        //设置广播
        Intent intent2 = new Intent();
        intent2.setAction("com.g.android.RING");
       // intent2.putExtra("noteId", noteId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent2, 0);

        //根据不同的版本使用不同的设置方法,将在calendar对应的时间启动paddingintent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mAlarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mAlarmManager.setExact(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), pendingIntent);
        } else {
            mAlarmManager.set(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), pendingIntent);
        }

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
