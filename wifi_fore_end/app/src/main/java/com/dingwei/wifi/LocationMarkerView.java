package com.dingwei.wifi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

public class LocationMarkerView extends View {

    private static final String TAG = "LocationMarkerView";

    private Paint paint;
    private PointF location;
    private Bitmap markerBitmap;

    public LocationMarkerView(Context context) {
        super(context);
        init(context);
    }

    public LocationMarkerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LocationMarkerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        paint = new Paint();
        markerBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.location_icon);
        location = new PointF(0.5f, 0.5f); // default location in the center
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        // Obtain the map container's dimensions
        int mapWidth = ((View) getParent()).getWidth();
        int mapHeight = ((View) getParent()).getHeight();

        float x = mapWidth * location.x;
        float y = mapHeight * location.y;

        // Draw the marker image at the specified location
        if (markerBitmap != null) {
            float markerWidth = markerBitmap.getWidth();
            float markerHeight = markerBitmap.getHeight();
            canvas.drawBitmap(markerBitmap, x - markerWidth / 2, y - markerHeight / 2, paint);
        }

        Log.i(TAG,"in onDraw() But real point is x: "+x+" y: "+y);
    }

    public void updateLocation(float x, float y) {
        Log.i(TAG, "draw! "+x+" "+y);
        location.set(x, y);
        invalidate(); // request to redraw the view
    }

    public PointF getLocation() {
        return location;
    }

}
