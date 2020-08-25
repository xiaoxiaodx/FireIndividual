package com.example.firecommandandcontrolsystem.GLMapRender;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.opengl.Matrix;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.CoordinateConverter;
import com.amap.api.maps.CustomRenderer;
import com.amap.api.maps.model.LatLng;
import com.example.firecommandandcontrolsystem.R;
import com.example.firecommandandcontrolsystem.fragment.ShowGlobalMap;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static com.amap.api.maps.model.BitmapDescriptorFactory.getContext;

public class GLMapRender implements CustomRenderer {

    private float[] translate_vector = new float[4];
    public static float SCALE = 0.005F;// 缩放暂时使用这个

    private LatLng center = new LatLng(28.21149245890368,112.87748234731855);// 北京市经纬度

    private BuildingOverlay cube ;

    private AMap aMap;
    private Context mcontext;

    float width, height;

    public GLMapRender(AMap aMap, Context context) {
        this.aMap = aMap;
        mcontext = context;
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center,15));
    }

    float[] mvp = new float[16];

    @Override
    public void onDrawFrame(GL10 gl) {

        if(cube != null) {
            Matrix.setIdentityM(mvp, 0);

            //偏移
            PointF pointF = aMap.getProjection().toOpenGLLocation(center);

            Matrix.multiplyMM(mvp,0, aMap.getProjectionMatrix(),0,aMap.getViewMatrix(),0);

            Matrix.translateM(mvp, 0 , pointF.x , pointF.y  , 0);
            int scale = 1;
            Matrix.scaleM(mvp, 0 , scale, scale, scale);

            cube.drawES20(mvp);
        }

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //创建cube
        cube = new BuildingOverlay(aMap,gps2gaode(new LatLng(28.21149245890368,112.87748234731855)),
                gps2gaode(new LatLng(28.211270510430364,112.87755440532865)),
                        gps2gaode(new LatLng(28.21165090324281,112.87805965348026)),
                                gps2gaode(new LatLng(28.21140894759759,112.8781247075325)));


        cube.addFloorInfoInBuilding(1,0, loadImgBitmap(R.mipmap.indoor1234),new float[]{1f,1f,0f,0f});
        cube.addFloorInfoInBuilding(2,1, loadImgBitmap(R.mipmap.indoor1234),new float[]{1f,1f,0f,0f});
        cube.addFloorInfoInBuilding(3,2, loadImgBitmap(R.mipmap.indoor1234),new float[]{1f,1f,0f,0f});
        cube.addFloorInfoInBuilding(4,3, loadImgBitmap(R.mipmap.indoor1234),new float[]{1f,1f,0f,0f});
        cube.addFloorInfoInBuilding(5,4, loadImgBitmap(R.mipmap.indoor1234),new float[]{1f,1f,0f,0f});
        cube.addFloorInfoInBuilding(6,5, loadImgBitmap(R.mipmap.indoor1234),new float[]{1f,1f,0f,0f});
     //   cube.updateVertexPostion();

        cube.initShader();
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

    private Bitmap loadImgBitmap(int resid){

        return BitmapFactory.decodeResource(mcontext.getResources(), resid);
    }
}
