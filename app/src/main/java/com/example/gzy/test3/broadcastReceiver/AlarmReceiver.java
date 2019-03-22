package com.example.gzy.test3.broadcastReceiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import com.example.gzy.test3.R;
import com.example.gzy.test3.activity.ContentActivity;

/**
 * created by gzy on 2019/3/22.
 * Describle;
 * activity->service,一到时间启动broadcastreceiver，
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("com.g.android.RING")){
            Intent intent2 = new Intent(context,ContentActivity.class);
            //int noteId = intent.getIntExtra("noteId",1);
          //  NoteBody noteBody = NotesLab.get(context).queryNote(noteId);
            //发送通知
            PendingIntent pi = PendingIntent.getActivity(context,0,intent2,0);
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                    .setContentTitle("闹钟")
                    .setContentText("闹钟响了")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.mipmap.ic_launcher))
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setAutoCancel(true)
                    .setContentIntent(pi);
            Notification notification = builder.build();
            manager.notify(1,notification);

            //发送一条清空闹铃图标的广播
//            NotesLab.get(context).updateFlag(noteId,0);
//            Intent intent1 = new Intent("com.g.android.NoColor");
//            intent1.putExtra("noteId",noteId);
//            context.sendBroadcast(intent1);
        }
    }
}

