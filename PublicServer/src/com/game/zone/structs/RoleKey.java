/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.zone.structs;

/**
 *  玩家定位Key
 * @author zhaibiao
 */
public class RoleKey {
    private long id;
    private int modeId;
    private int size;

    public RoleKey(int modeId, int size) {
        this.modeId = modeId;
        this.size = size;
    }
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getModeId() {
        return modeId;
    }

    public void setModeId(int modeId) {
        this.modeId = modeId;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
    
    
}
