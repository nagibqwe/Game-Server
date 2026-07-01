/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.map.structs;

/**
 *
 * @author hewei@haowan123.com
 */
public class MapMonster extends BaseNpc{

    @Override
    protected void onForceStopMove() {

    }

    @Override
    public String toString() {
        return "怪物【" + name + "】 curPos = " + curPos + " modelId=" + modelId + " id =" + id;
    }
}
