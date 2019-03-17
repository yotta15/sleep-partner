package com.example.gzy.test3.UI;

import android.content.Context;
import android.widget.Toast;

/**
 * created by gzy on 2019/3/13.
 * Describle;
 */
public class Toastview {
    public void showToast(Context context,String content) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }

}
