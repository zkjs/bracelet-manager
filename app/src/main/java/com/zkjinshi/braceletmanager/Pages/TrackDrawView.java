package com.zkjinshi.braceletmanager.Pages;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.zkjinshi.braceletmanager.models.AccessPointVo;
import com.zkjinshi.braceletmanager.models.DrawableType;
import com.zkjinshi.braceletmanager.models.DrawableVo;
import com.zkjinshi.braceletmanager.models.FloorVo;
import com.zkjinshi.braceletmanager.models.Gps;
import com.zkjinshi.braceletmanager.models.TrackPointVo;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static android.view.MotionEvent.INVALID_POINTER_ID;

/**
 * 自定义轨迹&楼层绘制View
 * Created by yejun on 11/28/16.
 * Copyright (C) 2016 qinyejun
 */

public class TrackDrawView extends View {

    private List<TrackPointVo> track;
    private List<AccessPointVo> aps;
    private List<FloorVo> floors;
    private int floor = 2;

    final float measure = 960;          //轨迹地图坐标基准尺寸，即通过api获取的坐标比例尺为960
    final float scale = getResources().getDisplayMetrics().density; //当前屏幕分辨率
    final int padding = 20;             // 界面留白尺寸
    final int textSize = 30;            // 界面文字大小
    final int lineColor = Color.GRAY;   // 轨迹线颜色
    final int lineWidth = 6;            // 轨迹线宽度
    final int apColor = Color.BLUE;     // AP 标志点的颜色
    final int startColor = Color.CYAN;  // 轨迹开始点颜色
    final int endColor = Color.RED;     // 轨迹结束点颜色

    private float maxScale = 8.0f;
    private float minScale = 1.0f;

    // 以下成员变量用于处理drap & scale
    private float mLastTouchX;  // 手指接触屏幕最后位置X
    private float mLastTouchY;  // 手指接触屏幕最后位置Y
    private int mActivePointerId = INVALID_POINTER_ID;
    private float mPosX = 0;    // 拖拽移动距离X
    private float mPosY = 0;    // 拖拽移动距离Y
    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1.f;
    private boolean isScaling = false;
    private ZoomListener mZoomListener;

    private int screenWidth;
    private int screenHeight;

    private Bounds bounds;

    public TrackDrawView(Context context, Point screenSize) {
        super(context);

        screenWidth = screenSize.x;
        screenHeight = screenSize.y;
        mScaleDetector = new ScaleGestureDetector(context, new TrackDrawView.ScaleListener());
    }

    public void setScale(float scale) {
        mScaleFactor = scale;
        invalidate();
    }

    public float getScale() {
        return mScaleFactor;
    }

    public void setMaxScale(float maxScale) {
        this.maxScale = maxScale;
    }

    public float getMaxScale() {
        return maxScale;
    }

    public void setMinScale(float minScale) {
        this.minScale = minScale;
    }

    public float getMinScale() {
        return minScale;
    }

    public List<TrackPointVo> getTrack() {
        return track;
    }

    public void setTrack(List<TrackPointVo> track) {
        this.track = track;
        invalidate();
    }

    public List<AccessPointVo> getAps() {
        return aps;
    }

    public void setAps(List<AccessPointVo> aps) {
        this.aps = aps;
        invalidate();
    }

    public List<FloorVo> getFloors() {
        return floors;
    }

    public void setFloors(List<FloorVo> floors) {
        this.floors = floors;
        calcBounds();
        invalidate();
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
        this.mPosX = 0;
        this.mPosY` = 0;
        invalidate();
    }

    public void setZoomListener(ZoomListener zoomListener) {
        mZoomListener = zoomListener;
    }

    /**
     * 放大地图
     */
    public void zoomin() {
        if( mScaleFactor + 1 > maxScale ) return;
        mScaleFactor += 1;
        invalidate();
        if (mZoomListener !=null) mZoomListener.zoomFinished(mScaleFactor);
    }

    /**
     * 缩小地图
     */
    public void zoomout() {
        if( mScaleFactor - 1 < minScale ) return;
        mScaleFactor -= 1;
        invalidate();
        if (mZoomListener !=null) mZoomListener.zoomFinished(mScaleFactor);
    }

    /**
     * 自定义View绘制逻辑
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Log.i("onDraw", "start drawing");
        Log.i("Scale", "scale=" + scale);
        Log.i("Screen Size", "width=" + screenWidth + " , height=" + screenHeight);

        canvas.save();

        canvas.scale(mScaleFactor, mScaleFactor);
        canvas.translate(mPosX/mScaleFactor, mPosY/mScaleFactor);

        // 创建画笔
        Paint p = new Paint();

        // 绘制楼层房间
        if (floors != null && floors.size() > 0) {
            for (FloorVo f : floors) {
                if (f.getFloor() == floor) {
                    drawFloor(canvas, p, f);
                    break;
                }
            }
        }

        /*if (null != aps && aps.size() > 0) {
            // 画AP
            p.reset();
            p.setColor(apColor);
            p.setTextSize(textSize);
            for (AccessPointVo ap : aps) {
                Gps point = convertCoords(ap.getGps());
                canvas.drawCircle((int) point.getLng(), (int) point.getLat(), 8, p);
                canvas.drawText(ap.getAddress(), (int) point.getLng() + 8, (int) point.getLat() - 12, p);
            }
        }*/

        if (null != track && track.size() > 1) {
            // 画轨迹
            p.reset();
            p.setColor(lineColor);
            p.setStrokeWidth(lineWidth);
            p.setAntiAlias(true);
            p.setStyle(Paint.Style.STROKE);
            p.setPathEffect(new DashPathEffect(new float[]{6, 4}, 0));  // 设置轨迹为虚线

            Path path = new Path();
            Gps firstP = convertCoords(track.get(0).getGps());
            path.moveTo((int) firstP.getLng(), (int) firstP.getLat());
            for (int i = 1; i < track.size(); i++) {
                Gps point = convertCoords(track.get(i).getGps());
                path.lineTo((int) point.getLng(), (int) point.getLat());
                //Log.i("onDraw","lineTo:"+point.getLng()+","+point.getLat());
            }
            //path.close();
            canvas.drawPath(path, p);

            p.reset();
            p.setColor(lineColor);
            p.setStrokeWidth(2);
            p.setAntiAlias(true);
            p.setStyle(Paint.Style.STROKE);
            for (int i = 1; i < track.size(); i++) {
                Gps point = convertCoords(track.get(i).getGps());
                canvas.drawCircle((int) point.getLng(), (int) point.getLat(), 8/mScaleFactor<1?1:8/mScaleFactor, p);
            }

            // 画起始点
            p.reset();
            p.setColor(startColor);
            p.setTextSize(textSize/mScaleFactor);
            Gps start = convertCoords(track.get(0).getGps());
            Gps end = convertCoords(track.get(track.size() - 1).getGps());
            canvas.drawCircle((int) start.getLng(), (int) start.getLat(), 10/mScaleFactor<1?1:10/mScaleFactor, p);
            p.setColor(endColor);
            canvas.drawCircle((int) end.getLng(), (int) end.getLat(), 8/mScaleFactor<1?1:8/mScaleFactor, p);
            //终点时间
            canvas.drawText(formatTime(track.get(track.size() - 1).getTimestamp()), (int) end.getLng() + 6, (int) end.getLat() - 6, p);

        }

        canvas.restore();

    }

    /**
     * 绘制楼层房间
     *
     * @param canvas
     * @param p
     * @param floor
     */
    private void drawFloor(Canvas canvas, Paint p, FloorVo floor) {
        p.reset();
        p.setColor(Color.LTGRAY);
        p.setStrokeWidth(mScaleFactor>3?2:3);
        p.setStyle(Paint.Style.STROKE);

        // draw rooms
        for (DrawableVo d : floor.getDrawables()) {
            if (d.getType().equals(DrawableType.Polygon.toString())) {//多边形
                drawPolygon(canvas, p, d);
            }
        }

        // add text
        p.reset();
        p.setColor(Color.GRAY);
        p.setTextSize(7);
        for (DrawableVo d : floor.getDrawables()) {
            if (d.getType().equals(DrawableType.Polygon.toString())) {//多边形
                //List<Float> center = getCenter(d);
                //drawText(canvas, p, d.getTitle(), center.get(0) - 10, center.get(1));
                drawText(canvas, p, d);
            }
        }

    }

    /**
     * 获取房间中心点坐标
     *
     * @param drawable
     * @return
     */
    private List<Float> getCenter(DrawableVo drawable) {
        if (drawable.getData() == null) return null;

        List<Float> center = Arrays.asList(0.0f, 0.0f);
        List<List<Float>> gpsData = drawable.getData().getPath();
        if (gpsData == null || gpsData.size() < 3) return null;


        Float totalX = 0.0f;
        Float totalY = 0.0f;
        for (List<Float> g : gpsData) {
            totalX += g.get(0);
            totalY += g.get(1);
        }
        Float x = totalX / gpsData.size();
        Float y = totalY / gpsData.size();

        return Arrays.asList(x, y);
    }

    /**
     * 房间最坐标坐标
     * @param drawable
     * @return
     */
    private float getLeft(DrawableVo drawable) {
        if (drawable.getData() == null) return 0;

        List<Float> center = Arrays.asList(0.0f, 0.0f);
        List<List<Float>> gpsData = drawable.getData().getPath();
        if (gpsData == null || gpsData.size() < 3) return 0;


        float left = gpsData.get(0).get(0);
        for (List<Float> g : gpsData) {
            if (g.get(0) < left) left = g.get(0);
        }

        return left;
    }

    /**
     * 绘制多边形（房间）
     *
     * @param drawable
     */
    private void drawPolygon(Canvas canvas, Paint p, DrawableVo drawable) {
        if (drawable.getData() == null) return;

        List<List<Float>> gpsData = drawable.getData().getPath();
        if (gpsData == null || gpsData.size() < 3) return;

        Path path = new Path();
        List<Float> firstP = convertFloorCoords(gpsData.get(0));
        path.moveTo(firstP.get(0), firstP.get(1));
        for (List<Float> g : gpsData) {
            List<Float> point = convertFloorCoords(g);
            path.lineTo(point.get(0), point.get(1));
            Log.i("drawPolygon", "lineTo:" + point.get(0) + "," + point.get(1));
        }
        path.close();
        canvas.drawPath(path, p);
    }

    /**
     * 绘制文字
     *
     * @param canvas
     * @param p
     * @param content
     * @param x
     * @param y
     */
    private void drawText(Canvas canvas, Paint p, String content, float x, float y) {
        canvas.drawText(content, x, y, p);
    }

    private void drawText(Canvas canvas, Paint p, DrawableVo drawable) {
        List<Float> center = getCenter(drawable);
        float centerX = center.get(0);
        float centerY = center.get(1);
        float left = getLeft(drawable);
        float roomWidth = (centerX - left) * 2;
        float count = drawable.getTitle().length();
        float fontWidth = 100;
        float fontSize = 6;
        float startPointX = left;
        //p.setTextSize(6); //360/3=120 <=> 6

        if (count * fontWidth > roomWidth) {
            float fontScale = fontWidth/(roomWidth/count);
            fontSize = fontSize/fontScale;
            fontWidth = roomWidth / count;
            if (fontSize < 1) fontSize = 1;
            p.setColor(Color.RED);
        } else {
            p.setColor(Color.GRAY);
        }

        startPointX = centerX - fontWidth*count/4;
        if (startPointX < left) startPointX = left;

        List<Float> startPoint = convertFloorCoords(Arrays.asList(startPointX, centerY));
        p.setTextSize(fontSize);

        canvas.drawText(drawable.getTitle(), startPoint.get(0), startPoint.get(1), p);
    }

    /**
     * 根据当前屏幕尺寸转换坐标到相应比例
     *
     * @param origin
     * @return
     */
    private List<Float> convertFloorCoords(List<Float> origin) {
        int minSize = Math.min(screenHeight, screenWidth) - padding * 2;
        float s = minSize / (float) 6000;
        return Arrays.asList(origin.get(0) * s + padding * 2, origin.get(1) * s + padding);
    }

    /**
     * 根据当前屏幕尺寸转换坐标到相应比例
     *
     * @param gps
     * @return
     */
    private Gps convertCoords(Gps gps) {
        int minSize = Math.min(screenHeight, screenWidth) - padding * 2;
        double s = minSize / measure;
        return new Gps(gps.getLat() * s + padding * 2, gps.getLng() * s + padding);
    }

    /**
     * 格式化显示时间 ，如 10:08:08
     *
     * @param timestamp
     * @return
     */
    private String formatTime(long timestamp) {
        Date date = new Date();
        date.setTime(timestamp);
        return new SimpleDateFormat("HH:mm:ss").format(date);
    }

    /**
     * 手势事件处理
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Let the ScaleGestureDetector inspect all events.
        //mScaleDetector.onTouchEvent(event);

        int action = MotionEventCompat.getActionMasked(event);
        switch (action) {
            case (MotionEvent.ACTION_DOWN):
                Log.d("TouchEvent", "Action was DOWN");

                final int pointerIndex = MotionEventCompat.getActionIndex(event);
                final float x = event.getX(pointerIndex);
                final float y = event.getY(pointerIndex);

                // Remember where we started (for dragging)
                mLastTouchX = x;
                mLastTouchY = y;
                // Save the ID of this pointer (for dragging)
                mActivePointerId = MotionEventCompat.getPointerId(event, 0);

                return true;
            case (MotionEvent.ACTION_MOVE):
                Log.d("TouchEvent", "Action was MOVE");

                if (isScaling) return true;

                final int pointerIndex2 =
                        MotionEventCompat.findPointerIndex(event, mActivePointerId);

                final float x2 = event.getX(pointerIndex2);
                final float y2 = event.getY(pointerIndex2);

                // Calculate the distance moved
                final float dx = x2 - mLastTouchX;
                final float dy = y2 - mLastTouchY;

                mPosX += dx;
                mPosY += dy;

                invalidate();

                // Remember this touch position for the next move event
                mLastTouchX = x2;
                mLastTouchY = y2;

                return true;
            case (MotionEvent.ACTION_UP):
                Log.d("TouchEvent", "Action was UP");
                mActivePointerId = INVALID_POINTER_ID;
                isScaling = false;
                return true;
            case (MotionEvent.ACTION_CANCEL):
                Log.d("TouchEvent", "Action was CANCEL");
                mActivePointerId = INVALID_POINTER_ID;
                isScaling = false;
                return true;
            case (MotionEvent.ACTION_OUTSIDE):
                Log.d("TouchEvent", "Movement occurred outside bounds " +
                        "of current screen element");
                mActivePointerId = INVALID_POINTER_ID;
                isScaling = false;
                return true;
            case MotionEvent.ACTION_POINTER_UP:

                final int pointerIndex3 = MotionEventCompat.getActionIndex(event);
                final int pointerId = MotionEventCompat.getPointerId(event, pointerIndex3);

                if (pointerId == mActivePointerId) {
                    // This was our active pointer going up. Choose a new
                    // active pointer and adjust accordingly.
                    final int newPointerIndex = pointerIndex3 == 0 ? 1 : 0;
                    mLastTouchX = event.getX(pointerIndex3);
                    mLastTouchY = event.getY(pointerIndex3);
                    mActivePointerId = MotionEventCompat.getPointerId(event, newPointerIndex);
                }

                isScaling = false;

                return true;
        }

        return super.onTouchEvent(event);

    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();
            isScaling = true;

            // Don't let the object get too small or too large.
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));

            invalidate();
            return true;
        }
    }

    /**
     * 地图缩放结束事件
     */
    public interface ZoomListener {
        public void zoomFinished(float scale);
    }

    private void calcBounds() {
        if (floors.size() < 1) return;
        if (bounds == null) bounds = new Bounds();
        for (FloorVo f : floors) {
            for (DrawableVo d : f.getDrawables()) {
                for (List<Float> p : d.getData().getPath()) {
                    List<Float> point = convertFloorCoords(p);
                    float x = point.get(0);
                    float y = point.get(1);
                    if (x > bounds.right) bounds.right = x;
                    if (y > bounds.bottom) bounds.bottom = y;
                }
            }
        }
    }


    private class Bounds {
        public float top = 0;
        public float right = 0;
        public float bottom = 0;
        public float left = 0;
    }
}
