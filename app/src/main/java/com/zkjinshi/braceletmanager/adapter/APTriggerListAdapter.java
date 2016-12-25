package com.zkjinshi.braceletmanager.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.zkjinshi.braceletmanager.R;
import com.zkjinshi.braceletmanager.models.APVo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yejun on 12/25/16.
 * Copyright (C) 2016 qinyejun
 */

public class APTriggerListAdapter extends RecyclerView.Adapter<APTriggerListAdapter.ViewHolder> {

    private List<APVo> mData;
    private LayoutInflater inflater;
    private APTriggerListAdapter.OnItemClickListener listener;
    private List<String> triggers = new ArrayList<>();

    public APTriggerListAdapter(List<APVo> data) { this.mData = data; }

    public void setOnItemClickListener(APTriggerListAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setData(List<APVo> data) {
        if(mData == null) {
            mData = data;
        } else {
            mData.clear();
            mData.addAll(data);
        }
        notifyDataSetChanged();
    }

    public void setData(APVo data, int position) {
        mData.set(position,data);
        notifyItemChanged(position);
    }

    public void removeItem(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
    }

    public void setTriggers(List<String> triggers) {
        if (triggers == null) return;
        this.triggers.clear();
        this.triggers.addAll(triggers);
        notifyDataSetChanged();
    }

    @Override
    public APTriggerListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_ap_trigger, parent, false);
        return new APTriggerListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(APTriggerListAdapter.ViewHolder holder, int position) {
        APVo ap = mData.get(position);
        holder.tvName.setText(ap.getAlias() + "(" + ap.getApid() + ")");
        holder.tvLocation.setText(ap.getFloor() + "楼  " + ap.getAddress());
        holder.tvCamera.setText(ap.getCamera() == 1 ? "有摄像头" : "无摄像头");
        if (triggers.contains(ap.getApid())) {
            holder.cbAP.setChecked(true);
        } else {
            holder.cbAP.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        if (mData == null) { return 0;}
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_location)
        TextView tvLocation;
        @BindView(R.id.tv_camera)
        TextView tvCamera;
        @BindView(R.id.cb_ap)
        CheckBox cbAP;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    APVo ap = mData.get(getLayoutPosition());
                    if (triggers.contains(ap.getApid())) {
                        triggers.remove(ap.getApid());
                    } else {
                        triggers.add(ap.getApid());
                    }
                    notifyDataSetChanged();
                    if (listener != null) {
                        listener.onClick(view, getLayoutPosition(), ap);
                    }
                }
            });
        }
    }


    public interface OnItemClickListener{
        void onClick(View v, int position, APVo selectedItem);
    }
}
