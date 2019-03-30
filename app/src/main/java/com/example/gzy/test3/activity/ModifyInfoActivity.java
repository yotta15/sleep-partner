package com.example.gzy.test3.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.gzy.test3.R;
import com.githang.statusbar.StatusBarCompat;

/**
 * created by gzy on 2019/3/30.
 * Describle;
 */
public class ModifyInfoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modifyuserlayout);
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.white));
    }
}
