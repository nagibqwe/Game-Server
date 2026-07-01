package com.game.map.structs;

import game.core.map.Position;

/**
 * 传送点
 * @author zenghai <zenghai@haowan123.com>
 */
public class Transport {
    
    private int id;
    private Position initPos;
    private int targetID; //目标地图ID
    private Position targetPos;
    private boolean jump = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Position getInitPos() {
        return initPos;
    }

    public void setInitPos(Position initPos) {
        this.initPos = initPos;
    }

    public int getTargetID() {
        return targetID;
    }

    public void setTargetID(int targetID) {
        this.targetID = targetID;
    }

    public Position getTargetPos() {
        return targetPos;
    }

    public void setTargetPos(Position targetPos) {
        this.targetPos = targetPos;
    }

    public boolean isJump() {
        return jump;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }
}
