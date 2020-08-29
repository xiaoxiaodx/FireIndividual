package com.example.firecommandandcontrolsystem.myClass;

import android.graphics.Color;

import com.example.firecommandandcontrolsystem.R;


public class MyImageColor {


    //标识
    static public int identity = 0;


    static public int getIdColor(int id) {

        switch (id) {

            case 0:
                return Color.parseColor("#ff0000");
            case 1:
                return Color.parseColor("#f4ea2a");

            case 2:
                return Color.parseColor("#1afa29");

            case 3:
                return Color.parseColor("#84af1c");

            case 4:
                return Color.parseColor("#1296db");

            case 5:
                return Color.parseColor("#3317d1");

            case 6:
                return Color.parseColor("#13227a");

            case 7:
                return Color.parseColor("#052409");

            case 8:
                return Color.parseColor("#d81e06");

            case 9:
                return Color.parseColor("#d4237a");

            case 10:
                return Color.parseColor("#e0620d");

        }

        return Color.parseColor("#000000");

    }



    static public float[] selectRGBAByID(int id) {


        switch (id) {
            case 1:
                return new float[]{0, 0, 1, 1};

            case 2:
                return new float[]{0, 1, 0, 1};

            case 3:
                return new float[]{1, 0, 0, 1};

            case 4:
                return new float[]{1, 1, 0, 1};

            case 5:
                return new float[]{1, 0, 1, 1};

            case 6:
                return new float[]{0, 1, 1, 1};

            default:
                return new float[]{0, 0, 0, 1};
        }
    }


    static  public int getIamgeRes(int id) {
        int res = 1;
        switch (id) {
            case 1:
                res = R.mipmap.marker_1;
                break;
            case 2:
                res = R.mipmap.marker_2;
                break;
            case 3:
                res = R.mipmap.marker_3;
                break;
            case 4:
                res = R.mipmap.marker_4;
                break;
            case 5:
                res = R.mipmap.marker_5;
                break;
            case 6:
                res = R.mipmap.marker_6;
                break;
            case 7:
                res = R.mipmap.marker_7;
                break;
            case 8:
                res = R.mipmap.marker_8;
                break;
            case 9:
                res = R.mipmap.marker_9;
                break;
            case 10:
                res = R.mipmap.marker_10;
                break;
        }
        return res;
    }

}
