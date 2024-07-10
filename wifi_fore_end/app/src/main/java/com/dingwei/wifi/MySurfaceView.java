package com.dingwei.wifi;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MySurfaceView extends SurfaceView
        implements SurfaceHolder.Callback, Runnable {

    // SurfaceHolder
    private SurfaceHolder mHolder;
    // 用于绘图的Canvas
    private Canvas mCanvas;
    // 子线程标志位
    private boolean mIsDrawing;
    // 地图图片
    private Bitmap mBitmap;
    // 画笔
    private Paint mPaint;
    // 返回的定位相对坐标
    private float X=0.5F;
    private float Y=0.5F;

    private static final long DOUBLE_CLICK_TIME_SPACE = 300;    //两次点击的间隔
    private float mCurrentScaleMax; //最大缩放倍率
    private float mCurrentScale;    //当前缩放倍率
    private float mCurrentScaleMin; //最小缩放倍率
    private PointF mStartPoint; //
    private volatile PointF mapCenter;  // mapCenter表示地图中心在屏幕上的坐标
    private long lastClickTime; // 记录上一次点击屏幕的时间，以判断双击事件
    private float windowWidth, windowHeight;    //屏幕宽度，屏幕高度
    private float oldRate = 1000;
    private float oldDist = 1;
    private float offsetX, offsetY;
    private boolean isShu = true;
    private Status mStatus = Status.NONE;
    private Bitmap location_icon;
    private Path mPath;
    private Switch switch_path;


//    public void setX(float x) {
//        X = x;
//    }
//
//    public void setY(float y){
//        Y = y;
//    }

    private enum Status {
        NONE, ZOOM, DRAG
    };


    public MySurfaceView(Context context) {
        super(context);
        initView();
    }

    public MySurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MySurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    //初始化View
    private void initView() {
        //初始化mHolder
        mHolder = getHolder();
        mHolder.addCallback(this);

        //初始化画笔和点
        mPaint = new Paint();
        mStartPoint = new PointF();
        mapCenter = new PointF();

        // 获取屏幕的宽和高
        windowWidth = getResources().getDisplayMetrics().widthPixels;   //宽度就是屏幕宽度
        windowHeight = getResources().getDisplayMetrics().heightPixels*(float)(10f/16f);    //这里由于view未完全创建，不能直接获取view宽高，但可以通过控件的权重和屏幕高度算出来
/*        // 因为view不是全屏，所以获取view宽高
        windowWidth = getWidth();
        windowHeight = getHeight();*/

        Log.d("TEST","W:"+String.valueOf(windowWidth));
        Log.d("TEST","H:"+String.valueOf(windowHeight));

        //定义bitmap，这里写在HomeFragment里了
        Bitmap map = BitmapFactory.decodeResource(this.getContext().getResources(), R.drawable.map); //地图图片
        setBitmap(map);
        location_icon = BitmapFactory.decodeResource(this.getContext().getResources(), R.mipmap.location_icon); //定位图标图片

        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setKeepScreenOn(true);
        //mHolder.setFormat(PixelFormat.OPAQUE);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d("PAN","=========surfaceCreated========");
        mIsDrawing = true;
        new Thread(this).start();   //创建surfaceview时新建一个子线程来绘制
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder,
                               int format, int width, int height) {
        Log.d("PAN","=========surfaceChanged========");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d("PAN","=========surfaceDestroyed========");
        mIsDrawing = false;
    }

    //设置地图图片
    public void setBitmap(Bitmap bitmap) {
        if (mBitmap != null) {
            mBitmap.recycle();
        }
        mBitmap = bitmap;
        // 设置最小缩放为铺满屏幕，最大缩放为最小缩放的4倍
        mCurrentScaleMin = Math.min(windowHeight / mBitmap.getHeight(),
                windowWidth / mBitmap.getWidth());
        mCurrentScale = mCurrentScaleMin;
        mCurrentScaleMax = mCurrentScaleMin * 4;
        mapCenter.set(mBitmap.getWidth() * mCurrentScale / 2,
                mBitmap.getHeight() * mCurrentScale / 2);
        float bitmapRatio = mBitmap.getHeight() / mBitmap.getWidth();
        float winRatio = windowHeight / windowWidth;
        // 判断屏幕铺满的情况，isShu为true表示屏幕横向被铺满，为false表示屏幕纵向被铺满
        if (bitmapRatio <= winRatio) {
            isShu = true;
        } else {
            isShu = false;
        }
        //draw();
    }

    // 处理拖拽事件
    private void drag(MotionEvent event) {
        PointF currentPoint = new PointF();
        currentPoint.set(event.getX(), event.getY());
        offsetX = currentPoint.x - mStartPoint.x;
        offsetY = currentPoint.y - mStartPoint.y;
        Log.d("TEST", String.valueOf(offsetX));
        Log.d("TEST", String.valueOf(offsetY));
        // 以下是进行判断，防止出现图片拖拽离开屏幕
//        if (offsetX > 0
//                && mapCenter.x + offsetX - mBitmap.getWidth() * mCurrentScale
//                / 2 > 0) {
//            offsetX = 0;
//        }
//        if (offsetX < 0
//                && mapCenter.x + offsetX + mBitmap.getWidth() * mCurrentScale
//                / 2 < windowWidth) {
//            offsetX = 0;
//        }
//        if (offsetY > 0
//                && mapCenter.y + offsetY - mBitmap.getHeight() * mCurrentScale
//                / 2 > 0) {
//            offsetY = 0;
//        }
//        if (offsetY < 0
//                && mapCenter.y + offsetY + mBitmap.getHeight() * mCurrentScale
//                / 2 < windowHeight) {
//            offsetY = 0;
//        }
        mapCenter.x += offsetX;
        mapCenter.y += offsetY;
        //draw();
        mStartPoint = currentPoint;
    }


    // 处理多点触控缩放事件
    private void zoomAction(MotionEvent event) {
        float newDist = spacing(event);
        if (newDist > 10.0f) {
            mCurrentScale = oldRate * (newDist / oldDist);
            if (mCurrentScale < mCurrentScaleMin) {
                mCurrentScale = mCurrentScaleMin;
            } else if (mCurrentScale > mCurrentScaleMax) {
                mCurrentScale = mCurrentScaleMax;
            }

            if (isShu) {
                if (mapCenter.x - mBitmap.getWidth() * mCurrentScale / 2 > 0) {
                    mapCenter.x = mBitmap.getWidth() * mCurrentScale / 2;
                } else if (mapCenter.x + mBitmap.getWidth() * mCurrentScale / 2 < windowWidth) {
                    mapCenter.x = windowWidth - mBitmap.getWidth()
                            * mCurrentScale / 2;
                }
                if (mapCenter.y - mBitmap.getHeight() * mCurrentScale / 2 > 0) {
                    mapCenter.y = mBitmap.getHeight() * mCurrentScale / 2;
                }
            } else {

                if (mapCenter.y - mBitmap.getHeight() * mCurrentScale / 2 > 0) {
                    mapCenter.y = mBitmap.getHeight() * mCurrentScale / 2;
                } else if (mapCenter.y + mBitmap.getHeight() * mCurrentScale
                        / 2 < windowHeight) {
                    mapCenter.y = windowHeight - mBitmap.getHeight()
                            * mCurrentScale / 2;
                }

                if (mapCenter.x - mBitmap.getWidth() * mCurrentScale / 2 > 0) {
                    mapCenter.x = mBitmap.getWidth() * mCurrentScale / 2;
                }
            }
        }
        Log.d("touch", String.valueOf(mCurrentScale));
        //draw();
    }

    public static double getRandomValue() {
        Random rand = new Random();
        return rand.nextDouble() ; // 返回一个 [-0.5, 0.5) 范围内的随机浮点数
    }

    // 计算两个触摸点的距离
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        Log.d("touch", String.valueOf((float) Math.sqrt(x * x + y * y)));
        return (float) Math.sqrt(x * x + y * y);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                if (event.getPointerCount() == 1) {
                    // 如果两次点击时间间隔小于一定值，则默认为双击事件
                    if (event.getEventTime() - lastClickTime < DOUBLE_CLICK_TIME_SPACE) {
                        //双击
                    } else {
                        mStartPoint.set(event.getX(), event.getY());
                        mStatus = Status.DRAG;
                    }
                }
                lastClickTime = event.getEventTime();
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                float distance = spacing(event);
                if (distance > 10f) {
                    mStatus = Status.ZOOM;
                    oldDist = distance;
                }
                break;

            case MotionEvent.ACTION_MOVE:

                if (mStatus == Status.DRAG) {
                    drag(event);
                } else if (mStatus == Status.ZOOM) {
                    zoomAction(event);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mStatus != Status.ZOOM) {
                    //点击标记
                }

            case MotionEvent.ACTION_POINTER_UP:
                oldRate = mCurrentScale;
                mStatus = Status.NONE;
                break;
            default:
                break;
        }

        return true;
    }


/*    float x=0;
    float y=0;
    Path mPath = new Path();*/

    public static final int TIME_IN_FRAME = 100;
    public int count1 = 0;//定位刷新频率
    public int count2 = 0;//路径刷新频率
    @Override
    public void run() {
        //画笔每次的变化
        while(mIsDrawing){
            long startTime = System.currentTimeMillis();

            //调用绘图函数
            draw();

            /**取得更新结束的时间**/
            long endTime = System.currentTimeMillis();

            /**计算出一次更新的毫秒数**/
            int diffTime  = (int)(endTime - startTime);

            /**确保每次更新时间为30毫秒**/
            while(diffTime <=TIME_IN_FRAME) {
                diffTime = (int)(System.currentTimeMillis() - startTime);
                /**线程等待**/
                Thread.yield();
            }
        }
    }

    //7.12使用地图长37格，宽28格，实测左上角为（0，0） ytq
    public static final double RelativeDisPerX = 0.0270;   //0.0746;    //代表地图上的每格X占到了地图宽度的比例
    public static final double RelativeDisPerY = 0.0357;   //0.1012;    //代表地图上的每格Y占到了地图高度的比例


    //绘图操作
    private void draw() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("xy", Context.MODE_PRIVATE);
        float x = sharedPreferences.getFloat("x", 0);
        float y = sharedPreferences.getFloat("y", 0);
        setX(x);
        setY(y);

        Log.d("PAN","============draw========");
        try {
            mCanvas = mHolder.lockCanvas();
            // draw sth绘制过程
            // 定义画笔
            mPaint.setStyle(Paint.Style.STROKE); //只描边
            mPaint.setStrokeWidth(10);
            mPaint.setColor(Color.RED);
            mPaint.setAntiAlias(true); //设置抗锯齿
            mPaint.setDither(true); //设置防抖动

            // 绘制地图图片
            Matrix matrix = null;
            if (mBitmap != null) {
                mCanvas.drawColor(Color.GRAY);
                matrix = new Matrix();
                matrix.setScale(mCurrentScale, mCurrentScale,
                        mBitmap.getWidth() / 2,
                        mBitmap.getHeight() / 2);//以图片中心为原点进行放大，为什么不用mapCenter，因为在处理拖拽时，可能还没拖过去，这个mapCenter可能还不是当前实际上的图片中心，而是将要拖过去的图片中心
                matrix.postTranslate(
                        mapCenter.x - mBitmap.getWidth() / 2,
                        mapCenter.y - mBitmap.getHeight() / 2);//处理拖拽操作
                mCanvas.drawBitmap(mBitmap, matrix, mPaint);
            }
            // 设置图标矩阵缩放，图标不进行缩放
            matrix.setScale(1.0f, 1.0f);
            // 绘制定位点
            // 使用Matrix使得Bitmap的宽和高发生变化，在这里使用的mapX和mapY都是相对值
            matrix.postTranslate(
                    (float) (mapCenter.x - location_icon.getWidth()
                                                / 2 - mBitmap.getWidth()
                                                * mCurrentScale / 2 //算到这里是获取当前地图左上角所在屏幕上的坐标（像素数）
                                                + mBitmap.getWidth()
                                                * X//相对坐标
                                                * mCurrentScale),    //这三项相乘的结果是在当前缩放比下，目标位置离左边缘的像素数
                    (float) (mapCenter.y - location_icon.getHeight()
                                                - mBitmap.getHeight()
                                                * mCurrentScale / 2
                                                + mBitmap.getHeight()
                                                * Y //相对坐标
                                                * mCurrentScale));
            mCanvas.drawBitmap(location_icon, matrix, mPaint);
        } catch (Exception e) {
        } finally {
            if (mCanvas != null)
                mHolder.unlockCanvasAndPost(mCanvas);//保证每次都将绘图的内容提交
        }
    }
}