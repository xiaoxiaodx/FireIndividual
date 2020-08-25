package com.example.firecommandandcontrolsystem.myClass;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.firecommandandcontrolsystem.R;

import java.util.List;


public class FloorInfo extends View {

    private Context mycontext;

    private Paint paintAxis;

    private String[] xnums = {"0", "2", "4", "6", "8", "10"};
    private String[] ynums = {"-3", "-2", "-1", "0", "1", "2", "3", "4", "5"};

    private static final int startX = 30;
    private static final int scaleSize = 4;

    public FloorInfo(Context context) {
        super(context);
        mycontext = context;
    }

    public FloorInfo(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);


        mycontext = context;
        this.getMeasuredHeight();
    }

    private int getTextWidth(Paint paint, String str) {
        int iRet = 0;
        if (str != null && str.length() > 0) {
            int len = str.length();
            float[] widths = new float[len];
            paint.getTextWidths(str, widths);
            for (int j = 0; j < len; j++) {
                iRet += (int) Math.ceil(widths[j]);
            }
        }
        return iRet;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);


        paintAxis = new Paint();
        paintAxis.setStyle(Paint.Style.STROKE);
        paintAxis.setDither(true);
        paintAxis.setAntiAlias(true);
        paintAxis.setColor(ContextCompat.getColor(getContext(), R.color.fontcolor_dep3));
        paintAxis.setTextSize(20);


        int Max_Y = this.getMeasuredHeight();
        int Max_X = this.getMeasuredWidth();

        if (Max_X == 0 || Max_Y == 0)
            return;
        int LINE_NUM_Y = ynums.length;
        int LINE_NUM_X = xnums.length;

        Log.e("ssssssssss", "" + Max_X);
        int axisXW = Max_X - startX;
        int axisYH = Max_Y - startX;
        //画X轴
        canvas.drawLine(startX, axisYH, Max_X, axisYH, paintAxis);
        for (int i = 0; i < LINE_NUM_X; i++) {
            //画刻度
            if (i == 0)
                canvas.drawLine(startX + axisXW / LINE_NUM_X * i, axisYH, startX + axisXW / LINE_NUM_X * i, 0, paintAxis);
            else
                canvas.drawLine(startX + axisXW / LINE_NUM_X * i, axisYH, startX + axisXW / LINE_NUM_X * i, axisYH + scaleSize, paintAxis);


            Rect rect = new Rect();
            paintAxis.getTextBounds(ynums[i], 0, ynums[i].length(), rect);
            int w = rect.width();
            int h = rect.height();
            canvas.drawText(xnums[i], startX + axisXW / LINE_NUM_X * i + 5, axisYH + h + 5, paintAxis);
        }

        //画Y轴
        for (int i = 0; i < LINE_NUM_Y; i++) {
            //画横线
            canvas.drawLine(startX - scaleSize, axisYH - axisYH / LINE_NUM_Y * i, Max_X, axisYH - axisYH / LINE_NUM_Y * i, paintAxis);

            Rect rect = new Rect();
            paintAxis.getTextBounds(ynums[i], 0, ynums[i].length(), rect);
            int w = rect.width();
            int h = rect.height();
            canvas.drawText(ynums[i], startX - scaleSize - w - 5, axisYH - axisYH / LINE_NUM_Y * i - axisYH / LINE_NUM_Y / 2 + h / 2, paintAxis);
        }


        DataApplication dataApplication = (DataApplication) mycontext.getApplicationContext();
        List<Firemen> listFiremen = dataApplication.listFiremen;


        for (int i = 0; i < listFiremen.size(); i++) {

            //axisYH - axisYH/LINE_NUM_Y*i
            Firemen firemen = listFiremen.get(i);

            int floor = firemen.getFloor();
            int deviceid = Integer.valueOf(firemen.getBindDeviceId());
            for (int j = 0; j < ynums.length; j++) {

                if (ynums[j] == String.valueOf(floor)) {

                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), dataApplication.getIamgeRes(deviceid));

                    canvas.drawBitmap(bitmap, 100, axisYH - axisYH / LINE_NUM_Y * j, paintAxis);

                    break;
                }
            }

        }
    }



}
