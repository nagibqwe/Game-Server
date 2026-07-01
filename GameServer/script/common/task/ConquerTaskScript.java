package common.task;

import com.data.*;
import com.data.bean.Cfg_Statue_model_Bean;
import com.data.bean.Cfg_Task_conquer_Bean;
import com.data.struct.ReadArray;
import com.data.struct.ReadIntegerArrayEs;
import com.data.struct.ReadLongArrayEs;
import com.game.backpack.structs.Item;
import com.game.backpack.structs.ItemCoinType;
import com.game.chat.structs.Notify;
import com.game.count.structs.BaseCountType;
import com.game.count.structs.Count;
import com.game.dailyactive.structs.DailyActiveDefine;
import com.game.manager.Manager;
import com.game.map.manager.MapManager;
import com.game.map.structs.MapObject;
import com.game.npc.structs.Npc;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.task.log.ConquerTaskAcceptLog;
import com.game.task.log.ConquerTaskFinishLog;
import com.game.task.log.GuildTaskGiveUpLog;
import com.game.task.log.GuildTaskRefreshLog;
import com.game.task.script.IConquerTask;
import com.game.task.script.ILevelTask;
import com.game.task.script.ITaskScript;
import com.game.task.structs.*;
import com.game.utils.MessageUtils;
import com.game.utils.RandomUtils;
import com.game.utils.Utils;
import com.game.welfare.struct.RetrieveType;
import game.core.dblog.LogService;
import game.core.map.Position;
import game.core.script.IScript;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.taskMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 帮会日常、周常任务脚本
 */
public class ConquerTaskScript implements ITaskScript, ILevelTask, IConquerTask {

    private static final Logger log = LogManager.getLogger(ConquerTaskScript.class);
    //仙盟任务类型
    private static final int TYPE_GUILD = 2;
    //仙盟任务池数量
    private static final int GuildMaxNum = 6;

    @Override
    public int getId() {
        return ScriptEnum.ConquerTaskBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void computeTask(Player player, boolean isLogin, boolean isRefresh) {
        if (!player.isHaveGuild()) {
            //退出公会依旧重置仙盟任务次数
            if(isLogin){
                long receiverTime = player.getConquerTaskTime().get(TYPE_GUILD) == null ? 0 : player.getConquerTaskTime().get(TYPE_GUILD);
                boolean isSame = isSameDayOrWeek(TYPE_GUILD, receiverTime);
                if(!isSame){
                    player.getConquerTaskCount().put(TYPE_GUILD, 0);
                    player.getGuildTaskPool().setRefreshCount(0);
                    sendResGuildTaskPool(player);
                }
            }else if(isRefresh){
                player.getConquerTaskCount().put(TYPE_GUILD, 0);
                player.getGuildTaskPool().setRefreshCount(0);
                sendResGuildTaskPool(player);
            }
            return;
        }
        checkConquerTask(player.getCurConquerTasks());
        if (player.getCurConquerTasks().get(TYPE_GUILD) != null && player.getGuildTaskPool().getTaskIds().isEmpty()) {
            refreshGuildTaskPool(player);
            sendResGuildTaskPool(player);
        }
        Iterator<Map.Entry<Integer, ConquerTask>> conquerTaskIte = player.getCurConquerTasks().entrySet().iterator();
        while (conquerTaskIte.hasNext()) {
            Map.Entry<Integer, ConquerTask> conquerTask = conquerTaskIte.next();
            int subType = conquerTask.getKey();
            ConquerTask task = conquerTask.getValue();
            //从上个时间节点到现在共做了多少次任务
            int count = player.getConquerTaskCount().get(subType) == null ? 0 : player.getConquerTaskCount().get(subType);
            //上次接取任务的时间
            long receiverTime = player.getConquerTaskTime().get(subType) == null ? 0 : player.getConquerTaskTime().get(subType);

            boolean isSame = isSameDayOrWeek(subType, receiverTime);
            if (!isSame) {
                //不同一天或同一周
                player.getConquerTaskCount().put(subType, 0);
                player.getConquerTaskTime().put(subType, TimeUtils.Time());
                if (subType == TYPE_GUILD) {
                    player.getGuildTaskPool().setRefreshCount(0);
                } else {
                    if (task.getModelId() == 0) {
                        player.getCurConquerTasks().put(subType, new ConquerTask());
                        noticeDeleteTask(player, Task.GUILD_TASK, task.getModelId());
                    }
                    //初始化一个前置任务，用于客户端显示在可接任务列表中
                    int taskId = getConquerPreTaskId(player, subType);
                    if (taskId == -1) {
//                    log.error("找不到帮会日常、周常任务的前置任务，直接开始任务，subType = " + subType);
                        acceptTask(player, 0, subType, isLogin, isRefresh);
                        continue;
                    }
                    acceptTask(player, taskId, subType, isLogin, isRefresh);
                }
            } else {
                if (subType == TYPE_GUILD) return;
                if (task.getModelId() == 0 && count == 0) {
                    //没有前置任务，重新接取
                    int taskId = getConquerPreTaskId(player, subType);
                    if (taskId == -1) {
//                        log.info("找不到帮会日常、周常任务的前置任务，直接开始任务，subType = " + subType);
                        acceptTask(player, 0, subType, isLogin, isRefresh);
                        continue;
                    }
                    acceptTask(player, taskId, subType, isLogin, isRefresh);
                } else if (task.getModelId() != 0 && count < (Global.GuildTaskMax.get(subType).get(1) + 1)) {
                    //检查任务配置是否正确，不正确重新随机一个
                    int conquerTaskId = task.getModelId();
                    Cfg_Task_conquer_Bean model = CfgManager.getCfg_Task_conquer_Container().getValueByKey(conquerTaskId);
                    if (model == null) {
                        player.getCurConquerTasks().put(subType, new ConquerTask());
                        acceptTask(player, 0, subType, isLogin, isRefresh);
                    }
                } else if (task.getModelId() == 0 && count < (Global.GuildTaskMax.get(subType).get(1) + 1) && isLogin) {
                    //任务断层，重新接取
                    acceptTask(player, 0, subType, isLogin, isRefresh);
                }
            }
        }
    }

    @Override
    public void acceptTask(Player player, int modelId, int subType, boolean isLogin, boolean isRefresh) {
//        log.info("玩家 = " + player.getName() + "，接取到帮会任务，任务subtype = " + subType);
        if (!player.isHaveGuild()) {
            if (subType == TYPE_GUILD) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.Guildtaskhint1);
            }
            log.info(TaskHelp.getPlayerInfo(player) + "还没有帮会！");
            return;
        }

        int count = player.getConquerTaskCount().get(subType) == null ? 0 : player.getConquerTaskCount().get(subType);
        if (count >= Global.GuildTaskMax.get(subType).get(1)) {
            log.info(TaskHelp.getLog(player, Task.GUILD_TASK, TaskHelp.TaskMax));
            return;
        }

        ConquerTask conquerTask = player.getCurConquerTasks().get(subType);
        if (conquerTask != null && conquerTask.getModelId() != 0) {
            if (conquerTask.isIsReceive()) {
                log.info(TaskHelp.getLog(player, Task.GUILD_TASK, TaskHelp.HaveTask));
                return;
            } else {
                conquerTask.setIsReceive(true);
            }
        } else {
            conquerTask = new ConquerTask();
            conquerTask.setSubType(subType);
            if (!initTask(player, conquerTask, modelId)) {
                log.info("帮会日常、周常任务初始化失败！");
                return;
            }
            player.getCurConquerTasks().put(subType, conquerTask);
        }
        //receive = false表示可以接取但是还没有接取
        if (subType != TYPE_GUILD && !conquerTask.isIsReceive()) {
            --count;
        }
        player.getConquerTaskCount().put(subType, count + 1);
        Cfg_Task_conquer_Bean model = CfgManager.getCfg_Task_conquer_Container().getValueByKey(conquerTask.getModelId());
        Manager.biManager.getScript().biTask(player, conquerTask.getModelId(), Task.GUILD_TASK, TaskConst.BI_RECEIVE, model.getTask_name(), conquerTask.getSubType(), model.getTask_grade());
        if (!isLogin) {
            changeTask(player, conquerTask, isRefresh);
        }
        writeAcceptTaskLog(player, conquerTask);
    }

    @Override
    public boolean onFinishTask(Player player, int modelId, int taskId, int finishPer, boolean isGm, int subType) {
        int rewardPer = TaskConst.DEFAULT_REWARD;
        //检查任务是否存在
        ConquerTask task = (ConquerTask) Manager.taskManager.deal().getTaskByModelId(player, Task.GUILD_TASK, taskId);
        if (task == null) {
            log.info(TaskHelp.getLog(player, Task.GUILD_TASK, TaskHelp.NoFind, taskId));
            buildResTaskFinish(player, taskId, TaskConst.PLAYER_NO_THIS_TASK, rewardPer, subType);
            return false;
        }
        //检查配置
        Cfg_Task_conquer_Bean bean = CfgManager.getCfg_Task_conquer_Container().getValueByKey(taskId);
        if (bean == null) {
            log.info(TaskHelp.configError(Task.GUILD_TASK, taskId, TaskHelp.ConfigNoTask));
            return false;
        }
        //客户端不发subtype时，以配置表的为准
        int realSubType = subType;
        if (bean.getConquer_subtype() != subType) {
            realSubType = bean.getConquer_subtype();
        }
        int goldnum = 0;
        if (realSubType != TYPE_GUILD) {
            if (rewardPer > 1) {
                Integer[] overCostGold = bean.getDouble_currency().getValue();
                if ((overCostGold != null) && overCostGold.length == 2) {
                    goldnum = overCostGold[1];
                } else {
                    log.info(TaskHelp.configError(Task.GUILD_TASK, taskId, " over_currency error"));
                    return false;
                }
            }
        }

        //如果是与对话相关任务都闲检查一遍与npc或者雕像的距离
        if (bean.getAuto_commit() == 1) {
            if (bean.getTask_type() == Task.ACTION_TYPE_NPC_TALK) {
                int npcId = task.targetModels().get(0);
                int statueId = bean.getStatue_ID();
                if (!nearNpcOrStatue(player, npcId, statueId)) {
                    buildResTaskFinish(player, taskId, TaskConst.TOO_FAR_WITH_NPC, rewardPer, realSubType);
                    return false;
                }
            }
        }

        if (!task.checkFinish(false, player)) {
            buildResTaskFinish(player, taskId, TaskConst.TASK_NOT_FINISH, rewardPer, realSubType);
            return false;
        }

        if (realSubType != TYPE_GUILD && goldnum > 0) {
            //元宝扣除成功，则完成任务
            if (Manager.currencyManager.manager().decGold(player, goldnum, ItemChangeReason.TaskFinishFanBeiDec, task.getId())) {
                task.setTaskRewardPer(rewardPer);
                noResumeFinishTask(player, task, rewardPer);
            } else {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.Task_TwoPer_Over_ConquerTask);//{0}任务2倍完成元宝扣除失败
                Manager.backpackManager.manager().sendItemNotEnough(player, ItemCoinType.GemCoin);
                return false;
            }
        } else {
            noResumeFinishTask(player, task, rewardPer);

            if (bean.getTask_grade() == 4) {
                Manager.countManager.addCount(player, BaseCountType.GuildTaskTotal, 0, Count.RefreshType.CountType_Forever, 1);
                Manager.controlManager.deal().operate(player, FunctionVariable.GuildTaskTotal, 1);
            }
        }
        Manager.biManager.getScript().biTask(player, task.getModelId(), Task.GUILD_TASK, TaskConst.BI_FINISH, bean.getTask_name(), task.getSubType(), bean.getTask_grade());
        try {
//            log.info("玩家 = " + player.getName() + "，完成帮会任务，开始接取下一个任务，任务subtype = " + realSubType);
            if (realSubType != TYPE_GUILD) {
                Manager.taskManager.deal().acceptTask(player, Task.GUILD_TASK, 0, realSubType, false);
            }
        } catch (Exception e) {
            log.error(TaskHelp.getPlayerInfo(player) + "接取帮会任务失败", e);
        }
        return true;
    }

    @Override
    public void noResumeFinishTask(Player player, ConquerTask task, int finishType) {
        task.setNeedLevel(0);
        task.setNeedDegree(0);
        buildResTaskFinish(player, task.getModelId(), TaskConst.SUCCESS, finishType, task.getSubType());
        //增加记数
        sendRewards(player, task, finishType);

        player.getCurConquerTasks().put(task.getSubType(), new ConquerTask());

        if (task.getSubType() == 1) {
            Manager.countManager.addCount(player, BaseCountType.Event_TIMES, FunctionVariable.GuildRiChangNum, Count.RefreshType.CountType_Forever, 1);
            Manager.controlManager.deal().operate(player, FunctionVariable.GuildRiChangNum, 1);
        } else if (task.getSubType() == 0) {
            Manager.countManager.addCount(player, BaseCountType.Event_TIMES, FunctionVariable.GuildZhouChangNum, Count.RefreshType.CountType_Forever, 1);
            Manager.controlManager.deal().operate(player, FunctionVariable.GuildZhouChangNum, 1);
        } else if (task.getSubType() == TYPE_GUILD) {
            player.getCurConquerTasks().get(task.getSubType()).setSubType(task.getSubType());
            player.getGuildTaskPool().getTaskIds().remove((Integer) task.getModelId());
            Manager.countManager.addCount(player, BaseCountType.Event_TIMES, FunctionVariable.GuildRiChangNum, Count.RefreshType.CountType_Forever, 1);
            Manager.controlManager.deal().operate(player, FunctionVariable.GuildRiChangNum, 1);
        }

        if (log.isDebugEnabled()) {
            log.debug("conquerTask finshTask() - end");
        }
        try {
            writeFinishTaskLog(player, task);
            taskFinishAfter(player, task.getModelId(), task.getSubType(), task.getTargetType());
        } catch (Exception e) {
            log.error(e, e);
        }
    }

    @Override
    public void taskFinishAfter(Player player, int currentModelId, int subType, int targetType) {
        //周常任务没有最终奖励
        int count = player.getConquerTaskCount().get(subType);
        if ((subType == 0 && count % 10 == 0) || (subType != 0 && subType != TYPE_GUILD && count == Global.GuildTaskMax.get(subType).get(1))) {
            List<List<Long>> rewardArray = new ArrayList<>();
            for (ReadArray<Long> item : Global.GuildTaskRingReward.getValuees()) {
                if (item.size() < 3) {
                    log.info("Global.CONQUER_TASK_REWARD配置出错！！");
                    continue;
                }
                if (item.get(0) != subType) {
                    continue;
                }
                List<Long> list = new ArrayList<>();
                list.add(item.get(1));
                list.add(item.get(2));
                rewardArray.add(list);
            }
            List<Item> createItems = Item.createItems(rewardArray, 1);
            // 是否有足够的空间加入多个物品
            if (!Manager.backpackManager.manager().addItems(player, createItems, getReason(subType), currentModelId)) {
                Manager.taskManager.deal().sendRewardByMail(player, createItems, getReason(subType), getReason(subType));
            }
            Manager.taskManager.deal().writeRewardLog(player, currentModelId, Task.GUILD_TASK, createItems, TaskConst.DEFAULT_REWARD);
        }
        if (subType == 0) {
            Manager.dailyActiveManager.deal().addDailyProgress(player, DailyActiveDefine.ZP_WEEK, 1);
        } else if (subType == 1) {
            Manager.dailyActiveManager.deal().addDailyProgress(player, DailyActiveDefine.ZP_DAILY, 1);
        } else if (subType == TYPE_GUILD) {
            Manager.dailyActiveManager.deal().addDailyProgress(player, DailyActiveDefine.GUILD_TASK, 1);
            //仙盟任务完成后需要刷新任务池
            refreshGuildTaskPool(player);
            sendResGuildTaskPool(player);

            Manager.retrieveResManager.getScript().count(player, RetrieveType.GuildTask);
        }
    }

    @Override
    public void changeTask(Player player, Task task, boolean isRefresh) {
        if (!Manager.taskManager.deal().checkFunctionIsOpen(player, Task.GUILD_TASK)) {
            return;
        }
        taskMessage.ResConquerTaskChang.Builder msg = taskMessage.ResConquerTaskChang.newBuilder();
        msg.setQuestsTask((taskMessage.conquerTaskInfo) buildTaskInfo(player, task));
        MessageUtils.send_to_player(player, taskMessage.ResConquerTaskChang.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    /**
     * 初始化帮会日常、周常任务
     *
     * @param player  当前玩家
     * @param task
     * @param modelId
     * @return 是否接任务成功
     */
    public boolean initTask(Player player, Task task, int modelId) {
        if (log.isDebugEnabled()) {
            log.debug("conquerTask init() - start");
        }
        ConquerTask conquerTask = (ConquerTask) task;
        int taskId = 0;
        boolean receive = false;
        if (task.getSubType() == TYPE_GUILD) {
            if (modelId <= 0) {
                return false;
            }
            //检查任务池中是否有该任务
            if (!player.getGuildTaskPool().getTaskIds().contains((Integer) modelId)) {
                log.info("请求接的仙盟任务不在任务池中，任务ID:" + modelId);
                return false;
            }
            taskId = modelId;
            receive = true;
        } else {
            taskId = getCanReceiveTask(player, task.getSubType(), false);
        }
        if (taskId < 0) {
            log.info("没有找到合适的帮会日常、周常任务，请策划同学注意一下： 玩家等级 + " + player.getLevel() + ",subType=" + task.getSubType());
            return false;
        }
        Cfg_Task_conquer_Bean model = CfgManager.getCfg_Task_conquer_Container().getValueByKey(taskId);
        if (model == null) {
            log.error("公会任务不存在！ " + taskId);
            return false;
        }
        if (task.getSubType() != TYPE_GUILD) {
            receive = model.getRecieve() == 0;
            if (modelId > 0) {
                receive = false;
            }
        }
        conquerTask.setIsReceive(receive);
        conquerTask.setTargetType(model.getTask_type());
        conquerTask.setOwnerId(player.getId());
        conquerTask.setModelId(model.getId());
        conquerTask.setFinish(false);
        conquerTask.setTaskRewardPer(1);
        conquerTask.setTargetMap(model.getGoal_map());

        Manager.taskManager.deal().fullTaskOptionParam(conquerTask, model.getGoal_npc(), model.getTask_x_z());
        player.getConquerTaskTime().put(task.getSubType(), TimeUtils.Time());
        player.getCurConquerTasks().put(task.getSubType(), conquerTask);
        return true;
    }

    public void sendRewards(Player player, Task task, int rewardType) {
        //先扣除需要的物品
        ReadLongArrayEs rewardArray = getRewardArray(player, task, rewardType);
        List<Item> createItems = Item.createItems(rewardArray);

        // 是否有足够的空间加入多个物品
        List<Item> spilthGoods = new ArrayList<>();
        for (Item item : createItems) {
            if (Manager.backpackManager.manager().onHasAddSpace(player, item)) {
                Manager.backpackManager.manager().addItem(player, item, getReason(task.getSubType()), task.getModelId());
            } else {
                spilthGoods.add(item);
            }
        }
        //无空间则发邮件
        if (spilthGoods.size() > 0) {
            Manager.taskManager.deal().sendRewardByMail(player, spilthGoods, getReason(task.getSubType()), getReason(task.getSubType()));
        }

        Manager.taskManager.deal().writeRewardLog(player, task.getModelId(), Task.GUILD_TASK, createItems, rewardType);
    }

    @Override
    public void oneKeyFinishAllConquerTask(Player player, int subType) {
        //检查配置表数据是否正常
        final ConquerTask nowTask = player.getCurConquerTasks().get(subType);
        if (nowTask.getModelId() == 0) {
            return;
        }
        //完成次数已达最大上限
        int conquerTaskCount = player.getConquerTaskCount().get(subType) == null ? 0 : player.getConquerTaskCount().get(subType);
        if (conquerTaskCount > Global.GuildTaskMax.get(subType).get(1)) {
            return;
        }

        Cfg_Task_conquer_Bean bean = CfgManager.getCfg_Task_conquer_Container().getValueByKey(nowTask.getModelId());
        if (bean == null) {
            log.info(TaskHelp.getPlayerInfo(player) + "帮会日常、周常任务配置不存在！" + nowTask.getModelId());
            return;
        }
        if (bean.getOver_currency().size() < 2) {
            log.info(TaskHelp.getPlayerInfo(player) + "帮会日常、周常任务配置消耗错误！" + nowTask.getModelId());
            return;
        }

        //当前任务之前消耗的金币数量之和，用作扣除
        ArrayList<Integer[]> oneKeyFinishNeedGold = new ArrayList<>();
        //当前任务的消耗的金币数量，与oneKeyFinishNeedGold一起作用，用作判断是否够消耗
        Integer[] nowFinishNeedGold = null;
        if (nowTask != null) {
            nowFinishNeedGold = bean.getOver_currency().getValue();
        }
        //当前金币够扫荡到的位置
        int randomCount = conquerTaskCount;
        //随机任务ID
        int randomTaskId = nowTask.getModelId();
        //奖励
        List<List<Long>> rewardArray = new ArrayList<>();
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.ConquerTaskBaseScript);
        //是不是周常任务
        boolean isWeek = subType == 0;

        int costNum = 0;
        while (randomCount <= Global.GuildTaskMax.get(subType).get(1)) {
            Manager.taskManager.deal().mergeArrayList(oneKeyFinishNeedGold, nowFinishNeedGold);
            if (!Manager.backpackManager.manager().canDeleteItemNumList(player, oneKeyFinishNeedGold)) {
                Manager.taskManager.deal().removeLastAdd(oneKeyFinishNeedGold, nowFinishNeedGold);
                if (!Manager.currencyManager.manager().canDecBindGoldOrGold(player, costNum + Global.Daily_task_cost)) {
                    MessageUtils.notify_player(player, Notify.ERROR, MessageString.ItemNotEnough);
                    break;
                } else {
                    costNum += Global.Daily_task_cost;
                }
            }
            if (isWeek && randomCount % 10 == 0) {
                List<List<Long>> finalRewards = new ArrayList<>();
                for (ReadArray<Long> item : Global.GuildTaskRingReward.getValuees()) {
                    if (item.size() < 3) {
                        log.info("Global.GuildTaskRingReward配置有误！！");
                        continue;
                    }
                    if (item.get(0) != subType) {
                        continue;
                    }
                    List<Long> list = new ArrayList<>();
                    list.add(item.get(1));
                    list.add(item.get(2));
                    finalRewards.add(list);
                }
                Manager.taskManager.deal().addRewardsAndHeapUp(rewardArray, finalRewards);
            }
            Manager.taskManager.deal().addRewardsAndHeapUp(rewardArray, bean.getRewards());
            player.getLastConquerTaskId().put(subType, randomTaskId);
            randomTaskId = ((ILevelTask) is).getCanReceiveTask(player, subType, false);
            bean = CfgManager.getCfg_Task_conquer_Container().getValueByKey(randomTaskId);
            if (bean == null) {
                log.info(TaskHelp.getPlayerInfo(player) + "帮会日常、周常任务配置不存在！" + randomTaskId);
                break;
            }
            nowFinishNeedGold = bean.getOver_currency().getValue();
            ++randomCount;
        }

        if (randomCount <= Global.GuildTaskMax.get(subType).get(1)) {
            return;
        }
        //扣除金币
        ReadArray<Integer>[] newa = Manager.taskManager.deal().buildEs(oneKeyFinishNeedGold);
        ReadIntegerArrayEs fianlCost = new ReadIntegerArrayEs(newa);
        Manager.backpackManager.manager().removeItemOrCurrencies(player, fianlCost, nowTask.getModelId(), ItemChangeReason.GuildTaskOneKeyDec);
        Manager.currencyManager.manager().decBindGoldOrGold(player, costNum, ItemChangeReason.GuildTaskOneKeyDec, nowTask.getModelId());

        if (randomCount > conquerTaskCount) {
            player.getCurConquerTasks().put(subType, new ConquerTask());
            //刷新任务已经做了多少次
            player.getConquerTaskCount().put(subType, randomCount);
            if (isWeek) {
                Manager.dailyActiveManager.deal().addDailyProgress(player, DailyActiveDefine.ZP_WEEK, randomCount - conquerTaskCount);
                Manager.countManager.addCount(player, BaseCountType.Event_TIMES, FunctionVariable.GuildZhouChangNum, Count.RefreshType.CountType_Forever, randomCount - conquerTaskCount);
                Manager.controlManager.deal().operate(player, FunctionVariable.GuildZhouChangNum, randomCount - conquerTaskCount);
            } else {
                Manager.dailyActiveManager.deal().addDailyProgress(player, DailyActiveDefine.ZP_DAILY, randomCount - conquerTaskCount);
                Manager.countManager.addCount(player, BaseCountType.Event_TIMES, FunctionVariable.GuildRiChangNum, Count.RefreshType.CountType_Forever, randomCount - conquerTaskCount);
                Manager.controlManager.deal().operate(player, FunctionVariable.GuildRiChangNum, randomCount - conquerTaskCount);
            }
            //发消息通知客户端当前任务完成
            taskMessage.ResTaskFinish.Builder builder = taskMessage.ResTaskFinish.newBuilder();
            builder.setCurrentCount(conquerTaskCount);
            builder.setType(Task.GUILD_TASK);
            builder.setFinshType(1);
            builder.setModelId(nowTask.getModelId());
            builder.setState(TaskConst.SUCCESS);
            builder.setSubmitResult(true);
            MessageUtils.send_to_player(player, taskMessage.ResTaskFinish.MsgID.eMsgID_VALUE, builder.build().toByteArray());

            if (!isWeek) {
                List<List<Long>> finalRewards = new ArrayList<>();
                for (ReadArray<Long> item : Global.GuildTaskRingReward.getValuees()) {
                    if (item.size() < 3) {
                        log.info("Global.CONQUER_TASK_REWARD配置有误！！");
                        continue;
                    }
                    if (item.get(0) != subType) {
                        continue;
                    }
                    List<Long> list = new ArrayList<>();
                    list.add(item.get(1));
                    list.add(item.get(2));
                    finalRewards.add(list);
                }
                Manager.taskManager.deal().addRewardsAndHeapUp(rewardArray, finalRewards);
            }
        }
        Manager.biManager.getScript().biTask(player, nowTask.getModelId(), Task.GUILD_TASK, TaskConst.BI_FINISH, bean.getTask_name(), subType, bean.getTask_grade());
        //发送奖励
        Manager.taskManager.deal().sendOneKeyReward(player, rewardArray, nowTask.getModelId(), Task.GUILD_TASK, getReason(subType));
    }

    private int getReason(int subType) {
        switch (subType) {
            case 0:
                return ItemChangeReason.GuildTaskGet;
            case 1:
                return ItemChangeReason.GuildTaskGet1;
            case 2:
                return ItemChangeReason.GuildTaskGet2;
        }
        return ItemChangeReason.TaskRewardsGet;
    }


    private int getReasonDec(int subType) {
        switch (subType) {
            case 0:
                return ItemChangeReason.GuildTaskDec;
            case 1:
                return ItemChangeReason.GuildTaskDec1;
            case 2:
                return ItemChangeReason.GuildTaskDec2;
        }
        return ItemChangeReason.TaskRewardsDec;
    }

    @Override
    public boolean conquerTaskOneKeyFinish(Player player, taskMessage.ReqOneKeyOverTask mess) {
        int taskModelId = mess.getTaskModelId();
        int subType = mess.getSubType();

        ConquerTask nowTask = player.getCurConquerTasks().get(subType);
        //当前没有任务
        if (nowTask.getModelId() == 0) {
            log.info(TaskHelp.getLog(player, Task.GUILD_TASK, TaskHelp.NoTask));
            return false;
        }
        Cfg_Task_conquer_Bean bean = CfgManager.getCfg_Task_conquer_Container().getValueByKey(nowTask.getModelId());
        if (bean == null) {
            log.error(TaskHelp.configError(Task.GUILD_TASK, nowTask.getModelId(), TaskHelp.ConfigNoTask));
            return false;
        }

        if (taskModelId == -1 && bean.getTask_type() != Task.ACTION_TYPE_NPC_TALK) {
            log.error("帮会任务不支持当前是不是对话任务时传递不正确的参数");
            return false;
        }

        if (nowTask.isFinish()) {
            log.error(TaskHelp.getLog(player, Task.GUILD_TASK, TaskHelp.HaveFinish));
            return false;
        }

        Integer[] overTaskGold = bean.getOver_currency().getValue();
        if (overTaskGold == null) {
            log.error(TaskHelp.configError(Task.GUILD_TASK, nowTask.getModelId(), " over_currency no config!"));
            return false;
        }

        if (overTaskGold.length != 2) {
            log.error(TaskHelp.configError(Task.GUILD_TASK, nowTask.getModelId(), " over_currency config error!"));
            return false;
        }
        int modelId = overTaskGold[0];
        int num = overTaskGold[1];
        if (!Manager.backpackManager.manager().removeItemOrCurrency(player, modelId, num, nowTask.getId(), getReasonDec(subType))) {
            if (!Manager.currencyManager.manager().decBindGoldOrGold(player, Global.Daily_task_cost, getReason(subType), IDConfigUtil.getLogId())) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.ItemNotEnough);
                return false;
            }
        }

        nowTask.setFinish(true);
        nowTask.setFinishType(Task.FINISH_TYPE_SUPPER_FINISH);
        nowTask.setCurNum(nowTask.targetNum());
        nowTask.noResumeFinishTask(player, TaskConst.DEFAULT_REWARD);
        Manager.biManager.getScript().biTask(player, nowTask.getModelId(), Task.GUILD_TASK, TaskConst.BI_FINISH, bean.getTask_name(), subType, bean.getTask_grade());
        Manager.taskManager.deal().acceptTask(player, Task.GUILD_TASK, 0, subType, false);
        return true;
    }

    @Override
    public Object buildTaskInfo(Player player, Task task) {
        taskMessage.conquerTaskInfo.Builder msg = taskMessage.conquerTaskInfo.newBuilder();

        msg.setModelId(task.getModelId());
        msg.setCount(player.getConquerTaskCount().get(task.getSubType()));
        msg.setMaxCount(Global.GuildTaskMax.get(task.getSubType()).get(1));

        List<Integer> targetModels = task.targetModels();
        for (Integer key : targetModels) {
            msg.setMonsters(buildTaskAttribute(player, task, key));
        }
        if (!msg.hasMonsters()) {
            msg.setMonsters(Manager.taskManager.deal().buildNullTaskAttribute());
        }
        msg.setIsReceive(task.isIsReceive());
        return msg.build();
    }

    private taskMessage.taskAttribute.Builder buildTaskAttribute(Player player, Task task, int model) {
        taskMessage.taskAttribute.Builder sb = taskMessage.taskAttribute.newBuilder();
        sb.setModel(model);
        sb.setNum(task.getCurTaskProgress(player));
        sb.setMapId(task.getTargetMap());
        Manager.taskManager.deal().buildTaskAttributeSame(task, sb);
        return sb;
    }

    @Override
    public HashMap<TaskCondition, List<Integer>> loadLevelTask(int type) {
        Cfg_Task_conquer_Bean[] conquerBeans = CfgManager.getCfg_Task_conquer_Container().getValuees();
        HashMap<TaskCondition, List<Integer>> temp = new HashMap<>();
        for (Cfg_Task_conquer_Bean conquerBean : conquerBeans) {
            if (conquerBean.getRecieve() == 1) {
                continue;
            }
            //这儿的100表示的是配置表中根本就没有职业区分，
            TaskCondition condition = new TaskCondition(100, conquerBean.getLevel_min(), conquerBean.getLevel_max(), conquerBean.getConquer_subtype());
            List<Integer> tempIds;
            if (temp.containsKey(condition)) {
                tempIds = temp.get(condition);
            } else {
                tempIds = new ArrayList<>();
                temp.put(condition, tempIds);
            }
            tempIds.add(conquerBean.getId());
        }
        return temp;
    }

    @Override
    public ReadLongArrayEs getRewardArray(Player player, Task task, int rate) {
        if (task == null || player == null) {
            log.error("获取帮会日常、周常任务奖励列表时数据错误！");
            return null;
        }
        Cfg_Task_conquer_Bean bean = CfgManager.getCfg_Task_conquer_Container().getValueByKey(task.getModelId());
        if (bean == null) {
            log.error("帮会日常、周常任务配置表中没有此任务(" + task.getModelId() + ")");
            return null;
        }

        if (bean.getRewards().isEmpty()) {
            log.error("帮会任务配置表中此任务(" + task.getModelId() + ")没有配置奖励信息");
        }
        List<List<Long>> base = new ArrayList<>();
        for (ReadArray<Long> temp : bean.getRewards().getValuees()) {
            List<Long> ail = new ArrayList<>();
            ail.add(temp.get(0));
            ail.add(temp.get(1) * rate);
            base.add(ail);
        }
        return Utils.toReadLongArrayEsByList(base);
    }

    @Override
    public int getCanReceiveTask(Player player, int subType, boolean isFinal) {
        List<Integer> tempIds = getConquerTasks(player, subType);
        List<Integer> taskIds = new ArrayList<>();
        for (Integer integer : tempIds) {
            if ((integer > 100) && !isFinal) {
                taskIds.add(integer);
            } else if ((integer < 100) && isFinal) {
                taskIds.add(integer);
            }
        }
        if (taskIds.isEmpty()) {
            return -1;
        }
        taskIds.remove(player.getLastConquerTaskId().get(subType));
        return taskIds.get(RandomUtils.random(taskIds.size()));
    }

    @Override
    public boolean checkTaskIsFinish(Player player, int taskId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void buildResTaskFinish(Player player, int taskId, int state, int finishPer, int subType) {
        taskMessage.ResTaskFinish.Builder builder = taskMessage.ResTaskFinish.newBuilder();
        builder.setCurrentCount(player.getConquerTaskCount().get(subType) == null ? 0 + 1 : player.getConquerTaskCount().get(subType) + 1);
        builder.setType(Task.GUILD_TASK);
        builder.setFinshType(0);
        builder.setModelId(taskId);
        builder.setState(state);
        if (state == TaskConst.SUCCESS) {
            builder.setSubmitResult(true);
        } else {
            builder.setSubmitResult(false);
        }
        MessageUtils.send_to_player(player, taskMessage.ResTaskFinish.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    private List<Integer> getConquerTasks(Player player, int subType) {
        List<Integer> tempIds = new ArrayList<>();
        java.util.Map<TaskCondition, List<Integer>> temp = Manager.taskManager.getCanReceiveTasks().get(Task.GUILD_TASK);
        if (temp == null || temp.isEmpty()) {
            return tempIds;
        }
        for (java.util.Map.Entry<TaskCondition, List<Integer>> entrySet : temp.entrySet()) {
            if (entrySet.getKey().subTypeTaskCompare(player, subType)) {
                tempIds.addAll(entrySet.getValue());
            }
        }
        return tempIds;
    }

    /**
     * 检查是不是所有类型的帮会日常、周常都有记录
     *
     * @param curConquerTasks
     */
    private void checkConquerTask(ConcurrentHashMap<Integer, ConquerTask> curConquerTasks) {
        for (Cfg_Task_conquer_Bean bean : CfgManager.getCfg_Task_conquer_Container().getValuees()) {
            if (curConquerTasks.containsKey(bean.getConquer_subtype())) {
                continue;
            }
            curConquerTasks.put(bean.getConquer_subtype(), new ConquerTask());
        }
    }

    /**
     * 获取帮会日常、周常的前置任务
     */
    private int getConquerPreTaskId(Player player, int subType) {
        int taskId = -1;
        for (Cfg_Task_conquer_Bean bean : CfgManager.getCfg_Task_conquer_Container().getValuees()) {
            if (bean.getRecieve() == 0) {
                continue;
            }
            if ((bean.getProfessional() >= 0 && bean.getProfessional() < 100) && (bean.getProfessional() != player.getCareer())) {
                continue;
            }
            if ((bean.getLevel_max() < player.getLevel()) || (player.getLevel() < bean.getLevel_min())) {
                continue;
            }
            if (bean.getConquer_subtype() != subType) {
                continue;
            }
            taskId = bean.getId();
            break;
        }
        return taskId;
    }

    /**
     * 根据类型判断是不是规定时间内
     *
     * @param type
     * @param receiverTime
     * @return
     */
    private boolean isSameDayOrWeek(int type, long receiverTime) {
        //策划强制改为0点刷新
       // long zeroOffset = Global.Daily_times_reset_time * GlobalType.MILLIS_PER_HOUR;
        boolean isSame = false;
        //周常
        if (type == 0 && TimeUtils.isSameWeek(receiverTime , TimeUtils.Time() )) {
            isSame = true;
        }
        //日常
        if (type == 1 && TimeUtils.isSameDay(receiverTime , TimeUtils.Time() )) {
            isSame = true;
        }
        //仙盟任务
        if (type == TYPE_GUILD && TimeUtils.isSameDay(receiverTime , TimeUtils.Time() )) {
            isSame = true;
        }
        return isSame;
    }

    @Override
    public void noticeDeleteTask(Player player, int taskType, int modelId) {
        taskMessage.ResTaskDelete.Builder builder = taskMessage.ResTaskDelete.newBuilder();
        builder.setType(taskType);
        builder.setModelId(modelId);
        MessageUtils.send_to_player(player, taskMessage.ResTaskDelete.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    @Override
    public boolean nearNpcOrStatue(Player player, int npcId, int statueId) {
        return nearNpc(player, npcId) || nearStatue(player, statueId);
    }

    /**
     * 检查玩家是否在某个雕像附近
     *
     * @param statueId
     * @return
     */
    private boolean nearStatue(Player player, int statueId) {
        Cfg_Statue_model_Bean statueBean = CfgManager.getCfg_Statue_model_Container().getValueByKey(statueId);
        if (statueBean == null) {
            log.error("策划的雕像配置估计有点问题，雕像id：" + statueId);
            return false;
        }
        Position position = MapManager.getPos(statueBean.getX(), statueBean.getY());
        double distance = Utils.getDistance(player.getCurGps().getPos(), position);
        if (distance > 10) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.TASK_TO_NPC);
            return false;
        }
        return true;
    }

    /**
     * 检查玩家是否在某个NPC附近
     *
     * @param npcId
     * @return
     */
    private boolean nearNpc(Player player, int npcId) {
        if (npcId < 1) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.TASK_NPC_NOTHVAE);
            return false;
        }
        MapObject map = Manager.mapManager.getMap(player.gainMapId());
        Npc npc = map.getNpcs().get((long) npcId);
        double distance = 100D;
        if (npc != null) {
            distance = Utils.getDistance(npc.gainCurPos(), player.gainCurPos());
        }
        if (distance > 10) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.TASK_TO_NPC);
            return false;
        }
        return true;
    }

    @Override
    public void writeAcceptTaskLog(Player player, Task task) {
        ConquerTaskAcceptLog clog = new ConquerTaskAcceptLog();
        try {
            Manager.taskManager.deal().fillTaskLogBaseInfo(player, clog, task);
            clog.setTaskCount(player.getConquerTaskCount().get(task.getSubType()));
            LogService.getInstance().execute(clog);
        } catch (Exception e) {
            log.error(e, e);
        }
    }

    @Override
    public void writeFinishTaskLog(Player player, Task task) {
        ConquerTaskFinishLog clog = new ConquerTaskFinishLog();
        ConquerTask conquerTask = (ConquerTask) task;
        try {
            Manager.taskManager.deal().fillTaskLogBaseInfo(player, clog, task);
            clog.setTaskCount(player.getConquerTaskCount().get(task.getSubType()));
            clog.setFinishType(conquerTask.getFinishType());
            clog.setRewardRate(conquerTask.getTaskRewardPer());
            LogService.getInstance().execute(clog);
        } catch (Exception e) {
            log.error(e, e);
        }
    }

    @Override
    public void updateTaskProgress(Player player, Task task) {
    }

    @Override
    public void onReqRefreshGuildTaskPool(Player player, taskMessage.ReqRefreshGuildTaskPool messInfo) {
        boolean costGold = messInfo.getUseGold();
        if (!player.isHaveGuild()) {
            log.error(TaskHelp.getPlayerInfo(player) + "还没有帮会！");
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.Guildtaskhint1);
            return;
        }

        int cost = 0;
        //检查免费刷新次数
        if (player.getGuildTaskPool().getRefreshCount() >= Global.Guild_task_refresh_free) {
            if (!costGold) {
//                MessageUtils.notify_player(player, Notify.ERROR, MessageString.Not_Enough_Gold);//免费次数不足
                return;
            }
            //刷新消耗元宝配置
            int costCount = player.getGuildTaskPool().getRefreshCount() - Global.Guild_task_refresh_free + 1;
            for (ReadArray<Integer> rs : Global.Guild_task_refresh_cost.getValuees()) {
                if (costCount >= rs.get(0)) {
                    cost = rs.get(1);
                }
            }

            if(!Manager.currencyManager.manager().canRemoveGold(player, cost)){
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.Not_Enough_Gold);//元宝不足
                return;
            }
        }

        if (costGold) {//扣元宝
            if(!Manager.currencyManager.manager().decGold(player, cost, ItemChangeReason.GuildTaskRefreshDec, IDConfigUtil.getLogId())){
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.Not_Enough_Gold);//元宝扣除失败
                return;
            }
        }
        player.getGuildTaskPool().setRefreshCount(player.getGuildTaskPool().getRefreshCount() + 1);
        refreshGuildTaskPool(player);

        //手动请求刷新的时候，增加替换低等级任务为当前等级任务的处理
        checkGuildTaskLevel(player);

        MessageUtils.notify_player(player, Notify.ERROR, MessageString.Guild_task_refresh);
        sendResGuildTaskPool(player);
    }

    private void checkGuildTaskLevel(Player player) {
        int curTaskId = 0;
        ConquerTask curTask = player.getCurConquerTasks().get(TYPE_GUILD);
        if(curTask != null){
            curTaskId = curTask.getModelId();
        }

        List<Integer> checkTaskList = new ArrayList<>(player.getGuildTaskPool().getTaskIds());
        for (int i = 0; i < checkTaskList.size(); i++) {
            int taskId = checkTaskList.get(i);
            Cfg_Task_conquer_Bean bean = CfgManager.getCfg_Task_conquer_Container().getValueByKey(taskId);
            if (bean == null) {
                log.info(TaskHelp.configError(Task.GUILD_TASK, taskId, TaskHelp.ConfigNoTask));
                continue;
            }

            //只检查A级以上的任务
            if(bean.getTask_grade()<3){
                continue;
            }

            //排除已接任务
            if(curTaskId>0&&bean.getId() == curTaskId){
                continue;
            }

            if(player.getLevel()<=bean.getLevel_max()){
                continue;
            }

            int replaceTaskId = getSameGradeTask(player, bean);
            if(replaceTaskId>0){
                player.getGuildTaskPool().getTaskIds().set(i, replaceTaskId);
            }
        }
    }

    private int getSameGradeTask(Player player, Cfg_Task_conquer_Bean bean){
        for (Cfg_Task_conquer_Bean sBean:CfgManager.getCfg_Task_conquer_Container().getValuees()) {
            if(sBean.getTask_grade()<3){
                continue;
            }
            if(bean.getTask_grade() != sBean.getTask_grade()){
                continue;
            }
            if(!(sBean.getLevel_min()<player.getLevel()&&player.getLevel()<=sBean.getLevel_max())){
                continue;
            }
            //检查当前任务池是否存在该任务
            if(player.getGuildTaskPool().getTaskIds().contains(sBean.getId())){
                continue;
            }
            return sBean.getId();
        }
        return 0;
    }

    @Override
    public taskMessage.conquerTaskInfo.Builder buildTaskMessage(Player player, int taskId) {
        ConquerTask task = new ConquerTask();
        taskMessage.conquerTaskInfo.Builder msg = taskMessage.conquerTaskInfo.newBuilder();
        msg.setModelId(taskId);
        msg.setMaxCount(Global.GuildTaskMax.get(TYPE_GUILD).get(1));

        Cfg_Task_conquer_Bean bean = CfgManager.getCfg_Task_conquer_Container().getValueByKey(taskId);
        if (bean == null) {
            log.info(TaskHelp.configError(Task.GUILD_TASK, taskId, TaskHelp.ConfigNoTask));
            return msg;
        }

        //客户端需要初始化数据
        task.setTargetType(bean.getTask_type());
        task.setOwnerId(player.getId());
        task.setModelId(bean.getId());
        task.setFinish(false);
        task.setTaskRewardPer(1);
        task.setTargetMap(bean.getGoal_map());
        Manager.taskManager.deal().fullTaskOptionParam(task, bean.getGoal_npc(), bean.getTask_x_z());

        msg.setIsReceive(false);
        msg.setCount(0);
        msg.setMonsters(buildTaskAttribute(player, task, getTargetModelId(bean)));

        if (!msg.hasMonsters()) {
            msg.setMonsters(Manager.taskManager.deal().buildNullTaskAttribute());
        }
        return msg;
    }

    private int getTargetModelId(Cfg_Task_conquer_Bean bean) {
        return Manager.taskManager.deal().getTargetModelByType(bean.getTask_type(), bean.getGoal_npc());
    }

    private int getCurTaskIndex(Player player) {
        int index = -1;
        ConquerTask task = player.getCurConquerTasks().get(TYPE_GUILD);
        if (task == null || task.getModelId() <= 0) {
            return index;
        }
        return player.getGuildTaskPool().getTaskIds().indexOf(task.getModelId());
    }

    @Override
    public void refreshGuildTaskPool(Player player) {
        int randomCount = GuildMaxNum;
        int index = getCurTaskIndex(player);
        //是否排除进行的任务
        boolean exCurTask = false;
        //排除A和S级任务
        List<Integer> remainIndexs = getRemainIndex(player);
        for (Integer i : remainIndexs) {
            if (i == index) {
                exCurTask = true;
            }
            --randomCount;
        }
        if (index >= 0 && !exCurTask) {//排除进行的任务
            --randomCount;
            remainIndexs.add(index);
        }
//        log.info("============oldTaskIds:"+player.getGuildTaskPool().getTaskIds());
//        log.info("============remainIndexs:"+remainIndexs);
        //排除的任务模型ID
        List<Integer> exTaskIds = getTaskIds(player, remainIndexs);
        //随机品质,只会有一个S级
        List<Integer> qualityList = new ArrayList<>();
        boolean hasS = false;
        for (int i = 0; i < randomCount; i++) {
            int quality = randomQuality();
            if (hasS && quality == 4) {
//                log.info("===============已经有S级，随机一个");
                quality = RandomUtils.random(1, 3);
            }
            if (quality == 4) {
                hasS = true;
            }
            qualityList.add(quality);
        }
//        log.info("============qualityList:"+qualityList);

        if (!hasS) {//没有随机到S级时则随机算上A级
            List<Integer> aIndexs = getIndexByQuality(player, 3, index);
            for (int i = 0; i < aIndexs.size(); i++) {
                int quality = randomQuality();
                if (quality == 4) {//随机到S级，优先替换掉B或C，如果没有则替换A级
                    int aIndex = aIndexs.get(i);
                    int c = qualityList.indexOf(1);
                    int b = qualityList.indexOf(2);
                    if (c >= 0) {
                        qualityList.set(c, quality);
                    } else if (b >= 0) {
                        qualityList.set(b, quality);
                    } else {
                        remainIndexs.remove((Integer) aIndex);
                        exTaskIds.remove((Integer) player.getGuildTaskPool().getTaskIds().get(aIndex));
                        qualityList.add(quality);
                    }
                    hasS = true;
                    break;
                }
            }
//            log.info("============A>>>>qualityList:"+qualityList);
//            log.info("============A>>>>remainIndexs:"+remainIndexs);
        }

        Map<Integer, List<Cfg_Task_conquer_Bean>> pool = getTaskPool(player, qualityList);
        ArrayDeque<Integer> randomTaskIds = new ArrayDeque<>();
        for (Integer quality : qualityList) {
            List<Cfg_Task_conquer_Bean> list = pool.get(quality);
            if (list == null) {
//                log.info("任务表中未找到符合条件的任务，任务品质："+quality);
                continue;
            }
            int randomTaskId = getRandomTaskId(list, exTaskIds);
            if (randomTaskId <= 0) {
                continue;
            }
            randomTaskIds.add(randomTaskId);
            exTaskIds.add(randomTaskId);
        }
        if (randomTaskIds.isEmpty()) {
            return;
        }
//        log.info("============exTaskIds:"+exTaskIds);
//        log.info("============randomTaskIds:"+randomTaskIds);
        if (randomTaskIds.size() < qualityList.size()) {//配置错误导致无法随机导任务
            log.error("任务表中未找到符合条件的任务，请检查配置");
            return;
        }

        int newIndex = player.getGuildTaskPool().getTaskIds().size();
        for (int i = 0; i < GuildMaxNum; i++) {
            if (i >= newIndex) {
                player.getGuildTaskPool().getTaskIds().add(randomTaskIds.pop());
            } else {
                if (!remainIndexs.contains((Integer) i)) {
                    player.getGuildTaskPool().getTaskIds().set(i, randomTaskIds.pop());
                }
            }
        }
//        log.info("============guildTaskIds:"+player.getGuildTaskPool().getTaskIds());
        if (hasS) {
            MessageUtils.notify_guild_Chat(Manager.guildsManager.GetGuildByPlayer(player), MessageString.Guild_task_refresh_text, player.getName());
        }
        writeRefreshTaskPoolLog(player, 0);
    }

    private List<Integer> getIndexByQuality(Player player, int quality, int index) {
        List<Integer> indexs = new ArrayList<>();
        for (int i = 0; i < player.getGuildTaskPool().getTaskIds().size(); i++) {
            Cfg_Task_conquer_Bean bean = CfgManager.getCfg_Task_conquer_Container().getValueByKey(player.getGuildTaskPool().getTaskIds().get(i));
            if (bean == null) {
                continue;
            }
            if (index >= 0 && index == i) {//排除进行中的任务
                continue;
            }
            if (bean.getTask_grade() == quality) {
                indexs.add(i);
            }
        }
        return indexs;
    }

    /**
     * 获取保留(A级和S级)的下标
     *
     * @param player
     * @return
     */
    private List<Integer> getRemainIndex(Player player) {
        List<Integer> remainIndexs = new ArrayList<>();
        for (int i = 0; i < player.getGuildTaskPool().getTaskIds().size(); i++) {
            Cfg_Task_conquer_Bean bean = CfgManager.getCfg_Task_conquer_Container().getValueByKey(player.getGuildTaskPool().getTaskIds().get(i));
            if (bean == null) {
                continue;
            }
            if (bean.getTask_grade() == 3 || bean.getTask_grade() == 4) {
                remainIndexs.add(i);
            }
        }
        return remainIndexs;
    }

    private int randomQuality() {
        Integer weightSum = 0;
        for (ReadArray<Integer> rs : Global.Guild_task_quality_chance.getValuees()) {
            weightSum += rs.get(1);
        }
        if (weightSum <= 0) {
//            log.info("Error: weightSum=" + weightSum.toString());
            return RandomUtils.random(1, 4);
        }
        int quality = 0;
        Integer n = new Random().nextInt(weightSum); // n in [0, weightSum)
        Integer m = 0;
        for (ReadArray<Integer> rs : Global.Guild_task_quality_chance.getValuees()) {
            if (m <= n && n < m + rs.get(1)) {
                quality = rs.get(0);
//                log.info("This Random taskId is " + taskId);
                break;
            }
            m += rs.get(1);
        }
        return quality;
    }

    private int getChance(int num, int max) {
        int sum = 0;
        for (int i = 0; i < num; i++) {
            sum += Global.Guild_task_quality_chance.get(i).get(1);
        }
        int result = num == 0 ? 0 : sum / (max - num);
//        log.info("===============增加的总概率:"+sum+",增加的概率:"+result);
        return result;
    }

    private Map<Integer, List<Cfg_Task_conquer_Bean>> getTaskPool(Player player, List<Integer> qualityList) {
        Map<Integer, List<Cfg_Task_conquer_Bean>> result = new HashMap<>();
        for (Cfg_Task_conquer_Bean bean : CfgManager.getCfg_Task_conquer_Container().getValuees()) {
            if (bean.getConquer_subtype() != TYPE_GUILD) {
                continue;
            }
            if (bean.getRecieve() == 0) {
                continue;
            }
            if ((bean.getProfessional() >= 0 && bean.getProfessional() < 100) && (bean.getProfessional() != player.getCareer())) {
                continue;
            }
            if ((bean.getLevel_max() < player.getLevel()) || (player.getLevel() < bean.getLevel_min())) {
                continue;
            }
            if (!qualityList.contains((Integer) bean.getTask_grade())) {
                continue;
            }

            if (result.containsKey(bean.getTask_grade())) {
                result.get(bean.getTask_grade()).add(bean);
            } else {
                List<Cfg_Task_conquer_Bean> list = new ArrayList<>();
                list.add(bean);
                result.put(bean.getTask_grade(), list);
            }
        }
        return result;
    }

    private int getRandomTaskId(List<Cfg_Task_conquer_Bean> list, List<Integer> taskIds) {
        Integer weightSum = 0;
        int taskId = 0;
        for (Cfg_Task_conquer_Bean bean : list) {
            if (taskIds.contains(bean.getId())) {
                continue;
            }
            weightSum += bean.getTask_weight();
        }

        if (weightSum <= 0) {
//            log.info("Error: weightSum=" + weightSum.toString());
            return 0;
        }
        Integer n = new Random().nextInt(weightSum); // n in [0, weightSum)
        Integer m = 0;
        for (Cfg_Task_conquer_Bean bean : list) {
            if (taskIds.contains(bean.getId())) {
                continue;
            }
            if (m <= n && n < m + bean.getTask_weight()) {
                taskId = bean.getId();
//                log.info("This Random taskId is " + taskId);
                break;
            }
            m += bean.getTask_weight();
        }
        return taskId;
    }

    private List<Integer> getTaskIds(Player player, List<Integer> remainIndexs) {
        List<Integer> taskIds = new ArrayList<>();
        for (Integer index : remainIndexs) {
            taskIds.add(player.getGuildTaskPool().getTaskIds().get(index));
            ;
        }
        return taskIds;
    }

    @Override
    public void online(Player player) {
        if (!player.isHaveGuild()) {
            return;
        }
        sendResGuildTaskPool(player);
    }

    @Override
    public void zeroClockDeal(Player player) {
        if (!player.isHaveGuild()) {
            return;
        }
        Manager.taskManager.guild().refreshGuildTaskPool(player);
        sendResGuildTaskPool(player);
        log.info("每日到点仙盟任务刷新");
    }

    @Override
    public void sendResGuildTaskPool(Player player) {
        taskMessage.ResGuildTaskPool.Builder builder = taskMessage.ResGuildTaskPool.newBuilder();
        ConquerTask task = player.getCurConquerTasks().get(TYPE_GUILD);
        boolean isReceive = false;
        if (task != null && task.getModelId() > 0) {
            isReceive = true;
        }
        for (Integer taskId : player.getGuildTaskPool().getTaskIds()) {
            if (isReceive && task.getModelId() == taskId) {//客户端要求排除已接任务
                continue;
            }
            builder.addTaskList(buildTaskMessage(player, taskId));
        }
        builder.setRefreshCount(player.getGuildTaskPool().getRefreshCount());
        builder.setReceiveCount(player.getConquerTaskCount().get(TYPE_GUILD) == null ? 0 : player.getConquerTaskCount().get(TYPE_GUILD));
//        log.info("===========ResGuildTaskPool:"+builder.build().toString());
        MessageUtils.send_to_player(player, taskMessage.ResGuildTaskPool.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    @Override
    public void onReqGiveUpTask(Player player, taskMessage.ReqGiveUpTask messInfo) {
        if (!player.isHaveGuild()) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.Guildtaskhint1);
            return;
        }
        ConquerTask task = player.getCurConquerTasks().get(TYPE_GUILD);
        if (task == null || task.getModelId() <= 0) {
            return;
        }
        noticeDeleteTask(player, messInfo.getType(), (int) messInfo.getTaskId());
        writeGiveUpTaskLog(player, task);
        //清理支援信息
        Manager.worldHelpManager.getScript().clearTask(player.getId());
        //放弃任务也要更新日常次数和活跃点
        Manager.dailyActiveManager.deal().addDailyProgress(player, DailyActiveDefine.GUILD_TASK, 1);
        //放弃任务需要刷新任务
        int newTaskId = refreshGuildTask(player, player.getGuildTaskPool().getTaskIds().indexOf(task.getModelId()));

        task = new ConquerTask();
        task.setSubType(TYPE_GUILD);
        player.getCurConquerTasks().put(TYPE_GUILD, task);

        taskMessage.ResConquerTaskChang.Builder builder = taskMessage.ResConquerTaskChang.newBuilder();
        builder.setQuestsTask(buildTaskMessage(player, newTaskId));
        MessageUtils.send_to_player(player, taskMessage.ResConquerTaskChang.MsgID.eMsgID_VALUE, builder.build().toByteArray());

        Cfg_Task_conquer_Bean bean = CfgManager.getCfg_Task_conquer_Container().getValueByKey(newTaskId);
        if (bean != null) {
            if (bean.getTask_grade() == 4) {
                MessageUtils.notify_guild_Chat(Manager.guildsManager.GetGuildByPlayer(player), MessageString.Guild_task_refresh_text, player.getName());
            }
        }

        Cfg_Task_conquer_Bean oldBean = CfgManager.getCfg_Task_conquer_Container().getValueByKey((int)messInfo.getTaskId());
        if(oldBean != null){
            Manager.biManager.getScript().biTask(player, oldBean.getId(), Task.GUILD_TASK, 3, oldBean.getTask_name(), messInfo.getType(), oldBean.getTask_grade());
        }
    }

    private int refreshGuildTask(Player player, int taskIndex) {
        int oldTaskId = player.getGuildTaskPool().getTaskIds().get(taskIndex);
        List<Integer> qualityList = new ArrayList<>();
        qualityList.add(randomQuality());
        Map<Integer, List<Cfg_Task_conquer_Bean>> pool = getTaskPool(player, qualityList);
        List<Integer> taskIds = new ArrayList<>(player.getGuildTaskPool().getTaskIds());
        taskIds.remove(taskIndex);
        int taskId = getRandomTaskId(pool.get(qualityList.get(0)), taskIds);
        player.getGuildTaskPool().getTaskIds().set(taskIndex, taskId);
        writeRefreshTaskPoolLog(player, oldTaskId);
        return taskId;
    }

    private void writeRefreshTaskPoolLog(Player player, int rTaskId) {
        GuildTaskRefreshLog gtlog = new GuildTaskRefreshLog();
        gtlog.setPlayer(player);
        try {
            gtlog.setFreshCount(player.getGuildTaskPool().getRefreshCount());
            gtlog.setrTaskId(rTaskId);
            gtlog.setTaskPool(player.getGuildTaskPool().getTaskIds().toString());
            LogService.getInstance().execute(gtlog);
        } catch (Exception e) {
            log.error(e, e);
        }
    }

    private void writeGiveUpTaskLog(Player player, Task task) {
        GuildTaskGiveUpLog gtlog = new GuildTaskGiveUpLog();
        try {
            Manager.taskManager.deal().fillTaskLogBaseInfo(player, gtlog, task);
            LogService.getInstance().execute(gtlog);
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
