package com.example.firecommandandcontrolsystem.adhocNetwork.ProtcocolClass;

public class FloorConfig {
    public FloorConfig(float firstfloorheight_top, float topheightperfloor, float firstfloorheight_bottom, float bottomheightperfloor) {
        this.firstfloorheight_top = firstfloorheight_top;
        this.topheightperfloor = topheightperfloor;
        this.firstfloorheight_bottom = firstfloorheight_bottom;
        this.bottomheightperfloor = bottomheightperfloor;
    }

    public float firstfloorheight_top;
    public float topheightperfloor;
    public float firstfloorheight_bottom;
    public float bottomheightperfloor;
}
