package com.example.firecommandandcontrolsystem.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firecommandandcontrolsystem.Adapter.IndoorFloorSwitchAdapter;
import com.example.firecommandandcontrolsystem.MainActivity;
import com.example.firecommandandcontrolsystem.R;
import com.example.firecommandandcontrolsystem.myClass.DataApplication;
import com.example.firecommandandcontrolsystem.myClass.IndoorMap;

import java.util.ArrayList;
import java.util.List;

public class ShowIndoorMap extends Fragment {


    IndoorMap indoorView;
    RecyclerView listfloor;
    List<String> floorinfo = new ArrayList<>();

    public ShowIndoorMap(MainActivity listern) {
        listern.setIndoorMapListener(new MainActivity.IndoorMapinterface() {
            @Override
            public void notifyMapUpdate() {
                if (indoorView != null) {
                    indoorView.update();
                   // Log.e("test", "ShowIndoorMap update");
                }
            }
        });
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_indoormap, container, false);

        indoorView = (IndoorMap) view.findViewById(R.id.indoormap);

        listfloor = view.findViewById(R.id.list_floor);


        floorinfo.add("F6");
        floorinfo.add("F5");
        floorinfo.add("F4");
        floorinfo.add("F3");
        floorinfo.add("F2");
        floorinfo.add("F1");
        floorinfo.add("负一");
        floorinfo.add("负二");

        final IndoorFloorSwitchAdapter indoorFloorSwitchAdapter = new IndoorFloorSwitchAdapter(getContext(), floorinfo);

        indoorFloorSwitchAdapter.setOnItemClickListener(new IndoorFloorSwitchAdapter.onItemClick() {
            @Override
            public void onItemClick(View view, int position) {
                DataApplication dataApplication = (DataApplication) getContext().getApplicationContext();

                dataApplication.indoormap_floor_curSelectIndex = position;

                if (indoorView != null) {

                    int floor = 0;
                    switch (position) {

                        case 0:
                            floor = 6;
                            break;
                        case 1:
                            floor = 5;
                            break;
                        case 2:
                            floor = 4;
                            break;
                        case 3:
                            floor = 3;
                            break;
                        case 4:
                            floor = 2;
                            break;
                        case 5:
                            floor = 1;
                            break;
                        case 6:
                            floor = -1;
                            break;
                        case 7:
                            floor = -2;
                            break;
                    }
                    indoorView.updateSelectFloor(floor);
                }

                indoorFloorSwitchAdapter.notifyDataSetChanged();
            }
        });
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getContext());
        layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        listfloor.setLayoutManager(layoutManager1);
        listfloor.setOverScrollMode(View.OVER_SCROLL_NEVER);

        listfloor.setAdapter(indoorFloorSwitchAdapter);
        return view;
    }
}
