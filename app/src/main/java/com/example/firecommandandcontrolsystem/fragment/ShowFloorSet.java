package com.example.firecommandandcontrolsystem.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.firecommandandcontrolsystem.R;
import com.example.firecommandandcontrolsystem.myClass.DataApplication;

public class ShowFloorSet extends Fragment {


    public ShowFloorSet(interfaceFloorConifg floorConifg){

        this.floorConifg = floorConifg;
    }
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        final DataApplication dataApplication = (DataApplication)getContext().getApplicationContext();

        View view = inflater.inflate(R.layout.fragment_floorset,container,false);

        final EditText editTextTopFirstHeight = view.findViewById(R.id.topfirst_height);
        final EditText editTextTopHeight = view.findViewById(R.id.top_height);
        final EditText editTextLowFirstHeight = view.findViewById(R.id.lowfirst_height);
        final EditText editTextLowHeight = view.findViewById(R.id.low_height);


        editTextTopFirstHeight.setText(String.valueOf(dataApplication.topFirstHeight));
        editTextTopHeight.setText(String.valueOf(dataApplication.topHeight));
        editTextLowFirstHeight.setText(String.valueOf(dataApplication.lowFirstHeight));
        editTextLowHeight.setText(String.valueOf(dataApplication.lowHeight));


        Button btnFoorConfig = view.findViewById(R.id.floor_set);

        btnFoorConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DataApplication dataApplication1 = (DataApplication)getContext().getApplicationContext();

                 float f1 = Float.valueOf(editTextTopFirstHeight.getText().toString());
                 float f2 = Float.valueOf(editTextTopHeight.getText().toString());
                 float f3 = Float.valueOf(editTextLowFirstHeight.getText().toString());
                 float f4 = Float.valueOf(editTextLowHeight.getText().toString());

                dataApplication1.topFirstHeight =  f1;
                dataApplication1.topHeight = f2;
                dataApplication1.lowFirstHeight = f3;
                dataApplication1.lowHeight = f4;
                if(floorConifg != null){
                    floorConifg.interfaceFloorConifg(f1,f2,f3,f4);
                }
            }
        });

        return view;
    }
    public interface  interfaceFloorConifg{
        void interfaceFloorConifg(float topfirstHeight,float topheight,float lowfirstheight,float lowheight);
    }
    /**
     *定义一个变量储存数据
     */
    private interfaceFloorConifg floorConifg;


}
