package com.example.firecommandandcontrolsystem.opengl;

import android.graphics.PointF;
import android.opengl.Matrix;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.CustomRenderer;
import com.amap.api.maps.model.LatLng;
import com.example.firecommandandcontrolsystem.GLMapRender.Line3D;
import com.example.firecommandandcontrolsystem.myClass.DataApplication;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class CubeMapRender implements CustomRenderer {

    private float[] translate_vector = new float[4];
    public static float SCALE = 0.005F;// 缩放暂时使用这个


    List<LatLng> listptf = new ArrayList<>();
    private LatLng center = new LatLng(112.87748234731855,28.21149245890368);

    private Cube cube;

    private Line3D line3Ds;

    private AMap aMap;

    float width, height;

    DataApplication dataApplication = null;


    public CubeMapRender(AMap aMap,DataApplication dataApplication) {
        this.aMap = aMap;
        this.dataApplication = dataApplication;
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(this.center, 16));
    }

    float[] mvp = new float[16];


    private List<Float> listf = new ArrayList<>();
    private List<Float> listf1 = new ArrayList<>();
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onDrawFrame(GL10 gl) {

        //if (cube != null) {
            Matrix.setIdentityM(mvp, 0);
            //偏移
            PointF centerpf = aMap.getProjection().toOpenGLLocation(center);

            if(centerpf == null)
                return;
            Matrix.multiplyMM(mvp, 0, aMap.getProjectionMatrix(), 0, aMap.getViewMatrix(), 0);
            Matrix.translateM(mvp, 0, centerpf.x, centerpf.y, 0);
            int scale = 1;
            Matrix.scaleM(mvp, 0, scale, scale, scale);

           // if(isCubeInit)
                cube.drawES20(mvp);


//            List<Firemen> listFiremen = dataApplication.listFiremen;
//
//            for(int i=0;i<listFiremen.size();i++){
//                Firemen firemen = listFiremen.get(i);
//                List<Float> listf = new ArrayList<>();
//                listf.clear();
//                List<LatLng> listlatlng =  firemen.getHistoricalTrack();
//                List<PointF> listgl = firemen.getHistoricalTrackgl();
//                List<Float> listheight = firemen.getHistoricalHeight();
//                //for (int j=0;j<listgl.size();j++){
//                for (int j=0;j<listlatlng.size();j++){
//                    PointF pf = aMap.getProjection().toOpenGLLocation(listlatlng.get(j));
//                    //PointF pf = listgl.get(j);
//                    //Log.e("bbbb",""+pf +","+ listheight.get(j));
//                    listf.add(pf.x - centerpf.x);
//                    listf.add(pf.y - centerpf.y);
//                    listf.add(listheight.get(j)*80);
//                }
//                line3Ds.drawES20(mvp,listf,firemen.getShowColor());
//            }

        //}
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        this.width = width;
        this.height = height;
    }

    private boolean isCubeInit = false;
    public void createCube(List<PointF> listf,LatLng center,int zoom){

//        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, zoom));
//        if(cube != null) {
//            this.center = center;
//            cube.initVetex(listf);
//            isCubeInit = true;
//        }
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {


        //cube = new Cube(aMap, BitmapFactory.decodeResource(dataApplication.getApplicationContext().getResources(), R.mipmap.indoor1234));
        //创建cube
        cube = new Cube(0.2f,0.2f,0.2f);
        cube.initShader();
        line3Ds = new Line3D();
    }

    @Override
    public void OnMapReferencechanged() {



    }

}
