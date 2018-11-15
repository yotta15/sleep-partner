package com.example.gzy.test3.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.gzy.test3.R;
import com.example.gzy.test3.activity.TestActivity;
import com.example.gzy.test3.activity.TestFragment;

public class    RegisterFragmentTwo extends Fragment {
    public static RegisterFragmentTwo newInstance(int position, String name) {

        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putString("name", name);
        RegisterFragmentTwo fragment = new RegisterFragmentTwo();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final int position = getArguments().getInt("position");
        String name = getArguments().getString("name");
        View view = inflater.inflate(R.layout.register_info, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
