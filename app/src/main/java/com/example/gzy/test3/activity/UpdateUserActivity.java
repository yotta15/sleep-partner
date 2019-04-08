package com.example.gzy.test3.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gzy.test3.R;
import com.example.gzy.test3.fragment.IRegisterOneView;
import com.example.gzy.test3.model.UserModel;
import com.example.gzy.test3.presenter.LoginPresenterImpl;
import com.example.gzy.test3.presenter.RegisterPresenterImpl;
import com.example.gzy.test3.presenter.UpdateUserPresenter;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import static cn.bmob.v3.Bmob.getApplicationContext;

/**
 * created by gzy on 2018/11/15.
 * Describle;
 * 用户输入他们的电子邮件，请求重置自己的密码。
 * Bmob向他们的邮箱发送一封包含特殊的密码重置链接的电子邮件。
 * 用户根据向导点击重置密码连接，打开一个特殊的Bmob页面，根据提示他们可以输入一个新的密码。
 * 用户的密码已被重置为新输入的密码。
 */
public class UpdateUserActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mEmail;
    private Button btnSendEmail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_pwd);
        mEmail = (EditText) findViewById(R.id.email);
        btnSendEmail = (Button) findViewById(R.id.sendEmail);
        btnSendEmail.setOnClickListener(this);
        //第一：默认初始化
        Bmob.initialize(this, "5a5dcb5264e14c4fa9886e8511707ac0");


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sendEmail:
                if ("".equals(mEmail.getText().toString())) {
                    Toast.makeText(UpdateUserActivity.this, "请填入邮箱", Toast.LENGTH_LONG).show();

                } else {
                    if (isEmail(mEmail.getText().toString())) {
                        UpdatePassword(mEmail.getText().toString().trim());
                    } else {
                        Toast.makeText(UpdateUserActivity.this, "请输入正确邮箱", Toast.LENGTH_LONG).show();
                    }
                }
                break;

        }
    }

    public static boolean isEmail(String strEmail) {
        String strPattern = "^[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";
        if (TextUtils.isEmpty(strPattern)) {
            return false;
        } else {
            return strEmail.matches(strPattern);
        }
    }

    public void UpdatePassword(String email) {
        BmobUser.resetPasswordByEmail(email, new UpdateListener() {

            @Override
            public void done(BmobException e) {
                if (e == null) {
                    //？未收到邮件  跳转到登入页
                    Toast.makeText(UpdateUserActivity.this, "success", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(UpdateUserActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
