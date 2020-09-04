package com.example.firecommandandcontrolsystem.adhocNetwork;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.firecommandandcontrolsystem.MainActivity;
import com.example.firecommandandcontrolsystem.adhocNetwork.ProtcocolClass.FloorConfig;
import com.example.firecommandandcontrolsystem.adhocNetwork.ProtcocolClass.GpsInfo;
import com.example.firecommandandcontrolsystem.adhocNetwork.ProtcocolClass.Rescue;
import com.example.firecommandandcontrolsystem.adhocNetwork.ProtcocolClass.Retreat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class AdhocClientThread {


    public static int HostPort = 9000;
    public static String HostIp = "192.168.2.127";
    public static Socket socket;
    private OutputStream outputStream = null;//定义输出流
    private InputStream inputStream = null;//定义输入流


    public boolean isNeedRec = false;


    public AdhocProtocol protocol = new AdhocProtocol();

    private static final char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};


    static public Handler receiHandler;
    private InetSocketAddress SerAddr;

    private String TAG = "AdhocClientThread";


    boolean flagGpsInfoResponse = false;
    int heartBeatPackageCount = 0;    //心跳包计数
    boolean flagHeartBeatResponse = false;//心跳包应答标志

    int configHeightPackageCount = 0;
    int rescuePackageCount = 0;
    int retreatPackageCount = 0;

    final int sendGpsInfo = 232;//由于发生位置信息包和接收其他人的位置信息包的固定有效数据ID是一样的，所以在此重新定义一个ID，用于信息传递的识别，没有其他含义
    String strbuff = "";

    static public int identificationID = 0;


    public Timer timer;
    public TimerTask timerTask;


    public AdhocClientThread(String ip, int port, int id) {


        identificationID = id;
        insTimer();

        restartSocket(ip, port);
    }


    private void insTimer()//初始化定时器任务。定时发送心跳包和定位包
    {
        timer = new Timer();
        timerTask = new TimerTask() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
            @Override
            public void run() {

                if (!flagGpsInfoResponse) {//在没有定位应答时，每2秒一次心跳包
                    Message msgHeart = new Message();
                    msgHeart.what = AdhocProtocol.heartBeat;
//                    if (receiHandler != null)
//                        receiHandler.sendMessage(msgHeart);
                }

//                if (!flagHeartBeatResponse) {
//                    restartSocket();
//                }

            }
        };
    }


    public void restartSocket(String ip, int port) {
        HostIp = ip;
        HostPort = port;

        timer.cancel();
        timerTask.cancel();

        isNeedRec = false;

        if (receiHandler != null)
            receiHandler.getLooper().quitSafely();
        if (socket != null) {

            try {
                if (outputStream != null)
                    outputStream.close();
                if (inputStream != null)
                    inputStream.close();

                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        // MainActivity.sendMsgToHandle(MainActivity.HANDLERTYPR_NET_STATE, 0, MainActivity.DISCONNECT, port);
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        //MainActivity.sendMsgToHandle(MainActivity.HANDLERTYPR_NET_STATE, 0, MainActivity.RECONNECTING, port);


        socket = null;
        timer = null;
        timerTask = null;
        receiHandler = null;
        insTimer();
        this.run();

    }

    public void run() {

        Receive_Thread receive_thread = new Receive_Thread();
        receive_thread.start();


        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Send_Thread send_thread = new Send_Thread();
        send_thread.start();

        timer.schedule(timerTask, 100, 2000);
    }

    class Receive_Thread extends Thread {
        public void run()//重写run方法
        {
            try {
                SerAddr = new InetSocketAddress(HostIp, HostPort);
                socket = new Socket();
                socket.connect(SerAddr, 3000);

                MainActivity.sendMsgToHandle(MainActivity.Adhoc_Connect, true, 0, 0);
                isNeedRec = true;
                inputStream = socket.getInputStream();
                byte[] buffer = new byte[1024];
                while (isNeedRec) {
                    int length = inputStream.read(buffer);
                    if (length > 0) {
                        byte[] by = new byte[length];
                        System.arraycopy(buffer, 0, by, 0, length);
                        protocol.phaseReciveData(by);
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();

                MainActivity.sendMsgToHandle(MainActivity.Adhoc_Connect, false, 0, 0);
            }

        }
    }

    class Send_Thread extends Thread {
        String strbuff = "";

        public void run()//重写run方法
        {
            Looper.prepare();


            //outputStream = socket.getOutputStream();

            receiHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {

                   // Log.e(TAG, "receiHandler：" + msg.what + "," + msg.obj);

                    byte[] sendbyte;
                    switch (msg.what) {
                        //主动发送
                        case AdhocProtocol.heartBeat:
                            if (flagHeartBeatResponse) {
                                flagHeartBeatResponse = false;
                                heartBeatPackageCount++;
                            } else {//没有心跳应答-----报警或者其他操作
                                //  MainActivity.sendMsgToHandle(MainActivity.HANDLERTYPR_NET_STATE, 0, MainActivity.TRANSPORT_NOHEARTREPLY, 0);
                                strbuff += "";
                            }
                            byte[] heartByteArr = protocol.heartBeatPackage((byte) identificationID, heartBeatPackageCount);
                            sendByteDate(heartByteArr);

                            strbuff += "发送心跳包----->" + bytesToHexString(heartByteArr);
                            break;
                        case AdhocProtocol.heartBeatResponse:
                            Log.e(TAG, "心跳应答：");
                            strbuff += "解析到心跳应答";
                            flagHeartBeatResponse = true;
                            break;
                        case AdhocProtocol.configHeight:

                            FloorConfig floorConfig =(FloorConfig)msg.obj;
                            floorConfig.count ++;
                            byte[] floorArr = protocol.configHeightPackage(floorConfig.count,floorConfig.curfloor,floorConfig.firstfloorheight,floorConfig.eachfloorheight,floorConfig.fu_firstfloorheight,floorConfig.fu_eachfloorheight);
                            Log.e(TAG, "楼层配置："+bytesToHexString(floorArr));
                            sendByteDate(floorArr);
                            break;
                        case AdhocProtocol.retreat:

                            Retreat retreat = (Retreat) msg.obj;
                            retreat.count++;
                            byte[] retreatArr = protocol.retreatPackage(retreat.count, (byte) retreat.cmdLevel, retreat.isALLRetreat, retreat.listRetreat);
                            sendByteDate(retreatArr);

                            break;
                        case AdhocProtocol.rescue:

                            Rescue rescue = (Rescue) msg.obj;
                            rescue.count++;
                            byte[] rescueArr = protocol.rescuePackage(rescue.count, (byte) rescue.id, (byte) rescue.localstate,
                                    rescue.lat, (byte) rescue.latstate, rescue.lng, (byte) rescue.lngstate, rescue.height,
                                    (byte) rescue.floor, rescue.isAllRescue, rescue.listRescue);

                            sendByteDate(rescueArr);

                            break;

                        //被动接收
                        case AdhocProtocol.RecGpsInfo:

                            int id = ((GpsInfo) msg.obj).id;
                            int count = ((GpsInfo) msg.obj).count;
                            byte[] gpsByteArr = protocol.gpsInfoReplyPackage((byte) id, count);
                            sendByteDate(gpsByteArr);

                            MainActivity.sendMsgToHandle(MainActivity.CMD_RecGpsInfo, msg.obj, 0, 0);
                            break;
                        case AdhocProtocol.Sos:

                            MainActivity.sendMsgToHandle(MainActivity.CMD_RecSos, msg.obj, 0, 0);

                            break;
                        case AdhocProtocol.recConfigHeightReponse:
                            Log.e(TAG,"楼层配置回应");
                            MainActivity.sendMsgToHandle(MainActivity.CMD_RecRetreat, msg.obj, 0, 0);
                            break;
                        case AdhocProtocol.recRescueReponse:
                            Log.e(TAG,"救援回应");
                            MainActivity.sendMsgToHandle(MainActivity.CMD_RecRescue, msg.obj, 0, 0);
                            break;
                        case AdhocProtocol.recRetreatRepose:
                            Log.e(TAG,"撤退回应");
                            MainActivity.sendMsgToHandle(MainActivity.CMD_RecFloorConifg, msg.obj, 0, 0);
                            break;
                        default:
                            break;
                    }
                    if (strbuff != "") {
                        msg.obj = strbuff;
                        // MainActivity.sendMsgToHandle(HANDLERTYPR_LOG, msg.obj, 0, 0);
                        strbuff = "";
                    }
                }
            };

            Looper.loop();// 启动 xxdc
        }
    }


    private void sendByteDate(byte[] bytes) {
        //Log.e(TAG, "test:" + bytesToHexString(bytes));
        if (socket != null) {
            try {
                outputStream = socket.getOutputStream();
                outputStream.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String bytesToHexString(byte[] bytes) {
        char[] buf = new char[bytes.length * 2];
        int index = 0;
        for (byte b : bytes) { // 利用位运算进行转换，可以看作方法一的变种 songheting?
            buf[index++] = HEX_CHAR[b >>> 4 & 0xf];
            buf[index++] = HEX_CHAR[b & 0xf];
        }

        return new String(buf);
    }


}
