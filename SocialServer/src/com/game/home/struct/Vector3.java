package com.game.home.struct;

import game.message.HomeMessage;

/**
 * @Desc TODO
 * @Date 2021/7/23 17:00
 * @Auth ZUncle
 */
public class Vector3 {

    float x;
    float y;
    float z;

    public Vector3() {
    }

    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3(HomeMessage.Vector3 pos) {
        this.x = pos.getX();
        this.y = pos.getY();
        this.z = pos.getZ();
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }
}
