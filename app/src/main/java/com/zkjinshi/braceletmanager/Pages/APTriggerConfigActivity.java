package com.zkjinshi.braceletmanager.Pages;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zkjinshi.braceletmanager.R;
import com.zkjinshi.braceletmanager.adapter.APTriggerListAdapter;
import com.zkjinshi.braceletmanager.base.BaseActivity;
import com.zkjinshi.braceletmanager.common.http.EndpointHelper;
import com.zkjinshi.braceletmanager.common.http.HttpLoadingCallback;
import com.zkjinshi.braceletmanager.common.http.OkHttpHelper;
import com.zkjinshi.braceletmanager.common.utils.DialogUtil;
import com.zkjinshi.braceletmanager.common.view.DividerItemDecoration;
import com.zkjinshi.braceletmanager.models.APVo;
import com.zkjinshi.braceletmanager.response.BaseResponse;
import com.zkjinshi.braceletmanager.response.NormalResponse;
import com.zkjinshi.braceletmanager.response.data.NormalListData;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Response;

/**
 * Created by yejun on 12/25/16.
 * Copyright (C) 2016 qinyejun
 */

public class APTriggerConfigActivity extends BaseActivity {
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;

    private APVo mAP;
    private APTriggerListAdapter mAdapter;
    private List<String> triggers = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ap_trigger_config);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                APTriggerConfigActivity.this.finish();
            }
        });

        ButterKnife.bind(this);

        initView();
        loadData();
    }

    private void initView() {
        mAdapter = new APTriggerListAdapter(new ArrayList<APVo>());
        mRecyclerview.setAdapter(mAdapter);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerview.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        mAdapter.setOnItemClickListener(new APTriggerListAdapter.OnItemClickListener() {
            @Override
            public void onClick(View v, int position, APVo selectedItem) {

                if (triggers.contains(selectedItem.getApid())) {
                    triggers.remove(selectedItem.getApid());
                } else {
                    triggers.add(selectedItem.getApid());
                }
            }
        });

        mAP = (APVo) getIntent().getSerializableExtra("ap");
        if (mAP.getTriggers() != null) {
            triggers = mAP.getTriggers();
            mAdapter.setTriggers(triggers);
        }
    }

    private void loadData() {
        String url = EndpointHelper.apList();
        OkHttpHelper.getInstance().get(url, new HttpLoadingCallback<NormalResponse<NormalListData<APVo>>>(this) {
            @Override
            public void onSuccess(Response response, NormalResponse<NormalListData<APVo>> data) {

                List<APVo> list = new ArrayList<>();
                if (data.getData().getList() != null) {
                    list.addAll(data.getData().getList());
                    APTriggerConfigActivity.this.mAdapter.setData(list);
                } else {
                    DialogUtil.getInstance().showToast(APTriggerConfigActivity.this, data.getStatus());
                }
            }

        });
    }

    private void submitTriggers() {

        Gson gson = new Gson();
        HashMap<String, Object> params = new HashMap<>();
        params.put("apid", mAP.getApid());
        params.put("camera", "1");
        params.put("triggerLogic", "and");
        params.put("triggers",triggers);

        Log.i("Trigger", gson.toJson(triggers));

        OkHttpHelper.getInstance().put(EndpointHelper.modifyAPTrigger(), params, new HttpLoadingCallback<BaseResponse>(this) {
            @Override
            public void onSuccess(Response response, BaseResponse result) {
                if (result.getStatus().equals("ok")) {
                    Toast.makeText(APTriggerConfigActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    EventBus.getDefault().post("APTriggerChanged");
                    APTriggerConfigActivity.this.finish();
                } else {
                    Toast.makeText(APTriggerConfigActivity.this, "修改失败，请检查网络是否畅通", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.ap_trigger_config, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            submitTriggers();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
