package com.game.structs;

/**
 *
 * 玩家状态
 */
public enum EntityState {
 
    Stand(0), //站立
    Move(1), //移动
    Run(2),     //跑
    Fly(3),     //飞行
    Dead(4), //死亡
    Pick(5), //采集
    ChangeMap(6), //切换地图
    ExitGame(8), //退出地图
    LoginGame(9), //登录游戏
    RunBack(10),    //回跑
    Jump(11),   //跳跃
    ReConnect(12),   //重连的状态
    Sitting(13),

    Max(31) //最大32个，
    ;
    private final int value;

    private EntityState(int value) {
        this.value = 1 << value;
    }

    public int getValue() {
        return this.value;
    }

    public boolean compare(int state) {
        return ((this.value & state) != 0);
    }
    public boolean compare(EntityState state) {
        return this.value == state.getValue();
    }
}
