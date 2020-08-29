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
import static javax.microedition.khronos.opengles.GL10.GL_TEXTURE1;
import static javax.microedition.khronos.opengles.GL10.GL_TEXTURE2;
import static javax.microedition.khronos.opengles.GL10.GL_TEXTURE3;
import static javax.microedition.khronos.opengles.GL10.GL_TEXTURE4;
import static javax.microedition.khronos.opengles.GL10.GL_TEXTURE5;

public class FiremenMarker {
    ArrayList<Float> verticesList = new ArrayList<Float>();


    float vertex[] = {
            -10000, -10000, 0,
            10000, -10000, 0,
            10000, 10000, 0,
            -10000, 10000, 0,

            -10000, -10000, 10000,
            10000, -10000, 10000,
            10000, 10000, 10000,
            -10000, 10000, 10000,


            -10000, -10000, 20000,
            10000, -10000, 20000,
            10000, 10000, 20000,
            -10000, 10000, 20000,

            -10000, -10000, 40000,
            10000, -10000, 40000,
            10000, 10000, 40000,
            -10000, 10000, 40000,

            -10000, -10000, 50000,
            10000, -10000, 50000,
            10000, 10000, 50000,
            -10000, 10000, 50000,

            -10000, -10000, 60000,
            10000, -10000, 60000,
            10000, 10000, 60000,
            -10000, 10000, 60000,
    };

    float[] textureData = {
            0.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f,
            1.0f, 1.0f,
    };


    short indices[] = {
            0, 1, 2,
            2, 3, 0,
    };





    AMap aMap;
    public FiremenMarker(AMap amap, LatLng lefttop, LatLng leftbottom, LatLng righttop, LatLng rightbottom) {

        this.aMap = amap;
        this.lefttop = lefttop;
        this.leftbottom = leftbottom;
        this.righttop = righttop;
        this.rightbottom = rightbottom;


//        updateVertexBuff();
    }


    private FloatBuffer vertextBuffer;
    private ShortBuffer indexBuffer;
    private FloatBuffer colorBuffer;
    private FloatBuffer textureBuffer;
    private IntBuffer showImgBuffer;



    public void updateVertexPostion(LatLng latlng,float height) {

        verticesList.clear();

//        PointF gllefttop = aMap.getProjection().toOpenGLLocation(lefttop);
//        PointF glleftbottom = aMap.getProjection().toOpenGLLocation(leftbottom);
//        PointF glrighttop = aMap.getProjection().toOpenGLLocation(righttop);
//        PointF glrightbottom = aMap.getProjection().toOpenGLLocation(rightbottom);
//
//        addFloatInInverticesList(gllefttop, glleftbottom, glrighttop, glrightbottom, 500);

        PointF tmpptf = aMap.getProjection().toOpenGLLocation(latlng);

        PointF gllefttop = new PointF(tmpptf.x - 600,tmpptf.y);
        PointF glleftbottom = new PointF(tmpptf.x + 600,tmpptf.y);

        PointF glrighttop = new PointF(tmpptf.x - 600,tmpptf.y);
        PointF glrightbottom = new PointF(tmpptf.x - 600,tmpptf.y);

        addFloatInInverticesList(gllefttop, glleftbottom, glrighttop, glrightbottom, height);

        updateVertexBuff();
    }

    private void addFloatInInverticesList(PointF lt, PointF lb, PointF rt, PointF rb, float height) {
        verticesList.add(lt.x);
        verticesList.add(lt.y);
        verticesList.add(height);

        verticesList.add(lb.x);
        verticesList.add(lb.y);
        verticesList.add(height);

        verticesList.add(lb.x);
        verticesList.add(lb.y);
        verticesList.add(0f);

        verticesList.add(lt.x);
        verticesList.add(lt.y);
        verticesList.add(0f);
    }



    private void updateVertexBuff() {
        //index
        indexBuffer = ByteBuffer.allocateDirect(indices.length * 4)
                .order(ByteOrder.nativeOrder())
                .asShortBuffer();
        indexBuffer.clear();
        indexBuffer.put(indices);
        indexBuffer.position(0);

        //texture
        textureBuffer = ByteBuffer.allocateDirect(textureData.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        textureBuffer.put(textureData);
        textureBuffer.position(0);

        vertextBuffer = ByteBuffer.allocateDirect(verticesList.size() * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertextBuffer.clear();
        for (Float f : verticesList) {
            vertextBuffer.put(f);
        }
        vertextBuffer.position(0);

    }


    class MyShader {
        String vertexShader = "precision highp float;\n" +
                "        attribute vec3 aVertex;//顶点数组,三维坐标\n" +
                "        uniform mat4 aMVPMatrix;//mvp矩阵\n" +
                "        attribute vec2 aTextureVertex;//纹理位置\n" +
                "        varying vec2 v_texPo;" +
                "        void main(){\n" +
                "            gl_Position = aMVPMatrix * vec4(aVertex, 1.0);\n" +
                "            v_texPo = aTextureVertex;\n" +
                "        }";

        String fragmentShader = "//有颜色 没有纹理\n" +
                "        precision highp float;\n" +
                "        uniform sampler2D sTexture0;" +
                "        varying vec2 v_texPo;" +
                "        void main(){\n" +
                "           gl_FragColor = texture2D(sTexture0, v_texPo);\n" +
                "        }";

        int aVertex, aMVPMatrix, aColor, aTextureVertex;
        int[] textureID = new int[1];
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
            aTextureVertex = GLES20.glGetAttribLocation(program, "aTextureVertex");


            //创建纹理
            glGenTextures(textureID.length, textureID, 0);

            for (int i = 0; i < textureID.length; i++) {

                GLES20.glBindTexture(GL10.GL_TEXTURE_2D, textureID[i]);
                // 设置纹理被缩小（距离视点很远时被缩小）时的滤波方式
                GLES20.glTexParameterf(GL10.GL_TEXTURE_2D,
                        GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
                // 设置纹理被放大（距离视点很近时被方法）时的滤波方式
                GLES20.glTexParameterf(GL10.GL_TEXTURE_2D,
                        GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
                // 设置在横向、纵向上都是平铺纹理
                GLES20.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
                        GL10.GL_REPEAT);
                GLES20.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
                        GL10.GL_REPEAT);
                GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, floorLevelInfoList.get(i).bitmap, 0);

            }
        }
    }

    MyShader shader;

    public void initShader() {
        shader = new MyShader();
        shader.create();
    }

    public void drawES20(float[] mvp,LatLng curlatlng,float height) {

        GLES20.glUseProgram(shader.program);

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glEnable(GL_BLEND);
        GLES20.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        GLES20.glDisable(GL_LIGHTING);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        //updateVertexPostion();
        if(vertextBuffer == null || indexBuffer == null || textureBuffer == null)
            updateVertexPostion(curlatlng,height);
        //顶点指针
        GLES20.glEnableVertexAttribArray(shader.aVertex);
        GLES20.glVertexAttribPointer(shader.aVertex, 3, GLES20.GL_FLOAT, false, 0, vertextBuffer);

        //颜色指针
        GLES20.glUniform4f(shader.aColor, 0.5f, 0.3f, 1.0f, 0.3f);

        GLES20.glEnableVertexAttribArray(shader.aTextureVertex);
        GLES20.glVertexAttribPointer(shader.aTextureVertex, 2, GLES20.GL_FLOAT, false, 0, textureBuffer);

        //纹理
        GLES20.glActiveTexture(GL_TEXTURE0);
        GLES20.glBindTexture(GL10.GL_TEXTURE_2D, shader.textureID[0]);
        GLES20.glUniform1i(GLES20.glGetUniformLocation(shader.program, "sTexture0"), 0);



        GLES20.glUniformMatrix4fv(shader.aMVPMatrix, 1, false, mvp, 0);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices.length, GLES20.GL_UNSIGNED_SHORT, indexBuffer);
        GLES20.glDisableVertexAttribArray(shader.aVertex);

        GLES20.glDisable(GLES20.GL_DEPTH_TEST);
    }

    List<FloorLevelInfo> floorLevelInfoList = new ArrayList<>();


    private LatLng lefttop;
    private LatLng leftbottom;
    private LatLng righttop;
    private LatLng rightbottom;


    class FloorLevelInfo {
        public FloorLevelInfo(int floor, int height, Bitmap bitmap, float[] defaultColor) {
            this.floor = floor;
            this.height = height;
            this.bitmap = bitmap;
            this.defaultColor = defaultColor;
        }

        public int floor;
        public int height;
        public Bitmap bitmap;       //楼层的位图
        public float[] defaultColor = new float[]{0.5f, 0.5f, 0.5f, 1.0f};//没有位图楼层显示的颜色
    }


    public boolean addFloorInfoInBuilding(int floor, int height, Bitmap bitmap, float[] color) {

        floorLevelInfoList.add(new FloorLevelInfo(floor, height, bitmap, color));

        return true;
    }

}
