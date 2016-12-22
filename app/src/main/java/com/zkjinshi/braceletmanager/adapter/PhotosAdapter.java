package com.zkjinshi.braceletmanager.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.zkjinshi.braceletmanager.R;
import com.zkjinshi.braceletmanager.models.PhotoVo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yejun on 12/21/16.
 * Copyright (C) 2016 qinyejun
 */

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.ViewHolder> {

    private Context context;

    private List<PhotoVo> mData;
    private LayoutInflater inflater;
    private PhotosAdapter.OnItemClickListener listener;

    public PhotosAdapter(Context context, List<PhotoVo> data) {
        this.context = context;
        this.mData = data;
    }

    public void setOnItemClickListener(PhotosAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setData(List<PhotoVo> data) {
        if(mData == null) {
            mData = data;
        } else {
            mData.clear();
            mData.addAll(data);
        }
        notifyDataSetChanged();
    }

    public void setData(PhotoVo data, int position) {
        mData.set(position,data);
        notifyItemChanged(position);
    }

    public void removeItem(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_photo, parent, false);
        return new PhotosAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PhotoVo p = mData.get(position);
        holder.tvTimestamp.setText(p.getFormatedTime());
        //Picasso.with(context).load(p.getPath()).into(holder.ivPhoto);
        holder.ivPhoto.setImageURI(Uri.parse(p.getPath()));

    }

    @Override
    public int getItemCount() {
        if (mData == null) { return 0;}
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_timestamp)
        TextView tvTimestamp;
        @BindView(R.id.iv_photo)
        SimpleDraweeView ivPhoto;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onClick(view, getLayoutPosition(), mData.get(getLayoutPosition()));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener{
        void onClick(View v, int position, PhotoVo selectedItem);
    }
}
