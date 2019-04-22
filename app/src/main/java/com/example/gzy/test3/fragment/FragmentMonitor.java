package com.example.gzy.test3.fragment;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.gzy.test3.R;
import com.example.gzy.test3.service.AudioRecordFunc;
import com.example.gzy.test3.service.AudioRecorder;
import com.example.gzy.test3.service.AudioRecorderService;
import com.example.gzy.test3.service.SensorService;
import com.example.gzy.test3.service.WriteDataUtil;

import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.BIND_AUTO_CREATE;


public class FragmentMonitor extends Fragment implements View.OnClickListener {
    private Timer timer;
    private TimerTask timerTask;
    private View view;

    private Button sleep, getup;
    private int i = 0;
    private AudioRecordFunc audioRecorder;
    private AudioRecorder maudioRecorder;
    private ImageView imageView;
    AudioRecorderService.MyBinder audiobinder;
    SensorService.MyBinder sensorbinder;
    WriteDataUtil writeDataUtil;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        audioRecorder = new AudioRecordFunc();
        maudioRecorder = new AudioRecorder();
        writeDataUtil=new WriteDataUtil();

    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.sleepfragment, container, false);

        Intent intent1 = new Intent(getActivity(), AudioRecorderService.class);
        AudioServiceConn audioServiceConn = new AudioServiceConn();
        getActivity().bindService(intent1, audioServiceConn, BIND_AUTO_CREATE);

        Intent intent2 = new Intent(getActivity(), SensorService.class);
        SensorServiceConn sensorServiceConn = new SensorServiceConn();
        getActivity().bindService(intent2, sensorServiceConn, BIND_AUTO_CREATE);

        sleep = (Button) view.findViewById(R.id.sleep);
        sleep.setOnClickListener(this);
        getup = (Button) view.findViewById(R.id.getup);
        getup.setOnClickListener(this);
        getup.setClickable(false);
        getup.setTextColor(ContextCompat.getColor(getActivity(), R.color.gray));
        imageView = (ImageView) view.findViewById(R.id.iv_clock);
        setFlickerAnimation(imageView);
        imageView.setOnClickListener(this);


        return view;
    }



    //实现图片闪烁效果
    private void setFlickerAnimation(ImageView iv_chat_head) {
        final Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
        animation.setDuration(500); // duration - half a second
        animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
        animation.setRepeatMode(Animation.REVERSE); //
        iv_chat_head.setAnimation(animation);
    }



    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sleep:
                //bindservice 只会启动 onbind一次

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        Message message = new Message();
                        message.what = 1;
                        myhandle.sendMessage(message);

                    }
                }).start();


                //    startTime();
                break;
            case R.id.getup:
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        Message message = new Message();
                        message.what = 2;
                        myhandle.sendMessage(message);

                    }
                }).start();
                //   stopTime();
                break;
            case R.id.iv_clock:
//                Intent intent=new Intent(getActivity(), AlarmActivity.class);
//                startActivity(intent);
            default:
                break;
        }

    }

    private class SensorServiceConn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            sensorbinder = (SensorService.MyBinder) iBinder;
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
            audiobinder = (AudioRecorderService.MyBinder) iBinder;
            AudioRecorderService audioRecorderService = audiobinder.getservice();
            audioRecorderService.setCallback(new AudioRecorderService.Callback() {
                @Override
                public void onDataChange(String data) {
                    //记录
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    //TODO 改用bindservice启动，binder中内调停止service
    @SuppressLint("HandlerLeak")
    Handler myhandle = new Handler() {

        public void handleMessage(Message msg) {

            if (1 == msg.what) {
                startTime();
                sleep.setClickable(false);
                sleep.setTextColor(ContextCompat.getColor(getActivity(), R.color.gray));
                writeDataUtil.oncreate();
                //   test.onCreate(getContext());

                //bindservice 是异步操作，不能马上调用service操作
                //      for(int i=10000000;i>0;i--){}

                //getActivity().startService((new Intent(getActivity() , SensorService.class)));
               sensorbinder.startRecord();
                audiobinder.startRecord();
                //SaudioRecorder.startRecord();

                // maudioRecorder.getNoiseLevel();
                getup.setClickable(true);
                getup.setTextColor(ContextCompat.getColor(getActivity(), R.color.sleep));
            }

            if (2 == msg.what) {
                stopTime();
                sleep.setClickable(true);
                sleep.setTextColor(ContextCompat.getColor(getActivity(), R.color.sleep));
                //stoprecord
               //  getActivity().unbindService(sensorServiceConn);
                sensorbinder.stopitself();
                writeDataUtil.onpause();
            //    audiobinder.stopRecord();
                // getActivity().stopService(intent);
//                audioRecorder.stopRecord();
                //   test.stop();
                //      getActivity().stopService(intent2);
                getup.setClickable(false);
                getup.setTextColor(ContextCompat.getColor(getActivity(), R.color.gray));
            }
        }
    };


    /**
     * 开始
     */
    public void startTime() {


        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {

                i++;
                //异常，在Android中不允许Activity里新启动的线程访问该Activity里的UI组件，这样会导致新启动的线程无法改变UI组件的属性值。
//                Toast.makeText(getActivity(), "晚安", Toast.LENGTH_SHORT).show();
            }
        };
        // 延迟0秒，周期60豪秒
        timer.schedule(timerTask, 0, 1000);
        Toast.makeText(getActivity(), "晚安", Toast.LENGTH_SHORT).show();
        //旧的getcolor（）版本已经过时，

//                 sleep.setEnabled(false);
    }

    /**
     * 停止
     */
    private void stopTime() {
        if (timer != null) {
            Toast.makeText(getActivity(), "你睡了 " + i / 3600 + "小时" + i / 60 + "分" + i + "秒", Toast.LENGTH_SHORT).show();

            timer.cancel();
            i = 0;
        } else {
            //这里应该单独做一个界面，显示睡觉时间
            Toast.makeText(getActivity(), "你睡了 " + i / 3600 + "小时" + i / 60 + "分" + i + "秒", Toast.LENGTH_SHORT).show();

        }
    }

}
