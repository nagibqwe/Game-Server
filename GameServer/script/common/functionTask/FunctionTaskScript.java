package common.functionTask;

import com.data.FunctionVariable;
import com.data.ItemChangeReason;
import com.data.bean.Cfg_Today_function_Bean;
import com.data.bean.Cfg_Today_function_recharge_Bean;
import com.data.bean.Cfg_Today_function_task_Bean;
import com.data.container.Cfg_Today_function_Container;
import com.data.container.Cfg_Today_function_recharge_Container;
import com.data.container.Cfg_Today_function_task_Container;
import com.data.struct.ReadIntegerArray;
import com.game.backpack.structs.Item;
import com.game.bi.struct.BIActiityTypeEnum;
import com.game.chat.structs.Notify;
import com.game.functionTask.script.IFunctionTaskScript;
import com.game.functionTask.struct.FunctionTask;
import com.game.functionTask.struct.FunctionTaskData;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.recharge.structs.RechargeItemInfo;
import com.game.script.structs.ScriptEnum;
import com.game.task.structs.Task;
import com.game.task.structs.TaskConst;
import com.game.utils.MessageUtils;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.CommonMessage;
import game.message.FunctionTaskMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * @Auther: gouzhongliang
 * @Date: 2021/9/28 10:16
 */
public class FunctionTaskScript implements IFunctionTaskScript {

    private Logger log = LogManager.getLogger(FunctionTaskScript.class);

    @Override
    public int getId() {
        return ScriptEnum.FunctionTaskScript;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }

    /**
     * 玩家上线
     */
    public void online(Player player){
        try{
            //服务器当前开服天数
            int openDays = TimeUtils.getOpenServerDay();

            int playerDay = player.getFunctionTaskData().getDays();
            if(playerDay != openDays){
                initPlayerData(player);
            }

            //登录处理
            Manager.controlManager.operate(player, FunctionVariable.Daily_Log_In, 1);

            FunctionTaskMessage.ResAllFunctionTask.Builder msg = FunctionTaskMessage.ResAllFunctionTask.newBuilder();

            List<Integer> ids = Manager.functionTaskManager.getTaskIds();
            for(Integer id : ids){
                FunctionTask t = player.getFunctionTaskData().getTasks().get(id);
                if(t == null){
                    continue;
                }
                Cfg_Today_function_task_Bean bean = Cfg_Today_function_task_Container.GetInstance().getValueByKey(id);
                if(bean == null){
                    continue;
                }
                FunctionTaskMessage.FunctionTask.Builder task = FunctionTaskMessage.FunctionTask.newBuilder();
                task.setId(bean.getId());
                task.setNum(t.getNum());
                if(bean.getVariable() == null || bean.getVariable().size() == 0){
                    continue;
                }
                task.setMaxNum(bean.getVariable().get(bean.getVariable().size() - 1));
                task.setGet(t.isGet());
                msg.addTasks(task);
            }

            Cfg_Today_function_recharge_Bean rechargeBean = Cfg_Today_function_recharge_Container.GetInstance().getValueByKey(Manager.functionTaskManager.getRechargeId());
            if(rechargeBean != null){
                FunctionTaskMessage.FunctionRechargeTask.Builder recharge = FunctionTaskMessage.FunctionRechargeTask.newBuilder();
                recharge.setGet(player.getFunctionTaskData().getRechargeIds().contains(rechargeBean.getId()));
                recharge.setRechargeid(rechargeBean.getRechargeID());
                recharge.setNum(rechargeBean.getDiscount());
                RechargeItemInfo cfg = Manager.rechargeManager.getRechargeItemInfoMap().get(rechargeBean.getRechargeID());
                if(cfg != null){
                    List<Item> items = Item.createItems(cfg.getGoods_reward());
                    for(Item it : items){
                        CommonMessage.ShowItemInfo.Builder item = CommonMessage.ShowItemInfo.newBuilder();
                        item.setModelId(it.getItemModelId());
                        item.setCount(it.getNum());
                        recharge.addRewards(item);
                    }
                    List<Item> its = Item.createItems(player.getCareer(), rechargeBean.getReward(), 1);
                    for(Item it : its){
                        CommonMessage.ShowItemInfo.Builder item = CommonMessage.ShowItemInfo.newBuilder();
                        item.setModelId(it.getItemModelId());
                        item.setCount(it.getNum());
                        recharge.addRewards(item);
                    }
                    msg.setRechargeTask(recharge);
                }
            }
            MessageUtils.send_to_player(player, FunctionTaskMessage.ResAllFunctionTask.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            log.info("functionTaskOnline,player:"+player.getName(),"msg:"+msg.build().toString());
        }catch (Exception e){
            log.error("玩家上线", e);
        }
    }

    private void initPlayerData(Player player) {
        int openDays = TimeUtils.getOpenServerDay();
        FunctionTaskData data = player.getFunctionTaskData();
        data.setDays(openDays);
        Map<Integer, FunctionTask> tasks = new HashMap<>();
        for(Integer id : Manager.functionTaskManager.getTaskIds()){
            FunctionTask t = new FunctionTask();
            t.setGet(false);
            t.setId(id);
            t.setComplete(false);
            Cfg_Today_function_task_Bean bean = Cfg_Today_function_task_Container.GetInstance().getValueByKey(t.getId());
            t.setNum(0);
            //唯一任务，继承前一天的数据
            if(bean.getType() == 2){
                FunctionTask old = data.getTasks().get(t.getId());
                if(old != null){
                    t.setComplete(old.isComplete());
                    t.setGet(old.isGet());
                    t.setNum(old.getNum());
                }
            }
            tasks.put(id, t);

            if(bean.getIf_after_open() == 1){
                //检测任务完成情况
                int progress = Manager.controlManager.deal().getFuncProgress(player, bean.getVariable());
                t.setNum(progress);
                if(t.getNum() >= bean.getVariable().get(bean.getVariable().size() - 1)){
                    t.setComplete(true);
                    Manager.biManager.getScript().biTask(player, t.getId(), Task.FunctionTask, TaskConst.BI_FINISH, "", 0, 0);
                }
            }

        }
        data.setTasks(tasks);
    }

    @Override
    public void init() {
        try{
            //服务器当前开服天数
            int openDays = TimeUtils.getOpenServerDay();
            //当前是周几
            int weenkDay = TimeUtils.getDayOfWeek(TimeUtils.Time());
            //今天开启的活动
            List<Integer> open = new ArrayList<>();
            //获取今天的任务
            for(Cfg_Today_function_Bean bean : Cfg_Today_function_Container.GetInstance().getValuees()){
                ReadIntegerArray day = bean.getOpen_day();
                if(day.size() >= 2 && day.get(0) <= openDays && day.get(1) >= openDays){
                    ReadIntegerArray week = bean.getWeek_day();
                    if(week == null || week.size() == 0){
                        open.add(bean.getId());
                    }else if(week.contains(weenkDay)){
                        open.add(bean.getId());
                    }
                }
            }

            //今天的任务
            List<Integer> ts = new ArrayList<>();
            for(Cfg_Today_function_task_Bean task : Cfg_Today_function_task_Container.GetInstance().getValuees()){
                for(Integer id : task.getToday_functionID().getValue()){
                    if(open.contains(id)){
                        ts.add(task.getId());
                        continue;
                    }
                }
            }

            Manager.functionTaskManager.setTaskIds(ts);

            //今天的充值任务
            int rechargeId = 0;
            for(Cfg_Today_function_recharge_Bean recharge : Cfg_Today_function_recharge_Container.GetInstance().getValuees()){
                ReadIntegerArray days = recharge.getOpen_day();
                if(days.size() == 2 && days.get(0) <= openDays && days.get(1) >= openDays){
                    rechargeId = recharge.getId();
                    break;
                }
            }
            Manager.functionTaskManager.setRechargeId(rechargeId);
        }catch(Exception e){
            log.error("初始化功能任务失败", e);
        }
    }

    @Override
    public void getAward(Player player, int id) {
        FunctionTask task = player.getFunctionTaskData().getTasks().get(id);
        if(task == null){
            log.error("任务不存在");
            return;
        }
        if(task.isGet()){
            log.error("奖励已领取");
            return;
        }
        Cfg_Today_function_task_Bean bean = Cfg_Today_function_task_Container.GetInstance().getValueByKey(id);
        if(bean == null){
            log.error("配置不存在");
            return;
        }
        int v = bean.getVariable().get(bean.getVariable().size() - 1);
        if(task.getNum() >= v){//完成了
            List<Item> items = Item.createItems(player.getCareer(), bean.getReward(), 1);
            int msId = Manager.backpackManager.manager().onHasAddSpaces(player, items);
            if (msId != 0) {
                log.error("背包空间不足！");
                MessageUtils.notify_player(player, Notify.ERROR, msId);
                return;
            }
            task.setGet(true);
            Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.FunctionTaskGet, IDConfigUtil.getLogId());

            //发送更新信息
            FunctionTaskMessage.FunctionTask.Builder t = FunctionTaskMessage.FunctionTask.newBuilder();
            t.setId(bean.getId());
            t.setGet(task.isGet());
            t.setNum(task.getNum());
            t.setMaxNum(v);
            FunctionTaskMessage.ResFunctionTaskUpdate.Builder res = FunctionTaskMessage.ResFunctionTaskUpdate.newBuilder();
            res.setTasks(t);
            MessageUtils.send_to_player(player, FunctionTaskMessage.ResFunctionTaskUpdate.MsgID.eMsgID_VALUE, res.build().toByteArray());
        }
    }

    @Override
    public void onRefreshUpProgress(Player player, int change, int... types) {
        try{
            int type = types[0];
            //获取进度值
            for(FunctionTask task : player.getFunctionTaskData().getTasks().values()){
                if(!task.isComplete()){
                    Cfg_Today_function_task_Bean bean = Cfg_Today_function_task_Container.GetInstance().getValueByKey(task.getId());
                    if (bean == null){
                        continue;
                    }
                    ReadIntegerArray variable = bean.getVariable();
                    if(type == variable.get(0)){
                        int need = 0;
                        if(bean.getIf_after_open() == 1){
                            //检测任务完成情况
                            int progress = Manager.controlManager.deal().getFuncProgress(player, bean.getVariable());
                            task.setNum(progress);
                            need = variable.get(bean.getVariable().size() - 1);
                        }else{
                            if(variable.size() == 3){
                                if(types[1] == variable.get(1)){
                                    task.setNum(task.getNum() + change);
                                    need = variable.get(2);
                                }
                            }else{
                                task.setNum(task.getNum() + change);
                                need = variable.get(1);
                            }
                        }
                        if(need > 0){
                            if(task.getNum() >= need){
                                task.setComplete(true);
                            }

                            FunctionTaskMessage.FunctionTask.Builder t = FunctionTaskMessage.FunctionTask.newBuilder();
                            t.setId(bean.getId());
                            t.setGet(false);
                            t.setNum(task.getNum());
                            t.setMaxNum(need);
                            FunctionTaskMessage.ResFunctionTaskUpdate.Builder res = FunctionTaskMessage.ResFunctionTaskUpdate.newBuilder();
                            res.setTasks(t);
                            MessageUtils.send_to_player(player, FunctionTaskMessage.ResFunctionTaskUpdate.MsgID.eMsgID_VALUE, res.build().toByteArray());
                        }

                        if(task.isComplete()){
                            Manager.biManager.getScript().biTask(player, task.getId(), Task.FunctionTask, TaskConst.BI_FINISH, "", 0, 0);
                        }
                    }
                }
            }
        }catch(Exception e){
            log.error("更新功能任务进度失败",e);
        }
    }

    @Override
    public void onRecharge(Player player, int goodsId) {
        try{
            int rechargeId = Manager.functionTaskManager.getRechargeId();
            if(player.getFunctionTaskData().getRechargeIds().contains(rechargeId)){
                log.warn("玩家已领取奖励");
                return;
            }
            Cfg_Today_function_recharge_Bean bean = Cfg_Today_function_recharge_Container.GetInstance().getValueByKey(rechargeId);
            if(goodsId != bean.getRechargeID()){
                return;
            }

            List<Item> items = Item.createItems(player.getCareer(), bean.getReward(), 1);

            int msId = Manager.backpackManager.manager().onHasAddSpaces(player, items);
            if (msId != 0) {
                log.error("背包空间不足！");
                MessageUtils.notify_player(player, Notify.ERROR, msId);
                return;
            }

            player.getFunctionTaskData().getRechargeIds().add(bean.getId());
            Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.FunctionTaskRechargeGet, IDConfigUtil.getLogId());

            //刷新
            //发送更新信息
            FunctionTaskMessage.FunctionRechargeTask.Builder t = FunctionTaskMessage.FunctionRechargeTask.newBuilder();
            t.setRechargeid(rechargeId);
            t.setGet(true);
            t.setNum(bean.getDiscount());
            RechargeItemInfo cfg = Manager.rechargeManager.getRechargeItemInfoMap().get(bean.getRechargeID());
            List<Item> its = Item.createItems(cfg.getGoods_reward());
            for(Item it : its){
                CommonMessage.ShowItemInfo.Builder item = CommonMessage.ShowItemInfo.newBuilder();
                item.setModelId(it.getItemModelId());
                item.setCount(it.getNum());
                t.addRewards(item);
            }
            for(Item item : items){
                CommonMessage.ShowItemInfo.Builder info = CommonMessage.ShowItemInfo.newBuilder();
                info.setCount(item.getNum());
                info.setModelId(item.getItemModelId());
                t.addRewards(info);
            }
            FunctionTaskMessage.ResFunctionTaskUpdate.Builder res = FunctionTaskMessage.ResFunctionTaskUpdate.newBuilder();
            res.setRechargeTask(t);
            MessageUtils.send_to_player(player, FunctionTaskMessage.ResFunctionTaskUpdate.MsgID.eMsgID_VALUE, res.build().toByteArray());

            Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.FunctionTask, ItemChangeReason.FunctionTaskRechargeGet, goodsId);
        }catch (Exception e){
            log.error("",e);
        }
    }

    @Override
    public boolean canReward(Player player, int goods_id) {
        Cfg_Today_function_recharge_Bean bean = Cfg_Today_function_recharge_Container.GetInstance().getValueByKey(Manager.functionTaskManager.getRechargeId());
        if(bean == null){
            return false;
        }
        //是否已充值
        if(player.getFunctionTaskData().getRechargeIds().contains(bean.getId())){
            return false;
        }
        if(bean.getRechargeID() == goods_id){
            return true;
        }
        return false;
    }
}
