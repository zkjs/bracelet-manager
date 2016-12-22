package com.zkjinshi.braceletmanager.Pages;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.stfalcon.frescoimageviewer.ImageViewer;
import com.zkjinshi.braceletmanager.Pages.views.ImageOverlayView;
import com.zkjinshi.braceletmanager.R;
import com.zkjinshi.braceletmanager.adapter.PhotosAdapter;
import com.zkjinshi.braceletmanager.base.BaseActivity;
import com.zkjinshi.braceletmanager.common.http.EndpointHelper;
import com.zkjinshi.braceletmanager.common.http.HttpLoadingCallback;
import com.zkjinshi.braceletmanager.common.http.OkHttpHelper;
import com.zkjinshi.braceletmanager.common.utils.DialogUtil;
import com.zkjinshi.braceletmanager.models.PatientVo;
import com.zkjinshi.braceletmanager.models.PhotoVo;
import com.zkjinshi.braceletmanager.response.NormalResponse;
import com.zkjinshi.braceletmanager.response.data.NormalListData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Response;

/**
 * Created by yejun on 12/21/16.
 * Copyright (C) 2016 qinyejun
 */

public class BraceletPhotosActivity extends BaseActivity {

    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;

    private PatientVo patient;
    private List<PhotoVo> photos;
    private PhotosAdapter mAdapter;

    private ImageOverlayView overlayView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bracelet_photos);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BraceletPhotosActivity.this.finish();
            }
        });

        ButterKnife.bind(this);

        patient = (PatientVo) getIntent().getSerializableExtra("patient");

        initView();
        loadData();
    }

    private void initView() {

        mAdapter = new PhotosAdapter(this, new ArrayList<PhotoVo>());
        mRecyclerview.setAdapter(mAdapter);
        mRecyclerview.setLayoutManager(new GridLayoutManager(this, 3));

        mAdapter.setOnItemClickListener(new PhotosAdapter.OnItemClickListener() {
            @Override
            public void onClick(View v, int position, PhotoVo selectedItem) {

                if (photos==null || photos.size()<1) return;
                List<String> images = new ArrayList<String>(photos.size());
                for (PhotoVo p : photos) {
                    images.add(p.getPath());
                }
                String[] allImages = images.toArray(new String[images.size()]);

                overlayView = new ImageOverlayView(BraceletPhotosActivity.this);
                new ImageViewer.Builder(BraceletPhotosActivity.this, allImages)
                        .setStartPosition(position)
                        .setImageMargin(BraceletPhotosActivity.this, R.dimen.image_margin)
                        .setImageChangeListener(getImageChangeListener())
                        //.setOnDismissListener(getDisissListener())
                        .setCustomDraweeHierarchyBuilder(getHierarchyBuilder())
                        .setOverlayView(overlayView)
                        .show();
            }
        });
    }

    private void loadData() {
        if (patient == null) {
            return;
        }
        String url = EndpointHelper.braceletPhotos(patient.getBracelet());
        OkHttpHelper.getInstance().get(url, new HttpLoadingCallback<NormalResponse<NormalListData<PhotoVo>>>(this) {

            @Override
            public void onSuccess(Response response, NormalResponse<NormalListData<PhotoVo>> data) {
                if (data.getStatus().equals("ok")) {
                    photos = new ArrayList<>();
                    if (data.getData().getList() != null) {
                        photos.addAll(data.getData().getList());
                        BraceletPhotosActivity.this.mAdapter.setData(photos);
                    }else{
                        DialogUtil.getInstance().showToast(BraceletPhotosActivity.this, data.getStatus());
                    }
                } else {
                    DialogUtil.getInstance().showToast(BraceletPhotosActivity.this, data.getError());
                }
            }
        });
    }

    private ImageViewer.OnImageChangeListener getImageChangeListener() {
        return new ImageViewer.OnImageChangeListener() {
            @Override
            public void onImageChange(int position) {
                PhotoVo photo = photos.get(position);
                String url = photo.getPath();
                //overlayView.setShareText(url);
                overlayView.setDescription(photo.getFormatedTime());
            }
        };
    }

    private ImageViewer.OnDismissListener getDisissListener() {
        return new ImageViewer.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        };
    }

    private GenericDraweeHierarchyBuilder getHierarchyBuilder() {
        RoundingParams roundingParams = new RoundingParams();
        roundingParams.setRoundAsCircle(true);

        return GenericDraweeHierarchyBuilder.newInstance(getResources());
//                .setRoundingParams(roundingParams);
    }

}
