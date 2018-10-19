package com.example.gzy.test3;


import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private EditText from, to, subject, theme, message;
    private TextView show;
    private Spinner receiverList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.second_main);
        from = (EditText) findViewById(R.id.from);
        to = (EditText) findViewById(R.id.to);
        subject = (EditText) findViewById(R.id.subject);
        theme = (EditText) findViewById(R.id.theme);
        message = (EditText) findViewById(R.id.message);
        show = (TextView) findViewById(R.id.show);
        receiverList = (Spinner) findViewById(R.id.receiverlist);

//        String[] arr={"bob@sina.com","john@qq.com","mark@163.com"};
//        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,R.layout.array_item,arr);
//        spinner.setAdapter(adapter);
        final Button shoujian = (Button) findViewById(R.id.shoujian);
        final Button login = (Button) findViewById(R.id.login);
        final Button logout = (Button) findViewById(R.id.logout);
//        final Button back=(Button) findViewById(R.id.back);
        final Button send = (Button) findViewById(R.id.send);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] users = MainActivity.this.getResources().getStringArray(R.array.users);
                boolean hasUser = false;
                if (from.getText().toString().trim().equals("")) {
                    from.setText(null);
                    from.setHint("请输入用户名");
                } else {
                    for (String user : users) {
                        if (user.equals(from.getText().toString())) {
                            String st = from.getText().toString();
                            from.setText(null);
                            from.setHint("用户" + st + "已登入");
                            subject.setText(user + "@" + MainActivity.this.getResources().getString(R.string.email));
                            message.append("\n\t\t\t签名：" + user);
                            login.setEnabled(false);
                            send.setEnabled(true);
                            logout.setEnabled(true);
                            hasUser = true;
                            break;
                        }
                    }
                    if (!hasUser) {
                        String st = from.getText().toString();
                        from.setText(null);
                        from.setHint("用户" + st + "不存在");
                    }
                }
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("提示");
                builder.setMessage("确认注销用户吗？");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        from.setText(null);
                        from.setHint("用户名");
                        from.setText(null);
                        to.setText(null);
                        subject.setText(null);
                        message.setText(MainActivity.this.getString(R.string.nchu));
                        show.setText(null);
                        receiverList.setSelection(0);
                        login.setEnabled(true);
                        logout.setEnabled(false);
                        send.setEnabled(false);
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });
        shoujian.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                            builder.setTitle("选择常用收件人");
                                            final String[] arr= Arrays.copyOfRange(MainActivity.this.getResources().getStringArray(R.array.receiverList),1,MainActivity.this.getResources().getStringArray(R.array.receiverList).length);
                                            builder.setItems(arr, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    to.setText(arr[i].toString());

                                                }
                                            });
                                            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });
                                            builder.create().show();

                                        }

                                    }
        );

        receiverList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view,
                                       int position,  //被选收件人在列表中的位置
                                       long id) {
                if (position != 0) {
                    to.setText(receiverList.getItemAtPosition(position).toString());
                } else {
                    to.setText(null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent();
//                //为intent设置action，catagory属性
//                intent.setAction(Intent.ACTION_MAIN);
//                intent.addCategory(Intent.CATEGORY_HOME);
//                startActivity(intent);
//            }
//        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (to.getText().toString().trim().equals("")) {
                    show.setText("请输入收件人");
                } else {
                    show.setText("成功发送到" + to.getText());
                }
            }
        });
    }

}

//textview 中的文字自动消失，massage的append问题
