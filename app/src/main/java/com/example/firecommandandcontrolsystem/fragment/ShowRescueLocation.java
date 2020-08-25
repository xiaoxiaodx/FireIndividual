package com.example.firecommandandcontrolsystem.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.firecommandandcontrolsystem.R;
import com.example.firecommandandcontrolsystem.myClass.MyMapView;

public class ShowRescueLocation extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_rescueloction,container,false);


        return view;
    }
}
