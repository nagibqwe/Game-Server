package com.game.map.structs;

import com.game.structs.Vector3;

//地图配置文件里的物件对象
public class ByteMapItem {

    private int id;     //地图物件id
    private String name;   //物件名字
    private Vector3 pos;
    private float rotW = 0.0f; //旋转坐标

    public ByteMapItem() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Vector3 getPos() {
        return pos;
    }

    public void setPos(Vector3 pos) {
        this.pos = pos;
    }

    public float getRotW() {
        return rotW;
    }

    public void setRotW(float rotW) {
        this.rotW = rotW;
    }

    public float getMapX() {
        return pos.getX();
    }

    public float getMapY() {
        return pos.getY();
    }

}
