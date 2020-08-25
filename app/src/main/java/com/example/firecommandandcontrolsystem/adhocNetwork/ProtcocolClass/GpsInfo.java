package com.example.firecommandandcontrolsystem.adhocNetwork.ProtcocolClass;

public class GpsInfo {

    public GpsInfo(int id, int count, double lat, double lng, int latState, int lngState, float height, int locationState, int floor, int footBattery, int phoneBattery, int adhocBattery) {
        this.id = id;
        this.count = count;
        this.lat = lat;
        this.lng = lng;
        this.latState = latState;
        this.lngState = lngState;
        this.height = height;
        this.locationState = locationState;
        this.floor = floor;
        this.footBattery = footBattery;
        this.phoneBattery = phoneBattery;
        this.adhocBattery = adhocBattery;
    }

    public int id;
    public int count;
    public double lat;
    public double lng;
    public int latState;
    public int lngState;
    public float height;

    public int locationState;

    public int floor;
    public int footBattery;
    public int phoneBattery;
    public int adhocBattery;
}
