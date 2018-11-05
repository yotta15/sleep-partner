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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Bundle;

import com.example.gzy.test3.R;
import com.example.gzy.test3.presenter.LoginPresenterImpl;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, ILoginView {

    private EditText username, password;
    private Button bt_username_clear;
    private Button bt_pwd_clear;
    private Button forgive_pwd;
    private Button bt_pwd_eye;
    private Button login;
    private Button register;
    private boolean isOpen = false;
    private LoginPresenterImpl loginPresenter;
//    SharedPreferences sharedPreferences;
//    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = (EditText) findViewById(R.id.username);
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String user = username.getText().toString().trim();
                if ("".equals(user)) {
                    bt_username_clear.setVisibility(View.INVISIBLE);
                } else {
                    bt_username_clear.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {}
        });
        password = (EditText) findViewById(R.id.password);
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String pwd = password.getText().toString().trim();
                if ("".equals(pwd)) {
                    bt_pwd_clear.setVisibility(View.INVISIBLE);
                } else {
                    bt_pwd_clear.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable s) {
                if (!username.getText().toString().equals("")) {
                    login.setEnabled(true);
                    login.setVisibility(View.VISIBLE);
                }
            }
        });

        bt_username_clear = (Button) findViewById(R.id.bt_username_clear);
        bt_username_clear.setOnClickListener(this);
        bt_pwd_clear = (Button) findViewById(R.id.bt_pwd_clear);
        bt_pwd_clear.setOnClickListener(this);
        bt_pwd_eye = (Button) findViewById(R.id.bt_pwd_eye);
        bt_pwd_eye.setOnClickListener(this);
        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(this);
        register = (Button) findViewById(R.id.register);
        register.setOnClickListener(this);
        forgive_pwd = (Button) findViewById(R.id.forgive_pwd);
        forgive_pwd.setOnClickListener(this);

        loginPresenter = new LoginPresenterImpl(this);
        //获取只能被本应用程序读写的SharedPreferences
//        sharedPreferences = getSharedPreferences("sleep_partner", MODE_PRIVATE);
//        editor = sharedPreferences.edit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_username_clear:
                username.setText("");
                break;
            case R.id.bt_pwd_clear:
                password.setText("");
                break;
            case R.id.bt_pwd_eye:
                if (isOpen) {
                    isOpen = false;
                } else {
                    isOpen = true;
                }
                changePwdOpenOrClose(isOpen);
                break;
            case R.id.login:
                loginPresenter.doLogin(username.getText().toString().trim(), password.getText().toString().trim());
//                if (username.getText().toString().trim().equals(account)
//                        && password.getText().toString().trim().equals(pwd)) {
//
//                    Intent intent = new Intent(LoginActivity.this, ThirdActivity.class);
//                    startActivity(intent);
//
//                } else {//用户名huo密码错误
//
//                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
//                    builder.setTitle("提示");
//                    builder.setMessage("用户名或密码错误");
//                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            dialogInterface.dismiss();
//
//                        }
//                    });
//                    builder.create().show();
//                }
                break;
            case R.id.register:
// 注册按钮
                //  loginPresenter.doRegister();
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                Toast.makeText(LoginActivity.this, "注册", Toast.LENGTH_SHORT).show();
                break;
            case R.id.forgive_pwd:
// 忘记密码按钮，携带账户号
                Toast.makeText(LoginActivity.this, "忘记密码", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    @Override
    /**
     * 密码可见与不可见的切换
     * @param flagc
     */
    public void changePwdOpenOrClose(boolean flag) {
        if (flag) {
            bt_pwd_eye.setBackgroundResource(R.drawable.password_open);
            password.setTransformationMethod(HideReturnsTransformationMethod
                    .getInstance());
        } else {
            bt_pwd_eye.setBackgroundResource(R.drawable.password_close);
            password.setTransformationMethod(PasswordTransformationMethod
                    .getInstance());
        }
    }

    @Override
    public void clearText() {

    }


    @Override
    public void onLoginResult(Boolean result, int code) {
        if (result) {
            Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, ThirdActivity.class));
        } else
            Toast.makeText(this, "Login Fail, code = " + code, Toast.LENGTH_SHORT).show();

    }

}
