package com.example.firecommandandcontrolsystem.uitls;

import android.os.Environment;
import android.util.Log;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 邹奇 on 2017/7/18.
 * 文件工具类
 */

public class FileUtils {

    public static final String TAG = "FileUtils*";
    /**
     * 字符串保存到手机内存设备中
     *
     * @param str
     */
    static public Map<String, File> fileManager = new HashMap<>();

    public static boolean isSdCardExist() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    static public void addFileInstance(String InstanceName, String filedir, String fileName) {//filedir为根目录下的相对路径
        try {
            Log.e(TAG,"InstanceName:"+InstanceName +"   filedir:"+ filedir + "   fileName:"+fileName);
            File fileDir = new File(Environment.getExternalStorageDirectory().toString() + filedir);
            if (isSdCardExist()) {
                if (!fileDir.exists()) {
                    fileDir.mkdirs();
                    Log.e(TAG,"路径创建成功");
                }
            }
            // 创建指定路径的文件
            File file = new File(fileDir, fileName);
            // 如果文件不存在
            if (!file.exists()) {
                // 创建新的空文件
                file.createNewFile();
            }
            fileManager.put(InstanceName, file);
            Log.e(TAG,"file:" + file.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveFile(File file,String buffStr) {
        if(file == null)
            return;
        // 创建String对象保存文件名路径
        try {
            BufferedWriter fileBuff = new BufferedWriter(new FileWriter(file, true));
            fileBuff.newLine();
            fileBuff.write(buffStr);
            fileBuff.flush();
            fileBuff.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //保存一行数据到指定文件
    //id:为当前保存的文件标识，调用该函数，同一个is会保存到同一文件
    //dir:为所保存文件所在路径，（如果路径不存在，函数内部自己会新增路径）
    //fileName:所保存的文件名字
    //content：所保存的文件内容，暂时只接受字符串
    public static void saveData(String id,String dir,String content){
        File file = FileUtils.fileManager.get(id);
        if(file == null) {
            SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-HH:mm:ss");
            Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
            String strTime = formatter.format(curDate);
            FileUtils.addFileInstance(id, dir, strTime+"--"+id+"--"+".txt");
        }else {
            FileUtils.saveFile(file,content);
        }
    }
    public static void saveData5(String id,String dir,String content){
        File file = FileUtils.fileManager.get(id);
        if(file == null) {
            SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-HH:mm:ss");
            Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
            String strTime = formatter.format(curDate);
            FileUtils.addFileInstance(id, dir, "--"+strTime+"--"+".txt");
        }else {
            FileUtils.saveFile(file,content);
        }
    }



    public static void saveData2(String id,String dir,String content){
        File file = FileUtils.fileManager.get(id);
        if(file == null) {
            SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-HH:mm:ss");
            Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
            String strTime = formatter.format(curDate);
            FileUtils.addFileInstance(id, dir, id+"--"+"ble.txt");
        }else {
            FileUtils.saveFile(file,content);
        }
    }

    /**
     * 删除已存储的文件
     */
    public static void deletefile(String fileName) {
        try {
            // 找到文件所在的路径并删除该文件
            File file = new File(Environment.getExternalStorageDirectory(), fileName);
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取文件里面的内容
     *
     * @return
     */
    public static String readFile(File file,String fileName) {
        if(file == null)
            return "";
        try {
            BufferedReader bfReader =  new BufferedReader(new FileReader(file));
            String buffStr = bfReader.readLine();
            if(buffStr != null)
                return buffStr;
            else
                return null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

}
