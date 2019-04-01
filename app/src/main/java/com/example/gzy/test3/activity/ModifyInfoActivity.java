package com.example.gzy.test3.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gzy.test3.R;
import com.example.gzy.test3.presenter.UserPresenterImpl;
import com.githang.statusbar.StatusBarCompat;

import cn.bmob.v3.Bmob;

/**
 * created by gzy on 2019/3/30.
 * Describle;
 * TODO 页面修改
 * 可以修改手机号、密码、年龄、地区、性别、昵称、身高、体重、邮箱
 */
public class ModifyInfoActivity extends AppCompatActivity implements View.OnClickListener{
    TextView mTVage, mTVsex, mTVweight, mTVheight, mTVemail;
    UserPresenterImpl userPresenter;

    //ImageView
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modifyuserlayout);
        Bmob.initialize(this, "5a5dcb5264e14c4fa9886e8511707ac0");
        userPresenter=new UserPresenterImpl();
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.white));
        mTVage=(TextView) findViewById(R.id.per_tv_age);
        mTVage.setText(String.valueOf(userPresenter.<Integer>queryData("age")));
        mTVsex=(TextView) findViewById(R.id.per_tv_sex);
        mTVsex.setText(userPresenter.<String>queryData("sex"));
        mTVweight=(TextView) findViewById(R.id.per_tv_weight);
        mTVweight.setText(String.valueOf(userPresenter.<Integer>queryData("weight")));
        mTVheight=(TextView) findViewById(R.id.per_tv_height);
        mTVheight.setText(String.valueOf(userPresenter.<Integer>queryData("height")));
        mTVemail=(TextView) findViewById(R.id.per_tv_email);
        mTVemail.setText(userPresenter.<String>queryData("email"));
    }

    @Override
    public void onClick(View view) {

    }
}
