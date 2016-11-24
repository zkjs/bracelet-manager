package com.zkjinshi.braceletmanager.Pages;

import android.app.usage.UsageEvents;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.zkjinshi.braceletmanager.R;
import com.zkjinshi.braceletmanager.base.BaseActivity;
import com.zkjinshi.braceletmanager.common.http.EndpointHelper;
import com.zkjinshi.braceletmanager.common.http.HttpLoadingCallback;
import com.zkjinshi.braceletmanager.common.http.OkHttpHelper;
import com.zkjinshi.braceletmanager.common.utils.DialogUtil;
import com.zkjinshi.braceletmanager.models.BraceletVo;
import com.zkjinshi.braceletmanager.response.BaseResponse;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;

/**
 * 注册绑定病人和手环
 * Created by yejun on 11/3/16.
 * Copyright (C) 2016 qinyejun
 */

public class PatientRegisterActivity extends BaseActivity {

    @BindView(R.id.et_name)
    EditText mEtName;
    @BindView(R.id.rb_gender_m)
    RadioButton mRbGenderM;
    @BindView(R.id.rb_gender_f)
    RadioButton mRbGenderF;
    @BindView(R.id.rg_gender)
    RadioGroup mRgGender;
    @BindView(R.id.et_age)
    EditText mEtAge;
    @BindView(R.id.et_pathogenesis)
    EditText mEtPathogenesis;
    @BindView(R.id.et_room_no)
    EditText mEtRoomNo;
    @BindView(R.id.btn_bracelet)
    Button mBtnBracelet;
    @BindView(R.id.btn_confirm)
    Button mBtnConfirm;

    private String braceletSelected;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_register);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PatientRegisterActivity.this.finish();
            }
        });

        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);
    }


    @OnClick({R.id.btn_bracelet, R.id.btn_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_bracelet:
                chooseBracelet();
                break;
            case R.id.btn_confirm:
                savePatient();
                break;
        }
    }

    /**
     * 打开手环选择Activity
     */
    private void chooseBracelet() {
        Intent intent = new Intent(this, BraceletChooserActivity.class);
        startActivity(intent);
    }

    /**
     * 绑定病人和手环
     */
    private void savePatient() {
        String name = mEtName.getText().toString().trim();
        String age = mEtAge.getText().toString().trim();
        String pathogenesis = mEtPathogenesis.getText().toString().trim();
        String roomNo = mEtRoomNo.getText().toString().trim();
        String gender = "";
        if(mRbGenderF.isChecked()){
            gender = "0";
        }
        if(mRbGenderM.isChecked()){
            gender = "1";
        }
        if (TextUtils.isEmpty(name)
                || TextUtils.isEmpty(age)
                || TextUtils.isEmpty(pathogenesis)
                || TextUtils.isEmpty(roomNo)
                || TextUtils.isEmpty(gender) ) {
            Toast.makeText(this,"请将表单填写完整",Toast.LENGTH_SHORT).show();
            return;
        }
        if (braceletSelected == null || TextUtils.isEmpty(braceletSelected)) {
            Toast.makeText(this,"请选择一个手环",Toast.LENGTH_SHORT).show();
            return;
        }

        String url = EndpointHelper.bindBracelet();
        HashMap<String, String> params = new HashMap<>();
        params.put("bracelet", braceletSelected);
        params.put("patientName", name);
        params.put("patientGender", gender);
        params.put("patientRemark", pathogenesis);
        params.put("patientRoom", roomNo);
        params.put("patientAge", age);

        OkHttpHelper.getInstance().put(url, params, new HttpLoadingCallback<BaseResponse>(this) {
            @Override
            public void onSuccess(Response response, BaseResponse data) {
                if (data.getStatus().equals("ok")) {
                    DialogUtil.getInstance().showToast(PatientRegisterActivity.this, "绑定成功");
                    EventBus.getDefault().post("BindSuccess");
                    PatientRegisterActivity.this.finish();
                } else {
                    DialogUtil.getInstance().showToast(PatientRegisterActivity.this, data.getError());
                }
            }

        });

    }

    @Subscribe( threadMode = ThreadMode.MAIN)
    public void onBus(BraceletVo data){
        this.braceletSelected = data.getBracelet();
    }
}
