package com.zkjinshi.braceletmanager.Pages;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.Polygon;
import com.amap.api.maps2d.model.PolygonOptions;
import com.amap.api.maps2d.model.Polyline;
import com.amap.api.maps2d.model.PolylineOptions;
import com.zkjinshi.braceletmanager.R;
import com.zkjinshi.braceletmanager.base.BaseActivity;
import com.zkjinshi.braceletmanager.common.http.EndpointHelper;
import com.zkjinshi.braceletmanager.common.http.HttpLoadingCallback;
import com.zkjinshi.braceletmanager.common.http.OkHttpHelper;
import com.zkjinshi.braceletmanager.common.utils.DialogUtil;
import com.zkjinshi.braceletmanager.models.BuildingVo;
import com.zkjinshi.braceletmanager.models.DrawableType;
import com.zkjinshi.braceletmanager.models.DrawableVo;
import com.zkjinshi.braceletmanager.models.Gps;
import com.zkjinshi.braceletmanager.models.PatientVo;
import com.zkjinshi.braceletmanager.models.SOSMessage;
import com.zkjinshi.braceletmanager.models.TrackPointVo;
import com.zkjinshi.braceletmanager.response.NormalResponse;
import com.zkjinshi.braceletmanager.response.data.NormalListData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Response;

/**
 * 在高德地图上显示病人位置信息
 * Created by yejun on 11/4/16.
 * Copyright (C) 2016 qinyejun
 */

public class PatientTrackActivity extends BaseActivity implements AMap.InfoWindowAdapter,
        AMap.OnInfoWindowClickListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_address)
    TextView mTvAddress;

    private AMap aMap;
    private MapView mapView;
    private Polyline polyline;

    private String bracelet;
    private List<TrackPointVo> track;
    private SOSMessage sos;
    private PatientVo patient;
    private List<BuildingVo> buildings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_track);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PatientTrackActivity.this.finish();
            }
        });

        ButterKnife.bind(this);

        initView();

        mapView = (MapView) findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，实现地图生命周期管理
        mapView.onCreate(savedInstanceState);
        aMap = mapView.getMap();
        aMap.setInfoWindowAdapter(this);
        aMap.setOnInfoWindowClickListener(this);

        //loadTrackData();
        loadBuildingData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，实现地图生命周期管理
        mapView.onSaveInstanceState(outState);
    }

    private void initView() {
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

    /**
     * 加载病人轨迹数据
     */
    private void loadTrackData() {
        if (bracelet == null || TextUtils.isEmpty(bracelet)) {
            return;
        }
        String url = EndpointHelper.braceletTrack(bracelet);
        OkHttpHelper.getInstance().get(url, new HttpLoadingCallback<NormalResponse<NormalListData<TrackPointVo>>>(this){

            @Override
            public void onSuccess(Response response, NormalResponse<NormalListData<TrackPointVo>> result) {
                if (result.getStatus().equals("ok")) {
                    track = result.getData().getList();
                    updateMapTrack();
                } else {
                    DialogUtil.getInstance().showToast(PatientTrackActivity.this, result.getError());
                }
            }
        });
    }

    /**
     * 加载建筑物绘制数据
     */
    private void loadBuildingData() {
        String url = EndpointHelper.buildingMap();
        OkHttpHelper.getInstance().get(url, new HttpLoadingCallback<NormalResponse<NormalListData<BuildingVo>>>(this) {
            @Override
            public void onSuccess(Response response, NormalResponse<NormalListData<BuildingVo>> result) {
                if (result.getStatus().equals("ok")) {
                    buildings = result.getData().getList();
                    updateMapBuildings();
                } else {
                    DialogUtil.getInstance().showToast(PatientTrackActivity.this, result.getError());
                }
            }
        });
    }

    /**
     * 更新地图上病人轨迹
     */
    private void updateMapTrack() {
        if (track == null || track.size() == 0) {
            return;
        }
        List<LatLng> latLngs = new ArrayList<LatLng>();
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        for (TrackPointVo p : track) {
            latLngs.add(new LatLng(p.getGps().getLat(), p.getGps().getLng()));
            boundsBuilder.include(new LatLng(p.getGps().getLat(), p.getGps().getLng()));
        }
        polyline = aMap.addPolyline(new PolylineOptions().addAll(latLngs).width(8).color(Color.GREEN));

        aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 10));

        MarkerOptions markerOption = new MarkerOptions().anchor(0.5f, 0.5f)
                .position(latLngs.get(latLngs.size()-1));
        if (sos != null) {
            markerOption.title(sos.getMessage());
            markerOption.snippet(sos.getAddress());
        } else if ( patient != null ) {
            markerOption.title(patient.getPatientName());
        }

        aMap.addMarker(markerOption).showInfoWindow();
    }

    /**
     * 更新地图上医院建筑
     */
    private void updateMapBuildings() {
        if (buildings == null || buildings.size() == 0) {
            return;
        }
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();

        for (BuildingVo b : buildings) {
            drawBuilding(b);
            boundsBuilder.include(new LatLng(b.getLat(), b.getLng()));
        }

        aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 10));
    }

    /**
     * 在地图上绘制建筑物图层
     * @param building
     */
    private void drawBuilding(BuildingVo building) {
        if (building.getDrawables().size() <= 0)  return;

        for (DrawableVo d: building.getDrawables()) {
            if (d.getType().equals(DrawableType.Polygon.toString())) {//多边形
                drawPolygon(d);
            }
        }

        MarkerOptions markerOption = new MarkerOptions().anchor(0.5f, 0.5f)
                .position(new LatLng(building.getLat(), building.getLng()));

        if (sos != null) {
            markerOption.title(sos.getMessage());
            markerOption.snippet(sos.getAddress());
        } else if ( patient != null ) {
            markerOption.title(patient.getPatientName());
        } else {
            markerOption.title(building.getTitle());
            markerOption.snippet(building.getTitle());
        }

        aMap.addMarker(markerOption).showInfoWindow();
    }

    /**
     * 绘制多边形
     * @param drawable
     */
    private void drawPolygon(DrawableVo drawable) {
        if (drawable.getData() == null) return;

        List<List<Float>> gpsData = drawable.getData().getPath();
        if(gpsData == null || gpsData.size() < 3) return;

        List<LatLng> path = new ArrayList<>();
        for (List<Float> g : gpsData) {
            if (g.size()<2) continue;
            path.add(new LatLng(g.get(1), g.get(0)));
        }

        aMap.addPolygon(new PolygonOptions().addAll(path)
                .fillColor(Color.argb(75, 204, 227, 232))
                .strokeColor(Color.argb(90, 142, 157, 161))
                .strokeWidth(2) );

    }



    /**
     * 监听自定义infowindow窗口的infocontents事件回调
     */
    @Override
    public View getInfoContents(Marker marker) {
        View infoContent = getLayoutInflater().inflate(
                R.layout.custom_info_contents, null);
        render(marker, infoContent);
        return infoContent;
    }

    /**
     * 监听自定义infowindow窗口的infowindow事件回调
     */
    @Override
    public View getInfoWindow(Marker marker) {
        View infoWindow = getLayoutInflater().inflate(
                R.layout.custom_info_window, null);

        render(marker, infoWindow);
        return infoWindow;
    }

    /**
     * 自定义infowinfow窗口
     */
    public void render(Marker marker, View view) {
        String title = marker.getTitle();
        TextView titleUi = ((TextView) view.findViewById(R.id.title));
        if (title != null) {
            SpannableString titleText = new SpannableString(title);
            titleText.setSpan(new ForegroundColorSpan(Color.RED), 0, titleText.length(), 0);
            //titleUi.setTextSize(15);
            titleUi.setText(titleText);

        } else {
            titleUi.setText("");
        }
        String snippet = marker.getSnippet();
        TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
        if (snippet != null) {
            SpannableString snippetText = new SpannableString(snippet);
            //snippetText.setSpan(new ForegroundColorSpan(Color.GREEN), 0, snippetText.length(), 0);
            //snippetUi.setTextSize(20);
            snippetUi.setText(snippetText);
        } else {
            snippetUi.setText("");
        }
    }

    /**
     * 查看病人实时轨迹
     * @param marker
     */
    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent intent = new Intent(this, TrackDrawActivity.class);
        if (patient != null) {
            intent.putExtra("patient", patient);
        }
        if (sos != null) {
            intent.putExtra("sos", sos);
        }
        if (buildings != null && buildings.size() > 0) {
            intent.putExtra("BuildingID", buildings.get(0).getId());
        }
        startActivity(intent);
    }
}
