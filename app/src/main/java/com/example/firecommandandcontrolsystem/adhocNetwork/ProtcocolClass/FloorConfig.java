package com.example.firecommandandcontrolsystem.adhocNetwork.ProtcocolClass;

public class FloorConfig {
    public FloorConfig(float curfloor, float firstfloorheight, float eachfloorheight, float fu_firstfloorheight, float fu_eachfloorheight) {
        this.curfloor = curfloor;
        this.firstfloorheight = firstfloorheight;
        this.eachfloorheight = eachfloorheight;
        this.fu_firstfloorheight = fu_firstfloorheight;
        this.fu_eachfloorheight = fu_eachfloorheight;
    }

    static public int count;
    public float curfloor;
    public float firstfloorheight;
    public float eachfloorheight;
    public float fu_firstfloorheight;
    public float fu_eachfloorheight;

}
