package com.example.gzy.test3.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.gzy.test3.R;
import com.example.gzy.test3.activity.ContentActivity;
import com.example.gzy.test3.activity.MainActivity;

/**
 * 此页面用于注册 地区、个性化设置
 */

public class RegisterFragmentThree extends Fragment {
    private  Button btnSkip;
    public static RegisterFragmentThree newInstance(int position, String name) {

        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putString("name", name);
        RegisterFragmentThree fragment = new RegisterFragmentThree();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_start, container, false);
        final int position = getArguments().getInt("position");
        String name = getArguments().getString("name");
       // View view = inflater.inflate(R.layout.register_phone, container, false);
        btnSkip=(Button)view.findViewById(R.id.btnSkip);
//        Button btn = new Button(container.getContext(), null);
//        btn.setGravity(Gravity.CENTER);
//        btn.setText("当前状态:" + name);

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),ContentActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
