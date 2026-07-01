/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.map.structs;

import game.core.map.Position;

/**
 * 出生点、复活点
 * @author zenghai <zenghai@haowan123.com>
 */
public class BrithPoint extends Position {
    
    private int id; //编号
    
    private int     campID;     //阵营ID

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public int getCampID() {
        return campID;
    }

    public void setCampID(int campID) {
        this.campID = campID;
    }
    
}
