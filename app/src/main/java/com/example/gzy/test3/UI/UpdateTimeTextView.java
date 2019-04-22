package com.example.gzy.test3.UI;

/**
 * created by gzy on 2019/4/22.
 * Describle;
 */

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UpdateTimeTextView extends android.support.v7.widget.AppCompatTextView {

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            UpdateTimeTextView.this.setText((String) msg.obj);
        }
    };
    private String DEFAULT_TIME_FORMAT = "HH:mm";

    public UpdateTimeTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
//                    SimpleDateFormat sdf=new SimpleDateFormat(DEFAULT_TIME_FORMAT);
//                    String str=sdf.format(new Date());
                    SimpleDateFormat dateFormatter = new SimpleDateFormat(DEFAULT_TIME_FORMAT);
                    String time = dateFormatter.format(Calendar.getInstance().getTime());
                    handler.sendMessage(handler.obtainMessage(100, time));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    //根据毫秒时间获取格式化的提示
    private String convertTimeToFormat(long timeMills) {
        long curTime = Calendar.getInstance().getTimeInMillis();
        long time = (curTime - timeMills) / (long) 1000;//已经将单位转换成秒

        if (time > 0 && time < 60) {
            return "刚刚";
        } else if (time >= 60 && time < 3600) {
            return time / 60 + "分钟前";
        } else if (time >= 3600 && time < 3600 * 24) {
            return time / 3600 + "小时前";
        } else if (time >= 3600 * 24 && time < 3600 * 24 * 30) {
            return time / 3600 / 24 + "天前";
        } else if (time >= 3600 * 24 * 30 && time < 3600 * 24 * 30 * 12) {
            return time / 3600 / 24 / 30 + "个月前";
        } else if (time >= 3600 * 24 * 30 * 12) {
            return time / 3600 / 24 / 30 / 12 + "年前";
        } else {
            return "刚刚";
        }
    }
}
