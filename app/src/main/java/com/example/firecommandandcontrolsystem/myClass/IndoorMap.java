package com.example.firecommandandcontrolsystem.myClass;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.opengl.Matrix;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.example.firecommandandcontrolsystem.MainActivity;
import com.example.firecommandandcontrolsystem.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class IndoorMap extends View {

    private Context mycontext;


    private int curFloor = 1;

    public IndoorMap(Context context) {
        super(context);
        mycontext = context;
    }

    public IndoorMap(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mycontext = context;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);


        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bitmap = null;
        if(curFloor == 1)
            bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.indoor1, options);
        else if(curFloor ==2)
            bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.indoor2, options);
        else if(curFloor ==3)
            bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.indoor3, options);
        else if(curFloor ==4)
            bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.indoor4, options);
        else
            bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.indoor1234, options);
        //获取图片的宽高
        int height = options.outHeight;
        int width = options.outWidth;

        float hwRadio = (float) height / (float) width;

        int windowW = getMeasuredWidth();
        int windowH = getMeasuredHeight();
        Log.i("wk", "图片的宽度:" + width + "图片的高度:" + height);
        Log.i("wkradio", "whRadio:" + hwRadio);
        Log.i("wk", "窗口宽度:" + windowW + "窗口高度:" + windowH);

        float tmpH = windowW * hwRadio;
        int imgShowW = 0;
        int imgShowH = 0;
        if (tmpH > getMeasuredHeight()) {
            imgShowH = windowH;
            imgShowW = (int) (imgShowH / hwRadio);
        } else {
            imgShowW = windowW;
            imgShowH = (int) (imgShowW * hwRadio);
        }


        Rect srcRect = new Rect(0, 0, width, height);
        Rect desRect = new Rect((windowW - imgShowW) / 2, (windowH - imgShowH) / 2, (windowW - imgShowW) / 2 + imgShowW, (windowH - imgShowH) / 2 + imgShowH);

        canvas.drawBitmap(bitmap, srcRect, desRect, new Paint());


        double[] left_top = {MyMapView.indoor_leftTop.latitude, MyMapView.indoor_leftTop.longitude, 0};
        double[] left_bottom = {MyMapView.indoor_leftBottom.latitude, MyMapView.indoor_leftBottom.longitude, 0};
        double[] right_top = {MyMapView.indoor_rightTop.latitude, MyMapView.indoor_rightTop.longitude, 0};
        double[] right_bottom = {MyMapView.indoor_rightBottom.latitude, MyMapView.indoor_rightBottom.longitude, 0};


        set_blh_to_map(left_top, left_bottom, right_top, right_bottom, MyMapView.indoor_v_long / imgShowH, imgShowW, imgShowH);


//        double[] ptlefttop = get_map_x_y(new double[]{MyMapView.indoor_leftTop.longitude,MyMapView.indoor_leftTop.latitude,0});
//        double[] ptrightbottom = get_map_x_y(new double[]{MyMapView.indoor_rightBottom.longitude,MyMapView.indoor_rightBottom.latitude,0});

//        double[] ptlefttop = get_map_x_y(new double[]{MyMapView.indoor_leftTop.latitude,MyMapView.indoor_leftTop.longitude,0});
//        double[] ptleftbottom = get_map_x_y(new double[]{MyMapView.indoor_leftBottom.latitude,MyMapView.indoor_leftBottom.longitude,0});
//
//        double[] ptrighttop = get_map_x_y(new double[]{MyMapView.indoor_rightTop.latitude,MyMapView.indoor_rightTop.longitude,0});
//        double[] ptrightbottom = get_map_x_y(new double[]{MyMapView.indoor_rightBottom.latitude,MyMapView.indoor_rightBottom.longitude,0});


//        Log.e("test0","indoor:"+imgShowW + ","+imgShowH);
//
//        Log.e("test0","indoor   lefttop:"+ptlefttop[0]+","+ptlefttop[1]);
//
//        Log.e("test0","indoor   leftbottom:"+ptleftbottom[0]+","+ptleftbottom[1]);
//
//        Log.e("test0","indoor   righttop:"+ptrighttop[0]+","+ptrighttop[1]);
//
//        Log.e("test0","indoor   rightbottom:"+ptrightbottom[0]+","+ptrightbottom[1]);

        drawLines((windowW - imgShowW) / 2, (windowH - imgShowH) / 2, imgShowW, imgShowH, canvas);

    }

    public void update() {

        this.invalidate();
    }

    public void updateSelectFloor(int floor) {

        curFloor = floor;

        this.invalidate();
    }

    private void drawLines(int offsetX, int offsetY, int imgW, int imgH, Canvas canvas) {


        DataApplication dataApplication = (DataApplication) mycontext.getApplicationContext();

        List<Firemen> listfiremen = dataApplication.listFiremen;


        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setDither(true);
        paint.setStrokeWidth(5);
        paint.setAntiAlias(true);


        for (int i = 0; i < listfiremen.size(); i++) {

            Firemen firemen = listfiremen.get(i);

            List<Firemen.LatLngFloor> listFloorTrack = firemen.listIndoorPoint;

            int lastindex = -1;
            for (int k = 0; k < listFloorTrack.size(); k++) {

                Firemen.LatLngFloor indoorPt = listFloorTrack.get(k);

                if (indoorPt.floor == curFloor) {

                    paint.setColor(Color.parseColor(firemen.getShowColorStr()));

                    double[] localPt = {indoorPt.latLng.latitude, indoorPt.latLng.longitude, 0};
                    double[] screenPt = get_map_x_y(localPt);
                    float x = (float) (offsetX + screenPt[0]);
                    float y = (float) (offsetY + screenPt[1]);
                    canvas.drawPoint(x, y, paint);
                    // canvas.drawPoints(pts, paint);
                    lastindex = k;
                }
            }

            if(lastindex > -1) {
                LatLng lastPt = listFloorTrack.get(lastindex).latLng;
                double[] localPt = {lastPt.latitude, lastPt.longitude, 0};
                double[] screenPt = get_map_x_y(localPt);
                Bitmap bitmap = firemen.getBitmap();
                canvas.drawBitmap(bitmap, offsetX +(float) screenPt[0] - bitmap.getWidth() / 2, offsetY+(float) screenPt[1] - bitmap.getHeight(), paint);
            }
        }

    }

//    private void drawLines(int offsetX, int offsetY, int imgW, int imgH, Canvas canvas) {
//
//
//        DataApplication dataApplication = (DataApplication) mycontext.getApplicationContext();
//
//        List<Firemen> listfiremen = dataApplication.listFiremen;
//
//
//        Paint paint = new Paint();
//        paint.setStyle(Paint.Style.STROKE);
//        paint.setDither(true);
//        paint.setStrokeWidth(5);
//        paint.setAntiAlias(true);
//        paint.setColor(ContextCompat.getColor(getContext(), R.color.fontcolor_dep3));
//
//
//        Point ptLefttop = MyMapView.aMap.getProjection().toScreenLocation(MyMapView.indoor_leftTop);
//        Point ptLeftbottom = MyMapView.aMap.getProjection().toScreenLocation(MyMapView.indoor_leftBottom);
//        Point ptRighttop = MyMapView.aMap.getProjection().toScreenLocation(MyMapView.indoor_rightTop);
//        Point ptRightbottom = MyMapView.aMap.getProjection().toScreenLocation(MyMapView.indoor_rightBottom);
//
//        float wdx = ptRighttop.x - ptLefttop.x;
//        float wdy = ptRighttop.y - ptLefttop.x;
//
//        float hdx = ptLeftbottom.x - ptLefttop.x;
//        float hdy = ptLeftbottom.y - ptLefttop.y;
//        MyMapView.indoor_buildingW = Math.sqrt(wdx * wdx + wdy * wdy);
//        MyMapView.indoor_buildingH = Math.sqrt(hdx * hdx + hdy * hdy);
//
//
//        double radioW = (double) imgW / MyMapView.indoor_buildingW;
//        double radioH = (double) imgH / MyMapView.indoor_buildingW;
//        Log.e("test", "radio:" + radioW + "," + radioH + "," + offsetX + "," + offsetY);
//
//        for (int i = 0; i < listfiremen.size(); i++) {
//
//            Firemen firemen = listfiremen.get(i);
//
//            HashMap<Integer, List<LatLng>> hashMap = firemen.listTrackHashMap;
//
//            for (Integer floor : hashMap.keySet()) {
//                Log.e("test", "key=" + floor + " value=" + hashMap.get(floor));
//
//
////                if (floor != dataApplication.indoormap_floor_curSelectIndex)
////                    break;
//                List<LatLng> latLngs = firemen.listTrackHashMap.get(floor);
//                float[] pts = new float[latLngs.size() * 2];
//                for (int j = 0; j < latLngs.size(); j++) {
//
//                    LatLng curlatlbf = latLngs.get(j);
//                    Point ScreenPt = MyMapView.aMap.getProjection().toScreenLocation(curlatlbf);
//                    float dx = ScreenPt.x - ptLefttop.x;
//                    float dy = ScreenPt.y - ptLefttop.y;
//                    pts[j * 2] = (float) (offsetX + dx * radioW);
//                    pts[j * 2 + 1] = (float) (offsetY + dy * radioH);
//
//                    Log.e("test", "points:" + pts[j * 2] + "," + pts[j * 2 + 1]);
//                }
//                canvas.drawPoints(pts, paint);
//            }
//
//
//        }
//
//    }


    final double deg = 3.1415926 / 180;
    final double Re = 6378137;
    final double f = 1 / 298.257;
    final double Rp = (1 - f) * Re;
    final double e2 = Math.pow(Math.sqrt(2 * f - Math.pow(f, 2.0)), 2.0);
    final double ep2 = Math.pow(Math.sqrt(Math.pow(Re, 2.0) - Math.pow(Rp, 2.0)) / Rp, 2.0);
    double[] xyz0_array_3 = new double[3];
    double[] Cne0_array_9 = new double[9];
    double delta_theta = 0.0;
    double tx = 0.0;
    double ty = 0.0;
    double map_resolution = 0.0;


    void set_blh_to_map(double[] left_up_blh_array, double[] left_down_blh_array, double[] right_up_blh_array, double[] right_down_blh_array, double tmp_map_resolution, double map_wight, double map_high) {
        double[] left_up_xyz_array_3 = new double[3];
        double[] left_down_xyz_array_3 = new double[3];
        double[] right_up_xyz_array_3 = new double[3];
        double[] right_down_xyz_array_3 = new double[3];
        double[] Cne_array_9 = new double[9];
        double[] xyz_array_3 = new double[3];

        double tmp_delta_theta = 0.0;
        double tmp_tx = 0.0;
        double tmp_ty = 0.0;
        double[][] return_data = new double[2][];
        return_data[0] = new double[3];
        return_data[1] = new double[9];

        return_data = blh2xyz(left_down_blh_array);
        xyz0_array_3 = return_data[0];
        Cne0_array_9 = return_data[1];
        left_down_xyz_array_3[0] = 0.0;
        left_down_xyz_array_3[1] = 0.0;
        left_down_xyz_array_3[2] = 0.0;

        return_data = blh2xyz(left_up_blh_array);
        xyz_array_3 = return_data[0];
        Cne_array_9 = return_data[1];
        xyz_array_3[0] = xyz_array_3[0] - xyz0_array_3[0];
        xyz_array_3[1] = xyz_array_3[1] - xyz0_array_3[1];
        xyz_array_3[2] = xyz_array_3[2] - xyz0_array_3[2];
        left_up_xyz_array_3[0] = Cne0_array_9[0] * xyz_array_3[0] + Cne0_array_9[3] * xyz_array_3[1] + Cne0_array_9[6] * xyz_array_3[2];
        left_up_xyz_array_3[1] = Cne0_array_9[1] * xyz_array_3[0] + Cne0_array_9[4] * xyz_array_3[1] + Cne0_array_9[7] * xyz_array_3[2];
        left_up_xyz_array_3[2] = Cne0_array_9[2] * xyz_array_3[0] + Cne0_array_9[5] * xyz_array_3[1] + Cne0_array_9[8] * xyz_array_3[2];

        return_data = blh2xyz(right_up_blh_array);
        xyz_array_3 = return_data[0];
        Cne_array_9 = return_data[1];
        xyz_array_3[0] = xyz_array_3[0] - xyz0_array_3[0];
        xyz_array_3[1] = xyz_array_3[1] - xyz0_array_3[1];
        xyz_array_3[2] = xyz_array_3[2] - xyz0_array_3[2];
        right_up_xyz_array_3[0] = Cne0_array_9[0] * xyz_array_3[0] + Cne0_array_9[3] * xyz_array_3[1] + Cne0_array_9[6] * xyz_array_3[2];
        right_up_xyz_array_3[1] = Cne0_array_9[1] * xyz_array_3[0] + Cne0_array_9[4] * xyz_array_3[1] + Cne0_array_9[7] * xyz_array_3[2];
        right_up_xyz_array_3[2] = Cne0_array_9[2] * xyz_array_3[0] + Cne0_array_9[5] * xyz_array_3[1] + Cne0_array_9[8] * xyz_array_3[2];

        return_data = blh2xyz(right_down_blh_array);
        xyz_array_3 = return_data[0];
        Cne_array_9 = return_data[1];
        xyz_array_3[0] = xyz_array_3[0] - xyz0_array_3[0];
        xyz_array_3[1] = xyz_array_3[1] - xyz0_array_3[1];
        xyz_array_3[2] = xyz_array_3[2] - xyz0_array_3[2];
        right_down_xyz_array_3[0] = Cne0_array_9[0] * xyz_array_3[0] + Cne0_array_9[3] * xyz_array_3[1] + Cne0_array_9[6] * xyz_array_3[2];
        right_down_xyz_array_3[1] = Cne0_array_9[1] * xyz_array_3[0] + Cne0_array_9[4] * xyz_array_3[1] + Cne0_array_9[7] * xyz_array_3[2];
        right_down_xyz_array_3[2] = Cne0_array_9[2] * xyz_array_3[0] + Cne0_array_9[5] * xyz_array_3[1] + Cne0_array_9[8] * xyz_array_3[2];

        if (Math.abs(tmp_map_resolution) < 0.000001) {
            map_resolution = Math.sqrt(Math.pow(left_down_xyz_array_3[0] - right_down_xyz_array_3[0], 2) + Math.pow(left_down_xyz_array_3[1] - right_down_xyz_array_3[1], 2)) / map_wight;
            //map_resolution = map_resolution + Math.sqrt(Math.pow(left_down_xyz_array_3[0],2) + Math.pow(left_down_xyz_array_3[1],2))  / map_high;
            //map_resolution = map_resolution / 2;

        } else {
            map_resolution = tmp_map_resolution;
        }
        tmp_delta_theta = Math.atan2(-map_high * map_resolution + map_high * map_resolution, map_wight * map_resolution) - Math.atan2(right_down_xyz_array_3[1] - left_down_xyz_array_3[1], right_down_xyz_array_3[0] - left_down_xyz_array_3[0]);
        //tmp_delta_theta = tmp_delta_theta + Math.atan2(map_high*map_resolution, map_wight*map_resolution) - Math.atan2(right_up_xyz_array_3[1] - left_down_xyz_array_3[1], right_up_xyz_array_3[0] - left_down_xyz_array_3[0]);
        //tmp_delta_theta = tmp_delta_theta/2;

        delta_theta = tmp_delta_theta;
        tx = 0.0;
        ty = -map_high * map_resolution;


    }

    double[] get_map_x_y(double[] blh_array_3) {
        double[] Cne_array_9 = new double[9];
        double[] xyz_array_3 = new double[3];
        double[] tmp_xyz_array_3 = new double[3];
        double[] map_xyz_array_3 = new double[3];
        double[][] return_data = new double[2][];
        return_data[0] = new double[3];
        return_data[1] = new double[9];
        double[] map_x_y_z_array_3 = new double[3];

        return_data = blh2xyz(blh_array_3);
        xyz_array_3 = return_data[0];
        Cne_array_9 = return_data[1];

        xyz_array_3[0] = xyz_array_3[0] - xyz0_array_3[0];
        xyz_array_3[1] = xyz_array_3[1] - xyz0_array_3[1];
        xyz_array_3[2] = xyz_array_3[2] - xyz0_array_3[2];
        tmp_xyz_array_3[0] = Cne0_array_9[0] * xyz_array_3[0] + Cne0_array_9[3] * xyz_array_3[1] + Cne0_array_9[6] * xyz_array_3[2];
        tmp_xyz_array_3[1] = Cne0_array_9[1] * xyz_array_3[0] + Cne0_array_9[4] * xyz_array_3[1] + Cne0_array_9[7] * xyz_array_3[2];
        tmp_xyz_array_3[2] = Cne0_array_9[2] * xyz_array_3[0] + Cne0_array_9[5] * xyz_array_3[1] + Cne0_array_9[8] * xyz_array_3[2];

        map_xyz_array_3[0] = Math.cos(delta_theta) * tmp_xyz_array_3[0] - Math.sin(delta_theta) * tmp_xyz_array_3[1] + tx;
        map_xyz_array_3[1] = Math.sin(delta_theta) * tmp_xyz_array_3[0] + Math.cos(delta_theta) * tmp_xyz_array_3[1] + ty;
        map_xyz_array_3[2] = blh_array_3[2];

        map_x_y_z_array_3[0] = map_xyz_array_3[0] / map_resolution;
        map_x_y_z_array_3[1] = -map_xyz_array_3[1] / map_resolution;
        map_x_y_z_array_3[2] = map_xyz_array_3[2];

        return map_x_y_z_array_3;
    }


    double[][] blh2xyz(double[] blh_array_3) {
        double sB;
        double cB;
        double sL;
        double cL;
        double N;
        double[][] return_data = new double[2][];
        return_data[0] = new double[3];
        return_data[1] = new double[9];
        double[] xyz_array_3 = new double[3];
        double[] Cne_array_9 = new double[9];

        sB = Math.sin(blh_array_3[0] * Math.PI / 180);
        cB = Math.cos(blh_array_3[0] * Math.PI / 180);
        sL = Math.sin(blh_array_3[1] * Math.PI / 180);
        cL = Math.cos(blh_array_3[1] * Math.PI / 180);
        N = Re / Math.sqrt(1.0 - e2 * (sB * sB));

        xyz_array_3[0] = (N + blh_array_3[2]) * cB * cL;
        xyz_array_3[1] = (N + blh_array_3[2]) * cB * sL;
        xyz_array_3[2] = (N * (1.0 - e2) + blh_array_3[2]) * sB;
        Cne_array_9[0] = -sL;
        Cne_array_9[1] = -sB * cL;
        Cne_array_9[2] = cB * cL;
        Cne_array_9[3] = cL;
        Cne_array_9[4] = -sB * sL;
        Cne_array_9[5] = cB * sL;
        Cne_array_9[6] = 0.0;
        Cne_array_9[7] = cB;
        Cne_array_9[8] = sB;
        return_data[0] = xyz_array_3;
        return_data[1] = Cne_array_9;

        return return_data;
    }

    double[][] xyz2blh(double[] xyz_array_3) {
        double s;
        double theta;
        double L;
        double B;
        double sB;
        double cB;
        double sL;
        double cL;
        double[][] return_data = new double[2][];
        return_data[0] = new double[3];
        return_data[1] = new double[9];
        double[] blh_array_3 = new double[3];
        double[] Cne_array_9 = new double[3];

        s = Math.sqrt(xyz_array_3[0] * xyz_array_3[0] + xyz_array_3[1] * xyz_array_3[1]);

        theta = Math.atan2(xyz_array_3[2] * Re, s * Rp);

        L = Math.atan2(xyz_array_3[1], xyz_array_3[0]);

        B = Math.atan2(xyz_array_3[2] + ep2 * Rp * Math.pow(Math.sin(theta), 3.0), s
                - e2 * Re * Math.pow(Math.cos(theta), 3.0));

        sB = Math.sin(B);
        cB = Math.cos(B);
        blh_array_3[0] = B;
        blh_array_3[1] = L;
        blh_array_3[2] = s / cB - Re / Math.sqrt(1.0 - e2 * (sB * sB));


        sL = Math.sin(L);
        cL = Math.cos(L);
        Cne_array_9[0] = -sL;
        Cne_array_9[1] = -sB * cL;
        Cne_array_9[2] = cB * cL;
        Cne_array_9[3] = cL;
        Cne_array_9[4] = -sB * sL;
        Cne_array_9[5] = cB * sL;
        Cne_array_9[6] = 0.0;
        Cne_array_9[7] = cB;
        Cne_array_9[8] = sB;

        return_data[0] = blh_array_3;
        return_data[1] = Cne_array_9;

        return return_data;

    }


}