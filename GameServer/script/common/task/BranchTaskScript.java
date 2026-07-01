package common.task;

import com.data.CfgManager;
import com.data.ItemChangeReason;
import com.data.MessageString;
import com.data.bean.Cfg_FunctionVariable_Bean;
import com.data.bean.Cfg_Task_branch_Bean;
import com.data.struct.ReadLongArrayEs;
import com.game.backpack.structs.Item;
import com.game.chat.structs.Notify;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.task.log.BranchTaskAcceptLog;
import com.game.task.log.BranchTaskFinishLog;
import com.game.task.script.IBranchScript;
import com.game.task.script.ITaskScript;
import com.game.task.structs.BranchTask;
import com.game.task.structs.Task;
import com.game.task.structs.TaskConst;
import com.game.utils.BIUtils;
import com.game.utils.MessageUtils;
import game.core.dblog.LogService;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.taskMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * 支线任务的处理
 *
 * @author admin
 */
public class BranchTaskScript implements ITaskScript, IBranchScript {

    private static final Logger log = LogManager.getLogger(BranchTaskScript.class);

    @Override
    public int getId() {
        return ScriptEnum.BranchTaskBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void computeTask(Player player, boolean isLogin, boolean isRefresh) {
        Iterator<BranchTask> it = player.getCurBranchTask().iterator();
        while (it.hasNext()) {
            BranchTask branchTask = it.next();
            Cfg_Task_branch_Bean bean = CfgManager.getCfg_Task_branch_Container().getValueByKey(branchTask.getModelId());
            if (bean == null) {
                continue;
            }

            //检查限时任务是否过期，过期则删除
            if (bean.getSubtype()==1 && isOverTime(bean)) {
                noticeDeleteTask(player, Task.BRANCH_TASK, branchTask.getModelId());
                it.remove();
                continue;
            }

            checkTaskProgress(player, branchTask, bean);
        }
    }

    @Override
    public void acceptTask(Player player, int modelId, int subType, boolean isLogin, boolean isRefresh) {
        //支线任务的接取：不需要特定的modelId，方式是扫描全部支线任务，把满足玩家条件的任务全部发给玩家
        //不同于主线任务，玩家的当前已接取支线任务是用列表来保存，而主线任务只是保存一个modelId
        if (player == null) {
            return;
        }
        Set<Integer> curIds = new HashSet<>();
        List<BranchTask> nowBranchTasks = player.getCurBranchTask();
        Iterator<BranchTask> iterator = nowBranchTasks.iterator();
        //把一些无效的支线任务给删除,和废弃一些任务
        while (iterator.hasNext()) {
            BranchTask branchTask = iterator.next();
            Cfg_Task_branch_Bean tempBean = CfgManager.getCfg_Task_branch_Container().getValueByKey(branchTask.getModelId());
            if (tempBean == null) {
                iterator.remove();
                continue;
            }
        }
        for (BranchTask bt : player.getCurBranchTask()) {
            curIds.add(bt.getModelId());
        }
        Cfg_Task_branch_Bean[] branchBeans = CfgManager.getCfg_Task_branch_Container().getValuees();
        for (Cfg_Task_branch_Bean bean : branchBeans) {
            if (curIds.contains(bean.getBranchId())) {
                continue;
            }

            if (player.getBranchOverList().contains(bean.getBranchId())) {
                continue;
            }

            if (isOverTime(bean)) {
                continue;
            }

            if (!Manager.controlManager.deal().checkFuncProgress(player, bean.getConditions_value())) {
                continue;
            }

            BranchTask task = new BranchTask();
            task.setModelId(bean.getBranchId());
            task.setOwnerId(player.getId());
            task.setTargetType(bean.getConditions_type());
            player.getCurBranchTask().add(task);

            updateTaskProgress(player, task);

            Manager.biManager.getScript().biTask(player, task.getModelId(), Task.BRANCH_TASK, TaskConst.BI_RECEIVE, bean.getName(), task.getSubType(),0);
            writeAcceptTaskLog(player, task);
//            log.error(TaskHelp.getPlayerInfo(player) + "支线任务：taskId：" + task.getModelId() + " 接取时状态： " + task.getCurNum());
            if (!isLogin) {
                changeTask(player, task, isRefresh);
            }
        }
    }

    @Override
    public boolean onFinishTask(Player player, int modelId, int taskId, int finishPer, boolean isGm, int subType) {
        BranchTask task = (BranchTask) Manager.taskManager.deal().getTaskByModelId(player, Task.BRANCH_TASK, taskId);
        if (task == null) {
            log.error("角色身上找不到请求的支线任务,角色 :" + player.getId() + "任务ID:" + taskId);
            buildResTaskFinish(player, taskId, TaskConst.TASK_NOT_FINISH);
            return false;
        }
        //检查其它事件
        Cfg_Task_branch_Bean bean = CfgManager.getCfg_Task_branch_Container().getValueByKey(taskId);
        if (bean == null) {
            log.error("没有找到支线任务的modeI数据:" + modelId);
            return false;
        }
        if (isOverTime(bean)) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.Task_Branch_Time);
            return false;
        }

        if (!task.checkFinish(true, player)) {
            return false;
        }

        Manager.biManager.getScript().biTask(player, task.getModelId(), Task.BRANCH_TASK, TaskConst.BI_FINISH, bean.getName(), task.getSubType(), 0);
//        log.error(player.getName() + "(" + player.getId() + ")" + " 完成支线任务id:" + task.getModelId());
        noResumeFinishTask(player, task, 1);
        //暂时没有支线任务来触发支线任务的情况
        try {
            Manager.taskManager.deal().acceptTask(player, Task.BRANCH_TASK, 0, 0, false);
        } catch (Exception e) {
            log.error(e, e);
        }
        return true;
    }

    public void noResumeFinishTask(Player player, BranchTask task, int finishType) {
        task.setNeedLevel(0);
        task.setNeedDegree(0);
        // 向客户端发送任务完成消息
        buildResTaskFinish(player, task.getModelId(), TaskConst.SUCCESS);

        ReadLongArrayEs rewardArray = getRewardArray(player, task, 1);
        List<Item> createItems = Item.createItems(rewardArray);
        sendRewards(player, task.getModelId(), createItems);

        player.getBranchOverList().add(task.getModelId());
        player.getCurBranchTask().remove(task);

        // 记日志
        try {
            writeFinishTaskLog(player, task);
        } catch (Exception e) {
            log.error(e, e);
        }
        taskFinishAfter(player, task.getModelId(), task.getSubType(), task.getTargetType());
    }

    @Override
    public void taskFinishAfter(Player player, int currentModelId, int subType, int targetType) {
//        if (targetType == Task.ACTION_TYPE_VIP_STATE_TASK) {
//            Manager.taskManager.action(player, Task.ACTION_TYPE_VIP_STATE_TASK, Task.ACTION_TYPE_VIP_STATE_TASK, 1);
//        }
    }

    @Override
    public void changeTask(Player player, Task task, boolean isRefresh) {
        if (!Manager.taskManager.deal().checkFunctionIsOpen(player, Task.BRANCH_TASK)) {
            return;
        }
        taskMessage.ResBranchTaskChang.Builder msg = taskMessage.ResBranchTaskChang.newBuilder();
        msg.setBranchTask((taskMessage.branchTaskInfo) buildTaskInfo(player, task));
        MessageUtils.send_to_player(player, taskMessage.ResBranchTaskChang.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    public void sendRewards(Player player, int taskModelID, List<Item> createItems) {
        // 物品加入背包
        long actionId = IDConfigUtil.getLogId();
        List<Item> spilthGoods = new ArrayList<>();
        for (Item item : createItems) {
            if (Manager.backpackManager.manager().onHasAddSpace(player, item)) {
                Manager.backpackManager.manager().addItem(player, item, ItemChangeReason.BranchTaskRewardGet, actionId);
            } else {
                spilthGoods.add(item);
            }
        }
        //无空间则发邮件
        if (spilthGoods.size() > 0) {
            Manager.taskManager.deal().sendRewardByMail(player, spilthGoods, ItemChangeReason.BranchTaskRewardGet, actionId);
        }
        // 记录奖品日志
        Manager.taskManager.deal().writeRewardLog(player, taskModelID, Task.BRANCH_TASK, createItems, 1);
    }

    private void buildResTaskFinish(Player player, int taskId, int state) {
        taskMessage.ResTaskFinish.Builder builder = taskMessage.ResTaskFinish.newBuilder();
        builder.setCurrentCount(0);
        builder.setType(Task.BRANCH_TASK);
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
    public Object buildTaskInfo(Player player, Task task) {
        taskMessage.branchTaskInfo.Builder msg = taskMessage.branchTaskInfo.newBuilder();
        msg.setModelId(task.getModelId());
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
    public boolean action(Player player, BranchTask task, int model, int num) {
        if (!task.targetModels().contains(model)) {
            return false;
        }
        int need = task.targetNum();

        int integer = task.getCurNum();
        if (integer >= need) {
            return true;//已经满足了
        }
        switch (model) {
            case 18:                //挑战副本
            case 37:                //升级活跃勋章
                integer = num;
                break;
            default:
                integer += num;
                break;
        }

        if (integer >= need) {
            integer = need;
            task.setFinish(true);
        }
        task.setCurNum(integer);
        return true;
    }

    @Override
    public ReadLongArrayEs getRewardArray(Player player, Task task, int rate) {
        String key = Task.BRANCH_TASK + "_" + task.getModelId() + "_" + player.getCamp() + "_" + player.getCareer() + "1";
        //直接检查如果存在缓存，就直接返回
        if (Manager.taskManager.getRewardStr().containsKey(key)) {
            return Manager.taskManager.getRewardStr().get(key);
        }
        Cfg_Task_branch_Bean branchBean = CfgManager.getCfg_Task_branch_Container().getValueByKey(task.getModelId());
        //物品奖励
        ReadLongArrayEs array = branchBean.getTask_reward();

        //装备强化（物品ID_数量_强化等级_附加属性类型|附加属性比例;物品ID_数量;物品ID_数量）
        //写入缓存
        Manager.taskManager.getRewardStr().put(key, array);
        return array;
    }

    @Override
    public boolean checkTaskIsFinish(Player player, int taskId) {
        boolean isFinish = false;
        if (player.getBranchOverList().contains(taskId)) {
            isFinish = true;
        }
        //返回任务查询请求的结果
        taskMessage.ResTaskIsFinish.Builder builder = taskMessage.ResTaskIsFinish.newBuilder();
        builder.setIsFinish(isFinish);
        builder.setTaskId(taskId);
        builder.setType(Task.BRANCH_TASK);
        MessageUtils.send_to_player(player, taskMessage.ResTaskIsFinish.MsgID.eMsgID_VALUE, builder.build().toByteArray());
        return isFinish;
    }

    @Override
    public void updateTaskProgress(Player player, Task task) {
        Cfg_Task_branch_Bean bean = CfgManager.getCfg_Task_branch_Container().getValueByKey(task.getModelId());
        if (bean == null) {
            return;
        }
        if (task.getTargetType() == Task.ACTION_TYPE_FUNCTION) {
            if (Manager.controlManager.deal().checkFuncProgress(player, bean.getDemand_value())) {
                task.setCurNum(task.targetNum());
            } else {
                task.setCurNum(Manager.controlManager.deal().getFuncProgress(player, bean.getDemand_value().get(0)));
            }
        }
        task.checkFinish(false, player);
    }

    private void checkTaskProgress(Player player, Task task, Cfg_Task_branch_Bean bean) {
        if (task.getTargetType() == Task.ACTION_TYPE_FUNCTION) {
            if (Manager.controlManager.deal().checkFuncProgress(player, bean.getDemand_value())) {
                task.setCurNum(task.targetNum());
            } else {
                int old = task.getCurNum();
                int now = Manager.controlManager.deal().getFuncProgress(player, bean.getDemand_value().get(0));
                task.setCurNum(now);
                if (old != now) {
                    Cfg_FunctionVariable_Bean fvbean = CfgManager.getCfg_FunctionVariable_Container().getValueByKey(bean.getDemand_value().get(0).get(0));
                    if (fvbean != null && fvbean.getTask() != 0) {
                        task.changeTask(player);
                    }
                }
            }
        }
        task.checkFinish(false, player);
    }

    @Override
    public void noticeDeleteTask(Player player, int taskType, int modelId) {
        taskMessage.ResTaskDelete.Builder builder = taskMessage.ResTaskDelete.newBuilder();
        builder.setType(taskType);
        builder.setModelId(modelId);
        MessageUtils.send_to_player(player, taskMessage.ResTaskDelete.MsgID.eMsgID_VALUE, builder.build().toByteArray());
        player.getBranchOverList().add(modelId);
    }

    private boolean isOverTime(Cfg_Task_branch_Bean bean) {
        return bean.getIfGono() > 0 && TimeUtils.getOpenServerDay() > bean.getIfGono();
    }

    @Override
    public void zeroTaskClean(Player player) {
        Iterator<BranchTask> it = player.getCurBranchTask().iterator();
        while (it.hasNext()) {
            BranchTask branchTask = it.next();
            Cfg_Task_branch_Bean bean = CfgManager.getCfg_Task_branch_Container().getValueByKey(branchTask.getModelId());
            if (bean == null) {
                continue;
            }

            //检查限时任务是否过期，过期则删除
            if (bean.getSubtype()==1 && isOverTime(bean)) {
                noticeDeleteTask(player, Task.BRANCH_TASK, branchTask.getModelId());
                it.remove();
                continue;
            }
        }
    }

    @Override
    public void writeAcceptTaskLog(Player player, Task task) {
        BranchTaskAcceptLog clog = new BranchTaskAcceptLog();
        try {
            Manager.taskManager.deal().fillTaskLogBaseInfo(player, clog, task);
            LogService.getInstance().execute(clog);
        } catch (Exception e) {
            log.error(e, e);
        }
    }

    @Override
    public void writeFinishTaskLog(Player player, Task task) {
        BranchTaskFinishLog clog = new BranchTaskFinishLog();
        try {
            Manager.taskManager.deal().fillTaskLogBaseInfo(player, clog, task);
            LogService.getInstance().execute(clog);
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
