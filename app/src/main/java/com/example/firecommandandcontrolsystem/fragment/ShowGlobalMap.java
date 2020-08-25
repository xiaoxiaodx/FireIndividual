package com.example.firecommandandcontrolsystem.fragment;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.CoordinateConverter;
import com.amap.api.maps.Projection;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.BuildingOverlay;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;

import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;

import com.example.firecommandandcontrolsystem.GLMapRender.GLMapRender;
import com.example.firecommandandcontrolsystem.MainActivity;
import com.example.firecommandandcontrolsystem.R;
import com.example.firecommandandcontrolsystem.adhocNetwork.ProtcocolClass.GpsInfo;
import com.example.firecommandandcontrolsystem.myClass.DataApplication;
import com.example.firecommandandcontrolsystem.myClass.Firemen;
import com.example.firecommandandcontrolsystem.myClass.MapSelectPolygon;

import com.example.firecommandandcontrolsystem.myClass.MyMapView;
import com.example.firecommandandcontrolsystem.opengl.CubeMapRender;

import java.util.ArrayList;

import java.util.List;


public class ShowGlobalMap extends Fragment {


    public static Bitmap mBitmap;
    public static void load(Resources resources) {
        mBitmap = BitmapFactory.decodeResource(resources, R.mipmap.circle_fill);
    }
    private MyMapView mapView;
    private AMap aMap;

    //  private ModePicklPoint modePicklPoint;

    private MapSelectPolygon mapSelectPolygon;

    DataApplication dataApplication;


    static int testindex = 0;

    public ShowGlobalMap(final MainActivity activity) {


        dataApplication = (DataApplication) activity.getApplication();

        testindex = 0;
        activity.setListener(new MainActivity.interfaceGaodeMap() {
            @Override
            public void setup3Dmodel() {
                if(mapSelectPolygon == null)
                    mapSelectPolygon = new MapSelectPolygon(mapView.getMap());



            aMap.setCustomRenderer(cubeMapRender);
                return;
            }

            @Override
            public void updateLngLat(GpsInfo info) {

                updateFiremenInfo(info);

            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_show_globalmap, container, false);

        mapView = (MyMapView) view.findViewById(R.id.gaodeMap);
        mapView.onCreate(savedInstanceState);


        Button btn = view.findViewById(R.id.test);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (aMap != null) {
                    LatLng pt = getScreenCenterLatlng();
                    Log.e("******", "中心坐标1：" + pt);
                    List<PointF> listf = new ArrayList<>();
                    for (int i = 0; i < mapSelectPolygon.latLngList.size(); i++) {
                        listf.add(aMap.getProjection().toOpenGLLocation(mapSelectPolygon.latLngList.get(i)));
                    }

                    if (cubeMapRender != null) {
                        aMap.clear();
                        cubeMapRender.createCube(listf, pt, (int) aMap.getCameraPosition().zoom);
                        Log.e("******", "**********1");

                    }
                }
                aMap.getUiSettings().setAllGesturesEnabled(true);
            }
        });


        init3DEnvir();

        return view;
    }

    private CubeMapRender cubeMapRender;

    private GLMapRender glMapRender;

    private LatLng getScreenCenterLatlng() {

        if (mapView != null) {
            int left = mapView.getLeft();
            int top = mapView.getTop();
            int right = mapView.getRight();
            int bottom = mapView.getBottom();

            int x = (int) (mapView.getX() + (right - left) / 2);
            int y = (int) (mapView.getY() + (bottom - top) / 2);

            Projection projection = aMap.getProjection();
            LatLng pt = projection.fromScreenLocation(new Point(x, y));
            return pt;
        } else
            return null;

    }

    void updateFiremenInfo(GpsInfo info) {


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
        if (aMap == null) {
            aMap = mapView.getMap();
            //   aMap.showMapText(false);
            //   aMap.showBuildings(false);

//            DataApplication dataApplication = (DataApplication) getActivity().getApplication();
//
//
//            cubeMapRender = new CubeMapRender(aMap, dataApplication);
//
//            aMap.setCustomRenderer(cubeMapRender);


            glMapRender = new GLMapRender(aMap,getContext());
            aMap.setCustomRenderer(glMapRender);

            load(getResources());
        }
    }


    /**
     * 方法必须重写
     */

    @Override

    public void onResume() {

        super.onResume();
//        if(mapView != null)
//        mapView.onResume();

    }

    /**
     * 方法必须重写
     */

    @Override

    public void onPause() {

        super.onPause();
//        if(mapView != null)
//        mapView.onPause();

    }

    /**
     * 方法必须重写
     */

    @Override

    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
//        if(mapView != null)
//        mapView.onSaveInstanceState(outState);

    }

    /**
     * 方法必须重写
     */

    @Override

    public void onDestroy() {

        super.onDestroy();

//        if(mapView != null)
//        mapView.onDestroy();

    }


}