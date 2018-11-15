package com.example.gzy.test3.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cn.bmob.v3.BmobUser;


/**
 * Created by gzy on 2018/3/27.
 */

public class HomeActivity extends AppCompatActivity {
    Intent intent;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BmobUser bmobUser = BmobUser.getCurrentUser();
        if (bmobUser != null) {
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            // 允许用户使用应用
        } else {
            intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            //缓存用户对象为空时， 可打开用户登入界面…
        }
    }

}
