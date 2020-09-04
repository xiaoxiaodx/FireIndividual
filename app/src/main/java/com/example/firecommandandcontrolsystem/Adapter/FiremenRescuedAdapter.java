package com.example.firecommandandcontrolsystem.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firecommandandcontrolsystem.R;
import com.example.firecommandandcontrolsystem.myClass.DataApplication;
import com.example.firecommandandcontrolsystem.myClass.Firemen;

import java.util.List;

public class FiremenRescuedAdapter extends RecyclerView.Adapter<FiremenRescuedAdapter.ViewHolder> {


    private View myview;
    private Context myContext;
    private List<Firemen> myArrData;

    public static interface onItemClick {

        void onItemClick(View view, int position);
    }


    private FiremenRescuedAdapter.onItemClick onItemClick;

    public void setOnItemClickListener(FiremenRescuedAdapter.onItemClick listener) {
        this.onItemClick = listener;
    }

    public FiremenRescuedAdapter(Context context, List<Firemen> arrData) {
        myContext = context;
        myArrData = arrData;

    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgView;
        TextView textView;
        LinearLayout linearlayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgView = (ImageView) itemView.findViewById(R.id.imageview);
            textView = (TextView) itemView.findViewById(R.id.textview);
            textView.setTag(false);
            linearlayout = (LinearLayout) itemView.findViewById(R.id.linearlayout);
        }
    }

    @NonNull
    @Override
    public FiremenRescuedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        myview = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_retreat, parent, false);
        ViewHolder holder = new ViewHolder(myview);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FiremenRescuedAdapter.ViewHolder holder, final int position) {
        Firemen data = myArrData.get(position);

        if (position == ((DataApplication) myContext.getApplicationContext()).firemenInfo_rescued_curSelectIndex) {
            if (data.isSelectRescued())
                data.setSelectRescued(false);
            else
                data.setSelectRescued(true);
        }

        if (data.isSelectRescued())
            holder.linearlayout.setBackgroundColor(Color.parseColor("#02e5f8"));
        else
            holder.linearlayout.setBackgroundColor(Color.parseColor("#063859"));
        holder.textView.setText(String.valueOf(data.getBindDeviceId()));

        holder.linearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick.onItemClick(view, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return myArrData.size();
    }
}
