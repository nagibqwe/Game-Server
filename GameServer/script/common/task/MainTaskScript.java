package common.task;

import com.data.CfgManager;
import com.data.FunctionVariable;
import com.data.ItemChangeReason;
import com.data.MessageString;
import com.data.bean.Cfg_Task_Bean;
import com.data.struct.*;
import com.game.backpack.structs.Item;
import com.game.backpack.structs.ItemCoinType;
import com.game.chat.structs.Notify;
import com.game.manager.Manager;
import com.game.map.structs.MapObject;
import com.game.npc.structs.Npc;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerAttributeType;
import com.game.script.structs.ScriptEnum;
import com.game.structs.ServerStr;
import com.game.task.log.MainTaskLog;
import com.game.task.script.ITaskScript;
import com.game.task.structs.*;
import com.game.utils.BIUtils;
import com.game.utils.MessageUtils;
import com.game.utils.Utils;
import game.core.dblog.LogService;
import game.core.util.IDConfigUtil;
import game.core.util.JsonUtils;
import game.message.CopyMapMessage;
import game.message.taskMessage;
import game.message.taskMessage.mainTaskInfo;
import org.apache.commons.lang.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 主线任务的脚本
 *
 * @author admin
 */
public class MainTaskScript implements ITaskScript {

    private static final Logger log = LogManager.getLogger(MainTaskScript.class);

    @Override
    public int getId() {
        return ScriptEnum.MainTaskBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }


    @Override
    public void computeTask(Player player, boolean isLogin, boolean isRefresh) {
        // 第一次或者是主线任务已经做完
        if (player.getCurMainTasks().size() <= 0) {
            if (player.getOverMainTaskIDs().isEmpty()) {
                //第一次上线
                resetTask(player);
            } else {
                // 检查是否有异常
                int length = player.getOverMainTaskIDs().size();
                int overRecentlyTaskId = player.getOverMainTaskIDs().get(length - 1);
                Cfg_Task_Bean tempMainBean = CfgManager.getCfg_Task_Container().getValueByKey(overRecentlyTaskId);
                if (tempMainBean == null) {
                    log.info("估计是老账户，所以没有了任务ID，所以重设任务");
                    resetTask(player);
                    return;
                }

                if (tempMainBean.getPost_task_id() == 0) {
                    log.info("此玩家的主线任务已经做完 玩家信息：" + TaskHelp.getPlayerInfo(player));
                } else {
                    //条件满足就接任务
                    if (!Manager.controlManager.deal().checkFuncProgress(player, tempMainBean.getConditions_value())) {
                        return;
                    }
                    acceptTask(player, tempMainBean.getPost_task_id(), 0, true, isRefresh);
                }
            }
        }
        //检查当前主线任务还在不在配置表中
        if (player.getCurMainTasks().size() > 0) {
            MainTask mainTask = player.getCurMainTasks().get(0);
            if (mainTask != null) {
                Cfg_Task_Bean q_task_mainBean = CfgManager.getCfg_Task_Container().getValueByKey(mainTask.getModelId());
                if (q_task_mainBean == null) {
                    log.error("任务模型找不到重置任务：原因你的角色是很早之前创建的，目前配置表已经没有这个ID了：" + mainTask.getModelId());
                    player.getCurMainTasks().clear();
                    player.setCurMainTaskId(0);
                    player.getOverMainTaskIDs().clear();
                    resetTask(player);
                    return;
                }
            }
        }
    }

    @Override
    public void acceptTask(Player player, int modelId, int subType, boolean isLogin, boolean isRefresh) {
        if (player == null) {
            return;
        }
        Task taskByModelId = Manager.taskManager.deal().getTaskByModelId(player, Task.MAIN_TASK, modelId);
        if (taskByModelId != null) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.TASK_NOT_REPLAY);
            return;
        }
        if (player.getCurMainTasks().size() > 0) {
            log.error("玩家身上是有主线任务的，任务id：" + player.getCurMainTasks().get(0).getModelId());
            return;
        }
        Cfg_Task_Bean taskModel = CfgManager.getCfg_Task_Container().getValueByKey(modelId);
        if (taskModel == null) {
            log.error("当前主线任务id在主线配置表中找不到数据：" + modelId);
            return;
        }
        if (player.overMainTask(modelId)) {
            log.error("此主线任务已经在完成列表中了，尝试接取其下一个任务(" + modelId + ")");
            Manager.taskManager.deal().acceptTask(player, Task.MAIN_TASK, taskModel.getPost_task_id(), 0, isLogin);
            return;
        }
        MainTask task = new MainTask();
        task.initTask(player, modelId);
        updateTaskProgress(player, task);
        Manager.taskManager.deal().fullTaskOptionParam(task, taskModel.getTarget(), taskModel.getTask_x_z());
        Manager.biManager.getScript().biTask(player, task.getModelId(), Task.MAIN_TASK, TaskConst.BI_RECEIVE, taskModel.getTask_name(), task.getSubType(), 0);
        //考虑到第一次发送主线，就不需要change
        if (!player.isIsFirstGetTask()) {
            changeTask(player, task, isRefresh);
        }
        //跳跃传送
        try {
            if (taskModel.getFlyteleport() > 0) {
                Manager.mapManager.transport().onJumpFly(player, taskModel.getFlyteleport());
            }
        } catch (Exception e) {
            log.error("玩家完成任务后的传送出问题了，飞行id：" + taskModel.getFlyteleport(), e);
        }

        player.setIsFirstGetTask(false);

        log.info("玩家[{}] 领取任务id={}", player.getName(), player.getCurMainTasks().get(0).getModelId());

        //检查是否已经完成任务
        if (task.getTaskType() != Task.ACTION_TYPE_NPC_TALK) {
            int num = 0;
            if (task.getTaskType() == Task.ACTION_TYPE_NEED_LEVEL) {
                num = player.getLevel();
            }
            if (task.getTaskType() == Task.ACTION_TYPE_VIP_STATE_BREAK) {
                num = player.getStateVip().getLv();
            }
            Manager.taskManager.deal().action(player, (short) task.getTaskType(), task.targetModels().get(0), num);
        }

        //因为npc 和 怪物隐藏的问题，所以接取任务之后要及时刷新一下
        Manager.taskManager.deal().taskChangeRoundSync(player, taskModel.getShow_npc(), taskModel.getShow_monster(), taskModel.getShow_gather());
    }

    @Override
    public boolean onFinishTask(Player player, int taskModelID, int taskId, int finishPer, boolean isGm, int subType) {
        Task task = Manager.taskManager.deal().getTaskByModelId(player, Task.MAIN_TASK, taskId);
        if (task == null) {
            log.error(TaskHelp.getLog(player, TaskTypeEnum.MAIN_TASK.getType(), TaskHelp.NoFind, taskId) + " 当前主线Id:" + player.getCurMainTaskId() + "当前主线个数：" + player.getCurMainTasks().size());
            buildResTaskFinish(player, taskId, TaskConst.PLAYER_NO_THIS_TASK);
            return false;
        }
        MainTask maintask = (MainTask) task;
        Cfg_Task_Bean model = CfgManager.getCfg_Task_Container().getValueByKey(maintask.getModelId());
        if (model == null) {
            log.error("主线任务在主线任务配置表中找不到,id:" + maintask.getModelId());
            return false;
        }
        //如果是GM则不检查这个任务处理
        if (!isGm) {
            if (model.getAuto_commit() == 1) {
                if (model.getType() == Task.ACTION_TYPE_NPC_TALK) {
                    Integer[] items = model.getTarget().getValue();
                    int q_endnpc = items[0];
                    if (q_endnpc == 0) {
                        log.error("该任务需要NPC提交但未配置NPC模型任务ID=" + taskId);
                        return false;
                    }
                }
                if (model.getEndpath() != null && model.getEndpath().size() >= 2) {
                    Integer[] endPath = model.getEndpath().getValue();
                    Integer mapId = endPath[0];
                    Integer npcId = endPath[1];

                    MapObject map = Manager.mapManager.getMap(player.gainMapId());
                    if (map == null) {
                        log.error(TaskHelp.getPlayerInfo(player) + "提交主线任务：" + taskId + " 时，玩家地图为空！");
                        buildResTaskFinish(player, taskId, TaskConst.MAP_IS_NULL);
                        return false;
                    }
                    //如果地图不符合，则需要到目标地图
                    if (map.getMapModelId() != mapId) {
                        MessageUtils.notify_player(player, Notify.ERROR, MessageString.TASK_TO_NPC);
                        log.error("策划配置主线任务" + task.getModelId() + "的地图提交任务的ID是" + mapId + " npcId" + npcId + " 有错误！");
                        return false;
                    }
                    Npc npc = map.getNpcs().get((long) npcId);
                    double distance = Utils.getDistance(npc.gainCurPos(), player.gainCurPos());
                    if (distance > 10) {
                        MessageUtils.notify_player(player, Notify.ERROR, MessageString.TASK_TO_NPC);
                        log.error(TaskHelp.getPlayerInfo(player) + ":" + task.getModelId() + "的主线提交任务的ID是" + mapId + " npcId" + npcId);
                        buildResTaskFinish(player, taskId, TaskConst.TOO_FAR_WITH_NPC);
                        return false;
                    }
                }
            }
            if (player.getLevel() < task.getNeedLevel()) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.TASK_NEED_LEVEL_START, ServerStr.getLevelNameHighStr(task.getNeedLevel()), ServerStr.getLevelNameSlowStr(task.getNeedLevel()));
                buildResTaskFinish(player, taskId, TaskConst.TASK_NOT_FINISH);
//                log.error("检查level不通过 taskId={}", taskId);
                return false;
            }

            if (!task.checkFinish(true, player)) {
                //检查不通过
                buildResTaskFinish(player, taskId, TaskConst.TASK_NOT_FINISH);
//                log.error("检查不通过 taskId={}", taskId);
                return false;
            }
        }

        ReadLongArrayEs rewardArray = getRewardArray(player, task, 1);
        List<Item> createItems = Item.createItems(rewardArray);
        //发奖励
        try {
            sendRewards(player, taskId, createItems);
        } catch (Exception e) {
            log.error("主线任务发奖励出现异常", e);
        }
        buildResTaskFinish(player, taskId, TaskConst.SUCCESS);
        //添加完成的任务ID值
        player.getOverMainTaskIDs().add(taskId);
        player.getCurMainTasks().remove(maintask);
        player.setCurMainTaskId(0);

        MainTaskLog mainTaskLog = new MainTaskLog();
        try {
            mainTaskLog.setRoleId(player.getId());
            mainTaskLog.setFinishmodelId(taskId);
            mainTaskLog.setFinishtaskInfo(JsonUtils.toJSONString(this));
            mainTaskLog.setFinishlevel(player.getLevel());
            mainTaskLog.setFinishonlinetime(player.getAccunonlinetime());
            mainTaskLog.setFshmoney((int) Manager.currencyManager.manager().getCurrencyNum(player, ItemCoinType.BindMoney));
            mainTaskLog.setUserId(player.getUserId());
            mainTaskLog.setPlatformName(player.getPlatformName());
            mainTaskLog.setRolename(player.getName());
            mainTaskLog.setSid(player.getCreateServerId());
        } catch (Exception e) {
            log.error(e, e);
        }
        log.info("玩家[{}] 完成任务id={}", player.getName(), maintask.getModelId());

        Manager.biManager.getScript().biTask(player, task.getModelId(), Task.MAIN_TASK, TaskConst.BI_FINISH, model.getTask_name(), task.getSubType(), 0);
        //检查功能开放
        taskFinishAfter(player, task.getModelId(), task.getSubType(), task.getTargetType());
        try {
            if (player.getCurMainTasks().size() > 0) {
                MainTask accept = player.getCurMainTasks().get(0);
                if (accept != null) {
                    mainTaskLog.setAcceptmodelId(accept.getModelId());
                    mainTaskLog.setAcceptlevel(player.getLevel());
                    mainTaskLog.setAcceptonlinetime(player.getAccunonlinetime());
                    mainTaskLog.setAccmoney((int) Manager.currencyManager.manager().getCurrencyNum(player, ItemCoinType.BindMoney));
                }
            }
            LogService.getInstance().execute(mainTaskLog);
        } catch (Exception e) {
            log.error(e, e);
        }
        return true;
    }

    @Override
    public void taskFinishAfter(Player player, int taskModelId, int subType, int targetType) {
        //检查宠物激活
        Manager.petManager.deal().finishTaskGetPet(player, taskModelId);
        //检测功能开启
        Manager.controlManager.deal().operate(player, FunctionVariable.PlayerTaskID, taskModelId);

        Manager.newFashionManager.deal().activeNewFashion(player, FunctionVariable.PlayerTaskID, taskModelId);

        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.Task); //计算任务加成属性

        checkCanActiveSkill(player, taskModelId);

        addBuff(player, taskModelId);
        //检查特殊操作
        dealSpecialAction(player, targetType, taskModelId);
    }

    private void resetTask(Player player) {
        int currTaskID = player.getCurMainTaskId();
        //只有第一次的时候这个 currTaskId 才会为0
        if (currTaskID < 1) {
            //重新接任务
            acceptTask(player, Manager.taskManager.deal().getFirstMainTaskId(), 0, true, false);
            return;
        }

        Cfg_Task_Bean bean = CfgManager.getCfg_Task_Container().getValueByKey(currTaskID);
        if (bean == null) {
            log.error("没有找到当前主线任务 的任务 ID：" + currTaskID);
            return;
        }
        if (bean.getPost_task_id() < 1) {
            log.error("当前主线任务 ID：" + currTaskID + ", 没有配置 下一个主线任务");
            return;
        }

        int nextId = bean.getPost_task_id();
        acceptTask(player, nextId, 0, true, false);
    }


    private void sendRewards(Player player, int taskModelID, List<Item> createItems) {
        // 主线任务的奖励背包满时改为走邮件
        long actionId = IDConfigUtil.getLogId();
        List<Item> spilthGoods = new ArrayList<>();
        for (Item item : createItems) {
            if (Manager.backpackManager.manager().onHasAddSpace(player, item)) {
                Manager.backpackManager.manager().addItem(player, item, ItemChangeReason.MainTaskRewardGet, actionId);
            } else {
                spilthGoods.add(item);
            }
        }
        //无空间则发邮件
        if (spilthGoods.size() > 0) {
            Manager.taskManager.deal().sendRewardByMail(player, spilthGoods, ItemChangeReason.MainTaskRewardGet, actionId);
        }

        // 记录奖品日志
        Manager.taskManager.deal().writeRewardLog(player, taskModelID, Task.MAIN_TASK, createItems, 1);
    }

    private void buildResTaskFinish(Player player, int taskId, int state) {
        taskMessage.ResTaskFinish.Builder builder = taskMessage.ResTaskFinish.newBuilder();
        builder.setCurrentCount(0);
        builder.setType(Task.MAIN_TASK);
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

    @Override
    public Object buildTaskInfo(Player player, Task tt) {
        MainTask task = (MainTask) tt;
        mainTaskInfo.Builder b = mainTaskInfo.newBuilder();
        if (task == null) {
            return b;
        }
        b.setModelId(task.getModelId());
        List<Integer> keySet = task.targetModels();
        for (Integer key : keySet) {
            b.setUseItems(buildTaskAttribute(player, task, key));
        }

        //发送等级相关的数据
        if (task.getTaskType() == Task.ACTION_TYPE_NEED_LEVEL || task.getTaskType() == Task.ACTION_TYPE_VIP_STATE_BREAK) {
            b.setUseItems(buildTaskAttribute(player, task, 0));
        }
        if (!b.hasUseItems()) {
            b.setUseItems(Manager.taskManager.deal().buildNullTaskAttribute());
        }
        //添加完成列表
        return b.build();
    }

    private taskMessage.taskAttribute.Builder buildTaskAttribute(Player player, Task task, int model) {
        taskMessage.taskAttribute.Builder sb = taskMessage.taskAttribute.newBuilder();
        sb.setModel(model);
        sb.setNum(task.getCurTaskProgress(player));
        sb.setMapId(task.getTargetMap());
        Manager.taskManager.deal().buildTaskAttributeSame(task, sb);
        return sb;
    }

    private void dealSpecialAction(Player player, int targetType, int taskModelId) {
        if (targetType == Task.ACTION_TYPE_PLANE_FABAO) {//位面演示副本特殊处理
            MapObject map = Manager.mapManager.getMap(player.gainMapId());
            if (map.getMapModelId() == targetModels(taskModelId).get(2)) {//还在副本中则请求踢出副本
                CopyMapMessage.ResCopyMapBitFinish.Builder builder = CopyMapMessage.ResCopyMapBitFinish.newBuilder();
                MessageUtils.send_to_player(player, CopyMapMessage.ResCopyMapBitFinish.MsgID.eMsgID_VALUE, builder.build().toByteArray());
                //设置地图销毁标识
                map.setStop(true);
            }
        }
    }

    private ReadIntegerArray targetModels(int modelId) {
        Cfg_Task_Bean model = CfgManager.getCfg_Task_Container().getValueByKey(modelId);
        if (model == null) {
            log.error(modelId + "主线任务模型找不着 model =");
            return null;
        }
        return model.getTarget();
    }

    /**
     * 检查能够激活技能
     *
     * @param taskModelId
     */
    private void checkCanActiveSkill(Player player, int taskModelId) {
        Cfg_Task_Bean taskmodel = CfgManager.getCfg_Task_Container().getValueByKey(taskModelId);
        if (taskmodel == null) {
            log.error("当前主线任务id在主线配置表中找不到数据：" + taskModelId);
            return;
        }

        ReadIntegerArrayEs array = taskmodel.getSet_act_skill();
        int career = player.getCareer();
        StringBuilder sb = new StringBuilder();
        //激活这一天徽章所带的技能
        if (!array.isEmpty()) {
            for (ReadArray i : array.getValuees()) {
                if ((int) i.get(0) == career) {
                    Manager.skillManager.addSkill(player, (int) i.get(1));
                    sb.append(i.get(1)).append(",");
                }
            }
        }
    }

    @Override
    public void changeTask(Player player, Task task, boolean isRefresh) {
        taskMessage.ResMainTaskChange.Builder msg = taskMessage.ResMainTaskChange.newBuilder();
        msg.setMainTask((mainTaskInfo) buildTaskInfo(player, task));
        MessageUtils.send_to_player(player, taskMessage.ResMainTaskChange.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public ReadLongArrayEs getRewardArray(Player player, Task task, int rate) {
        Cfg_Task_Bean model = CfgManager.getCfg_Task_Container().getValueByKey(task.getModelId());
        // 奖励
        ReadArray<Long>[] readArrays = new ReadArray[]{};
        readArrays = (ReadArray<Long>[]) ArrayUtils.addAll(readArrays, model.getRewards().getValuees());
        //装备奖励(根据职业奖励的)

        ReadLongArrayEs equipArray = model.getEquip();
        for (ReadArray<Long> tempArray : equipArray.getValuees()) {
            if (tempArray.get(0) == player.getCareer()) {
                Long[] values = Arrays.copyOfRange(tempArray.getValue(), 1, tempArray.getValue().length);
                ReadArray<Long>[] valuees = new ReadLongArray[1];
                ReadLongArray tempA = new ReadLongArray(values);
                valuees[0] = tempA;
                readArrays = (ReadArray<Long>[]) ArrayUtils.addAll(readArrays, valuees);
            }
        }
        return new ReadLongArrayEs(readArrays);
    }

    @Override
    public boolean checkTaskIsFinish(Player player, int taskId) {
        boolean isFinish = false;
        if (player.getOverMainTaskIDs().contains(taskId)) {
            isFinish = true;
        }
        //返回任务查询请求的结果
        taskMessage.ResTaskIsFinish.Builder builder = taskMessage.ResTaskIsFinish.newBuilder();
        builder.setIsFinish(isFinish);
        builder.setTaskId(taskId);
        builder.setType(Task.MAIN_TASK);
        MessageUtils.send_to_player(player, taskMessage.ResTaskIsFinish.MsgID.eMsgID_VALUE, builder.build().toByteArray());
        return isFinish;
    }

    @Override
    public void updateTaskProgress(Player player, Task task) {
        if (task.getTargetType() == Task.ACTION_TYPE_NEED_LEVEL) {
            task.setCurNum(player.getLevel());
        }
        if (task.getTargetType() == Task.ACTION_TYPE_VIP_STATE_BREAK) {
            task.setCurNum(player.getStateVip().getLv());
        }
    }

    /**
     * 加buff
     */
    private void addBuff(Player player, int id) {
        Cfg_Task_Bean bean = CfgManager.getCfg_Task_Container().getValueByKey(id);
        if (bean == null) {
            log.error("Cfg_Main_taskBean无法找到数据，id = " + id);
            return;
        }
        int buffId = bean.getBuff();
        if (buffId <= 0) {
            return;
        }
        Manager.buffManager.deal().onAddBuff(player, player, buffId);
    }

    @Override
    public void noticeDeleteTask(Player player, int taskType, int modelId) {

    }

    @Override
    public void writeAcceptTaskLog(Player player, Task task) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void writeFinishTaskLog(Player player, Task task) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

