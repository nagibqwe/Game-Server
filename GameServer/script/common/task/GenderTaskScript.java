package common.task;

import com.data.*;
import com.data.bean.Cfg_Changejob_Bean;
import com.data.bean.Cfg_Task_gender_Bean;
import com.data.struct.ReadArray;
import com.data.struct.ReadIntegerArrayEs;
import com.data.struct.ReadLongArrayEs;
import com.game.backpack.structs.Item;
import com.game.backpack.structs.ItemCoinType;
import com.game.chat.structs.Notify;
import com.game.mail.structs.MailType;
import com.game.manager.Manager;
import com.game.map.structs.MapObject;
import com.game.npc.structs.Npc;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerAttributeType;
import com.game.player.structs.PlayerDefine;
import com.game.script.structs.ScriptEnum;
import com.game.task.log.GenderTaskAcceptLog;
import com.game.task.log.GenderTaskFinishLog;
import com.game.task.script.IGenderTask;
import com.game.task.script.ITaskScript;
import com.game.task.structs.GenderTask;
import com.game.task.structs.Task;
import com.game.task.structs.TaskConst;
import com.game.task.structs.TaskHelp;
import com.game.utils.BIUtils;
import com.game.utils.MessageUtils;
import com.game.utils.Utils;
import game.core.dblog.LogService;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.PlayerMessage;
import game.message.taskMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * 转职任务脚本
 */
public class GenderTaskScript implements ITaskScript, IGenderTask {

    private static final Logger log = LogManager.getLogger(GenderTaskScript.class);

    @Override
    public int getId() {
        return ScriptEnum.GenderTaskBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void computeTask(Player player, boolean isLogin, boolean isRefresh) {

    }

    @Override
    public void acceptTask(Player player, int modelId, int subType, boolean isLogin, boolean isRefresh) {
        if (player == null) {
            return;
        }

        GenderTask task = player.getCurGenderTask();
        if (task.getModelId() != 0 && !task.isFinish()) {
            return;
        }
        boolean isSuccess = task.initTask(player, modelId);
        if (!isSuccess) {
            log.info("领取任务失败：" + modelId);
            return;
        }

        updateTaskProgress(player, task);
        changeTask(player, task, isRefresh);

        writeAcceptTaskLog(player, task);

        Cfg_Task_gender_Bean bean = CfgManager.getCfg_Task_gender_Container().getValueByKey(task.getModelId());
        Manager.biManager.getScript().biTask(player, task.getModelId(), Task.GENDER_TASK, TaskConst.BI_RECEIVE, bean.getTask_name(), task.getSubType(), 0);
    }

    @Override
    public boolean onFinishTask(Player player, int modelId, int taskId, int finishPer, boolean isGm, int subType) {
        Task task = Manager.taskManager.deal().getTaskByModelId(player, Task.GENDER_TASK, taskId);
        if (task == null) {
            log.error("角色身上找不到请求的任务,角色 :" + player.getId() + "任务ID:" + taskId);
            return false;
        }
        GenderTask genderTask = (GenderTask) task;
        Cfg_Task_gender_Bean model = CfgManager.getCfg_Task_gender_Container().getValueByKey(genderTask.getModelId());
        if (model == null) {
            log.error("转职任务找不到" + genderTask.getModelId());
            return false;
        }
        boolean isLast = false;
        Cfg_Task_gender_Bean next = CfgManager.getCfg_Task_gender_Container().getValueByKey(genderTask.getModelId() + 1);
        if (next == null || next.getGenderClass() != model.getGenderClass()) {
            isLast = true;
        }

        if (!isGm && !isLast) {
            if (model.getAuto_commit() == 1) {
                //对话任务
                if (!genderTask.isFinish() && model.getTask_type() == Task.ACTION_TYPE_NPC_TALK) {
                    int npcId = model.getGoal_npc().getValue()[0];

                    if (npcId < 1) {
                        MessageUtils.notify_player(player, Notify.ERROR, MessageString.TASK_NPC_NOTHVAE);
                        return false;
                    }

                    //检查NPC的距离
                    MapObject map = Manager.mapManager.getMap(player.gainMapId());
                    Npc npc = map.getNpcs().get((long) npcId);
                    if (npc == null) {
                        MessageUtils.notify_player(player, Notify.ERROR, MessageString.TASK_NPC_NOTHVAE);
                        return false;
                    }
                    double distance = Utils.getDistance(npc.gainCurPos(), player.gainCurPos());
                    if (distance > 10) {
                        MessageUtils.notify_player(player, Notify.ERROR, MessageString.TASK_TO_NPC);
                        return false;
                    }
                }
            }
            if (genderTask.getTargetType() == Task.ACTION_TYPE_COLLECT_REAL_ITEM) {
                if (!Manager.backpackManager.manager().onRemoveItem(player,
                        task.targetModels().get(0), task.targetNum(), ItemChangeReason.CollectTaskSubmitItemDec, IDConfigUtil.getLogId())) {
                    MessageUtils.notify_player(player, Notify.ERROR, MessageString.Not_Enough_Gold);
                    return false;
                }
            }
            if (!genderTask.checkFinish(true, player)) {
                buildResTaskFinish(player, taskId, TaskConst.TASK_NOT_FINISH);
                return false;
            }
        }

        if(player.getOverGenderTaskIds().contains(taskId)){//任务已经做过了
            buildResTaskFinish(player, taskId, TaskConst.SUCCESS);
            return false;
        }

        // 发奖励
        long actionId = IDConfigUtil.getLogId();
        Cfg_Task_gender_Bean genderBean = CfgManager.getCfg_Task_gender_Container().getValueByKey(genderTask.getModelId());
        List<Item> createItems = Item.createItems(genderBean.getRewards());
        if (!Manager.backpackManager.manager().addItems(player, createItems, ItemChangeReason.GenderTaskRewardGet, actionId)) {
            MessageUtils.notify_player(player, Notify.CHAT_SYS_BULL, MessageString.MainTaskNoBagCell);
            return false;
        }
        buildResTaskFinish(player, taskId, TaskConst.SUCCESS);

        genderTask.setFinish(true);
        if(!player.getOverGenderTaskIds().contains(taskId)){
            player.getOverGenderTaskIds().add(taskId);
        }
        checkChangeJobFinish(player);

        // 记录奖品日志
        writeFinishTaskLog(player, genderTask);
        Manager.biManager.getScript().biTask(player, genderTask.getModelId(), Task.GENDER_TASK, TaskConst.BI_FINISH, model.getTask_name(), task.getSubType(), 0);

        //尝试接取下一个任务
        int q_next_task = model.getPost_task_id();
        if (q_next_task > 0) {
            Manager.taskManager.deal().acceptTask(player, Task.GENDER_TASK, q_next_task, 0, false);
        }

        return true;
    }

    //检查现阶段转职任务是否完成
    private void checkChangeJobFinish(Player player) {
        GenderTask task = player.getCurGenderTask();
        if (task.getModelId() == 0) {
            return;
        }
        Cfg_Task_gender_Bean bean = CfgManager.getCfg_Task_gender_Container().getValueByKey(task.getModelId());
        if (bean == null) {
            return;
        }
        Cfg_Task_gender_Bean nextBean = CfgManager.getCfg_Task_gender_Container().getValueByKey(task.getModelId() + 1);
        if (nextBean != null && nextBean.getGenderClass() == bean.getGenderClass()) {
            return;
        }
        int genderClass = bean.getGenderClass();
        Cfg_Changejob_Bean changejobBean = CfgManager.getCfg_Changejob_Container().getValueByKey(genderClass + 1);
        if (changejobBean == null) {
            return;
        }
        player.setXsGrade(genderClass + 1);
        Manager.newFashionManager.deal().addFashionID(player, changejobBean.getModel_change());
        log.info("转职完成！ grade={}, player={}", player.getXsGrade(), player);

        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.XiSui, PlayerAttributeType.Task);
        Manager.controlManager.deal().operate(player, FunctionVariable.ChangeJob, 0);

        long actionId = IDConfigUtil.getLogId();
        List<Item> items = getItems(player, changejobBean.getChangejob_reward());
        if (Manager.backpackManager.manager().onHasAddSpaces(player, items) == 0) {
            Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.GenderStageFinishGet, actionId);
        } else {
            Manager.mailManager.sendMailToPlayer(player.getId(), 1, MessageString.System, MessageString.BAG_FULL_MAIL_TITLE, MessageString.BAG_FULL_MAIL_CONTENT, items, ItemChangeReason.GenderStageFinishGet);
        }

        //转职完成，强制给一点经验让玩家的经验值发生变化
        Manager.currencyManager.manager().onAddItemCoin(player, ItemCoinType.EXP, 1, ItemChangeReason.GenderStageFinishGet, actionId);

        //转职完成 发送公告
        //@todo 修改公告
        if (changejobBean.getNotice() != 0 || changejobBean.getChatchannel() != null) {
            MessageUtils.notify_allOnlinePlayer(changejobBean.getNotice(),changejobBean.getChatchannel(), MessageString.CHANGEJOB_NOTICE1,player.getId()+"", player.getName(), changejobBean.getChangejob_name(), Utils.makeUrlStr(MessageString.RECHARGE_AWARDGET_NOTICE));
        }


        PlayerMessage.ResGenderClassChange.Builder builder = PlayerMessage.ResGenderClassChange.newBuilder();
        builder.setGenderClass(player.getXsGrade());
        MessageUtils.send_to_player(player, PlayerMessage.ResGenderClassChange.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    private List<Item> getItems(Player player, ReadIntegerArrayEs itemBean) {
        List<Item> items = new ArrayList<>();
        for (ReadArray<Integer> l : itemBean.getValuees()) {
            if (l.get(3) != PlayerDefine.CAREER_All && l.get(3) != player.getCareer()) {
                continue;
            }
            items.addAll(Item.createItems(l.get(0), l.get(1), l.get(2) == 1));
        }
        return items;
    }

    private void buildResTaskFinish(Player player, int taskId, int state) {
        taskMessage.ResTaskFinish.Builder builder = taskMessage.ResTaskFinish.newBuilder();
        builder.setCurrentCount(0);
        builder.setType(Task.GENDER_TASK);
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

    /**
     * build开头的方法都是用来构建与客户端交互的协议体的
     */
    @Override
    public Object buildTaskInfo(Player player, Task task) {
        taskMessage.genderTaskInfo.Builder msg = taskMessage.genderTaskInfo.newBuilder();
        msg.setModelId(task.getModelId());
        List<Integer> targetModels = task.targetModels();
        for (Integer key : targetModels) {
            msg.setTarget(buildTaskAttribute(player, task, key));
        }
        if (!msg.hasTarget()) {
            msg.setTarget(Manager.taskManager.deal().buildNullTaskAttribute());
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
    public void taskFinishAfter(Player player, int currentModelId, int subType, int targetType) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void changeTask(Player player, Task task, boolean isRefresh) {
        if (!Manager.taskManager.deal().checkFunctionIsOpen(player, Task.GENDER_TASK)) {
            return;
        }
        taskMessage.ResGenderTaskChange.Builder msg = taskMessage.ResGenderTaskChange.newBuilder();
        msg.setGenderTask((taskMessage.genderTaskInfo) buildTaskInfo(player, task));
        MessageUtils.send_to_player(player, taskMessage.ResGenderTaskChange.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public ReadLongArrayEs getRewardArray(Player player, Task task, int rate) {
        Cfg_Task_gender_Bean genderBean = CfgManager.getCfg_Task_gender_Container().getValueByKey(task.getModelId());
        return genderBean.getRewards();
    }

    @Override
    public boolean checkTaskIsFinish(Player player, int taskId) {
        boolean isFinish = false;
        if (player.getOverGenderTaskIds().contains(taskId)) {
            isFinish = true;
        }
        taskMessage.ResTaskIsFinish.Builder builder = taskMessage.ResTaskIsFinish.newBuilder();
        builder.setIsFinish(isFinish);
        builder.setTaskId(taskId);
        builder.setType(Task.GENDER_TASK);
        MessageUtils.send_to_player(player, taskMessage.ResTaskIsFinish.MsgID.eMsgID_VALUE, builder.build().toByteArray());
        return isFinish;
    }

    @Override
    public void loadGenderTaskCache() {
        Manager.taskManager.getGenderTaskCache().clear();
        Cfg_Task_gender_Bean[] genderBeans = CfgManager.getCfg_Task_gender_Container().getValuees();
        for (Cfg_Task_gender_Bean cb : genderBeans) {
            int key = cb.getGender();
            List<Cfg_Task_gender_Bean> value;
            if (Manager.taskManager.getGenderTaskCache().containsKey(key)) {
                value = Manager.taskManager.getGenderTaskCache().get(key);
            } else {
                value = new ArrayList<>(8);
                Manager.taskManager.getGenderTaskCache().put(key, value);
            }
            value.add(cb);
        }
    }

    @Override
    public void levelUpDealGenderTask(Player player) {
        //转职任务没有功能开启就不检查
        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.TaskZhuanZhi)) {
            return;
        }
        GenderTask task = player.getCurGenderTask();
        if (task.getModelId() != 0 && !task.isFinish()) {
            return;
        }
        Cfg_Task_gender_Bean genderBean = CfgManager.getCfg_Task_gender_Container().getValueByKey(task.getModelId());
        for (Cfg_Task_gender_Bean bean : CfgManager.getCfg_Task_gender_Container().getValuees()) {
            if (genderBean != null && bean.getGenderClass() <= genderBean.getGenderClass()) {
                continue;
            }
            if (player.getLevel() < bean.getLevel()) {
                continue;
            }
            if (bean.getGender() != 9 && bean.getGender() != player.getCareer()) {
                continue;
            }
            acceptTask(player, bean.getId(), Task.GENDER_TASK, false, false);
            break;
        }
    }

    @Override
    public void oneKeyFinishAllGenderTask(Player player, boolean isGm) {
        GenderTask nowTask = player.getCurGenderTask();
        if (nowTask == null || nowTask.getModelId() == 0) {
            return;
        }
        Cfg_Task_gender_Bean bean = CfgManager.getCfg_Task_gender_Container().getValueByKey(nowTask.getModelId());
        if (bean == null) {
            log.error(TaskHelp.getPlayerInfo(player) + "转职任务配置不存在！" + nowTask.getModelId());
            return;
        }
        int startId = nowTask.isFinish() ? nowTask.getModelId() + 1 : nowTask.getModelId();
        int nextId = 0;
        //TODO 计算元宝消耗及完成的任务
        int needGold = 0;
        List<Integer> taskIdList = new ArrayList<>();
        while (true) {
            Cfg_Task_gender_Bean start = CfgManager.getCfg_Task_gender_Container().getValueByKey(startId);
            if (start == null) {
                break;
            }
            Cfg_Task_gender_Bean next = CfgManager.getCfg_Task_gender_Container().getValueByKey(start.getPost_task_id());
            if (next == null || next.getGenderClass() != start.getGenderClass() || player.getLevel() < next.getLevel()) {
                break;
            }
            nextId = start.getPost_task_id();
            needGold += start.getSkipCost();
            taskIdList.add(start.getId());
            startId += 1;
        }
        int oneKeyNeedCoinNum = Manager.playerManager.xiSuiScript().calcOneKeyCoinNum(player, player.getXsGrade());
        if (oneKeyNeedCoinNum < 0) {
            log.info("一键完成的任务 不满足条件！ player={}", player);
            return;
        }
        needGold += oneKeyNeedCoinNum;
        if (taskIdList.size() <= 0 || needGold <= 0) {
            log.info("没有可一键完成的任务！ player={}", player);
            return;
        }
        long actionId = IDConfigUtil.getLogId();
        boolean decFlag = Manager.currencyManager.manager().decGold(player, needGold, ItemChangeReason.GenderTaskOneKeyFinishDec, actionId);
        if (!decFlag) {
            MessageUtils.notify_player(player, Notify.NORMAL, MessageString.Not_Enough_Gold);
            log.info("仙人指路元宝不足！grade={} level={} player={}", player.getXsGrade(), player.getXsLevel(), player);
            return;
        }
        Manager.playerManager.xiSuiScript().oneKeySucess(player, player.getXsGrade());
        log.info("仙人指路一键完成，当前任务：{},{} player={}", nowTask.getModelId(), nowTask.isFinish(), player);
        if (!nowTask.isFinish()) {
            nowTask.setFinish(true);
            buildResTaskFinish(player, nowTask.getModelId(), TaskConst.SUCCESS);
            checkChangeJobFinish(player);
        }
        //TODO 发送任务奖励
        for (Integer cfgId : taskIdList) {
            Cfg_Task_gender_Bean genderBean = CfgManager.getCfg_Task_gender_Container().getValueByKey(cfgId);
            List<Item> createItems = Item.createItems(genderBean.getRewards());
            if (!Manager.backpackManager.manager().addItems(player, createItems, ItemChangeReason.GenderTaskRewardGet, actionId)) {
                Manager.mailManager.sendMailToPlayer(player.getId(), MailType.SysCommonRewardMail
                        , MessageString.System, MessageString.BAGISSPACETOMAIL
                        , MessageString.GetAwardNotEnoughSpaceContent, createItems, ItemChangeReason.GenderTaskRewardGet);
            }
            if (!player.getOverGenderTaskIds().contains(genderBean.getId())) {
                player.getOverGenderTaskIds().add(genderBean.getId());
            }
        }
        if (nextId > 0) {
            Manager.taskManager.deal().acceptTask(player, Task.GENDER_TASK, nextId, 0, false);
        }
    }

    @Override
    public void updateTaskProgress(Player player, Task task) {
        if (task.getTargetType() == Task.ACTION_TYPE_NEED_LEVEL) {
            task.setCurNum(player.getLevel());
        }
        if (task.getTargetType() == Task.ACTION_TYPE_VIP_STATE_BREAK) {
            task.setCurNum(player.getStateVip().getLv());
        } else if (task.getTargetType() == Task.ACTION_TYPE_FUNCTION) {
            Cfg_Task_gender_Bean bean = CfgManager.getCfg_Task_gender_Container().getValueByKey(task.getModelId());
            if (bean == null) {
                return;
            }
            int curNum = Manager.controlManager.deal().getFuncProgress(player, bean.getGoal_npc());
            task.setCurNum(curNum);
        }
    }

    @Override
    public void noticeDeleteTask(Player player, int taskType, int modelId) {

    }

    @Override
    public void writeAcceptTaskLog(Player player, Task task) {
        GenderTaskAcceptLog genderTaskAcceptLog = new GenderTaskAcceptLog();
        genderTaskAcceptLog.setLevel(player.getLevel());
        genderTaskAcceptLog.setRoleId(player.getId());
        genderTaskAcceptLog.setRoleName(player.getName());
        genderTaskAcceptLog.setTimes(TimeUtils.Time());
        genderTaskAcceptLog.setTaskId(task.getModelId());
        genderTaskAcceptLog.setServerId(player.getCurServerId());
        genderTaskAcceptLog.setUserId(player.getUserId());
        LogService.getInstance().execute(genderTaskAcceptLog);
        log.error(player.getName() + "(" + player.getId() + ")" + " 成功接取转职任务 " + player.getCurGenderTask().getModelId());
    }

    @Override
    public void writeFinishTaskLog(Player player, Task task) {
        GenderTaskFinishLog genderTaskFinishLog = new GenderTaskFinishLog();
        genderTaskFinishLog.setLevel(player.getLevel());

        StringBuilder reward = new StringBuilder();
        ReadLongArrayEs rewardArray = getRewardArray(player, task, 1);
        List<Item> createItems = Item.createItems(rewardArray);
        for (Item it : createItems) {
            String name = Manager.backpackManager.manager().getName(it.getItemModelId());
            reward.append(name).append("(").append(it.getNum()).append("),");
        }

        genderTaskFinishLog.setReward(reward.toString());
        genderTaskFinishLog.setRoleId(player.getId());
        genderTaskFinishLog.setRoleName(player.getName());
        genderTaskFinishLog.setServerId(player.getCurServerId());
        genderTaskFinishLog.setTaskId(task.getModelId());
        genderTaskFinishLog.setUserId(player.getUserId());
        LogService.getInstance().execute(genderTaskFinishLog);
    }
}
