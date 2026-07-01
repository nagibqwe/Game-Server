package com.game.home.struct;

import game.message.HomeMessage;

/**
 * @Desc TODO
 * @Date 2021/7/12 18:23
 * @Auth ZUncle
 */
public class FurnitureFix {
    long id;
    int modelId;
    Vector3 pos;
    int dir;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public Vector3 getPos() {
        return pos;
    }

    public void setPos(Vector3 pos) {
        this.pos = pos;
    }

    public int getDir() {
        return dir;
    }

    public void setDir(int dir) {
        this.dir = dir;
    }

    public HomeMessage.FurnitureCell.Builder encode() {

        HomeMessage.Vector3.Builder mPos = HomeMessage.Vector3.newBuilder();
        mPos.setX(pos.getX());
        mPos.setY(pos.getY());
        mPos.setZ(pos.getZ());

        HomeMessage.FurnitureCell.Builder m = HomeMessage.FurnitureCell.newBuilder();
        m.setId(id);
        m.setModelId(modelId);
        m.setDir(dir);
        m.setPos(mPos);
        return m;
    }


}
