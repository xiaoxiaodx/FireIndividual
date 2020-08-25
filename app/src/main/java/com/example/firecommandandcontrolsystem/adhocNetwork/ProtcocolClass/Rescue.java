package com.example.firecommandandcontrolsystem.adhocNetwork.ProtcocolClass;

import java.util.ArrayList;
import java.util.List;

public class Rescue {

    public Rescue(int id, double lat, double lng, int latstate, int lngstate, float height, int localstate, int floor) {
        this.id = id;
        this.lat = lat;
        this.lng = lng;
        this.latstate = latstate;
        this.lngstate = lngstate;
        this.height = height;
        this.localstate = localstate;
        this.floor = floor;
    }

    public boolean isAllRescue = false;
    public static int count = 0;
    public int id;
    public double lat;
    public double lng;
    public int latstate;
    public int lngstate;
    public float height;

    public int localstate;
    public int floor;


    public List<Integer> listRescue = new ArrayList<>();
}
