package com.example.gzy.test3.presenter;

import android.content.Context;
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


import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

import static android.content.Context.MODE_PRIVATE;
import static cn.bmob.v3.Bmob.getApplicationContext;

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
        UserModel user = new UserModel();
        user.setName("root");
        user.setPhone("123456");
        user.setPasswd("123456");
        user.setAge("18");
        user.setSex("男");
        user.setArea("南昌市");
        user.setGrade("0");
        user.setQq("123");
        user.setWeight("100");
        user.setHeight("100");
        user.save(new SaveListener<String>() {
            @Override
            public void done(String objectId,BmobException e) {
                if(e==null){
                    Toast.makeText(getApplicationContext(),"success",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    @Override
    public void doLogin(String name, String passwd) {
        Boolean isLoginSuccess = true;
       final int code=0;
//       if(user!=null){
//
//       }
       // final int code = user.checkUserValidity(name,passwd);
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
