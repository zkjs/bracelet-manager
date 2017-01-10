package com.zkjinshi.braceletmanager.Pages;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zkjinshi.braceletmanager.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 设置界面
 * Created by yejun on 11/3/16.
 * Copyright (C) 2016 qinyejun
 */

public class SettingFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_setting, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // load data here
    }


    @OnClick({R.id.btn_server, R.id.btn_password, R.id.btn_mqtt_testing})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btn_server:
                intent = new Intent(this.getActivity(), SettingServerActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_password:
                intent = new Intent(this.getActivity(), SettingPasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_mqtt_testing:
                intent = new Intent(this.getActivity(), MqttPubActivity.class);
                startActivity(intent);
                break;
        }
    }
}
