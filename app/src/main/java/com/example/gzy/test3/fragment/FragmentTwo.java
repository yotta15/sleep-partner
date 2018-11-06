package com.example.gzy.test3.fragment;

import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;


import com.example.gzy.test3.R;

import java.util.HashMap;

public class FragmentTwo extends Fragment implements View.OnClickListener {
    private GridLayout mGridLayout;
    private int columnCount; //列数
    private int screenWidth; //屏幕宽度
    private Button button,image1,image2,image3,image4,image5,image6,image7;
    private SoundPool soundPool;
    private HashMap<Integer, Integer> soundmap = new HashMap<>();

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.secondfragment, container, false);
        mGridLayout = (GridLayout) view.findViewById(R.id.gridlayout);
        columnCount = mGridLayout.getColumnCount();
        screenWidth = getActivity().getResources().getDisplayMetrics().widthPixels;
//        Log.d(TAG, "column:" + columnCount + ";  screenwidth:" + screenWidth);
        //拿到GridLayout中的列数/屏幕的宽度,然后通过GridLayout.getChildCount()去获取GridLayout中的元素个数,然后循环设置宽度就可以做到平分效果
        for (int i = 0; i < mGridLayout.getChildCount(); i++) {

            button = (Button) mGridLayout.getChildAt(i);
            button.setWidth(screenWidth / columnCount);
//            button.setOnClickListener(this);
//            Log.d(TAG, Integer.toString(button.getWidth()));
        }
        AudioAttributes attr = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();
        soundPool = new SoundPool.Builder()
                .setAudioAttributes(attr)
                .setMaxStreams(1)
                .build();
        //设置音效池的属性，最多容纳1个音频流
        soundmap.put(1,soundPool.load(getActivity(),R.raw.bgm001,1));
        soundmap.put(2,soundPool.load(getActivity(),R.raw.scene_jingxin,1));
        soundmap.put(3,soundPool.load(getActivity(),R.raw.bgm003,1));
        soundmap.put(4,soundPool.load(getActivity(),R.raw.bgm005,1));
        soundmap.put(5,soundPool.load(getActivity(),R.raw.bgm006,1));
        soundmap.put(6,soundPool.load(getActivity(),R.raw.bgm008,1));
        soundmap.put(7,soundPool.load(getActivity(),R.raw.heart_sound,1));
        image1=(Button) view.findViewById(R.id.image1);
        image1.setOnClickListener(this);
        image2=(Button) view.findViewById(R.id.image2);
        image2.setOnClickListener(this);
        image3=(Button) view.findViewById(R.id.image3);
        image3.setOnClickListener(this);
        image4=(Button) view.findViewById(R.id.image4);
        image4.setOnClickListener(this);
        image5=(Button) view.findViewById(R.id.image5);
        image5.setOnClickListener(this);
        image6=(Button) view.findViewById(R.id.image6);
        image6.setOnClickListener(this);
        image7=(Button) view.findViewById(R.id.image7);
        image7.setOnClickListener(this);

        return view;

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onClick(View view) {
      switch (view.getId()){
          case R.id.image1:
              soundPool.play(soundmap.get(1),1,1,10,-1,1);
              break;
          case R.id.image2:
              soundPool.play(soundmap.get(2),1,1,10,0,1);
              break;
          case R.id.image3:
              soundPool.play(soundmap.get(3),1,1,10,-1,1);
              break;
          case R.id.image4:
              soundPool.play(soundmap.get(4),1,1,10,-1,1);
              break;
          case R.id.image5:
              soundPool.play(soundmap.get(5),1,1,10,-1,1);
              break;
          case R.id.image6:
              soundPool.play(soundmap.get(6),1,1,10,-1,1);
              break;
          case R.id.image7:
              soundPool.play(soundmap.get(7),1,1,10,-1,1);
              break;
           default:
               break;


      }
    }
}
