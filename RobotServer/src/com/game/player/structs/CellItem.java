/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.player.structs;

/**
 *格子物品
 * @author xuchangming <xysoko@qq.com>
 */
public class CellItem {
    private long id;

    private int itemModelId;

    private long num;

    private int gridId;
    //是否绑定
    private boolean bind;
    //失效时间
    private int losttime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getItemModelId() {
        return itemModelId;
    }

    public void setItemModelId(int itemModelId) {
        this.itemModelId = itemModelId;
    }

    public long getNum() {
        return num;
    }

    public void setNum(long num) {
        this.num = num;
    }

    public int getGridId() {
        return gridId;
    }

    public void setGridId(int gridId) {
        this.gridId = gridId;
    }

    public boolean isBind() {
        return bind;
    }

    public void setBind(boolean bind) {
        this.bind = bind;
    }

    public int getLosttime() {
        return losttime;
    }

    public void setLosttime(int losttime) {
        this.losttime = losttime;
    }
}
