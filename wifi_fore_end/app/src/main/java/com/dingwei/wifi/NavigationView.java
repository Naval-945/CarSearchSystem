package com.dingwei.wifi;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




public class NavigationView extends View {

    private Paint paint;            //老子的魔法画笔

    // 定义橘色点的相对坐标
    private Map<String, PointF> orangePoints = new HashMap<>();

    // 用于存储需要绘制的路线信息
    private List<Route> routes = new ArrayList<>();


    public NavigationView(Context context) {
        super(context);
        init();
        initOrangePoints();
    }

    public NavigationView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
        initOrangePoints();
    }

    public NavigationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        initOrangePoints();
    }

    // 初始化方法，设置画笔属性
    private void init() {
        paint = new Paint();
        paint.setColor(0xFF00FF00); // green color
        paint.setStrokeWidth(10f);
        paint.setStyle(Paint.Style.STROKE);
    }

    // 外部调用此方法来添加一条路线
    public void addRoute(PointF start, PointF intermediate, PointF end) {
        routes.add(new Route(start, intermediate, end));
        invalidate(); // 请求重新绘制视图
    }

    public void addRoute(PointF start, PointF end) {
        routes.add(new Route(start, end));
        invalidate(); // 请求重新绘制视图
    }

    // 外部调用此方法来清除所有路线
    public void clearRoutes() {
        routes.clear();
        invalidate(); // 请求重新绘制视图
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        for (Route route : routes) {

            if(route.intermediateLocation == null){
                float startX = getWidth() * route.startLocation.x;
                float startY = getHeight() * route.startLocation.y;
                float endX = getWidth() * route.endLocation.x;
                float endY = getHeight() * route.endLocation.y;

                canvas.drawLine(startX,startY,endX,endY,paint);
            }else{
                float startX = getWidth() * route.startLocation.x;
                float startY = getHeight() * route.startLocation.y;
                float intermediateX = getWidth() * route.intermediateLocation.x;
                float intermediateY = getHeight() * route.intermediateLocation.y;
                float endX = getWidth() * route.endLocation.x;
                float endY = getHeight() * route.endLocation.y;

                canvas.drawLine(startX, startY, intermediateX, intermediateY, paint);
                canvas.drawLine(intermediateX, intermediateY, endX, endY, paint);
            }
        }
    }

    //初始化橘色点
    public void initOrangePoints() {
        orangePoints.put("A4", new PointF(0.43f, 0.21f));
        orangePoints.put("A1", new PointF(0.55f, 0.215f));
        orangePoints.put("A3", new PointF(0.44f, 0.78f));
        orangePoints.put("A2", new PointF(0.555f, 0.775f));
        orangePoints.put("GATE", new PointF(0.44f, 0.5f));
    }

    public PointF getNearestOrangePoint(String zone) {
        return orangePoints.get(zone);
    }

    // 用于存储路线信息的内部类
    private static class Route {
        PointF startLocation;
        PointF intermediateLocation;
        PointF endLocation;

        Route(PointF start, PointF intermediate, PointF end) {
            this.startLocation = start;
            this.intermediateLocation = intermediate;
            this.endLocation = end;
        }

        Route(PointF start, PointF end){
            this.startLocation = start;
            this.endLocation = end;
            this.intermediateLocation = null;
        }
    }

}
