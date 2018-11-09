package com.example.gzy.test3.presenter;

import android.widget.Toast;

import com.example.gzy.test3.model.UserModel;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

import static cn.bmob.v3.Bmob.getApplicationContext;

public class RegisterPresenterImpl implements IRegisterPresenter {
    public  void initUser(String phoneNum,String passwd){
        UserModel user = new UserModel();

        user.setPhone(phoneNum);
        user.setPasswd(passwd);

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
    public void doRegister() {

    }
}
