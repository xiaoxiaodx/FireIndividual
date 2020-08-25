package com.example.firecommandandcontrolsystem.myClass;

import android.app.Application;
import android.graphics.Color;
import android.graphics.PointF;

import com.amap.api.maps.model.LatLng;
import com.example.firecommandandcontrolsystem.R;

import java.util.ArrayList;
import java.util.List;


//所有的数据都在此类里面
public class DataApplication extends Application {


    public int mastermenuCurSelectIndex = 0;

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
    public int deviceManagerCurSelectIndex = 0;
    public int deviceManager_firmenlist_curSelectIndex = -1;

    public List<Firemen> listFiremen = new ArrayList<>();
    public List<Firemen> listRescue = new ArrayList<>();
    public List<Firemen> listRescued = new ArrayList<>();

    //自组网网络
    public String adhocClient_ip;
    public int adhocClient_port;
    public int identificationID;
    //云端
    public String cloudClient_ip;
    public int cloudClient_port;

    //楼层配置数据
    public float topFirstHeight = 0;
    public float topHeight = 0;
    public float lowFirstHeight = 0;
    public float lowHeight = 0;

    //3D模型数据
    public LatLng modelCenter;
    public int modelzoomLevel;
    public List<PointF> modelPoints = new ArrayList<>(); //opengl里面的顶点坐标值，以视图中心为原点

    public DataApplication() {
        listFiremen.clear();
        listRescue.clear();
        listRescued.clear();

//        Firemen firemen1 = new Firemen();
//        firemen1.setFloor(1);
//        firemen1.setGroud("012");
//        firemen1.setName("wangwu");
//        firemen1.setBindDeviceId(1);
//
//        Firemen firemen2 = new Firemen();
//        firemen2.setFloor(2);
//        firemen2.setGroud("012");
//        firemen2.setName("wangwu");
//        firemen2.setBindDeviceId(2);
//
//        Firemen firemen3 = new Firemen();
//        firemen3.setFloor(3);
//        firemen3.setGroud("012");
//        firemen3.setName("wangwu");
//        firemen3.setBindDeviceId(3);
//
//        Firemen firemen4 = new Firemen();
//        firemen4.setFloor(4);
//        firemen4.setGroud("012");
//        firemen4.setName("wangwu");
//        firemen4.setBindDeviceId(4);
//
//        Firemen firemen5 = new Firemen();
//        firemen5.setFloor(5);
//        firemen5.setGroud("012");
//        firemen5.setName("wangwu");
//        firemen5.setBindDeviceId(5);
//
//        listFiremen.add(firemen1);
//        listFiremen.add(firemen2);
//        listFiremen.add(firemen3);
//        listFiremen.add(firemen4);
//        listFiremen.add(firemen5);
//
//
//        listRescue.add(firemen1);
//
//        listRescued.add(firemen3);
//        listRescued.add(firemen2);
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

    public Firemen findFiremenFromID(int id){

        for(int i=0;i<listFiremen.size();i++){

            Firemen firemen = listFiremen.get(i);
            if(firemen.getBindDeviceId() == id) {
                return firemen;
            }

        }
        return null;
    }
}
