package com.example.firecommandandcontrolsystem.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.firecommandandcontrolsystem.Adapter.FiremenAdapter;
import com.example.firecommandandcontrolsystem.Adapter.FiremenRetreatAdapter;
import com.example.firecommandandcontrolsystem.R;
import com.example.firecommandandcontrolsystem.myClass.DataApplication;
import com.example.firecommandandcontrolsystem.myClass.Firemen;

import java.util.ArrayList;
import java.util.List;

public class ShowCmdRetreat extends Fragment {


    GridView firemenRetreatGirdview;
    FiremenRetreatAdapter firemenRetreatAdapter;

    public  ShowCmdRetreat(interfaceRetreat retreat){

        cmdRetreat = retreat;

    }
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_cmd_retreat,container,false);

        firemenRetreatGirdview  = view.findViewById(R.id.firemen_retreat_gridview);

        firemenRetreatAdapter = new FiremenRetreatAdapter(getContext(),((DataApplication)getContext().getApplicationContext()).listFiremen);

        firemenRetreatGirdview.setAdapter(firemenRetreatAdapter);

        firemenRetreatGirdview.setOverScrollMode(View.OVER_SCROLL_NEVER);


        firemenRetreatAdapter.setOnItemClickListener(new FiremenRetreatAdapter.onItemClick() {
            @Override
            public void onItemClick(View view, int position) {

                DataApplication dataApplication = (DataApplication) view.getContext().getApplicationContext();

                dataApplication.firemenInfo_retreat_curSelectIndex = position;

                firemenRetreatAdapter.notifyDataSetChanged();
            }
        });

        View viewSend = view.findViewById(R.id.firemen_retreat_send);

        viewSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(cmdRetreat != null){
                    List<Integer> listretreate = new ArrayList<>();
                    List<Firemen> firemens = ((DataApplication)((DataApplication) getContext().getApplicationContext())).listFiremen;

                    for(int i=0;i<firemens.size();i++){
                        Firemen firemen = firemens.get(i);
                        if(firemen.isSelectRetreat()){
                            listretreate.add( Integer.valueOf(firemen.getBindDeviceId()));
                            break;
                        }
                    }


                    cmdRetreat.interfaceRetreat(2,listretreate);
                }

            }
        });

        return view;
    }

    public interface  interfaceRetreat{
        void interfaceRetreat(int cmdlevel,List<Integer> listretreat);
    }
    /**
     *定义一个变量储存数据
     */
    private interfaceRetreat cmdRetreat;
}
