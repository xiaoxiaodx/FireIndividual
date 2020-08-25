package com.example.firecommandandcontrolsystem.opengl;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.PointF;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;
import android.view.View;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.example.firecommandandcontrolsystem.R;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;

import static android.opengl.GLES10.GL_BLEND;
import static javax.microedition.khronos.opengles.GL10.GL_LIGHTING;
import static javax.microedition.khronos.opengles.GL10.GL_ONE_MINUS_SRC_ALPHA;
import static javax.microedition.khronos.opengles.GL10.GL_SRC_ALPHA;








class Cube {
    ArrayList<Float> verticesList = new ArrayList<Float>();

    short indices[] = {
            0, 4, 5,
            0, 5, 1,
            1, 5, 6,
            1, 6, 2,
            2, 6, 7,
            2, 7, 3,
            3, 7, 4,
            3, 4, 0,
            4, 7, 6,
            4, 6, 5,
            3, 0, 1,
            3, 1, 2,
    };

    //
    float[] colors = {
            1f, 0f, 0f, 0.1f, // vertex 0 red
            0f, 1f, 0f, 0.1f, // vertex 1 green
            0f, 0f, 1f, 0.1f, // vertex 2 blue
            1f, 1f, 0f, 0.1f, // vertex 3
            0f, 1f, 1f, 0.1f, // vertex 4
            1f, 0f, 1f, 0.1f, // vertex 5
            0f, 0f, 0f, 0.1f, // vertex 6
            1f, 1f, 1f, 0.1f, // vertex 7
    };

    public Cube(float width, float height, float depth) {

        // 地图坐标系比较大，将值放大以免太小看不见
        width *= 10000;
        height *= 10000;
        depth *= 10000;


        width /= 2;
        height /= 2;
        depth /= 2;

        //尽量不要让z轴有负数出现
        float vertices1[] = {
                -width, -height, -0,
                width, -height, -0,
                width, height, -0,
                -width, height, -0,
                -width, -height, depth,
                width, -height, depth,
                width, height, depth,
                -width, height, depth,
        };

        for (int i = 0; i < vertices1.length; i++) {
            verticesList.add(vertices1[i]);
        }

        //index
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(indices.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        indexBuffer = byteBuffer.asShortBuffer();
        indexBuffer.put(indices);
        indexBuffer.position(0);


        //color
        ByteBuffer byteBuffer1 = ByteBuffer.allocateDirect(colors.length * 4);
        byteBuffer1.order(ByteOrder.nativeOrder());
        colorBuffer = byteBuffer1.asFloatBuffer();
        colorBuffer.put(colors);
        colorBuffer.position(0);

        init();

    }


    private FloatBuffer vertextBuffer;
    private ShortBuffer indexBuffer;
    private FloatBuffer colorBuffer;

    private void init() {
        if (vertextBuffer == null) {
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(verticesList.size() * 4);
            byteBuffer.order(ByteOrder.nativeOrder());
            vertextBuffer = byteBuffer.asFloatBuffer();
        }
        vertextBuffer.clear();
        for (Float f : verticesList) {
            vertextBuffer.put(f);
        }
        vertextBuffer.position(0);
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
                "            gl_FragColor = color;\n" +
                "        }";

        int aVertex,aMVPMatrix,aColor;
        int program;

        public void create() {
            int vertexLocation = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
            int fragmentLocation = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);

            GLES20.glShaderSource(vertexLocation,vertexShader);
            GLES20.glCompileShader(vertexLocation);

            GLES20.glShaderSource(fragmentLocation,fragmentShader);
            GLES20.glCompileShader(fragmentLocation);

            program = GLES20.glCreateProgram();
            GLES20.glAttachShader(program,vertexLocation);
            GLES20.glAttachShader(program,fragmentLocation);
            GLES20.glLinkProgram(program);


            aVertex  = GLES20.glGetAttribLocation(program, "aVertex");
            aMVPMatrix = GLES20.glGetUniformLocation(program,"aMVPMatrix");
            aColor = GLES20.glGetAttribLocation(program,"aColor");

        }
    }

    MyShader shader;

    public void initShader() {
        shader = new MyShader();
        shader.create();
    }

    public void drawES20(float[] mvp) {

        GLES20.glUseProgram(shader.program);

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glEnable(GL_BLEND);
        GLES20.glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);
        GLES20.glDisable(GL_LIGHTING);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        GLES20.glEnableVertexAttribArray(shader.aVertex);

        //顶点指针
        GLES20.glVertexAttribPointer(shader.aVertex, 3, GLES20.GL_FLOAT, false, 0, vertextBuffer);

        //颜色指针
        GLES20.glEnableVertexAttribArray(shader.aColor);
        GLES20.glVertexAttribPointer(shader.aColor,4, GLES20.GL_FLOAT,false,0,colorBuffer);

        GLES20.glUniformMatrix4fv(shader.aMVPMatrix,1,false,mvp,0);

        //开始画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices.length, GLES20.GL_UNSIGNED_SHORT, indexBuffer);
        GLES20.glDisableVertexAttribArray(shader.aVertex);

        GLES20.glDisable(GLES20.GL_DEPTH_TEST);


    }




}
//
//class Cube {
//    public ArrayList<Float> verticesList = new ArrayList<Float>();
////
////        short indices[];
////     float[] colors;
//
//    short textureindices[] = {
//            3, 0, 1,
//            3, 1, 2,
//    };
//    short indices[] = {
//            0, 4, 5,
//            0, 5, 1,
//
//            1, 5, 6,
//            1, 6, 2,
//
//            2, 6, 7,
//            2, 7, 3,
//
//            3, 7, 4,
//            3, 4, 0,
//
//            4, 7, 6,
//            4, 6, 5,
//
//            3, 0, 1,
//            3, 1, 2,
//    };
//
//    float[] colors = {
//            1f, 0f, 0f, 0.1f, // vertex 0 red
//            0f, 1f, 0f, 0.1f, // vertex 1 green
//            0f, 0f, 1f, 0.1f, // vertex 2 blue
//            1f, 1f, 0f, 0.1f, // vertex 3
//            0f, 1f, 1f, 0.1f, // vertex 4
//            1f, 0f, 1f, 0.1f, // vertex 5
//            0f, 0f, 0f, 0.1f, // vertex 6
//            1f, 1f, 1f, 0.1f, // vertex 7
//    };
//    private int textureId;
//    public Cube(AMap aMap,Bitmap bitmap) {
//        init();
//
//
//        if(bitmap == null)return;
//
//        int[] textureIds = new int[1];
//        //生成纹理
//        GLES20.glGenTextures(1, textureIds, 0);
//        if (textureIds[0] == 0) {
//            return;
//        }
//        textureId = textureIds[0];
//        //绑定纹理
//        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
//        //环绕方式
//        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
//        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);
//        //过滤方式
//        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
//        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
//
//        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
//        bitmap.recycle();
//
//    }
//
//
//    public void initVetex(List<PointF> listPt) {
//
//        Log.e("CubeMap:", "latlng:" + listPt);
//
//        int ptSize = listPt.size();
//        for (int i = 0; i < ptSize; i++) {
//            PointF ptf = listPt.get(i);
//
//            Log.e("CubeMap:", "opengl ptf:" + ptf);
//            verticesList.add(ptf.x);
//            verticesList.add(ptf.y);
//            verticesList.add(0F);
//        }
////
//        for (int j = 0; j < ptSize; j++) {
//            PointF ptf = listPt.get(j);
//            Log.e("CubeMap:", "openg2 ptf:" + ptf);
//            verticesList.add(ptf.x);
//            verticesList.add(ptf.y);
//            verticesList.add(1000f);
//        }
//        Log.e("Cube:", "vertext:" + verticesList);
////
////        indices = new short[((ptSize - 1) * 4) ];
////
////
////        int indicesIndex = 0;
////        //上下表面
////        short bottomIndex = 0;
////        short topIndex = (short) ptSize;
////        for (int i = 1; i < ptSize - 1; i++) {
////
////
////            indices[indicesIndex++] = bottomIndex;
////            indices[indicesIndex++] = (short) (bottomIndex + i);
////            indices[indicesIndex++] = (short) (bottomIndex + i + 1);
////
////            indices[indicesIndex++] = topIndex;
////            indices[indicesIndex++] = (short) (topIndex + i);
////            indices[indicesIndex++] = (short) (topIndex + i + 1);
////            Log.e("test",""+bottomIndex  + ","+topIndex);
////        }
//        //侧面
////        for (int i = 0; i < ptSize - 1; i++) {
////            indices[indicesIndex++] = (short) (bottomIndex + i);
////            indices[indicesIndex++] = (short) (bottomIndex + i + 1);
////            indices[indicesIndex++] = (short) (topIndex + i);
////
////            indices[indicesIndex++] = (short) (topIndex + i);
////            indices[indicesIndex++] = (short) (bottomIndex + i + 1);
////            indices[indicesIndex++] = (short) (topIndex + i + 1);
////        }
////
////        indices[indicesIndex++] = (short) (bottomIndex);
////        indices[indicesIndex++] = (short) (bottomIndex + ptSize - 1);
////        indices[indicesIndex++] = (short) (topIndex);
////
////        indices[indicesIndex++] = (short) (topIndex + ptSize - 1);
////        indices[indicesIndex++] = (short) (bottomIndex + ptSize - 1);
////        indices[indicesIndex++] = (short) (topIndex);
//
//
//        colors = new float[ptSize * 2 * 4];
//        int colorsIndex = 0;
//        for (int i = 0; i < ptSize; i++) {
//            colors[colorsIndex++] = 1f;
//            colors[colorsIndex++] = 1f;
//            colors[colorsIndex++] = 0f;
//            colors[colorsIndex++] = 0.3f;
//        }
//
//        for (int j = ptSize; j < ptSize*2; j++) {
//            colors[colorsIndex++] = 1f;
//            colors[colorsIndex++] = 1f;
//            colors[colorsIndex++] = 0f;
//            colors[colorsIndex++] = 1f;
//        }
//        init();
//    }
//
//
//    private FloatBuffer vertextBuffer ;
//    private ShortBuffer indexBuffer;
//    private ShortBuffer textureindexBuffer;
//    private FloatBuffer colorBuffer;
//
//    private void init() {
//        //index
//        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(indices.length * 2);
//        byteBuffer.order(ByteOrder.nativeOrder());
//        indexBuffer = byteBuffer.asShortBuffer();
//        indexBuffer.put(indices);
//        indexBuffer.position(0);
//
//
//        ByteBuffer texturebyteBuffer = ByteBuffer.allocateDirect(textureindices.length * 4);
//        byteBuffer.order(ByteOrder.nativeOrder());
//        textureindexBuffer = byteBuffer.asShortBuffer();
//        textureindexBuffer.put(textureindices);
//        textureindexBuffer.position(0);
//
//        //color
//        ByteBuffer byteBuffer1 = ByteBuffer.allocateDirect(colors.length * 4);
//        byteBuffer1.order(ByteOrder.nativeOrder());
//        colorBuffer = byteBuffer1.asFloatBuffer();
//        colorBuffer.put(colors);
//        colorBuffer.position(0);
//
//        //  if (vertextBuffer == null) {
//        ByteBuffer byteBuffer2 = ByteBuffer.allocateDirect(verticesList.size() * 4);
//        byteBuffer2.order(ByteOrder.nativeOrder());
//        vertextBuffer = byteBuffer2.asFloatBuffer();
//        // }
//        vertextBuffer.clear();
//        for (Float f : verticesList) {
//            vertextBuffer.put(f);
//        }
//        vertextBuffer.position(0);
//    }
//
//
//    class MyShader {
//        String vertexShader = "precision highp float;\n" +
//                "        attribute vec3 aVertex;//顶点数组,三维坐标\n" +
//                "        attribute vec4 aColor;//颜色数组,三维坐标\n" +
//                "        uniform mat4 aMVPMatrix;//mvp矩阵\n" +
//                "        varying vec4 color;//\n" +
//                "        void main(){\n" +
//                "            gl_Position = aMVPMatrix * vec4(aVertex, 1.0);\n" +
//                "            color = aColor;\n" +
//                "        }";
//
//        String fragmentShader = "//有颜色 没有纹理\n" +
//                "        precision highp float;\n" +
//                "        varying vec4 color;//\n" +
//                "        uniform sampler2D u_TextureUnit;\n"+
//                "        void main(){\n" +
//                "            gl_FragColor = color;\n" +
//                "        }";
//
//        int aVertex, aMVPMatrix, aColor;
//        int program;
//
//        public void create() {
//
//            int vertexLocation = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
//            int fragmentLocation = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
//
//            GLES20.glShaderSource(vertexLocation, vertexShader);
//            GLES20.glCompileShader(vertexLocation);
//
//            GLES20.glShaderSource(fragmentLocation, fragmentShader);
//            GLES20.glCompileShader(fragmentLocation);
//
//            program = GLES20.glCreateProgram();
//            GLES20.glAttachShader(program, vertexLocation);
//            GLES20.glAttachShader(program, fragmentLocation);
//            GLES20.glLinkProgram(program);
//
//
//            aVertex = GLES20.glGetAttribLocation(program, "aVertex");
//            aMVPMatrix = GLES20.glGetUniformLocation(program, "aMVPMatrix");
//            aColor = GLES20.glGetAttribLocation(program, "aColor");
//        }
//    }
//
//    MyShader shader;
//
//    public void initShader() {
//        shader = new MyShader();
//        shader.create();
//    }
//
//    public void drawES20(float[] mvp) {
//
//
//        GLES20.glUseProgram(shader.program);
//        GLES20.glDisable(GLES20.GL_DEPTH_TEST);
//
//        GLES20.glEnable(GL_BLEND);
//        GLES20.glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);
//     //   GLES20.glDepthMask(GL_FALSE);
//        GLES20.glDisable(GL_LIGHTING);
//        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
//
//        GLES20.glEnableVertexAttribArray(shader.aVertex);
//
//        //顶点指针
//        GLES20.glVertexAttribPointer(shader.aVertex, 3, GLES20.GL_FLOAT, false, 0, vertextBuffer);
//
//        //颜色指针
//        GLES20.glEnableVertexAttribArray(shader.aColor);
//        GLES20.glVertexAttribPointer(shader.aColor, 4, GLES20.GL_FLOAT, false, 0, colorBuffer);
//
//        GLES20.glUniformMatrix4fv(shader.aMVPMatrix, 1, false, mvp, 0);
//
//        //开始画
//        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices.length, GLES20.GL_UNSIGNED_SHORT, indexBuffer);
//        GLES20.glDisableVertexAttribArray(shader.aVertex);
//
//        GLES20.glDisable(GLES20.GL_DEPTH_TEST);
//        GLES20.glDisable(GL_BLEND);
//        GLES20.glEnable(GL_LIGHTING);
//       // GLES20.glDepthMask(GL_TRUE);
//
//    }
//
//}
