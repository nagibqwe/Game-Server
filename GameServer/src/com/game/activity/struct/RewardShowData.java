package com.game.activity.struct;

/**
 * @author gaozhaoguang
 * @desc 奖励展示信息
 * @date Created on 2020/9/8 20:56
 **/
public class RewardShowData {
    //奖励展示类型
    private int type;
    //奖励物品ID
    private int itemID;
    //奖励的3D模型ID
    private int modelID;
    //奖励的3D模型大小
    private int scale;
    //奖励的3D模型的x方向偏移
    private int xOffset;
    //奖励的3D模型的y方向偏移
    private int yOffset;
    //奖励的3D模型的x方向旋转
    private int xRot;
    //奖励的3D模型的y方向旋转
    private int yRot;
    //奖励的3D模型的z方向旋转
    private int zRot;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public int getModelID() {
        return modelID;
    }

    public void setModelID(int modelID) {
        this.modelID = modelID;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public int getxOffset() {
        return xOffset;
    }

    public void setxOffset(int xOffset) {
        this.xOffset = xOffset;
    }

    public int getyOffset() {
        return yOffset;
    }

    public void setyOffset(int yOffset) {
        this.yOffset = yOffset;
    }

    public int getxRot() {
        return xRot;
    }

    public void setxRot(int xRot) {
        this.xRot = xRot;
    }

    public int getyRot() {
        return yRot;
    }

    public void setyRot(int yRot) {
        this.yRot = yRot;
    }

    public int getzRot() {
        return zRot;
    }

    public void setzRot(int zRot) {
        this.zRot = zRot;
    }
}
