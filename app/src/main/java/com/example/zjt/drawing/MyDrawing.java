package com.example.zjt.drawing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zjt on 18-2-21.
 */

public class MyDrawing extends View {
    private final int MODE_DRAW = 0x1, MODE_ERASE = 0x2;
    private final float MAX_TOUCH = 1;

    private float mX, mY;
    private List<PathInfo> mPathList = null;// 画图的线条
    private List<PathInfo> mEraseList = null;// 擦除的线条
    private PathInfo mPathInfo = null;
    private Paint mPaint, mErasePaint;
    private Bitmap mBitmap, mCacheBitmap;
    private Canvas mCanvas;
    private int mColor, mWidth, mMode = MODE_DRAW, index = 0;
    private PorterDuffXfermode mPorterDuffXfermode;

    public MyDrawing(Context context) {
        super(context);
        init();
    }

    public MyDrawing(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyDrawing(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPathList = new ArrayList<>();
        mEraseList = new ArrayList<>();
        mPaint = new Paint();
        mErasePaint = new Paint();
        mColor = Color.parseColor("#000000");
        mWidth = 20;
        mPorterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);
        setPaintStyle(mPaint);
        setPaintStyle(mErasePaint);
    }

    private void setPaintStyle(Paint paint) {
        paint.setStrokeWidth(mWidth);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);// 线冒样式：圆角
        paint.setStrokeCap(Paint.Cap.ROUND);// 线段连接处样式：圆角
    }

    public void setColor(int color) {
        this.mColor = color;
    }

    public void switchMode() {
        if (this.mMode == MODE_DRAW) {
            this.mMode = MODE_ERASE;
        } else {
            createViewBitmap();
            mPathList.clear();
            mEraseList.clear();
            this.mMode = MODE_DRAW;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mBitmap == null) {
            createBitmap();
        }
    }

    private void createBitmap() {
        mBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_4444);
        mCanvas = new Canvas(mBitmap);
    }

    private void createViewBitmap() {
        setDrawingCacheEnabled(true);
        buildDrawingCache();
        Bitmap bitmap = getDrawingCache();
        if (bitmap != null)
            mCacheBitmap = Bitmap.createBitmap(bitmap);
        destroyDrawingCache();
        setDrawingCacheEnabled(false);
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mCacheBitmap != null) {
            mCanvas.drawBitmap(mCacheBitmap, 0, 0, null);
        }
        if (mPathList != null) {
            for (int i = 0; i < mPathList.size(); i++) {
                PathInfo pathInfo = mPathList.get(i);
                mPaint.setStrokeWidth(pathInfo.width);
                mPaint.setColor(pathInfo.color);

                mCanvas.drawPath(pathInfo.path, mPaint);
            }
        }
        mErasePaint.setXfermode(mPorterDuffXfermode);
        mErasePaint.setStyle(Paint.Style.STROKE);
        for (int i = 0; i < mEraseList.size(); i++) {
            mErasePaint.setStrokeWidth(mEraseList.get(i).width);
            mErasePaint.setColor(mEraseList.get(i).color);
            mCanvas.drawPath(mEraseList.get(i).path, mErasePaint);
        }
        canvas.drawBitmap(mBitmap, 0, 0, null);
    }

    private void touch_down(float x, float y) {
        mX = x;
        mY = y;

        mPathInfo = new PathInfo();
        mPathInfo.path.moveTo(x, y);
        mPathInfo.width = this.mWidth;
        if (this.mMode == MODE_DRAW) {
            mPathInfo.color = this.mColor;
            mPathList.add(mPathInfo);
        } else if (this.mMode == MODE_ERASE) {
            mPathInfo.color = Color.WHITE;
            mEraseList.add(mPathInfo);
        }
    }

    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= MAX_TOUCH || dy >= MAX_TOUCH) {
            mPathInfo.path.lineTo(x, y);
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_down(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                break;
        }
        invalidate();
        return true;
    }
}
