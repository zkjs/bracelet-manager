package com.zkjinshi.braceletmanager.Pages;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.zkjinshi.braceletmanager.R;
import com.zkjinshi.braceletmanager.base.MyApplication;
import com.zkjinshi.braceletmanager.common.utils.CacheUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 登录界面
 * Created by yejun on 11/22/16.
 * Copyright (C) 2016 qinyejun
 */

public class SigninActivity extends AppCompatActivity {

    @BindView(R.id.et_pwd)
    EditText mEtPwd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        initView();
    }

    private void initView() {

    }

    @OnClick(R.id.btn_confirm)
    public void onClick() {
        String pwd = mEtPwd.getText().toString().trim();

        if (TextUtils.isEmpty(pwd) || !CacheUtil.getInstance().getPassword().equals(pwd)) {
            Toast.makeText(this, "密码不正确", Toast.LENGTH_SHORT).show();
            return;
        }
        MyApplication.setAuthRequired(false);
        MyApplication.setAuthActivityShowing(false);
        finish();
    }

    @Override
    public void onBackPressed() {
    }
}
