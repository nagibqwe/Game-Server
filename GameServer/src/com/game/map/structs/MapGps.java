
package com.game.map.structs;

import game.core.map.Position;

/**
 * 角色定位
 *
 * @author zhaibiao
 */
public class MapGps {

    private int line;//第几线
    private long mapId;//地图Id;
    private int modelId;//地图模型
    private int type;//地图归属类型：0:世界观地图和休息室及副本，1:世界地图，2:跨服副本
    private Position pos = new Position(0, 0);//坐标

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public long getMapId() {
        return mapId;
    }

    public void setMapId(long mapId) {
        this.mapId = mapId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public Position getPos() {
        return pos;
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }

    public void reset() {
        this.line = 0;
        this.mapId = 0;
        this.modelId = 0;
        this.pos = new Position(0,0);//, type)
        this.type = 0;
    }

    @Override
    public String toString() {
        return "MapGps{" + "mapId=" + mapId + ", modelId=" + modelId + ", type=" + type + ", pos=" + pos + '}';
    }

}
