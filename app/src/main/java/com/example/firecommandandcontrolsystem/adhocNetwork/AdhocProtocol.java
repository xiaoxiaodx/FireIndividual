package com.example.firecommandandcontrolsystem.adhocNetwork;

import android.os.Message;
import android.util.Log;


import com.example.firecommandandcontrolsystem.adhocNetwork.ProtcocolClass.GpsInfo;
import com.example.firecommandandcontrolsystem.adhocNetwork.ProtcocolClass.Sos;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.autonavi.base.amap.mapcore.Convert.bytesToHexString;
import static com.example.firecommandandcontrolsystem.uitls.HelperDataConversion.Double2byte;
import static com.example.firecommandandcontrolsystem.uitls.HelperDataConversion.bytes2Int;
import static com.example.firecommandandcontrolsystem.uitls.HelperDataConversion.float2byte;
import static com.example.firecommandandcontrolsystem.uitls.HelperDataConversion.int2Byte;


public class AdhocProtocol {
    //协议版本号
    final byte versionNum = (byte) 15;
    //固定头
    final byte[] fixHead = {(byte) 0x24, (byte) 0xDB};

    final String TAG = "AdhocProtocol*";
    //各种包的有效数据长度(包含包类型)
    final int heartBeatLength = 5;


    final int gpsInfoLength = 32;
    final int sosLength = 2;
    final int gpsInfoReplyLength = 5;
    final int sosReplyLength = 6;


    //发送包类型
    static public final byte heartBeat = (byte) 0x30;
    static public final byte heartBeatResponse = (byte) 0x31;

    static public final byte gpsInfo = (byte) 0x40;
    static public final byte RecGpsInfo = (byte) 0x40;//接收其他队友的Gps信息
    static public final byte gpsInfoReply = (byte) 0x41;


    static public final byte configHeight = (byte) 0x64;
    static public final byte retreat = (byte) 0x60;
    static public final byte rescue = (byte) 0x62;
    //接收包类型
    static public final byte recRetreatRepose = (byte) 0x61;
    static public final byte recRescueReponse = (byte) 0x63;
    static public final byte recConfigHeightReponse = (byte) 0x65;

    static public final byte Sos = (byte) 0x50;
    public static final byte SosRepley = (byte) 0x51;


    public static final byte gpsInfoByMainActivity = (byte) 0x01;
    //final byte SosReply = (byte)0x51;
    //最小数据长度，最大数据长度
    final int minFrameLength = 5;
    final int maxFrameLength = 38;

    final int minValidDataLength = minFrameLength - 4;
    final int maxValidDataLength = maxFrameLength - 4;
    //	byte[] PreBuff = new byte[]{};

    private int NetBatteryBuff = 0;
    static boolean flagReplyHeardBeat = true;
    static boolean flagReplyGpsInfo = true;
    static boolean flagReplySos = true;
    public ArrayList<Byte> PreBuff = new ArrayList<Byte>();

    //数据校验
    public String phaseReciveData(byte[] data) {

        int dataBuffLength = PreBuff.size() + data.length;

        byte[] dataBuff = new byte[dataBuffLength];
        for (int i = 0; i < PreBuff.size(); i++) {
            dataBuff[i] = PreBuff.get(i);
        }
        //将上一次遗留的字节与这次来的数据拼接起来
        System.arraycopy(data, 0, dataBuff, PreBuff.size(), data.length);
        PreBuff.clear();
        boolean flagFindNewHead = false;
        int preIndex = 0;
        //轮询查找协议固定头，每次找到新的头后，PreBuff开始缓存数据直到解包出一帧有效数据，每解到一帧数据，PreBuff清除缓存，并等到找到新的协议头
        //当还剩下数据的数据不足一帧时，缓存到PreBuff中
        for (int index = 0; index < dataBuff.length; ) {
            if (dataBuff.length - index > minFrameLength) {//小于最小的一帧长度，将数据留到下一桢保存到PreBuff
                if (dataBuff[index] == fixHead[0] && dataBuff[index + 1] == fixHead[1]) {//固定头查询(多帧情况解析)
                    flagFindNewHead = true;
                    int validDataLen = 0xff & dataBuff[index + 2];//有效数据长度
                    int checkLen = 0xff & (validDataLen + 3);     //校验数据长度=有效数据长度+有效数据+字节头
                    if (dataBuff.length - index > checkLen) {//解析时判断后续数据长度是否充足不够
                       // Log.e("tag1", dataBuff.length + "," + index + "," + validDataLen + "checklen" + checkLen);
                        byte[] checkArr = new byte[validDataLen + 3];
                        System.arraycopy(dataBuff, index, checkArr, 0, validDataLen + 3);
                        byte checkBit = checkSum_crc8(checkArr, (byte) checkArr.length);
                        if (checkBit == dataBuff[index + checkLen]) {
                            byte[] validDataArr = new byte[validDataLen];
                            //Log.e("tag", checkArr.length + "," + validDataArr.length);
                            System.arraycopy(checkArr, 3, validDataArr, 0, validDataLen);
                            phaseValidData(validDataArr);

                            index += (checkLen + 1);
                            flagFindNewHead = false;
                            PreBuff.clear();
                            continue;
                        }
                    }
                }
            } else
                PreBuff.add(dataBuff[index]);
            if (flagFindNewHead)
                PreBuff.add(dataBuff[index]);
            index++;
        }
        return "";
    }

    String phaseValidData(byte[] data) {
        String buffString = "";

        Message msg = new Message();

        //Log.e(TAG, "phaseValidData " + data[0]);
        switch (data[0]) {//数据包类型
            case heartBeatResponse:
                int vesionNum = 0xff & data[1];
                int identificationID = 0xff & data[2];

                buffString = String.valueOf(vesionNum) + "," + String.valueOf(identificationID);

                Message msgHeartBeatR = new Message();
                msgHeartBeatR.what = heartBeatResponse;

                AdhocClientThread.receiHandler.sendMessage(msgHeartBeatR);
                break;
            case RecGpsInfo:

                int phaseIndex = 1;
                int personid2 = 0xff & data[phaseIndex++];
                int gpsCount = (data[phaseIndex] << 8) | data[++phaseIndex];

                phaseIndex++;
                int localState = data[phaseIndex++];
//                if (data[phaseIndex] == (byte) 0x00)
//                    localState = "GNSS未定位";
//                else if (data[phaseIndex] == (byte) 0x01)
//                    localState = "GNSS定位";
//                else if (data[phaseIndex] == (byte) 0x02)
//                    localState = "融合定位";
//                else if (data[phaseIndex] == (byte) 0x03)
//                    localState = "纯惯导定位";
                byte[] tmpLat = new byte[8];    //纬度

                System.arraycopy(data, phaseIndex, tmpLat, 0, 8);

                // Log.e("recHex lat:", bytesToHexString(tmpLat));
                double lat = Double.longBitsToDouble(getLong(tmpLat));
                phaseIndex += 8;
                int latType = data[phaseIndex++];
//                if (data[phaseIndex] == (byte) 0x01)
//                    latType = "北纬";
//                else if (data[phaseIndex] == (byte) 0x02)
//                    latType = "南纬";
          //      phaseIndex++;

                byte[] tmpLng = new byte[8];    //经度
                System.arraycopy(data, phaseIndex, tmpLng, 0, 8);
                //Log.e("recHex lng:", bytesToHexString(tmpLng));
                double lng = Double.longBitsToDouble(getLong(tmpLng));
                phaseIndex += 8;
                int lngType = data[phaseIndex++];
//                if (data[phaseIndex] == (byte) 0x01)
//                    lngType = "东经";
//                else if (data[phaseIndex] == (byte) 0x02)
//                    lngType = "西经";
//                phaseIndex++;
                byte[] temheight = new byte[4];    //高程
                System.arraycopy(data, phaseIndex, tmpLat, 0, 4);
                float height = Float.intBitsToFloat(getInt(tmpLat));
                phaseIndex += 4;

                int floor = data[phaseIndex++];

                int batteryFoot = 0xff & data[phaseIndex++];
                int batteryPhone = 0xff & data[phaseIndex++];
                int batteryNet = 0xff & data[phaseIndex++];

                int otherPos = 0xff & data[phaseIndex];
                //包类型，人员ID,定位包 流水号,定位状态,纬度,纬度半球,经度,经度半球,高程,楼层,足部电量，手机电量，自组网电量，获取队友位置
                buffString = String.valueOf(personid2) + "," + String.valueOf(gpsCount) + ","
                        + localState + "," + String.valueOf(lat) + "," + latType + "," + String.valueOf(lng) + "," + lngType + "," + String.valueOf(height) + ","
                        + String.valueOf(floor) + "," + String.valueOf(batteryFoot) + "," + String.valueOf(batteryPhone) + "," + String.valueOf(batteryNet)
                        + "," + String.valueOf(otherPos);
                GpsInfo gpsInfo = new GpsInfo(personid2,gpsCount,lat,lng,latType,lngType,height,localState,floor,batteryFoot,batteryPhone,batteryNet);

                //Log.e("Rec", "解析到定位包：" + buffString);
                Message msgGpsInfo = new Message();
                msgGpsInfo.what = RecGpsInfo;
                msgGpsInfo.obj = gpsInfo;
                if (AdhocClientThread.receiHandler != null)
                    AdhocClientThread.receiHandler.sendMessage(msgGpsInfo);
                break;
            case Sos:
                Log.e(TAG, "SOS："+bytesToHexString(data));
                 phaseIndex = 1;
                 personid2 = 0xff & data[phaseIndex++];
//                 int count = (data[phaseIndex] << 8) | data[++phaseIndex];
//                phaseIndex++;
//
//                 int rescuecount = 0xff & data[phaseIndex++];

                 Sos sos = new Sos();
                 sos.count = 1;
                 sos.id = personid2;

//                 for(int i=0;i<rescuecount;i++)
//                     sos.listRescue.add(phaseIndex++);

                Message msgSos = new Message();
                msgSos.what = Sos;
                msgSos.obj = sos;
                if (AdhocClientThread.receiHandler != null)
                    AdhocClientThread.receiHandler.sendMessage(msgSos);
                break;
            case recRetreatRepose:
                recCommonReposne(data, data[0]);
                break;
            case recRescueReponse:
                recCommonReposne(data, data[0]);
                break;
            case recConfigHeightReponse:
                recCommonReposne(data, data[0]);
                break;
            default:
                break;
        }
        return buffString;
    }

    //接收一般数据回应 有效数据只包含 id和流水号
    private void recCommonReposne(byte[] data, byte cmd) {

        Message msg = new Message();
        Log.e("recRescueReponse:", bytesToHexString(data));
        int phaseIndex = 1;
        int personid = 0xff & data[phaseIndex++];

//        byte[] countBytes = new byte[4];
//        System.arraycopy(data, phaseIndex, countBytes, 0, 4);
//
//        int count = bytes2Int(countBytes, true);

        msg.what = cmd;
        msg.obj =  personid ;
        if (AdhocClientThread.receiHandler != null)
            AdhocClientThread.receiHandler.sendMessage(msg);

    }

    public byte[] heartBeatPackage(byte personId, int packageCount) {
        byte[] byteArr = new byte[heartBeatLength + 4];
        int index = 0;
        System.arraycopy(fixHead, 0, byteArr, 0, 2);
        index += 2;
        byteArr[index] = (byte) heartBeatLength & 0xff;    //头加有效数据长度， 3个字节  kaochanggongzhengfutamen
        index++;
        //有效数据 zuijinhengaoxin
        System.arraycopy(heartBeatData(personId, packageCount), 0, byteArr, index, heartBeatLength);
        //校验位
        byte check = checkSum_crc8(byteArr, (byte) (byteArr.length - 1));
        byteArr[byteArr.length - 1] = check;
        return byteArr;
    }

    public byte[] gpsInfoPackage(byte personId, short packageCount, byte locationState, double lat, byte latType, double lng,
                                 byte lngType, float height, byte floor, byte batteryFoot, byte batteryPhone, byte batteryNet, ArrayList<Byte> getTeammatePos) {
        byte[] byteArrGps = gpsInfoData(personId, packageCount, locationState, lat, latType, lng, lngType, height, floor, batteryFoot, batteryPhone, batteryNet, getTeammatePos);
        byte[] byteArr = new byte[byteArrGps.length + 4];

        int index = 0;
        System.arraycopy(fixHead, 0, byteArr, 0, 2);
        index += 2;
        byteArr[index] = (byte) (byteArrGps.length & 0xff);    //头加有效数据长度， 3个字节
        index++;
        //有效数据
        System.arraycopy(byteArrGps, 0, byteArr, index, byteArrGps.length);
        //校验位
        byte[] checkArr = new byte[(byteArr.length - 1)];
        System.arraycopy(byteArr, 0, checkArr, 0, checkArr.length);
        byte check = checkSum_crc8(checkArr, (byte) (checkArr.length));
        byteArr[byteArr.length - 1] = check;
        return byteArr;
    }

    byte[] retreatPackage(int count, byte cmdLevel, boolean isAllRetreat, List<Integer> listid) {

        int idCount = 0;


        if (isAllRetreat)
            idCount = 1;
        else {
            idCount = listid.size();
        }

        int validlen = 6 + idCount;
        byte[] byteArr = new byte[validlen + 4];

        try {
            int index = 0;
            System.arraycopy(fixHead, 0, byteArr, 0, 2);
            index += 2;
            byteArr[index++] = (byte) (validlen & 0xff);


            byteArr[index++] = retreat;
            byte[] bytecount = int2Byte(count, true);
            System.arraycopy(bytecount, 0, byteArr, index, 4);
            index += 4;

            byteArr[index++] = cmdLevel;

            if(isAllRetreat)
                byteArr[index++] = (byte) 0xff;
            else
                for (int i = 0; i < idCount; i++) {

                    int id = listid.get(i);
                    byteArr[index++] = (byte) (id & 0xff);

                }
            byte check = checkSum_crc8(byteArr, (byte) (byteArr.length - 1));
            byteArr[byteArr.length - 1] = check;
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("retreatPackage数组越界！！！");
        }
        return byteArr;
    }

    byte[] rescuePackage(int count, byte rescuedid, byte rescuedstate,double rescuedLat,byte rescuedLat1,double rescuedLng,byte rescuedLng1,float rescuedHeight,byte floor,
        boolean isAllRescue,List<Integer> rescueidlist
    ) {

        int idCount = 0;

        if (isAllRescue)
            idCount = 1;
        else {
            idCount = rescueidlist.size();
        }

        int validlen = 26 + idCount;
        byte[] byteArr = new byte[validlen + 4];

        try {
            int index = 0;
            System.arraycopy(fixHead, 0, byteArr, 0, 2);
            index += 2;
            byteArr[index++] = (byte) (validlen & 0xff);

            byteArr[index++] = rescue;

//            byte[] bytecount = int2Byte(count, true);
//            System.arraycopy(bytecount, 0, byteArr, index, 4);
//            index += 4;

            byteArr[index++] = rescuedid;
            byteArr[index++] = rescuedstate;


            Log.e("test","搜救1："+rescuedLat+","+rescuedLng);
            //纬度
            long longLat = Double.doubleToLongBits(rescuedLat);
            byte[] byteArrlat = getBytes(longLat);
            System.arraycopy(byteArrlat, 0, byteArr, index, byteArrlat.length);
            index = index + byteArrlat.length;
            byteArr[index++] = rescuedLat1;
            //经度
            long longLng = Double.doubleToLongBits(rescuedLng);
            byte[] byteArrnLng = getBytes(longLng);
            System.arraycopy(byteArrnLng, 0, byteArr, index, byteArrnLng.length);
            index = index + byteArrnLng.length;
            byteArr[index++] = rescuedLng1;
            //高度
            int floatHeight = Float.floatToIntBits(rescuedHeight);
            byte[] byteHeight = getBytes(floatHeight);
            System.arraycopy(byteHeight, 0, byteArr, index, byteHeight.length);
            index = index + byteHeight.length;

            byteArr[index++] = floor;

            if(isAllRescue)
                byteArr[index++] = (byte) ( 0xff);
            else
                for (int i = 0; i < idCount; i++) {
                    int id = rescueidlist.get(i);
                    byteArr[index++] = (byte) (id & 0xff);
                }

            byte check = checkSum_crc8(byteArr, (byte) (byteArr.length - 1));
            byteArr[byteArr.length - 1] = check;
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("rescuePackage数组越界！！！");
        }
        return byteArr;
    }



    byte[] configHeightPackage(int count,float curFloor, float firstHeight, float topheight, float lowFirstHeight, float lowHeight) {
        byte[] byteArr = new byte[25 + 4];

        int index = 0;
        System.arraycopy(fixHead, 0, byteArr, 0, 2);
        index += 2;
        byteArr[index++] = (byte) 25 & 0xff;    //头加有效数据长度， 3个字节  kaochanggongzhengfutamen
        try {
            byteArr[index++] = configHeight;
            byte[] bytecount = int2Byte(count, true);
            byte[] bytefirstHeight = float2byte(firstHeight, true);
            byte[] bytetopheight = float2byte(topheight, true);
            byte[] bytelowFirstHeight = float2byte(lowFirstHeight, true);
            byte[] bytelowHeight = float2byte(lowHeight, true);

            System.arraycopy(bytecount, 0, byteArr, index, 4);
            index += 4;
            System.arraycopy(bytefirstHeight, 0, byteArr, index, 4);
            index += 4;
            System.arraycopy(bytetopheight, 0, byteArr, index, 4);
            index += 4;
            System.arraycopy(bytelowFirstHeight, 0, byteArr, index, 4);
            index += 4;
            System.arraycopy(bytelowHeight, 0, byteArr, index, 4);
            index += 4;
            System.arraycopy(bytelowHeight, 0, byteArr, index, 4);

            byte check = checkSum_crc8(byteArr, (byte) (byteArr.length - 1));
            byteArr[byteArr.length - 1] = check;
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("configHeightPackage数组越界！！！");
        }
        return byteArr;
    }

    byte[] heartBeatData(byte personId, int packageCount) {
        byte[] byteArr = new byte[heartBeatLength + 4];
        int index = 0;

        try {
            System.arraycopy(fixHead, 0, byteArr, 0, 2);
            index += 2;
            byteArr[index++] = heartBeat;
            byteArr[index++] = versionNum;
            byteArr[index++] = personId;
            byteArr[index++] = (byte) (packageCount >> 24);
            byteArr[index++] = (byte) (packageCount >> 16);
            byteArr[index++] = (byte) (packageCount >> 8);
            byteArr[index] = (byte) packageCount;

            byte check = checkSum_crc8(byteArr, (byte) (byteArr.length - 1));
            byteArr[byteArr.length - 1] = check;

        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("heartBeatPackage数组越界！！！");
        }
        return byteArr;
    }


    public byte[] sosReplyPackage(byte personId, int packageCount, boolean isRescue) {

        byte[] byteArr = new byte[sosReplyLength + 4];
        int index = 0;
        try {
            System.arraycopy(fixHead, 0, byteArr, 0, 2);
            index += 2;
            byteArr[index++] = (byte) (sosReplyLength & 0xff);    //头加有效数据长度， 3个字节
            byteArr[index++] = SosRepley;

            byteArr[index++] = personId;
            byte[] byteCount = int2Byte(packageCount, true);
            System.arraycopy(byteCount, 0, byteArr, index, byteCount.length);
            index += byteCount.length;

            if (isRescue)
                byteArr[index++] = 1;
            else
                byteArr[index++] = 0;
            byte check = checkSum_crc8(byteArr, (byte) (byteArr.length - 1));
            byteArr[byteArr.length - 1] = check;
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("sosReplyPackage数组越界！！！");
        }
        return byteArr;
    }

    public byte[] gpsInfoReplyPackage(byte personId, int packageCount) {

        byte[] byteArr = new byte[gpsInfoReplyLength + 4];
        int index = 0;
        try {


            System.arraycopy(fixHead, 0, byteArr, 0, 2);
            index += 2;
            byteArr[index++] = (byte) (gpsInfoReplyLength & 0xff);    //头加有效数据长度， 3个字节
            byteArr[index++] = gpsInfoReply;

            byteArr[index++] = personId;


            byte[] byteCount = int2Byte(packageCount, true);
            System.arraycopy(byteCount, 0, byteArr, index, byteCount.length);
            index += byteCount.length;

            byte check = checkSum_crc8(byteArr, (byte) (byteArr.length - 1));
            byteArr[byteArr.length - 1] = check;
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("gpsInfoReplyPackage数组越界！！！");
        }
        return byteArr;
    }


    byte[] gpsInfoData(byte personId, short packageCount, byte locationState, double lat, byte latType, double lng,
                       byte lngType, float height, byte floor, byte batteryFoot, byte batteryPhone, byte batteryNet, ArrayList<Byte> getTeammatePos) {
        int listLen = getTeammatePos.size();
        if (listLen > 0)
            listLen = listLen - 1;
/**
 */
        byte[] byteArr = new byte[gpsInfoLength + listLen + 4];
        int index = 0;
        try {

            byteArr[index++] = gpsInfo;
            byteArr[index++] = personId;
            byteArr[index++] = (byte) packageCount;
            byteArr[index++] = (byte) (packageCount >> 8);
            byteArr[index++] = locationState;
            //纬度
            long longLat = Double.doubleToLongBits(lat);
            byte[] byteArrlat = getBytes(longLat);
            System.arraycopy(byteArrlat, 0, byteArr, index, byteArrlat.length);
            index = index + byteArrlat.length;
            byteArr[index++] = latType;
            //经度
            long longLng = Double.doubleToLongBits(lng);
            byte[] byteArrnLng = getBytes(longLng);
            System.arraycopy(byteArrnLng, 0, byteArr, index, byteArrnLng.length);
            index = index + byteArrnLng.length;
            byteArr[index++] = lngType;
            //高度
            int floatHeight = Float.floatToIntBits(height);
            byte[] byteHeight = getBytes(floatHeight);
            System.arraycopy(byteHeight, 0, byteArr, index, byteHeight.length);
            index = index + byteHeight.length;

            byteArr[index++] = floor;
            byteArr[index++] = batteryFoot;
            byteArr[index++] = batteryFoot;
            byteArr[index++] = batteryNet;

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            String time = df.format(date);


            long epoch = df.parse(time).getTime() / 1000;
            int unixTimer = (int) (epoch & 0xffffffff);
            Log.e("*********time:", time + ", " + epoch + "," + unixTimer);

            byteArr[index++] = (byte) unixTimer;
            byteArr[index++] = (byte) (unixTimer >> 8);
            byteArr[index++] = (byte) (unixTimer >> 16);
            byteArr[index++] = (byte) (unixTimer >> 24);

            if (getTeammatePos.size() == 0) {
                byteArr[index] = 0x00;
            } else {
                for (int i = 0; i < getTeammatePos.size(); i++) {
                    byteArr[index + i] = getTeammatePos.get(i);
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("gpsInfoPackage数组越界！！！");
        }
        return byteArr;
    }


    byte checkSum_crc8(byte[] pData, byte len) {
        byte Crc;
        byte[] ch = new byte[8];
        byte ch1;
        byte i, j, k;
        Crc = (byte) 0xff;
        for (i = 0; i < len; i++) {
            ch1 = pData[i];
            for (j = 0; j < 8; j++) {
                ch[j] = (byte) (ch1 & 0x01);
                ch1 >>= 1;
            }
            for (k = 0; k < 8; k++) {
                ch[7 - k] <<= 7;
                if (((Crc ^ ch[7 - k]) & 0x80) > 0)
                    Crc = (byte) ((Crc << 1) ^ 0x1d);
                else
                    Crc <<= 1;
            }
        }
        Crc ^= 0xff;
        return Crc;
    }


    public byte[] getBytes(int data) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (data & 0xff);
        bytes[1] = (byte) ((data & 0xff00) >> 8);
        bytes[2] = (byte) ((data & 0xff0000) >> 16);
        bytes[3] = (byte) ((data & 0xff000000) >> 24);
        return bytes;
    }

    public byte[] getBytes(long data) {
        byte[] bytes = new byte[8];
        bytes[0] = (byte) (data & 0xff);
        bytes[1] = (byte) ((data >> 8) & 0xff);
        bytes[2] = (byte) ((data >> 16) & 0xff);
        bytes[3] = (byte) ((data >> 24) & 0xff);
        bytes[4] = (byte) ((data >> 32) & 0xff);
        bytes[5] = (byte) ((data >> 40) & 0xff);
        bytes[6] = (byte) ((data >> 48) & 0xff);
        bytes[7] = (byte) ((data >> 56) & 0xff);
        return bytes;
    }

    byte[] double2Bytes(double d) {
        long value = Double.doubleToRawLongBits(d);
        byte[] byteRet = new byte[8];
        for (int i = 0; i < 8; i++) {
            byteRet[i] = (byte) ((value >> 8 * i) & 0xff);
        }
        return byteRet;
    }


    public int getInt(byte[] bytes) {
        return (0xff & bytes[0]) | (0xff00 & (bytes[1] << 8)) | (0xff0000 & (bytes[2] << 16)) | (0xff000000 & (bytes[3] << 24));
    }

    public long getLong(byte[] bytes) {
        return (0xffL & (long) bytes[0]) | (0xff00L & ((long) bytes[1] << 8)) | (0xff0000L & ((long) bytes[2] << 16)) | (0xff000000L & ((long) bytes[3] << 24))
                | (0xff00000000L & ((long) bytes[4] << 32)) | (0xff0000000000L & ((long) bytes[5] << 40)) | (0xff000000000000L & ((long) bytes[6] << 48)) | (0xff00000000000000L & ((long) bytes[7] << 56));
    }

    public float getFloat(byte[] bytes) {
        return Float.intBitsToFloat(getInt(bytes));
    }

    public double getDouble(byte[] bytes) {
        return Double.longBitsToDouble(getLong(bytes));
    }

}
