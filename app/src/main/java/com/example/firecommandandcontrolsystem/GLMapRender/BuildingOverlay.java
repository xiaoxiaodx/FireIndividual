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

public class BuildingOverlay {

    ArrayList<Float> verticesList = new ArrayList<Float>();
    ArrayList<Float> textureverticesList = new ArrayList<Float>();
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
    public BuildingOverlay(AMap amap,LatLng lefttop, LatLng leftbottom, LatLng righttop, LatLng rightbottom) {

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



    public void updateVertexPostion() {

        verticesList.clear();
        textureverticesList.clear();
        indexverticesList.clear();

        PointF gllefttop = aMap.getProjection().toOpenGLLocation(lefttop);
        PointF glleftbottom = aMap.getProjection().toOpenGLLocation(leftbottom);
        PointF glrighttop = aMap.getProjection().toOpenGLLocation(righttop);
        PointF glrightbottom = aMap.getProjection().toOpenGLLocation(rightbottom);

        //添加平面楼层纹理
        int i = 0;
        for (; i < floorLevelInfoList.size(); i++) {

            FloorLevelInfo floorLevelInfo = floorLevelInfoList.get(i);

            int glfloor = floorLevelInfo.floor;
            float glheight = floorLevelInfo.height * GLMapRender.heightRadio;

            //先添加顶点坐标，为了更好的绘制纹理,该顶点数据不包含上下2个平面的顶点
            addFloatInInverticesList(gllefttop, glleftbottom, glrighttop, glrightbottom, glheight);

            //添加纹理坐标,在着色器里面 以纹理坐标的z分量值除以10 来确认绘制哪张纹理图片
            //0.1代表绘制1楼
            float texturez = ((float) glfloor) / 10;
            addFloatTextureInverticesList(texturez);

            //添加索引，每次循环为4个点
            int indexstart = i * 4;
            addFloatInindexList((short) indexstart);
        }

//        上下2个面的顶点，方便绘制侧面
//        addFloatInInverticesList(gllefttop, glleftbottom, glrighttop, glrightbottom, 0);
//        addFloatTextureInverticesList(0);
//
//        addFloatInInverticesList(gllefttop, glleftbottom, glrighttop, glrightbottom, floorLevelInfoList.get(floorLevelInfoList.size()-1).height*500);
//        addFloatTextureInverticesList(0);



        addFloatInindexList1((short) ((i-1)* 4));

//        Log.e("teset","v:"+verticesList.toString());
//        Log.e("teset","t:"+textureverticesList.toString());
//        Log.e("teset","i:"+indexverticesList.toString());

        updateVertexBuff();
    }

    //侧面，默认是连续的2个平面,每个平面4个点
    private void addFloatInindexList1(short indexstart)
    {

        short bottomstart = 0;
        short topstart = indexstart;

        for(int i=0;i<4;i++){

            indexverticesList.add((short) (bottomstart + i));
            if(i == 3) {
                indexverticesList.add((short) (bottomstart ));
                indexverticesList.add((short) (topstart ));
            }else{
                indexverticesList.add((short) (bottomstart + i + 1));
                indexverticesList.add((short) (topstart + i + 1));
            }
            indexverticesList.add((short) (topstart + i));
            if(i==3){
                indexverticesList.add((short) (topstart ));
            }else{
                indexverticesList.add((short) (topstart + i+1));
            }
            indexverticesList.add((short) (bottomstart + i));
        }
    }

    //只管水平面的  不管侧面
    private void addFloatInindexList(short indexstart) {
        //2个3角行
        indexverticesList.add(indexstart);
        indexverticesList.add((short) (indexstart + 1));
        indexverticesList.add((short) (indexstart + 2));


        indexverticesList.add((short) (indexstart + 2));
        indexverticesList.add((short) (indexstart + 3));
        indexverticesList.add((short) (indexstart));
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

    private void addFloatTextureInverticesList(float texturez) {
        textureverticesList.add(0.0f);
        textureverticesList.add(0.0f);
        textureverticesList.add(texturez);

        textureverticesList.add(0.0f);
        textureverticesList.add(1.0f);
        textureverticesList.add(texturez);

        textureverticesList.add(1.0f);
        textureverticesList.add(1.0f);
        textureverticesList.add(texturez);

        textureverticesList.add(1.0f);
        textureverticesList.add(0.0f);
        textureverticesList.add(texturez);
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

        //texture
        textureBuffer = ByteBuffer.allocateDirect(textureverticesList.size() * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        for (Float f : textureverticesList) {
            textureBuffer.put(f);
        }
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
                "        attribute vec3 aTextureVertex;//纹理位置\n" +
                "        varying vec3 v_texPo;" +
                "        void main(){\n" +
                "            gl_Position = aMVPMatrix * vec4(aVertex, 1.0);\n" +
                "            v_texPo = aTextureVertex;\n" +
                "        }";

        String fragmentShader = "//有颜色 没有纹理\n" +
                "        precision highp float;\n" +
                "        uniform sampler2D sTexture0;" +
                "        uniform sampler2D sTexture1;" +
                "        uniform sampler2D sTexture2;" +
                "        uniform sampler2D sTexture3;" +
                "        uniform sampler2D sTexture4;" +
                "        uniform sampler2D sTexture5;" +
                "        varying vec3 v_texPo;" +
                "        uniform vec4 aColor;" +
                "        void main(){\n" +
                "           if(v_texPo.z == 0.1){ \n" +
                "           gl_FragColor = texture2D(sTexture0, vec2(v_texPo));\n" +
                "           }\n" +
                "           else if(v_texPo.z == 0.2){ \n" +
                "           gl_FragColor = texture2D(sTexture1, vec2(v_texPo));\n" +
                "           }\n" +
                "           else if(v_texPo.z == 0.3){ \n" +
                "           gl_FragColor = texture2D(sTexture2, vec2(v_texPo));\n" +
                "           }\n" +
                "           else if(v_texPo.z == 0.4){ \n" +
                "           gl_FragColor = texture2D(sTexture3, vec2(v_texPo));\n" +
                "           }\n" +
                "           else if(v_texPo.z == 0.5){ \n" +
                "           gl_FragColor = texture2D(sTexture4, vec2(v_texPo));\n" +
                "           }\n" +
                "           else if(v_texPo.z == 0.6){ \n" +
                "           gl_FragColor = texture2D(sTexture5, vec2(v_texPo));\n" +
                "           }\n" +
                "           else{\n" +
                "               gl_FragColor = vec4(0,0,1,0.3);\n" +
                "           }\n" +
                "        }";

        int aVertex, aMVPMatrix, aColor, aTextureVertex;
        int[] textureID = new int[6];
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

    public void drawES20(float[] mvp) {

        if(shader == null)
            return;
        GLES20.glUseProgram(shader.program);

       GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glEnable(GL_BLEND);
        GLES20.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        GLES20.glDisable(GL_LIGHTING);
       // GLES20.glEnable(GLES20.GL_DEPTH_TEST);



        //updateVertexPostion();
        if(vertextBuffer == null || indexBuffer == null || textureBuffer == null)
            updateVertexPostion();
        //顶点指针
        GLES20.glEnableVertexAttribArray(shader.aVertex);
        GLES20.glVertexAttribPointer(shader.aVertex, 3, GLES20.GL_FLOAT, false, 0, vertextBuffer);

        //颜色指针
        GLES20.glUniform4f(shader.aColor, 0.5f, 0.3f, 1.0f, 0.3f);

        GLES20.glEnableVertexAttribArray(shader.aTextureVertex);
        GLES20.glVertexAttribPointer(shader.aTextureVertex, 3, GLES20.GL_FLOAT, false, 0, textureBuffer);

        //纹理
        GLES20.glActiveTexture(GL_TEXTURE0);
        GLES20.glBindTexture(GL10.GL_TEXTURE_2D, shader.textureID[0]);
        GLES20.glUniform1i(GLES20.glGetUniformLocation(shader.program, "sTexture0"), 0);

        GLES20.glActiveTexture(GL_TEXTURE1);
        GLES20.glBindTexture(GL10.GL_TEXTURE_2D, shader.textureID[1]);
        GLES20.glUniform1i(GLES20.glGetUniformLocation(shader.program, "sTexture1"), 1);

        GLES20.glActiveTexture(GL_TEXTURE2);
        GLES20.glBindTexture(GL10.GL_TEXTURE_2D, shader.textureID[2]);
        GLES20.glUniform1i(GLES20.glGetUniformLocation(shader.program, "sTexture2"), 2);

        GLES20.glActiveTexture(GL_TEXTURE3);
        GLES20.glBindTexture(GL10.GL_TEXTURE_2D, shader.textureID[3]);
        GLES20.glUniform1i(GLES20.glGetUniformLocation(shader.program, "sTexture3"), 3);

        GLES20.glActiveTexture(GL_TEXTURE4);
        GLES20.glBindTexture(GL10.GL_TEXTURE_2D, shader.textureID[4]);
        GLES20.glUniform1i(GLES20.glGetUniformLocation(shader.program, "sTexture4"), 4);

        GLES20.glActiveTexture(GL_TEXTURE5);
        GLES20.glBindTexture(GL10.GL_TEXTURE_2D, shader.textureID[5]);
        GLES20.glUniform1i(GLES20.glGetUniformLocation(shader.program, "sTexture5"), 5);


        GLES20.glUniformMatrix4fv(shader.aMVPMatrix, 1, false, mvp, 0);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indexverticesList.size(), GLES20.GL_UNSIGNED_SHORT, indexBuffer);
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