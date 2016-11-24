package com.zkjinshi.braceletmanager.Pages;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.zkjinshi.braceletmanager.R;
import com.zkjinshi.braceletmanager.base.BaseActivity;
import com.zkjinshi.braceletmanager.common.utils.CacheUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 修改/设置 密码
 * Created by yejun on 11/22/16.
 * Copyright (C) 2016 qinyejun
 */

public class SettingPasswordActivity extends BaseActivity {
    @BindView(R.id.et_pwd_old)
    EditText mEtPwdOld;
    @BindView(R.id.et_pwd_new)
    EditText mEtPwdNew;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_pwd);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingPasswordActivity.this.finish();
            }
        });

        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        if (TextUtils.isEmpty(CacheUtil.getInstance().getPassword())) {
            mEtPwdOld.setVisibility(View.GONE);
        } else {
            mEtPwdOld.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.btn_confirm)
    public void onClick() {
        String oldPwd = mEtPwdOld.getText().toString().trim();
        String newPwd = mEtPwdNew.getText().toString().trim();
        if (!TextUtils.isEmpty(CacheUtil.getInstance().getPassword())) {
            if (TextUtils.isEmpty(oldPwd)) {
                Toast.makeText(this, "请输入旧密码", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!CacheUtil.getInstance().getPassword().equals(oldPwd)) {
                Toast.makeText(this, "旧密码不正确", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if (TextUtils.isEmpty(newPwd)) {
            Toast.makeText(this, "请输入新密码", Toast.LENGTH_SHORT).show();
            return;
        }

        CacheUtil.getInstance().setPassword(newPwd);
        Toast.makeText(this, "密码已保存", Toast.LENGTH_SHORT).show();
        finish();
    }
}
