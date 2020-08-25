package com.example.firecommandandcontrolsystem.myClass;

import android.graphics.Color;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polygon;
import com.amap.api.maps.model.PolygonOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.example.firecommandandcontrolsystem.R;

import java.util.ArrayList;
import java.util.List;

public class MapSelectPolygon {


    Polygon polygon;
    Polyline polyline;


    boolean isEnd = false;


    Marker oriMarker;
    AMap aMap;


    public ArrayList<LatLng> latLngList = new ArrayList<LatLng>();

    public MapSelectPolygon(AMap aMap) {

        if (aMap == null)
            return;

        this.aMap = aMap;


        polyline = aMap.addPolyline((new PolylineOptions())
                .width(4));

        polyline.setColor(R.color.fontcolor_dep3);
        polygon = aMap.addPolygon(new PolygonOptions().strokeWidth(4f)
                .strokeColor(Color.parseColor("#8862a46b")).fillColor(Color.parseColor("#8862a46b")));

        setListenClick();

    }

    private void setListenClick() {


        if (aMap != null) {
            aMap.setOnMapClickListener(new AMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    if (!isEnd) {
                        addMarker(latLng);
                    }
                }
            });
        }



        //marker点击监听

        aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {

            @Override

            public boolean onMarkerClick(Marker marker) {

                if (isEnd) {

                } else {

                    if ( oriMarker.equals(marker)) {

                        //封闭图形

                        isEnd = true;

                        //添加marker

                        //addMarker(marker.getPosition());

                        //画多边形
                        polygon.setPoints(latLngList);


                        return true;

                    } else {

                        //未封闭,点击其它marke

                    }

                }

                return false;

            }

        });
    }


    private void addMarker(LatLng latLng) {


        MarkerOptions options = new MarkerOptions();
        options.position(latLng).draggable(false).visible(true);
        Marker marker = aMap.addMarker(options);
        marker.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.circle_null));

        marker.setAnchor(0.5f, 0.5f);

        if(latLngList.size() == 0)
            oriMarker = marker;
        latLngList.add(latLng);
        polyline.setPoints(latLngList);

    }



}
