package com.kingtous.remoteaudioplayer.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.kingtous.remoteaudioplayer.R;
import com.kingtous.remoteaudioplayer.model.BluetoothDeviceModel;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class BluetoothDeviceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<BluetoothDeviceModel> deviceModels;

    public BluetoothDeviceAdapter(ArrayList<BluetoothDeviceModel> deviceModels){
        this.deviceModels=deviceModels;
    }

    //========接口============
    public interface OnItemClickListener {
        void OnClick(View view, int Position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.bluetooth_device_item, parent, false);
        return new deviceHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        ((deviceHolder) holder).name.setText(deviceModels.get(position).getName());
        ((deviceHolder) holder).mac.setText(deviceModels.get(position).getMac());
        //=======通过接口回调===========
        ((deviceHolder) holder).cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.OnClick(((deviceHolder) holder).cardView, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return deviceModels.size();
    }

    public class deviceHolder extends RecyclerView.ViewHolder {

        public CardView cardView;
        public TextView name;
        public TextView mac;

        public deviceHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name_bluetooth_device_name);
            mac = itemView.findViewById(R.id.name_bluetooth_device_mac);
            cardView = itemView.findViewById(R.id.card_BLUETOOTH);
        }
    }
}
