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

public class ShowNetSet extends Fragment {


    EditText editjizhan_ip;
    EditText editjizhan_port;
    EditText editcloud_ip;
    EditText editcloud_port;
    DataApplication dataApplication;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_netset,container,false);

        dataApplication = (DataApplication)getContext().getApplicationContext();

        editjizhan_ip = view.findViewById(R.id.jizhan_ip);
        editjizhan_port = view.findViewById(R.id.jizhan_port);
        editcloud_ip = view.findViewById(R.id.cloud_ip);
        editcloud_port = view.findViewById(R.id.cloud_port);

        dataApplication.getNetConfig();

        editjizhan_ip.setText(dataApplication.adhocClient_ip);
        editjizhan_port.setText(String.valueOf(dataApplication.adhocClient_port));
        editcloud_ip.setText(dataApplication.cloudClient_ip);
        editcloud_port.setText(String.valueOf(dataApplication.cloudClient_port));

        Button btn_jizhan_connnect = view.findViewById(R.id.btn_jizhan_connect);
        Button btn_cloud_connect = view.findViewById(R.id.btn_cloud_connect);

        btn_jizhan_connnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                String adhoc_ip = editjizhan_ip.getText().toString();
                int adhoc_port = Integer.valueOf(editjizhan_port.getText().toString());
                String cloud_ip = editcloud_ip.getText().toString();
                int cloud_port = Integer.valueOf(editcloud_port.getText().toString());


                dataApplication.saveNetConfig(adhoc_ip,adhoc_port,cloud_ip,cloud_port);
                if(netConnect != null){
                    netConnect.interfaceAdhocConnect(adhoc_ip,adhoc_port);
                    netConnect.interfaceCloudConnect(cloud_ip,cloud_port);
                }

            }
        });

        btn_cloud_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DataApplication dataApplication = (DataApplication)getContext().getApplicationContext();

                String ip = editcloud_ip.getText().toString();
                int port = Integer.valueOf(editcloud_port.getText().toString());

                dataApplication.cloudClient_ip = ip;
                dataApplication.cloudClient_port = port;
                if(netConnect != null)
                    netConnect.interfaceCloudConnect(ip,port);
            }
        });


        return view;
    }

    public ShowNetSet(interfaceNetConnect iconnect){


        this.netConnect = iconnect;
    }


    public interface  interfaceNetConnect{
        void interfaceAdhocConnect(String ip,int port);
        void interfaceCloudConnect(String ip,int port);
    }
    /**
     *定义一个变量储存数据
     */
    private interfaceNetConnect netConnect;



}
