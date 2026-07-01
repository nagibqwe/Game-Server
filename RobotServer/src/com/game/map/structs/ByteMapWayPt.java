
package com.game.map.structs;

import java.util.List;

public class ByteMapWayPt {
    private float mapX;
    private float mapY;
    private String name;
    private int id;
    private int neighborCount;  //相邻点的数量
    private  List<Integer> neighborIds; //相邻点

    public ByteMapWayPt() {
    }

    public ByteMapWayPt(float mapX, float mapY, String name, int id, int neighborCount) {
        this.mapX = mapX;
        this.mapY = mapY;
        this.name = name;
        this.id = id;
        this.neighborCount = neighborCount;
    }
    
    public float getMapX() {
        return mapX;
    }

    public void setMapX(float mapX) {
        this.mapX = mapX;
    }

    public float getMapY() {
        return mapY;
    }

    public void setMapY(float mapY) {
        this.mapY = mapY;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNeighborCount() {
        return neighborCount;
    }

    public void setNeighborCount(int neighborCount) {
        this.neighborCount = neighborCount;
    }

    public List<Integer> getNeighborIds() {
        return neighborIds;
    }

    public void setNeighborIds(List<Integer> neighborIds) {
        this.neighborIds = neighborIds;
    }
    
   
    
}
