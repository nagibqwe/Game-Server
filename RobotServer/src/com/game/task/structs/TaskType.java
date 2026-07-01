package com.game.task.structs;

/**
 * 任务相关类型
 */
public class TaskType {

    ////////////////////////////任务分类//////////////////////////////////////////
    /**
     * 主线任务
     */
    public static transient final int MAIN_TASK = 0;
    /**
     * 日常任务
     */
    public static transient final int DAILY_TASK = 1;
    /**
     * 帮会日常、周常
     */
    public static transient final int GUILD_TASK = 2;
    /**
     * 支线任务
     */
    public static transient final int BRANCH_TASK = 3;
    /**
     * 边界任务
     */
    public static transient final int BORDER_TASK = 4;
    /**
     * 转职任务
     */
    public static transient final int GENDER_TASK = 5;
    /**
     * 护送任务
     */
    public static transient final int ESCORT_TASK = 6;
    /**
     * 所有任务
     */
    public static transient final int ALL_TASK = 100;


    ////////////////////////////任务执行分类//////////////////////////////////////////
    /**
     * NPC对话
     */
    public static transient final short ACTION_TYPE_NPC_TALK = 0;
    /**
     * 杀怪
     */
    public static transient final short ACTION_TYPE_KILL_MONSTER = 1;
    /**
     * 采集物品(虚拟存在)
     */
    public static transient final short ACTION_TYPE_GATHER = 2;
    /**
     * 使用道具
     */
    public static transient final short ACTION_TYPE_USE_ITEM = 3;
    /**
     * 提交道具
     */
    public static transient final short ACTION_TYPE_SUBMIT_ITEM = 4;
    /**
     * 押镖(或者说护送)
     */
    public static transient final short ACTION_TYPE_ESCORT = 5;
    /**
     * 功能操作
     */
    public static transient final short ACTION_TYPE_FUNCTION = 6;
    /**
     * 等级要求完成任务(卡等级任务)
     */
    public static transient final short ACTION_TYPE_NEED_LEVEL = 7;
    /**
     * 副本通关(必须通关)
     */
    public static transient final short ACTION_TYPE_SUCCESSFUL_ZONE = 8;
    /**
     * 到达某个指定的坐标
     */
    public static transient final short ACTION_TYPE_ARRIVE_POS = 9;
    /**
     * 收集虚拟道具
     */
    public static transient final short ACTION_TYPE_COLLECT_ITEM = 10;
    /**
     * 收集真实道具
     */
    public static transient final short ACTION_TYPE_COLLECT_REAL_ITEM = 11;
    /**
     * 完成位面
     */
    public static transient final short ACTION_TYPE_PLANE = 12;
    /**
     * 完成X个境界任务
     */
    public static transient final short ACTION_TYPE_VIP_STATE_TASK = 13;
    /**
     * 境界到达XX
     */
    public static transient final short ACTION_TYPE_VIP_STATE_BREAK = 14;
    /**
     * 完成法宝演示位面副本
     */
    public static transient final short ACTION_TYPE_PLANE_FABAO = 15;
    /**
     * 完成仙盟任务副本
     */
    public static transient final short ACTION_TYPE_GUILD_TASK_CLONE = 16;
}
