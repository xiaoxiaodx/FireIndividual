package com.example.firecommandandcontrolsystem.fragment;

import android.content.SharedPreferences;
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


    EditText editText_firstFloorHeight;
    EditText editText_eachFloorHeight;
    EditText editText_fu1_floorheight;
    EditText editText_fu_eachfloorheight;
    EditText editText_curFloor;

    DataApplication dataApplication;
    public ShowFloorSet(interfaceFloorConifg floorConifg) {

        this.floorConifg = floorConifg;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        dataApplication = (DataApplication) getContext().getApplicationContext();




        View view = inflater.inflate(R.layout.fragment_floorset, container, false);

        editText_firstFloorHeight = view.findViewById(R.id.firstFloorHeight);
        editText_eachFloorHeight = view.findViewById(R.id.eachFloorHeight);
        editText_fu1_floorheight = view.findViewById(R.id.fu1_floorheight);
        editText_fu_eachfloorheight = view.findViewById(R.id.fu_eachfloorheight);
        editText_curFloor = view.findViewById(R.id.curfloor);

        dataApplication.getFloorConfig();

        editText_firstFloorHeight.setText(String.valueOf(dataApplication.firstFloorHeight));
        editText_eachFloorHeight.setText(String.valueOf(dataApplication.eachFloorHeight));
        editText_fu1_floorheight.setText(String.valueOf(dataApplication.fu1_floorheight));
        editText_fu_eachfloorheight.setText(String.valueOf(dataApplication.fu_eachfloorheight));
        editText_curFloor.setText(String.valueOf(dataApplication.curfloor));





        Button btnFoorConfig = view.findViewById(R.id.floor_set);

        btnFoorConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                float f1 = Float.valueOf(editText_firstFloorHeight.getText().toString());
                float f2 = Float.valueOf(editText_eachFloorHeight.getText().toString());
                float f3 = Float.valueOf(editText_fu1_floorheight.getText().toString());
                float f4 = Float.valueOf(editText_fu_eachfloorheight.getText().toString());
                float f5 = Float.valueOf(editText_curFloor.getText().toString());



                dataApplication.saveFloorConfig(f5,f1,f2,f3,f4);
                if (floorConifg != null) {
                    floorConifg.interfaceFloorConifg(f5,f1, f2, f3, f4);
                }
            }
        });

        return view;
    }


    public interface interfaceFloorConifg {
        void interfaceFloorConifg(float curFloorNum,float firstFloorHeight,float eachFloorHeight,float fu1_floorheight,float fu_eachfloorheight);
    }

    /**
     * 定义一个变量储存数据
     */
    private interfaceFloorConifg floorConifg;


}
