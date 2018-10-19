package com.example.gzy.test3;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;


/**
 * Created by gzy on 2018/3/27.
 */

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Intent intent;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        sharedPreferences = getSharedPreferences("sleep_partner", MODE_PRIVATE);
        editor = sharedPreferences.edit();
       if(null == sharedPreferences.getString("account",null) ) {
           setContentView(R.layout.homepage);
           Button button1 = (Button) findViewById(R.id.button1);
           button1.setOnClickListener(this);
           Button button2 = (Button) findViewById(R.id.button2);
           button2.setOnClickListener(this);
       }else {
           intent=new Intent(this,ThirdActivity.class);
           startActivity(intent);
       }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button1:
                intent=new Intent(this,RegisterActivity.class);
                startActivity(intent);
                //注册
                break;
            case R.id.button2:
                intent=new Intent(this,LoginActivity.class);
                startActivity(intent);
                //登入
                break;
        }
    }
}
