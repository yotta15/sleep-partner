package com.example.gzy.test3.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gzy.test3.R;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by gzy on 2018/3/24.
 */

public class RegisterActivity extends Activity implements View.OnClickListener {
    private Button btnSendMsg, btnSubmitCode;
    private EditText etPhoneNumber, etCode,etpwd;
    private int i = 60;//倒计时
    private String Appkey;
    private String  AppSecret;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.register_phone);
        sharedPreferences=getSharedPreferences("sleep_partner",MODE_PRIVATE);
        editor=sharedPreferences.edit();
        etPhoneNumber = (EditText) findViewById(R.id.etPhoneNumber);
        etCode = (EditText) findViewById(R.id.etCode);
        etpwd=(EditText) findViewById(R.id.etpwd);
        btnSendMsg = (Button) findViewById(R.id.btnSendMsg);
        btnSubmitCode = (Button) findViewById(R.id.btnSubmitCode);
        Appkey="24d4eb1e64dce";
        AppSecret="251b4f6d2cf288974ad84a2cda845574";
        // 启动短信验证sdk
        SMSSDK.initSDK(this, Appkey, AppSecret);

        //initSDK方法是短信SDK的入口，需要传递您从MOB应用管理后台中注册的SMSSDK的应用AppKey和AppSecrete，如果填写错误，后续的操作都将不能进行
        EventHandler eventHandler = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }
        };
        //注册回调监听接口
        SMSSDK.registerEventHandler(eventHandler);

        btnSendMsg.setOnClickListener(this);
        btnSubmitCode.setOnClickListener(this);
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == -1) {
                btnSendMsg.setText(i + " s");
            } else if (msg.what == -2) {
                btnSendMsg.setText("重新发送");
                btnSendMsg.setClickable(true);
                i = 60;
            } else {
                int event = msg.arg1;
                int result = msg.arg2;
                Object data = msg.obj;
                Log.e("asd", "event=" + event + "  result=" + result + "  ---> result=-1 success , result=0 error");
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // 短信注册成功后，返回MainActivity,然后提示
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        // 提交验证码成功,调用注册接口，之后直接登录
                        //当号码来自短信注册页面时调用登录注册接口
                        //当号码来自绑定页面时调用绑定手机号码接口
                        //成功时存入账号和密码
                        SimpleDateFormat sdf=new SimpleDateFormat("YYYY年MM月DD日"+"hh:mm:ss");
                        editor.putString("time",sdf.format(new Date()));
                        editor.putString("account",etPhoneNumber.getText().toString().trim());
                        editor.putString("pwd",etpwd.getText().toString().trim());
                        //必须要提交
                        editor.commit();
                        Toast.makeText(getApplicationContext(), "短信验证成功"+sharedPreferences.getString("account",null)
                                        +sharedPreferences.getString("time",null),
                                Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                        startActivity(intent);


                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        Toast.makeText(getApplicationContext(), "验证码已经发送",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        ((Throwable) data).printStackTrace();
                    }
                } else if (result == SMSSDK.RESULT_ERROR) {
                    try {
                        Throwable throwable = (Throwable) data;
                        throwable.printStackTrace();
                        JSONObject object = new JSONObject(throwable.getMessage());
                        String des = object.optString("detail");//错误描述
                        int status = object.optInt("status");//错误代码
                        if (status > 0 && !TextUtils.isEmpty(des)) {
                            Log.e("asd", "des: " + des);
                            Toast.makeText(RegisterActivity.this, des, Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } catch (Exception e) {
                        //do something
                    }
                }
            }
        }
    };

    @Override
    public void onClick(View v) {
        String phoneNum = etPhoneNumber.getText().toString().trim();
        switch (v.getId()) {
            case R.id.btnSendMsg:
                if (TextUtils.isEmpty(phoneNum)) {
                    Toast.makeText(getApplicationContext(), "手机号码不能为空",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                SMSSDK.getVerificationCode("86", phoneNum);
                btnSendMsg.setClickable(false);
                //开始倒计时
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (; i > 0; i--) {
                            handler.sendEmptyMessage(-1);
                            if (i <= 0) {
                                break;
                            }
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        handler.sendEmptyMessage(-2);
                    }
                }).start();
                break;
            case R.id.btnSubmitCode:
                String code = etCode.getText().toString().trim();
                if (TextUtils.isEmpty(phoneNum)) {
                    Toast.makeText(getApplicationContext(), "手机号码不能为空",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(code)) {
                    Toast.makeText(getApplicationContext(), "验证码不能为空",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                SMSSDK.submitVerificationCode("86", phoneNum, code);
                break;
            default:
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 销毁回调监听接口
        SMSSDK.unregisterAllEventHandler();
    }
}
