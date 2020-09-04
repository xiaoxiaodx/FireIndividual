package com.example.firecommandandcontrolsystem.myClass;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;

import com.amap.api.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Firemen {


    private final String TAG = "Firemen";
    static final public int CMD_STATE_WAIT = 0;     //等待接收指令
    static final public int CMD_STATE_RESCUE = 1;   //救援
    static final public int CMD_STATE_RESCUED = 2;  //被救援
    static final public int CMD_STATE_RETREAT = 4;  //撤退

    //位置信息
    private LatLng curLocation = new LatLng(0.0000001, 0.000000001);
    private int floor;
    private float height;
    private int localState;
    private boolean isShowTrack = true;
    private List<LatLng> historicalTrack = new ArrayList<>();
    private List<PointF> historicalTrackgl = new ArrayList<>();
    private List<Float> historicalHeight = new ArrayList<>();
    private int lngState;
    private int latState;

    //室内轨迹点缓存,每个楼层一个List
    public List<LatLngFloor> listIndoorPoint= new ArrayList<>();

    //基本信息
    private String name;
    private int bindDeviceId;
    private String groud;
    private float[] showColor = new float[4];
    private String showColorStr;
    private Bitmap bitmap;


    //设备信息
    float airpressHeight;
    float airpress;
    float terminalBattery;
    float footBattery;
    float phoneBattery;


    //身体信息
    private String bloodType;
    //接收指令的状态  总共4个字节，暂时以最后一个字节  作为指令接收状态  1个字节有8位 每位都可以代表一个接收指令
    /*
    * 0x 00 00 00 00    无指令（位状态 0000 0000  0000 0000 0000 0000 0000 0000）
    * 0x 00 00 00 01    救援他人（位状态 0000 0000  0000 0000 0000 0000 0000 0001）
    * 0x 00 00 00 02    报警 被救援（位状态 0000 0000  0000 0000 0000 0000 0000 0010）
    * 0x 00 00 00 04    撤退（位状态 0000 0000  0000 0000 0000 0000 0000 0100）
    * */
    private int cmd_state = CMD_STATE_WAIT;

    //指令选中状态
    private boolean isSelectRescue = false;
    private boolean isSelectRescued = false;
    private boolean isSelectRetreat = false;


    public int getCmdStateRescued(){
        return (int)(cmd_state & CMD_STATE_RESCUED);
    }

    public void setCMDstate(int cmd){

        cmd_state |=  CMD_STATE_RESCUED;
    }
    public boolean isShowTrack() {
        return isShowTrack;
    }

    public void setShowTrack(boolean showTrack) {
        isShowTrack = showTrack;
    }

    public List<PointF> getHistoricalTrackgl() {
        return historicalTrackgl;
    }

    public void setHistoricalTrackgl(List<PointF> historicalTrackgl) {
        this.historicalTrackgl = historicalTrackgl;
    }

    public List<Float> getHistoricalHeight() {
        return historicalHeight;
    }

    public void setHistoricalHeight(List<Float> historicalHeight) {
        this.historicalHeight = historicalHeight;
    }

    public float getPhoneBattery() {
        return phoneBattery;
    }

    public void setPhoneBattery(float phoneBattery) {
        this.phoneBattery = phoneBattery;
    }

    public int getLngState() {
        return lngState;
    }

    public void setLngState(int lngState) {
        this.lngState = lngState;
    }

    public int getLatState() {
        return latState;
    }

    public void setLatState(int latState) {
        this.latState = latState;
    }

    public int getLocalState() {
        return localState;
    }

    public void setLocalState(int localState) {
        this.localState = localState;
    }

    public float getAirpressHeight() {
        return airpressHeight;
    }

    public void setAirpressHeight(float airpressHeight) {
        this.airpressHeight = airpressHeight;
    }

    public float getAirpress() {
        return airpress;
    }

    public void setAirpress(float airpress) {
        this.airpress = airpress;
    }

    public float getTerminalBattery() {
        return terminalBattery;
    }

    public void setTerminalBattery(float terminalBattery) {
        this.terminalBattery = terminalBattery;
    }

    public float getFootBattery() {
        return footBattery;
    }

    public void setFootBattery(float footBattery) {
        this.footBattery = footBattery;
    }

    public boolean isSelectRescue() {
        return isSelectRescue;
    }

    public void setSelectRescue(boolean selectRescue) {
        isSelectRescue = selectRescue;
    }

    public boolean isSelectRescued() {
        return isSelectRescued;
    }

    public void setSelectRescued(boolean selectRescued) {
        isSelectRescued = selectRescued;
    }

    public boolean isSelectRetreat() {
        return isSelectRetreat;
    }

    public void setSelectRetreat(boolean selectRetreat) {
        isSelectRetreat = selectRetreat;
    }

    public int getCmd_state() {
        return cmd_state;
    }

    public void setCmd_state(int cmd_state) {
        this.cmd_state = cmd_state;
    }

    public LatLng getCurLocation() {
        return curLocation;
    }

    public void setCurLocation(LatLng curLocation) {
        this.curLocation = curLocation;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public List<LatLng> getHistoricalTrack() {
        return historicalTrack;
    }

    public void setHistoricalTrack(List<LatLng> historicalTrack) {
        this.historicalTrack = historicalTrack;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBindDeviceId() {
        return bindDeviceId;
    }

    public void setBindDeviceId(int bindDeviceId) {
        this.bindDeviceId = bindDeviceId;
    }

    public String getGroud() {
        return groud;
    }

    public void setGroud(String groud) {
        this.groud = groud;
    }

    public float[] getShowColor() {
        return showColor;
    }

    public void setShowColor(float[] showColor) {
        this.showColor = showColor;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getShowColorStr() {
        return showColorStr;
    }

    public void setShowColorStr(String showColorStr) {
        this.showColorStr = showColorStr;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    static class LatLngFloor{

        public LatLngFloor(int floor, LatLng latLng) {
            this.floor = floor;
            this.latLng = latLng;
        }

        public int floor;
        LatLng latLng;
    }
}
