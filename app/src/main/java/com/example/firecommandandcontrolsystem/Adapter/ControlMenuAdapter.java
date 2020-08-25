package com.example.firecommandandcontrolsystem.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.firecommandandcontrolsystem.R;
import java.util.List;

public class ControlMenuAdapter extends BaseAdapter {


    public interface onItemTouch {

        void onItemTouch(String string);
    }


    public onItemTouch onItemTouch;

//    public void setOnItemClickListener(onItemTouch listener) {
//        this.onItemTouch = listener;
//    }

    private Context myContext;
    private List<ControlMenuInfo> myArrData;
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
        final ViewHolder holder;

        final ControlMenuInfo info = myArrData.get(position);

        if (convertView == null) {
            LayoutInflater inflater = ((Activity)myContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.item_controlmenu, parent, false);
            holder = new ViewHolder();
            holder.linearLayout = (LinearLayout) convertView.findViewById(R.id.linearlayout);
            holder.imgview = (ImageView) convertView.findViewById(R.id.imageview);
            holder.txtview = (TextView) convertView.findViewById(R.id.textview);
            holder.imgview.setTag(false);
            convertView.setTag(holder);

            holder.linearLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int action = event.getAction();

                    //Log.e("ControlMenuAdapter","333 "+action+"  "+position);

                    if (action == MotionEvent.ACTION_DOWN) { // 按下
                        holder.imgview.setTag(true);
                        notifyDataSetChanged();
                        if(onItemTouch != null)
                            onItemTouch.onItemTouch(holder.txtview.getText().toString());
                        //switchBtnState(holder,true,info);
                    } else if (action == MotionEvent.ACTION_UP) { // 松开
                        holder.imgview.setTag(false);
                        notifyDataSetChanged();
                        //switchBtnState(holder,false,info);
                    }else if( action == MotionEvent.ACTION_CANCEL){
                        holder.imgview.setTag(false);
                        notifyDataSetChanged();
                        //switchBtnState(holder,false,info);
                    }

                    return true;
                }
            });

        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        //Log.e("ControlMenuAdapter"," "+position + ","+info.isPress);
        if((boolean)holder.imgview.getTag()){
            holder.txtview.setTextColor(Color.parseColor(info.txtColor_select));
            holder.imgview.setImageResource(info.imgResId_select);
            holder.linearLayout.setBackgroundResource(info.btnBgColor_select);

        }else{
            holder.txtview.setTextColor(Color.parseColor(info.txtColor));
            holder.imgview.setImageResource(info.imgResId);
            holder.linearLayout.setBackgroundColor(Color.parseColor(info.btnBgColor));
        }
        holder.txtview.setText(info.txt);

        return convertView;
    }


    private void switchBtnState(ViewHolder holder,boolean isPress,ControlMenuInfo info){

        if(isPress){

           // Log.e("ControlMenuAdapter", "press:"+position);
            holder.txtview.setTextColor(Color.parseColor(info.txtColor_select));
            holder.imgview.setImageResource(info.imgResId_select);
            holder.linearLayout.setBackgroundResource(info.btnBgColor_select);

            if(onItemTouch != null)
                onItemTouch.onItemTouch(info.txt);

        }else{

           // Log.e("ControlMenuAdapter", "release:"+position);
            holder.txtview.setTextColor(Color.parseColor(info.txtColor));
            holder.imgview.setImageResource(info.imgResId);
            holder.linearLayout.setBackgroundColor(Color.parseColor(info.btnBgColor));
        }


        holder.txtview.setText(info.txt);

    }

    public ControlMenuAdapter(Context context, List<ControlMenuInfo> arrData,onItemTouch touch) {
        myContext = context;
        myArrData = arrData;
        onItemTouch = touch;
    }

    @Override
    public int getCount() {
        return myArrData.size();
    }


    private class ViewHolder {
        LinearLayout linearLayout;
        ImageView imgview;
        TextView txtview;

    }


    public static class ControlMenuInfo{


        public ControlMenuInfo(String txt,String txtColor, String txtColor_select, int imgResId,int imgResId_select,String btnBgColor, int getBtnBgColor_select) {
            this.txt = txt;
            this.imgResId = imgResId;
            this.btnBgColor = btnBgColor;
            this.txtColor = txtColor;
            this.imgResId_select = imgResId_select;
            this.btnBgColor_select = getBtnBgColor_select;
            this.txtColor_select = txtColor_select;
        }

        public boolean isPress = false;
        public String txt;
        //normal
        public int imgResId;
        public String btnBgColor;
        public String txtColor;
        //select
        public int imgResId_select;
        public int btnBgColor_select;
        public String txtColor_select;
    }
}

