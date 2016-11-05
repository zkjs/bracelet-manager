package com.zkjinshi.braceletmanager.Pages;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zkjinshi.braceletmanager.R;
import com.zkjinshi.braceletmanager.common.http.EndpointHelper;
import com.zkjinshi.braceletmanager.common.http.HttpLoadingCallback;
import com.zkjinshi.braceletmanager.common.http.OkHttpHelper;
import com.zkjinshi.braceletmanager.common.utils.DialogUtil;
import com.zkjinshi.braceletmanager.models.AccessPointVo;
import com.zkjinshi.braceletmanager.models.Gps;
import com.zkjinshi.braceletmanager.models.PatientVo;
import com.zkjinshi.braceletmanager.models.SOSMessage;
import com.zkjinshi.braceletmanager.models.TrackPointVo;
import com.zkjinshi.braceletmanager.response.NormalResponse;
import com.zkjinshi.braceletmanager.response.data.TrackListData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Response;

/**
 * Created by yejun on 11/5/16.
 * Copyright (C) 2016 qinyejun
 */

public class TrackDrawActivity extends AppCompatActivity {

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
    private SOSMessage sos;
    private PatientVo patient;

    private DrawView trackView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_draw);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        init();
        initMapView();
        loadData();
    }

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
    }

    private void initMapView() {
        //LinearLayout layout = (LinearLayout) findViewById(R.id.map);
        trackView = new DrawView(this);
        trackView.setMinimumHeight(500);
        trackView.setMinimumWidth(300);
        //通知view组件重绘
        trackView.invalidate();
        mMap.addView(trackView);

    }

    private void loadData() {
        if (bracelet == null || TextUtils.isEmpty(bracelet)) {
            return;
        }
        String url = EndpointHelper.braceletTrackCoords(bracelet);
        OkHttpHelper.getInstance().get(url, new HttpLoadingCallback<NormalResponse<TrackListData>>(this){

            @Override
            public void onSuccess(Response response, NormalResponse<TrackListData> result) {
                if (result.getStatus().equals("ok")) {
                    track = result.getData().getList();
                    aps = result.getData().getAps();
                    updateTrack();
                } else {
                    DialogUtil.getInstance().showToast(TrackDrawActivity.this, result.getError());
                }
            }

        });
    }

    private void updateTrack() {
        trackView.invalidate();
    }

    private Point getScreenSize() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }


    public class DrawView extends View {

        final float measure = 960;
        final float scale = getResources().getDisplayMetrics().density;
        final int padding = 20;
        final int textSize = 30;
        final int textColor = Color.BLUE;
        final int lineColor = Color.GRAY;
        final int lineWidth = 6;
        final int apColor = Color.BLUE;
        final int startColor = Color.RED;
        final int endColor = Color.GREEN;

        int screenWidth;
        int screenHeight;

        public DrawView(Context context) {
            super(context);

            Point size = getScreenSize();
            screenWidth = size.x;
            screenHeight = size.y;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            Log.i("onDraw", "start drawing");
            Log.i("Scale", "scale="+scale);
            Log.i("Screen Size", "width="+screenWidth + " , height="+screenHeight);

            // 创建画笔
            Paint p = new Paint();
            p.setColor(lineColor);
            p.setStrokeWidth(lineWidth);
            p.setAntiAlias(true);
            p.setStyle(Paint.Style.STROKE);

            if (null != track && track.size() > 1) {
                // 画轨迹
                Path path = new Path();
                Gps firstP = convertCoords(track.get(0).getGps());
                path.moveTo((int) firstP.getLng(), (int) firstP.getLat());
                for (int i = 1; i < track.size(); i++) {
                    Gps point = convertCoords(track.get(i).getGps());
                    path.lineTo((int) point.getLng(), (int) point.getLat());
                    Log.i("onDraw","lineTo:"+point.getLng()+","+point.getLat());
                }
                //path.close();
                canvas.drawPath(path, p);

                // 画起始点
                p.reset();
                p.setColor(startColor);
                p.setTextSize(textSize);
                Gps start = convertCoords(track.get(0).getGps());
                Gps end = convertCoords(track.get(track.size() - 1).getGps());
                canvas.drawCircle((int) start.getLng(), (int) start.getLat(), 8, p);
                p.setColor(endColor);
                canvas.drawCircle((int) end.getLng(), (int) end.getLat(), 8, p);
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

        }


        private Gps convertCoords(Gps gps) {
            int minSize = Math.min(screenHeight, screenWidth) - padding*2;
            double s = minSize/measure;
            return new Gps(gps.getLat()*s + padding*2, gps.getLng()*s + padding);
        }
    }
}
