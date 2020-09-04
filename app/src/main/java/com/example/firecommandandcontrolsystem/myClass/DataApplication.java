package com.example.firecommandandcontrolsystem.myClass;

import android.app.Application;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.example.firecommandandcontrolsystem.R;
import com.example.firecommandandcontrolsystem.fragment.ShowDeviceManager;

import java.util.ArrayList;
import java.util.List;


//所有的数据都在此类里面
public class DataApplication extends Application {


    public int mastermenuCurSelectIndex = 0;

    //室内地图
    public int indoormap_floor_curSelectIndex = 1;

    //全局定位
    public int mapglobalMenuCurSelectIndex = 0;

    //系统设置左菜单设置
    public int systemSetMenuCurSelectIndex = 0;

    //人员信息
    public int firemenInfoMenuCurSelectIndex = 0;//左菜单
    public int firemenInfo_rescue_curSelectIndex = -1;
    public int firemenInfo_rescued_curSelectIndex = -1;
    public int firemenInfo_retreat_curSelectIndex = -1;

    //设备管理
    public int deviceManagerCurSelectIndex = -1;
    public int deviceManager_firmenlist_curSelectIndex = -1;

    //设备管理里面的数据
    public List<TerminalInfo> terminalInfoList = new ArrayList<>();
    public List<PersonInfo> personInfoList = new ArrayList<>();

    //在线人员表,列表只做数据引用，应避免直接进行增删
    public List<ShowDeviceManager.DeviceInfo> listdeviceinfo = new ArrayList<>();
    public List<Firemen> listFiremen = new ArrayList<>();
    public List<Firemen> listRescue = new ArrayList<>();
    public List<Firemen> listRescued = new ArrayList<>();
    private int sosCount = 0;
    //自组网网络
    public String adhocClient_ip;
    public int adhocClient_port;
    public int identificationID;
    //云端
    public String cloudClient_ip;
    public int cloudClient_port;

    //楼层配置数据
    public float firstFloorHeight = 0;
    public float eachFloorHeight = 0;
    public float fu1_floorheight = 0;
    public float fu_eachfloorheight = 0;
    public float curfloor = 0;

    //3D模型数据

    public float model_firstfloorheight;
    public float model_eachFloorHeight;
    public int model_floornum = 4;
    public List<LatLng> modelPoints = new ArrayList<>(); //所选点集合


    public DataApplication() {
        listFiremen.clear();
        listRescue.clear();
        listRescued.clear();
    }

    public interface DataapplicationInterfaceCallback {

        void toltalNumberOfFiremenChange(int count);

        void onlineNumberOfFiremenChange(int count);

        void sosNumberOfFiremen(int count);

        void notifyIndoorUpdate();

        void showToast(String str);

        void notifyFloorInfoUpdate();
    }


    private DataapplicationInterfaceCallback dataapplicationInterface;

    public void setListener(DataapplicationInterfaceCallback interfacepar) {
        this.dataapplicationInterface = interfacepar;
    }


    //所有的设备信息 应该通过此函数将数据添加进来，因为设备数量就是总人数  需要反馈到主活动窗口
    public void addDeviceInfoInList(ShowDeviceManager.DeviceInfo info) {

        listdeviceinfo.add(info);

        if (dataapplicationInterface != null)
            dataapplicationInterface.toltalNumberOfFiremenChange(listdeviceinfo.size());
    }

    //唯一增加消防员 的函数
    public void addFiremenInList(Firemen firemen) {
        listFiremen.add(firemen);

        //消防员添加的同时 应把他添加进救援人员列表
        listRescue.add(firemen);

        if (dataapplicationInterface != null)
            dataapplicationInterface.onlineNumberOfFiremenChange(listFiremen.size());

    }

    public void setSosFiremenState(int id) {
        Firemen firemen = findFiremenFromID(id);
        if (firemen == null) {
            if (dataapplicationInterface != null)
                dataapplicationInterface.showToast("求救人员:" + id + " 未在线，");
            return;
        }

        int cmdRescuedState = firemen.getCmdStateRescued();

        Log.e("test", "求救人员:" + cmdRescuedState + "," + firemen.getCmd_state());

        if (firemen.getCmdStateRescued() == Firemen.CMD_STATE_RESCUED)
            return;
        else
            firemen.setCMDstate(Firemen.CMD_STATE_RESCUED);

        sosCount++;

        listRescued.add(firemen);
        listRescue.remove(firemen);

        if (dataapplicationInterface != null)
            dataapplicationInterface.sosNumberOfFiremen(sosCount);
    }


    public void addIndoorLatlngInFiremen(Firemen curFiremen, int floor, LatLng pt) {


        List<Firemen.LatLngFloor> listindoor = curFiremen.listIndoorPoint;


        listindoor.add(new Firemen.LatLngFloor(floor,pt));



//        if (curFiremen.listTrackHashMap.containsKey(floor)) {
//            curFiremen.listTrackHashMap.get(floor).add(pt);
//        } else {
//            List<LatLng> list = new ArrayList<>();
//            list.add(pt);
//            curFiremen.listTrackHashMap.put(floor, list);
//        }
//
        if (dataapplicationInterface != null) {
            dataapplicationInterface.notifyIndoorUpdate();
            dataapplicationInterface.notifyFloorInfoUpdate();
        }
    }


    public int getIamgeRes(int id) {
        int res = 1;
        switch (id) {
            case 1:
                res = R.mipmap.marker_1;
                break;
            case 2:
                res = R.mipmap.marker_2;
                break;
            case 3:
                res = R.mipmap.marker_3;
                break;
            case 4:
                res = R.mipmap.marker_4;
                break;
            case 5:
                res = R.mipmap.marker_5;
                break;
            case 6:
                res = R.mipmap.marker_6;
                break;
            case 7:
                res = R.mipmap.marker_7;
                break;
            case 8:
                res = R.mipmap.marker_8;
                break;
            case 9:
                res = R.mipmap.marker_9;
                break;
            case 10:
                res = R.mipmap.marker_10;
                break;
        }
        return res;
    }

    public int getIdColor(int id) {

        switch (id) {

            case 1:
                return Color.parseColor("#f4ea2a");

            case 2:
                return Color.parseColor("#1afa29");

            case 3:
                return Color.parseColor("#84af1c");

            case 4:
                return Color.parseColor("#1296db");

            case 5:
                return Color.parseColor("#3317d1");

            case 6:
                return Color.parseColor("#13227a");

            case 7:
                return Color.parseColor("#052409");

            case 8:
                return Color.parseColor("#d81e06");

            case 9:
                return Color.parseColor("#d4237a");

            case 10:
                return Color.parseColor("#e0620d");

        }

        return Color.parseColor("#000000");

    }



    public Firemen findFiremenFromID(int id) {

        for (int i = 0; i < listFiremen.size(); i++) {

            Firemen firemen = listFiremen.get(i);
            if (firemen.getBindDeviceId() == id) {
                return firemen;
            }
        }
        return null;
    }

    public Firemen findFiremenFromID(int id, List<Firemen> list) {

        for (int i = 0; i < list.size(); i++) {

            Firemen firemen = list.get(i);
            if (firemen.getBindDeviceId() == id) {
                return firemen;
            }
        }
        return null;
    }

    public class TerminalInfo {

        public int number;
        public String name;
        public float[] color;

    }

    public class PersonInfo {
        public String name;
        public int number;
        public String group;
    }


    public void saveFloorConfig(float curFloorNum, float firstFloorHeight, float eachFloorHeight, float fu1_floorheight, float fu_eachfloorheight) {
        SharedPreferences sharedPreferences = getSharedPreferences("floorConifg", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("curFloorNum", curFloorNum);
        editor.putFloat("firstFloorHeight", firstFloorHeight);
        editor.putFloat("eachFloorHeight", eachFloorHeight);
        editor.putFloat("fu1_floorheight", fu1_floorheight);
        editor.putFloat("fu_eachfloorheight", fu_eachfloorheight);

        editor.commit();
    }

    public void getFloorConfig() {
        SharedPreferences sharedPreferences = getSharedPreferences("floorConifg", 0);

        curfloor = sharedPreferences.getFloat("curFloorNum", 0);
        firstFloorHeight = sharedPreferences.getFloat("firstFloorHeight", 0);
        eachFloorHeight = sharedPreferences.getFloat("eachFloorHeight", 0);
        fu1_floorheight = sharedPreferences.getFloat("fu1_floorheight", 0);
        fu_eachfloorheight = sharedPreferences.getFloat("fu_eachfloorheight", 0);

    }

    public void saveNetConfig(String jizhanIp, int jizhanPort, String cloudIp, int cloudPort) {
        SharedPreferences sharedPreferences = this.getSharedPreferences("netConifg", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("jizhanIp", jizhanIp);
        editor.putInt("jizhanPort", jizhanPort);
        editor.putString("cloudIp", cloudIp);
        editor.putInt("cloudPort", cloudPort);

        editor.commit();
    }

    public void getNetConfig() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("netConifg", 0);

        adhocClient_ip = sharedPreferences.getString("jizhanIp", "");
        adhocClient_port = sharedPreferences.getInt("jizhanPort", 0);
        cloudClient_ip = sharedPreferences.getString("cloudIp", "");
        cloudClient_port = sharedPreferences.getInt("cloudPort", 0);
    }


    public float[] getColorByID(int id) {

        switch (id) {

            case 1:
                return new float[]{0.96f, 0.92f, 0.16f, 1};
            case 2:
                return new float[]{0.10f, 0.98f, 0.16f, 1};
            case 3:
                return new float[]{0.70f, 1f, 0f, 1};
            case 4:
                return new float[]{0.0f, 0.65f, 1f, 1};
            case 5:
                return new float[]{0.45f, 0.0f, 1f, 1};
            case 6:
                return new float[]{0.0f, 0.15f, 1f, 1};
            case 7:
                return new float[]{0.0f, 0.63f, 0.08f, 1};
            case 8:
                return new float[]{1f, 0.11f, 0f, 1};
            case 9:
                return new float[]{1f, 0.0f, 0.49f, 1};
            case 10:
                return new float[]{1f, 0.41f, 0.01f, 1};

        }
        return new float[]{1f, 1f, 1f, 1};
    }

    public String getColorStrByID(int id) {

        switch (id) {
            case 1:
                return "#f4ea2a";
            case 2:
                return "#1afa29";
            case 3:
                return "#b4ff00";
            case 4:
                return "#00a7ff";
            case 5:
                return "#7200ff";
            case 6:
                return "#0025ff";
            case 7:
                return "#00a015";
            case 8:
                return "#ff1d00";
            case 9:
                return "#ff007d";
            case 10:
                return "#ff6802";
            default:

        }
        return "#000000";
    }

    public Bitmap getPersonBitMapByID(int id) {
        switch (id) {

            case 1:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.marker_1);
            case 2:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.marker_2);
            case 3:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.marker_3);
            case 4:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.marker_4);
            case 5:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.marker_5);
            case 6:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.marker_6);
            case 7:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.marker_7);
            case 8:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.marker_8);
            case 9:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.marker_9);
            case 10:
                return BitmapFactory.decodeResource(getResources(), R.mipmap.marker_10);
            default:

        }
        return null;
    }
}
