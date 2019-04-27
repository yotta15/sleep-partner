package com.example.gzy.test3.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;


import com.example.gzy.test3.R;
import com.example.gzy.test3.activity.AlarmActivity;
import com.example.gzy.test3.activity.SleepActivity;


public class FragmentMonitor extends Fragment implements View.OnClickListener {
    private View view;
    private Button sleep;

    private ImageView imageView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.sleepfragment, container, false);
        sleep = (Button) view.findViewById(R.id.sleep);
        sleep.setOnClickListener(this);
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
                Intent intent = new Intent(getActivity(), SleepActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_clock:
                Intent intent2=new Intent(getActivity(), AlarmActivity.class);
                startActivity(intent2);
            default:
                break;
        }

    }

}
