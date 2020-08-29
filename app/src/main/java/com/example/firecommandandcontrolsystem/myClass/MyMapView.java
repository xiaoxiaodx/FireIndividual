package com.example.firecommandandcontrolsystem.myClass;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.CoordinateConverter;
import com.amap.api.maps.MapView;
import com.amap.api.maps.Projection;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.LatLng;
import com.example.firecommandandcontrolsystem.GLMapRender.GLMapRender;
import com.example.firecommandandcontrolsystem.R;
import com.example.firecommandandcontrolsystem.adhocNetwork.ProtcocolClass.GpsInfo;

import java.util.HashMap;
import java.util.List;


//地图
public class MyMapView extends MapView {


    AMap aMap;
    public MyMapView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);


        aMap = getMap();
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

        DataApplication dataApplication = (DataApplication)getContext().getApplicationContext();

        List<Firemen> listfiremen = dataApplication.listFiremen;

        Firemen curFiremen = dataApplication.findFiremenFromID(info.id);

        LatLng curlatlng = gps2gaode(new LatLng(info.lat, info.lng));
        if (curFiremen == null) {
            curFiremen = new Firemen();
            listfiremen.add(curFiremen);

            aMap.moveCamera(CameraUpdateFactory.changeLatLng(curlatlng));
        }


        curFiremen.setFloor(info.floor);
        curFiremen.setFootBattery(info.footBattery);
        curFiremen.setCurLocation(curlatlng);
        curFiremen.setLatState(info.latState);
        curFiremen.setLngState(info.lngState);
        curFiremen.setBindDeviceId(info.id);
        curFiremen.setPhoneBattery(info.phoneBattery);
        curFiremen.setTerminalBattery(info.adhocBattery);
        curFiremen.getHistoricalTrack().add(curlatlng);
        curFiremen.getHistoricalHeight().add(info.height);
        curFiremen.selectColorByID(info.id);

        PointF pf = aMap.getProjection().toOpenGLLocation(curlatlng);
        curFiremen.getHistoricalTrackgl().add(pf);
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

            aMap.setCustomRenderer(new GLMapRender(aMap,getContext()));

            //load(getResources());
        }
    }
    void load(Resources resources) {
       // mBitmap = BitmapFactory.decodeResource(resources, R.mipmap.circle_fill);
    }

}