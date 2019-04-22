package com.example.gzy.test3.UI;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * created by gzy on 2019/4/22.
 * Describle;
 */
public class CusFntTextView extends TextView {

    public CusFntTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CusFntTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CusFntTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "Futura.ttf");
            setTypeface(tf);
        }
    }
}

