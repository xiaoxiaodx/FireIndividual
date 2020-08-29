package com.example.firecommandandcontrolsystem.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.firecommandandcontrolsystem.Adapter.FiremenDeviceAdapter;
import com.example.firecommandandcontrolsystem.R;
import com.example.firecommandandcontrolsystem.myClass.DataApplication;

public class ShowFiremenInfo extends Fragment {


    GridView firemenGirdview;
    FiremenDeviceAdapter firemenDeviceAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_firemendeivceinfo,container,false);

        firemenGirdview  = view.findViewById(R.id.firemen_gridview);

        firemenDeviceAdapter = new FiremenDeviceAdapter(getContext(),((DataApplication)getContext().getApplicationContext()).listFiremen);

        firemenGirdview.setAdapter(firemenDeviceAdapter);

        firemenGirdview.setOverScrollMode(View.OVER_SCROLL_NEVER);

        return view;
    }







}
