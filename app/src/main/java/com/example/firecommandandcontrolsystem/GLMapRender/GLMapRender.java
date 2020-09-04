package com.example.firecommandandcontrolsystem.GLMapRender;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.opengl.Matrix;
import android.util.Log;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.CoordinateConverter;
import com.amap.api.maps.CustomRenderer;
import com.amap.api.maps.model.LatLng;
import com.example.firecommandandcontrolsystem.R;
import com.example.firecommandandcontrolsystem.myClass.DataApplication;
import com.example.firecommandandcontrolsystem.myClass.Firemen;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static com.amap.api.maps.model.BitmapDescriptorFactory.getContext;

public class GLMapRender implements CustomRenderer {

    private float[] translate_vector = new float[4];
    public static float SCALE = 0.005F;// 缩放暂时使用这个

    public static float heightRadio = 50;
    private LatLng center = new LatLng(28.21149245890368, 112.87748234731855);// 北京市经纬度

    private BuildingOverlay cube = null;
    private BuildingOverlaySimple buildingOverlaySimple;

    private AMap aMap;
    private Context mcontext;

    float width, height;

    float firstFloorHeigh;
    float eachFloorHeight;
    int floorNum;
    List<LatLng> listpt;

    public GLMapRender(AMap aMap, Context context) {
        this.aMap = aMap;
        mcontext = context;

        this.dataApplication = (DataApplication) context.getApplicationContext();
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(gps2gaode(center), 20));
    }

    public GLMapRender(AMap aMap, Context context,List<LatLng> listpt,float firstFloorHeigh,float eachFloorHeight,int floorNum) {
        this.aMap = aMap;
        this.listpt = listpt;
        mcontext = context;
        this.firstFloorHeigh = firstFloorHeigh;
        this.eachFloorHeight = eachFloorHeight;
        this.floorNum = floorNum;
        this.dataApplication = (DataApplication) context.getApplicationContext();
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(listpt.get(0), 15));
    }

    DataApplication dataApplication = null;
    float[] mvp = new float[16];
    private Line3D line3Ds = null;
    private FiremenMarker marker = null;

    @Override
    public void onDrawFrame(GL10 gl) {

        Matrix.setIdentityM(mvp, 0);

        //偏移
        PointF pointF = aMap.getProjection().toOpenGLLocation(center);

        Matrix.multiplyMM(mvp, 0, aMap.getProjectionMatrix(), 0, aMap.getViewMatrix(), 0);



        //Matrix.translateM(mvp, 0, pointF.x, pointF.y, 0);
        int scale = 1;
        Matrix.scaleM(mvp, 0, scale, scale, scale);


        if(buildingOverlaySimple != null) {

            List<LatLng> modelPoints = dataApplication.modelPoints;

            if(modelPoints.size() > 2) {
                buildingOverlaySimple.drawES20(mvp, modelPoints, dataApplication.model_floornum, dataApplication.model_firstfloorheight, dataApplication.model_eachFloorHeight);
            }
        }


        if (cube != null)
            cube.drawES20(mvp);

        List<Firemen> listFiremen = dataApplication.listFiremen;
        for (int i = 0; i < listFiremen.size(); i++) {
            Firemen firemen = listFiremen.get(i);
            List<Float> listf = new ArrayList<>();
            listf.clear();
            List<LatLng> listlatlng = firemen.getHistoricalTrack();
            List<PointF> listgl = firemen.getHistoricalTrackgl();
            List<Float> listheight = firemen.getHistoricalHeight();
            //for (int j=0;j<listgl.size();j++){
            for (int j = 0; j < listlatlng.size(); j++) {
                PointF pf = aMap.getProjection().toOpenGLLocation(listlatlng.get(j));
                if(pf == null)
                    break;
                listf.add(pf.x );
                listf.add(pf.y );
                listf.add(listheight.get(j) * GLMapRender.heightRadio );
            }
            line3Ds.drawES20(mvp, listf, firemen.getShowColor());

            if (marker != null) {
               // Matrix.translateM( mvp, 0, pointF.x, pointF.y, 0);
                marker.drawES20(mvp,gps2gaode(firemen.getCurLocation()),firemen.getHeight()*GLMapRender.heightRadio,firemen.getBitmap());
            }
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        this.width = width;
        this.height = height;


    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        Log.e("test","onSurfaceCreated：");
        line3Ds = new Line3D();

        //创建cube
//        cube = new BuildingOverlay(aMap, gps2gaode(new LatLng(28.21149245890368, 112.87748234731855)),
//                gps2gaode(new LatLng(28.211270510430364, 112.87755440532865)),
//                gps2gaode(new LatLng(28.21165090324281, 112.87805965348026)),
//                gps2gaode(new LatLng(28.21140894759759, 112.8781247075325)));
//
//        cube.addFloorInfoInBuilding(1, 0, loadImgBitmap(R.mipmap.indoor1234), new float[]{1f, 1f, 0f, 0f});
//        cube.addFloorInfoInBuilding(2, 5, loadImgBitmap(R.mipmap.indoor1234), new float[]{1f, 1f, 0f, 0f});
//        cube.addFloorInfoInBuilding(3, 9, loadImgBitmap(R.mipmap.indoor1234), new float[]{1f, 1f, 0f, 0f});
//        cube.addFloorInfoInBuilding(4, 13, loadImgBitmap(R.mipmap.indoor1234), new float[]{1f, 1f, 0f, 0f});
//        cube.addFloorInfoInBuilding(5, 17, loadImgBitmap(R.mipmap.indoor1234), new float[]{1f, 1f, 0f, 0f});
//        cube.addFloorInfoInBuilding(6, 21, loadImgBitmap(R.mipmap.indoor1234), new float[]{1f, 1f, 0f, 0f});
//
//        cube.initShader();

        buildingOverlaySimple = new BuildingOverlaySimple(aMap);
        buildingOverlaySimple.initShader();

        marker = new FiremenMarker(aMap);
//        marker.addFloorInfoInBuilding(1, 0, loadImgBitmap(R.mipmap.indoor1234), new float[]{1f, 1f, 0f, 0f});
//        marker.addFloorInfoInBuilding(2, 5, loadImgBitmap(R.mipmap.circle_fill), new float[]{1f, 1f, 0f, 0f});
//        marker.addFloorInfoInBuilding(3, 9, loadImgBitmap(R.mipmap.marker_1), new float[]{1f, 1f, 0f, 0f});
//        marker.addFloorInfoInBuilding(4, 13, loadImgBitmap(R.mipmap.marker_2), new float[]{1f, 1f, 0f, 0f});
//        marker.addFloorInfoInBuilding(5, 17, loadImgBitmap(R.mipmap.marker_3), new float[]{1f, 1f, 0f, 0f});
//        marker.addFloorInfoInBuilding(6, 21, loadImgBitmap(R.mipmap.marker_4), new float[]{1f, 1f, 0f, 0f});
        marker.initShader();
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

    @Override
    public void OnMapReferencechanged() {

    }

    private Bitmap loadImgBitmap(int resid) {

        return BitmapFactory.decodeResource(mcontext.getResources(), resid);
    }
}
