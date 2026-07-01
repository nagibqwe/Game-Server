package com.game.home.struct;

import game.message.HomeMessage;

/**
 * @Desc TODO  家具
 * @Date 2021/7/12 18:21
 * @Auth ZUncle
 */
public class Furniture {

    int modelId;
    int count;

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }


    public HomeMessage.Furniture.Builder encode() {
        HomeMessage.Furniture.Builder m = HomeMessage.Furniture.newBuilder();
        m.setModelId(modelId);
        m.setCount(count);
        return m;
    }

}
