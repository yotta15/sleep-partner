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


import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
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
    public void doLogin(String phone, String passwd) {

        BmobQuery<BmobUser> query = new BmobQuery<BmobUser>();
        query.addWhereEqualTo("phone", phone);
        query.addWhereEqualTo("password",passwd);
        query.findObjects(new FindListener<BmobUser>() {
            @Override
            public void done(List<BmobUser> object, BmobException e) {
                if(e==null){
                    iLoginView.onLoginResult(true);


                }else{
                    iLoginView.onLoginResult(false);


                }
            }
        });



    }



}
