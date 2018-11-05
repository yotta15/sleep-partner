package com.example.gzy.test3.presenter;

public interface ILoginPresenter {
    void changePwdOpenOrClose(boolean flag);
    void clearText();
    void doLogin(String name,String passwd);
    void doRegister();
}
