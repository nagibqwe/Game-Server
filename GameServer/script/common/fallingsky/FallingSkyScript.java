package common.fallingsky;

import com.data.*;
import com.data.bean.Cfg_FallingSky_Level_Bean;
import com.data.bean.Cfg_FallingSky_Task_Bean;
import com.data.struct.ReadArray;
import com.data.struct.ReadIntegerArrayEs;
import com.game.backpack.structs.Item;
import com.game.backpack.structs.ItemCoinType;
import com.game.bi.struct.BIActiityTypeEnum;
import com.game.fallingsky.script.IFallingSky;
import com.game.fallingsky.struct.FallingSkyData;
import com.game.fallingsky.struct.FallingSkyLevel;
import com.game.fallingsky.struct.FallingSkyTask;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.structs.GlobalType;
import com.game.task.structs.Task;
import com.game.task.structs.TaskConst;
import com.game.utils.MessageUtils;
import com.game.utils.ServerParamUtil;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.FallingSkyMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * 天禁令
 * Created by cxl on 2020/11/9.
 */
public class FallingSkyScript implements IFallingSky {


    private static final Logger logger = LogManager.getLogger("FallingSkyScript");

    @Override
    public int getId() {
        return  ScriptEnum.FallingSkyScript;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }

    @Override
    public void loadData() {
        int round = 0;
        int maxRound = Global.FallingSky_Round_Limit.get(1);
        long nowTime = TimeUtils.Time();
        if (ServerParamUtil.fallingSkyData.size() == 0) {
            round = 1;
        } else {
            for (Integer roundKey : ServerParamUtil.fallingSkyData.keySet()) {
                long overTime = ServerParamUtil.fallingSkyData.get(roundKey);
                if (nowTime > overTime) {
                    round = (roundKey + 1) > maxRound ? 1 : (roundKey + 1);
                } else {
                    round = roundKey;
                }
                break;
            }
        }
        long periodTime = Global.FallingSky_Round_Limit.get(0)* GlobalType.MILLIS_PER_DAY;
        long waitTime = getWaitTime(periodTime);
        Manager.fallingSkyManager.setRound(round);
        Manager.fallingSkyManager.setNextRoundTime(waitTime);
        ServerParamUtil.fallingSkyData.clear();
        ServerParamUtil.fallingSkyData.put(round, waitTime);
        ServerParamUtil.saveFallingSkyData();

    }

    @Override
    public void online(Player player) {
        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.TJLBase)) {
            return;
        }
        if (TimeUtils.getOpenServerDay()  <=  Global. FallingSky_Round_Open_Day){
            return;
        }
        checkRoundReward(player);
        initFallingSky(player);
    }


    private void initFallingSky(Player player){
        FallingSkyData skyData =  player.getFallingSkyData();
        FallingSkyMessage.ResOnlineFallSkyInfo.Builder msg =  FallingSkyMessage.ResOnlineFallSkyInfo.newBuilder();
        msg.setLastTime( Manager.fallingSkyManager.getNextRoundTime());
        msg.setRound( skyData.getRound());
        msg.setHasPay(skyData.isPay());
        msg.addAllTaskDataList(buildTaskDatas(player));
        msg.addAllLevelDataList(buildLevelDatas(player));
        MessageUtils.send_to_player(player, FallingSkyMessage.ResOnlineFallSkyInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }



    /**
     * 检车当前轮到期发奖
     * @param player
     */
    private void checkRoundReward(Player player){
        int curRound =  Manager.fallingSkyManager.getRound();
        FallingSkyData skyData =  player.getFallingSkyData();
        long nowTime = TimeUtils.Time();
        if (skyData.getRound() != curRound){
            if (skyData.getRound() <=0){
                skyData.setRound(curRound);
                skyData.setLoginTime(nowTime);
                return;
            }
            sendRoundLevelReward( player);
            sendTaskReward(player,1);
            sendTaskReward(player,2);
            sendTaskReward(player,3);
            skyData.setRound(curRound);
            skyData.setLoginTime(nowTime);
            skyData.getFallingSkyTaskMap().clear();
        }else {
            if (skyData.getLoginTime() <=0){
                skyData.setLoginTime(TimeUtils.Time());
                return;
            }
            int curDay = TimeUtils.getCurDayByTime(nowTime);
            int lastLoginDay = TimeUtils.getCurDayByTime(skyData.getLoginTime());
            if (lastLoginDay != curDay){
                //每日任务发奖
                sendTaskReward(player,1);
                checkPhaseTask(player);
            }
            skyData.setLoginTime(nowTime);
        }
    }

    /**
     * 每轮里面的阶段任务检查
     * 每周算一个小阶段
     * @param player
     */
    private void checkPhaseTask(Player player){

        long nowTime =  TimeUtils.Time();
        int curDay = TimeUtils.getCurDayByTime(nowTime);
        long open_Day_Millis =  Global.FallingSky_Round_Open_Day * GlobalType.MILLIS_PER_DAY;
        long openTime = TimeUtils.getBeginTime(TimeUtils.getOpenServerTime()) + open_Day_Millis;
        int openDay =  TimeUtils.getCurDayByTime(openTime);
        int phaseRound =  (((curDay - openDay) /Manager.fallingSkyManager.WeekDay) %4)+1;
        long periodTime = Manager.fallingSkyManager.WeekDay* GlobalType.MILLIS_PER_DAY;
        long waitTime = getWaitTime(periodTime);
        Manager.fallingSkyManager.setNextPhaseRoundTime(waitTime);
        Manager.fallingSkyManager.setPhaseRound(phaseRound);
        if (curDay  > openDay){
           if ((curDay - openDay) % Manager.fallingSkyManager.WeekDay  == 0) {
                sendTaskReward(player,2);
           }
        }
    }


    /**
     * 每轮等级奖励发送
     * @param player
     */
    private void sendRoundLevelReward(Player player){
        boolean isPay =  player.getFallingSkyData().isPay();
        int round = player.getFallingSkyData().getRound();
        HashMap<Integer, FallingSkyLevel>  skyLevelMap =  player.getFallingSkyData().getFallingSkyLevelMap();
        long skyCoinNum =   Manager.currencyManager.manager().getCurrencyNum(player, ItemCoinType.FallingSkyCoin);

        List<Cfg_FallingSky_Level_Bean> sky_level_beans = new ArrayList<>();
        for (Cfg_FallingSky_Level_Bean bean : CfgManager.getCfg_FallingSky_Level_Container().getValuees()){
            if (bean.getGoup() != round){
                continue;
            }
            sky_level_beans.add(bean);
        }
        for (Cfg_FallingSky_Level_Bean sky_level_bean : sky_level_beans){
            if (skyCoinNum  < sky_level_bean.getExp().get(1)){
                continue;
            }
            List<ReadIntegerArrayEs> rewards = new ArrayList<>();
            if (skyLevelMap.containsKey(sky_level_bean.getId())){
                FallingSkyLevel fallingSkyLevel =   skyLevelMap.get(sky_level_bean.getId());
                if (!fallingSkyLevel.isGetFree()){
                    rewards .add(sky_level_bean.getFreeReward());
                }
                if (isPay && !fallingSkyLevel.isGetPay()){
                    rewards.add(sky_level_bean.getPayReward());
                }
            }else {
                rewards.add(sky_level_bean.getFreeReward());
                if (isPay){
                    rewards.add(sky_level_bean.getFreeReward());
                }
            }
            for (ReadIntegerArrayEs arrayEs : rewards){
                sendLevelReward(player,arrayEs,true, sky_level_bean.getLevel());
            }
        }

        if (skyCoinNum>0){
            Manager.currencyManager.manager().onDecItemCoin(player, skyCoinNum,
                    ItemChangeReason.FallingSkyDec, IDConfigUtil.getLogId(), ItemCoinType.FallingSkyCoin);
        }
        skyLevelMap.clear();
    }

    /**
     * type 1每日奖励，2阶段奖励,3每轮奖励
     * 每日任务刷新发奖
     * @param player
     */
    private void sendTaskReward(Player player,int type){
        HashMap<Integer, FallingSkyTask> taskMap =  player.getFallingSkyData().getFallingSkyTaskMap();
        if (taskMap.size()<=0){
            return;
        }
        Iterator<Map.Entry<Integer, FallingSkyTask>> iter = taskMap.entrySet().iterator();
        while (iter.hasNext()){
            Map.Entry<Integer, FallingSkyTask> entry  =   iter.next();
            FallingSkyTask skyTask =  entry.getValue();
            Cfg_FallingSky_Task_Bean bean = CfgManager.getCfg_FallingSky_Task_Container().getValueByKey(skyTask.getTaskID());
            if (bean == null){
                logger.error("Cfg_FallingSky_Task_Bean is null {}",skyTask.getTaskID());
                continue;
            }
            if (bean.getType() != type){
                continue;
            }
            iter.remove();
            if (!skyTask.isState()  && skyTask.getProgress() >= bean.getCondition().get(1)){
                sendTaskReward(player,bean.getReward(),true);
            }
        }
    }

    /**
     * 发送奖励
     * @param player
     * @param reward
     * @param isMaill
     */
    private void sendReward(Player player, ReadIntegerArrayEs reward,boolean isMaill, int reason){
        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.TJLBase)) {
            return;
        }
        if (TimeUtils.getOpenServerDay()  <=  Global. FallingSky_Round_Open_Day){
            return;
        }
        long actionId = IDConfigUtil.getLogId();
        List<Item> items = Item.createItems(player.getCareer(),reward, 1);
        if (isMaill){
           String conext =   MessageString.Falling_Sky_Reward_Mail + "@_@" + Manager.fallingSkyManager.getRound();
           Manager.mailManager.sendMailToPlayer(player.getId(),
                    MessageString.System, MessageString.System, MessageString.Falling_Sky_Reward_Mail_Title,conext, items,reason);
        }else {
            if (! Manager.backpackManager.manager().addItems(player, items, reason, actionId)){
                Manager.mailManager.sendMailToPlayer(player.getId(),
                        MessageString.System, MessageString.System, MessageString.System, MessageString.BAG_FULL_MAIL_CONTENT, items, reason);
            }
        }
    }

    private void sendLevelReward(Player player, ReadIntegerArrayEs reward,boolean isMaill, int level){
        sendReward(player, reward, isMaill, ItemChangeReason.FallingSkyLevelRewardGain);
        Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.FallingSky_LevelAward, ItemChangeReason.FallingSkyLevelRewardGain, level);
    }

    private void sendTaskReward(Player player, ReadIntegerArrayEs reward,boolean isMaill){
        sendReward(player, reward, isMaill, ItemChangeReason.FallingSkyTaskRewardGain);
        Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.FallingSky_TaskAward, ItemChangeReason.FallingSkyTaskRewardGain);
    }

    @Override
    public void tick() {

        long nowTime = TimeUtils.Time();
        long waitTime = Manager.fallingSkyManager.getNextRoundTime();
        if (nowTime < waitTime) {
            return;
        }
        int maxRound = Global.FallingSky_Round_Limit.get(1);
        int curRound = Manager.fallingSkyManager.getRound();
        int nextRound = (curRound + 1) > maxRound ? 1 : (curRound + 1);
        long periodTime = Global.FallingSky_Round_Limit.get(0)* GlobalType.MILLIS_PER_DAY;
        waitTime = getWaitTime(periodTime);
        Manager.fallingSkyManager.setRound(nextRound);
        Manager.fallingSkyManager.setNextRoundTime(waitTime);
        ServerParamUtil.fallingSkyData.clear();
        ServerParamUtil.fallingSkyData.put(nextRound, waitTime);
        ServerParamUtil.saveFallingSkyData();


        for (Player player : Manager.playerManager.getPlayersCache().values()) {
            if (player.isOnline()) {
                if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.TJLBase)) {
                    continue;
                }
                if (TimeUtils.getOpenServerDay()  <=  Global. FallingSky_Round_Open_Day){
                    return;
                }
                checkRoundReward(player);
                initFallingSky(player);
            }
        }
    }

    /**
     * 进度刷新
     * @param player
     * @param type
     */
    @Override
    public void onRefreshUpProgress(Player player, int type,int num) {

        int round = Manager.fallingSkyManager.getRound();
        HashMap<Integer, FallingSkyTask> taskHashMap =  player.getFallingSkyData().getFallingSkyTaskMap();
        List<FallingSkyMessage.FallSkyTaskData> taskDatas = new ArrayList<>();
        for (Cfg_FallingSky_Task_Bean bean: CfgManager.getCfg_FallingSky_Task_Container().getValuees()){
            if (bean.getGroup() != round){
                continue;
            }
            if (bean.getCondition().get(0) != type){
                continue;
            }
            FallingSkyTask fallingSkyTask = taskHashMap.get(bean.getId());
            if (fallingSkyTask == null){
                fallingSkyTask =new FallingSkyTask();
                fallingSkyTask.setTaskID(bean.getId());
                fallingSkyTask.setState(false);
                taskHashMap.put(bean.getId(),fallingSkyTask);
            }

            fallingSkyTask.setProgress(fallingSkyTask.getProgress() + num);
            taskDatas.add( buildTaskData(fallingSkyTask));
        }
        if(taskDatas.size()>0){
            onResRefreshFallSkyTask(player,taskDatas);
        }
    }
    private int getProgress(Player player, ReadArray<Integer> param) {
        return Manager.controlManager.deal().getFuncProgress(player, param);
    }

    /**
     * 等级奖励领取
     * @param player
     * @param levelID
     * @param isFree
     */
    @Override
    public void onReqGetFallSkyLevelReward(Player player, int levelID, boolean isFree) {
        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.TJLBase)) {
            return;
        }
        if (TimeUtils.getOpenServerDay()  <=  Global. FallingSky_Round_Open_Day){
            return;
        }
        Cfg_FallingSky_Level_Bean bean = CfgManager.getCfg_FallingSky_Level_Container().getValueByKey(levelID);
        if (bean == null){
            logger.error("Cfg_FallingSky_Level_Bean  is null  {}" ,levelID);
            return;
        }

        long skyCoinNum =   Manager.currencyManager.manager().getCurrencyNum(player, ItemCoinType.FallingSkyCoin);
        if (skyCoinNum < bean.getExp().get(1)){
            logger.error("材料不足  {} " ,skyCoinNum);
            return;
        }
        HashMap<Integer, FallingSkyLevel> levelHashMap =  player.getFallingSkyData().getFallingSkyLevelMap();
        FallingSkyLevel level = levelHashMap.get(levelID);
        if(level == null){
            level = new FallingSkyLevel();
            level.setLevelID(levelID);
            levelHashMap.put(levelID,level);
        }
        if (isFree){
            //TODO 领取免费奖励
            if (level.isGetFree()){
                logger.error("免费奖励已领取level {}",level);
                return;
            }
            sendLevelReward(player, bean.getFreeReward(),false, bean.getLevel());
            level.setGetFree(true);
            onResRefreshFallSkyLevel(player,buildLevelData(level));
        }else {
            //TODO 领取付费奖励
            if (!player.getFallingSkyData().isPay()){
                logger.error("未付费不能领取付费奖励  {} " ,levelID);
                return;
            }
            if (level.isGetPay()){
                logger.error("付费奖励已经领取  {} " ,levelID);
                return;
            }
            sendLevelReward(player, bean.getPayReward(),false, bean.getLevel());
            level.setGetPay(true);
            onResRefreshFallSkyLevel(player,buildLevelData(level));
        }
    }

    /**
     * 一键领取等级奖励
     * @param player
     */
    public void onReqOnekeyGetFallSkyLevelReward(Player player){

        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.TJLBase)) {
            return;
        }
        if (TimeUtils.getOpenServerDay()  <=  Global. FallingSky_Round_Open_Day){
            return;
        }
        int round = Manager.fallingSkyManager.getRound();
        HashMap<Integer, FallingSkyLevel> levelHashMap =  player.getFallingSkyData().getFallingSkyLevelMap();
        boolean isPay =  player.getFallingSkyData().isPay();
        List<FallingSkyMessage.FallSkyLevelData> levelDatas = new ArrayList<>();
        for (Cfg_FallingSky_Level_Bean fallingSky_level_bean :
                CfgManager.getCfg_FallingSky_Level_Container().getValuees()){

            if (fallingSky_level_bean.getGoup() != round){
                continue;
            }
            long skyCoinNum =   Manager.currencyManager.manager().getCurrencyNum(player, ItemCoinType.FallingSkyCoin);
            if (skyCoinNum < fallingSky_level_bean.getExp().get(1)){
                continue;
            }

            FallingSkyLevel level = levelHashMap.get(fallingSky_level_bean.getId());
            if(level == null){
                level = new FallingSkyLevel();
                level.setLevelID(fallingSky_level_bean.getId());
                levelHashMap.put(fallingSky_level_bean.getId(),level);
            }
            List<ReadIntegerArrayEs> rewards = new ArrayList<>();
            if (!level.isGetFree()){
                rewards .add(fallingSky_level_bean.getFreeReward());
                level.setGetFree(true);
            }
            if (isPay && !level.isGetPay()){
                rewards.add(fallingSky_level_bean.getPayReward());
                level.setGetPay(true);
            }
            for (ReadIntegerArrayEs arrayEs : rewards){
                sendLevelReward(player,arrayEs,false, fallingSky_level_bean.getLevel());
            }
            levelDatas.add(buildLevelData(level));
        }
        if (levelDatas.size()>0){
            FallingSkyMessage.ResRefreshFallSkyLevel.Builder msg = FallingSkyMessage.ResRefreshFallSkyLevel.newBuilder();
            msg.addAllLevelData(levelDatas);
            MessageUtils.send_to_player(player, FallingSkyMessage.ResRefreshFallSkyLevel.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }
    }

    /**
     * 一键领取任务奖励
     * @param player
     */
    public void onReqOnekeyGetFallSkyTaskReward(Player player){
        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.TJLBase)) {
            return;
        }
        if (TimeUtils.getOpenServerDay()  <=  Global.FallingSky_Round_Open_Day){
            return;
        }

        HashMap<Integer, FallingSkyTask> taskMap =  player.getFallingSkyData().getFallingSkyTaskMap();
        if (taskMap.size()<=0){
            return;
        }
        List<FallingSkyMessage.FallSkyTaskData> taskDatas = new ArrayList<>();
        for (FallingSkyTask task :taskMap.values()){
            if (task.isState()){
                continue;
            }
            Cfg_FallingSky_Task_Bean bean = CfgManager.getCfg_FallingSky_Task_Container().getValueByKey(task.getTaskID());
            if (bean == null){
                logger.error("Cfg_FallingSky_Task_Bean is null  {}", task.getTaskID());
                continue;
            }
            if (task.getProgress() < bean.getCondition().get(1)){
                continue;
            }

            //发奖
            sendTaskReward(player,bean.getReward(),false);
            task.setState(true);
            taskDatas.add(buildTaskData(task));

            int type = 0;
            if(bean.getType() == 1){
                type = Task.FallingSky_TASK_DAY;
            }else if(bean.getType() == 2){
                type = Task.FallingSky_TASK_STAGE;
            }else if(bean.getType() == 3){
                type = Task.FallingSky_TASK_ROUND;
            }
            if(type != 0){
                Manager.biManager.getScript().biTask(player, bean.getId(), type, TaskConst.BI_RECEIVE,"",0,0);
            }
        }
        onResRefreshFallSkyTask(player,taskDatas);
    }
    /**
     * 任务奖励领取
     * @param player
     * @param taskID
     */
    @Override
    public void onReqGetFallSkyTaskReward(Player player, int taskID) {
        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.TJLBase)) {
            return;
        }
        if (TimeUtils.getOpenServerDay()  <=  Global.FallingSky_Round_Open_Day){
            return;
        }
        HashMap<Integer, FallingSkyTask> taskHashMap =  player.getFallingSkyData().getFallingSkyTaskMap();
        if (!taskHashMap.containsKey(taskID)){
            logger.error("任务未完成 不能领奖  {}", taskID);
            return;
        }
        Cfg_FallingSky_Task_Bean bean = CfgManager.getCfg_FallingSky_Task_Container().getValueByKey(taskID);
        if (bean == null){
            logger.error("Cfg_FallingSky_Task_Bean is null  {}", taskID);
            return;
        }
        FallingSkyTask fallingSkyTask =  taskHashMap.get(taskID);
        if (fallingSkyTask.isState()){
            logger.error("已领取  {}", taskID);
            return;
        }
        if (fallingSkyTask.getProgress() < bean.getCondition().get(1)){
            logger.error("任务未完成 不能领奖  {}", taskID);
            return;
        }

        //发奖
        sendTaskReward(player,bean.getReward(),false);
        fallingSkyTask.setState(true);
        List<FallingSkyMessage.FallSkyTaskData> taskDatas = new ArrayList<>();
        taskDatas.add(buildTaskData(fallingSkyTask));
        onResRefreshFallSkyTask(player,taskDatas);
        int type = 0;
        if(bean.getType() == 1){
            type = Task.FallingSky_TASK_DAY;
        }else if(bean.getType() == 2){
            type = Task.FallingSky_TASK_STAGE;
        }else if(bean.getType() == 3){
            type = Task.FallingSky_TASK_ROUND;
        }
        if(type != 0){
            Manager.biManager.getScript().biTask(player, bean.getId(), type, TaskConst.BI_RECEIVE,"",0,0);
        }
    }
    //刷新付费状态
    public void onResRefreshRechargeState(Player player,int goodsID){
        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.TJLBase)) {
            return;
        }
        if (TimeUtils.getOpenServerDay()  <=  Global. FallingSky_Round_Open_Day){
            return;
        }

        if (Global.FallingSky_RechargeId  != goodsID){
            return;
        }
        player.getFallingSkyData().setPay(true);

        FallingSkyMessage.ResRefreshRechargeState.Builder msg = FallingSkyMessage.ResRefreshRechargeState.newBuilder();
        msg.setHasPay( player.getFallingSkyData().isPay());
        MessageUtils.send_to_player(player, FallingSkyMessage.ResRefreshRechargeState.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    public  void onDailyRefreshTask(Player player){
        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.TJLBase)) {
            return;
        }
        if (TimeUtils.getOpenServerDay() <= Global.FallingSky_Round_Open_Day    ){
            return;
        }
        FallingSkyData skyData =  player.getFallingSkyData();
        skyData.setLoginTime(TimeUtils.Time());
        //每日任务奖励
        sendTaskReward(player,1);
        //阶段任务
        checkPhaseTask(player);
        initFallingSky(player);
    }


    //任务进度刷新
    private void onResRefreshFallSkyTask(Player player, List<FallingSkyMessage.FallSkyTaskData> taskDatas){

        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.TJLBase)) {
            return;
        }
        if (TimeUtils.getOpenServerDay()  <=  Global. FallingSky_Round_Open_Day){
            return;
        }

        FallingSkyMessage.ResRefreshFallSkyTask.Builder msg = FallingSkyMessage.ResRefreshFallSkyTask.newBuilder();
        msg.addAllTaskData(taskDatas);
        MessageUtils.send_to_player(player, FallingSkyMessage.ResRefreshFallSkyTask.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }
    //单个等级奖励状态刷新
    private void onResRefreshFallSkyLevel(Player player,FallingSkyMessage.FallSkyLevelData level){
        FallingSkyMessage.ResRefreshFallSkyLevel.Builder msg = FallingSkyMessage.ResRefreshFallSkyLevel.newBuilder();
        msg.addLevelData(level);
        MessageUtils.send_to_player(player, FallingSkyMessage.ResRefreshFallSkyLevel.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }


    private List<FallingSkyMessage.FallSkyTaskData> buildTaskDatas(Player player){
        List<FallingSkyMessage.FallSkyTaskData> taskDatas = new ArrayList<>();
        for (Map.Entry<Integer,FallingSkyTask> entry :player.getFallingSkyData().getFallingSkyTaskMap().entrySet()){
            FallingSkyMessage.FallSkyTaskData.Builder taskData = FallingSkyMessage.FallSkyTaskData.newBuilder();
            FallingSkyTask task = entry.getValue();
            taskData.setState(task.isState());
            taskData.setProgress(task.getProgress());
            taskData.setTaskID(task.getTaskID());
            taskDatas.add(taskData.build());

        }
        return taskDatas;
    }
    private List<FallingSkyMessage.FallSkyLevelData> buildLevelDatas(Player player){
        List<FallingSkyMessage.FallSkyLevelData> levelDatas = new ArrayList<>();
        for (Map.Entry<Integer,FallingSkyLevel> entry :player.getFallingSkyData().getFallingSkyLevelMap().entrySet()){
            FallingSkyMessage.FallSkyLevelData.Builder levelData = FallingSkyMessage.FallSkyLevelData.newBuilder();
            FallingSkyLevel level = entry.getValue();
            levelData.setIsGetfeelReward(level.isGetFree());
            levelData.setIsGetpayReward(level.isGetPay());
            levelData.setLevelId(level.getLevelID());
            levelDatas.add(levelData.build());
        }
        return levelDatas;
    }
    private  FallingSkyMessage.FallSkyTaskData buildTaskData(FallingSkyTask task){
        FallingSkyMessage.FallSkyTaskData.Builder taskData = FallingSkyMessage.FallSkyTaskData.newBuilder();
        taskData.setState(task.isState());
        taskData.setProgress(task.getProgress());
        taskData.setTaskID(task.getTaskID());
        return taskData.build();
    }

    private  FallingSkyMessage.FallSkyLevelData buildLevelData(FallingSkyLevel level){
        FallingSkyMessage.FallSkyLevelData.Builder levelData = FallingSkyMessage.FallSkyLevelData.newBuilder();
        levelData.setIsGetfeelReward(level.isGetFree());
        levelData.setIsGetpayReward(level.isGetPay());
        levelData.setLevelId(level.getLevelID());
        return levelData.build();
    }
    private long getWaitTime(long periodTime){
        long open_Day =  Global.FallingSky_Round_Open_Day * GlobalType.MILLIS_PER_DAY;
        long openTime = TimeUtils.getBeginTime(TimeUtils.getOpenServerTime()) + open_Day;
        long waitTime = 0;
        long nowTime = TimeUtils.Time();
        long intervalTime = nowTime < openTime ? 0:nowTime - openTime;
        int intervalDay = (int) (intervalTime / periodTime);
        if (intervalDay > 0) {
            int beyondTime = (int) (intervalTime % periodTime);
            waitTime = (nowTime - beyondTime) + periodTime;
        } else {
            waitTime = openTime + periodTime;
        }
        return waitTime;
    }

    /**
     * 重设开服时间后，天禁令默认改为第一轮
     */
    public void onOpenTimeChange(){
        long periodTime = Global.FallingSky_Round_Limit.get(0)* GlobalType.MILLIS_PER_DAY;
        long waitTime = getWaitTime(periodTime);
        Manager.fallingSkyManager.setRound(1);
        Manager.fallingSkyManager.setNextRoundTime(waitTime);
        ServerParamUtil.fallingSkyData.clear();
        ServerParamUtil.fallingSkyData.put(1, waitTime);
        ServerParamUtil.saveFallingSkyData();
    }
}
