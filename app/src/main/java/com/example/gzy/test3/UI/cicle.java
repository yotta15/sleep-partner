package com.example.gzy.test3.UI;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.example.gzy.test3.R;

/**
 * created by gzy on 2019/3/24.
 * Describle;
 */
public class cicle extends View {

    //    定义画笔
    Paint paint;

    public cicle(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    //    重写draw方法
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

//        实例化画笔对象
        paint = new Paint();
//        给画笔设置颜色
        paint.setColor(getResources().getColor(android.R.color.holo_blue_bright));
//        设置画笔属性
//        paint.setStyle(Paint.Style.FILL);//画笔属性是实心圆
        paint.setStyle(Paint.Style.STROKE);//画笔属性是空心圆
        paint.setStrokeWidth(8);//设置画笔粗细

        /*四个参数：
                参数一：圆心的x坐标
                参数二：圆心的y坐标
                参数三：圆的半径
                参数四：定义好的画笔
                圆在上半部分
                */
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, 200, paint);

    }


}
