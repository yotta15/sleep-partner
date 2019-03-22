package com.example.gzy.test3.activity;

import android.app.AlarmManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.gzy.test3.R;
import com.example.gzy.test3.service.AlarmService;
import com.githang.statusbar.StatusBarCompat;

import java.util.Calendar;


/**
 * created by gzy on 2019/3/21.
 * Describle;
 */
public class AlarmActivity extends AppCompatActivity  implements View.OnClickListener {
    private  ImageView  mreturn;
    private  Switch mswitch;
    private TimePicker mtimePicker;
    private AlarmManager alarmManager;
    Intent intent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //状态栏透明
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.white));

        setContentView(R.layout.alarm_set);
        intent = new Intent(AlarmActivity.this, AlarmService.class);

        //用于获取alarmmanager对象
        alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE) ;
//        Intent intent = new Intent(getActivity(), AlarmService.class);
//        intent.putExtra("calendar", CalendarLab.get(getActivity()).getCalendar());
//        intent.putExtra("noteId", noteId);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        getActivity().startService(intent);
////发送一条启动闹铃图标的广播
//        Intent intentIcon = new Intent("com.gaozhidong.android.Color");
//        intentIcon.putExtra("noteId", noteId);
//        getActivity().sendBroadcast(intentIcon);

        mreturn=(ImageView)findViewById(R.id.iv_return);
        mreturn.setOnClickListener(this);
        mswitch=(Switch)findViewById(R.id.of_switch);

        mtimePicker=(TimePicker)findViewById(R.id.tp_clock);
        mtimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hourofday, int minute) {
                Toast.makeText(getApplicationContext(), "time:" + hourofday + minute, Toast.LENGTH_LONG).show();
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(System.currentTimeMillis());
                c.set(Calendar.HOUR, hourofday);
                c.set(Calendar.MINUTE, minute);
                intent.putExtra("calendar", c);

            }
        });
        mswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    AlarmActivity.this.startService(intent);

                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_return:
                break;

        }

    }
}
