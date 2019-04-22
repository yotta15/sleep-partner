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
public class clock extends View {

    //    定义画笔
    Paint paint;

    public clock(Context context, @Nullable AttributeSet attrs) {
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
        paint.setStrokeWidth(10);//设置画笔粗细
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, 200, paint);

        paint.setStrokeWidth(4);
        paint.setTextSize(40);
        paint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetrics fontMetrics=paint.getFontMetrics();

        /*四个参数：
                参数一：圆心的x坐标
                参数二：圆心的y坐标
                参数三：圆的半径
                参数四：定义好的画笔
                圆在上半部分
                (fontMetrics.bottom - fontMetrics.top)/2  文字高度的一半
                 float distance=(fontMetrics.bottom - fontMetrics.top)/2 - fontMetrics.bottom;
                 float baseline=rectF.centerY()+distance;
                */
        float distance=(fontMetrics.bottom - fontMetrics.top)/2 - fontMetrics.bottom;
        //float baseline=canvas.getHeight()/2  +distance;

        //y值减去元的半径
        canvas.drawText("12",getWidth()/2,getHeight()/2-200+fontMetrics.bottom - fontMetrics.top,paint);
        canvas.drawText("6",getWidth()/2,getHeight()/2+200-distance,paint);
        canvas.drawText("9",getWidth()/2-170,getHeight()/2+distance,paint);
        canvas.drawText("3",getWidth()/2+170,getHeight()/2+distance,paint);
    }


}
