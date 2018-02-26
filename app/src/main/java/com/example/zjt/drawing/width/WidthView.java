package com.example.zjt.drawing.width;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by zjt on 18-2-26.
 */

public class WidthView extends View {
    private final int STYLE_CIRCLE = 0x1, STYLE_RECT = 0x2, STYLE_LINE = 0x3;
    private Paint mWidthPaint;
    private int mWidth, mColor, mStyle, mActiveColor;
    private Path mPath;
    private boolean isActive = false;

    public WidthView(Context context) {
        super(context);
        init();
    }

    public WidthView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WidthView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mWidthPaint = new Paint();
        mWidthPaint.setStyle(Paint.Style.FILL);
        mWidth = 10;
        mColor = Color.BLACK;
        mActiveColor = Color.WHITE;
        mStyle = STYLE_CIRCLE;
        mWidthPaint.setStrokeWidth(mWidth);
        mWidthPaint.setColor(mColor);
    }

    public void setWidth(int width) {
        mWidth = width;
    }

    public void setColor(int color) {
        mColor = color;
    }

    public void setActiveColor(int color){
        this.mActiveColor = color;
    }

    public void setStyle(int style) {
        this.mStyle = style;
    }

    public void setActive(boolean active){
        this.isActive = active;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mWidthPaint.setStrokeWidth(mWidth);
        mWidthPaint.setColor(isActive? mActiveColor: mColor);
        switch (mStyle) {
            case STYLE_LINE:
                if (mPath == null)
                    mPath = new Path();
                float x = getPaddingLeft() + getLeft(),
                        y = getPaddingTop() + getTop();
                mPath.moveTo(x, y);
                mPath.lineTo(x, getBottom());
                canvas.drawPath(mPath, mWidthPaint);
                break;
            case STYLE_RECT:
                break;
            case STYLE_CIRCLE:
                canvas.drawCircle((float) (getPaddingLeft() + getLeft() + getWidth() * 0.5),
                        (float) (getPaddingTop() + getTop() + getHeight() * 0.5), mWidth, mWidthPaint);
                break;
        }
    }
}
