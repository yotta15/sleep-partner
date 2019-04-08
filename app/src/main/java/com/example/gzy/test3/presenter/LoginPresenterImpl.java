package com.example.gzy.test3.presenter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.example.gzy.test3.R;
import com.example.gzy.test3.activity.ILoginView;
import com.example.gzy.test3.model.UserModel;


import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import static android.content.Context.MODE_PRIVATE;
import static cn.bmob.v3.Bmob.getApplicationContext;

public class LoginPresenterImpl implements ILoginPresenter {
    ILoginView iLoginView;
    UserModel user;
    Handler handler;
    public   LoginPresenterImpl(ILoginView iLoginView){
        this.iLoginView = iLoginView;
        //initUser();
        handler = new Handler(Looper.getMainLooper());
    }
    private void initUser(){
        UserModel user = new UserModel();

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
    public void doLogin(String username, String passwd) {
        BmobUser.loginByAccount(username, passwd, new LogInListener<BmobUser>() {
            @Override
            public void done(BmobUser user, BmobException e) {
                if (e == null) {
                    iLoginView.onLoginResult(true);
                } else {
                    iLoginView.onLoginResult(false);
                    Toast.makeText(getApplicationContext(),"用户名或者密码不正确",Toast.LENGTH_LONG).show();
                    Log.i("loginError",e.getMessage());
                }
            }
        });

    }



}
