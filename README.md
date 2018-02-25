
## 前言
![博客地址](http://blog.csdn.net/qq_34911465/article/details/79344254)<br>
之前写过一个Android的画图类，自定义View实现的，其中的擦除效果很不自然，每次擦除都会将线条整条删除，而不是手指指到哪里就擦除哪里，很不自然。一直没有想明白如何做，直到看到了这篇文章：![刮刮卡效果](http://blog.csdn.net/lmj623565791/article/details/40162163)。 结合文章的方法和自己的理解进行了重写，完成了安卓的画图效果。

![画图](http://img.blog.csdn.net/20180221214552816?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvcXFfMzQ5MTE0NjU=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)
## 绘制顺序
如上图所示，首先是绘制的顺序：
原理很简单，首先我们将底层的Bitmap绘制出来，也就是绘制mCacheBitmap。
然后绘制列表中的路径，这是可以正常显示的路径，也就是绘制mPathList的内容。
最后设置了mErasePaint的Xfermode后进行下一步的绘制，我们通过如下设置mPorterDuffXfermode来设置DST_OUT的模式：
```
        mPorterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);
        mErasePaint.setXfermode(mPorterDuffXfermode);
```
这也就是擦除的效果，可以参考前面贴出的博客，于是就绘制了mEraseList的内容。
```
    private void createBitmap() {
        mBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mCacheBitmap != null)
            mCanvas.drawBitmap(mCacheBitmap, 0, 0, null);
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
```

## 切换
当我们从绘制切换到擦除时，是不需要多余的操作的，因为我们的绘制过程是正常的绘制，直接绘制在画板上就可以了，而擦除则需要设置特殊的模式，两相结合也是没有问题的，因为我们的擦除是后绘制的。
而当从擦除切换到绘制时，就会有比较大的问题了，在切换时，我们首先要将目前的View的Bitmap导出，这样后面无论如何绘制都与前面的无关，不会有任何影响。
导出View的Bitmap：
```
    private void createViewBitmap() {
        setDrawingCacheEnabled(true);
        buildDrawingCache();
        Bitmap bitmap = getDrawingCache();
        if (bitmap != null)
            mCacheBitmap = Bitmap.createBitmap(bitmap);
        destroyDrawingCache();
        setDrawingCacheEnabled(false);
    }
```
然后要将绘制的路径列表和擦除列表都清空，去除前面的影响。然后再进行绘制。

### 路径判断
这里主要是讲讲如何进行Android的绘制，也就是触摸事件。首先我们是获得当前的点，并且要新建一个Path对象，这里我封装在PathInfo里了。：
手指按下事件：
```
    private void touch_down(float x, float y) {
        mX = x;
        mY = y;
        Log.d("tag", x + " " + y);

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
```
手指移动事件：
每次调用lineTo方法就能绘制从起点到当前点的路径了。
注意要设置画笔的模式是STROKE。参考：![Paint.Style.?](http://blog.csdn.net/qq_34911465/article/details/79342400)
```
    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= MAX_TOUCH || dy >= MAX_TOUCH) {
            mPathInfo.path.lineTo(x, y);
        }
    }
```
onTouchEvent:
```
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
```

## 效果图：
![这里写图片描述](http://img.blog.csdn.net/20180221220810384?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvcXFfMzQ5MTE0NjU=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

#### 后记
本文简单的总结了一个小例子，放在github上，欢迎查看：https://github.com/zjtone/AndroidDrawing
