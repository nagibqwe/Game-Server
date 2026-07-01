package common.task;

import com.data.*;
import com.data.bean.Cfg_Recharge_daily_superreward_Bean;
import com.data.bean.Cfg_Statue_model_Bean;
import com.data.bean.Cfg_Task_daily_Bean;
import com.data.struct.ReadArray;
import com.data.struct.ReadIntegerArrayEs;
import com.data.struct.ReadLongArrayEs;
import com.game.backpack.structs.Item;
import com.game.chat.structs.Notify;
import com.game.chum.struct.ChumPrivilege;
import com.game.count.structs.BaseCountType;
import com.game.count.structs.Count;
import com.game.count.structs.VariantType;
import com.game.dailyactive.structs.DailyActiveDefine;
import com.game.manager.Manager;
import com.game.map.manager.MapManager;
import com.game.map.structs.MapObject;
import com.game.npc.structs.Npc;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.structs.GlobalType;
import com.game.task.log.DailyTaskAcceptLog;
import com.game.task.log.DailyTaskFinishLog;
import com.game.task.script.IDailyTask;
import com.game.task.script.ILevelTask;
import com.game.task.script.ITaskScript;
import com.game.task.structs.*;
import com.game.utils.MessageUtils;
import com.game.utils.RandomUtils;
import com.game.utils.Utils;
import com.game.vip.manager.VipManager;
import com.game.welfare.struct.RetrieveType;
import game.core.dblog.LogService;
import game.core.map.Position;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.CommonMessage;
import game.message.taskMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 日常任务的脚本
 *
 * @author admin
 */
public class DailyTaskScript implements ITaskScript, IDailyTask, ILevelTask {

    private static final Logger log = LogManager.getLogger(DailyTaskScript.class);

    @Override
    public int getId() {
        return ScriptEnum.DailyTaskBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void computeTask(Player player, boolean isLogin, boolean isRefresh) {
        checkDailyTask(player.getCurDailyTasks());
        Iterator<Map.Entry<Integer, DailyTask>> dailyTaskIte = player.getCurDailyTasks().entrySet().iterator();
        while (dailyTaskIte.hasNext()) {
            Map.Entry<Integer, DailyTask> dailyTask = dailyTaskIte.next();
            int subType = dailyTask.getKey();
            DailyTask task = dailyTask.getValue();
            //从上个时间节点到现在共做了多少次任务
            int count = player.getDailyTaskCount().get(subType) == null ? 0 : player.getDailyTaskCount().get(subType);
            //上次接取任务的时间
            long receiverTime = player.getDailyTaskTime().get(subType) == null ? 0 : player.getDailyTaskTime().get(subType);
            //策划强制改为0点刷新
            //long zeroOffset = Global.Daily_times_reset_time * GlobalType.MILLIS_PER_HOUR;
            if (!TimeUtils.isSameDay(receiverTime, TimeUtils.Time())) {
                //不同一天
                player.getDailyTaskCount().put(subType, 0);
                player.getDailyTaskTime().put(subType, TimeUtils.Time());
                player.getDailyTaskFinishIds().put(task.getSubType(), new ArrayList<>());
                if (task.getModelId() != 0) {
                    player.getCurDailyTasks().put(subType, new DailyTask());
                    noticeDeleteTask(player, Task.DAILY_TASK, task.getModelId());
                }
                //初始化一个前置任务
                int taskId = getDailyProTaskId(player, subType);
                if (taskId == -1) {
                    acceptTask(player, 0, subType, isLogin, true);
                    continue;
                }
                acceptTask(player, taskId, subType, isLogin, true);
            } else {
                if (task.getModelId() == 0 && count == 0) {
                    int taskId = getDailyProTaskId(player, subType);
                    if (taskId == -1) {
                        acceptTask(player, 0, subType, isLogin, isRefresh);
                        continue;
                    }
                    acceptTask(player, taskId, subType, isLogin, isRefresh);
                } else if (task.getModelId() != 0 && count < (Global.DailyTaskMax.get(subType).get(1) + 1)) {
                    int dailyTaskId = task.getModelId();
                    Cfg_Task_daily_Bean model = CfgManager.getCfg_Task_daily_Container().getValueByKey(dailyTaskId);
                    if (model == null) {
                        player.getCurDailyTasks().put(subType, new DailyTask());
                        acceptTask(player, 0, subType, isLogin, isRefresh);
                    }
                } else if (task.getModelId() == 0 && count < (Global.DailyTaskMax.get(subType).get(1) + 1)) {
                    acceptTask(player, 0, subType, isLogin, isRefresh);
                }
            }
        }
    }

    @Override
    public void acceptTask(Player player, int modelId, int subType, boolean isLogin, boolean isRefresh) {
        //同一天 接取的次数达到上限
        int count = player.getDailyTaskCount().get(subType) == null ? 0 : player.getDailyTaskCount().get(subType);
        if (count >= Global.DailyTaskMax.get(subType).get(1)) {
//            log.error(TaskHelp.getLog(player, Task.DAILY_TASK, TaskHelp.TaskMax));
            return;
        }

        DailyTask task = player.getCurDailyTasks().get(subType);

        if (task.getModelId() != 0) {
            if (task.isIsReceive()) {
//                log.error(TaskHelp.getLog(player, Task.DAILY_TASK, TaskHelp.HaveTask));
                return;
            } else {
                task.setIsReceive(true);
                player.getDailyTaskCount().put(subType, count + 1);
            }
        } else {
            task = new DailyTask();
            task.setSubType(subType);
            if (!initTask(player, task, modelId, isRefresh)) {
//                log.error("在接日常任务的时候，初始化失败！");
                return;
            }
            player.getCurDailyTasks().put(subType, task);
            if (task.isIsReceive()) {
                player.getDailyTaskCount().put(subType, count + 1);
            }
        }

        Cfg_Task_daily_Bean model = CfgManager.getCfg_Task_daily_Container().getValueByKey(task.getModelId());
        Manager.biManager.getScript().biTask(player, task.getModelId(), Task.DAILY_TASK, TaskConst.BI_RECEIVE, model.getTask_name(), task.getSubType(), 0);
//        log.info("接取日常任务，player = " + player.getName() + "; 当前环数 = " + count + "; 任务id = " + task.getModelId() + "; subType = " + subType + "，可以接取但是还没有接取 = " + !task.isIsReceive());
        if (!isLogin) {
            changeTask(player, task, isRefresh);
        }

        Manager.taskManager.deal().taskChangeRoundSync(player, model.getShow_npc(), model.getShow_monster(), model.getShow_gather());

        if (subType == 0) {//法宝任务
            Manager.countManager.addVariant(player, VariantType.FaBaoTaskNum, 1);
            Manager.controlManager.deal().operate(player, FunctionVariable.RealmStifleTaskNum, 1);
        }
        writeAcceptTaskLog(player, task);
    }

    @Override
    public boolean onFinishTask(Player player, int taskModelID, int taskId, int finishPer, boolean isGm, int subType) {
        int rewardPer = TaskConst.DEFAULT_REWARD;
        DailyTask task = (DailyTask) Manager.taskManager.deal().getTaskByModelId(player, Task.DAILY_TASK, taskId);
        if (task == null) {
            log.error(TaskHelp.getLog(player, Task.DAILY_TASK, TaskHelp.NoFind, taskId));
            buildResTaskFinish(player, taskId, TaskConst.PLAYER_NO_THIS_TASK, rewardPer, subType);
            return false;
        }
        //检查其它事件
        Cfg_Task_daily_Bean bean = CfgManager.getCfg_Task_daily_Container().getValueByKey(taskId);
        if (bean == null) {
            log.error(TaskHelp.configError(Task.DAILY_TASK, taskId, TaskHelp.ConfigNoTask));
            return false;
        }

        if (!isGm) {
            //对话任务
            if (bean.getAuto_commit() == 1) {
                if (!task.isFinish() && bean.getTask_type() == Task.ACTION_TYPE_NPC_TALK) {
                    Integer[] targetArray = bean.getGoal_npc().getValue();
                    int npcId = targetArray[0];

                    if (npcId < 1) {
                        MessageUtils.notify_player(player, Notify.ERROR, MessageString.TASK_NPC_NOTHVAE);
                        return false;
                    }

                    //检查NPC的距离
                    MapObject map = Manager.mapManager.getMap(player.gainMapId());
                    Npc npc = map.getNpcs().get((long) npcId);
                    double distance = 100D;
                    if (npc != null) {
                        distance = Utils.getDistance(npc.gainCurPos(), player.gainCurPos());
                    } else {
                        if (bean.getStatue_ID() > 0) {
                            Cfg_Statue_model_Bean statueBean = CfgManager.getCfg_Statue_model_Container().getValueByKey(bean.getStatue_ID());
                            if (statueBean == null) {
                                log.error("策划的雕像配置估计有点问题，雕像id：" + bean.getStatue_ID());
                                return false;
                            }
                            Position position = MapManager.getPos(statueBean.getX(), statueBean.getY());
                            distance = Utils.getDistance(player.getCurGps().getPos(), position);
                        } else {
                            log.error("策划的配置估计有点问题，没有雕像id：" + bean.getId());
                        }
                    }
                    if (distance > 10) {
                        MessageUtils.notify_player(player, Notify.ERROR, MessageString.TASK_TO_NPC);
                        buildResTaskFinish(player, taskId, TaskConst.TOO_FAR_WITH_NPC, rewardPer, subType);
                        return false;
                    }
                }
            }
        }

        if (!task.checkFinish(true, player)) {
            buildResTaskFinish(player, taskId, TaskConst.TASK_NOT_FINISH, rewardPer, subType);
            return false;
        }
        task.setTaskRewardPer(rewardPer);
        //扣除元宝数量
        finishTask(player, task, rewardPer);
        Manager.biManager.getScript().biTask(player, task.getModelId(), Task.DAILY_TASK, TaskConst.BI_FINISH, bean.getTask_name(), task.getSubType(), 0);
//        log.info("完成日常任务，player = " + player.getName() + "; 当前环数 = " + player.getDailyTaskCount().get(subType) + "; 任务id = " + taskId + "; subType = " + subType);
        try {
            Manager.taskManager.deal().acceptTask(player, Task.DAILY_TASK, 0, subType, false);
        } catch (Exception e) {
            log.error(e, e);
        }
        return true;
    }

    @Override
    public void finishTask(Player player, DailyTask task, int finishPer) {
        try {
            buildResTaskFinish(player, task.getModelId(), TaskConst.SUCCESS, finishPer, task.getSubType());


            sendRewards(player, task, finishPer);


            if (player.getDailyTaskFinishIds().get(task.getSubType()) == null) {
                player.getDailyTaskFinishIds().put(task.getSubType(), Arrays.asList(task.getModelId()));
            } else {
                player.getDailyTaskFinishIds().get(task.getSubType()).add(task.getModelId());
            }

            player.getCurDailyTasks().put(task.getSubType(), new DailyTask());

            if (player.getDailyTaskCount().get(task.getSubType()) == Global.DailyTaskMax.get(task.getSubType()).get(1)) {
                Manager.taskManager.deal().action(player, Task.ACTION_TYPE_FUNCTION, BranchType.THECHIEFTASK.getValue(), 1);
            }
//            自动检测奖励废弃
            // dealFinalReward(player, task.getModelId(), task.getSubType());

            Manager.countManager.addVariant(player, VariantType.SHOUXI_TASK_FINISH_NUM, 1);
            if (task.getSubType() == 0) {
                Manager.dailyActiveManager.deal().addDailyProgress(player, DailyActiveDefine.EXP_DAILY, 1);
                Manager.countManager.addVariant(player, VariantType.Daily_ShangJingFunc_Times, 1);
                Manager.controlManager.deal().operate(player, FunctionVariable.Daily_ShangJingFunc_Times, 1);

                Manager.countManager.addCount(player, BaseCountType.Event_TIMES, FunctionVariable.RiChangJingYanNum, Count.RefreshType.CountType_Forever, 1);
                Manager.controlManager.deal().operate(player, FunctionVariable.RiChangJingYanNum, 1);

                Manager.countManager.addVariant(player, VariantType.FaBaoTaskFinishNumWeek, 1);
                Manager.controlManager.deal().operate(player, FunctionVariable.RealmStifleTaskCompNum, 1);

                Manager.retrieveResManager.getScript().count(player, RetrieveType.Treasure);
            } else if (task.getSubType() == 1) {
                Manager.dailyActiveManager.deal().addDailyProgress(player, DailyActiveDefine.MONEY_DAILY, 1);
                Manager.countManager.addCount(player, BaseCountType.Event_TIMES, FunctionVariable.RiChangYinBiNum, Count.RefreshType.CountType_Forever, 1);
                Manager.controlManager.deal().operate(player, FunctionVariable.RiChangYinBiNum, 1);
                Manager.retrieveResManager.getScript().count(player, RetrieveType.Weapon);
            } else if (task.getSubType() == 2) {//活跃点获得和消耗日常

            }
            writeFinishTaskLog(player, task);
            taskFinishAfter(player, task.getModelId(), task.getSubType(), task.getTargetType());
        } catch (Exception e) {
            log.error(e, e);
        }
    }

    //检查日常环的所有任务是否完成
    private void dealFinalReward(Player player, int currentModelId, int subType) {
        if (player.getDailyTaskCount().get(subType) != (int) Global.DailyTaskMax.get(subType).get(1)) {
            return;
        }

        taskMessage.ResDailyTaskFinish.Builder msg = taskMessage.ResDailyTaskFinish.newBuilder();
        msg.setTaskCount(Global.DailyTaskMax.get(subType).get(1));

        //累计任务奖励
        HashMap<Long, List<Long>> rewardMap = new HashMap<>();
        List<Integer> finishTaskIds = player.getDailyTaskFinishIds().get(subType);
        for (int id : finishTaskIds) {
            Cfg_Task_daily_Bean bean = CfgManager.getCfg_Task_daily_Container().getValueByKey(id);
            if (bean == null) {
                log.error("没有找到日常任务的数据 id为:" + id);
                continue;
            }
            if (bean.getDaily_subtype() != subType) {
                continue;
            }
            ReadLongArrayEs arrays = bean.getRewards_5();
            for (ReadArray<Long> array : arrays.getValuees()) {
                if (rewardMap.containsKey(array.get(0))) {
                    List<Long> list = rewardMap.get(array.get(0));
                    list.set(1, list.get(1) + array.get(1));
                    rewardMap.put(array.get(0), list);
                } else {
                    List<Long> list = new ArrayList<>();
                    list.add(array.get(0));
                    list.add(array.get(1));
                    rewardMap.put(array.get(0), list);
                }
            }
        }

        CommonMessage.ShowItemInfo.Builder sii;
        for (List<Long> reward : rewardMap.values()) {
            sii = CommonMessage.ShowItemInfo.newBuilder();
            sii.setModelId(reward.get(0).intValue());
            sii.setCount(reward.get(1));
            msg.addRewards(sii);
        }

        //完成所有日常的额外奖励
        HashMap<Long, List<Long>> extraRewardMap = new HashMap<>();
        for (ReadArray<Integer> item : Global.DailyTaskRingReward.getValuees()) {
            if (item.size() < 3) {
                log.error("Global.DAILY_TASK_REWARD配置出错！！");
                continue;
            }
            if (item.get(0) != subType) {
                continue;
            }

            if (extraRewardMap.containsKey(item.get(1))) {
                List<Long> list = extraRewardMap.get(item.get(1));
                list.set(1, list.get(1) + item.get(2));
                extraRewardMap.put((long) item.get(1), list);
            } else {
                List<Long> list = new ArrayList<>();
                list.add((long) item.get(1));
                list.add((long) item.get(2));
                extraRewardMap.put((long) item.get(1), list);
            }
        }

        for (List<Long> extra : extraRewardMap.values()) {
            sii = CommonMessage.ShowItemInfo.newBuilder();
            sii.setModelId(extra.get(0).intValue());
            sii.setCount(extra.get(1));
            msg.addExtraRewards(sii);
        }

        List<Item> createItems = Item.createItemsWithLongCollection(extraRewardMap.values(), 1);
        if (!Manager.backpackManager.manager().addItems(player, createItems, getReason(subType), currentModelId)) {
            Manager.taskManager.deal().sendRewardByMail(player, createItems, getReason(subType), getReason(subType));
        }

//        log.error("******************ResDailyTaskFinish:"+msg.build());
        MessageUtils.send_to_player(player, taskMessage.ResDailyTaskFinish.MsgID.eMsgID_VALUE, msg.build().toByteArray());

        Manager.taskManager.deal().writeRewardLog(player, currentModelId, Task.DAILY_TASK, createItems, TaskConst.DEFAULT_REWARD);
    }

    @Override
    public void taskFinishAfter(Player player, int currentModelId, int subType, int targetType) {
        switch (subType) {
            case 0:
                // 法定日常
                Manager.chumManager.getScript().helpReward(player, ChumPrivilege.FB_TASK);
                break;
            case 1:
                // 神兵日常
                Manager.chumManager.getScript().helpReward(player, ChumPrivilege.SB_TASK);
                break;
        }
    }

    @Override
    public void changeTask(Player player, Task task, boolean isRefresh) {
        if (!Manager.taskManager.deal().checkFunctionIsOpen(player, Task.DAILY_TASK)) {
            return;
        }
        if (player.isSendTaskInfo()) {
            taskMessage.ResDailyTaskChang.Builder msg = taskMessage.ResDailyTaskChang.newBuilder();
            msg.setDailyTask((taskMessage.dailyTaskInfo) buildTaskInfo(player, task));
            msg.setIsAuto(!isRefresh);
            MessageUtils.send_to_player(player, taskMessage.ResDailyTaskChang.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }
    }

    /**
     * 日常任务的初始化
     *
     * @param player
     * @param dailyTask
     * @return
     */
    public boolean initTask(Player player, DailyTask dailyTask, int modelId, boolean isRefresh) {

        dailyTask.setOwnerId(player.getId());
        if (dailyTask.getSubType() == 2 && isRefresh) {//0点重置活跃任务接取状态
            player.getLastDailyTaskId().remove(2);
        }
        int taskId = getCanReceiveTask(player, dailyTask.getSubType(), false);
        if (taskId < 0) {
//            log.error("没有符合当前条件的日常任务：等级" + player.getLevel() + "职业：" + player.getCareer());
            return false;
        }
        Cfg_Task_daily_Bean model = CfgManager.getCfg_Task_daily_Container().getValueByKey(taskId);
        if (model == null) {
            log.error("Cfg_Task_dailyBean无法找到数据，taskId = " + taskId);
            return false;
        }
        //初始化任务进度
        if (model.getDaily_subtype() == 2) {
            int curProgress = Manager.controlManager.deal().getFuncProgress(player, model.getGoal_npc());
//            log.info("=========初始化时curProgress:" + curProgress);
            dailyTask.setCurNum(curProgress);
        }
        int tempStar = randomStar(model.getId());
        if (tempStar == -1) {
            log.error("随机生成星级异常 taskId：" + model.getId());
        } else {
            dailyTask.setStar(tempStar);
            if (tempStar == 5) {
                dailyTask.setFullStar(true);
            } else {
                dailyTask.setFullStar(false);
            }
        }
        boolean receive = model.getRecieve() == 0;
        if (modelId > 0) {
            receive = false;
        }
        //@todo 赏金任务 第一次 不自动接取
        if (dailyTask.getSubType() == 0) {
            if (player.getDailyTaskCount().get(dailyTask.getSubType()) == 0) {
                receive = false;
            }
        }

        dailyTask.setIsReceive(receive);
        dailyTask.setModelId(model.getId());
        dailyTask.setTargetType(model.getTask_type());
        dailyTask.setTargetMap(model.getGoal_map());
        dailyTask.setNeedLevel(model.getLevel_min());
        dailyTask.setNeedDegree(0);
        dailyTask.setSubType(model.getDaily_subtype());
        player.getLastDailyTaskId().put(dailyTask.getSubType(), taskId);

        Manager.taskManager.deal().fullTaskOptionParam(dailyTask, model.getGoal_npc(), model.getTask_x_z());
        return true;
    }

    public void sendRewards(Player player, DailyTask task, int rewardType) {
        //先扣除需要的物品
        if (task == null) {
            log.error("角色身上找不到请求的分支任务,角色 :" + player.getId() + "任务ID:");
            return;
        }

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

        Manager.taskManager.deal().writeRewardLog(player, task.getModelId(), Task.DAILY_TASK, createItems, rewardType);
    }

    private void buildResTaskFinish(Player player, int taskId, int state, int finishPer, int subType) {
        taskMessage.ResTaskFinish.Builder builder = taskMessage.ResTaskFinish.newBuilder();
        builder.setCurrentCount(player.getDailyTaskCount().get(subType) + 1);
        builder.setType(Task.DAILY_TASK);
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

    private void sendResTaskFinish(Player player, int taskId, int type, int state, int count, int finishType, boolean isSubmitResult) {
        taskMessage.ResTaskFinish.Builder builder = taskMessage.ResTaskFinish.newBuilder();
        builder.setModelId(taskId);
        builder.setType(type);
        builder.setFinshType(finishType);
        builder.setCurrentCount(count);
        builder.setState(state);
        builder.setSubmitResult(isSubmitResult);
        MessageUtils.send_to_player(player, taskMessage.ResTaskFinish.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    @Override
    public void oneKeyFinishAllDailyTask(Player player, int subType, boolean checkCon, int taskCount) {

        if (player.getLevel() < Global.DailyTask_saodang_level) {//快速扫荡需要等级
            return;
        }
        //检查配置表数据是否正常
        final DailyTask nowTask = player.getCurDailyTasks().get(subType);
        if (nowTask.getModelId() == 0) {
            return;
        }

        //接取次数
        int dailyTaskCount = player.getDailyTaskCount().get(subType) == null ? 0 : player.getDailyTaskCount().get(subType);
        //完成次数
        int finishTaskCount = player.getDailyTaskCount().get(subType) == null ? 0 : player.getDailyTaskCount().get(subType)>0?player.getDailyTaskCount().get(subType)-1:player.getDailyTaskCount().get(subType);
        //默认为剩余环数
        if (taskCount == 0) {//完成当前所有任务
            taskCount = Global.DailyTaskMax.get(subType).get(1) - dailyTaskCount;
        }

        //判断当日完成次数 和 选择次数 不能超过最大值
        if (finishTaskCount + taskCount > Global.DailyTaskMax.get(subType).get(1)) {
            return;
        }

        Cfg_Task_daily_Bean bean = CfgManager.getCfg_Task_daily_Container().getValueByKey(nowTask.getModelId());
        if (bean == null) {
            log.error(TaskHelp.getPlayerInfo(player) + "日常任务配置不存在！" + nowTask.getModelId());
            return;
        }
//        if (subType == 0 && !VipManager.isCanFreeFabaoSweep(player)) {
//            log.error(TaskHelp.getPlayerInfo(player) + "法宝日常任务一键完成功能VIP条件未达到！taskId=" + nowTask.getModelId());
//            return;
//        }
//        if (subType == 1 && !VipManager.isCanFreeMagicalSweep(player)) {
//            log.error(TaskHelp.getPlayerInfo(player) + "神兵日常任务一键完成功能VIP条件未达到！taskId=" + nowTask.getModelId());
//            return;
//        }
//        if (player.getLevel() >= Global.VIP_Wipe_Out_Free.get(1)) {//VIP免费扫荡检查
//            checkCon = false;
//        }


        if (checkCon && bean.getOver_currency().size() < 2) {
            log.error(TaskHelp.getPlayerInfo(player) + "日常任务配置消耗错误！" + nowTask.getModelId());
            return;
        }
        //当前金币够扫荡到的位置
        int randomCount = dailyTaskCount;
        //随机日常任务ID
        int randomTaskId = nowTask.getModelId();
        //当前任务之前消耗的金币数量之和，用作扣除
        ArrayList<Integer[]> oneKeyFinishNeedGold = new ArrayList<>();
        //当前任务的消耗的金币数量，与oneKeyFinishNeedGold一起作用，用作判断是否够消耗
        Integer[] nowFinishNeedGold = null;
        nowFinishNeedGold = bean.getOver_currency().getValue();
        //奖励
        List<List<Long>> rewardArray = new ArrayList<>();
        //消耗绑元
        int costNum = 0;
        int maxCount = randomCount + taskCount;
        while (randomCount < maxCount) {
            if (checkCon) {
                Manager.taskManager.deal().mergeArrayList(oneKeyFinishNeedGold, nowFinishNeedGold);
                if (!Manager.backpackManager.manager().canDeleteItemNumList(player, oneKeyFinishNeedGold)) {
                    Manager.taskManager.deal().removeLastAdd(oneKeyFinishNeedGold, nowFinishNeedGold);
                    if (!Manager.currencyManager.manager().canDecBindGoldOrGold(player, costNum + Global.DailyTask_saodang_num)) {
                        MessageUtils.notify_player(player, Notify.ERROR, MessageString.ItemNotEnough);
                        break;
                    } else {
                        costNum += Global.DailyTask_saodang_num;
                    }
                }
            }

            Manager.taskManager.deal().addRewardsAndHeapUp(rewardArray, bean.getRewards_5());
//            System.out.println("id = " + bean.getId() + ", 奖励 = " + bean.getRewards_5() + ", 当前环数 = " + randomCount);
            player.getLastDailyTaskId().put(subType, randomTaskId);
            randomTaskId = getCanReceiveTask(player, subType, false);
            bean = CfgManager.getCfg_Task_daily_Container().getValueByKey(randomTaskId);
            if (bean == null) {
                log.error(TaskHelp.getPlayerInfo(player) + "日常任务配置不存在！" + randomTaskId);
                break;
            }
            nowFinishNeedGold = bean.getOver_currency().getValue();
            ++randomCount;
        }

//        if (randomCount < Global.DailyTaskMax.get(subType).get(1)) {
//            return;
//        }

        if (checkCon) {
            //扣除金币
            ReadArray<Integer>[] newa = Manager.taskManager.deal().buildEs(oneKeyFinishNeedGold);
            ReadIntegerArrayEs fianlCost = new ReadIntegerArrayEs(newa);
            Manager.backpackManager.manager().removeItemOrCurrencies(player, fianlCost, nowTask.getModelId(), ItemChangeReason.DailyTaskOneKeyDec);
            Manager.currencyManager.manager().decBindGoldOrGold(player, costNum, ItemChangeReason.DailyTaskOneKeyDec, nowTask.getModelId());
        }

        int addTimes = randomCount - dailyTaskCount;

        Manager.countManager.addVariant(player, VariantType.SHOUXI_TASK_FINISH_NUM, addTimes);

        if (randomCount > dailyTaskCount) {
            player.getCurDailyTasks().put(subType, new DailyTask());
            //刷新任务已经做了多少次
            player.getDailyTaskCount().put(subType, randomCount);
            if (subType == 0) {
                Manager.dailyActiveManager.deal().addDailyProgress(player, DailyActiveDefine.EXP_DAILY, addTimes);
                Manager.countManager.addVariant(player, VariantType.Daily_ShangJingFunc_Times, addTimes);
                Manager.controlManager.deal().operate(player, FunctionVariable.Daily_ShangJingFunc_Times, addTimes);

                Manager.countManager.addCount(player, BaseCountType.Event_TIMES, FunctionVariable.RiChangJingYanNum, Count.RefreshType.CountType_Forever, addTimes);
                Manager.controlManager.operate(player, FunctionVariable.RiChangJingYanNum, addTimes);

                Manager.countManager.addVariant(player, VariantType.FaBaoTaskFinishNumWeek, addTimes);
                Manager.controlManager.operate(player, FunctionVariable.RealmStifleTaskCompNum, addTimes);

                Manager.retrieveResManager.getScript().count(player, RetrieveType.Treasure, addTimes);
            } else if (subType == 1) {
                Manager.dailyActiveManager.deal().addDailyProgress(player, DailyActiveDefine.MONEY_DAILY, addTimes);
                Manager.countManager.addCount(player, BaseCountType.Event_TIMES, FunctionVariable.RiChangYinBiNum, Count.RefreshType.CountType_Forever, addTimes);
                Manager.controlManager.operate(player, FunctionVariable.RiChangYinBiNum, addTimes);
                Manager.retrieveResManager.getScript().count(player, RetrieveType.Weapon, addTimes);
            }

            //发消息通知客户端当前任务完成
            sendResTaskFinish(player, nowTask.getModelId(), Task.DAILY_TASK, TaskConst.SUCCESS, dailyTaskCount, 1, true);

        }
        Manager.biManager.getScript().biTask(player, nowTask.getModelId(), Task.DAILY_TASK, TaskConst.BI_FINISH, bean.getTask_name(), nowTask.getSubType(), 0);
        //发送奖励
        Manager.taskManager.deal().sendOneKeyReward(player, rewardArray, nowTask.getModelId(), Task.DAILY_TASK, getReason(subType));
    }

    private int getReason(int subType) {
        switch (subType) {
            case 0:
                return ItemChangeReason.DailyTaskGet;
            case 1:
                return ItemChangeReason.DailyTaskGet1;
            case 2:
                return ItemChangeReason.DailyTaskGet2;
        }
        return ItemChangeReason.TaskRewardsGet;
    }

    private int getReasonDec(int subType) {
        switch (subType) {
            case 0:
                return ItemChangeReason.DailyTaskDec;
            case 1:
                return ItemChangeReason.DailyTaskDec1;
            case 2:
                return ItemChangeReason.DailyTaskDec2;
        }
        return ItemChangeReason.TaskRewardsDec;
    }

    @Override
    public boolean dailyTaskOneKeyFinish(Player player, taskMessage.ReqOneKeyOverTask mess) {
        int subType = mess.getSubType();
        DailyTask nowTask = player.getCurDailyTasks().get(subType);
        //当前没有任务
        if (nowTask.getModelId() == 0) {
            log.error(TaskHelp.getLog(player, Task.DAILY_TASK, TaskHelp.NoTask));
            return false;
        }
        Cfg_Task_daily_Bean bean = CfgManager.getCfg_Task_daily_Container().getValueByKey(nowTask.getModelId());
        if (bean == null) {
            log.error(TaskHelp.configError(Task.DAILY_TASK, nowTask.getModelId(), TaskHelp.ConfigNoTask));
            return false;
        }
        int taskModelId = mess.getTaskModelId();
        if (taskModelId == -1 && bean.getTask_type() != Task.ACTION_TYPE_NPC_TALK) {
            log.error("日常任务不支持当前是不是对话任务时传递不正确的参数");
            return false;
        }
        if (nowTask.isFinish()) {
            log.error(TaskHelp.getLog(player, Task.DAILY_TASK, TaskHelp.HaveFinish));
            return false;
        }
        //VIP宝珠激活检查
        if (!player.getVipPearl().canFree()) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.C_Vip_Power_No_Ues_Notice);
            return false;
        }
        if (subType == 0 && !Manager.vipManager.power().isCanFreeFabaoSweep(player)) {
            log.error(TaskHelp.getPlayerInfo(player) + "法宝任务一键完成功能VIP条件未达到！taskId=" + nowTask.getModelId());
            return false;
        }
        if (subType == 1 && !Manager.vipManager.power().isCanFreeMagicalSweep(player)) {
            log.error(TaskHelp.getPlayerInfo(player) + "神兵任务一键完成功能VIP条件未达到！taskId=" + nowTask.getModelId());
            return false;
        }
        if (player.getLevel() < Global.VIP_Wipe_Out_Free.get(1)) {//VIP免费扫荡检查
            Integer[] overTaskGold = bean.getOver_currency().getValue();
            if (overTaskGold == null) {
                log.error("策划的日常任务的over_currency没有配置，任务id为：" + nowTask);
                return false;
            }
            if (overTaskGold.length != 2) {
                log.error("策划的日常任务的over_currency配置出错，任务id为：" + nowTask);
                return false;
            }

            int modelId = overTaskGold[0];
            int num = overTaskGold[1];
            if (!Manager.backpackManager.manager().removeItemOrCurrency(player, modelId, num, nowTask.getId(), getReasonDec(subType))) {
                if (!Manager.currencyManager.manager().decGold(player, Global.Daily_task_cost, ItemChangeReason.DailyTaskDec, IDConfigUtil.getLogId())) {
                    MessageUtils.notify_player(player, Notify.ERROR, MessageString.ItemNotEnough);
                    return false;
                }
            }
        }

        nowTask.setFinish(true);
        nowTask.setFinishType(Task.FINISH_TYPE_SUPPER_FINISH);
        nowTask.setCurNum(nowTask.targetNum());
        nowTask.noResumeFinishTask(player, TaskConst.DEFAULT_REWARD);
        Manager.biManager.getScript().biTask(player, nowTask.getModelId(), Task.DAILY_TASK, TaskConst.BI_FINISH, bean.getTask_name(), nowTask.getSubType(), 0);
        Manager.taskManager.deal().acceptTask(player, Task.DAILY_TASK, 0, subType, false);
        return true;
    }

    @Override
    public Object buildTaskInfo(Player player, Task tt) {
        DailyTask task = (DailyTask) tt;
        taskMessage.dailyTaskInfo.Builder info = taskMessage.dailyTaskInfo.newBuilder();
        info.setModelId(task.getModelId());
        info.setCount(player.getDailyTaskCount().get(task.getSubType()));
        info.setMaxCount(Global.DailyTaskMax.get(task.getSubType()).get(1));

        List<Integer> targetModels = task.targetModels();
        for (Integer key : targetModels) {
            info.setUseItems(buildTaskAttribute(player, task, key));
        }
        //如果检查到没有设置，就设定一个空的TaskAttribute
        if (!info.hasUseItems()) {
            info.setUseItems(Manager.taskManager.deal().buildNullTaskAttribute());
        }
        info.setStar(task.getStar());
        info.setIsfull(task.isFullStar());
        info.setIsReceive(tt.isIsReceive());
        info.setOneKeyState(task.isFinish());


        return info.build();
    }

    private taskMessage.taskAttribute.Builder buildTaskAttribute(Player player, DailyTask task, int model) {
        taskMessage.taskAttribute.Builder sb = taskMessage.taskAttribute.newBuilder();
        sb.setModel(model);
        sb.setNum(task.getCurTaskProgress(player));
        sb.setMapId(task.getTargetMap());
        Manager.taskManager.deal().buildTaskAttributeSame(task, sb);
        return sb;
    }

    @Override
    public HashMap<TaskCondition, List<Integer>> loadLevelTask(int type) {
        Cfg_Task_daily_Bean[] dailyBeans = CfgManager.getCfg_Task_daily_Container().getValuees();
        // 格式为 最低等级_最大等级，当前条件下所属任务实体
        HashMap<TaskCondition, List<Integer>> temp = new HashMap<>();
        for (Cfg_Task_daily_Bean bean : dailyBeans) {
            if (bean.getRecieve() == 1) {
                continue;
            }
            TaskCondition condition = new TaskCondition(bean.getProfessional(), bean.getLevel_min(), bean.getLevel_max(), bean.getDaily_subtype());
            List<Integer> tempIds;
            if (temp.containsKey(condition)) {
                tempIds = temp.get(condition);
            } else {
                tempIds = new ArrayList<>();
                temp.put(condition, tempIds);
            }
            tempIds.add(bean.getId());
        }
        return temp;
    }

    @Override
    public ReadLongArrayEs getRewardArray(Player player, Task task, int rate) {
        Cfg_Task_daily_Bean bean = CfgManager.getCfg_Task_daily_Container().getValueByKey(task.getModelId());
        if (bean == null) {
            log.error("没有找到日常任务的数据 id为:" + task.getModelId());
            return null;
        }
        DailyTask dailyTask = (DailyTask) task;
        ReadLongArrayEs array = null;
        switch (dailyTask.getStar()) {
            case 0:
                array = bean.getRewards_0();
                break;
            case 1:
                array = bean.getRewards_1();
                break;
            case 2:
                array = bean.getRewards_2();
                break;
            case 3:
                array = bean.getRewards_3();
                break;
            case 4:
                array = bean.getRewards_4();
                break;
            case 5:
                array = bean.getRewards_5();
                break;
            default:
                log.error(TaskHelp.getPlayerInfo(player) + "首席任务星级错误:" + dailyTask.getStar());
                break;
        }
        if (array == null) {
            log.error("日常任务奖励物品不能为空！id=" + task.getModelId() + " 星级： " + dailyTask.getStar());
            return null;
        }
        if (rate == 1) {
            return array;
        } else if (rate > 1) {
            List<List<Long>> arrays = new ArrayList<>();
            for (ReadArray<Long> tempArray : array.getValuees()) {
                List<Long> shouldAdd = new ArrayList<>();
                shouldAdd.add(tempArray.get(0));
                shouldAdd.add(tempArray.get(1) * rate);
                arrays.add(shouldAdd);
            }
            return Utils.toReadLongArrayEsByList(arrays);
        }
        return array;
    }

    /**
     * 获取的是正常的十环
     *
     * @param player
     * @return
     */
    @Override
    public int getCanReceiveTask(Player player, int subType, boolean isFinal) {
        List<Integer> tempIds = getDailyTasks(player, subType);
        List<Integer> taskIds = new ArrayList<>();
        for (Integer integer : tempIds) {
            if (integer > 100) {//只取ID大于100的(任务ID在100以前的是固定刷任务,暂时废弃)
                taskIds.add(integer);
            }
        }
        if (taskIds.isEmpty()) {
            return -1;
        }
        //首先去除玩家已经接取的任务
        taskIds.remove(player.getLastDailyTaskId().get(subType));
//        log.info("日常任务开始随机，player = " + player.getName() + "; 随机组 = " + taskIds);
        if (taskIds.size() <= 0) {
            return -1;
        }

        //检查是否有指定接取任务
        for (int taskId : taskIds) {
            Cfg_Task_daily_Bean bean = CfgManager.getCfg_Task_daily_Container().getValueByKey(taskId);
            if (bean == null) {
                continue;
            }
            if (bean.getGuide_type() == 1) {
                return taskId;
            }
        }

        //再随机
        return taskIds.get(RandomUtils.random(taskIds.size()));
    }

    /**
     * 检查是不是所有类型的普通日常、周常都有记录
     *
     * @param curDailyTasks
     */
    private void checkDailyTask(ConcurrentHashMap<Integer, DailyTask> curDailyTasks) {
        for (Cfg_Task_daily_Bean bean : CfgManager.getCfg_Task_daily_Container().getValuees()) {
            if (curDailyTasks.containsKey(bean.getDaily_subtype())) {
                continue;
            }
            curDailyTasks.put(bean.getDaily_subtype(), new DailyTask());
        }
    }

    @Override
    public void noticeDeleteTask(Player player, int taskType, int modelId) {
        taskMessage.ResTaskDelete.Builder builder = taskMessage.ResTaskDelete.newBuilder();
        builder.setType(taskType);
        builder.setModelId(modelId);
        MessageUtils.send_to_player(player, taskMessage.ResTaskDelete.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    /**
     * 获取日常任务的前置任务
     *
     * @param player
     * @param subType
     * @return
     */
    private int getDailyProTaskId(Player player, int subType) {
        int taskId = -1;
        for (Cfg_Task_daily_Bean bean : CfgManager.getCfg_Task_daily_Container().getValuees()) {
            if (bean.getRecieve() == 0) {
                continue;
            }
            //判断是否等于9 不等于9 需要判断职业
            if (bean.getProfessional() != 9) {
                if ((bean.getProfessional() >= 0 && bean.getProfessional() < 100) && (bean.getProfessional() != player.getCareer())) {
                    continue;
                }

            }


            if ((bean.getLevel_max() < player.getLevel()) || (player.getLevel() < bean.getLevel_min())) {
                continue;
            }
            if (bean.getDaily_subtype() != subType) {
                continue;
            }
            taskId = bean.getId();
            break;
        }
        return taskId;
    }

    private List<Integer> getDailyTasks(Player player, int subType) {
        List<Integer> tempIds = new ArrayList<>();
        java.util.Map<TaskCondition, List<Integer>> temp = Manager.taskManager.getCanReceiveTasks().get(Task.DAILY_TASK);
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
     * 随机生成星级
     *
     * @param taskId 任务Id
     * @return 生成的星级
     */
    @Override
    public int randomStar(int taskId) {
        Cfg_Task_daily_Bean model = CfgManager.getCfg_Task_daily_Container().getValueByKey(taskId);
        Integer[] starArray = model.getStar().getValue();
        if (starArray.length < 6) {
            log.debug("策划配置日常任务星级个数不对，刷星操作 日常任务ID为" + taskId);
        }
        int[] maxValue = new int[6];
        int totalNumber = 0;
        for (int i = 0; i < 6; i++) {
            totalNumber += starArray[i];
            maxValue[i] = totalNumber;
        }
        int randomValue = new Random().nextInt(totalNumber) + 1;
        for (int i = 0; i < maxValue.length; i++) {
            if (randomValue <= maxValue[i]) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean checkTaskIsFinish(Player player, int taskId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void writeAcceptTaskLog(Player player, Task task) {
        DailyTaskAcceptLog dlog = new DailyTaskAcceptLog();
        DailyTask dailyTask = (DailyTask) task;
        try {
            if (task.isIsReceive()) {
                Manager.taskManager.deal().fillTaskLogBaseInfo(player, dlog, task);
                dlog.setStar(dailyTask.getStar());
                dlog.setTaskCount(player.getDailyTaskCount().get(dailyTask.getSubType()));
                LogService.getInstance().execute(dlog);
            }
        } catch (Exception e) {
            log.error(player.getName() + "(" + player.getId() + ")在记录接取日常任务的出错", e);
        }
    }

    @Override
    public void writeFinishTaskLog(Player player, Task task) {
        try {
            DailyTask dailyTask = (DailyTask) task;
            DailyTaskFinishLog finishLog = new DailyTaskFinishLog();
            Manager.taskManager.deal().fillTaskLogBaseInfo(player, finishLog, task);

            finishLog.setStar(dailyTask.getStar());
            finishLog.setTaskCount(player.getDailyTaskCount().get(dailyTask.getSubType()));
            finishLog.setFinishType(dailyTask.getTaskRewardPer());

            LogService.getInstance().execute(finishLog);
        } catch (Exception e) {
            log.error("(" + player.getName() + ")(" + player.getId() + ")在写日常任务完成日志时报错", e);
        }
    }

    @Override
    public void updateTaskProgress(Player player, Task task) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * 请求环数奖励
     *
     * @param player
     * @param mess
     */
    public void reqDailyTaskCountReward(Player player, taskMessage.ReqDailyTaskCountReward mess) {
        long actionId = IDConfigUtil.getLogId();
        List<Item> items = new ArrayList<>();
        taskMessage.ResDailyTaskCountReward.Builder msg = taskMessage.ResDailyTaskCountReward.newBuilder();
        taskMessage.DailyTaskCountReward.Builder dailyTaskCountRewardBuilder = taskMessage.DailyTaskCountReward.newBuilder();
        //次数判断
        if (mess.getCount() == Global.DailyTaskRingReward.get(0).get(1)) {
            items.add(Item.createItem(Global.DailyTaskRingReward.get(0).get(2), Global.DailyTaskRingReward.get(0).get(3), true));
        } else if (mess.getCount() == Global.DailyTaskRingReward.get(1).get(1)) {
            items.add(Item.createItem(Global.DailyTaskRingReward.get(1).get(2), Global.DailyTaskRingReward.get(1).get(3), true));
        } else {
            return;
        }
        long isReward = Manager.countManager.getCount(player, BaseCountType.DailyTaskRingReward, mess.getCount());
        if (isReward == 1) {
            return;
        }
        if (items.size() > 0) {
            if (!Manager.backpackManager.manager().addItems(player, items, getReason(0), actionId)) {
                Manager.mailManager.sendMailToPlayer(player.getId(),
                        MessageString.System, MessageString.System, MessageString.System, MessageString.BAG_FULL_MAIL_CONTENT, items, getReason(0));
            }
        }
        //标记每天
        Manager.countManager.setCount(player, BaseCountType.DailyTaskRingReward, mess.getCount(), Count.RefreshType.CountType_Day, 1);

        long newIsReward = Manager.countManager.getCount(player, BaseCountType.DailyTaskRingReward, mess.getCount());

        dailyTaskCountRewardBuilder.setCount(mess.getCount());
        //判断等于1 表示已经领取了
        dailyTaskCountRewardBuilder.setIsReward(newIsReward == 1);

        msg.setCountReward(dailyTaskCountRewardBuilder);
        MessageUtils.send_to_player(player, taskMessage.ResDailyTaskCountReward.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }
}
