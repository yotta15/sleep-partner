package com.example.gzy.test3.broadcastReceiver;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.gzy.test3.R;
import com.example.gzy.test3.activity.ContentActivity;

import static android.app.Notification.VISIBILITY_SECRET;
import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * created by gzy on 2019/3/22.
 * Describle;
 * activity->service,broadcastreceiver，
 * TODO 闹钟开始前10分钟设置提醒关闭闹钟
 */
public class AlarmReceiver extends BroadcastReceiver {
    NotificationManager notificationManager;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {


        Log.i("receiver", "ring");
        Toast.makeText(context, "receicer", Toast.LENGTH_LONG).show();
        //    Intent intent2 = new Intent(context,ContentActivity.class);
        //adnroid 8.0 需要设置渠道名字
        String id = "my_channel_01";
        String name = "SLEEP_PARNTER";
        notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        Notification notification = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_LOW);
            // Toast.makeText(this, mChannel.toString(), Toast.LENGTH_SHORT).show();
            //  Log.i(TAG, mChannel.toString());
            mChannel.enableLights(true);
            //锁屏显示通知
            mChannel.setLockscreenVisibility(VISIBILITY_SECRET);
            //闪关灯的灯光颜色
            mChannel.setLightColor(Color.RED);
            //桌面launcher的消息角标
            mChannel.canShowBadge();
            //是否允许震动
            mChannel.enableVibration(true);
            //获取系统通知响铃声音的配置
            mChannel.getAudioAttributes();
            //获取通知取到组
            mChannel.getGroup();
            //设置可绕过  请勿打扰模式
            mChannel.setBypassDnd(true);
            //设置震动模式
            mChannel.setVibrationPattern(new long[]{100, 100, 200});
            //是否会有灯光
            mChannel.shouldShowLights();
            notificationManager.createNotificationChannel(mChannel);
            notification = new Notification.Builder(context)
                    .setChannelId(id)
                    .setSmallIcon(R.drawable.inform)
                    .setContentTitle("闹钟")
                    .setContentText("开启美好一天")
                    .setWhen(System.currentTimeMillis())
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .build();
        } else {
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.inform)
                    .setContentTitle("闹钟")
                    .setContentText("开启美好一天")
                    .setWhen(System.currentTimeMillis())
                    .setOngoing(true);
            notification = notificationBuilder.build();
        }
        notificationManager.notify(111123, notification);

    }

}

