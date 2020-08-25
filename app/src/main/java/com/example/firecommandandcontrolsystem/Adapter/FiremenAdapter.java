package com.example.firecommandandcontrolsystem.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firecommandandcontrolsystem.R;
import com.example.firecommandandcontrolsystem.myClass.DataApplication;
import com.example.firecommandandcontrolsystem.myClass.Firemen;

import java.util.List;

public class FiremenAdapter extends RecyclerView.Adapter<FiremenAdapter.ViewHolder> {


    private Context myContext;
    private List<Firemen> myArrData;
    private View myview;


    public static interface onItemClick {

        void onItemClick(View view, int position);
    }


    private onItemClick onItemClick;

    public void setOnItemClickListener(onItemClick listener) {
        this.onItemClick = listener;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameView;
        TextView idView;
        TextView groupView;
        LinearLayout itemlayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameView = (TextView) itemView.findViewById(R.id.name);
            idView = (TextView) itemView.findViewById(R.id.deviceId);
            groupView = (TextView) itemView.findViewById(R.id.group);
            itemlayout = (LinearLayout) itemView.findViewById(R.id.firemen_layout_item);


        }
    }
    public FiremenAdapter(Context context, List<Firemen>arrData) {
        myContext = context;
        myArrData = arrData;
    }
    @NonNull
    @Override
    public FiremenAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        myview = LayoutInflater.from(parent.getContext()).inflate(R.layout.firemeninfo_item, parent, false);
        FiremenAdapter.ViewHolder holder = new FiremenAdapter.ViewHolder(myview);

        holder.itemlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick.onItemClick( view, (Integer) view.getTag());
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FiremenAdapter.ViewHolder holder, int position) {

        Firemen data = myArrData.get(position);

        holder.nameView.setText(data.getName());
        holder.groupView.setText(data.getGroud());
        holder.idView.setText(String.valueOf(data.getBindDeviceId()));
        holder.itemlayout.setTag(position);


        if(position == ((DataApplication)myContext.getApplicationContext()).deviceManager_firmenlist_curSelectIndex){

            holder.itemlayout.setBackgroundColor(Color.parseColor("#063859"));

        }else
            holder.itemlayout.setBackgroundColor(Color.parseColor("#022e49"));
    }

    @Override
    public int getItemCount() {
        return myArrData.size();
    }
}
