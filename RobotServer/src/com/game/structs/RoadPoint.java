/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.structs;

import com.game.map.manager.MapsConfigManager;
import com.game.map.structs.ByteMapCfg;


/**
 *
 * @author zenghai <zenghai@haowan123.com>
 */
public class RoadPoint {

    //权值
    private int weight;
    //父格子
    private int farther = -1;
    //路点
    private Position pos;

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getFarther() {
        return farther;
    }

    public void setFarther(int farther) {
        this.farther = farther;
    }

    public Position getPos() {
        return pos;
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }
    
    public int getIndex(int mapDataId) {
        ByteMapCfg mapCfg = MapsConfigManager.getInstance().getMapCfg(mapDataId);
        if(mapCfg == null){
            return 0;
        }
        return mapCfg.getColCellCount() * (int) pos.getY() + (int) pos.getX();
    }
    

}
