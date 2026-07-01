/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs;

/**
 * 掉落包配置
 *
 * @author hewei
 */
public final class Cfg_Drop_package_Bean {

    /**
     * 掉落包id
     */
    public final int dropPackageId;
    /**
     * 掉落列表:Integer[0]是道具配置表id,Integer[1]是最小数量,Integer[2]是最大数量,Integer[3]是权重
     */
    public final ReadIntegerArrayEs dropItems;
    /**
     * 权重和
     */
    public final int sumWeight;

    /**
     * 掉落包id
     *
     * @return
     */
    public int getDropPackageId() {
        return dropPackageId;
    }

    /**
     * 掉落列表:Integer[0]是道具配置表id,Integer[1]是最小数量,Integer[2]是最大数量,Integer[3]是权重
     *
     * @return
     */
    public ReadIntegerArrayEs getDropItems() {
        return dropItems;
    }

    /**
     * 权重和
     *
     * @return
     */
    public int getSumWeight() {
        return sumWeight;
    }

    /**
     * 初始化
     *
     * @param dropPackageId 掉落包id
     * @param sumWeight 权重和
     * @param drops 掉落列表,格式为:[[1,2,3,4],[1,2,3,4]]
     */
    public Cfg_Drop_package_Bean(int dropPackageId, int sumWeight, String drops) {
        this.dropPackageId = dropPackageId;
        this.sumWeight = sumWeight;
        dropItems = new ReadIntegerArrayEs(drops, "]", ",");
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("dropPackageId:").append(dropPackageId).append(";");
        str.append("dropItems:").append(dropItems).append(";");
        str.append("sumWeight:").append(sumWeight).append(";");
        return str.toString();
    }

}
