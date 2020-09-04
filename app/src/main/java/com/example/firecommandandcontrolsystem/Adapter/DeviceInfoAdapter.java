package com.example.firecommandandcontrolsystem.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
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
import com.example.firecommandandcontrolsystem.fragment.ShowDeviceManager;
import com.example.firecommandandcontrolsystem.myClass.DataApplication;

import java.util.List;

public class DeviceInfoAdapter extends RecyclerView.Adapter<DeviceInfoAdapter.ViewHolder> {


    private Context myContext;
    private List<ShowDeviceManager.DeviceInfo> myArrData;
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
        ImageView selcetcolor;
        LinearLayout itemlayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameView = (TextView) itemView.findViewById(R.id.name);
            idView = (TextView) itemView.findViewById(R.id.deviceId);
            groupView = (TextView) itemView.findViewById(R.id.group);
            selcetcolor = (ImageView) itemView.findViewById(R.id.trackcolor);
            itemlayout = (LinearLayout) itemView.findViewById(R.id.firemen_layout_item);

        }
    }

    public DeviceInfoAdapter(Context context, List<ShowDeviceManager.DeviceInfo> arrData) {
        myContext = context;
        myArrData = arrData;
    }

    @NonNull
    @Override
    public DeviceInfoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        myview = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_personinfo, parent, false);
        DeviceInfoAdapter.ViewHolder holder = new DeviceInfoAdapter.ViewHolder(myview);

        holder.itemlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick.onItemClick(view, (Integer) view.getTag());
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ShowDeviceManager.DeviceInfo data = myArrData.get(position);

        holder.nameView.setText(data.name);
        holder.groupView.setText(data.group);
        holder.idView.setText(String.valueOf(data.deviceid));
        switch (data.colorindex) {

            case 1:
                holder.selcetcolor.setBackgroundResource(R.drawable.shape_color1);
                break;
            case 2:
                holder.selcetcolor.setBackgroundResource(R.drawable.shape_color2);
                break;
            case 3:
                holder.selcetcolor.setBackgroundResource(R.drawable.shape_color3);
                break;
            case 4:
                holder.selcetcolor.setBackgroundResource(R.drawable.shape_color4);
                break;
            case 5:
                holder.selcetcolor.setBackgroundResource(R.drawable.shape_color5);
                break;
            case 6:
                holder.selcetcolor.setBackgroundResource(R.drawable.shape_color6);
                break;
            case 7:
                holder.selcetcolor.setBackgroundResource(R.drawable.shape_color7);
                break;
            case 8:
                holder.selcetcolor.setBackgroundResource(R.drawable.shape_color8);
                break;
            case 9:
                holder.selcetcolor.setBackgroundResource(R.drawable.shape_color9);
                break;
            case 10:
                holder.selcetcolor.setBackgroundResource(R.drawable.shape_color10);
                break;
            default:
                break;
        }


        Log.e("test","slect:"+position +","+ ((DataApplication) myContext.getApplicationContext()).deviceManagerCurSelectIndex);
        if (position == ((DataApplication) myContext.getApplicationContext()).deviceManagerCurSelectIndex) {

            holder.itemlayout.setBackgroundColor(Color.parseColor("#063859"));

        } else
            holder.itemlayout.setBackgroundColor(Color.parseColor("#022e49"));

        holder.itemlayout.setTag(position);
    }

    @Override
    public int getItemCount() {
        return myArrData.size();
    }
}
