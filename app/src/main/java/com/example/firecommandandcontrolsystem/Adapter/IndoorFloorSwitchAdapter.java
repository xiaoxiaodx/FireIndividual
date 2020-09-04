package com.example.firecommandandcontrolsystem.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firecommandandcontrolsystem.R;
import com.example.firecommandandcontrolsystem.myClass.DataApplication;
import com.example.firecommandandcontrolsystem.myClass.Firemen;

import java.util.List;

public class IndoorFloorSwitchAdapter extends RecyclerView.Adapter<IndoorFloorSwitchAdapter.ViewHolder>{


    private View myview = null;
    private Context myContext;
    private List<String> myArrData;


    private boolean selects[] = new boolean[100];

    public static interface onItemClick {
        void onItemClick(View view, int position);
    }


    private onItemClick onItemClick;

    public void setOnItemClickListener(onItemClick listener) {
        this.onItemClick = listener;
    }

    public IndoorFloorSwitchAdapter(Context context, List<String> arrData) {
        myContext = context;
        myArrData = arrData;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        RelativeLayout layout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textview);
            layout = (RelativeLayout) itemView.findViewById(R.id.linearlayout);
            layout.setTag(-1);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        myview = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_floor_switch, parent, false);
        final ViewHolder holder = new ViewHolder(myview);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        String data = myArrData.get(position);


        if (position == ((DataApplication) myContext.getApplicationContext()).indoormap_floor_curSelectIndex)
            holder.layout.setBackgroundColor(Color.parseColor("#02e5f8"));
        else
            holder.layout.setBackgroundColor(Color.parseColor("#063859"));

        holder.textView.setText(data);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("hello test:", "点击了 :" +position);
                onItemClick.onItemClick(view, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return myArrData.size();
    }
}
