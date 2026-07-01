/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.horse.structs;

/**
 * 多人坐骑
 *
 * @author admin
 */
public class MultiPlayerHorse {

    private int trainCount;         //培养次数
    private int modelId;            //策划配置id
    private long ownerId;           //主人Id

    public int getTrainCount() {
        return trainCount;
    }

    public void setTrainCount(int trainCount) {
        this.trainCount = trainCount;
    }

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

}
