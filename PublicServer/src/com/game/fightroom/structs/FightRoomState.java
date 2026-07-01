/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.fightroom.structs;

/**
 *
 * @author soko <xuchangming@haowan123.com>
 */
public class FightRoomState {

    public static final int FR_NULL = 0;//无状态
    public static final int CREATEROOM = 1;//创建
    public static final int READYROOM = 2;//准备
    public static final int FIGHT_WAIT = 3;//战斗等待
    public static final int FIGHTING = 4;//战斗打架状态
    public static final int FIGHTEND = 5;//结束
    public static final int FIGHTREWARDEND = 6;//结算完成
    public static final int FIGHTTODEL = 7;//可以删除的状态了
}
