package com.app.sarinda.trackme.tools;

import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.sarinda.trackme.R;
import com.app.sarinda.trackme.VehicleHistoryData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chathura on 7/31/2018.
 */

public class VehicleHistoryAdapter extends RecyclerView.Adapter<VehicleHistoryAdapter.MyViewHolder> {

    private List<VehicleHistoryData> historyList;

    public VehicleHistoryAdapter(List<VehicleHistoryData> historyList) {
        this.historyList = historyList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView mSpeed;
        private TextView mDistance;
        private TextView mDate;

        public MyViewHolder(View view) {
            super(view);

            mSpeed=view.findViewById(R.id.aSpeed);
            mDistance=view.findViewById(R.id.aDistance);
            mDate=view.findViewById(R.id.mDate);
        }
    }
    @NonNull
    @Override
    public VehicleHistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.histrylistlayout,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleHistoryAdapter.MyViewHolder holder, int position) {
        VehicleHistoryData item=historyList.get(position);
            holder.mSpeed.setText(item.getSpeed());
            holder.mDistance.setText(item.getDistance());
            holder.mDate.setText(item.getDate());
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }
}
