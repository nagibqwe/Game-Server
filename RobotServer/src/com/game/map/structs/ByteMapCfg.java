/*
 * 地图资源的load  and  reload
 * 
 */
package com.game.map.structs;

import java.util.ArrayList;
import java.util.List;

public class ByteMapCfg {

    private int mapID;              //地图modulID
    private int maxPlayerCount;     //地图承载最大人数
    private int colCellCount;       //地图中的Cell列数量    一个cell在client 的大小 1.0
    private int rowCellCount;       //地图中的Cell行数量

    private double high;               //地图高度
    private byte[][] blocks;             //阻挡数据  index = trunc(y) * colCount + trunc(x);  阻挡信息中0是阻挡，1是非阻挡
    private float[][] heigh;           //高度数据

    private ArrayList<ByteMapTrigger> triggerCfg = new ArrayList<>();   //触发器
    private ArrayList<ByteMapItem> npcCfg = new ArrayList<>();
    private ArrayList<ByteMapItem> monsterCfg = new ArrayList<>();
    private ArrayList<ByteMapItem> reliveCfg = new ArrayList<>();    //复活点
    private ArrayList<ByteMapWayPt> wayCfg = new ArrayList<>();       //路点
    private ArrayList<ByteMapItem> collectCfg = new ArrayList<>();   //采集物
    private ArrayList<ByteMapItem> useItemCfg = new ArrayList<>();   //使用物品
    private ArrayList<ByteDynamicBlock> dynamics = new ArrayList<>();

    public ByteMapCfg() {
    }

    public ByteMapCfg(int mapID, int maxPlayerCount, int colCellCount, int rowCellCount, double high) {
        this.mapID = mapID;
        this.maxPlayerCount = maxPlayerCount;
        this.colCellCount = colCellCount;
        this.rowCellCount = rowCellCount;
        this.high = high;
    }

    public byte[][] getBlocks() {
        return blocks;
    }

    public void setBlocks(byte[][] blocks) {
        this.blocks = blocks;
    }

    public float[][] getHeigh() {
        return heigh;
    }

    public void setHeigh(float[][] heigh) {
        this.heigh = heigh;
    }

    public int getMapID() {
        return mapID;
    }

    public void setMapID(int mapID) {
        this.mapID = mapID;
    }

    public int getMaxPlayerCount() {
        return maxPlayerCount;
    }

    public void setMaxPlayerCount(int maxPlayerCount) {
        this.maxPlayerCount = maxPlayerCount;
    }

    public int getColCellCount() {
        return colCellCount;
    }

    public void setColCellCount(int colCellCount) {
        this.colCellCount = colCellCount;
    }

    public int getRowCellCount() {
        return rowCellCount;
    }

    public void setRowCellCount(int rowCellCount) {
        this.rowCellCount = rowCellCount;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public ArrayList<ByteMapTrigger> getTriggerCfg() {
        return triggerCfg;
    }

    public void setTriggerCfg(ArrayList<ByteMapTrigger> triggerCfg) {
        this.triggerCfg = triggerCfg;
    }

    public ArrayList<ByteMapItem> getNpcCfg() {
        return npcCfg;
    }

    public void setNpcCfg(ArrayList<ByteMapItem> npcCfg) {
        this.npcCfg = npcCfg;
    }

    public ArrayList<ByteMapItem> getMonsterCfg() {
        return monsterCfg;
    }

    public void setMonsterCfg(ArrayList<ByteMapItem> monsterCfg) {
        this.monsterCfg = monsterCfg;
    }

    public ArrayList<ByteMapItem> getReliveCfg() {
        return reliveCfg;
    }

    public void setReliveCfg(ArrayList<ByteMapItem> reliveCfg) {
        this.reliveCfg = reliveCfg;
    }

    public ArrayList<ByteMapWayPt> getWayCfg() {
        return wayCfg;
    }

    public void setWayCfg(ArrayList<ByteMapWayPt> wayCfg) {
        this.wayCfg = wayCfg;
    }

    public ArrayList<ByteMapItem> getUseItemCfg() {
        return useItemCfg;
    }

    public void setUseItemCfg(ArrayList<ByteMapItem> useItemCfg) {
        this.useItemCfg = useItemCfg;
    }

    public ArrayList<ByteMapItem> getCollectCfg() {
        return collectCfg;
    }

    public void setCollectCfg(ArrayList<ByteMapItem> collectCfg) {
        this.collectCfg = collectCfg;
    }

    public ArrayList<ByteDynamicBlock> getDynamics() {
        return dynamics;
    }

    public void setDynamics(ArrayList<ByteDynamicBlock> dynamics) {
        this.dynamics = dynamics;
    }

}
