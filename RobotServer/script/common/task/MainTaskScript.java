package common.task;

import com.data.CfgManager;
import com.data.bean.*;
import com.data.struct.ReadArray;
import com.data.struct.ReadIntegerArray;
import com.game.backpack.struct.ItemCoinType;
import com.game.cooldown.structs.CooldownTypes;
import com.game.equip.struct.Equip;
import com.game.function.struct.FunctionType;
import com.game.manager.Manager;
import com.game.map.structs.*;
import com.game.player.structs.CellItem;
import com.game.player.structs.EventDefine;
import com.game.player.structs.Player;
import com.game.script.ScriptEnum;
import com.game.structs.Position;
import com.game.task.script.IMainTaskScript;
import com.game.task.structs.TaskState;
import com.game.task.structs.TaskType;
import com.game.utils.ICallback;
import com.game.utils.MapUtils;
import game.core.util.TimeUtils;
import game.message.HookMessage;
import game.message.NatureMessage;
import game.message.RecycleMessage;
import game.message.taskMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainTaskScript implements IMainTaskScript {

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
    public int doMainTask(Player player) {
        int afterTime = 2000;
        int nowTaskId = player.getMainTask().getNowTaskId();
        if (nowTaskId == -1) {//所有主线任务已做完
            if(player.getMapModelId()>=1501&&player.getMapModelId()<=1545){//已经在副本中
                //检查行为状态
                if(player.getEventType() == EventDefine.Event_MainTask){
                    player.setEventType(EventDefine.Event_CopyMap);
                    return afterTime;
                }
            }else{
                log.info(player.getInfo()+"主线任务做完,taskId="+nowTaskId+",进入世界BOSS副本打怪,cloneId=1509");
                //进入世界boss副本

                if (player.getLevel()<800){
                    player.chatGM( "&setlevel 800");
                    player.setLevel(800);
                    player.chatGM("&addatt 1 30000");
                    player.chatGM("&addatt 2 30000");
                    player.chatGM("&addatt 3 30000");
                    player.chatGM("&addatt 4 30000");
                    return 5000 ;
                }
                if(Manager.dailyActivityManager.deal().enterDaily(player,4)>0){
                    player.setEventType(EventDefine.Event_CopyMap);
                    return 5000;
                }
                if(Manager.dailyActivityManager.deal().enterDaily(player,12)>0){
                    player.setEventType(EventDefine.Event_CopyMap);
                    return 5000;
                }
               // player.setEventType(EventDefine.Event_MoveStop);
                player.randMove(player.getCurPos());
                return afterTime;
            }
        }else if(nowTaskId == 0){//暂时没有主线任务
            player.randMove(player.getCurPos());
            return afterTime;
        }
        Cfg_Task_Bean bean = CfgManager.getCfg_Task_Container().getValueByKey(nowTaskId);
        if (bean == null) {
            log.error(player.getInfo()+"主线任务配置表数据错误，taskId="+nowTaskId);
            return gmFinishMainTask(player,nowTaskId);
        }
        if (bean.getPathMap() == 1010){
            return gmFinishMainTask(player,nowTaskId);
        }
        if (bean.getTask_id() == 99304  ||  bean.getTask_id() == 99305){
            return gmFinishMainTask(player,nowTaskId);
        }
        //检查任务时间
        Map<Integer,Long> waitTimeMap = player.getWaitMap().get(EventDefine.Event_MainTask);
        if(waitTimeMap == null||waitTimeMap.isEmpty()){
            waitTimeMap = new HashMap<>();
            //设置任务保护时间
            waitTimeMap.put(nowTaskId,TimeUtils.Time());
            player.getWaitMap().put(EventDefine.Event_MainTask,waitTimeMap);
        }else{
            long lastTime = waitTimeMap.get(nowTaskId);
            long nowTime = TimeUtils.Time();
            if (lastTime == 0) {
                waitTimeMap.put(nowTaskId,nowTime);
            }else if(nowTime-lastTime>=60*1000){
                log.info("使用GM命令完成，任务执行超时:"+(nowTime-lastTime));
                return gmFinishMainTask(player,nowTaskId);
            }
        }

        int taskType = bean.getType();
//        log.info(player.getInfo()+"开始执行任务,taskId="+taskId+",taskType="+taskType);
        switch (taskType){
            case TaskType.ACTION_TYPE_NPC_TALK: {//NPC对话
                return doTalkTask(player, bean);
            }
            case TaskType.ACTION_TYPE_KILL_MONSTER: {//地图杀怪
                return doKillMonsterTask(player, bean);
            }
            case TaskType.ACTION_TYPE_GATHER: {//采集(不进背包）
                return doGatherTask(player, bean);
            }
            case TaskType.ACTION_TYPE_FUNCTION: {//功能操作
                return doOprateTask(player, bean);
            }
            case TaskType.ACTION_TYPE_NEED_LEVEL: {//卡等级
                return doLevelTask(player, bean);
            }
            case TaskType.ACTION_TYPE_SUCCESSFUL_ZONE: {//副本通关
                return doKillCloneTask(player, bean);
            }
            case TaskType.ACTION_TYPE_PLANE: {//位面杀怪
                return doPlaneTask(player, bean);
            }
            case TaskType.ACTION_TYPE_PLANE_FABAO: {//位面杀怪
                return doPlaneFaBaoTask(player, bean);
            }
            default:
                break;
        }
        return afterTime;
    }

    /**
     * 完成对话任务
     * @param bean
     * @return
     */
    private int doTalkTask(Player player, Cfg_Task_Bean bean) {
        int afterTime = 1000;
        ReadArray<Integer> values = bean.getEndpath();
        int mapDataId = values.get(0);
        int npcId = values.get(1);
        ByteMapCfg cfg = Manager.mapCfgManager.getMapCfg(player.getMapModelId());
        if (cfg == null) {
            return 0;
        }
        if (player.getMapModelId() == mapDataId) {
            Position p = null;
            for (ByteMapItem npc : cfg.getNpcCfg()) {
                if (npc.getId() == npcId) {
                    p = new Position(npc.getMapX(), npc.getMapY());
                    break;
                }
            }
            if (p == null) {
                return 0;
            }
            double dis = MapUtils.getDistance(p, player.getCurPos());
            if (dis < 3) {//到了地方，交任务
                log.info(player.getInfo()+"距离较近,dis="+dis+"，移动到目标位置:"+p.getX()+" "+p.getY()+",当前位置:"+player.getCurPos().getX()+" "+player.getCurPos().getY()+",当前地图ID:"+player.getMapModelId());
//                afterTime = finishMainTask(player, bean);
                if(player.getMainTask().isFinish()){
                    sendFinishMainTask(player);
                }
                afterTime = 2000;
            } else {
                //有时候卡地图直接传送到目标
                if (checkMoveWaitTime(player, p)) return 2000;

                log.info(player.getInfo()+"移动到目标位置:"+p.getX()+" "+p.getY()+",当前位置："+player.getCurPos()+",目标地图ID:"+player.getMapModelId());
                player.moveAi_MoveToPos(p);
                afterTime = 2000;
            }
        } else {//如果是另外一张图
            player.moveToMapObject(mapDataId, npcId, player.MapObject_NPC);
            afterTime = 2000;
        }
        return afterTime;
    }



    /**
     * 完成杀怪任务
     */
    private int doKillMonsterTask(Player player, Cfg_Task_Bean bean) {
        int afterTime = 0;
        ReadArray<Integer> values = bean.getTarget();
        final int monsterId = values.get(0);
        Integer killNum = player.getMainTask().getTargetValue().get(monsterId);
        if (killNum == null) {
            killNum = 0;
        }
        if (killNum >= values.get(1)) {
            afterTime = finishMainTask(player, bean);
            return afterTime;
        }
        ByteMapCfg cfg = Manager.mapCfgManager.getMapCfg(player.getMapModelId());
        if (cfg == null) {
            return 1000;
        }
        if (player.getMapModelId() == bean.getPathMap()) {
            Position attackPos = new Position();
            int ret = player.doSomeForTarget(MapMonster.class, monsterId, player.getMapModelId(), 3, new ICallback() {
                @Override
                public void Invoke(Object... params) {
                    MapMonster m = (MapMonster) params[0];
                    player.attack(m.getCurPos(), monsterId);
                }
            }, attackPos, false);
            if (ret == 0) {
                player.moveToPos(player.getMapModelId(), attackPos);
                afterTime = 5000;
            } else if (ret == 1) {
                player.sendGetMonsterPos();
//                player.randMove(player.getCurPos());
            }
        } else {
            player.moveToMapObject(bean.getPathMap(), monsterId, player.MapObject_Monster);
            afterTime = 5000;
        }
        return afterTime;
    }

    /**
     * 完成采集任务
     * @param bean
     * @return
     */
    private int doGatherTask(Player player, Cfg_Task_Bean bean) {
        int afterTime = 2000;

        if(player.getMainTask().isFinish()){
            sendFinishMainTask(player);
            return 2000;
        }

        ReadArray<Integer> values = bean.getTarget();
        int gatherId = values.get(0);

        if(Manager.cooldownManager.isCooldowning(player, CooldownTypes.AI_GATHER_CD,String.valueOf(gatherId))){
            log.info(player.getInfo()+"正在进行采集操作");
            return afterTime;
        }
//        int shoujiNum = player.getMainTask().getTargetValue().get(key);
//        if (shoujiNum >= values.get(1)) {
//            afterTime = finishMainTask(player, bean);
//            return afterTime;
//        }
        ByteMapCfg cfg = Manager.mapCfgManager.getMapCfg(player.getMapModelId());
        if (cfg == null) {
            return afterTime;
        }
        if (player.getMapModelId() == bean.getPathMap()) {
            Position targetpos = new Position();
            int ret = player.doSomeForTarget(MapGather.class, gatherId, player.getMapModelId(), 2, new ICallback() {
                @Override
                public void Invoke(Object... params) {
                    player.gather((MapGather) params[0]);
                }
            }, targetpos, false);
            if (ret == 0) {
                player.moveAi_MoveToPos(targetpos);
            } // 找不到对象,问问服务器哪里有没有,然后随机走走
            else if (ret == 1) {
                player.randMove(player.getCurPos());
            }
        } else {
            player.moveToMapObject(bean.getPathMap(), gatherId, player.MapObject_Gather);
        }
        return afterTime;
    }

    /**
     * 卡等级任务
     * @return
     */
    private int doLevelTask(Player player, Cfg_Task_Bean bean){
        int afterTime = 1000;
        if(player.getMainTask().isFinish()){
            sendFinishMainTask(player);
            return 2000;
        }
        ReadIntegerArray ri = bean.getTarget();
        if(ri == null||ri.isEmpty()){
            log.error("Cfg_Task_Bean配置表错误,ID:"+bean.getTask_id());
            return afterTime;
        }
        if(ri.size() != 2){
            log.error("Cfg_Task_Bean配置表错误,ID:"+bean.getTask_id());
            return afterTime;
        }
        if(player.getLevel() < ri.get(1)){
            player.chatGM("&setlevel " + ri.get(1));
            return 2000;
        }
        return afterTime;
    }

    /**
     * 完成副本任务
     */
    private int doKillCloneTask(Player player, Cfg_Task_Bean bean) {
        //1700003_1_11008_1300
        ReadArray<Integer> tmp = bean.getTarget();
        int zoneModelId = bean.getServercloneId();
        final int monsterId = tmp.get(0);
        int num = tmp.get(1);
        int afterTime = 0;
        Integer killNum = player.getMainTask().getTargetValue().get(monsterId);
        if (killNum == null) {
            killNum = 0;
        }
        if (killNum >= num) {
            afterTime = finishMainTask(player, bean);
            return afterTime;
        }
        Cfg_Clone_map_Bean clone = CfgManager.getCfg_Clone_map_Container().getValueByKey(zoneModelId);
        if (clone == null) {
            log.info(player.getInfo() + "正在做任务:" + bean.getTask_name() + "(" + bean.getTask_id() + ")");
            return player.attack(player.getCurPos(), 0);
        }
        //已经在副本地图
        int maps = clone.getMapid();
        if (maps == player.getMapModelId()) {
//            log.info(this.getInfo() + "进入副本:" + clone.getDuplicate_name() + "[-] 开始攻击" + monsterId);
//            cloneatt += 1;
//            if (cloneatt > 4) {
//                chatGM(&kill 1);
//            }
            Position attackPos = new Position();
            int ret = player.doSomeForTarget(MapMonster.class, 0, player.getMapModelId(), 3, new ICallback() {
                @Override
                public void Invoke(Object... params) {
                    MapMonster m = (MapMonster) params[0];
                    player.attack(m.getCurPos(), 0);
                }
            }, attackPos, false);
            if (ret == 0) {
                player.moveToPos(player.getMapModelId(), attackPos);
            } else if (ret == 1) {//没有找到目标
                player.sendGetMonsterPos();
//                player.randMove(player.getCurPos());
            }
            return afterTime;
        }

        player.reqEnterZone(zoneModelId);
        log.info(player.getInfo()+"进入副本:" +clone.getId()+ "_" +clone.getDuplicate_name());
        return 10 * 1000;
    }

    /**
     * 进行位面杀怪任务
     * 需要先到指定坐标，然后进入位面副本,等完成任务之后再回到原地图
     * @param bean
     * @return
     */
    private int doPlaneTask(Player player, Cfg_Task_Bean bean){
        int afterTime = 2000;

        if(player.getMainTask().isFinish()){
            sendFinishMainTask(player);
            return 5000;
        }

        ByteMapCfg cfg = Manager.mapCfgManager.getMapCfg(player.getMapModelId());
        if (cfg == null) {
            return afterTime;
        }

        int task_clone_id = bean.getTarget().get(2);
        Cfg_Clone_map_Bean cloneBean = CfgManager.getCfg_Clone_map_Container().getValueByKey(task_clone_id);
        if (cloneBean == null) {
            log.error("Cfg_Clone_map_Bean没有找到位面副本数据,ID："+task_clone_id);
            return afterTime;
        }

        int cloneMapId = cloneBean.getMapid();
        if (cloneMapId == player.getMapModelId()) {//在位面副本中
            if(cloneBean.getId() == 1027){//1027副本特殊处理,手动请求位面刷怪
                if(checkPlaneCloneRefresh(player)){
                    if(player.getPlaneLoop() < 2){
                        player.reqFlashMonster();
                        player.setPlaneLoop(player.getPlaneLoop()+1);
                        return 3000;
                    }
                }
            }else if(cloneBean.getId() == 19002){
                if(player.getPlaneLoop() < 2){
                    player.reqFlashMonster();
                    player.setPlaneLoop(player.getPlaneLoop()+1);
                    return 3000;
                }
            }

            log.info(player.getInfo()+"寻怪攻击,怪物数量:"+player.getNpcs().size()+",当前地图ID:"+player.getMapModelId());
            //寻怪并战斗
            afterTime = player.findAndAttack();
        } else if(player.getMainTask().getMapId() == player.getMapModelId()){//在寻路地图中
            if(player.getMainTask().getxPos() == player.getCurPos().getX()&&player.getMainTask().getyPos() == player.getCurPos().getY()){//在目标位置
                //请求进入位面副本
                player.reqEnterZone(task_clone_id);
                afterTime = 3000;
            }else{//移动到目标位置
                Position p = new Position(player.getMainTask().getxPos(), player.getMainTask().getyPos());
                if (MapUtils.getDistance(p, player.getCurPos()) < 3) {
                    player.moveAi_MoveToPos(p);
                    log.info(player.getInfo()+"距离较近，移动到目标位置：X="+p.getX()+",Y="+p.getY()+",当前位置：X="+player.getCurPos().getX()+",Y="+player.getCurPos().getY()+",当前地图ID:"+player.getMapModelId());
                } else {
                    //有时候卡地图直接传送到目标
                    if (checkMoveWaitTime(player, p)) return 3000;

                    log.info(player.getInfo()+"移动到目标位置:"+p.getX()+" "+p.getY()+",当前位置："+player.getCurPos().getX()+" "+player.getCurPos().getY()+",当前地图ID:"+player.getMapModelId());
                    player.moveAi_MoveToPos(p);
                }
            }
        } else {//不在寻路地图，也不在位面副本中
            log.info(player.getInfo()+"不在寻路地图或位面副本中,任务寻路地图："+player.getMainTask().getMapId()+"，位面副本地图："+cloneMapId);
            Cfg_Mapsetting_Bean mapBean = CfgManager.getCfg_Mapsetting_Container().getValueByKey(player.getMapModelId());
            if(mapBean == null){
                log.error("配置表中没有找到地图数据,ID："+player.getMapModelId());
                return afterTime;
            }
            if(mapBean.getType() == 0){//在世界地图中
                player.reqTransportControl(1,player.getMainTask().getMapId());
                afterTime = 3000;
            } else {//在副本中,要先退出副本
                player.reqCopyMapOut();
//                player.chatGM("&entermap " + player.getMapModelId() + " " + player.getMainTask().getxPos() + " " + player.getMainTask().getyPos());
                afterTime = 3000;
//                player.moveToMapObject(mapDataId, npcId, MapObject_NPC);
            }
        }
        return afterTime;
    }

    /**
     * 进行位面演示副本
     * 需要先到指定坐标，然后进入位面副本,等完成任务之后再回到原地图
     * @param bean
     * @return
     */
    private int doPlaneFaBaoTask(Player player, Cfg_Task_Bean bean){
        int afterTime = 3000;
        ByteMapCfg cfg = Manager.mapCfgManager.getMapCfg(player.getMapModelId());
        if (cfg == null) {
            return afterTime;
        }

        int task_clone_id = bean.getTarget().get(2);
        Cfg_Clone_map_Bean cloneBean = CfgManager.getCfg_Clone_map_Container().getValueByKey(task_clone_id);
        if (cloneBean == null) {
            log.error("没有找到位面副本数据,ID："+task_clone_id);
            return afterTime;
        }

        int cloneMapId = cloneBean.getMapid();
        if (cloneMapId == player.getMapModelId()) {//在位面副本中
            sendFinishMainTask(player);
            log.info(player.getInfo()+"在位面演示副本中，请求完成任务,当前地图ID:"+player.getMapModelId());
            afterTime = 3000;
        } else if(player.getMainTask().getMapId() == player.getMapModelId()){//在寻路地图中
            if(player.getMainTask().getxPos() == player.getCurPos().getX()&&player.getMainTask().getyPos() == player.getCurPos().getY()){//在目标位置
                //请求进入位面副本
                player.reqEnterZone(task_clone_id);
                log.info(player.getInfo()+"请求进入位面副本："+task_clone_id);
                afterTime = 3000;
            }else{//移动到目标位置
                Position p = new Position(player.getMainTask().getxPos(), player.getMainTask().getyPos());
                if (MapUtils.getDistance(p, player.getCurPos()) < 3) {
                    player.moveAi_MoveToPos(p);
                    log.info(player.getInfo()+"距离较近，移动到目标位置：X="+p.getX()+",Y="+p.getY()+",当前位置：X="+player.getCurPos().getX()+",Y="+player.getCurPos().getY()+",当前地图ID:"+player.getMapModelId());
                } else {
                    //有时候卡地图直接传送到目标
                    if (checkMoveWaitTime(player, p)) return 3000;

                    log.info(player.getInfo()+"移动到目标位置:"+p.getX()+" "+p.getY()+",当前位置："+player.getCurPos().getX()+" "+player.getCurPos().getY()+",当前地图ID:"+player.getMapModelId());
                    player.moveAi_MoveToPos(p);
                }
            }
        } else {//不在寻路地图，也不在位面副本中
            log.info("玩家地图不一致,当前地图:"+player.getMapModelId()+",主线任务寻径地图ID："+player.getMainTask().getMapId()+"，主线位面副本地图："+cloneMapId);
            player.reqTransportControl(1,player.getMainTask().getMapId());
            afterTime = 5000;
            //            moveToMapObject(mapDataId, npcId, MapObject_NPC);
        }
        return afterTime;
    }

    /**
     * 操作任务,主要根据main_task表的target字段去FunctionVariable表查找对应操作功能
     * @param bean
     * @return
     */
    private int doOprateTask(Player player, Cfg_Task_Bean bean){
        int afterTime = 2000;

        if(player.getMainTask().isFinish()){
            sendFinishMainTask(player);
            return 2000;
        }

        int fvId = bean.getTarget().get(0);
        int fvVal = bean.getTarget().get(1);
        Cfg_FunctionVariable_Bean fbean = CfgManager.getCfg_FunctionVariable_Container().getValueByKey(fvId);
        if (fbean == null) {
            log.error("FunctionVariable配置表数据错误,ID="+fvId);
            return afterTime;
        }

        if(bean.getOpen_panel_param() == 1){//需要先到目标位置
            log.info(player.getInfo()+"执行操作任务"+bean.getTask_id()+"前需要先移动到目标位置,X="+bean.getEndpath().get(0)+",Y="+bean.getEndpath().get(1));
            player.moveToCurMapPos(bean.getEndpath().get(0),bean.getEndpath().get(1));
        }

        switch (fvId){
            case FunctionType.FUNCTION_PlayerLevel: {//玩家等级
                return doPlayerLevel(player, bean);
            }
            case FunctionType.FUNCTION_WornEquip: {//穿戴X件X阶及以上的X色X星装备
                return Manager.equipManager.deal().doWornEquipTask(player, bean);
//                return gmFinishMainTask(player,bean.getTask_id());
            }
            case FunctionType.FUNCTION_StateLevel: {//境界突破
                return doStateLevel(player, bean);
            }
            case FunctionType.FUNCTION_RiChangJingYanNum: {//累计完成日常经验任务次数
                  return gmFinishMainTask(player,bean.getTask_id());
//                return doRiChangJingYanNum(player);
            }
            case FunctionType.FUNCTION_EquipSmelt: {//装备熔炼
                return doEquipSmelt(player);
            }
            default:
                break;
        }

        return afterTime;
    }

    /**
     * 执行境界突破操作
     * @return
     */
    private int doStateLevel(Player player, Cfg_Task_Bean bean){
        int afterTime = 1000;

        int stateLv = bean.getTarget().get(1);
        Cfg_State_Bean stateBean = CfgManager.getCfg_State_Container().getValueByKey(stateLv*100+1);
        if(stateBean == null){
            log.error("没有找到配置数据,ID："+player.getMapModelId());
            return afterTime;
        }

        if(stateBean.getCondition().get(0) ==1&&player.getLevel()<stateBean.getCondition().get(1)){
            player.chatGM("&setlevel " + stateBean.getCondition().get(1));
//            return afterTime;
        }

        player.chatGM("&setstatevip " + stateLv);
        return afterTime;
    }

    /**
     * 执行玩家等级
     * @return
     */
    private int doPlayerLevel(Player player, Cfg_Task_Bean bean){
        int afterTime = 1000;
        int targetLv = bean.getTarget().get(1);
        if(player.getLevel()>=targetLv){
            return 1000;
        }

//        CellItem ci = player.getBags().get(ItemCoinType.ActivePoint);
//        if(ci == null){
            player.chatGM("&setlevel " + targetLv);
//        }else{
////            player.reqLeaderSitDown(true);
//        }

        return afterTime;
    }

    /**
     * 执行日常经验任务操作
     * @return
     */
    private int doRiChangJingYanNum(Player player){
        return 1000;
    }

    /**
     * 执行装备熔炼(回收)操作
     * @return
     */
    @Override
    public int doEquipSmelt(Player player){
        int afterTime = 5000;
        List<Long> rmList = new ArrayList();
        for (Equip eq:player.getBagEquips().values()) {
            if(eq.getQuality()>=7&&eq.getGender().contains(player.getCareer())){//保留本职业红色以上装备
                continue;
            }
            if(eq.getQuality()<=6&&eq.getGrade()<=16&&eq.getStar()<=1){//紫色
                rmList.add(eq.getId());
                player.getBagEquips().remove(eq.getId());
            }
        }
        if(rmList.isEmpty()){//没有可以熔炼的装备
            player.chatGM("&additem 2000005");
            return 2000;
        }
        log.info(player.getInfo()+"请求装备熔炼个数："+rmList.size());
        RecycleMessage.ReqRecycle.Builder msg = RecycleMessage.ReqRecycle.newBuilder();
        msg.addAllItemId(rmList);
        player.sendMsg(RecycleMessage.ReqRecycle.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        return afterTime;
    }

    @Override
    public void mainTaskChange(Player player, taskMessage.mainTaskInfo messInfo) {
        if(player.getMainTask().getNowTaskId() == messInfo.getModelId()){//更新任务进度
            player.getMainTask().updateTaskInfo(messInfo.getUseItems());
            Map<Integer,Long> waitTimeMap = player.getWaitMap().get(EventDefine.Event_MainTask);
            if(waitTimeMap!=null&&!waitTimeMap.isEmpty()){//进度更新时清空超时时间
                waitTimeMap.put(player.getMainTask().getNowTaskId(),TimeUtils.Time());
            }
            log.info(player.getInfo() + "更新任务进度，任务ID=" + messInfo.getModelId()+",进度:"+player.getMainTask().getTargetValue().get(messInfo.getUseItems().getModel()));
        }else {//接任务
            player.getMainTask().resetTaskInfo(messInfo);
            player.waitDoTime(500);
            log.info(player.getInfo() + "变更(接取)任务ID=" + messInfo.getModelId());
        }
    }

    @Override
    public void mainTaskFinish(Player player, taskMessage.ResTaskFinish messInfo) {
        if (messInfo.getState() == TaskState.TOO_FAR_WITH_NPC) {
            player.setOverlen(true);
            log.info(player.getInfo()+"与交任务的npc太远");
            return;
        }
        if (messInfo.getState() == TaskState.SUCCESS) {
            player.getMainTask().clear();
            player.getMainTask().getOverTaskIds().add(messInfo.getModelId());
            Map<Integer,Long> waitTimeMap = player.getWaitMap().get(EventDefine.Event_MainTask);
            if(waitTimeMap!=null&&!waitTimeMap.isEmpty()){
                waitTimeMap.clear();
            }

            Cfg_Task_Bean bean = CfgManager.getCfg_Task_Container().getValueByKey(messInfo.getModelId());
            if (bean == null) {
                log.error(player.getInfo()+"主线任务配置表数据错误，taskId="+messInfo.getModelId());
                return;
            }

            if(bean.getPost_task_id() <= 0){
                log.info(player.getInfo()+"没有主线任务了");
                player.getMainTask().setNowTaskId(-1);
                return;
            }

            if(!bean.getConditions_value().isEmpty()){
                int fvId = bean.getConditions_value().get(0).get(0);
                int fvValue = bean.getConditions_value().get(0).get(1);
                Cfg_FunctionVariable_Bean fbean = CfgManager.getCfg_FunctionVariable_Container().getValueByKey(fvId);
                if (fbean == null) {
                    log.error("FunctionVariable配置表数据错误,ID="+fvId);
                    return;
                }

                if(fvId == 1){//等级
                    if(player.getLevel()<fvValue){
                        player.getMainTask().setNowTaskId(-1);
                    }
                }
            }
        }
    }

    /**
     * 完成任务
     */
    private int finishMainTask(Player player, Cfg_Task_Bean bean) {
        int afterTime = 1000;
        if (player.getSession() == null) {
            return afterTime;
        }
        if (player.getSession().isWriteSuspended()) {
            log.error(player.getInfo()+"网络忙");
            return afterTime;
        }
        ReadArray<Integer> values = bean.getEndpath();
        int mapId = values.get(0);
        int npcId = values.get(1);
        if (mapId == player.getMapModelId()) {
            ByteMapCfg cfg = Manager.mapCfgManager.getMapCfg(player.getMapModelId());
            Position p = null;
            for (ByteMapItem npc : cfg.getNpcCfg()) {
                if (npc.getId() == npcId) {
                    p = new Position(npc.getMapX(), npc.getMapY());
                    break;
                }
            }
            if (p == null) {
                gmFinishMainTask(player,bean.getTask_id());
                log.info(player.getInfo() + " GM3完成任务：" + bean.getTask_id());
                return 2000;
            }
            if (player.isOverlen()) {
                log.info(player.getInfo() + "距离太远了，完成任务Id ：" + bean.getTask_id() + "(" + bean.getTask_name() + ")");
//                    chatGM("&moveto " + p.getX() + " " + p.getY());
//                    moveToPos(mapModelId, p);
                player.moveAi_MoveToPos(p);
                return 2000;
            }
            if (MapUtils.getDistance(p, player.getCurPos()) < 3) {
                player.setOverlen(false);
                sendFinishMainTask(player);
                return 2000;
            } else {
                //有时候卡地图直接传送到目标
                if (checkMoveWaitTime(player, p)) return 2000;

                player.moveAi_MoveToPos(p);
                afterTime = 2000;
            }
        } else {
            player.moveToMapObject(mapId, npcId, player.MapObject_NPC);
            afterTime = 2000;
        }
        return afterTime;
    }

    //完成任务
//    private int finishMainTask(Player player, Cfg_Task_Bean bean) {
//        if (player.getSession() == null) {
//            return 1000;
//        }
//        if (getSession().isWriteSuspended()) {
//            log.error(this.getInfo() + ", 网络忙");
//            return 1000;
//        }
//        int afterTime = 10000;
//        ReadArray<Integer> values = bean.getEndpath();
//        int mapId = values.get(0);
//        int npcId = values.get(1);
//        if (mapId == mapModelId) {
//            ByteMapCfg cfg = Manager.mapCfgManager.getMapCfg(mapModelId);
//            Position p = null;
//            for (ByteMapItem npc : cfg.getNpcCfg()) {
//                if (npc.getId() == npcId) {
//                    p = new Position(npc.getMapX(), npc.getMapY());
//                    break;
//                }
//            }
//            if (p == null) {
//                gmFinishMainTask(player,bean.getTask_id());
//                log.info(getId() + " GM3完成任务：" + bean.getTask_id());
//                return 10000;
//            }
//            if (overlen) {
//                log.info(this.getInfo() + " 距离太远了，完成任务Id ：" + bean.getTask_id() + "(" + bean.getTask_name() + ")");
////                    chatGM("&moveto " + p.getX() + " " + p.getY());
////                    moveToPos(mapModelId, p);
//                moveAi_MoveToPos(p);
//                return 1000;
//            }
//
//            if (MapUtils.getDistance(p, curPos) < 3) {
//                overlen = false;
//                sendFinishMainTask();
//                return 10000;
//            } else {
////                moveToPos(mapModelId, p);
//                moveAi_MoveToPos(p);
//                afterTime = 1000;
//            }
//        } else {
//            moveToMapObject(mapId, npcId, MapObject_NPC);
//            afterTime = 10000;
//        }
//
//        return afterTime;
//    }

    /**
     * GM命令过任务
     * @return
     */
    private int gmFinishMainTask(Player player, int taskId){
        player.chatGM("&overtask 0 " + taskId);
        return 2000;
    }

    private boolean checkPlaneCloneRefresh(Player player){
        if(player.getPlaneLoop()>2){
            return false;
        }

        if(player.getNpcs().values().size() == 0){
            log.info(player.getInfo()+"周围npc都被移除了");
            return true;
        }

        for (BaseNpc npc:player.getNpcs().values()) {
            if(npc instanceof MapMonster){
                return false;
            }
        }
        return true;
    }

    private boolean checkMoveWaitTime(Player player, Position p) {
        long now = TimeUtils.Time();
        long offset = now - player.getMoveWaitTime();
        if (player.getMoveWaitTime() == 0) {
            player.setMoveWaitTime(now);
        } else if (offset > 30 * 1000) {
            log.info(player.getInfo() + "移动超时：" + offset + "ms");
            player.chatGM("&moveto " + p.getX() + " " + p.getY());
            player.setMoveWaitTime(0);
            return true;
        }
        return false;
    }

    /**
     *  请求完成主线任务
     */
    private void sendFinishMainTask(Player player) {
        taskMessage.ReqTaskFinish.Builder msg = taskMessage.ReqTaskFinish.newBuilder();
        msg.setModelId(player.getMainTask().getNowTaskId());
        msg.setRewardPer(1);
        msg.setTaskId(player.getMainTask().getInstanceId());
        msg.setType(0);
        player.sendMsg(taskMessage.ReqTaskFinish.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        log.info(player.getInfo() + "请求完成主线任务,modelId:" + player.getMainTask().getNowTaskId());
    }

    private int doMoveToPos(Player player, Cfg_Task_Bean bean) {
        Integer killNum = player.getMainTask().getTargetValue().get(0);
        if (killNum == null) {
            killNum = 0;
        }
        if (killNum >= 1) {
            return finishMainTask(player,bean);
        }

        ReadArray<Integer> tmp = bean.getTarget();
        Position pp = new Position(tmp.get(0), tmp.get(1));
        if (player.getCurPos().compare(pp)) {
            taskMessage.ReqChangeTaskState.Builder ms = taskMessage.ReqChangeTaskState.newBuilder();
            ms.setModelId(bean.getTask_id());
            ms.setType(0);
            player.sendMsg(taskMessage.ReqChangeTaskState.MsgID.eMsgID_VALUE, ms.build().toByteArray());
            return 500;
        }
//        moveToPos(mapModelId, pp);
        return 1000;
    }
}
