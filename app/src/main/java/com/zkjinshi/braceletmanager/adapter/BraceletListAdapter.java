package com.zkjinshi.braceletmanager.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zkjinshi.braceletmanager.R;
import com.zkjinshi.braceletmanager.models.BraceletVo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yejun on 11/4/16.
 * Copyright (C) 2016 qinyejun
 */

public class BraceletListAdapter extends RecyclerView.Adapter<BraceletListAdapter.ViewHolder> {

    private List<BraceletVo> mData;
    private LayoutInflater inflater;
    private OnItemClickListener listener;

    public BraceletListAdapter(List<BraceletVo> data) { this.mData = data; }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setData(List<BraceletVo> data) {
        if(mData == null) {
            mData = data;
        } else {
            mData.clear();
            mData.addAll(data);
        }
        notifyDataSetChanged();
    }

    public void setData(BraceletVo data, int position) {
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
        View view = inflater.inflate(R.layout.item_bracelet, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BraceletVo statusVo = mData.get(position);
        holder.tvName.setText(statusVo.getName());
        holder.tvBracelet.setText(statusVo.getBracelet());
    }

    @Override
    public int getItemCount() {
        if (mData == null) { return 0;}
        return mData.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_bracelet)
        TextView tvBracelet;
        @BindView(R.id.tv_name)
        TextView tvName;

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
        void onClick(View v, int position, BraceletVo selectedItem);
    }
}
