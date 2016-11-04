package com.zkjinshi.braceletmanager.Pages;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.zkjinshi.braceletmanager.R;
import com.zkjinshi.braceletmanager.adapter.PatientListAdapter;
import com.zkjinshi.braceletmanager.common.http.EndpointHelper;
import com.zkjinshi.braceletmanager.common.http.HttpLoadingCallback;
import com.zkjinshi.braceletmanager.common.http.OkHttpHelper;
import com.zkjinshi.braceletmanager.common.utils.DialogUtil;
import com.zkjinshi.braceletmanager.common.view.DividerItemDecoration;
import com.zkjinshi.braceletmanager.models.PatientVo;
import com.zkjinshi.braceletmanager.response.NormalResponse;
import com.zkjinshi.braceletmanager.response.data.NormalListData;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Response;

/**
 * Created by yejun on 11/3/16.
 * Copyright (C) 2016 qinyejun
 */

public class PatientsFragment extends Fragment {

    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @BindView(R.id.refresh_layout)
    MaterialRefreshLayout mRefreshLayout;

    private PatientListAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bracelets, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initRefreshLayout();
        loadData();
    }

    private void initView() {

        mAdapter = new PatientListAdapter(new ArrayList<PatientVo>());
        mRecyclerview.setAdapter(mAdapter);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        mRecyclerview.addItemDecoration(new DividerItemDecoration(this.getActivity(), DividerItemDecoration.VERTICAL_LIST));

        mAdapter.setOnItemClickListener(new PatientListAdapter.OnItemClickListener() {
            @Override
            public void onClick(View v, int position, PatientVo selectedItem) {
                //EventBus.getDefault().post(selectedItem);
                Intent intent = new Intent(PatientsFragment.this.getActivity(), PatientDetailActivity.class);
                intent.putExtra("patient",selectedItem);
                startActivity(intent);
            }
        });
    }

    private void initRefreshLayout(){
        mRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                loadData();
            }
        });
    }

    private void loadData() {
        String url = EndpointHelper.patients();
        OkHttpHelper.getInstance().get(url, new HttpLoadingCallback<NormalResponse<NormalListData<PatientVo>>>(this.getActivity()) {
            @Override
            public void onSuccess(Response response, NormalResponse<NormalListData<PatientVo>> data) {

                List<PatientVo> list = new ArrayList<>();
                if (data.getData().getList() != null) {
                    list.addAll(data.getData().getList());
                    PatientsFragment.this.mAdapter.setData(list);
                    if (data.getData().getList().size() == 0) {
                        DialogUtil.getInstance().showToast(PatientsFragment.this.getActivity(), "没有绑定病人手环数据，点击右下角加号按钮添加病人");
                    }
                }else{
                    DialogUtil.getInstance().showToast(PatientsFragment.this.getActivity(), data.getStatus());
                }
            }

            @Override
            public void onFinish(Boolean success) {
                super.onFinish(success);
                mRefreshLayout.finishRefresh();
            }

        });
    }

    @Subscribe( threadMode = ThreadMode.MAIN)
    public void onBus(String result){
        if (result.equals("BindSuccess") || result.equals("UnbindSuccess")) {
            loadData();
        }
    }
}
