package com.example.firecommandandcontrolsystem.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firecommandandcontrolsystem.Adapter.FiremenAdapter;
import com.example.firecommandandcontrolsystem.Adapter.MasterMenuAdapter;
import com.example.firecommandandcontrolsystem.MainActivity;
import com.example.firecommandandcontrolsystem.R;
import com.example.firecommandandcontrolsystem.myClass.DataApplication;
import com.example.firecommandandcontrolsystem.myClass.MyMapView;

public class ShowDeviceManager extends Fragment{


    RecyclerView firemenlistview;
    FiremenAdapter firemenAdapter;



    public ShowDeviceManager(MainActivity listener){


    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_devicemanager,container,false);

        firemenlistview = view.findViewById(R.id.firemenlistview);


        firemenAdapter = new FiremenAdapter(getContext(),((DataApplication)getContext().getApplicationContext()).listFiremen);



        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        firemenlistview.setLayoutManager(layoutManager);
        firemenlistview.setAdapter(firemenAdapter);

        firemenlistview.setOverScrollMode(View.OVER_SCROLL_NEVER);



        firemenAdapter.setOnItemClickListener(new FiremenAdapter.onItemClick() {
            @Override
            public void onItemClick(View view, int position) {

                DataApplication dataApplication = (DataApplication) view.getContext().getApplicationContext();
                dataApplication.deviceManager_firmenlist_curSelectIndex = (Integer) view.getTag();

            }


        });




        return view;
    }


}
