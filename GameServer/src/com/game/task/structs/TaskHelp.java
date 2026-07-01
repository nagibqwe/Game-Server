package com.game.task.structs;

import com.game.player.structs.Player;

/**
 *
 * @author admin
 */
public class TaskHelp {
    
    //任务相关
    public static String TaskMax = "数量上限";
    public static String HaveTask = "当前有任务";
    public static String HaveFinish = "已经完成";
    public static String NoOpen = "未开启";
    public static String NoFind = "未找到";
    public static String NoTask = "没有任务";
    public static String TaskFinish = "任务完成";
    public static String SuccessAccept = "成功接取";
    public static String SuccessSubmit = "成功提交";
    public static String RequestAccept = "请求接取";
    public static String RequestSubmit = "请求提交";
    public static String RequestRefresh = "请求刷新任务";
    public static String RequsetOneKeyFinish = "请求一键完成";                                  //请求的一次完成一个任务
    public static String RequestOneKeyFinishAllTask = "请求一键完成所有";                      //请求的是一次完成某个类型的任务
    public static String NoTaskCanReceive = "当前条件下没有可接任务";
    
    //配置相关
    public static String ConfigNoTask = "找不到任务配置";
    public static String ConfigRewardError = "奖励配置出错";
    public static final String [] taskName = {"主线任务","日常任务","帮会任务","支线任务","","","边界任务","战场任务","转职任务","引导任务","","环任务","护送任务"};
    
    //玩家基本信息字符串化
    public static String getPlayerInfo(Player player){
        return player.getInfo();
    }
    
    public static String getLog(Player player, int type, String reason){
        return getPlayerInfo(player) + taskName[type] + reason;
    }
    
    public static String getLog(Player player, int type, String reason, int modelId){
        return getLog(player, type, reason) + "(任务Id：" + modelId + ")";
    }
    
    public static String configError(int type, int modelId, String params){
        return taskName[type] + params + "(任务Id: " + modelId + ")";
    }
    
    public static String getPlayerCondition(Player player){
        return "玩家当前情况，等级(" + player.getLevel() + ") 职业(" + player.getCareer() +")";
    }
    
}
