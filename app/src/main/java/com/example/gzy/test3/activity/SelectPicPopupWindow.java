package com.example.gzy.test3.activity;

/**
 * created by gzy on 2019/4/1.
 * Describle;
 */
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.gzy.test3.R;
import com.example.gzy.test3.util.DensityUtil;

public class SelectPicPopupWindow extends Activity implements OnClickListener{

    private Button btn_take_photo, btn_pick_photo, btn_cancel;
    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_bottom_set);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        btn_take_photo = (Button) this.findViewById(R.id.btn_take_photo);
        btn_pick_photo = (Button) this.findViewById(R.id.btn_pick_photo);
        btn_cancel = (Button) this.findViewById(R.id.btn_cancel);

        layout=(LinearLayout)findViewById(R.id.pop_layout);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) layout.getLayoutParams();
        params.width = getResources().getDisplayMetrics().widthPixels - DensityUtil.dp2px(getApplicationContext(), 16f);
        params.bottomMargin = DensityUtil.dp2px(getApplicationContext(), 8f);
        layout.setLayoutParams(params);
        //添加选择窗口范围监听可以优先获取触点，即不再执行onTouchEvent()函数，点击其他地方时执行onTouchEvent()函数销毁Activity
        layout.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(), "提示：点击窗口外部关闭窗口！",
                        Toast.LENGTH_SHORT).show();
            }
        });
        //添加按钮监听
        btn_cancel.setOnClickListener(this);
        btn_pick_photo.setOnClickListener(this);
        btn_take_photo.setOnClickListener(this);
    }

    //实现onTouchEvent触屏函数但点击屏幕时销毁本Activity
    @Override
    public boolean onTouchEvent(MotionEvent event){
        finish();
        return true;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_take_photo:
                break;
            case R.id.btn_pick_photo:
                break;
            case R.id.btn_cancel:
                break;
            default:
                break;
        }
        finish();
    }

}

