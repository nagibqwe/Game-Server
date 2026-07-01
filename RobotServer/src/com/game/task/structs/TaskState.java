package com.game.task.structs;

/**
 * 任务状态
 */
public class TaskState {
    public static final int SUCCESS = 0;                //成功

    //==================失败说明===================
    public static final int BACKPACK_FULL = 1;          //背包已满无法提交
    public static final int TASK_NOT_FINISH = 2;        //任务目标没有达到
    public static final int PLAYER_NO_THIS_TASK = 3;    //玩家身上没有这个任务
    public static final int TOO_FAR_WITH_NPC = 4;       //与npc距离过远
    public static final int MAP_IS_NULL = 5;            //地图为null

    public static final int DEFAULT_REWARD = 1;         //日常任务默认倍率

    //==================特殊状态说明===================
    public static final int KILL_MONSTER_SHARE = 1;       //共同杀怪共享,不包括击杀者
    public static final int RANGE_SHARE = 2;              //范围杀怪共享，即没有碰过怪，但是在怪物死亡时在怪物的附近
}
