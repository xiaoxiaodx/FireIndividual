package com.example.firecommandandcontrolsystem.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firecommandandcontrolsystem.Adapter.DeviceInfoAdapter;
import com.example.firecommandandcontrolsystem.MainActivity;
import com.example.firecommandandcontrolsystem.R;
import com.example.firecommandandcontrolsystem.dialog.Device_delete;
import com.example.firecommandandcontrolsystem.dialog.Device_edit;
import com.example.firecommandandcontrolsystem.myClass.DataApplication;

import java.util.ArrayList;
import java.util.List;

public class ShowDeviceManager extends Fragment {


    RecyclerView devicelistview;
    DeviceInfoAdapter deviceinfoadapter;


    List<DeviceInfo> listdeviceinfo = new ArrayList<>();


    public ShowDeviceManager(MainActivity listener) {


        listdeviceinfo.add(new DeviceInfo("小强", "小区一", 1, 1));
        listdeviceinfo.add(new DeviceInfo("小强1", "小区1", 2, 2));
        listdeviceinfo.add(new DeviceInfo("小强2", "小区1", 3, 3));
        listdeviceinfo.add(new DeviceInfo("小强3", "小区2", 4, 4));
        // deviceinfoadapter.notifyDataSetChanged();

        listener.setDeviceManagerListener(new MainActivity.DeviceManagerinterface() {
            @Override
            public void newDeivece() {

                Device_edit.Builder builder = new Device_edit.Builder(getContext());
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //设置你的操作事项

                        Device_edit device_add = (Device_edit) dialog;

                        listdeviceinfo.add(new DeviceInfo(device_add.edit_name, device_add.edit_group, device_add.selectindexColor, Integer.valueOf(device_add.edit_deviceid)));
                        deviceinfoadapter.notifyDataSetChanged();
                        Log.e("test", "" + device_add.selectindexColor + "," + device_add.edit_name + "," + device_add.edit_group + "," + device_add.edit_deviceid);
                    }
                });

                builder.setNegativeButton("取消",
                        new android.content.DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                builder.create().show();

            }


            @Override
            public void editSelect() {

                final DataApplication dataApplication = (DataApplication) getContext().getApplicationContext();

                if(dataApplication.deviceManagerCurSelectIndex < 0) {
                    Toast.makeText(getContext(), "当前未选中", Toast.LENGTH_SHORT).show();
                    return;
                }
                final DeviceInfo deviceInfo = listdeviceinfo.get(dataApplication.deviceManagerCurSelectIndex);


                Device_edit.Builder builder = new Device_edit.Builder(getContext());
                builder.setDeviceID(deviceInfo.deviceid)
                        .setTitle("设备*编辑")
                        .setGroup(deviceInfo.group)
                        .setName(deviceInfo.name)
                        .setColorSelect(deviceInfo.colorindex)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                //设置你的操作事项

                                Device_edit device_add = (Device_edit) dialog;

                                DeviceInfo deviceInfo = listdeviceinfo.get(dataApplication.deviceManagerCurSelectIndex);

                                deviceInfo.colorindex = device_add.selectindexColor;
                                deviceInfo.deviceid = Integer.valueOf(device_add.edit_deviceid);
                                deviceInfo.group = device_add.edit_group;
                                deviceInfo.name = device_add.edit_name;

                                deviceinfoadapter.notifyDataSetChanged();
                                Log.e("test", "" + device_add.selectindexColor + "," + device_add.edit_name + "," + device_add.edit_group + "," + device_add.edit_deviceid);
                            }
                        });

                builder.setNegativeButton("取消",
                        new android.content.DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                builder.create().show();
            }

            @Override
            public void deleteSelect() {
                Device_delete.Builder builder = new Device_delete.Builder(getContext());
                final DataApplication dataApplication = (DataApplication) getContext().getApplicationContext();

                if(dataApplication.deviceManagerCurSelectIndex < 0) {
                    Toast.makeText(getContext(), "当前未选中", Toast.LENGTH_SHORT).show();
                    return;
                }
                final DeviceInfo deviceInfo = listdeviceinfo.get(dataApplication.deviceManagerCurSelectIndex);
                builder .setDeviceID(deviceInfo.deviceid)
                        .setGroup(deviceInfo.group)
                        .setName(deviceInfo.name)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                //设置你的操作事项
                                DataApplication dataApplication = (DataApplication) getContext().getApplicationContext();

                                if(dataApplication.deviceManagerCurSelectIndex < 0) {
                                    Toast.makeText(getContext(), "当前未选中", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                listdeviceinfo.remove(dataApplication.deviceManagerCurSelectIndex);

                                deviceinfoadapter.notifyDataSetChanged();

                            }
                        });

                builder.setNegativeButton("取消",
                        new android.content.DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                builder.create().show();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_devicemanager, container, false);


        devicelistview = view.findViewById(R.id.personlistview);

        deviceinfoadapter = new DeviceInfoAdapter(getContext(), listdeviceinfo);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        devicelistview.setLayoutManager(layoutManager);
        devicelistview.setAdapter(deviceinfoadapter);
        devicelistview.setOverScrollMode(View.OVER_SCROLL_NEVER);

        deviceinfoadapter.setOnItemClickListener(new DeviceInfoAdapter.onItemClick() {
            @Override
            public void onItemClick(View view, int position) {

                DataApplication dataApplication = (DataApplication) view.getContext().getApplicationContext();
                dataApplication.deviceManagerCurSelectIndex = (Integer) view.getTag();

                deviceinfoadapter.notifyDataSetChanged();
                Log.e("test:", "" + position);
            }


        });

        return view;
    }


    public class DeviceInfo {
        public DeviceInfo(String name, String group, int colorindex, int deviceid) {
            this.name = name;
            this.group = group;
            this.colorindex = colorindex;
            this.deviceid = deviceid;
        }

        public String name;
        public String group;
        public int colorindex;
        public int deviceid;
    }
}
