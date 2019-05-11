package com.example.gzy.test3.activity;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.gzy.test3.R;
import com.example.gzy.test3.service.AudioService;
import com.example.gzy.test3.service.SensorService;
import com.example.gzy.test3.service.WriteDataUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * created by gzy on 2019/4/21.
 * Describle;
 */
public class SleepActivity extends AppCompatActivity implements View.OnClickListener,View.OnLongClickListener,Runnable{
    private ImageView imageView,medit;
    private TextView tvTime,tvShowalarm,tvShowtip;
    TimePickerView pvTime;
    Button mbtfinish;
    Handler handler =new Handler();
    private String DEFAULT_TIME_FORMAT = "HH:mm";
    AudioServiceConn audioServiceConn;
    SensorServiceConn sensorServiceConn;

    private AudioService.MyBinder audiobinder;
    private SensorService.MyBinder sensorbinder;
    private  WriteDataUtil writeDataUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        setContentView(R.layout.sleepactivity);
        initview();
        bindservice();
        handler.post(this);


        writeDataUtil=new WriteDataUtil();

        writeDataUtil.onCreate(getApplicationContext());
//        startRecord();

        //writeDataUtil.getBatteryinfo(getApplicationContext());
//        imageView = (ImageView) findViewById(R.id.sleep_background);
//        imageView.setScaleType(ImageView.ScaleType. CENTER_CROP);

    }

    public void bindservice(){
        Intent intent1 = new Intent(this, AudioService.class);
       audioServiceConn= new AudioServiceConn();
        bindService(intent1, audioServiceConn, BIND_AUTO_CREATE);

        Intent intent2 = new Intent(this, SensorService.class);
         sensorServiceConn = new SensorServiceConn();
        this.bindService(intent2, sensorServiceConn, BIND_AUTO_CREATE);


    }

    public void initview() {

       // Typeface tf1 = Typeface.createFromAsset(getAssets(), "fonts/huawen.ttf");
        Typeface tf2 = Typeface.createFromAsset(getAssets(), "fonts/youyuan.ttf");
        tvTime = (TextView) findViewById(R.id.tv_showtime);
        tvShowalarm=(TextView)findViewById(R.id.tv_showalarm);
        tvTime. setTypeface(tf2);
        tvShowalarm. setTypeface(tf2);
        mbtfinish=(Button) findViewById(R.id.bt_finish);
        mbtfinish.setOnLongClickListener(this);
        medit=(ImageView)findViewById(R.id.iv_edit);
        medit.setOnClickListener(this);
        tvShowtip=(TextView) findViewById(R.id.tv_showtip);
        String[] tips=getApplicationContext().getResources().getStringArray(R.array.tips);
        tvShowtip.setText(tips[(int)(Math.random()*tips.length)]);

        final SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_TIME_FORMAT);
        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                tvShowalarm.setText(sdf.format(date));
            }
        })
                .setType(new boolean[]{false, false, false, true, true, false})// 默认全部显示
                .setSubmitText("确定")//确认按钮文字
                .setContentTextSize(18)//滚轮文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleText("闹钟")//标题文字
                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(true)//是否循环滚动
                .setTitleColor(Color.WHITE)//标题文字颜色
                .setSubmitColor(Color.WHITE)//确定按钮文字颜色
                .setCancelColor(Color.WHITE)//取消按钮文字颜色
                .setTitleBgColor(0xFF666666)//标题背景颜色 Night mode
                .setBgColor(0xFF333333)//滚轮背景颜色 Night mode
                .setLabel("","","", "时", "分","")//默认设置为年月日时分秒
                .isCenterLabel(true) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isDialog(true)//是否显示为对话框样式
                .build();

    }

    /**
     * 设置状态栏透明
     */
    public void setStatusBar() {
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }

    }
    private class SensorServiceConn implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            sensorbinder = (SensorService.MyBinder) iBinder;
            sensorbinder.startRecord();
            SensorService sensorService = sensorbinder.getService();

            sensorService.setCallback(new SensorService.Callback() {
                @Override
                public void onDataChange(String data) {

                    writeDataUtil.SensorData(data);

                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.i("conn","disconneted");
        }
    }

    private class AudioServiceConn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            audiobinder = (AudioService.MyBinder) iBinder;
            audiobinder.startRecord();
            AudioService audioService = audiobinder.getservice();
            audioService.setCallback(new AudioService.Callback() {
                @Override
                public void onDataChange(String data) {
                    writeDataUtil.audioData(data);
                    //记录
                }

                @Override
                public void onVolumeChange(double data) {
                    writeDataUtil.volumeData(data);
                }

            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case (R.id.iv_edit):
                pvTime.show();
        }

    }

    @Override
    public void run() {
        handler.postDelayed(this, 60000);
        SimpleDateFormat dateFormatter = new SimpleDateFormat(DEFAULT_TIME_FORMAT);
        String  time = dateFormatter.format(Calendar.getInstance().getTime());
        tvTime.setText(time);
    }

    @Override
    public boolean onLongClick(View view) {
        sensorbinder.stopitself();
        audiobinder.stopRecord();
        writeDataUtil.ondestroy();
        unbindService(audioServiceConn);
        unbindService(sensorServiceConn);
        Intent intent=new Intent(this, ShowBarchartActivity.class);
        intent.putExtra("sleepinfo", writeDataUtil.getSleepinfo());
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        return false;
    }
}
