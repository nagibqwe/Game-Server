/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.structs;

import game.core.map.Position;

/**
 *
 * @author zenghai
 */
public class Vector3 extends Position {

    public static Vector3 zero = new Vector3(0, 0, 0);
    public static Vector3 one = new Vector3(1, 1, 1);

    private float h;  //高度

    public Vector3() {
    }

    public Vector3(float x, float h, float y) {
        this.setX(x);
        this.h = h;
        this.setY(y);
    }

    public float getH() {
        return h;
    }

    public void setH(float h) {
        this.h = h;
    }
}
