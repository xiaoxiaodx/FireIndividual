package com.example.firecommandandcontrolsystem.uitls;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

//Big-Endian  大端    此处缩写为BE
//little-Endian 小端   此处缩写为LE

/***********
 *  大端和小端的数据转换由  islittleEndian 决定 为真则是小端，为假为大端
 *  大端和小端转换过程正好相反，小端低字节放低位，高字节放高位，大端正好相反
 *  所以他们转换时位置加起来刚好是数据的字节长度，由于数组是从0开始的因此大小减1
 */
public class HelperDataConversion {
    //short与字节数组
    public static byte[] short2byte(short res,boolean islittleEndian) {
        int size = 1 ;
        if(islittleEndian){
            size = 0;
        }
        byte[] targerts = new byte[2];
        targerts[Math.abs(0 - size)] = (byte) (res & 0xff);
        targerts[Math.abs(1 - size)] = (byte) ((res >> 8)&0xff);
        return targerts;
    }

    public static short byte2Short(byte[] res,boolean islittleEndian ) {
        short targets = bytes2Short(res, 0,islittleEndian);// | 表示安位或
        return targets;
    }

    public static short bytes2Short(byte[] bytes, int offset,boolean islittleEndian) {
        int size = 1 ;
        if(islittleEndian){
            size = 0;
        }
        return (short) ((0xff00 & (bytes[Math.abs(offset + 1-size )] << 8))
                | (0x00ff & bytes[Math.abs(offset + 0-size)]));
    }

    //int与字节数组
    public static byte[] int2Byte(int res,boolean islittleEndian) {
        int size = 3 ;
        if(islittleEndian){
            size = 0;
        }
        byte[] targets = new byte[4];
        targets[Math.abs(0-size)] = (byte) (res & 0xff);
        targets[Math.abs(1-size)] = (byte) ((res >> 8) & 0xff);
        targets[Math.abs(2-size)] = (byte) ((res >> 16) & 0xff);
        targets[Math.abs(3-size)] = (byte) (res >>> 24);
        return targets;
    }

    public static int bytes2Int(byte[] bytes, int offset,boolean islittleEndian) {
        int size = 3 ;
        if(islittleEndian){
            size = 0;
        }
        return (0xff000000 & (bytes[Math.abs(offset + 3 - size)] << 24))
                | (0x00ff0000 & (bytes[Math.abs(offset + 2-size)] << 16))
                | (0x0000ff00 & (bytes[Math.abs(offset + 1-size)] << 8))
                | (0x000000ff & bytes[Math.abs(offset + 0-size)]);
    }

    public static int bytes2Int(byte[] bytes,boolean islittleEndian ) {
        return bytes2Int(bytes, 0,islittleEndian);
    }

    public static byte[] float2byte(float Value,boolean islittleEndian){
        int accum = Float.floatToIntBits(Value);
        return int2Byte(accum,islittleEndian);
    }

    //long 与字节数组
    public static byte[] long2byte(long res,boolean islittleEndian) {
        int size = 7 ;
        if(islittleEndian){
            size = 0;
        }
        byte[] targerts = new byte[8];
        targerts[Math.abs(0-size)] = (byte) (res & 0xff);
        targerts[Math.abs(1-size)] = (byte) ((res >> 8) & 0xff);
        targerts[Math.abs(2-size)] = (byte) ((res >> 16) & 0xff);
        targerts[Math.abs(3-size)] = (byte) ((res >> 24)&0xff);
        targerts[Math.abs(4-size)] = (byte) ((res >> 32)&0xff);
        targerts[Math.abs(5-size)] = (byte) ((res >> 40)&0xff);
        targerts[Math.abs(6-size)] = (byte) ((res >> 48)&0xff);
        targerts[Math.abs(7-size)] = (byte) ((res >> 56)&0xff);
        return targerts;
    }

    public static long byte2long(byte[] bytes,boolean islittleEndian) {

        return byte2long(bytes,0,islittleEndian);
    }

    public static long byte2long(byte[] bytes,int offset,boolean islittleEndian) {
        int size = 7 ;
        if(islittleEndian){
            size = 0;
        }
        return   (0xff00000000000000l & (bytes[Math.abs(offset + 7-size)] << 56))
                | (0x00ff000000000000l & (bytes[Math.abs(offset + 6-size)] << 48))
                | (0x0000ff0000000000l & (bytes[Math.abs(offset + 5-size)] << 40))
                | (0x000000ff00000000l) & (bytes[Math.abs(offset + 4-size)] << 32)
                | (0x00000000ff000000 & (bytes[Math.abs(offset + 3-size)] << 24))
                | (0x0000000000ff0000 & (bytes[Math.abs(offset + 2-size)] << 16))
                | (0x000000000000ff00 & (bytes[Math.abs(offset + 1-size)] << 8))
                | (0x00000000000000ff & bytes[Math.abs(offset + 0-size)]);
    }

    //float与字节数组
    public static float bytes2Float(byte[] bytes, int offset,boolean islittleEndian) {
        return Float.intBitsToFloat(bytes2Int(bytes, offset,islittleEndian));
    }

    public static float bytes2Float(byte[] bytes,boolean islittleEndian) {
        return bytes2Float(bytes, 0,islittleEndian);
    }

    //double与字节数组
    public static double byte2Double(byte[] Array,boolean islittleEndian) {
        return Double.longBitsToDouble(byte2long(Array,islittleEndian));
    }

    public static byte[] Double2byte(double Value, boolean islittleEndian)
    {
        long accum = Double.doubleToRawLongBits(Value);
        return long2byte(accum,islittleEndian);
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    public static int getUnsignedByte(byte data) { // 将data字节型数据转换为0~255 (0xFF
        // 即BYTE)。
        return data & 0x0FF;
    }

    public static char[] getChars(byte[] bytes) {
        Charset cs = Charset.forName("UTF-8");
        ByteBuffer bb = ByteBuffer.allocate(bytes.length);
        bb.put(bytes);
        bb.flip();
        CharBuffer cb = cs.decode(bb);

        return cb.array();
    }

    private static byte[] getBytes(char[] chars) {
        Charset cs = Charset.forName("UTF-8");
        CharBuffer cb = CharBuffer.allocate(chars.length);
        cb.put(chars);
        cb.flip();
        ByteBuffer bb = cs.encode(cb);

        return bb.array();
    }


    /**
     * 对象转数组
     * @param obj
     * @return
     */
    static public byte[] toByteArray (Object obj) {
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray ();
            oos.close();
            bos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return bytes;
    }

    /**
     * 数组转对象
     * @param bytes
     * @return
     */
    static public Object toObject (byte[] bytes) {
        Object obj = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream (bytes);
            ObjectInputStream ois = new ObjectInputStream (bis);
            obj = ois.readObject();
            ois.close();
            bis.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return obj;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    public static byte checkSum_crc8(byte[] data, int len) {
        byte Crc;
        byte[] ch = new byte[8];
        byte ch1;
        int i, j, k;

        Crc = (byte) 0xff;
        for (i = 0; i < len; i++) {
            ch1 = data[i];
            for (j = 0; j < 8; j++) {
                ch[j] = (byte) (ch1 & (byte) 0x01);
                ch1 >>= 1;
            }

            for (k = 0; k < 8; k++) {
                ch[7 - k] <<= 7;
                if (((Crc ^ ch[7 - k]) & (byte) 0x80) != 0)
                    Crc = (byte) ((Crc << 1) ^ (byte) 0x1d);
                else
                    Crc <<= 1;
            }
        }

        Crc ^= 0xff;
        return Crc;
    }

    public static int checkSum_crc16(byte[] pData, int dataLen) {
        final int wCRCTalbeAbs[] =
                {
                        0x0000, 0xCC01, 0xD801, 0x1400, 0xF001, 0x3C00, 0x2800, 0xE401, 0xA001, 0x6C00, 0x7800, 0xB401, 0x5000, 0x9C01, 0x8801, 0x4400,
                };

        int wCRC = 0xFFFF;
        byte chChar;
        for (int i = 0; i < dataLen; i++) {
            chChar = pData[i];
            wCRC = wCRCTalbeAbs[(chChar ^ wCRC) & 15] ^ (wCRC >> 4);
            wCRC = wCRCTalbeAbs[((chChar >> 4) ^ wCRC) & 15] ^ (wCRC >> 4);
        }
        return wCRC;
    }
}
