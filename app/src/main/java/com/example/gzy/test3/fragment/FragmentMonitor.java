package com.example.gzy.test3.fragment;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
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
import com.example.gzy.test3.activity.AlarmActivity;
import com.example.gzy.test3.service.AudioRecordFunc;
import com.example.gzy.test3.service.AudioRecorder;
import com.example.gzy.test3.service.AudioRecorderService;
import com.example.gzy.test3.service.SensorService;

import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.BIND_AUTO_CREATE;


public class  FragmentMonitor extends Fragment implements View.OnClickListener {
    private Timer timer;
    private TimerTask timerTask;
    private View view;
     private CardView mcardview;
    private Button sleep, getup;
    private int i = 0;
    private AudioRecordFunc audioRecorder;
    private AudioRecorder maudioRecorder;
    private ImageView imageView;
    AudioRecorderService.MyBinder mybinder;



    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.sleepfragment, container, false);
        Intent intent=new Intent(getActivity(), AudioRecorderService.class);
        MyServiceConn myServiceConn=new MyServiceConn();
        getActivity().bindService(intent,myServiceConn, BIND_AUTO_CREATE);

        sleep = (Button) view.findViewById(R.id.sleep);
        sleep.setOnClickListener(this);
        getup = (Button) view.findViewById(R.id.getup);
        getup.setOnClickListener(this);
        getup.setClickable(false);
        getup.setTextColor(ContextCompat.getColor(getActivity(), R.color.gray));
        imageView=(ImageView)view.findViewById(R.id.iv_clock);
        setFlickerAnimation(imageView);
        imageView.setOnClickListener(this);
//        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));  //image的布局方式
////        imageView.setImageResource(R.drawable.alarm);  //设置imageview呈现的图片
////        view.addView(imageView);
        return view;
     //   ViewGroup group = (ViewGroup) view.findViewById(R.id.viewGroup); //获取原来的布局容器
     //   ImageView imageView = new ImageView(this);  //创建imageview
        //添加到布局容器中，显示图片。

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

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        audioRecorder = new AudioRecordFunc();
        maudioRecorder = new AudioRecorder();
//        initview();
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

    private class MyServiceConn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            Log.i("connected","succeed");
            //iBinder为服务里面onBind()方法返回的对象，所以可以强转为自定义InterfaceMyBinder类型
            mybinder = (AudioRecorderService.MyBinder) iBinder;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }
    //TODO 改用bindservice启动，binder中内调停止service
    @SuppressLint("HandlerLeak")
    Handler myhandle = new Handler() {

        public void handleMessage(Message msg) {
          //  Intent intent = new Intent(getActivity(), AudioRecorderService.class);
            Intent intent2 = new Intent(getActivity(), SensorService.class);
            //开启服务

            if (1 == msg.what) {
                startTime();
                sleep.setClickable(false);
                sleep.setTextColor(ContextCompat.getColor(getActivity(), R.color.gray));

             //   test.onCreate(getContext());

                //bindservice 是异步操作，不能马上调用service操作
      //      for(int i=10000000;i>0;i--){}
            mybinder.startRecord();



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

                mybinder.stopRecord();
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

    public Handler getMyhandle() {
        return myhandle;
    }

    public void setMyhandle(Handler myhandle) {
        this.myhandle = myhandle;
    }
}
