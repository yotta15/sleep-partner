package com.example.gzy.test3.activity;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.gzy.test3.R;
import com.example.gzy.test3.broadcastReceiver.AlarmReceiver;
import com.example.gzy.test3.service.AlarmService;
import com.githang.statusbar.StatusBarCompat;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * created by gzy on 2019/3/21.
 * Describle;
 * 只能设置一天的闹钟
 * 耗时操作交给service
 */
public class AlarmActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView mreturn;
    private Switch mswitch;
    private TimePicker mtimePicker;
    private AlarmManager alarmManager;
    Intent intent;
    PendingIntent pendingIntent;
    Calendar c=Calendar.getInstance();
    boolean Tag = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //状态栏透明
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.white));

        setContentView(R.layout.alarm_set);
        intent = new Intent(AlarmActivity.this, AlarmService.class);

        //用于获取alarmmanager对象
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        //动态注册,android 8.0版本问题
        final IntentFilter intentFilter=new IntentFilter("com.gzy.android.RING");
        AlarmReceiver alarmReceiver=new AlarmReceiver();
        registerReceiver(alarmReceiver,intentFilter);

        mreturn = (ImageView) findViewById(R.id.iv_return);
        mreturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AlarmActivity.this,ContentActivity.class);
                startActivity(intent);
            }
        });
        mswitch = (Switch) findViewById(R.id.of_switch);

        mtimePicker = (TimePicker) findViewById(R.id.tp_clock);
        mtimePicker.setIs24HourView(true);
        //设置默认时间为当前时间加1
        int i = c.get(Calendar.HOUR_OF_DAY)+1;
        mtimePicker.setCurrentHour(i);
        mtimePicker.cancelPendingInputEvents();
        //TODO 日期设置记住用户习惯,重新设置闹钟的时候重启服务
        mtimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hourofday, int minute) {
                //   Toast.makeText(getApplicationContext(), "time:" + hourofday + minute, Toast.LENGTH_LONG).show();

                //  c.setTimeInMillis(System.currentTimeMillis());
                c.set(Calendar.HOUR, hourofday);
                c.set(Calendar.MINUTE, minute);
                c.set(Calendar.SECOND, 0);
                c.set(Calendar.MILLISECOND, 0);
              //  Toast.makeText(getApplicationContext(), "" + c.getTimeInMillis(), Toast.LENGTH_LONG).show();

                intent.putExtra("calendar", c);
                //TODO service并不会重复启动
                setAlarm();
            }
        });
        mswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    Tag = true;
                    c.setTimeInMillis(System.currentTimeMillis());
                    intent.putExtra("calandar",c);


                    setAlarm();
                    //  Toast.makeText(getApplicationContext(), "time", Toast.LENGTH_LONG).show();
                    // AlarmActivity.this.startService(intent);



                } else {
                    Tag = false;
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_return:
                break;

        }

    }

    private void setAlarm() {
        if (Tag) {
            if(isServiceRunning(getApplicationContext(),"com.example.gzy.test.service.AlarmService")){
                //TODO STOPSELF
            }
            getApplicationContext().startService(intent);
//            pendingIntent = PendingIntent.getService(AlarmActivity.this, 0, intent, 0);
//            //根据不同的版本使用不同的设置方法,将在calendar对应的时间启动paddingintent
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
//            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
//            } else {
//                alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
//            }
        }
    }
    /**
     * 判断服务是否开启
     *
     * @return
     */
    public static boolean isServiceRunning(Context context, String ServiceName) {
        if (TextUtils.isEmpty(ServiceName)) {
            return false;
        }
        ActivityManager myManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager
                .getRunningServices(30);
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName().toString()
                    .equals(ServiceName)) {
                return true;
            }
        }
        return false;
    }
  //页面销毁时需进行解注册
    @Override
    protected void onDestroy() {
        super.onDestroy();
        AlarmReceiver alarmReceiver=new AlarmReceiver();
        unregisterReceiver(alarmReceiver);
    }
}
