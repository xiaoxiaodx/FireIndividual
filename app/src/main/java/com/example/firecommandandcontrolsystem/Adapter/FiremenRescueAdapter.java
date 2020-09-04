package com.example.firecommandandcontrolsystem.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
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

public class FiremenRescueAdapter extends RecyclerView.Adapter<FiremenRescueAdapter.ViewHolder> {


    private View myview = null;
    private Context myContext;
    private List<Firemen> myArrData;


    private boolean selects[] = new boolean[100];

    public static interface onItemClick {
        void onItemClick(View view, int position);
    }


    private FiremenRescueAdapter.onItemClick onItemClick;

    public void setOnItemClickListener(FiremenRescueAdapter.onItemClick listener) {
        this.onItemClick = listener;
    }

    public FiremenRescueAdapter(Context context, List<Firemen> arrData) {
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

            linearlayout = (LinearLayout) itemView.findViewById(R.id.linearlayout);
            linearlayout.setTag(-1);
        }
    }

    @NonNull
    @Override
    public FiremenRescueAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        myview = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_retreat, parent, false);
        final ViewHolder holder = new ViewHolder(myview);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FiremenRescueAdapter.ViewHolder holder, final int position) {

        Firemen data = myArrData.get(position);


        if (position == ((DataApplication) myContext.getApplicationContext()).firemenInfo_rescue_curSelectIndex) {
            if (data.isSelectRescue())
                data.setSelectRescue(false);
             else
                data.setSelectRescue(true);
        }

        if (data.isSelectRescue())
            holder.linearlayout.setBackgroundColor(Color.parseColor("#02e5f8"));
         else
            holder.linearlayout.setBackgroundColor(Color.parseColor("#063859"));
        holder.textView.setText(String.valueOf(data.getBindDeviceId()));

        holder.linearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("hello test:", "点击了 :" +position+",");
                onItemClick.onItemClick(view, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return myArrData.size();
    }
}
