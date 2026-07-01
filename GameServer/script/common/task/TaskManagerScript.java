package common.task;

import com.data.*;
import com.data.bean.*;
import com.data.struct.ReadArray;
import com.data.struct.ReadIntegerArray;
import com.data.struct.ReadIntegerArrayEs;
import com.data.struct.ReadLongArrayEs;
import com.game.backpack.structs.Item;
import com.game.backpack.structs.ItemCoinType;
import com.game.chat.structs.Notify;
import com.game.count.structs.BaseCountType;
import com.game.count.structs.VariantType;
import com.game.fightserver.manager.FightClientManager;
import com.game.mail.structs.MailType;
import com.game.manager.Manager;
import com.game.map.structs.Area;
import com.game.map.structs.MapObject;
import com.game.map.structs.MapUtils;
import com.game.monster.structs.Monster;
import com.game.npc.structs.Npc;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerDefine;
import com.game.script.structs.ScriptEnum;
import com.game.server.GameServer;
import com.game.structs.Gather;
import com.game.structs.ServerStr;
import com.game.task.command.TaskFinishCommand;
import com.game.task.log.TaskLogBean;
import com.game.task.log.TaskRewardLog;
import com.game.task.script.*;
import com.game.task.structs.*;
import com.game.utils.MessageUtils;
import com.game.utils.Utils;
import game.core.dblog.LogService;
import game.core.script.IScript;
import game.core.util.IDConfigUtil;
import game.core.util.JsonUtils;
import game.message.CrossServerMessage.F2GTaskAction;
import game.message.MapMessage;
import game.message.taskMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * 任务基础功能的脚本
 */
public class TaskManagerScript implements IScript, ITaskDeal {

    private static final Logger log = LogManager.getLogger(TaskManagerScript.class);

    @Override
    public int getId() {
        return ScriptEnum.TaskManagerBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void action(Player player, short actionType, int model, int num) {
        action(player, actionType, model, num, 0L);
    }

    @Override
    public void action(Player player, short actionType, int model, int num, long sign) {
        if (GameServer.getInstance().IsFightServer()) {
            //跨服的任务数据同步
            sendF2GTaskAction(player, actionType, model, num, sign);
            return;
        }

        try {
            //主线任务
            List<MainTask> currentMainTasks = player.getCurMainTasks();
            for (MainTask mainTask : currentMainTasks) {
                if (mainTask.getTargetType() == actionType) {
                    actionTask(player, mainTask, actionType, model, num, Task.MAIN_TASK);
                }
            }
            //日常任务
            for (DailyTask dailyTask : player.getCurDailyTasks().values()) {
                if (dailyTask.getModelId() == 0 || !dailyTask.isIsReceive()) {
                    continue;
                }
                //如果是被共享的，则首席任务不计数
                if ((dailyTask.getTargetType() == actionType) && (sign != TaskConst.RANGE_SHARE)) {
                    actionTask(player, dailyTask, actionType, model, num, Task.DAILY_TASK);
                }
            }
            //帮会任务
            for (ConquerTask conquerTask : player.getCurConquerTasks().values()) {
                if (conquerTask.getModelId() == 0 || !conquerTask.isIsReceive()) {
                    continue;
                }
                if (conquerTask.getTargetType() == actionType) {
                    actionTask(player, conquerTask, actionType, model, num, Task.GUILD_TASK);
                }
            }
            //转职任务
            GenderTask genderTask = player.getCurGenderTask();
            if (genderTask.getTargetType() == actionType) {
                actionTask(player, genderTask, actionType, model, num, Task.GENDER_TASK);
            }
            //支线任务
            List<BranchTask> braTask = player.getCurBranchTask();
            if (braTask != null && !braTask.isEmpty()) {
                for (BranchTask branchTask : braTask) {
                    if (branchTask.getTargetType() == actionType) {
                        actionTask(player, branchTask, actionType, model, num, Task.BRANCH_TASK);
                    }
                }
            }

        } catch (Exception e) {
            log.error(e, e);
            GameServer.getInstance().getErrorLogThread().pushErrorExcptionLog("taskManagerScript taskAction" + e, Arrays.toString(e.getStackTrace()));
        }
    }

    /**
     * 检查任务收集数据及判定是否完成任务并通知变更
     *
     * @param player     玩家
     * @param task       任务
     * @param actionType 变化类型
     * @param model      变化模型值
     * @param value      变化量
     * @return 返回是否有变更
     */
    private void actionTask(Player player, Task task, int actionType, int model, int value, int type) {
        try {
            boolean isShouldDeal = task.targetModels().contains(model);
            boolean isChange = false;//是否通知客户端变更
            boolean isOver = false;//是否完成
            switch (actionType) {
                case Task.ACTION_TYPE_FUNCTION:
                    if (!isShouldDeal) {
                        break;
                    }
                    if (selectTaskCondition(player, type, task.getModelId())) {
                        task.setCurNum(task.targetNum());
                        isChange = true;
                        if (type == Task.MAIN_TASK) {
                            isOver = true;
                        }
                    }else if(type == Task.MAIN_TASK){//主线任务,则同步进度
                        Cfg_Task_Bean mainTaskBean = CfgManager.getCfg_Task_Container().getValueByKey(task.getModelId());
                        if (mainTaskBean == null) {
                            return;
                        }
                        int now = Manager.controlManager.deal().getFuncProgress(player, mainTaskBean.getTarget());
                        int old = task.getCurNum();
                        if (now != old) {
                            task.setCurNum(now);
                            isChange = true;
                        }
                    }else if(type == Task.DAILY_TASK && task.getSubType() == 2){//如果是引导支线,则同步进度
                        Cfg_Task_daily_Bean dailyTaskBean = CfgManager.getCfg_Task_daily_Container().getValueByKey(task.getModelId());
                        if (dailyTaskBean == null) {
                            return;
                        }
                        int now = Manager.controlManager.deal().getFuncProgress(player, dailyTaskBean.getGoal_npc());
                        int old = task.getCurNum();
                        if (now != old) {
                            task.setCurNum(now);
                            isChange = true;
                        }
                    }else if(type == Task.BRANCH_TASK){//如果是支线,则同步进度
                        Cfg_Task_branch_Bean branchTaskBean = CfgManager.getCfg_Task_branch_Container().getValueByKey(task.getModelId());
                        if (branchTaskBean == null) {
                            return;
                        }
                        int now = Manager.controlManager.deal().getFuncProgress(player, branchTaskBean.getDemand_value().get(0));
                        int old = task.getCurNum();
                        if (now != old) {
                            task.setCurNum(now);
                            isChange = true;
                        }
                    }else if(type == Task.GENDER_TASK){//如果是转职任务,则同步进度
                        Cfg_Task_gender_Bean genderTaskBean = CfgManager.getCfg_Task_gender_Container().getValueByKey(task.getModelId());
                        if (genderTaskBean == null) {
                            return;
                        }
                        int now = Manager.controlManager.deal().getFuncProgress(player, genderTaskBean.getGoal_npc());
                        int old = task.getCurNum();
                        if (now != old) {
                            task.setCurNum(now);
                            if(!task.isFinish()){
                                isChange = true;
                            }
                        }
                    }
                    break;
                case Task.ACTION_TYPE_NPC_TALK:
                    isOver = true;
                    break;
                case Task.ACTION_TYPE_NEED_LEVEL:
                case Task.ACTION_TYPE_VIP_STATE_BREAK:
                    task.setCurNum(value);
                    if (value >= task.targetNum()) {
                        isOver = true;
                    }
                    break;
                case Task.ACTION_TYPE_PLANE_FABAO:
                    isChange = true;
                    isOver = true;
                    break;
                default://叠加型任务
                    if (isShouldDeal) {
                        int need = task.targetNum();
                        int old = task.getCurNum();
                        int now = old + value;
                        now = Math.min(now, need);
                        if (now != old) {
                            task.setCurNum(now);
                            isChange = true;
                        }
                        if (now >= need) {
                            task.setFinish(true);
                            isOver = true;
                        }
                    }
            }
            //检查到应该发送change
            if (isChange) {
                task.changeTask(player);
            }
            //检查到任务完成，处理一些特殊操作
            if (isOver) {
                //修改为客户端主动请求完成任务,服务器不再主动完成任务
//                checkTaskFinishAfter(player, task);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }

    @Override
    public void acceptTask(Player player, int type, int modelId, int subType, boolean isLogin) {
        IScript is = getScriptByType(type);
        if (is == null) {
            return;
        }
        if (!checkFunctionIsOpen(player, type)) {
            return;
        }
        if (is instanceof ITaskScript) {
            ((ITaskScript) is).acceptTask(player, modelId, subType, isLogin, false);
        } else {
            log.error("接收任务时没有找到任务实例！  type=" + type);
        }
    }

    @Override
    public void acceptTask(Player player, int type, int modelId, int subType, boolean isLogin, boolean isRefresh) {
        IScript is = getScriptByType(type);
        if (is == null) {
            return;
        }
        if (!checkFunctionIsOpen(player, type)) {
            return;
        }
        if (is instanceof ITaskScript) {
            ((ITaskScript) is).acceptTask(player, modelId, subType, isLogin, isRefresh);
        } else {
            log.error("接收任务时没有找到任务实例！  type=" + type);
        }
    }

    @Override
    public void computeTask(Player player, int type, boolean isLogin, boolean isRefresh) {
        IScript is = getScriptByType(type);
        if (is == null) {
            return;
        }
        if (!checkFunctionIsOpen(player, type)) {
            return;
        }
        if (is instanceof ITaskScript) {
            ((ITaskScript) is).computeTask(player, isLogin, isRefresh);
        } else {
            log.error("接收任务时没有找到任务实例！  type=" + type);
        }
    }

    /**
     * 其他的任务(除主线意外任务)
     *
     * @param player
     * @param isLogin
     * @return 返回是否有新的日常任务了
     */
    private void computeAnotherTask(Player player, boolean isLogin, boolean isRefresh) {
        //普通日常、周常
        computeTask(player, Task.DAILY_TASK, isLogin, isRefresh);
        //帮会日常、周常
        computeTask(player, Task.GUILD_TASK, isLogin, isRefresh);
        //支线
        acceptTask(player, Task.BRANCH_TASK, 0, 0, isLogin);
        computeTask(player, Task.BRANCH_TASK, isLogin, isRefresh);
    }

    @Override
    public boolean checkFinish(Player player, Task task, boolean isPromp) {
        try {
            switch (task.getTargetType()) {
                case Task.ACTION_TYPE_NPC_TALK:
                    return true;
                case Task.ACTION_TYPE_PLANE_FABAO:
                    task.setFinish(true);
                    return true;
                default:
                    if (task.getCurNum() < task.targetNum()) {
                        if (isPromp) {
                            log.error(player.getInfo()+"任务进度未达成时收到客户端完成任务请求，taskID="+task.getId()+",目标进度："+task.targetNum()+",当前进度:"+task.getCurNum());
                            MessageUtils.notify_player(player, Notify.ERROR, MessageString.Task_Target_Not_Match, task.targetNum() + "", task.getCurNum() + "");
                        }
                        return false;
                    }
                    break;
            }
        } catch (Exception e) {
            log.error("配置错误", e);
            return false;
        }
        task.setFinish(true);
        return true;
    }

    @Override
    public void quickFinish(Player player, int taskType, int subType, boolean isCheck, boolean isGM,int taskCount) {
        IScript is = getScriptByType(taskType);
        if (is == null) {
            return;
        }
        if (!checkFunctionIsOpen(player, taskType)) {
            return;
        }
        switch (taskType) {
            case Task.DAILY_TASK:
                ((IDailyTask) is).oneKeyFinishAllDailyTask(player, subType, isCheck,taskCount);
                break;
            case Task.GUILD_TASK:
                ((IConquerTask) is).oneKeyFinishAllConquerTask(player, subType);
                break;
            case Task.GENDER_TASK:
                ((IGenderTask) is).oneKeyFinishAllGenderTask(player, isGM);
                break;
            default:
                log.error("不支持其他的任务一键完成所有任务！任务类型：" + taskType);
                break;
        }
    }

    @Override
    public void addTaskLevelUp(Player player) {
        try {
            if (player == null) {
                log.error("等级变化时， 玩家找不到！");
                return;
            }
            //卡等级任务的检查
            Manager.taskManager.deal().action(player, Task.ACTION_TYPE_NEED_LEVEL, 0, player.getLevel());

            computeAnotherTask(player, false, false);
            //处理转职任务
            levelUpDealGenderTask(player);
        } catch (Exception e) {
            log.error(e, e);
        }
    }

    @Override
    public void levelUpDealGenderTask(Player player) {
        IScript is = getScriptByType(Task.GENDER_TASK);
        if (is != null) {
            ((IGenderTask) is).levelUpDealGenderTask(player);
        }
    }

    @Override
    public void functionVariableChange(Player player, int type, int changeNum) {
        try {
            if (player == null) {
                log.error("functionVariable改变时无法更新任务信息， 玩家找不到！");
                return;
            }
            action(player, Task.ACTION_TYPE_FUNCTION, type, changeNum, 0L);
            //检测接受任务
            computeTask(player, Task.MAIN_TASK, false, false);
            //其他任务完成进度检查
            computeAnotherTask(player, false, false);
            //TODO 2020.11.18需求新增转职任务处理,主线任务完成，检查是否可以接取的转职任务
            if(type==FunctionVariable.PlayerTaskID){
                levelUpDealGenderTask(player);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }

    /**
     * 任务变更同步周围场景
     *
     * @param player
     * @param show_npcArray
     * @param show_monsterArray
     * @param show_gatherArray
     */
    @Override
    public void taskChangeRoundSync(Player player, ReadArray<Integer> show_npcArray, ReadArray<Integer> show_monsterArray, ReadArray<Integer> show_gatherArray) {
        try {
//            if (show_npcArray == null && show_monsterArray == null && show_gatherArray == null) {
//                return;
//            }
            boolean sendMsg = false;
            MapMessage.ResRoundObjs.Builder roundObjs = MapMessage.ResRoundObjs.newBuilder();
            for (Area area : Manager.mapManager.getRoundAreas(player)) {
                for (Monster monster : area.getMonsters().values()) {
                    if (monster == null) {
                        continue;
                    }
//                    if (!contains(show_monsterArray, monster.getModelId())) {
//                        continue;
//                    }
                    if (monster.canSee(player)) {
                        roundObjs.addMonsters(MapUtils.getMonsterInfo(monster));
                        sendMsg = true;
                    }
                }
                for (Npc npc : area.getNpcs().values()) {
                    if (npc == null) {
                        continue;
                    }
//                    if (!contains(show_npcArray, npc.getModelId())) {
//                        continue;
//                    }
                    if (npc.canSee(player)) {
                        roundObjs.addNpcs(MapUtils.getNpcInfo(npc));
                        sendMsg = true;
                    }
                }
                for (Gather gather : area.getCollects().values()) {
                    if (gather == null) {
                        continue;
                    }
//                    if (!contains(show_gatherArray, gather.getModelId())) {
//                        continue;
//                    }
                    if (gather.canSee(player)) {
                        roundObjs.addGathers(MapUtils.getGatherInfo(gather));
                        sendMsg = true;
                    }
                }
            }
            if (sendMsg) {
                MessageUtils.send_to_player(player, MapMessage.ResRoundObjs.MsgID.eMsgID_VALUE, roundObjs.build().toByteArray());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取能任务掉落的所以怪物id集合
     *
     * @return <>不会为null,至少空集合</>
     */
    @Override
    public List<Integer> getMonsterListCanTaskDrop(Player player) {
        //一般比较少，定义少一点
        List<Integer> monsterIds = new ArrayList<>(4);
        List<Task> shouldDealTask = new ArrayList<>(4);
        //主线
        for (MainTask mainTask : player.getCurMainTasks()) {
            int targetType = mainTask.getTargetType();
            if (targetType == Task.ACTION_TYPE_COLLECT_ITEM || targetType == Task.ACTION_TYPE_COLLECT_REAL_ITEM) {
                shouldDealTask.add(mainTask);
            }
        }
        //转职
        GenderTask genderTask = player.getCurGenderTask();
        int targetType = genderTask.getTargetType();
        if (targetType == Task.ACTION_TYPE_COLLECT_ITEM || targetType == Task.ACTION_TYPE_COLLECT_REAL_ITEM) {
            shouldDealTask.add(genderTask);
        }
        for (Task task : shouldDealTask) {
            monsterIds.add(task.getOptionalParam().get(Task.Optional_Key_Submit_Item_Id));
        }
        return monsterIds;
    }

    @Override
    public void loadAllCanReceiveTask() {
        Manager.taskManager.getCanReceiveTasks().clear();
        //日常任务
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.DailyTaskBaseScript);
        Manager.taskManager.getCanReceiveTasks().put(Task.DAILY_TASK, ((ILevelTask) is).loadLevelTask(Task.DAILY_TASK));
        //帮会日常、周常任务
        is = Manager.scriptManager.GetScriptClass(ScriptEnum.ConquerTaskBaseScript);
        Manager.taskManager.getCanReceiveTasks().put(Task.GUILD_TASK, ((ILevelTask) is).loadLevelTask(Task.GUILD_TASK));
        //转职任务
        is = Manager.scriptManager.GetScriptClass(ScriptEnum.GenderTaskBaseScript);
        ((IGenderTask) is).loadGenderTaskCache();
    }

    @Override
    public void loginCheckTask(Player player) {
        try {
            computeTask(player, Task.MAIN_TASK, true, false);
            computeAnotherTask(player, true, false);

            if (!player.getCurMainTasks().isEmpty()) {
                MainTask currentMainTask = player.getCurMainTasks().get(0);
                player.setCurMainTaskId(currentMainTask.getModelId());
            }
            sendLoginTaskInfo(player, false);
            //
            player.setSendTaskInfo(true);

//            Manager.taskManager.guild().online(player);
        } catch (Exception e) {
            log.error(e, e);
        }
    }

    private void sendLoginTaskInfo(Player player, boolean isnewdailytask) {
        try {
            //写发送任务日志
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(TaskHelp.getPlayerInfo(player));
            stringBuilder.append("登录时发送任务信息");
            //发送当前任务完成列表
            taskMessage.ResMainTaskOver.Builder mm = taskMessage.ResMainTaskOver.newBuilder();
            mm.addAllOverIDs(player.getOverMainTaskIDs());
            MessageUtils.send_to_player(player, taskMessage.ResMainTaskOver.MsgID.eMsgID_VALUE, mm.build().toByteArray());

            taskMessage.ResTaskList.Builder msg = taskMessage.ResTaskList.newBuilder();
            //主线
            List<MainTask> currentMainTasks = player.getCurMainTasks();
            if (currentMainTasks.size() < 1) {
                log.error("没有主线任务了， 请策划查看一下主线任务表");
                // 构造一个虚假任务
                taskMessage.mainTaskInfo.Builder builder = taskMessage.mainTaskInfo.newBuilder();
                builder.setModelId(-1);
                builder.setUseItems(buildNullTaskAttribute());
                builder.addAllOverIDs(player.getOverMainTaskIDs());
                msg.setMainTask(builder);
            } else {
                taskMessage.mainTaskInfo tempInfo = currentMainTasks.get(0).buildTaskInfo(player);
                taskMessage.mainTaskInfo.Builder builder = taskMessage.mainTaskInfo.newBuilder(tempInfo);
                builder.addAllOverIDs(player.getOverMainTaskIDs());
                msg.setMainTask(builder.build());
                stringBuilder.append("主线任务(").append(currentMainTasks.get(0).getModelId()).append(")");
            }
            //日常
            if (Manager.taskManager.deal().checkFunctionIsOpen(player, Task.DAILY_TASK)) {
                for (DailyTask dailyTask : player.getCurDailyTasks().values()) {
                    if (dailyTask.getModelId() == 0) {
                        continue;
                    }
                    msg.addDailyTask(dailyTask.buildTaskInfo(player));
                    stringBuilder.append("日常任务(").append(dailyTask.getModelId()).append(")");
                }

                //设置环数奖励状态
                for(int i = 0;i<Global.DailyTaskRingReward.size();i++){
                    taskMessage.DailyTaskCountReward.Builder dailyTaskCountRewardBuilder = taskMessage.DailyTaskCountReward.newBuilder();
                    int dailyTaskRingRewardCount =  Global.DailyTaskRingReward.get(i).get(1);
                    long newIsReward =   Manager.countManager.getCount(player, BaseCountType.DailyTaskRingReward,dailyTaskRingRewardCount);
                    dailyTaskCountRewardBuilder.setCount(dailyTaskRingRewardCount);
                    dailyTaskCountRewardBuilder.setIsReward(newIsReward == 1);
                    msg.addCountRewardList(dailyTaskCountRewardBuilder);
                }
            }
            //帮会
            if (Manager.taskManager.deal().checkFunctionIsOpen(player, Task.GUILD_TASK)) {
                for (Map.Entry<Integer,ConquerTask> entry : player.getCurConquerTasks().entrySet()) {
                    int subType = entry.getKey();
                    ConquerTask conquerTask = entry.getValue();
                    if (subType != 2&&conquerTask.getModelId() <= 0) {
                        continue;
                    }
                    if(subType == 2){//仙盟任务特殊处理
                        boolean isReceive = false;
                        if(conquerTask != null&&conquerTask.getModelId()>0){
                            isReceive = true;
                        }
                        for (Integer taskId:player.getGuildTaskPool().getTaskIds()) {
                            if(isReceive && conquerTask.getModelId() == taskId){//客户端要求排除已接任务
                                msg.addConquerTask(conquerTask.buildTaskInfo(player));
                            }else{
                                msg.addConquerTask(Manager.taskManager.guild().buildTaskMessage(player,taskId));
                            }
                        }
                    }else{
                        msg.addConquerTask(conquerTask.buildTaskInfo(player));
                    }
                    stringBuilder.append("帮会任务(").append(conquerTask.getModelId()).append(")");
                }
            }
            //引导任务，原来的支线任务
            if (Manager.taskManager.deal().checkFunctionIsOpen(player, Task.BRANCH_TASK)) {
                List<BranchTask> currentBranchTasks = player.getCurBranchTask();
                for (BranchTask branchTask : currentBranchTasks) {
                    if (CfgManager.getCfg_Task_branch_Container().getValueByKey(branchTask.getModelId()) != null) {
                        msg.addBranchTask(branchTask.buildTaskInfo(player));
                        stringBuilder.append("支线任务(").append(branchTask.getModelId()).append(")");
                    }
                }
            }
            //转职
            if (Manager.taskManager.deal().checkFunctionIsOpen(player, Task.GENDER_TASK)) {
                GenderTask genderTask = player.getCurGenderTask();
//                if (!genderTask.isFinish()) {
                if(!player.getOverGenderTaskIds().contains(genderTask.getModelId())){//任务已经做过了
                    msg.addGenderTask(genderTask.buildTaskInfo(player));
                    stringBuilder.append("转职任务(").append(genderTask.getModelId()).append(")");
                }
            }

            msg.setIshasnewdailytask(isnewdailytask ? 1 : 0);
            msg.setOverDailyTaskCount(0);
            int all = 0;
            for (int size : player.getConquerTaskCount().values()) {
                all += size;
            }
            msg.setOverConquerTaskCount(all);
            msg.setOverGenderTaskCount(1);

            long count = Manager.countManager.getVariant(player, VariantType.BATTLE_TASK_FREE_FRESH_COUNT);
            int freeCount = Global.IDontKnow1;
            long value = (freeCount - count) > 0 ? (freeCount - count) : 0;
            msg.setRemainFreeFreshCount((int) value);
            msg.setGuildRefreshCount(player.getGuildTaskPool().getRefreshCount());
            msg.setGuildReceiveCount(player.getConquerTaskCount().get(2)!=null?player.getConquerTaskCount().get(2):0);
//            log.info("===========ResTaskList:"+msg.build().toString());
            MessageUtils.send_to_player(player, taskMessage.ResTaskList.MsgID.eMsgID_VALUE, msg.build().toByteArray());

            for (Map.Entry<Integer, Integer> dailyCount : player.getDailyTaskCount().entrySet()) {
                stringBuilder.append("日常" + dailyCount.getKey() + "Count：").append(dailyCount.getValue());
            }
            stringBuilder.append("转职Count：").append(1);
            for (Map.Entry<Integer, Integer> conquerCount : player.getConquerTaskCount().entrySet()) {
                stringBuilder.append("帮会" + conquerCount.getKey() + "Count：").append(conquerCount.getValue());
            }
            log.info(stringBuilder.toString());
        } catch (Exception e) {
            log.error("发送任务队列时出错", e);
        }
    }

    @Override
    public void zeroClockDeal(Player player) {
        if (player == null) {
            return;
        }
        computeAnotherTask(player, false, true);
        //跨天处理公会任务池
        Manager.taskManager.guild().zeroClockDeal(player);
    }

    private boolean contains(ReadArray<Integer> list, int modelId) {
        if (list == null || list.size() <= 0) {
            return false;
        }
        for (int value : list.getValue()) {
            if (value == modelId) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据type返回判断条件
     * @param player
     * @param type
     * @param modelId
     * @return
     */
    private boolean selectTaskCondition(Player player, int type, int modelId) {
        switch (type) {
            case Task.MAIN_TASK:
                Cfg_Task_Bean mainTaskBean = CfgManager.getCfg_Task_Container().getValueByKey(modelId);
                if (mainTaskBean == null) {
                    return false;
                }
                return Manager.controlManager.deal().checkFuncProgress(player, mainTaskBean.getTarget());
            case Task.DAILY_TASK:
                Cfg_Task_daily_Bean dailyTaskBean = CfgManager.getCfg_Task_daily_Container().getValueByKey(modelId);
                if (dailyTaskBean == null) {
                    return false;
                }
                return Manager.controlManager.deal().checkFuncProgress(player, dailyTaskBean.getGoal_npc());
            case Task.BRANCH_TASK:
                Cfg_Task_branch_Bean branchBean = CfgManager.getCfg_Task_branch_Container().getValueByKey(modelId);
                if (branchBean == null) {
                    return false;
                }
                return Manager.controlManager.deal().checkFuncProgress(player, branchBean.getDemand_value());
            case Task.GENDER_TASK:
                Cfg_Task_gender_Bean genderBean = CfgManager.getCfg_Task_gender_Container().getValueByKey(modelId);
                if (genderBean == null) {
                    return false;
                }
                return Manager.controlManager.deal().checkFuncProgress(player, genderBean.getGoal_npc());
            default:
                return false;
        }
    }

    @Override
    public void OnReqTaskFinish(Player player, taskMessage.ReqTaskFinish mess) {
        onFinishTask(player, mess.getType(),mess.getModelId(),(int)mess.getTaskId(),mess.getRewardPer(),mess.getSubType());
    }

    @Override
    public void OnReqOneKeyOverTask(Player player, taskMessage.ReqOneKeyOverTask mess) {
        int type = mess.getType();
        boolean isSuccessFinish = false;
        IScript is = getScriptByType(mess.getType());
        if (is == null) {
            return;
        }
        if (!checkFunctionIsOpen(player, type)) {
            return;
        }
        switch (type) {
            case Task.DAILY_TASK:
                isSuccessFinish = ((IDailyTask) is).dailyTaskOneKeyFinish(player, mess);
                break;
            case Task.GUILD_TASK:
                isSuccessFinish = ((IConquerTask) is).conquerTaskOneKeyFinish(player, mess);
                break;
            default:
                log.error(TaskHelp.getPlayerInfo(player) + "试图一键完成某个任务，但是任务类型不匹配，其任务类型为(" + type + ")");
                break;
        }
        if (isSuccessFinish) {
            log.info(TaskHelp.getLog(player, type, TaskHelp.RequsetOneKeyFinish + " success", mess.getTaskModelId()));
        } else {
            log.info(TaskHelp.getLog(player, type, TaskHelp.RequsetOneKeyFinish + " failed", mess.getTaskModelId()));
        }
    }

    @Override
    public void OnReqQuickFinish(Player player, taskMessage.ReqQuickFinish mess) {
        quickFinish(player, mess.getType(), mess.getSubType(), true, false,mess.getTaskCount());
    }

    @Override
    public void OnReqChangeTaskState(Player player, taskMessage.ReqChangeTaskState mess) {
        int type = mess.getType();
        int modelId = mess.getModelId();
        Task task = getTaskByModelId(player, type, modelId);
        //1.检查玩家当前是否有此任务
        if (task == null) {
            log.error(TaskHelp.getLog(player, type, TaskHelp.NoFind, modelId));
            return;
        }
        //如果是支线任务走单独的处理,处理完就走人
        if (type == Task.BRANCH_TASK) {
            actionTask(player, task, task.getTargetType(), task.targetModels().get(0), 1, Task.BRANCH_TASK);
            return;
        }
        //2.检查是否在目标地图
        if (player.gainMapModelId() != task.getTargetMap()) {
            return;
        }
        //3.检查与目标的距离
        double distance = 1000;
        switch (task.getTargetType()) {
            case Task.ACTION_TYPE_PLANE_FABAO:
                distance = 0;
                break;
            case Task.ACTION_TYPE_ARRIVE_POS:
                distance = Utils.getDistance(task.getTargetPosition(), player.gainCurPos());
                break;
            case Task.ACTION_TYPE_SUBMIT_ITEM:
                Integer npcId = task.targetModels().get(0);
                if (npcId == null || npcId <= 0) {
                    return;
                }
                MapObject map = Manager.mapManager.getMap(player.gainMapId());
                Npc npc = map.getNpcs().get((long) npcId);
                if (npc != null) {
                    distance = Utils.getDistance(npc.gainCurPos(), player.gainCurPos());
                }
                break;
            default:
                break;
        }
        if (distance > Global.TaskFindRaner) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.TASK_TO_NPC);
            return;
        }
        //4.额外的检查
        switch (task.getTargetType()) {
            case Task.ACTION_TYPE_SUBMIT_ITEM:
                int itemId = task.getOptionalParam().get(Task.Optional_Key_Submit_Item_Id);
                //提交道具默认扣除一个
                if (!Manager.backpackManager.manager().onRemoveItem(player, itemId, 1, ItemChangeReason.TaskSubmitItemDec, IDConfigUtil.getLogId())) {
                    MessageUtils.notify_player(player, Notify.ERROR, MessageString.Item_Not_Enough, Manager.backpackManager.manager().getName(itemId));
                    return;
                }
                break;
            default:
                break;
        }
        //到指定位置，位面任务不算完成
        if (task.getTargetType() != Task.ACTION_TYPE_PLANE) {
            task.setFinish(true);
        }

        actionTask(player, task, task.getTargetType(), task.targetModels().get(0), 1, type);
    }

    @Override
    public void OnReqTaskReceive(Player player, taskMessage.ReqReceiveTask mess) {
        acceptTask(player, mess.getType(), mess.getTaskId(), mess.getSubType(), false);
    }

    @Override
    public void OnReqCheckTaskIsFinish(Player player, taskMessage.ReqCheckTaskIsFinish mess) {
        log.info(TaskHelp.getLog(player, mess.getType(), TaskHelp.RequestSubmit, mess.getTaskId()));
        IScript is = getScriptByType(mess.getType());
        if (is == null) {
            return;
        }
        if (is instanceof ITaskScript) {
            ((ITaskScript) is).checkTaskIsFinish(player, mess.getTaskId());
        } else {
            log.error("查询任务时没有找到任务实例！  mess=" + mess);
        }
    }

    @Override
    public void removeLastAdd(List<Integer[]> one, Integer[] other) {
        if (other == null || other.length < 2) {
            return;
        }
        Iterator<Integer[]> ite = one.iterator();
        while (ite.hasNext()) {
            Integer[] value = ite.next();
            if (value[0] == (int) other[0]) {
                value[1] -= other[1];
                if (value[1] <= 0) {
                    ite.remove();
                }
            }
        }
    }

    @Override
    public void mergeArrayList(List<Integer[]> one, Integer[] other) {
        if (other == null || other.length < 2) {
            return;
        }
        boolean canAdd = true;
        for (Integer[] value : one) {
            if (value[0] == (int) other[0]) {
                value[1] += other[1];
                canAdd = false;
            }
        }
        if (canAdd) {
            one.add(Arrays.stream(other).toArray(Integer[]::new));
        }
    }

    @Override
    public void addRewardsAndHeapUp(List<List<Long>> rewardArray, List<List<Long>> addRewards) {
        if (addRewards == null) {
            return;
        }

        for (List<Long> newItem : addRewards) {
            boolean add = false;
            for (List<Long> item : rewardArray) {
                if (newItem.get(0) == item.get(0)) {
                    item.set(1, item.get(1) + newItem.get(1));
                    add = true;
                    break;
                }
            }
            if (!add) {
                List<Long> l = new ArrayList<>();
                l.add(newItem.get(0));
                l.add(newItem.get(1));
                rewardArray.add(l);
            }
        }
    }

    @Override
    public void addRewardsAndHeapUp(List<List<Long>> rewardArray, ReadLongArrayEs addRewards) {
        if (addRewards == null) {
            return;
        }

        for (ReadArray<Long> newItem : addRewards.getValuees()) {
            boolean add = false;
            for (List<Long> item : rewardArray) {
                if (newItem.get(0) == item.get(0)) {
                    item.set(1, item.get(1) + newItem.get(1));
                    add = true;
                    break;
                }
            }
            if (!add) {
                List<Long> l = new ArrayList<>();
                l.add(newItem.get(0));
                l.add(newItem.get(1));
                rewardArray.add(l);
            }
        }
    }

    @Override
    public ReadArray<Integer>[] buildEs(ArrayList<Integer[]> oneKeyFinishNeedGold) {
        int size = oneKeyFinishNeedGold.size();
        ReadArray[] readArrays = new ReadArray[size];
        for (int i = 0; i < size; i++) {
            ReadIntegerArray readIntegerArray = new ReadIntegerArray(oneKeyFinishNeedGold.get(i));
            readArrays[i] = readIntegerArray;
        }
        return readArrays;
    }

    @Override
    public void sendOneKeyReward(Player player, List<List<Long>> rewardArray, int actionId, int type, int reason) {
        List<Item> createItems = Item.createItems(Utils.toReadLongArrayEsByList(rewardArray));
        // 是否有足够的空间加入多个物品
        List<Item> mailAccessory = new ArrayList<>();
        for (Item item : createItems) {
            if (Manager.backpackManager.manager().onHasAddSpace(player, item)) {
                Manager.backpackManager.manager().addItem(player, item, reason, actionId);
            } else {
                mailAccessory.add(item);
            }
        }
        //无空间则发邮件
        if (mailAccessory.size() > 0) {
            Manager.taskManager.deal().sendRewardByMail(player, mailAccessory, reason, actionId);
        }

        Manager.taskManager.deal().writeRewardLog(player, actionId, type, createItems, TaskConst.DEFAULT_REWARD);
    }

    public void sendRewardByMail(Player player, List<Item> list) {
        sendRewardByMail(player, list, 0, 0);
    }

    @Override
    public void sendRewardByMail(Player player, List<Item> list, int reason, long actionId) {
        if (player == null) {
            return;
        }

        if (list.size() < 1) {
            return;
        }

        MessageUtils.notify_player(player, Notify.ERROR, MessageString.BAGISSPACETOMAIL);
        Manager.mailManager.sendMailToPlayer(player.getId()
                , MailType.TaskSendMail
                , MessageString.Task_SYSTEM
                , MessageString.Task_Title
                , MessageString.Task_Content
                , list, reason, actionId);
    }

    private void sendF2GTaskAction(Player player, short actionType, int model, int num, long sign) {
        F2GTaskAction.Builder msg = F2GTaskAction.newBuilder();
        msg.setModelId(model);
        msg.setNum(num);
        msg.setRoleId(player.getId());
        msg.setType(actionType);
        msg.setSign(sign);
        FightClientManager.GetInstance().send_to_game(player.getIosession(), F2GTaskAction.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public boolean checkFunctionIsOpen(Player player, int type) {
        switch (type) {
            case Task.MAIN_TASK:
                return Manager.controlManager.deal().isOpenFunction(player, FunctionStart.TaskMain);
            case Task.DAILY_TASK:
                return Manager.controlManager.deal().isOpenFunction(player, FunctionStart.TaskDaily);
            case Task.GUILD_TASK:
                return Manager.controlManager.deal().isOpenFunction(player, FunctionStart.TaskGuild);
            case Task.BRANCH_TASK:
                return Manager.controlManager.deal().isOpenFunction(player, FunctionStart.TaskBranch);
            case Task.GENDER_TASK:
                return Manager.controlManager.deal().isOpenFunction(player, FunctionStart.TaskZhuanZhi);
            default:
                return false;
        }
    }

    /**
     * 根据任务类型获取任务的脚本
     *
     * @param type 任务类型
     * @return 处理该类型的任务脚本
     */
    private IScript getScriptByType(int type) {
        int scriptId;
        switch (type) {
            case Task.MAIN_TASK:
                scriptId = ScriptEnum.MainTaskBaseScript;
                break;
            case Task.DAILY_TASK:
                scriptId = ScriptEnum.DailyTaskBaseScript;
                break;
            case Task.GUILD_TASK:
                scriptId = ScriptEnum.ConquerTaskBaseScript;
                break;
            case Task.BRANCH_TASK:
                scriptId = ScriptEnum.BranchTaskBaseScript;
                break;
            case Task.GENDER_TASK:
                scriptId = ScriptEnum.GenderTaskBaseScript;
                break;
            default:
                scriptId = -1;
                break;
        }

        if (scriptId < 1) {
            log.error("查询任务时没有找到任务实例！没有找到任务实例ID=" + scriptId + " type=" + type);
            return null;
        }
        return Manager.scriptManager.GetScriptClass(scriptId);
    }

    @Override
    public Task getTaskByModelId(Player player, int type, int modelId) {
        switch (type) {
            case Task.MAIN_TASK:
                List<MainTask> currentMainTasks = player.getCurMainTasks();
                for (MainTask mainTask : currentMainTasks) {
                    if (mainTask.getModelId() == modelId) {
                        return mainTask;
                    }
                }
                break;
            case Task.GUILD_TASK:
                for (ConquerTask conquerTask : player.getCurConquerTasks().values()) {
                    if (conquerTask.getModelId() == 0) {
                        continue;
                    }
                    if (conquerTask.getModelId() == modelId) {
                        return conquerTask;
                    }
                }
                break;
            case Task.DAILY_TASK:
                for (DailyTask dailyTask : player.getCurDailyTasks().values()) {
                    if (dailyTask.getModelId() == 0) {
                        continue;
                    }
                    if (dailyTask.getModelId() == modelId) {
                        return dailyTask;
                    }
                }
                break;
            case Task.BRANCH_TASK:
                List<BranchTask> currentBranchTasks = player.getCurBranchTask();
                for (BranchTask branchTask : currentBranchTasks) {
                    if (branchTask.getModelId() == modelId) {
                        return branchTask;
                    }
                }
                break;
            case Task.GENDER_TASK:
                GenderTask genderTask = player.getCurGenderTask();
                if (genderTask.getModelId() == modelId) {
                    return genderTask;
                }
                break;
            default:
                // LOGGER.error(TaskHelp.getPlayerInfo(player) + "根据任务类型和ModelId 获取任务时，此任务类型暂时不处理：type：" + type + " taskId:" + modelId);
                break;
        }
        return null;
    }

    @Override
    public Task getTaskByModelId(Player player, int modelId) {
        Task task = getTaskByModelId(player, Task.MAIN_TASK, modelId);
        if (task != null) {
            return task;
        }
        task = getTaskByModelId(player, Task.DAILY_TASK, modelId);
        if (task != null) {
            return task;
        }
        task = getTaskByModelId(player, Task.GUILD_TASK, modelId);
        if (task != null) {
            return task;
        }
        task = getTaskByModelId(player, Task.GENDER_TASK, modelId);
        return task;
    }

    @Override
    public int getTargetModelByType(int type, ReadArray target) {
        if (target.size() < 2) {
            log.error("类型为：" + type + "的任务，任务目标配置，小于2个！");
            return 0;
        }
        switch (type) {
            case Task.ACTION_TYPE_VIP_STATE_BREAK:
                return 0;
            default:
                return (int) target.get(0);
        }
    }

    @Override
    public int getTargetNumByType(int type, ReadArray target) {
        if (target.size() < 2) {
            log.error("类型为：" + type + "的任务，任务目标配置，小于2个！");
            return 0;
        }
        switch (type) {
            case Task.ACTION_TYPE_SUBMIT_ITEM:
                return 1;
            case Task.ACTION_TYPE_NPC_TALK:
                return 0;
            case Task.ACTION_TYPE_FUNCTION:
                return (int) target.get(target.size() - 1);
            default:
                return (int) target.get(1);
        }
    }

    @Override
    public int getFirstMainTaskId() {
        return Global.FristTask;
    }

    @Override
    public void writeRewardLog(Player player, int modelId, int type, List<Item> rewardItem, int rewardType) {
        StringBuilder rewardString = new StringBuilder();
        for (Item it : rewardItem) {
            String name = Manager.backpackManager.manager().getName(it.getItemModelId());
            rewardString.append(name).append("(").append(it.getNum()).append("),");
        }
        try {
            writeRewardLog(player, modelId, type, rewardString.toString(), rewardType);
        } catch (Exception e) {
            log.error("玩家" + player.getName() + "(" + player.getId() + ")写入任务奖励日志时出错！参数信息：(modelId:" + modelId + ")(type:" + type + ")(reward:" + rewardString.toString() + ")");
        }
    }

    @Override
    public void writeRewardLog(Player player, int modelId, int type, String reward, int rewardType) {
        if (player == null) {
            return;
        }
        TaskRewardLog blog = new TaskRewardLog();
        blog.setModelId(modelId);
        blog.setPlatformname(player.getPlatformName());
        blog.setReward(reward);
        blog.setRoleId(player.getId());
        blog.setRolename(player.getName());
        blog.setType(type);
        blog.setUserId(player.getUserId());
        blog.setRewardType(rewardType);
        LogService.getInstance().execute(blog);
    }

    @Override
    public void fillTaskLogBaseInfo(Player player, TaskLogBean taskLog, Task task) {
        taskLog.setPlatform(player.getPlatformName());
        taskLog.setRoleId(player.getId());
        taskLog.setRoleName(player.getName());
        taskLog.setUserId(player.getUserId());
        taskLog.setSid(player.getCreateServerId());
        taskLog.setTaskInfo(JsonUtils.toJSONString(task));
        taskLog.setModelId(task.getModelId());
    }

    @Override
    public void fullTaskInitOptionParam(Task task, ReadArray target) {
        if (task.getTargetType() == Task.ACTION_TYPE_ARRIVE_POS) {
            task.getTargetPosition().setX((int) target.get(0));
            task.getTargetPosition().setY((int) target.get(1));
        } else if (task.getTargetType() == Task.ACTION_TYPE_SUBMIT_ITEM) {
            task.getOptionalParam().put(Task.Optional_Key_Middle_Npc_Id, (int) target.get(0));
            task.getOptionalParam().put(Task.Optional_Key_Submit_Item_Id, (int) target.get(1));
        } else if (task.getTargetType() == Task.ACTION_TYPE_COLLECT_ITEM) {
            if (target.size() < 3) {
                log.error("任务类型" + task.acqType() + " 任务id：" + task.getModelId() + "的target 少于三位");
                return;
            }
            task.getOptionalParam().put(Task.Optional_Key_Submit_Item_Id, (int) target.get(2));
        } else if (task.getTargetType() == Task.ACTION_TYPE_COLLECT_REAL_ITEM) {
            if (target.size() < 3) {
                log.error("任务类型" + task.acqType() + " 任务id：" + task.getModelId() + "的target 少于三位");
                return;
            }
            task.getOptionalParam().put(Task.Optional_Key_Submit_Item_Id, (int) target.get(2));
        }
    }

    @Override
    public void fullTaskOptionParam(Task task, ReadArray target, ReadArray pos) {
        if (task.getTargetType() == Task.ACTION_TYPE_ARRIVE_POS) {
            task.getTargetPosition().setX((int) target.get(0));
            task.getTargetPosition().setY((int) target.get(1));
        } else if (task.getTargetType() == Task.ACTION_TYPE_SUBMIT_ITEM) {
            task.getOptionalParam().put(Task.Optional_Key_Middle_Npc_Id, (int) target.get(0));
            task.getOptionalParam().put(Task.Optional_Key_Submit_Item_Id, (int) target.get(1));
        } else if (task.getTargetType() == Task.ACTION_TYPE_COLLECT_ITEM) {
            if (target.size() < 3) {
                log.error("任务类型" + task.acqType() + " 任务id：" + task.getModelId() + "的target 少于三位");
                return;
            }
            task.getOptionalParam().put(Task.Optional_Key_Submit_Item_Id, (int) target.get(2));
        } else if (task.getTargetType() == Task.ACTION_TYPE_COLLECT_REAL_ITEM) {
            if (target.size() < 3) {
                log.error("任务类型" + task.acqType() + " 任务id：" + task.getModelId() + "的target 少于三位");
                return;
            }
            task.getOptionalParam().put(Task.Optional_Key_Submit_Item_Id, (int) target.get(2));
        } else if (task.getTargetType() == Task.ACTION_TYPE_PLANE) {
            if (pos.size() < 2) {
                log.error("任务类型" + task.acqType() + " 任务id：" + task.getModelId() + "task_x_y 少于2位");
                return;
            }
            task.getTargetPosition().setX((int) pos.get(0));
            task.getTargetPosition().setY((int) pos.get(1));

            if (target.size() < 3) {
                log.error("任务类型" + task.acqType() + " 任务id：" + task.getModelId() + "的target 少于三位");
                return;
            }
            task.getOptionalParam().put(Task.Optional_Key_Copy_Map_Id, (int) target.get(2));
        } else if (task.getTargetType() == Task.ACTION_TYPE_PLANE_FABAO) {
            if (pos.size() < 2) {
                log.error("任务类型" + task.acqType() + " 任务id：" + task.getModelId() + "task_x_y 少于2位");
                return;
            }
            task.getTargetPosition().setX((int) pos.get(0));
            task.getTargetPosition().setY((int) pos.get(1));

            if (target.size() < 3) {
                log.error("任务类型" + task.acqType() + " 任务id：" + task.getModelId() + "的target 少于三位");
                return;
            }
            task.getOptionalParam().put(Task.Optional_Key_Copy_Map_Id, (int) target.get(2));
        }
    }

    @Override
    public void buildTaskAttributeSame(Task task, taskMessage.taskAttribute.Builder builder) {
        if (task.getTargetType() == Task.ACTION_TYPE_ARRIVE_POS) {
            builder.setXPos((int) task.getTargetPosition().getX());
            builder.setYPos((int) task.getTargetPosition().getY());
            builder.setNum(task.getCurNum());
        } else if (task.getTargetType() == Task.ACTION_TYPE_SUBMIT_ITEM) {
            builder.setItemId(task.getOptionalParam().get(Task.Optional_Key_Submit_Item_Id));
            builder.setNum(task.getCurNum());
        } else if (task.getTargetType() == Task.ACTION_TYPE_COLLECT_ITEM) {
            //对于收集虚拟道具的任务采用itemId 这个字段来传送 哪个怪身上掉这个道具
            builder.setItemId(task.getOptionalParam().get(Task.Optional_Key_Submit_Item_Id));
        } else if (task.getTargetType() == Task.ACTION_TYPE_COLLECT_REAL_ITEM) {
            //对于收集虚拟道具的任务采用itemId 这个字段来传送 哪个怪身上掉这个道具
            builder.setItemId(task.getOptionalParam().get(Task.Optional_Key_Submit_Item_Id));
        } else if (task.getTargetType() == Task.ACTION_TYPE_PLANE) {
            builder.setXPos((int) task.getTargetPosition().getX());
            builder.setYPos((int) task.getTargetPosition().getY());
        } else if (task.getTargetType() == Task.ACTION_TYPE_PLANE_FABAO) {
            builder.setXPos((int) task.getTargetPosition().getX());
            builder.setYPos((int) task.getTargetPosition().getY());
        }
        builder.setNeedNum(task.targetNum());
    }

    @Override
    public taskMessage.taskAttribute.Builder buildNullTaskAttribute() {
        taskMessage.taskAttribute.Builder sb = taskMessage.taskAttribute.newBuilder();
        sb.setModel(0);
        sb.setNum(0);
        sb.setMapId(0);
        sb.setNeedNum(0);
        return sb;
    }

    @Override
    public String getMainTaskChatName(int mainTaskId) {
        Cfg_Task_Bean bean = CfgManager.getCfg_Task_Container().getValueByKey(mainTaskId);
        if (bean == null) {
            return "mainTask" + mainTaskId;
        }
        return ServerStr.getChatTableName(bean.getTask_name());
    }

    @Override
    public boolean isTaskFinish(Player player, int taskId){
        Task task = getTaskByModelId(player, taskId);
        if(task == null){
            //主线任务检查
            return player.overMainTask(taskId);
        }
        return task.isFinish();
    }

    @Override
    public void onFinishTask(Player player, int taskType, int modelId, int taskId, int finishPer, int subType) {
        IScript is = getScriptByType(taskType);
        if (is == null) {
            return;
        }
        if (!checkFunctionIsOpen(player, taskType)) {
            return;
        }
        if (is instanceof ITaskScript) {
            ((ITaskScript) is).onFinishTask(player, modelId, taskId, finishPer, false, subType);
        } else {
            log.error("没有找到任务实例！ taskType=" + taskType);
        }
    }

    @Override
    public void onReqGetTarget(Player player) {
        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.TargetTask)) {
            return;
        }

        Cfg_Task_target_reward_Bean bean = CfgManager.getCfg_Task_target_reward_Container().getValueByKey(player.getTaskTargetStage());
        if (bean == null) {
            return;
        }

        long actionId = IDConfigUtil.getLogId();
        if(!Manager.currencyManager.manager().onDecItemCoin(player, bean.getNeed_num(), ItemChangeReason.TaskTargetRewardDec, actionId, ItemCoinType.TaskTarget)){
            return;
        }

        List<Item> createItems = getItems(player, bean.getReward());
        if(!Manager.backpackManager.manager().addItems(player, createItems, ItemChangeReason.TaskTargetRewardGet, actionId)){
            if(!Manager.mailManager.sendMailToPlayer(player.getId(), 1, MessageString.System,
                    MessageString.BAG_FULL_MAIL_TITLE, MessageString.BAG_FULL_MAIL_CONTENT, createItems, ItemChangeReason.TaskTargetRewardGet, actionId)){
                return;
            }
        }

        if(bean.getIf_loop() != 1){
            Cfg_Task_target_reward_Bean nextBean = CfgManager.getCfg_Task_target_reward_Container().getValueByKey(player.getTaskTargetStage()+1);
            if (nextBean == null) {
                return;
            }
            player.setTaskTargetStage(nextBean.getId());
        }

        sendResTargetInfoMessage(player);

        if(bean.getId()>1){
            MessageUtils.notify_player(player, Notify.CHAT_SYS_MARQUEE, MessageString.Target_Reward_Notice, player.getName(), String.valueOf(bean.getId()));
        }
        Manager.biManager.getScript().biRealm(player, 2, 2, 0, bean.getId(), "");
    }

    private List<Item> getItems(Player player, ReadIntegerArrayEs itemBean) {
        List<Item> items = new ArrayList<>();
        for (ReadArray<Integer> l : itemBean.getValuees()) {
            if (l.size()>=4&&l.get(3) != PlayerDefine.CAREER_All && l.get(3) != player.getCareer()) {
                continue;
            }
            items.addAll(Item.createItems(l.get(0), l.get(1), l.get(2) == 1));
        }
        return items;
    }

    @Override
    public void sendResTargetInfo(Player player) {
//        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.TargetTask)) {
//            return;
//        }

        sendResTargetInfoMessage(player);
    }

    private void sendResTargetInfoMessage(Player player) {
        taskMessage.ResTargetInfo.Builder msg = taskMessage.ResTargetInfo.newBuilder();
        msg.setStage(player.getTaskTargetStage());
        MessageUtils.send_to_player(player, taskMessage.ResTargetInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }
}
