package com.example.gzy.test3.activity;
/**
 * 成员变量私有化，放在同一package
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
public class ProgressView extends View {


    /**
     * 绘制的笔
     */
    private Paint mPaint;

    /**
     * 控件的宽高
     */
    private int mWidth;
    private int mHeight;

    /**
     * 圆的半径
     */
    private int circleRadius;
    /**
     * 圆弧的半径
     */
    private int stokenRadius;

    /**
     * 圆弧的宽度
     */
    private int stokenWidth;
    /**
     * 线与圆弧之间的距离
     */
    private int linePadding;

    /**
     * 选中和没选中的颜色
     */
    private int checkedColor = Color.WHITE;
    private int uncheckedColor = Color.parseColor("#ECA6AD");


    /**
     * 文字的大小
     */
    private int normaltextSize;

    /**
     * 文字距离顶部的距离
     */
    private int textPaddingTop;


    /**
     * 个数
     */
    private int circleCount = 0;

    /**
     * 文本的信息
     */
    private Paint.FontMetricsInt fontMetricsInt;

    /**
     * 圆的y轴
     */
    private int circlePointY;

    /**
     * 根据宽度算出平均分得的宽度
     */
    private int everyWidth;

    /**
     * 数据
     */
    public  List<Model> mModels = new ArrayList<>();


    /**
     * 常量
     *
     * @param context
     */
    public static final int CIRCLE_RADIUS = 4;
    public static final int STOKEN_RADIUS = 9;
    public static final int STOKEN_WIDTH = 2;
    public static final int LINE_PADDING = 6;
    public static final int TEXTPADDINGTOP = 10;
    public static final int NORMALTEXTSIZE = 14;
    public static final int AFTER = 144;
    public static final int STARTING = 143;
    public static final int BEFORE = 142;


    public ProgressView(Context context) {
        this(context, null);
    }

    public ProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initValues();
        initpaint();
    }

    /**
     * 配置一些初始值
     */
    private void initValues() {
        circleRadius = Utils.dip2px(getContext(), CIRCLE_RADIUS);
        stokenRadius = Utils.dip2px(getContext(), STOKEN_RADIUS);
        stokenWidth = Utils.dip2px(getContext(), STOKEN_WIDTH);
        linePadding = Utils.dip2px(getContext(), LINE_PADDING);
        textPaddingTop = Utils.dip2px(getContext(), TEXTPADDINGTOP);
        normaltextSize = Utils.sp2px(getContext(), NORMALTEXTSIZE);
    }

    /**
     * 配置画笔
     */
    private void initpaint() {
        mPaint = new Paint();
        mPaint.setStrokeWidth(stokenWidth);
        mPaint.setTextSize(normaltextSize);
        fontMetricsInt = mPaint.getFontMetricsInt();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        configValues();

    }

    /**
     * 配置需要用到的数据
     */
    private void configValues() {
        mWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        mHeight = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();

        circlePointY = mHeight / 2;
        everyWidth = mWidth / (circleCount + 1);
    }

    public void setData(List<Model> models) {
        mModels = models;
        fillInfo();
        invalidate();

    }

    private void fillInfo() {
        circleCount = mModels.size();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (circleCount == 0)
            return;
        for (int i = 0; i < circleCount; i++) {
            int color = getColor(i);
            boolean needStoken = needStoken(i);
            drawCircleWithParam(canvas, everyWidth * (i + 1), circlePointY, needStoken, color, mModels.get(i).name);
            if (i == circleCount - 1)
                return;
            drawLine(canvas, everyWidth * (i + 1), everyWidth * (i + 2), getColor(i + 1));

        }
    }


    private int getColor(int i) {
        int color;
        Model model = mModels.get(i);
        if (model.state == BEFORE) {
            color = checkedColor;
        } else if (model.state == STARTING) {
            color = checkedColor;
        } else {
            color = uncheckedColor;
        }
        return color;
    }

    private boolean needStoken(int i) {
        boolean needStoken = false;
        Model model = mModels.get(i);
        if (model.state == ProgressView.STARTING) {
            needStoken = true;
        }

        return needStoken;
    }

    /**
     * 画线
     *
     * @param canvas
     * @param circleStartX
     * @param mayEndX
     * @param color
     */
    private void drawLine(Canvas canvas, int circleStartX, int mayEndX, int color) {

        int startX = circleStartX + stokenRadius + stokenWidth + linePadding;
        int endX = mayEndX - stokenWidth - stokenRadius - linePadding;
        int startY = mHeight / 2;

        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(color);
        canvas.drawLine(startX, startY, endX, startY, mPaint);
    }

    /**
     * 画圆
     *
     * @param canvas
     * @param circleX
     * @param circleY
     * @param needStoken
     * @param color
     * @param value
     */
    private void drawCircleWithParam(Canvas canvas, int circleX, int circleY, boolean needStoken, int color, String value) {

        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(color);
        canvas.drawCircle(circleX, circleY, circleRadius, mPaint);
        int textWidth = (int) mPaint.measureText(value, 0, value.length());
        int textStartX = circleX - textWidth / 2;
        int textStartY = Math.abs(Math.abs(fontMetricsInt.bottom) + Math.abs(fontMetricsInt.top)) / 2 + circleY + textPaddingTop;
        textStartY += stokenRadius + stokenWidth;

        canvas.drawText(value, textStartX, textStartY, mPaint);
        if (needStoken) {
            mPaint.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(circleX, circleY, stokenRadius, mPaint);
        }
    }


     public  static class Model {
        String name;

        public Model(String name, int state) {
            this.name = name;
            this.state = state;
        }

        int state;  // BEFORE STARTING AFTER
    }
}
