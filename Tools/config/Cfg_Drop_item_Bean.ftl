/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs;

/**
 *
 * @author hewei
 */
public final class Cfg_Drop_item_Bean {

    /**
     * 物品掉落id
     */
    public final int dropId;
    /**
     * 掉落物品列表:Integer[0]是掉落类型(0是物品掉落，1掉落报),Integer[1]是物品id或者包id,Integer[2]概率,Integer[3]是最小数量,Integer[4]是最大数量,Integer[5]是否绑定(0不绑1绑定)
     */
    public final ReadIntegerArrayEs dropItems;
    /**
     * 每天全服触发此掉落id的次数
     */
    public final int serverdaily_control;

    /**
     * 每天同一个玩家触发此掉落id的次数
     */
    public final int localplayer_control;
	
	/**
     * 物品掉落id
     *
     * @return
     */
    public int getDropId() {
        return dropId;
    }

    /**
     * 掉落物品列表:Integer[0]是掉落类型(0是物品掉落，1掉落报),Integer[1]是物品id或者包id,Integer[2]概率,Integer[3]是最小数量,Integer[4]是最大数量,Integer[5]是否绑定(0不绑1绑定)
     *
     * @return
     */
    public ReadIntegerArrayEs getDropItems() {
        return dropItems;
    }

    /**
     * 每天全服触发此掉落id的次数
     *
     * @return
     */
    public int getServerdaily_control() {
        return serverdaily_control;
    }

    /**
     * 每天同一个玩家触发此掉落id的次数
     *
     * @return
     */
    public int getLocalplayer_control() {
        return localplayer_control;
    }

    /**
     *
     * @param dropId 物品掉落id
     * @param serverdaily_control 每天4:00到次日4:00，全服触发此掉落id的次数
     * @param localplayer_control 每天4:00到次日4:00，同一个玩家触发此掉落id的次数
     * @param drops 掉落列表，格式：[[1,2,5000,3,4,0],[1,2,5000,3,4,1]];Integer[0]是掉落类型(0是物品掉落，1掉落报),Integer[1]是物品id或者包id,Integer[2]概率,Integer[3]是最小数量,Integer[4]是最大数量,Integer[5]是否绑定(0不绑1绑定)
     */
    public Cfg_Drop_item_Bean(int dropId, int serverdaily_control, int localplayer_control, String drops) {
        this.dropId = dropId;
        this.serverdaily_control = serverdaily_control;
        this.localplayer_control = localplayer_control;
        dropItems = new ReadIntegerArrayEs(drops,"]",",");
    }
    
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("dropId:").append(dropId).append(";");
        str.append("dropItems:").append(dropItems).append(";");
        str.append("serverdaily_control:").append(serverdaily_control).append(";");
        str.append("localplayer_control_control:").append(localplayer_control).append(";");
        return str.toString();
    }
}
