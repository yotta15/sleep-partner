package com.example.gzy.test3.fragment;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.gzy.test3.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by w on 2018/11/7.
 */
public class MusicFragment extends Fragment {
    private ListView lv;
    private SimpleAdapter adapter;
    private List<Map<String,Object>> list;
    private LayoutInflater inflater;
    private SoundPool soundPool;
    private HashMap<Integer, Integer> soundmap = new HashMap<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_music,container,false);
        return view;
    }
    public void music_play(){
        AudioAttributes attr = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();
        soundPool = new SoundPool.Builder()
                .setAudioAttributes(attr)
                .setMaxStreams(1)
                .build();
        //设置音效池的属性，最多容纳1个音频流
        soundmap.put(1,soundPool.load(getActivity(),R.raw.music001,1));
        soundmap.put(2,soundPool.load(getActivity(),R.raw.music002,1));
        soundmap.put(3,soundPool.load(getActivity(),R.raw.music003,1));
        soundmap.put(4,soundPool.load(getActivity(),R.raw.music004,1));
        soundmap.put(5,soundPool.load(getActivity(),R.raw.music005,1));
        soundmap.put(6, soundPool.load(getActivity(), R.raw.music006, 1));
        soundmap.put(7,soundPool.load(getActivity(),R.raw.music007,1));
        soundmap.put(8,soundPool.load(getActivity(),R.raw.music008,1));
        soundmap.put(9,soundPool.load(getActivity(),R.raw.music009,1));
        soundmap.put(10,soundPool.load(getActivity(),R.raw.music010,1));
        soundmap.put(11,soundPool.load(getActivity(),R.raw.music011,1));
        soundmap.put(12,soundPool.load(getActivity(),R.raw.music012,1));
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lv=(ListView)getView().findViewById(R.id.listView);
        if(lv==null){
            Log.i("-----------debug", "null");
        }
        music_play();
        setAdapter();

    }
    private void setAdapter(){
        MySimpleAdapter mySimpleAdapter=new MySimpleAdapter(
                getActivity(),
                getData(),
                R.layout.music_listview,
                new String[]{"name","author","imgbutton"},
                new int[]{R.id.music_name,R.id.music_author,R.id.music_play}
        );
        lv.setAdapter(mySimpleAdapter);//配置适配器，获取对应item中的id
    }

    /************************************8
     * 自定义适配器
     */
    class MySimpleAdapter extends SimpleAdapter{
        public MySimpleAdapter(Context context, List<? extends Map<String, ?>> data,
                               int resource, String[] from, int[] to) {
            super(context, data, resource, from, to);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final View view=super.getView(position, convertView, parent);
            ImageButton BtnPlay=(ImageButton)view.findViewById(R.id.music_play);
            BtnPlay.setTag(this.getItem(position));//获取listView中的当前行
            BtnPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    soundPool.play(soundmap.get(position+1), 1, 1, 10, -1, 1);;
                }
            });
            return view;
        }
    }
    //数据的获取
    private List<? extends Map<String,?>> getData(){
        list=new ArrayList<Map<String,Object>>();
        //将需要的值传入map中
        Map<String,Object> map;
        String musicName[]=getResources().getStringArray(R.array.musicName);
        String musicAuthor[]=getResources().getStringArray(R.array.musicAuthor);
        for(int i=0;i<12;i++){
            map=new HashMap<String, Object>();
            map.put("name",musicName[i]);
            map.put("author",musicAuthor[i]);
            map.put("imgbutton",R.drawable.play);
            list.add(map);
        }

        return list;
    }

}
