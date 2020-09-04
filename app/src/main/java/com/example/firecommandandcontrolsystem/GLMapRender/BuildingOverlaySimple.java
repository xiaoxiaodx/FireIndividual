package com.example.firecommandandcontrolsystem.GLMapRender;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES10.GL_BLEND;
import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.glGenTextures;
import static javax.microedition.khronos.opengles.GL10.GL_LIGHTING;
import static javax.microedition.khronos.opengles.GL10.GL_ONE_MINUS_SRC_ALPHA;
import static javax.microedition.khronos.opengles.GL10.GL_SRC_ALPHA;

public class BuildingOverlaySimple {

    ArrayList<Float> verticesList = new ArrayList<Float>();
    ArrayList<Float> colorList = new ArrayList<Float>();
    ArrayList<Short> indexverticesList = new ArrayList<Short>();
//
//    float vertex[] = {
//            -10000, -10000, 0,
//            10000, -10000, 0,
//            10000, 10000, 0,
//            -10000, 10000, 0,
//
//            -10000, -10000, 10000,
//            10000, -10000, 10000,
//            10000, 10000, 10000,
//            -10000, 10000, 10000,
//
//
//            -10000, -10000, 20000,
//            10000, -10000, 20000,
//            10000, 10000, 20000,
//            -10000, 10000, 20000,
//
//            -10000, -10000, 40000,
//            10000, -10000, 40000,
//            10000, 10000, 40000,
//            -10000, 10000, 40000,
//
//            -10000, -10000, 50000,
//            10000, -10000, 50000,
//            10000, 10000, 50000,
//            -10000, 10000, 50000,
//
//            -10000, -10000, 60000,
//            10000, -10000, 60000,
//            10000, 10000, 60000,
//            -10000, 10000, 60000,
//    };
//
//    float[] textureData = {
//            0.0f, 1.0f, 0.1f,
//            1.0f, 1.0f, 0.1f,
//            1.0f, 0.0f, 0.1f,
//            0.0f, 0.0f, 0.1f,
//
//            0.0f, 1.0f, 0.2f,
//            1.0f, 1.0f, 0.2f,
//            1.0f, 0.0f, 0.2f,
//            0.0f, 0.0f, 0.2f,
//
//            0.0f, 1.0f, 0.3f,
//            1.0f, 1.0f, 0.3f,
//            1.0f, 0.0f, 0.3f,
//            0.0f, 0.0f, 0.3f,
//
//            0.0f, 1.0f, 0.4f,
//            1.0f, 1.0f, 0.4f,
//            1.0f, 0.0f, 0.4f,
//            0.0f, 0.0f, 0.4f,
//
//            0.0f, 1.0f, 0.5f,
//            1.0f, 1.0f, 0.5f,
//            1.0f, 0.0f, 0.5f,
//            0.0f, 0.0f, 0.5f,
//
//            0.0f, 1.0f, 0.6f,
//            1.0f, 1.0f, 0.6f,
//            1.0f, 0.0f, 0.6f,
//            0.0f, 0.0f, 0.6f,
//    };
//
//
//    short indices[] = {
//            0, 1, 2,
//            2, 3, 0,
//
//            4, 5, 6,
//            6, 7, 4,
//
//            8, 9, 10,
//            10, 11, 8,
//
//            12, 13, 14,
//            14, 15, 12,
//
//            16,17,18,
//            18,19,16,
//
//            20,21,22,
//            22,23,20,
//
//            0, 1, 21,
//            20, 21, 0,
//
//            1, 2, 22,
//            21, 22, 1,
//
//            2, 3, 23,
//            22, 23, 2,
//
//            3, 0, 20,
//            23, 20, 3
//    };
//
//
//    float[] colors = {
//            1f, 0f, 0f, 1f, // vertex 0 red
//            0f, 1f, 0f, 1f, // vertex 1 green
//            0f, 0f, 1f, 1f, // vertex 2 blue
//            1f, 1f, 0f, 1f, // vertex 3
//
//            0f, 1f, 1f, 0f, // vertex 4
//            1f, 0f, 1f, 0f, // vertex 5
//            0f, 0f, 0f, 0f, // vertex 6
//            1f, 1f, 1f, 0f, // vertex 7
//
//            0f, 1f, 1f, 1f, // vertex 4
//            1f, 0f, 1f, 1f, // vertex 5
//            0f, 0f, 0f, 1f, // vertex 6
//            1f, 1f, 1f, 1f, // vertex 7
//    };


    AMap aMap;

    public BuildingOverlaySimple(AMap amap) {

        this.aMap = amap;


        Log.e("test", "BuildingOverlaySimple");
//        updateVertexBuff();
    }


    private FloatBuffer vertextBuffer;
    private ShortBuffer indexBuffer;
    private FloatBuffer colorBuffer;



    public void updateVertexPostion(List<LatLng> latLngs, int floorNum, float firstFloorHeigh, float eachFloorHeight) {

        verticesList.clear();
        indexverticesList.clear();
        colorList.clear();


        //Log.e(" updateVertexPostion",floorNum+","+firstFloorHeigh+","+eachFloorHeight);
        int i = 0;
        //总点数
        int ptsSize = latLngs.size();


        for (; i < floorNum; i++) {

            for (int j = 0; j < ptsSize; j++) {
                PointF glptf = aMap.getProjection().toOpenGLLocation(latLngs.get(j));

                verticesList.add(glptf.x);
                verticesList.add(glptf.y);
                if (i == 0)
                    verticesList.add(0f);
                else if (i == 1)
                    verticesList.add(firstFloorHeigh * GLMapRender.heightRadio);
                else {
                    float height = firstFloorHeigh + eachFloorHeight * (i - 1);
                    verticesList.add(height * GLMapRender.heightRadio);
                }

                if(i==0 || i == floorNum-1){

                    colorList.add(0.023f);
                    colorList.add(0.145f);
                    colorList.add(0.22f);
                    colorList.add(0.4f);


                }else{
                    colorList.add(0.023f);
                    colorList.add(0.145f);
                    colorList.add(0.22f);
                    colorList.add(0.2f);

                }
            }


            //添加索引，每次循环为4个点
            int indexstart = i * ptsSize;
            addFloatInindexList((short) indexstart, ptsSize);
        }

        Log.e("test1", verticesList.toString());
        Log.e("test2", indexverticesList.toString());


        addFloatInindexList1((short) ((i - 1) * ptsSize), ptsSize);


        updateVertexBuff();
    }

    //侧面，默认是连续的2个平面,每个平面4个点
    private void addFloatInindexList1(short indexstart, int ptsSize) {

        short bottomstart = 0;
        short topstart = indexstart;

        for (int i = 0; i < ptsSize; i++) {

            indexverticesList.add((short) (bottomstart + i));
            if (i == ptsSize - 1) {
                indexverticesList.add((short) (bottomstart));
                indexverticesList.add((short) (topstart));
            } else {
                indexverticesList.add((short) (bottomstart + i + 1));
                indexverticesList.add((short) (topstart + i + 1));
            }
            indexverticesList.add((short) (topstart + i));
            if (i == ptsSize - 1) {
                indexverticesList.add((short) (topstart));
            } else {
                indexverticesList.add((short) (topstart + i + 1));
            }
            indexverticesList.add((short) (bottomstart + i));
        }
    }

    //只管水平面的  不管侧面
    private void addFloatInindexList(short indexstart, int ptsSize) {


        for (int i = 0; i < ptsSize - 2; i++) {
            indexverticesList.add(indexstart);
            indexverticesList.add((short) (indexstart + i + 1));
            indexverticesList.add((short) (indexstart + i + 2));
        }


    }

    private void addFloatInInverticesList(PointF lt, PointF lb, PointF rt, PointF rb, float height) {
        verticesList.add(lt.x);
        verticesList.add(lt.y);
        verticesList.add(height);

        verticesList.add(lb.x);
        verticesList.add(lb.y);
        verticesList.add(height);

        verticesList.add(rb.x);
        verticesList.add(rb.y);
        verticesList.add(height);

        verticesList.add(rt.x);
        verticesList.add(rt.y);
        verticesList.add(height);
    }


    private void updateVertexBuff() {
        //index
        indexBuffer = ByteBuffer.allocateDirect(indexverticesList.size() * 4)
                .order(ByteOrder.nativeOrder())
                .asShortBuffer();
        indexBuffer.clear();
        for (Short s : indexverticesList) {
            indexBuffer.put(s);
        }
        indexBuffer.position(0);


        vertextBuffer = ByteBuffer.allocateDirect(verticesList.size() * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertextBuffer.clear();
        for (Float f : verticesList) {
            vertextBuffer.put(f);
        }
        vertextBuffer.position(0);


        colorBuffer = ByteBuffer.allocateDirect(colorList.size() * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        colorBuffer.clear();
        for (Float f : colorList) {
            colorBuffer.put(f);
        }
        colorBuffer.position(0);

    }


    class MyShader {
        String vertexShader = "precision highp float;\n" +
                "        attribute vec3 aVertex;//顶点数组,三维坐标\n" +
                "        attribute vec4 aColor;//颜色数组,三维坐标\n" +
                "        uniform mat4 aMVPMatrix;//mvp矩阵\n" +
                "        varying vec4 color;//\n" +
                "        void main(){\n" +
                "            gl_Position = aMVPMatrix * vec4(aVertex, 1.0);\n" +
                "            color = aColor;\n" +
                "        }";

        String fragmentShader = "//有颜色 没有纹理\n" +
                "        precision highp float;\n" +
                "        varying vec4 color;//\n" +
                "        void main(){\n" +
                "               gl_FragColor = color;\n" +
                "        }";

        int aVertex, aMVPMatrix, aColor;

        int program;


        public void create() {
            int vertexLocation = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
            int fragmentLocation = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);

            GLES20.glShaderSource(vertexLocation, vertexShader);
            GLES20.glCompileShader(vertexLocation);

            GLES20.glShaderSource(fragmentLocation, fragmentShader);
            GLES20.glCompileShader(fragmentLocation);

            program = GLES20.glCreateProgram();
            GLES20.glAttachShader(program, vertexLocation);
            GLES20.glAttachShader(program, fragmentLocation);
            GLES20.glLinkProgram(program);


            aVertex = GLES20.glGetAttribLocation(program, "aVertex");
            aMVPMatrix = GLES20.glGetUniformLocation(program, "aMVPMatrix");
            aColor = GLES20.glGetAttribLocation(program,"aColor");
        }
    }

    MyShader shader;

    public void initShader() {
        shader = new MyShader();
        shader.create();
    }

    public void drawES20(float[] mvp, List<LatLng> listpts, int floornum, float firstFloorHeigh, float eachFloorHeight) {

        if (shader == null)
            return;
        GLES20.glUseProgram(shader.program);

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glEnable(GL_BLEND);
        GLES20.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        GLES20.glDisable(GL_LIGHTING);
        // GLES20.glEnable(GLES20.GL_DEPTH_TEST);

       // if(vertextBuffer == null || colorBuffer == null)
            updateVertexPostion(listpts, floornum, firstFloorHeigh, eachFloorHeight);
        //顶点指针
        GLES20.glEnableVertexAttribArray(shader.aVertex);
        GLES20.glVertexAttribPointer(shader.aVertex, 3, GLES20.GL_FLOAT, false, 0, vertextBuffer);

        //颜色指针
        GLES20.glEnableVertexAttribArray(shader.aColor);
        GLES20.glVertexAttribPointer(shader.aColor,4, GLES20.GL_FLOAT,false,0,colorBuffer);

        GLES20.glUniformMatrix4fv(shader.aMVPMatrix, 1, false, mvp, 0);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indexverticesList.size(), GLES20.GL_UNSIGNED_SHORT, indexBuffer);
        GLES20.glDisableVertexAttribArray(shader.aVertex);

        GLES20.glDisable(GLES20.GL_DEPTH_TEST);
    }

}
