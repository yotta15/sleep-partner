package com.example.gzy.test3.activity;


import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;

import com.example.gzy.test3.R;
import com.example.gzy.test3.presenter.LoginPresenterImpl;

import cn.bmob.v3.Bmob;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener, ILoginView {

    private EditText musername, mpassword;
    private ImageButton bt_username_clear,bt_pwd_clear,bt_pwd_eye;
    private Button login;
     private TextView forgive_pwd,register;
    private boolean isOpen = false;
    private LoginPresenterImpl loginPresenter;
//    SharedPreferences sharedPreferences;
//    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        //第一：默认初始化
        Bmob.initialize(this, "5a5dcb5264e14c4fa9886e8511707ac0");
        musername = (EditText) findViewById(R.id.username);
        musername.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String user = musername.getText().toString().trim();
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
        mpassword = (EditText) findViewById(R.id.password);
        mpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String pwd = mpassword.getText().toString().trim();
                if ("".equals(pwd)) {
                    bt_pwd_eye.setVisibility(View.INVISIBLE);
                    bt_pwd_clear.setVisibility(View.INVISIBLE);
                } else {
                    bt_pwd_eye.setVisibility(View.VISIBLE);
                    bt_pwd_clear.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable s) {
                if (!musername.getText().toString().equals("")) {
                    login.setEnabled(true);
                    login.setVisibility(View.VISIBLE);
                }
            }
        });

        bt_username_clear = (ImageButton) findViewById(R.id.bt_username_clear);
        bt_username_clear.setOnClickListener(this);
        bt_pwd_clear = (ImageButton) findViewById(R.id.bt_pwd_clear);
        bt_pwd_clear.setOnClickListener(this);
        bt_pwd_eye = (ImageButton) findViewById(R.id.bt_pwd_eye);
        bt_pwd_eye.setOnClickListener(this);
        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(this);
        register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(this);
        forgive_pwd = (TextView) findViewById(R.id.forgive_pwd);
        forgive_pwd.setOnClickListener(this);

        loginPresenter = new LoginPresenterImpl(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_username_clear:
                musername.setText("");
                break;
            case R.id.bt_pwd_clear:
                mpassword.setText("");
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
                if(!musername.getText().toString().equals("")&&!mpassword.getText().equals("")){
                    loginPresenter.doLogin(musername.getText().toString().trim(), mpassword.getText().toString().trim());
                }
                break;
            case R.id.register:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
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
            bt_pwd_eye.setImageResource(R.drawable.pswd_open);
            mpassword.setTransformationMethod(HideReturnsTransformationMethod
                    .getInstance());
        } else {
            bt_pwd_eye.setImageResource(R.drawable.pswd_close);
            mpassword.setTransformationMethod(PasswordTransformationMethod
                    .getInstance());
        }
    }

    @Override
    public void clearText() {

    }


    @Override
    public void onLoginResult(Boolean result) {
        if (result) {
            Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
        } else{
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
            builder.setTitle("提示");
            builder.setMessage("用户名或密码错误");
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
//            Toast.makeText(this, "Login Fail, code = " + code, Toast.LENGTH_SHORT).show();
            });
        }

}
}
