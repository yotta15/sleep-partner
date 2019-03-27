package com.example.gzy.test3.presenter;

import com.example.gzy.test3.model.UserModel;

import cn.bmob.v3.BmobUser;

/**
 * created by gzy on 2019/3/27.
 * Describle;
 */
public class UserPresenterImpl  implements  IUserPresenter{
    public boolean isLogin(){
        UserModel user = BmobUser.getCurrentUser(UserModel.class);
        if(user!=null){
            return true;
        }else {
            return  false;
        }

    }
}
