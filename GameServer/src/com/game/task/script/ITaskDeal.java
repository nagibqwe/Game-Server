package com.game.task.script;

import com.data.struct.ReadArray;
import com.data.struct.ReadLongArrayEs;
import com.game.backpack.structs.Item;
import com.game.player.structs.Player;
import com.game.task.log.TaskLogBean;
import com.game.task.structs.Task;
import game.message.taskMessage;
import game.message.taskMessage.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 任务对外处理接口
 */
public interface ITaskDeal {

    /**
     * 一键完成某个指定的任务
     * @param player
     * @param mess
     */
    void OnReqOneKeyOverTask(Player player, ReqOneKeyOverTask mess);

    /**
     * 一键完成所有任务
     * @param player
     * @param mess
     */
    void OnReqQuickFinish(Player player, ReqQuickFinish mess);

    /**
     * 请求完成任务
     * @param player
     * @param mess
     */
    void OnReqTaskFinish(Player player, ReqTaskFinish mess);

    void OnReqTaskReceive(Player player, ReqReceiveTask mess);

    void OnReqCheckTaskIsFinish(Player player, ReqCheckTaskIsFinish mess);

    /**
     * 请求任务状态变更，主要用于需要在某个具体坐标做特殊处理之后才能完成的任务
     * 客户端主动请求改变任务状态，服务器不处理任务状态
     * @param player
     * @param mess
     */
    void OnReqChangeTaskState(Player player, taskMessage.ReqChangeTaskState mess);

    /**
     * 有任务事件发生 通知到各个任务
     * 将任务的行为数据收集
     * @param player     玩家
     * @param actionType 行为类型
     * @param model
     * @param num
     * @param sign 唯一标识，参数（可以用来做一些特殊用途） 用来单场任务使用，或者是额外参数,如果sign == 100，表示是被共享着
     */
    void action(Player player, short actionType, int model, int num, long sign);

    void action(Player player, short actionType, int model, int num);

    /**
     * 处理任务
     */
    void computeTask(Player player, int type, boolean isLogin, boolean isRefresh);

    /**
     * 服务器自主控制接取任务
     * @param player
     * @param type
     * @param modelId
     * @param isLogin
     */
    void acceptTask(Player player, int type, int modelId, int subType, boolean isLogin);
    /**
     * 服务器自主控制接取任务
     * @param player
     * @param type
     * @param modelId
     * @param isLogin
     */
    public void acceptTask(Player player, int type, int modelId, int subType, boolean isLogin, boolean isRefresh);
    /**
     * 检查任务是否完成
     * @param player
     * @param task
     * @param isPromp  参数值， 任务， 玩家， 是否公告,是否发送提示到前端
     * @return 返回是否能完成
     */
    boolean checkFinish(Player player, Task task, boolean isPromp);

    /**
     * 一键完成所有任务
     * @param player
     * @param taskType
     * @param subType
     * @param isCheck
     * @param isGM
     */
    void quickFinish(Player player, int taskType, int subType, boolean isCheck, boolean isGM,int taskCount);

    /**
     * 加载所有可接任务
     */
    void loadAllCanReceiveTask();

    /**
     * 玩家登陆和断线重连重新计算任务
     * 任务数据检查并发送任务列表给客户端
     * @param player
     */
    void loginCheckTask(Player player);

    /**
     * 每天0点的定时事件
     * @param player
     */
    void zeroClockDeal(Player player);

    /**
     * 检查一些任务类型是否开启了
     * @param player 玩家
     * @param type   任务类型
     * @return 该玩家当前任务类型是否开启
     */
    boolean checkFunctionIsOpen(Player player, int type);

    /**
     * 检查功能操作类的任务
     * @param player
     * @param type
     */
    void functionVariableChange(Player player, int type, int changeNum);

    /**
     * 任务变更同步周围场景
     * @param player
     * @param show_npcArray
     * @param show_monsterArray
     * @param show_gatherArray
     */
    void taskChangeRoundSync(Player player, ReadArray<Integer> show_npcArray, ReadArray<Integer> show_monsterArray, ReadArray<Integer> show_gatherArray);

    /**
     * 玩家等级变化后处理任务的相关事宜
     * @param player
     */
    void addTaskLevelUp(Player player);

    /**
     * 玩家等级提升的时候处理转职任务
     * @param player
     */
    void levelUpDealGenderTask(Player player);

    /**
     * 添加数据，合并同类项
     * @param one
     * @param other
     * @return 新集合
     */
    void mergeArrayList(List<Integer[]> one, Integer[] other);

    /**
     * 扣除失败，删减上一轮的消耗
     * @param one
     * @param other
     */
    void removeLastAdd(List<Integer[]> one, Integer[] other);

    /**
     * 添加奖励并堆叠，减小奖励list长度，不然保存数据库会报错
     * @param rewardArray 原奖励列表
     * @param addRewards  新增奖励
     */
    void addRewardsAndHeapUp(List<List<Long>> rewardArray, List<List<Long>> addRewards);

    /**
     * 添加奖励并堆叠，减小奖励list长度，不然保存数据库会报错
     * @param rewardArray 原奖励列表
     * @param addRewards  新增奖励
     */
    void addRewardsAndHeapUp(List<List<Long>> rewardArray, ReadLongArrayEs addRewards);

    ReadArray<Integer>[] buildEs(ArrayList<Integer[]> oneKeyFinishNeedGold);

    /**
     * 一键完成发送奖励
     * @param player      玩家
     * @param rewardArray 奖励列表
     * @param actionId    唯一码
     * @param type    任务类型
     */
    void sendOneKeyReward(Player player, List<List<Long>> rewardArray, int actionId, int type, int reason);

    /**
     * 背包满时，以邮件方式发送奖励
     * @param player
     * @param list
     */
    void sendRewardByMail(Player player, List<Item> list);

    /**
     * 背包满时，以邮件方式发送奖励
     * @param player
     * @param list
     */
    void sendRewardByMail(Player player, List<Item> list, int reason, long actionId);

    /**
     * 写入领奖日志
     * @param player
     * @param modelId    任务配置表模型Id
     * @param type       任务类型如主线，日常等等
     * @param rewardItem 奖励拼接的字符串
     * @param rewardType 奖励的倍数
     */
    void writeRewardLog(Player player, int modelId, int type, List<Item> rewardItem, int rewardType);

    void writeRewardLog(Player player, int modelId, int type, String reward, int rewardType);

    /**
     * 填充任务日志基本信息
     */
    void fillTaskLogBaseInfo(Player player, TaskLogBean taskLog, Task task);
    
    /**
     * 用来填充任务可选项参数
     * 在任务初始化的时候根据target来填充任务的一些东西
     * @param task
     * @param target
     */
    void fullTaskInitOptionParam(Task task, ReadArray target);

    /**
     * 用来填充任务可选项参数
     * @param task
     * @param target  目标
     * @param pos     位置
     */
    void fullTaskOptionParam(Task task, ReadArray target, ReadArray pos);
    
    /**
     * 每个任务与客户端交互的时候都有一个taskAttribute 协议，此方法填充此协议的统一的地方
     * @param task
     * @param builder 
     */
    void buildTaskAttributeSame(Task task, taskMessage.taskAttribute.Builder builder);

    /**
     * 创建一个空的taskMessage.taskAttribute.Builder 对象
     */
    taskMessage.taskAttribute.Builder buildNullTaskAttribute();

    /**
     * 根据类型获取任务(2017.02.20)
     *
     * @param player
     * @param type
     * @param modelId
     * @return
     */
    Task getTaskByModelId(Player player, int type, int modelId);

    /**
     * 玩家身上是否有此模板类型的任务
     * @param player
     * @param modelId
     * @return
     */
    Task getTaskByModelId(Player player, int modelId);

    /**
     * 每个任务在获取任务目标id的时候都会调用这个方法，除了战场任务 获取任务的目标Id
     * @param type
     * @param target
     * @return
     */
    int getTargetModelByType(int type, ReadArray target);

    /**
     * 每个任务在获取任务目标个数的时候都会调用这个方法，除了战场任务 获取任务的目标数量
     * @param type
     * @param target
     * @return
     */
    int getTargetNumByType(int type, ReadArray target);

    /**
     * 首次主线任务ID
     */
    int getFirstMainTaskId();

    /**
     * 获取主线任务标识
     * @param mainTaskId
     * @return
     */
    String getMainTaskChatName(int mainTaskId);

    /**
     * 获取能任务掉落的所以怪物id集合
     * 转职任务
     * @return <>不会为null,至少空集合</>
     */
    List<Integer> getMonsterListCanTaskDrop(Player player);

    /**
     * 检查任务是否完成
     * @param player
     * @param taskId
     * @return
     */
    boolean isTaskFinish(Player player, int taskId);

    /**
     * 完成任务
     * @param player    玩家
     * @param taskType  任务类型
     * @param modelId   任务模型
     * @param taskId    任务的实际ID
     * @param finishPer 完成任务得奖倍数
     * @param subType   任务子类型
     */
    void onFinishTask(Player player, int taskType, int modelId, int taskId, int finishPer, int subType);

    /**
     * 请求领取目标阶段奖励
     * @param player
     */
    void onReqGetTarget(Player player);

    /**
     * 发送任务目标信息
     * @param player
     */
    void sendResTargetInfo(Player player);
}
