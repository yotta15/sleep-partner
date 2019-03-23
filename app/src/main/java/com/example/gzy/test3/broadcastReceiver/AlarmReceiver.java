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
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.gzy.test3.R;
import com.example.gzy.test3.activity.ContentActivity;

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
        String name = "我是渠道名字";
        notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        Notification notification = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_LOW);
            // Toast.makeText(this, mChannel.toString(), Toast.LENGTH_SHORT).show();
            //  Log.i(TAG, mChannel.toString());
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

        //int noteId = intent.getIntExtra("noteId",1);
        //  NoteBody noteBody = NotesLab.get(context).queryNote(noteId);
        //发送通知
        //PendingIntent pi = PendingIntent.getActivity(context,0,intent2,0);
//            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//            NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
//                    .setContentTitle("闹钟")
//                    .setContentText("闹钟响了")
//                    .setSmallIcon(R.mipmap.ic_launcher)
//                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.mipmap.ic_launcher))
//                    .setDefaults(NotificationCompat.DEFAULT_ALL)
//                    .setPriority(NotificationCompat.PRIORITY_MAX)
//                    .setAutoCancel(true)
//                    .setContentIntent(pi);
//            Notification notification = builder.build();
//            manager.notify(1,notification);

        //设置通知内容并开启
//        manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
//        Notification notification = new Notification.Builder(context)
//                .setChannelId(id)
//                .setSmallIcon(R.drawable.inform)
//                .setContentTitle("闹钟")
//                .setContentText("开启美好一天")
//                .setWhen(System.currentTimeMillis())
//                .setDefaults(Notification.DEFAULT_VIBRATE)
//                .setDefaults(Notification.DEFAULT_SOUND)
//                .build();
//
//
//        //设置声音循环播放
//        notification.flags = Notification.FLAG_INSISTENT;
//
//        notification.defaults = Notification.DEFAULT_VIBRATE;
//
//
//        /*
//         * 设置灯光
//         * 使用默认灯光或自定义灯光
//         * */
////        notification.defaults |= Notification.DEFAULT_LIGHTS;
//        //自定义
//        notification.ledARGB = 0xFFFFFF;//白色//0xff00ff00;//绿色//灯光颜色
//        notification.ledOnMS = 300;//亮持续时间
//        notification.ledOffMS = 1000;//暗的时间
//        notification.flags = Notification.FLAG_SHOW_LIGHTS;
//
//        //其他设置
//        //通知被点击后，自动消失，没起作用？？
//        notification.flags = Notification.FLAG_AUTO_CANCEL;
//
//        manager.notify(1, notification);// 这个notification 的 id 设为1023
//
    }

}

