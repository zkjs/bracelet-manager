package com.zkjinshi.braceletmanager.Pages;

import android.app.Fragment;
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
import com.zkjinshi.braceletmanager.adapter.BraceletListAdapter;
import com.zkjinshi.braceletmanager.common.http.EndpointHelper;
import com.zkjinshi.braceletmanager.common.http.HttpLoadingCallback;
import com.zkjinshi.braceletmanager.common.http.OkHttpHelper;
import com.zkjinshi.braceletmanager.common.utils.DialogUtil;
import com.zkjinshi.braceletmanager.common.view.DividerItemDecoration;
import com.zkjinshi.braceletmanager.models.BraceletVo;
import com.zkjinshi.braceletmanager.response.NormalResponse;
import com.zkjinshi.braceletmanager.response.data.NormalListData;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Response;

/**
 * 手环列表
 * Created by yejun on 11/3/16.
 * Copyright (C) 2016 qinyejun
 */

public class BraceletsFragment extends Fragment {
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @BindView(R.id.refresh_layout)
    MaterialRefreshLayout mRefreshLayout;

    private BraceletListAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        mAdapter = new BraceletListAdapter(new ArrayList<BraceletVo>());
        mRecyclerview.setAdapter(mAdapter);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        mRecyclerview.addItemDecoration(new DividerItemDecoration(this.getActivity(), DividerItemDecoration.VERTICAL_LIST));

        mAdapter.setOnItemClickListener(new BraceletListAdapter.OnItemClickListener() {
            @Override
            public void onClick(View v, int position, BraceletVo selectedItem) {
                EventBus.getDefault().post(selectedItem);
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
        String url = EndpointHelper.bracelets();
        OkHttpHelper.getInstance().get(url, new HttpLoadingCallback<NormalResponse<NormalListData<BraceletVo>>>(this.getActivity()) {
            @Override
            public void onSuccess(Response response, NormalResponse<NormalListData<BraceletVo>> data) {

                List<BraceletVo> list = new ArrayList<>();
                if (data.getData().getList() != null) {
                    list.addAll(data.getData().getList());
                    BraceletsFragment.this.mAdapter.setData(list);
                }else{
                    DialogUtil.getInstance().showToast(BraceletsFragment.this.getActivity(), data.getStatus());
                }
            }

            @Override
            public void onFinish(Boolean success) {
                super.onFinish(success);
                mRefreshLayout.finishRefresh();
            }

        });
    }
}
