package com.example.firecommandandcontrolsystem.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.firecommandandcontrolsystem.R;
import com.example.firecommandandcontrolsystem.myClass.DataApplication;
import com.example.firecommandandcontrolsystem.myClass.Firemen;

import java.util.List;

public class FiremenRetreatAdapter extends BaseAdapter {


    private Context myContext;
    private List<Firemen> myArrData;
    private onItemClick onItemClick;


    public static interface onItemClick {

        void onItemClick(View view, int position);
    }


    public void setOnItemClickListener(onItemClick listener) {
        onItemClick = listener;
    }

    public FiremenRetreatAdapter(Context context, List<Firemen>arrData) {
        myContext = context;
        myArrData = arrData;
    }

    @Override
    public int getCount() {
        return  myArrData.size();
    }

    @Override
    public Object getItem(int position) {
        return myArrData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        FiremenRetreatAdapter.ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = ((Activity)myContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.item_retreat, parent, false);
            holder = new ViewHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.textview);
            holder.textView.setTag(false);  // 是否被选中的标志位  设置到TAG
            holder.linearLayout = (LinearLayout) convertView.findViewById(R.id.linearlayout);
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClick.onItemClick( view, position);
                }
            });

            convertView.setTag(holder);
        } else {

            holder = (FiremenRetreatAdapter.ViewHolder) convertView.getTag();
        }


        if(position == ((DataApplication) myContext.getApplicationContext()).firemenInfo_retreat_curSelectIndex){

            if((boolean)holder.textView.getTag()){

                holder.textView.setTag(false);
                holder.linearLayout.setBackgroundColor(Color.parseColor("#063859"));
            }else{

                holder.textView.setTag(true);
                holder.linearLayout.setBackgroundColor(Color.parseColor("#02e5f8"));
            }
        }


        Firemen item = myArrData.get(position);
        //holder.linearLayout.setTag((Integer)position);
        holder.textView.setText(String.valueOf(item.getBindDeviceId()));
        return convertView;
    }

    private class ViewHolder {
        TextView textView;
        LinearLayout linearLayout;

    }
}
