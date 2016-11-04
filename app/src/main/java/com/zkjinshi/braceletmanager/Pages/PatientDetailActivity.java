package com.zkjinshi.braceletmanager.Pages;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zkjinshi.braceletmanager.R;
import com.zkjinshi.braceletmanager.common.http.EndpointHelper;
import com.zkjinshi.braceletmanager.common.http.HttpLoadingCallback;
import com.zkjinshi.braceletmanager.common.http.OkHttpHelper;
import com.zkjinshi.braceletmanager.common.utils.DialogUtil;
import com.zkjinshi.braceletmanager.models.PatientVo;
import com.zkjinshi.braceletmanager.response.BaseResponse;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;

/**
 * Created by yejun on 11/4/16.
 * Copyright (C) 2016 qinyejun
 */

public class PatientDetailActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_room)
    TextView mTvRoom;
    @BindView(R.id.tv_bracelet)
    TextView mTvBracelet;
    @BindView(R.id.tv_remark)
    TextView mTvRemark;
    @BindView(R.id.btn_confirm)
    Button mBtnConfirm;
    @BindView(R.id.btn_track)
    Button mBtnTrack;

    private PatientVo patient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        patient = (PatientVo) getIntent().getSerializableExtra("patient");
        initView();
    }

    private void initView() {
        String gender = patient.getPatientGender().equals("1") ? "男" : "女";
        String age = patient.getPatientAge() != null ? patient.getPatientAge() : "?";
        mTvName.setText(patient.getPatientName() + "  " + gender + "  " + age + "岁");
        mTvBracelet.setText(patient.getBracelet());
        mTvRemark.setText(patient.getPatientRemark() != null ? patient.getPatientRemark() : "");
        mTvRoom.setText(patient.getPatientRoom() != null ? patient.getPatientRoom() + "房" : "");
    }


    @OnClick(R.id.btn_confirm)
    public void onClick(View view) {
        String url = EndpointHelper.unbindBracelet(patient.getBracelet());
        HashMap<String, String> params = new HashMap<>();
        params.put("patientName", patient.getPatientName());
        params.put("patientGender", patient.getPatientGender());
        params.put("patientRoom", patient.getPatientRoom());

        OkHttpHelper.getInstance().put(url, params, new HttpLoadingCallback<BaseResponse>(this) {
            @Override
            public void onSuccess(Response response, BaseResponse result) {
                if (result.getStatus().equals("ok")) {
                    DialogUtil.getInstance().showToast(PatientDetailActivity.this, "解绑成功");
                    EventBus.getDefault().post("UnbindSuccess");
                    PatientDetailActivity.this.finish();
                } else {
                    DialogUtil.getInstance().showToast(PatientDetailActivity.this, "解绑失败:" + result.getError());
                }
            }

        });
    }

    @OnClick(R.id.btn_track)
    public void onClick() {
        Intent intent = new Intent(this, PatientTrackActivity.class);
        intent.putExtra("patient",patient);
        startActivity(intent);
    }
}
