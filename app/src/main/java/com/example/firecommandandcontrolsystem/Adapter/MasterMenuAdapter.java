package com.example.firecommandandcontrolsystem.Adapter;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firecommandandcontrolsystem.R;
import com.example.firecommandandcontrolsystem.myClass.DataApplication;

import java.util.ArrayList;
import java.util.List;


public class MasterMenuAdapter extends RecyclerView.Adapter<MasterMenuAdapter.ViewHolder> {
    private Context myContext;
    private List<String> myArrData;

    private View myview;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgView;
        TextView textView;
        LinearLayout menulayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgView = (ImageView) itemView.findViewById(R.id.menu_image);
            textView = (TextView) itemView.findViewById(R.id.menu_name);
            menulayout = (LinearLayout) itemView.findViewById(R.id.layout_mastermenu);
        }
    }

    public MasterMenuAdapter(Context context, List<String> arrData) {
        myContext = context;
        myArrData = arrData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        myview = LayoutInflater.from(parent.getContext()).inflate(R.layout.mastermenu_item, parent, false);
        ViewHolder holder = new ViewHolder(myview);


        return holder;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        String data = myArrData.get(position);


        if (data == "全局定位")
            holder.imgView.setImageResource(R.mipmap.quanjudingwei_top);
        else if (data == "搜救定位")
            holder.imgView.setImageResource(R.mipmap.shoujiudingwei_top);
        else if (data == "室内定位")
            holder.imgView.setImageResource(R.mipmap.shineidingwei_top);
        else if (data == "设备管理")
            holder.imgView.setImageResource(R.mipmap.shebeidingwei_top);
        else if (data == "人员信息")
            holder.imgView.setImageResource(R.mipmap.renyuanxinxi);
        else if (data == "系统设置")
            holder.imgView.setImageResource(R.mipmap.xitonshezhi_top);

        Resources resources = myContext.getResources();

        if (position != ((DataApplication)myContext.getApplicationContext()).mastermenuCurSelectIndex) {
            holder.menulayout.setBackground(resources.getDrawable(R.mipmap.mastermenu_select));

            holder.textView.setTextSize(22);
            ViewGroup.LayoutParams ps = holder.imgView.getLayoutParams();
            ps.height = 30;
            ps.width = 30;
            holder.imgView.setLayoutParams(ps);
        }else {
            holder.menulayout.setBackground(resources.getDrawable(R.mipmap.mastermenu));


            holder.textView.setTextSize(24);
            ViewGroup.LayoutParams ps = holder.imgView.getLayoutParams();
            ps.height = 32;
            ps.width = 32;
            holder.imgView.setLayoutParams(ps);
        }

        holder.textView.setText(data);
    }


    @Override
    public int getItemCount() {
        return myArrData.size();
    }


}
