package com.example.firecommandandcontrolsystem.GLMapRender;

import android.graphics.PointF;
import android.opengl.GLES20;
import android.util.Log;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import static javax.microedition.khronos.opengles.GL10.GL_NICEST;
import static javax.microedition.khronos.opengles.GL10.GL_POINT_SMOOTH;

public class Line3D {
    public ArrayList<Float> verticesList = new ArrayList<Float>();

    public Line3D() {
       initShader();
    }


    private FloatBuffer vertextBuffer;

    private void updateVertextBuffer(List<Float> listf) {
        ByteBuffer byteBuffer2 = ByteBuffer.allocateDirect(listf.size() * 4);
        byteBuffer2.order(ByteOrder.nativeOrder());
        vertextBuffer = byteBuffer2.asFloatBuffer();
        // }
        vertextBuffer.clear();
        for (Float f : listf) {
            vertextBuffer.put(f);
        }
        vertextBuffer.position(0);
    }

    class MyShader {
        String vertexShader = "precision highp float;\n" +
                "        attribute vec3 aVertex;//顶点数组,三维坐标\n" +
                "        uniform vec4 aColor;//颜色数组,三维坐标\n" +
                "        uniform mat4 aMVPMatrix;//mvp矩阵\n" +
                "        varying vec4 color;//\n" +
                "        void main(){\n" +
                "            gl_Position = aMVPMatrix * vec4(aVertex, 1.0);\n" +
                "            color = aColor;\n" +
                "            gl_PointSize = 3.0;"+
                "        }";

        String fragmentShader = "//有颜色 没有纹理\n" +
                "        precision highp float;\n" +
                "        varying vec4 color;//\n" +
                "        void main(){\n" +
                "            gl_FragColor = color;\n" +
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
            aColor = GLES20.glGetUniformLocation(program, "aColor");

        }
    }

    MyShader shader;

    public void initShader() {
        shader = new MyShader();
        shader.create();
    }

    public  synchronized void drawES20(float[] mvp, List<Float> listpts, float[] color) {

        if(shader == null)
            return;
        GLES20.glUseProgram(shader.program);

        GLES20.glEnable(GL_POINT_SMOOTH);
        GLES20.glHint(GL_POINT_SMOOTH, GL_NICEST);

        updateVertextBuffer(listpts);

        GLES20.glEnableVertexAttribArray(shader.aVertex);
        //顶点指针
        GLES20.glVertexAttribPointer(shader.aVertex, 3, GLES20.GL_FLOAT, false, 0, vertextBuffer);
        GLES20.glUniformMatrix4fv(shader.aMVPMatrix, 1, false, mvp, 0);
        GLES20.glUniform4f(shader.aColor, color[0], color[1], color[2],1f);

        //开始画
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, listpts.size()/3);
        GLES20.glDisableVertexAttribArray(shader.aVertex);
        //GLES20.glDisable(GLES20.GL_DEPTH_TEST);

        GLES20.glDisable(GL_POINT_SMOOTH);
    }
}

