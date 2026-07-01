/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.backpack.structs;

import com.game.util.IDConfigUtil;

/**
 *
 * @author lanxiang@haowan123.com
 */
public abstract class Item extends GameObject {

    private int itemModelId;

    private int num;

    private int gridId;

    private boolean bind;

    private int losttime;
    
    public int getItemModelId() {
        return itemModelId;
    }

    public void setItemModelId(int itemModelId) {
        this.itemModelId = itemModelId;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
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

    @Override
    public String toString() {
        return "[cellid=" + getGridId() + "][itemId=" + getId() + "][itemModleId=" + getItemModelId() + "][num=" + getNum() + "]";
    }

    //改名卡的type==10，为Gift
    public static Item createItem(int itemModelId) {
        Item item = new Gift();
        item.setId(IDConfigUtil.getId());
        item.setItemModelId(itemModelId);
        item.setNum(1); //发一个够了
        item.setBind(true);
        return item;
    }
    
}
