package com.example.gzy.test3.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


import com.example.gzy.test3.R;
import com.example.gzy.test3.service.AudioRecordFunc;
import com.example.gzy.test3.service.AudioRecorder;
import com.example.gzy.test3.service.RecordingService;
import com.example.gzy.test3.service.SensorService;

import java.util.Timer;
import java.util.TimerTask;


public class FragmentOne extends Fragment implements View.OnClickListener {
    private Timer timer;
    private TimerTask timerTask;
    private View view;
    private Button sleep, getup;
    private int i = 0;
    private AudioRecordFunc audioRecorder;
    private AudioRecorder maudioRecorder;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.firstfragment, container, false);
        sleep = (Button) view.findViewById(R.id.sleep);
        sleep.setOnClickListener(this);
        getup = (Button) view.findViewById(R.id.getup);
        getup.setOnClickListener(this);
        getup.setClickable(false);
        getup.setTextColor(ContextCompat.getColor(getActivity(), R.color.gray));
        return view;


    }

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        audioRecorder=new AudioRecordFunc();
        maudioRecorder=new AudioRecorder();
//        initview();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sleep:
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
            default:
                break;
        }

    }

    // public void  initview(){
//
//     new Thread() {
//         public void run () {
//             Looper.prepare();
    //TODO 改用bindservice启动，binder中内调停止service
    Handler myhandle = new Handler() {

        public void handleMessage(Message msg) {
            Intent intent = new Intent(getActivity(), RecordingService.class);
            Intent intent2=new Intent(getActivity(), SensorService.class);
            if (1 == msg.what) {
                startTime();
                sleep.setClickable(false);
                sleep.setTextColor(ContextCompat.getColor(getActivity(), R.color.gray));
                getActivity().startService(intent2);
                //SaudioRecorder.startRecord();

                // maudioRecorder.getNoiseLevel();
                getup.setClickable(true);
                getup.setTextColor(ContextCompat.getColor(getActivity(), R.color.sleep));
            }

            if (2 == msg.what) {
                stopTime();
                sleep.setClickable(true);
                sleep.setTextColor(ContextCompat.getColor(getActivity(), R.color.sleep));
              // getActivity().stopService(intent);
//                audioRecorder.stopRecord();
               // getActivity().stopService(intent2);
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

    public Handler getMyhandle() {
        return myhandle;
    }

    public void setMyhandle(Handler myhandle) {
        this.myhandle = myhandle;
    }
}
