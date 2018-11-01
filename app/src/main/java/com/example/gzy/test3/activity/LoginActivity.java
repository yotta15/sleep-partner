package com.example.gzy.test3.activity;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Bundle;

import com.example.gzy.test3.R;

public class LoginActivity extends AppCompatActivity {

    private EditText username, password;
    private Button bt_username_clear;
    private Button bt_pwd_clear;
    private Button forgive_pwd;
    private Button bt_pwd_eye;
    private Button login;
    private Button register;
    private boolean isOpen = false;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获取只能被本应用程序读写的SharedPreferences
        sharedPreferences=getSharedPreferences("sleep_partner",MODE_PRIVATE);
        editor=sharedPreferences.edit();
        initView();
    }
    private void initView() {
        username = (EditText) findViewById(R.id.username);
// 监听文本框内容变化
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
// 获得文本框中的用户
                String user = username.getText().toString().trim();
                if ("".equals(user)) {
// 用户名为空,设置按钮不可见
                    bt_username_clear.setVisibility(View.INVISIBLE);
                } else {
// 用户名不为空，设置按钮可见
                    bt_username_clear.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        password = (EditText) findViewById(R.id.password);
// 监听文本框内容变化
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
// 获得文本框中的密码
                String pwd = password.getText().toString().trim();
                if ("".equals(pwd)) {
// 密码为空,设置按钮不可见
                    bt_pwd_clear.setVisibility(View.INVISIBLE);
                } else {
// 密码不为空，设置按钮可见
                    bt_pwd_clear.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(!username.getText().toString().equals("") ){
                    //登入按钮可见
                    login.setEnabled(true);
                    login.setVisibility(View.VISIBLE);
                }
            }
        });

        ButtonClick bt = new ButtonClick();//类封装
        bt_username_clear = (Button) findViewById(R.id.bt_username_clear);
        bt_username_clear.setOnClickListener(bt);
        bt_pwd_clear = (Button) findViewById(R.id.bt_pwd_clear);
        bt_pwd_clear.setOnClickListener(bt);

        bt_pwd_eye = (Button) findViewById(R.id.bt_pwd_eye);
        bt_pwd_eye.setOnClickListener(bt);
        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(bt);
        register = (Button) findViewById(R.id.register);
        register.setOnClickListener(bt);
        forgive_pwd = (Button) findViewById(R.id.forgive_pwd);
        forgive_pwd.setOnClickListener(bt);
    }
    class ButtonClick implements  OnClickListener

    {
        public void onClick (View v){
            switch (v.getId()) {
                case R.id.bt_username_clear:
// 清除登录名
                    username.setText("");
                    break;
                case R.id.bt_pwd_clear:
// 清除密码
                    password.setText("");
                    break;
                case R.id.bt_pwd_eye:
// 密码可见与不可见的切换
                    if (isOpen) {
                        isOpen = false;
                    } else {
                        isOpen = true;
                    }
// 默认isOpen是false,密码不可见
                    changePwdOpenOrClose(isOpen);
                    break;
                case R.id.login:


                    //获取sharedpreferences的account和password信息，默认返回null
                   String account=sharedPreferences.getString("account",null);
                   String pwd=sharedPreferences.getString("pwd",null);
                    Toast.makeText(getApplicationContext(), account+pwd,
                            Toast.LENGTH_SHORT).show();
                    // 获得文本框中的用户,密码进行比较
                    if(username.getText().toString().trim().equals(account)
                            && password.getText().toString().trim().equals(pwd)){

                        Intent intent =new Intent(LoginActivity.this,ThirdActivity.class);
                        startActivity(intent);

                    }else {//用户名huo密码错误

                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                            builder.setTitle("提示");
                            builder.setMessage("用户名或密码错误");
                            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();

                                }
                            });
                            builder.create().show();

                        }

// TODO 登录按钮
                    break;
                case R.id.register:
// 注册按钮
                    Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                    Toast.makeText(LoginActivity.this, "注册",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.forgive_pwd:
// 忘记密码按钮，携带账户号
                    Toast.makeText(LoginActivity.this, "忘记密码", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    }
    /**
     * 密码可见与不可见的切换
     *
     * @param flag
     */
    private void changePwdOpenOrClose(boolean flag) {
// 第一次过来是false，密码不可见
        if (flag) {
// 密码可见
            bt_pwd_eye.setBackgroundResource(R.drawable.password_open);
// 设置EditText的密码可见
            password.setTransformationMethod(HideReturnsTransformationMethod
                    .getInstance());
        } else {
// 密码不接见
            bt_pwd_eye.setBackgroundResource(R.drawable.password_close);
// 设置EditText的密码隐藏
            password.setTransformationMethod(PasswordTransformationMethod
                    .getInstance());
        }
    }

}
