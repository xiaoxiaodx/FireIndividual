package com.example.firecommandandcontrolsystem.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firecommandandcontrolsystem.R;
import com.example.firecommandandcontrolsystem.myClass.Firemen;

import java.math.BigDecimal;
import java.util.List;

public class FiremenDeviceAdapter extends BaseAdapter {


    private Context myContext;
    private List<Firemen> myArrData;
    @Override
    public Object getItem(int position) {
        return myArrData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = ((Activity)myContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.item_firemendeviceinfo, parent, false);
            holder = new ViewHolder();
            holder.texdeviceid = (TextView) convertView.findViewById(R.id.texdeviceid);
            holder.txtname = (TextView) convertView.findViewById(R.id.txtname);
            holder.txtfloor = (TextView) convertView.findViewById(R.id.txtfloor);
            holder.txtlat = (TextView) convertView.findViewById(R.id.txtlat);
            holder.txtlng = (TextView) convertView.findViewById(R.id.txtlng);
            holder.txtairpressheight = (TextView) convertView.findViewById(R.id.txtairpressheight);
            holder.txtlocalstate = (TextView) convertView.findViewById(R.id.txtlocalstate);
            holder.txtairpress = (TextView) convertView.findViewById(R.id.txtairpress);
            holder.txtfootbattert = (TextView) convertView.findViewById(R.id.footbattert);
            holder.txtterminalbattert = (TextView) convertView.findViewById(R.id.terminalbattert);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        Firemen data = myArrData.get(position);


      //  BigDecimal b = new BigDecimal(d);
       //  d = b.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();

        holder.texdeviceid.setText(String.valueOf(data.getBindDeviceId()));
        holder.txtname.setText(data.getName());
        holder.txtfloor.setText(String.valueOf(data.getFloor()));
        holder.txtairpress.setText(String.valueOf(data.getAirpress()));
        holder.txtairpressheight.setText(String.valueOf(data.getAirpressHeight()));

        holder.txtfootbattert.setText(String.valueOf(data.getFootBattery()));
        holder.txtterminalbattert.setText(String.valueOf(data.getTerminalBattery()));

        String latStr =  new BigDecimal(data.getCurLocation().latitude).setScale(4, BigDecimal.ROUND_HALF_UP).toString();
        String lngStr =  new BigDecimal(data.getCurLocation().longitude).setScale(4, BigDecimal.ROUND_HALF_UP).toString();
        holder.txtlat.setText(latStr);
        holder.txtlng.setText(lngStr);

        holder.txtlocalstate.setText(String.valueOf(data.getLocalState()));

        return convertView;
    }

    private View myview;
    public FiremenDeviceAdapter(Context context, List<Firemen>arrData) {
        myContext = context;
        myArrData = arrData;
    }

    @Override
    public int getCount() {
        return myArrData.size();
    }


    private class ViewHolder {
        TextView texdeviceid;
        TextView txtname;
        TextView txtfloor;
        TextView txtlat;
        TextView txtlng;
        TextView txtairpressheight;
        TextView txtlocalstate;
        TextView txtairpress;
        TextView txtterminalbattert;
        TextView txtfootbattert;

    }
}
