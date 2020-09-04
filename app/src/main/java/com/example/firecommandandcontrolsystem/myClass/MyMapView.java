package com.example.firecommandandcontrolsystem.myClass;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.CoordinateConverter;
import com.amap.api.maps.MapView;
import com.amap.api.maps.Projection;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Polygon;
import com.amap.api.maps.model.PolygonOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.maps.utils.SpatialRelationUtil;
import com.example.firecommandandcontrolsystem.GLMapRender.GLMapRender;
import com.example.firecommandandcontrolsystem.R;
import com.example.firecommandandcontrolsystem.adhocNetwork.ProtcocolClass.GpsInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


//地图
public class MyMapView extends MapView {


    static public AMap aMap = null;
    DataApplication dataApplication;


    //室内平面地图 默认是一个矩形的cad图
    static public double indoor_buildingW = 0;//建筑宽
    static public double indoor_buildingH = 0;//建筑高
    //下面的变量指的是正面查看图片时  图片左上 左下 右上右下的 经纬度坐标
    static LatLng indoor_leftTop = new LatLng(28.21149245890368, 112.87748234731855);
    static LatLng indoor_leftBottom = new LatLng(28.211270510430364, 112.87755440532865);
    static LatLng indoor_rightTop = new LatLng(28.21165090324281, 112.87805965348026);
    static LatLng indoor_rightBottom = new LatLng(28.21140894759759, 112.8781247075325);

    static double indoor_h_long;

    static double indoor_v_long;
    Polygon indoorlimiteArea;


    double EarthR = 6378137.0;

    double indoorScale = 0;


    public MyMapView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

    }


    public void initMapData(Context context) {
        dataApplication = (DataApplication) context.getApplicationContext();

        aMap = getMap();

        indoorlimiteArea = aMap.addPolygon(new PolygonOptions().strokeWidth(4f)
                .strokeColor(Color.parseColor("#8862a46b")).fillColor(Color.parseColor("#8862a46b")));

        ArrayList<LatLng> latLngList = new ArrayList<LatLng>();

        LatLng indoor_leftTop_gaode = gps2gaode(indoor_leftTop);
        LatLng indoor_leftBottom_gaode = gps2gaode(indoor_leftBottom);
        LatLng indoor_rightTop_gaode = gps2gaode(indoor_rightTop);
        LatLng indoor_rightBottom_gaode = gps2gaode(indoor_rightBottom);


        latLngList.add(indoor_leftTop_gaode);
        latLngList.add(indoor_leftBottom_gaode);
        latLngList.add(indoor_rightTop_gaode);
        latLngList.add(indoor_rightBottom_gaode);


        indoor_h_long = AMapUtils.calculateLineDistance(indoor_leftTop_gaode,indoor_rightTop_gaode);
        indoor_v_long = AMapUtils.calculateLineDistance(indoor_leftTop_gaode,indoor_leftBottom_gaode);

        indoorlimiteArea.setPoints(latLngList);
        indoorlimiteArea.setVisible(false);

    }

    private LatLng getScreenCenterLatlng() {

        int left = getLeft();
        int top = getTop();
        int right = getRight();
        int bottom = getBottom();

        int x = (int) (getX() + (right - left) / 2);
        int y = (int) (getY() + (bottom - top) / 2);

        Projection projection = aMap.getProjection();
        LatLng pt = projection.fromScreenLocation(new Point(x, y));
        return pt;
    }

    public void updateFiremenInfo(GpsInfo info) {

        if(info.floor>=0)
            info.floor = info.floor+1;

        Firemen curFiremen = dataApplication.findFiremenFromID(info.id);
        LatLng gpsLatLng = new LatLng(info.lat, info.lng);
        LatLng gaodelatlng = gps2gaode(gpsLatLng);

        if (curFiremen == null) {
            curFiremen = new Firemen();
            dataApplication.addFiremenInList(curFiremen);
            curFiremen.setShowColor(dataApplication.getColorByID(info.id));
            curFiremen.setShowColorStr(dataApplication.getColorStrByID(info.id));
            curFiremen.setBitmap(dataApplication.getPersonBitMapByID(info.id));
             aMap.moveCamera(CameraUpdateFactory.changeLatLng(gaodelatlng));
        }

        updateFireInfo(curFiremen, info);
      // if (indoorlimiteArea.contains(gaodelatlng)) {
//            //每个经纬度距离左上角的水平距离和锤子距离
            //Log.e("test", "进入室内："+info.floor);
            dataApplication.addIndoorLatlngInFiremen(curFiremen, info.floor, gpsLatLng);
       // }
    }

    private void updateFireInfo(Firemen curFiremen, GpsInfo info) {
        LatLng gaodelatlng = gps2gaode(new LatLng(info.lat, info.lng));
        curFiremen.setFloor(info.floor);
        curFiremen.setFootBattery(info.footBattery);
        curFiremen.setCurLocation(new LatLng(info.lat, info.lng));
        curFiremen.setLatState(info.latState);
        curFiremen.setLngState(info.lngState);
        curFiremen.setBindDeviceId(info.id);
        curFiremen.setPhoneBattery(info.phoneBattery);
        curFiremen.setHeight(info.height);
        curFiremen.setTerminalBattery(info.adhocBattery);
        curFiremen.getHistoricalTrack().add(gaodelatlng);
        curFiremen.getHistoricalHeight().add(info.height);
    }


    private LatLng gps2gaode(LatLng latlng) {
        CoordinateConverter converter = new CoordinateConverter(getContext());
        // CoordType.GPS 待转换坐标类型
        converter.from(CoordinateConverter.CoordType.GPS);
        // sourceLatLng待转换坐标点 LatLng类型
        converter.coord(latlng);
        // 执行转换操作
        LatLng desLatLng = converter.convert();
        return desLatLng;
    }


    /**
     * 初始化AMap对象
     */
    public void init3DEnvir() {
        if (aMap != null) {

            aMap.setCustomRenderer(new GLMapRender(aMap, getContext()));

            //aMap.setCustomRenderer(new GLMapRender(aMap, getContext(),mapSelectPolygon.latLngList,firstheight,height,num));

            //load(getResources());
        }
    }

    static public Bitmap load(Resources resources) {
        return BitmapFactory.decodeResource(resources, R.mipmap.circle_fill);
    }

    MapSelectPolygon mapSelectPolygon;

    public void startSetup3Dmodel(float firstheight, float height, int num) {


        mapSelectPolygon.removeAmapClickListensr();
        if (mapSelectPolygon != null && mapSelectPolygon.latLngList.size() < 3) {
            Toast.makeText(getContext(), "必须选择3个点以上", Toast.LENGTH_SHORT).show();
        } else {
            dataApplication.modelPoints.clear();

            dataApplication.model_eachFloorHeight = height;
            dataApplication.model_firstfloorheight = firstheight;
            dataApplication.model_floornum = num;
            for (int i = 0; i < mapSelectPolygon.latLngList.size(); i++) {
                dataApplication.modelPoints.add(mapSelectPolygon.latLngList.get(i));
            }
        }
    }

    public void startSelectPoint() {
        mapSelectPolygon = new MapSelectPolygon(getMap());
        mapSelectPolygon.setListenClick();
    }


}