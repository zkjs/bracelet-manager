package com.zkjinshi.braceletmanager.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amap.api.maps2d.model.Text;
import com.zkjinshi.braceletmanager.R;
import com.zkjinshi.braceletmanager.models.BraceletVo;
import com.zkjinshi.braceletmanager.models.PatientVo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yejun on 11/4/16.
 * Copyright (C) 2016 qinyejun
 */

public class PatientListAdapter extends RecyclerView.Adapter<PatientListAdapter.ViewHolder> {

    private List<PatientVo> mData;
    private LayoutInflater inflater;
    private PatientListAdapter.OnItemClickListener listener;

    public PatientListAdapter(List<PatientVo> data) { this.mData = data; }

    public void setOnItemClickListener(PatientListAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setData(List<PatientVo> data) {
        if(mData == null) {
            mData = data;
        } else {
            mData.clear();
            mData.addAll(data);
        }
        notifyDataSetChanged();
    }

    public void setData(PatientVo data, int position) {
        mData.set(position,data);
        notifyItemChanged(position);
    }

    public void removeItem(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public PatientListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_patient, parent, false);
        return new PatientListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PatientListAdapter.ViewHolder holder, int position) {
        PatientVo vo = mData.get(position);
        String gender = vo.getPatientGender().equals("1") ? "男" : "女";
        String age = vo.getPatientAge() != null ? vo.getPatientAge() : "?";
        holder.tvName.setText(vo.getPatientName() + "  " + gender + "  " + age + "岁");
        holder.tvBracelet.setText(vo.getBracelet());
        holder.tvRemark.setText(vo.getPatientRemark() != null ? vo.getPatientRemark() : "");
        holder.tvRoom.setText(vo.getPatientRoom() != null ? vo.getPatientRoom()+"房" : "");
        if (vo.getAttached()==1) {
            holder.tvAttached.setVisibility(View.GONE);
        } else {
            holder.tvAttached.setVisibility(View.VISIBLE);
        }
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
        @BindView(R.id.tv_remark)
        TextView tvRemark;
        @BindView(R.id.tv_room)
        TextView tvRoom;
        @BindView(R.id.tv_attached)
        TextView tvAttached;

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
        void onClick(View v, int position, PatientVo selectedItem);
    }
}
