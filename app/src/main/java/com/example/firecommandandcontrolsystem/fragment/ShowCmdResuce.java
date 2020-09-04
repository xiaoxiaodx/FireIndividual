package com.example.firecommandandcontrolsystem.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firecommandandcontrolsystem.Adapter.FiremenRescueAdapter;
import com.example.firecommandandcontrolsystem.Adapter.FiremenRescuedAdapter;
import com.example.firecommandandcontrolsystem.Adapter.FiremenRetreatAdapter;
import com.example.firecommandandcontrolsystem.MainActivity;
import com.example.firecommandandcontrolsystem.R;
import com.example.firecommandandcontrolsystem.myClass.DataApplication;
import com.example.firecommandandcontrolsystem.myClass.Firemen;

import java.util.ArrayList;
import java.util.List;

public class ShowCmdResuce extends Fragment {

    RecyclerView listRescue ;
    RecyclerView listRescued ;
    FiremenRescueAdapter firemenRescueAdapter;
    FiremenRescuedAdapter firemenRescuedAdapter;


    public ShowCmdResuce(MainActivity activity,interfaceRescue resuce){

        cmdRescue = resuce;

        activity.setPersonInfoListener(new MainActivity.PersonInfointerface() {
            @Override
            public void notifyRescueUpdate() {
                if(firemenRescueAdapter != null)
                    firemenRescueAdapter.notifyDataSetChanged();
            }

            @Override
            public void notifyRescuedUpdate() {
                if(firemenRescuedAdapter != null)
                    firemenRescuedAdapter.notifyDataSetChanged();
            }

            @Override
            public void notifyRetreatUpdate() {

            }
        });
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_cmd_rescue,container,false);


        listRescue  = view.findViewById(R.id.rescue_list);
        listRescued = view.findViewById(R.id.rescued_list);


        firemenRescueAdapter = new FiremenRescueAdapter(getContext(),((DataApplication)getContext().getApplicationContext()).listRescue);
        firemenRescuedAdapter = new FiremenRescuedAdapter(getContext(),((DataApplication)getContext().getApplicationContext()).listRescued);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        listRescued.setLayoutManager(layoutManager);
        listRescued.setOverScrollMode(View.OVER_SCROLL_NEVER);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getContext());
        layoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        listRescue.setLayoutManager(layoutManager1);
        listRescue.setOverScrollMode(View.OVER_SCROLL_NEVER);

        view.findViewById(R.id.firemen_rescue_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cmdRescue != null){
                    int rescuedid = -1;
                    List<Integer> listrescue = new ArrayList<>();
                    List<Firemen> firemenrescued = ((DataApplication) getContext().getApplicationContext()).listRescued;
                    List<Firemen> firemenrescue = ((DataApplication)( getContext().getApplicationContext())).listRescue;

                    for(int i=0;i<firemenrescued.size();i++){

                        Firemen firemen = firemenrescued.get(i);
                        if(firemen.isSelectRescued()){
                            rescuedid = Integer.valueOf(firemen.getBindDeviceId());
                            break;
                        }
                    }

                    Log.e("test","营救人员数量："+firemenrescue.size());
                    for(int i=0;i<firemenrescue.size();i++){
                        Firemen firemen = firemenrescue.get(i);
                        if(firemen.isSelectRescue()){
                            listrescue.add(firemen.getBindDeviceId());
                            Log.e("test","营救人员："+firemen.getBindDeviceId());
                        }
                    }

                    cmdRescue.interfaceRescue(rescuedid,listrescue);
                }
            }
        });


        firemenRescueAdapter.setOnItemClickListener(new FiremenRescueAdapter.onItemClick() {
            @Override
            public void onItemClick(View view, int position) {
                DataApplication dataApplication = (DataApplication) view.getContext().getApplicationContext();
                dataApplication.firemenInfo_rescue_curSelectIndex = position;

                firemenRescueAdapter.notifyDataSetChanged();
            }
        });
        firemenRescuedAdapter.setOnItemClickListener(new FiremenRescuedAdapter.onItemClick() {
            @Override
            public void onItemClick(View view, int position) {
                DataApplication dataApplication = (DataApplication) view.getContext().getApplicationContext();
                dataApplication.firemenInfo_rescued_curSelectIndex = position;


                firemenRescuedAdapter.notifyDataSetChanged();
            }
        });

        listRescue.setAdapter(firemenRescueAdapter);
        listRescued.setAdapter(firemenRescuedAdapter);

        return view;
    }


    public interface  interfaceRescue{
        void interfaceRescue(int firemenid, List<Integer> listrescue);
    }
    /**
     *定义一个变量储存数据
     */
    private interfaceRescue cmdRescue;
}
