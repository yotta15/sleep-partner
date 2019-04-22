package com.example.gzy.test3.broadcastReceiver;

/**
 * created by gzy on 2019/4/21.
 * Describle;
 */


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.gzy.test3.service.SensorService;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        if (intent.getAction().equals("arui.alarm.action")) {
            Intent i = new Intent();
            i.setClass(context, SensorService.class);
            // 启动service
            // 多次调用startService并不会启动多个service 而是会多次调用onStart
            context.startService(i);
        }
    }

}
