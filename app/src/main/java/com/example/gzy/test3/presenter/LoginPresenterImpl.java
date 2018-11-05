package com.example.gzy.test3.presenter;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.example.gzy.test3.R;
import com.example.gzy.test3.activity.ILoginView;
import com.example.gzy.test3.model.UserModel;




import static android.content.Context.MODE_PRIVATE;

public class LoginPresenterImpl implements ILoginPresenter {
    ILoginView iLoginView;
    UserModel user;
    Handler handler;
    public   LoginPresenterImpl(ILoginView iLoginView){
        this.iLoginView = iLoginView;
        initUser();
        handler = new Handler(Looper.getMainLooper());
    }
    private void initUser(){
        user = new UserModel("mvp","mvp");
    }

    @Override
    public void changePwdOpenOrClose(boolean flag) {

    }

    @Override
    public void clearText() {

    }

    @Override
    public void doLogin(String name, String passwd) {
        Boolean isLoginSuccess = true;
        final int code = user.checkUserValidity(name,passwd);
        if (code!=0) isLoginSuccess = false;
        final Boolean result = isLoginSuccess;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //回调
                iLoginView.onLoginResult(result, code);
            }
        }, 3000);

    }


    @Override
    public void doRegister() {
//        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
//        Toast.makeText(LoginActivity.this, "注册", Toast.LENGTH_SHORT).show();

    }
}
