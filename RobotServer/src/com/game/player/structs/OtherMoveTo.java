/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.player.structs;

import com.game.structs.Position;

/**
 *移动事件的队列结构
 * @author xuchangming <xysoko@qq.com>
 */
public class OtherMoveTo {
    private long instanceId;
    private long endTime;
    private final Position pos = new Position();

    public long getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(long instanceId) {
        this.instanceId = instanceId;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public Position getPos() {
        return pos;
    }
    
    
}
