/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.structs;

import com.game.map.manager.MapsConfigManager;
import com.game.map.structs.ByteMapCfg;
import game.message.CommonMessage;

/**
 *
 * @author hewei@haowan123.com
 */
public class Position {

    private float x = 0.0f;
    private float y = 0.0f;

    public Position() {
    }

    public Position(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;

    }

    /**
     * 是否是同一个格子
     *
     * @param pos
     * @return
     */
    public boolean compare(Position pos) {
        return (int) this.x == (int) pos.getX() && (int) this.y == (int) pos.getY();
    }
    
    //获取点在地图中的索引
    public int getIndex(int mapDataId) {
        ByteMapCfg mapCfg = MapsConfigManager.getInstance().getMapCfg(mapDataId);
        if(mapCfg == null){
            return 0;
        }
        return mapCfg.getColCellCount() * (int) y + (int) x;
    }
    
    public CommonMessage.Position.Builder toPosition(){
        CommonMessage.Position.Builder msg = CommonMessage.Position.newBuilder();
        msg.setX(x);
        msg.setY(y);
        return msg;
    }

    @Override
    public String toString() {
        return "{" + x + " " + y + "}";
    }
}
