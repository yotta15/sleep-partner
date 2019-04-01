package com.example.gzy.test3.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gzy.test3.R;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import com.example.gzy.test3.activity.LoginActivity;
import com.example.gzy.test3.activity.RegisterActivity;
import com.example.gzy.test3.model.UserModel;
import com.example.gzy.test3.presenter.RegisterPresenterImpl;

import static cn.bmob.v3.Bmob.getApplicationContext;

/**
 * 此页面用于注册  昵称、手机号、密码
 */
public class RegisterFragmentOne extends Fragment implements View.OnClickListener, IRegisterOneView {
    private Button  btnSubmitCode;
    private EditText etPhoneNumber, etCode, etpwd, etusername;
    private TextView btnSendMsg;
    private int i = 60;//倒计时
    private String Appkey;
    private String AppSecret;


    UserModel user;
    int position;

    private RegisterPresenterImpl registerPresenter;

    public static RegisterFragmentOne newInstance(int position, String name) {

        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putString("name", name);
        RegisterFragmentOne fragment = new RegisterFragmentOne();
        fragment.setArguments(args);
        return fragment;
    }

    @android.support.annotation.Nullable
    @Override
    public View onCreateView(android.view.LayoutInflater inflater, @android.support.annotation.Nullable android.view.ViewGroup container, @android.support.annotation.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_phone, container, false);
        registerPresenter = new RegisterPresenterImpl(this);
        etusername = (EditText) view.findViewById(R.id.nickname);
        etPhoneNumber = (EditText) view.findViewById(R.id.etPhoneNumber);
        etCode = (EditText) view.findViewById(R.id.etCode);
        etpwd = (EditText) view.findViewById(R.id.etpwd);
        btnSendMsg = (TextView) view.findViewById(R.id.btnSendMsg);
        btnSubmitCode = (Button) view.findViewById(R.id.btnSubmitCode);
        Appkey = "24d4eb1e64dce";
        AppSecret = "251b4f6d2cf288974ad84a2cda845574";
        // 启动短信验证sdk
        SMSSDK.initSDK(getActivity().getApplicationContext(), Appkey, AppSecret);

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

        position = getArguments().getInt("position");
        String name = getArguments().getString("name");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if ( -1 ==msg.what ) {
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
                        SimpleDateFormat sdf = new SimpleDateFormat("YYYY年MM月DD日" + "hh:mm:ss");
//                        Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
//                        startActivity(intent);


                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        Toast.makeText(getActivity(), "验证码已经发送",
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
                            Toast.makeText(getActivity(), des, Toast.LENGTH_SHORT).show();
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
        String passwd = etpwd.getText().toString().trim();
        String username = etusername.getText().toString().trim();
        switch (v.getId()) {
            case R.id.btnSendMsg:
                if (TextUtils.isEmpty(phoneNum)) {
                    Toast.makeText(getApplicationContext(), "手机号码不能为空",
                            Toast.LENGTH_SHORT).show();
                    return;
                }  if(isMobile(phoneNum)) {
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
                SMSSDK.getVerificationCode("86", phoneNum);
                btnSendMsg.setClickable(false);
            }


                break;
            case R.id.btnSubmitCode:
                String code = etCode.getText().toString().trim();
                if (android.text.TextUtils.isEmpty(username)) {
                    Toast.makeText(getActivity(), "昵称不能为空",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (android.text.TextUtils.isEmpty(phoneNum)) {
                    Toast.makeText(getActivity(), "手机号码不能为空",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (android.text.TextUtils.isEmpty(code)) {
                    Toast.makeText(getActivity(), "验证码不能为空",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (android.text.TextUtils.isEmpty(passwd)) {
                    Toast.makeText(getActivity(), "密码不能为空",
                            Toast.LENGTH_SHORT).show();
                    return;
                }


                SMSSDK.submitVerificationCode("86", phoneNum, code);
                if (!phoneNum.equals("") && !passwd.equals("") && !username.equals("")) {
                    registerPresenter.initUser(username, phoneNum, passwd);
                }
                break;
            default:
                break;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 销毁回调监听接口
        cn.smssdk.SMSSDK.unregisterAllEventHandler();
    }

    @Override
    public boolean OnSignUpResult(boolean flag, String username, String password) {
        if (flag) {
            registerPresenter.LoginUser(username, password);
            ((RegisterActivity) getActivity()).setPosition(position);

        } else {
            Toast.makeText(getActivity(), "register error", Toast.LENGTH_LONG).show();
            Intent intent=new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        }
        return flag;
    }
    //这个方法是判断是否是电话号的
    public static boolean isMobile(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3,5,8][0-9]{9}$"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

}
