package com.zkjinshi.braceletmanager.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zkjinshi.braceletmanager.R;
import com.zkjinshi.braceletmanager.models.APVo;
import com.zkjinshi.braceletmanager.models.BraceletVo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yejun on 12/25/16.
 * Copyright (C) 2016 qinyejun
 */

public class APListAdapter extends RecyclerView.Adapter<APListAdapter.ViewHolder> {

    private List<APVo> mData;
    private LayoutInflater inflater;
    private APListAdapter.OnItemClickListener listener;
    private Context context;

    public APListAdapter(List<APVo> data, Context context) {
        this.mData = data;
        this.context = context;
    }

    public void setOnItemClickListener(APListAdapter.OnItemClickListener listener) {
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

    @Override
    public APListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_ap, parent, false);
        return new APListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(APListAdapter.ViewHolder holder, int position) {
        APVo ap = mData.get(position);
        holder.tvName.setText(ap.getAlias() + "(" + ap.getApid() + ")");
        holder.tvLocation.setText(ap.getFloor() + "楼  " + ap.getAddress());
        holder.tvCamera.setText(ap.getCamera() == 1 ? "有摄像头" : "无摄像头");

        if (ap.getTriggers() != null && ap.getTriggers().size() > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("拍照触发AP（" + ap.getTriggerLogic() + "）: ");
            for (String t : ap.getTriggers()) {
                sb.append("\n" + t);
            }
            holder.tvTrigger.setText(sb.toString());
        } else {
            holder.tvTrigger.setText("无拍照触发AP");
        }
        if (ap.getWorking() == 1) {
            holder.tvName.setTextColor(context.getResources().getColor(R.color.ap_status_on));
        } else if (ap.getWorking() == -1) {
            holder.tvName.setTextColor(context.getResources().getColor(R.color.ap_status_off));
        } else {
            holder.tvName.setTextColor(context.getResources().getColor(R.color.ap_status_unknown));
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
        @BindView(R.id.tv_trigger)
        TextView tvTrigger;

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
        void onClick(View v, int position, APVo selectedItem);
    }
}
