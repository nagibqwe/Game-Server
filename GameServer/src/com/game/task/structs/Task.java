package com.game.task.structs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.structs.GameObject;
import game.core.map.Position;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 任务类wq
 */
public abstract class Task extends GameObject {

    @JsonIgnore
    protected static transient final Logger logger = LogManager.getLogger(Task.class);

    ////////////////////////////任务分类//////////////////////////////////////////
    /**
     * 主线任务
     */
    @JsonIgnore
    public static transient final int MAIN_TASK = 0;
    /**
     * 日常任务
     */
    @JsonIgnore
    public static transient final int DAILY_TASK = 1;
    /**
     * 帮会日常、周常
     */
    @JsonIgnore
    public static transient final int GUILD_TASK = 2;
    /**
     * 支线任务
     */
    @JsonIgnore
    public static transient final int BRANCH_TASK = 3;
    /**
     * 转职任务
     */
    @JsonIgnore
    public static transient final int GENDER_TASK = 5;
    /**
     * 天禁令任务
     */
    @JsonIgnore
    public static transient final int FallingSky_TASK_DAY = 101;
    /**
     * 天禁令任务
     */
    @JsonIgnore
    public static transient final int FallingSky_TASK_STAGE = 102;
    /**
     * 天禁令任务
     */
    @JsonIgnore
    public static transient final int FallingSky_TASK_ROUND = 103;
    /**
     * 核心功能任务
     */
    @JsonIgnore
    public static transient final int FunctionTask = 104;
    /**
     * 所有任务
     */
    @JsonIgnore
    public static transient final int ALL_TASK = 100;

    ////////////////////////////////////////////任务完成的类型//////////////////////////////////////////////
    /**
     * 任务完成方式--普通完成
     */
    @JsonIgnore
    public transient static final int FINISH_TYPE_COMMON = 0;
    /**
     * 任务完成方式--一键完成
     */
    @JsonIgnore
    public transient static final int FINISH_TYPE_SUPPER_FINISH = 1;

    /////////////////////////////////////////////任务执行类型//////////////////////////////////////////////////////////
    /**
     * NPC对话
     */
    @JsonIgnore
    public static transient final short ACTION_TYPE_NPC_TALK = 0;
    /**
     * 杀怪
     */
    @JsonIgnore
    public static transient final short ACTION_TYPE_KILL_MONSTER = 1;
    /**
     * 采集物品(虚拟存在)
     */
    @JsonIgnore
    public static transient final short ACTION_TYPE_GATHER = 2;
    /**
     * 提交道具
     */
    @JsonIgnore
    public static transient final short ACTION_TYPE_SUBMIT_ITEM = 4;
    /**
     * 功能操作
     */
    @JsonIgnore
    public static transient final short ACTION_TYPE_FUNCTION = 6;
    /**
     * 等级要求完成任务(卡等级任务)
     */
    @JsonIgnore
    public static transient final short ACTION_TYPE_NEED_LEVEL = 7;
    /**
     * 到达某个指定的坐标
     */
    @JsonIgnore
    public static transient final short ACTION_TYPE_ARRIVE_POS = 9;
    /**
     * 收集虚拟道具
     */
    @JsonIgnore
    public static transient final short ACTION_TYPE_COLLECT_ITEM = 10;
    /**
     * 收集真实道具
     */
    @JsonIgnore
    public static transient final short ACTION_TYPE_COLLECT_REAL_ITEM = 11;
    /**
     * 完成位面
     */
    @JsonIgnore
    public static transient final short ACTION_TYPE_PLANE = 12;
    /**
     * 境界到达XX
     */
    @JsonIgnore
    public static transient final short ACTION_TYPE_VIP_STATE_BREAK = 14;
    /**
     * 完成法宝演示位面副本
     */
    @JsonIgnore
    public static transient final short ACTION_TYPE_PLANE_FABAO = 15;
    /**
     * 完成仙盟任务副本
     */
    @JsonIgnore
    public static transient final short ACTION_TYPE_GUILD_TASK_CLONE = 16;

    /////////////////////////////////////////一些可选的保留参数///////////////////////////////////////////
    /**
     * 提交的道具id
     */
    @JsonIgnore
    public static transient final String Optional_Key_Submit_Item_Id = "submitItemId";
    /**
     * 副本id
     */
    @JsonIgnore
    public static transient final String Optional_Key_Copy_Map_Id = "copyMapId";
    /**
     * 提交道具NPC的id
     */
    @JsonIgnore
    public static transient final String Optional_Key_Middle_Npc_Id = "submitNpcId";

    ////////////////////////////////////////////////任务自身数据////////////////////////////////////////////
    /**
     * 任务配置表id
     */
    protected int modelId = 0;
    /**
     * 所属角色id
     */
    protected long ownerId;
    /**
     * 当前任务需要的玩家等级
     */
    protected int needLevel = 1;
    /**
     * 当前任务需要的玩家阶数
     */
    protected int needDegree = 2;
    /**
     * 是否已经完成
     */
    protected boolean finish = false;
    /**
     * 此任务是否接受！
     */
    protected boolean isReceive = true;
    /**
     * 任务目标类型
     */
    protected int targetType;
    /**
     * 当前数量
     */
    protected int curNum;
    /**
     * 目标地图
     */
    protected int targetMap;
    /**
     * 一些需要使用坐标的任务
     */
    protected Position targetPosition = new Position();
    /**
     * 任务子类型
     */
    protected int subType = 0;
    /**
     * 默认奖励倍数
     */
    @JsonIgnore
    protected transient int taskRewardPer = 1;
    /**
     * 保留一些可选参数
     */
    protected ConcurrentHashMap<String, Integer> optionalParam = new ConcurrentHashMap<>(4);
    /**
     * 任务完成的方式。默认是自己手动完成
     */
    private int finishType = FINISH_TYPE_COMMON;

    ////////////////////////////自带方法////////////////////////////////

    /**
     * 获取任务目标
     *
     * @return
     */
    public abstract List<Integer> targetModels();

    /**
     * 任务完成需要的目标数量
     *
     * @return
     */
    public abstract int targetNum();

    /**
     * 完成任务
     *
     * @param player
     * @param isGM
     * @return
     */
    public abstract boolean finishTask(Player player, boolean isGM);

    /**
     * 任务状态改变，向客户端同步任务状态
     *
     * @param player
     */
    public abstract void changeTask(Player player);

    /**
     * 获得任务类型
     *
     * @return
     */
    public abstract byte acqType();

    /**
     * 检查是否结束
     *
     * @param isProm 是否发送提示到前端
     * @param player
     * @return
     */
    public boolean checkFinish(boolean isProm, Player player) {
        try {
            return Manager.taskManager.deal().checkFinish(player, this, isProm);
        } catch (Exception e) {
            logger.error(e, e);
        }
        return false;
    }

    /**
     * 根据任务类型排序
     *
     * @param o1
     * @param o2
     * @return
     */
    public int compare(Task o1, Task o2) {
        if (o1.acqType() != o2.acqType()) {
            return o1.acqType() - o2.acqType();
        }
        return 0;
    }

    /**
     * 获取当前任务进度
     *
     * @param player
     * @return
     */
    public int getCurTaskProgress(Player player) {
        switch (getTargetType()) {
            case ACTION_TYPE_NPC_TALK:
                return 0;
            case ACTION_TYPE_NEED_LEVEL:
                return player.getLevel();
            case ACTION_TYPE_VIP_STATE_BREAK:
                return player.getStateVip().getLv();
            default:
                return getCurNum();
        }
    }

    /////////////////////////getter和setter///////////////////////////////////////
    /**
     * 获取 任务配置表id
     *
     * @return modelId 任务配置表id
     */
    public int getModelId() {
        return this.modelId;
    }

    /**
     * 设置 任务配置表id
     *
     * @param modelId 任务配置表id
     */
    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    /**
     * 获取 所属角色id
     *
     * @return ownerId 所属角色id
     */
    public long getOwnerId() {
        return this.ownerId;
    }

    /**
     * 设置 所属角色id
     *
     * @param ownerId 所属角色id
     */
    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    /**
     * 获取 当前任务需要的玩家等级
     *
     * @return needLevel 当前任务需要的玩家等级
     */
    public int getNeedLevel() {
        return this.needLevel;
    }

    /**
     * 设置 当前任务需要的玩家等级
     *
     * @param needLevel 当前任务需要的玩家等级
     */
    public void setNeedLevel(int needLevel) {
        this.needLevel = needLevel;
    }

    /**
     * 获取 当前任务需要的玩家阶数
     *
     * @return needDegree 当前任务需要的玩家阶数
     */
    public int getNeedDegree() {
        return this.needDegree;
    }

    /**
     * 设置 当前任务需要的玩家阶数
     *
     * @param needDegree 当前任务需要的玩家阶数
     */
    public void setNeedDegree(int needDegree) {
        this.needDegree = needDegree;
    }

    /**
     * 获取 是否已经完成
     *
     * @return finish 是否已经完成
     */
    public boolean isFinish() {
        return this.finish;
    }

    /**
     * 设置 是否已经完成
     *
     * @param finish 是否已经完成
     */
    public void setFinish(boolean finish) {
        this.finish = finish;
    }

    /**
     * 获取 此任务是否接受！
     *
     * @return isReceive 此任务是否接受！
     */
    public boolean isIsReceive() {
        return this.isReceive;
    }

    /**
     * 设置 此任务是否接受！
     *
     * @param isReceive 此任务是否接受！
     */
    public void setIsReceive(boolean isReceive) {
        this.isReceive = isReceive;
    }

    /**
     * 获取 任务目标类型
     *
     * @return targetType 任务目标类型
     */
    public int getTargetType() {
        return this.targetType;
    }

    /**
     * 设置 任务目标类型
     *
     * @param targetType 任务目标类型
     */
    public void setTargetType(int targetType) {
        this.targetType = targetType;
    }

    /**
     * 获取 当前数量
     *
     * @return curNum 当前数量
     */
    public int getCurNum() {
        return this.curNum;
    }

    /**
     * 设置 当前数量
     *
     * @param curNum 当前数量
     */
    public void setCurNum(int curNum) {
        this.curNum = curNum;
    }

    /**
     * 获取 目标地图
     *
     * @return targetMap 目标地图
     */
    public int getTargetMap() {
        return this.targetMap;
    }

    /**
     * 设置 目标地图
     *
     * @param targetMap 目标地图
     */
    public void setTargetMap(int targetMap) {
        this.targetMap = targetMap;
    }

    /**
     * 获取 一些需要使用坐标的任务
     *
     * @return targetPosition 一些需要使用坐标的任务
     */
    public Position getTargetPosition() {
        return this.targetPosition;
    }

    /**
     * 设置 一些需要使用坐标的任务
     *
     * @param targetPosition 一些需要使用坐标的任务
     */
    public void setTargetPosition(Position targetPosition) {
        this.targetPosition = targetPosition;
    }

    /**
     * 获取 任务子类型
     *
     * @return subType 任务子类型
     */
    public int getSubType() {
        return this.subType;
    }

    /**
     * 设置 任务子类型
     *
     * @param subType 任务子类型
     */
    public void setSubType(int subType) {
        this.subType = subType;
    }

    /**
     * 获取 任务完成的方式。默认是自己手动完成
     *
     * @return finishType 任务完成的方式。默认是自己手动完成
     */
    public int getFinishType() {
        return this.finishType;
    }

    /**
     * 设置 任务完成的方式。默认是自己手动完成
     *
     * @param finishType 任务完成的方式。默认是自己手动完成
     */
    public void setFinishType(int finishType) {
        this.finishType = finishType;
    }

    /**
     * 获取 默认奖励倍数
     *
     * @return taskRewardPer 默认奖励倍数
     */
    public int getTaskRewardPer() {
        return this.taskRewardPer;
    }

    /**
     * 设置 默认奖励倍数
     *
     * @param taskRewardPer 默认奖励倍数
     */
    public void setTaskRewardPer(int taskRewardPer) {
        this.taskRewardPer = taskRewardPer;
    }

    /**
     * 获取 保留一些可选参数
     *
     * @return optionalParam 保留一些可选参数
     */
    public ConcurrentHashMap<String, Integer> getOptionalParam() {
        return this.optionalParam;
    }

    /**
     * 设置 保留一些可选参数
     *
     * @param optionalParam 保留一些可选参数
     */
    public void setOptionalParam(ConcurrentHashMap<String, Integer> optionalParam) {
        this.optionalParam = optionalParam;
    }
}
