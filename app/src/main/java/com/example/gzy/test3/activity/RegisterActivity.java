package com.example.gzy.test3.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.example.gzy.test3.R;
import com.example.gzy.test3.fragment.RegisterFragmentOne;
import com.example.gzy.test3.fragment.RegisterFragmentThree;
import com.example.gzy.test3.fragment.RegisterFragmentTwo;
import com.githang.statusbar.StatusBarCompat;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
public class RegisterActivity extends FragmentActivity
{
    List<Fragment> mLists = new ArrayList<>();
    private List<ProgressView.Model> mModels;
    private ProgressView mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.holo_blue_light));
        applyPermission();
        //第一：默认初始化
        Bmob.initialize(this, "5a5dcb5264e14c4fa9886e8511707ac0");

        mView = (ProgressView) findViewById(R.id.ffff);

        mModels = new ArrayList<>();

        mLists.add(RegisterFragmentOne.newInstance(0, "用户注册"));
        mLists.add(RegisterFragmentTwo.newInstance(1, "信息填写"));
        mLists.add(RegisterFragmentThree.newInstance(2, "最终完成"));

        mModels.add(new ProgressView.Model("用户注册", ProgressView.STARTING));
        mModels.add(new ProgressView.Model("信息填写", ProgressView.AFTER));
        mModels.add(new ProgressView.Model("最终完成", ProgressView.AFTER));

        mView.setData(mModels);
        switchFragment(null, mLists.get(0));


    }
    //动态申请权限,录音权限，读写权限
    public void applyPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.READ_PHONE_STATE};
            //验证是否许可权限
            for (String str : permissions) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                    return;
                }
            }
        }

    }
    public void switchFragment(Fragment from, Fragment to) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (from == null) {
            if (to != null) {
                transaction.replace(R.id.id_content, to);
                transaction.commit();
            }
        } else {
            if (!to.isAdded()) {
                transaction.hide(from).add(R.id.id_content, to).commit();
            } else {
                transaction.hide(from).show(to).commit();
            }

        }

    }

    public void setPosition(int position) {
        switchFragment(mLists.get(position), mLists.get(position + 1));
        mModels.get(position).state = ProgressView.BEFORE;
        mModels.get(position + 1).state = ProgressView.STARTING;
        mView.setData(mModels);
    }
}
