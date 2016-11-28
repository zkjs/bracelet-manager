package com.zkjinshi.braceletmanager.Pages;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.zkjinshi.braceletmanager.R;
import com.zkjinshi.braceletmanager.base.BaseActivity;
import com.zkjinshi.braceletmanager.common.http.EndpointHelper;
import com.zkjinshi.braceletmanager.common.http.HttpLoadingCallback;
import com.zkjinshi.braceletmanager.common.http.OkHttpHelper;
import com.zkjinshi.braceletmanager.common.utils.DialogUtil;
import com.zkjinshi.braceletmanager.models.AccessPointVo;
import com.zkjinshi.braceletmanager.models.FloorVo;
import com.zkjinshi.braceletmanager.models.PatientVo;
import com.zkjinshi.braceletmanager.models.SOSMessage;
import com.zkjinshi.braceletmanager.models.TrackPointVo;
import com.zkjinshi.braceletmanager.response.NormalResponse;
import com.zkjinshi.braceletmanager.response.data.NormalListData;
import com.zkjinshi.braceletmanager.response.data.TrackListData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;

/**
 * 病人实时轨迹图
 * Created by yejun on 11/5/16.
 * Copyright (C) 2016 qinyejun
 */

public class TrackDrawActivity extends BaseActivity implements TrackDrawView.ZoomListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_address)
    TextView mTvAddress;
    @BindView(R.id.map)
    LinearLayout mMap;
    @BindView(R.id.btn_zoomin)
    ImageButton mBtnZoomin;
    @BindView(R.id.btn_zoomout)
    ImageButton mBtnZoomout;
    @BindView(R.id.sp_floor_switch)
    Spinner mSpFloorSwitch;

    private String bracelet;
    private List<TrackPointVo> track;
    private List<AccessPointVo> aps;
    private List<FloorVo> floors;
    private SOSMessage sos;
    private PatientVo patient;
    private String buildingID;
    private List<String> floor_data_list;
    private ArrayAdapter<String> floor_arr_adapter;

    private TrackDrawView trackView;

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

        mSpFloorSwitch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                trackView.setFloor(floors.get(position).getFloor());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * 初始化轨迹绘制地图View
     */
    private void initMapView() {
        //LinearLayout layout = (LinearLayout) findViewById(R.id.map);
        trackView = new TrackDrawView(this, getScreenSize());
        trackView.setMinimumHeight(500);
        trackView.setMinimumWidth(300);
        trackView.setZoomListener(this);
        //通知view组件重绘
        trackView.invalidate();
        mMap.addView(trackView);

        mBtnZoomout.setEnabled(false);
    }

    /**
     * 加载病人轨迹数据
     */
    private void loadTrackData() {
        if (bracelet == null || TextUtils.isEmpty(bracelet)) return;

        String url = EndpointHelper.braceletTrackCoords(bracelet);
        OkHttpHelper.getInstance().get(url, new HttpLoadingCallback<NormalResponse<TrackListData>>(this) {

            @Override
            public void onSuccess(Response response, NormalResponse<TrackListData> result) {
                if (result.getStatus().equals("ok")) {
                    track = result.getData().getList();
                    //aps = result.getData().getAps();
                    trackView.setTrack(track);
                    //trackView.setAps(aps);
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
                    trackView.setFloors(floors);
                    updateSpinner();
                } else {
                    DialogUtil.getInstance().showToast(TrackDrawActivity.this, result.getError());
                }
            }
        });
    }

    private void updateSpinner() {
        floor_data_list = new ArrayList<>();
        for (FloorVo f: floors) {
            floor_data_list.add(f.getFloor() + "楼");
        }
        floor_arr_adapter = new ArrayAdapter<>(this, R.layout.spinner_layout, floor_data_list);
        //设置样式
        mSpFloorSwitch.setAdapter(floor_arr_adapter);
        mSpFloorSwitch.setSelection(1);
    }

    /**
     * 更新轨迹
     */
    private void updateMap() {
        trackView.invalidate();
    }

    /**
     * 获取手机屏幕尺寸
     *
     * @return
     */
    private Point getScreenSize() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    @OnClick({R.id.btn_zoomin, R.id.btn_zoomout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_zoomin:
                trackView.zoomin();
                break;
            case R.id.btn_zoomout:
                trackView.zoomout();
                break;
        }
    }

    @Override
    public void zoomFinished(float scale) {
        if (scale >= trackView.getMaxScale()) {
            mBtnZoomin.setEnabled(false);
        } else {
            mBtnZoomin.setEnabled(true);
        }
        if (scale <= trackView.getMinScale()) {
            mBtnZoomout.setEnabled(false);
        } else {
            mBtnZoomout.setEnabled(true);
        }
    }


}
