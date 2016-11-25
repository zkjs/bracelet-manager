package com.zkjinshi.braceletmanager.Pages;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps2d.model.LatLng;
import com.zkjinshi.braceletmanager.R;
import com.zkjinshi.braceletmanager.base.BaseActivity;
import com.zkjinshi.braceletmanager.common.http.EndpointHelper;
import com.zkjinshi.braceletmanager.common.http.HttpLoadingCallback;
import com.zkjinshi.braceletmanager.common.http.OkHttpHelper;
import com.zkjinshi.braceletmanager.common.utils.DialogUtil;
import com.zkjinshi.braceletmanager.models.AccessPointVo;
import com.zkjinshi.braceletmanager.models.DrawableType;
import com.zkjinshi.braceletmanager.models.DrawableVo;
import com.zkjinshi.braceletmanager.models.FloorVo;
import com.zkjinshi.braceletmanager.models.Gps;
import com.zkjinshi.braceletmanager.models.PatientVo;
import com.zkjinshi.braceletmanager.models.SOSMessage;
import com.zkjinshi.braceletmanager.models.TrackPointVo;
import com.zkjinshi.braceletmanager.response.NormalResponse;
import com.zkjinshi.braceletmanager.response.data.NormalListData;
import com.zkjinshi.braceletmanager.response.data.TrackListData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Response;

import static android.view.MotionEvent.INVALID_POINTER_ID;

/**
 * 病人实时轨迹图
 * Created by yejun on 11/5/16.
 * Copyright (C) 2016 qinyejun
 */

public class TrackDrawActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_address)
    TextView mTvAddress;
    @BindView(R.id.map)
    LinearLayout mMap;

    private String bracelet;
    private List<TrackPointVo> track;
    private List<AccessPointVo> aps;
    private List<FloorVo> floors;
    private SOSMessage sos;
    private PatientVo patient;
    private String buildingID;

    private DrawView trackView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_draw);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TrackDrawActivity.this.finish();
            }
        });

        ButterKnife.bind(this);

        init();
        initMapView();
        loadFloorsData();
        loadTrackData();
    }

    /**
     * 接收传递过来的参数
     */
    private void init() {
        if (getIntent().getSerializableExtra("patient") != null) {
            patient = (PatientVo) getIntent().getSerializableExtra("patient");
            bracelet = patient.getBracelet();

            mTvName.setText(patient.getPatientName());
        }
        if (getIntent().getSerializableExtra("sos") != null) {
            sos = (SOSMessage) getIntent().getSerializableExtra("sos");
            bracelet = sos.getBracelet();
            mTvName.setText(sos.getMessage());
            mTvAddress.setText(sos.getAddress());
        }
        if (getIntent().getStringExtra("BuildingID") != null) {
            buildingID = getIntent().getStringExtra("BuildingID");
        }
    }

    /**
     * 初始化轨迹绘制地图View
     */
    private void initMapView() {
        //LinearLayout layout = (LinearLayout) findViewById(R.id.map);
        trackView = new DrawView(this);
        trackView.setMinimumHeight(500);
        trackView.setMinimumWidth(300);
        //通知view组件重绘
        trackView.invalidate();
        mMap.addView(trackView);

    }

    /**
     * 加载病人轨迹数据
     */
    private void loadTrackData() {
        if (bracelet == null || TextUtils.isEmpty(bracelet)) return;

        String url = EndpointHelper.braceletTrackCoords(bracelet);
        OkHttpHelper.getInstance().get(url, new HttpLoadingCallback<NormalResponse<TrackListData>>(this){

            @Override
            public void onSuccess(Response response, NormalResponse<TrackListData> result) {
                if (result.getStatus().equals("ok")) {
                    track = result.getData().getList();
                    aps = result.getData().getAps();
                    updateMap();
                } else {
                    DialogUtil.getInstance().showToast(TrackDrawActivity.this, result.getError());
                }
            }

        });
    }

    /**
     * 加载楼层数据
     */
    private void loadFloorsData() {
        if (TextUtils.isEmpty(buildingID)) return;

        String url = EndpointHelper.floorMap(buildingID);
        OkHttpHelper.getInstance().get(url, new HttpLoadingCallback<NormalResponse<NormalListData<FloorVo>>>(this) {
            @Override
            public void onSuccess(Response response, NormalResponse<NormalListData<FloorVo>> result) {
                if (result.getStatus().equals("ok")) {
                    floors = result.getData().getList();
                    updateMap();
                } else {
                    DialogUtil.getInstance().showToast(TrackDrawActivity.this, result.getError());
                }
            }
        });

    }

    /**
     * 更新轨迹
     */
    private void updateMap() {
        trackView.invalidate();
    }

    /**
     * 获取手机屏幕尺寸
     * @return
     */
    private Point getScreenSize() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }


    /**
     * 自定义轨迹绘制View
     */
    public class DrawView extends View {

        final float measure = 960;          //轨迹地图坐标基准尺寸，即通过api获取的坐标比例尺为960
        final float scale = getResources().getDisplayMetrics().density; //当前屏幕分辨率
        final int padding = 20;             // 界面留白尺寸
        final int textSize = 30;            // 界面文字大小
        final int lineColor = Color.GRAY;   // 轨迹线颜色
        final int lineWidth = 6;            // 轨迹线宽度
        final int apColor = Color.BLUE;     // AP 标志点的颜色
        final int startColor = Color.CYAN;  // 轨迹开始点颜色
        final int endColor = Color.RED;     // 轨迹结束点颜色

        int screenWidth;
        int screenHeight;

        public DrawView(Context context) {
            super(context);

            Point size = getScreenSize();
            screenWidth = size.x;
            screenHeight = size.y;
        }

        /**
         * 自定义View绘制逻辑
         * @param canvas
         */
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            Log.i("onDraw", "start drawing");
            Log.i("Scale", "scale="+scale);
            Log.i("Screen Size", "width="+screenWidth + " , height="+screenHeight);

            canvas.save();
            // TODO: need to scale the canvas according the pinching event
            canvas.scale(2,2);
            canvas.translate(mPosX,mPosY);

            // 创建画笔
            Paint p = new Paint();

            // 绘制楼层房间
            if (floors != null && floors.size() > 0) {
                // TODO: Changing floor needed
                drawFloor(canvas, p, floors.get(1));
            }

            if (null != aps && aps.size() > 0) {
                // 画AP
                p.reset();
                p.setColor(apColor);
                p.setTextSize(textSize);
                for (AccessPointVo ap : aps) {
                    Gps point = convertCoords(ap.getGps());
                    canvas.drawCircle((int) point.getLng(), (int) point.getLat(), 8, p);
                    canvas.drawText(ap.getAddress(), (int) point.getLng()+8, (int) point.getLat()-12, p);
                }
            }

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
                    canvas.drawCircle((int) point.getLng(), (int) point.getLat(), 8, p);
                }

                // 画起始点
                p.reset();
                p.setColor(startColor);
                p.setTextSize(textSize);
                Gps start = convertCoords(track.get(0).getGps());
                Gps end = convertCoords(track.get(track.size() - 1).getGps());
                canvas.drawCircle((int) start.getLng(), (int) start.getLat(), 10, p);
                p.setColor(endColor);
                canvas.drawCircle((int) end.getLng(), (int) end.getLat(), 8, p);
                //终点时间
                canvas.drawText(formatTime(track.get(track.size() - 1).getTimestamp()), (int) end.getLng()+6, (int) end.getLat()-6, p);

            }

            canvas.restore();

        }

        /**
         * 绘制楼层房间
         * @param canvas
         * @param p
         * @param floor
         */
        private void drawFloor(Canvas canvas, Paint p, FloorVo floor) {
            p.reset();
            p.setColor(Color.LTGRAY);
            p.setStrokeWidth(lineWidth);
            p.setStyle(Paint.Style.STROKE);

            // draw rooms
            for (DrawableVo d : floor.getDrawables()) {
                if (d.getType().equals(DrawableType.Polygon.toString())) {//多边形
                    drawPolygon(canvas, p , d);
                }
            }

            // add text
            p.reset();
            p.setColor(Color.GRAY);
            p.setTextSize(7);
            for (DrawableVo d : floor.getDrawables()) {
                if (d.getType().equals(DrawableType.Polygon.toString())) {//多边形
                    List<Float> center = getCenter(d);
                    //TODO: need to calculate font size to fit the room size
                    drawText(canvas, p , d.getTitle(), center.get(0) - 10, center.get(1));
                }
            }

        }

        /**
         * 获取房间中心点坐标
         * @param drawable
         * @return
         */
        private List<Float> getCenter(DrawableVo drawable) {
            if (drawable.getData() == null) return null;

            List<Float> center = Arrays.asList(0.0f, 0.0f);
            List<List<Float>> gpsData = drawable.getData().getPath();
            if(gpsData == null || gpsData.size() < 3) return null;


            Float totalX = 0.0f;
            Float totalY = 0.0f;
            for (List<Float> g : gpsData) {
                totalX += g.get(0);
                totalY += g.get(1);
            }
            Float x = totalX / gpsData.size();
            Float y = totalY / gpsData.size();

            return convertFloorCoords(Arrays.asList(x,y));
        }

        /**
         * 绘制多边形（房间）
         * @param drawable
         */
        private void drawPolygon(Canvas canvas, Paint p, DrawableVo drawable) {
            if (drawable.getData() == null) return;

            List<List<Float>> gpsData = drawable.getData().getPath();
            if(gpsData == null || gpsData.size() < 3) return;

            Path path = new Path();
            List<Float> firstP = convertFloorCoords(gpsData.get(0));
            path.moveTo(firstP.get(0), firstP.get(1));
            for (List<Float> g : gpsData) {
                List<Float> point = convertFloorCoords(g);
                path.lineTo(point.get(0), point.get(1));
                Log.i("drawPolygon","lineTo:"+point.get(0)+","+point.get(1));
            }
            path.close();
            canvas.drawPath(path, p);
        }

        /**
         * 绘制文字
         * @param canvas
         * @param p
         * @param content
         * @param x
         * @param y
         */
        private void drawText(Canvas canvas, Paint p, String content, float x, float y) {
            canvas.drawText(content, x, y, p);
        }

        /**
         * 根据当前屏幕尺寸转换坐标到相应比例
         * @param origin
         * @return
         */
        private List<Float> convertFloorCoords(List<Float> origin) {
            int minSize = Math.min(screenHeight, screenWidth) - padding*2;
            float s = minSize/(float)6000;
            return Arrays.asList(origin.get(0) * s + padding * 2, origin.get(1) * s + padding);
        }

        /**
         * 根据当前屏幕尺寸转换坐标到相应比例
         * @param gps
         * @return
         */
        private Gps convertCoords(Gps gps) {
            int minSize = Math.min(screenHeight, screenWidth) - padding*2;
            double s = minSize/measure;
            return new Gps(gps.getLat()*s + padding*2, gps.getLng()*s + padding);
        }

        /**
         * 格式化显示时间 ，如 10:08:08
         * @param timestamp
         * @return
         */
        private String formatTime(long timestamp) {
            Date date = new Date();
            date.setTime(timestamp);
            return new SimpleDateFormat("HH:mm:ss").format(date);
        }


        private float mLastTouchX;  // 手指接触屏幕最后位置X
        private float mLastTouchY;  // 手指接触屏幕最后位置Y
        private int mActivePointerId = INVALID_POINTER_ID;
        private float mPosX = 0;    // 拖拽移动距离X
        private float mPosY = 0;    // 拖拽移动距离Y

        /**
         * 手势事件处理
         * @param event
         * @return
         */
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            int action = MotionEventCompat.getActionMasked(event);
            switch(action) {
                case (MotionEvent.ACTION_DOWN):
                    Log.d("TouchEvent", "Action was DOWN");

                    final int pointerIndex = MotionEventCompat.getActionIndex(event);
                    final float x = event.getX();
                    final float y = event.getY();

                    // Remember where we started (for dragging)
                    mLastTouchX = x;
                    mLastTouchY = y;
                    // Save the ID of this pointer (for dragging)
                    mActivePointerId = MotionEventCompat.getPointerId(event, 0);

                    return true;
                case (MotionEvent.ACTION_MOVE):
                    Log.d("TouchEvent", "Action was MOVE");

                    final int pointerIndex2 =
                            MotionEventCompat.findPointerIndex(event, mActivePointerId);

                    final float x2 = event.getX();
                    final float y2 = event.getY();

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
                    return true;
                case (MotionEvent.ACTION_CANCEL):
                    Log.d("TouchEvent", "Action was CANCEL");
                    mActivePointerId = INVALID_POINTER_ID;
                    return true;
                case (MotionEvent.ACTION_OUTSIDE):
                    Log.d("TouchEvent", "Movement occurred outside bounds " +
                            "of current screen element");
                    mActivePointerId = INVALID_POINTER_ID;
                    return true;
                case MotionEvent.ACTION_POINTER_UP:

                    final int pointerIndex3 = MotionEventCompat.getActionIndex(event);
                    final int pointerId = MotionEventCompat.getPointerId(event, pointerIndex3);

                    if (pointerId == mActivePointerId) {
                        // This was our active pointer going up. Choose a new
                        // active pointer and adjust accordingly.
                        final int newPointerIndex = pointerIndex3 == 0 ? 1 : 0;
                        mLastTouchX = event.getX();
                        mLastTouchY = event.getY();
                        mActivePointerId = MotionEventCompat.getPointerId(event, newPointerIndex);
                    }
                    return true;
            }

            return super.onTouchEvent(event);

        }
    }
}
