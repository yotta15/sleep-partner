package com.example.gzy.test3.presenter;

import android.widget.Toast;

import com.example.gzy.test3.fragment.IRegisterOneView;
import com.example.gzy.test3.model.UserModel;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import static cn.bmob.v3.Bmob.getApplicationContext;

public class RegisterPresenterImpl implements IRegisterPresenter {
    IRegisterOneView iRegisterOneView;

    public RegisterPresenterImpl(IRegisterOneView iRegisterOneView ){
        this.iRegisterOneView=iRegisterOneView;
    }
    boolean flag=false;

    public  void  initUser(final String name, String phoneNum, final String passwd){

        BmobUser user = new BmobUser();
        user.setUsername(name);
        user.setMobilePhoneNumber(phoneNum);
        user.setPassword(passwd);
        user.signUp(new SaveListener<UserModel>() {
            @Override
            public void done(UserModel s, BmobException e) {
                if (e == null) {
                    iRegisterOneView.OnSignUpResult(true,name,passwd);
                    Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_LONG).show();
                } else {
                    iRegisterOneView.OnSignUpResult(false,name,passwd);
                    Toast.makeText(getApplicationContext(), "手机号不能重复注册", Toast.LENGTH_LONG).show();
                }
            }
            });
    }
   public void LoginUser(String name,String passwd){

               BmobUser userlogin=new BmobUser();
               userlogin.setUsername(name);
               userlogin.setPassword(passwd);
               userlogin.login(new SaveListener<BmobUser>() {
                   @Override
                   public void done(BmobUser bmobUser, BmobException e) {
                       if(e==null){
                           Toast.makeText(getApplicationContext(),"success",Toast.LENGTH_LONG).show();
                       }else{
                           Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                       }
                   }
               });
           }





    public void UpdateUser() {
        BmobUser newUser = new BmobUser();
        newUser.setEmail("xxx@163.com");
        BmobUser bmobUser = BmobUser.getCurrentUser();
        newUser.update(bmobUser.getObjectId(),new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    Toast.makeText(getApplicationContext(),"success",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //内部方法不能用于返回值，
    public  void   queryUser(String column,String value){

        BmobQuery<BmobUser> query = new BmobQuery<BmobUser>();
        query.addWhereEqualTo(column, value);
        query.findObjects(new FindListener<BmobUser>() {
            @Override
            public void done(List<BmobUser> object, BmobException e) {
                if(e==null){
                    Toast.makeText(getApplicationContext(),"success",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }



}
