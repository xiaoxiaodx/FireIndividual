package com.example.firecommandandcontrolsystem.myClass;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.firecommandandcontrolsystem.R;


public class RescueLocation extends View {



    private Paint paintAxis;

    private String[] xnums = {"",  "5", "10",  "15", "20", "25", "35","45"};
    private String[] ynums = {"基站","-2","-1","0", "1", "2", "3", "4", "5"};

    private static final int startX = 50;
    private static final int scaleSize = 4;

    public RescueLocation(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        paintAxis = new Paint();
        paintAxis.setStyle(Paint.Style.STROKE);
        paintAxis.setDither(true);
        paintAxis.setAntiAlias(true);
        paintAxis.setColor(ContextCompat.getColor(getContext(), R.color.fontcolor_dep2));
        paintAxis.setTextSize(20);

        int Max_Y  = this.getMeasuredHeight();
        int Max_X = this.getMeasuredWidth();

        if(Max_X == 0 || Max_Y ==0)
            return;
        int LINE_NUM_Y = ynums.length;
        int LINE_NUM_X = xnums.length;

        Log.e("ssssssssss",""+Max_X);
        int axisXW = Max_X - startX;
        int axisYH = Max_Y - startX;
        //画X轴
        canvas.drawLine(startX,axisYH, Max_X, axisYH, paintAxis);
        for (int i = 0;i<LINE_NUM_X;i++){
            //画刻度
            if(i == 0)
                canvas.drawLine(startX + axisXW / LINE_NUM_X * i, axisYH, startX + axisXW / LINE_NUM_X * i, 0, paintAxis);
            else {

                canvas.drawLine(startX + axisXW / LINE_NUM_X * i, axisYH, startX + axisXW / LINE_NUM_X * i, axisYH - 10, paintAxis);
                paintAxis.setAlpha(50);
                canvas.drawLine(startX + axisXW / LINE_NUM_X * i, axisYH - 10, startX + axisXW / LINE_NUM_X * i, 0, paintAxis);
            }
            paintAxis.setAlpha(255);
            Rect rect = new Rect();
            paintAxis.getTextBounds(ynums[i], 0, ynums[i].length(), rect);
            int w = rect.width();
            int h = rect.height();
            canvas.drawText(xnums[i],startX + axisXW/LINE_NUM_X*i - w/2,axisYH + h +10,paintAxis);
        }

        //画Y轴
        for (int i = 0; i < LINE_NUM_Y; i++) {
            if(i == 0) {

            }else {

                canvas.drawLine(startX, axisYH - axisYH/LINE_NUM_Y*i, startX+10, axisYH - axisYH/LINE_NUM_Y*i, paintAxis);
                paintAxis.setAlpha(50);
                canvas.drawLine(startX+10, axisYH - axisYH/LINE_NUM_Y*i, Max_X, axisYH - axisYH/LINE_NUM_Y*i, paintAxis);
            }

            paintAxis.setAlpha(255);
            Rect rect = new Rect();
            paintAxis.getTextBounds(ynums[i], 0, ynums[i].length(), rect);
            int w = rect.width();
            int h = rect.height();
            canvas.drawText(ynums[i],startX-scaleSize -w-5,axisYH - axisYH/LINE_NUM_Y*i + h/2,paintAxis);
        }
    }

}
