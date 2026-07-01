package common.crazyweek;

import com.data.FunctionStart;
import com.game.crazyweek.script.ICrazyWeekScript;
import com.game.dailyactive.structs.DailyActiveDefine;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import game.core.net.Config.ServerConfig;
import game.core.util.TimeUtils;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author gaozhaoguang
 * @desc 周六狂欢
 * @date Created on 2020/9/8 10:21
 **/
public class CrazyWeekScript implements ICrazyWeekScript {

    @Override
    public void rechargeDeal(Player player, int rechargeNum) {

    }

    @Override
    public void initializeData() {
        getWeekFuncData();
    }

    @Override
    public void endActivity() {
        //0点的时候都关闭
        ConcurrentHashMap<Integer, int[]> newFuncs = getWeekFuncData();
        for (Map.Entry<Integer,int[]> e: newFuncs.entrySet()) {
            CloseFunc(e.getValue());
        }
    }

    @Override
    public void beginActivity() {

        int weekDay = TimeUtils.getDayOfWeek(TimeUtils.Time());
        ConcurrentHashMap<Integer, int[]> newFuncs = getWeekFuncData();
        OpenFunc(newFuncs.get(weekDay));
    }

    @Override
    public boolean funcIsOpen(int funcID){
        //当前是审核服的话,周六狂欢周一直开启,不做判断
        if(ServerConfig.getIsShenHe() > 0){
            return funcID == FunctionStart.CrazySat;
        }
        return Manager.crazyWeekManager.getOpenedFuncs().contains(funcID);
    }

    @Override
    public void reload() {

        //1.先把老的功能给关掉
        ConcurrentHashMap<Integer, int[]> oldFuncs = getWeekFuncData();
        for (Map.Entry<Integer,int[]> e: oldFuncs.entrySet()) {
            CloseFunc(e.getValue());
        }

        //2.在把新的功能给关掉
        clearWeekFuncData();
        ConcurrentHashMap<Integer, int[]> newFuncs = getWeekFuncData();
        for (Map.Entry<Integer, int[]> e : newFuncs.entrySet()) {
            CloseFunc(e.getValue());
        }

        //如果狂欢周开启,那么就开启当前的活动
        if(!Manager.dailyActiveManager.deal().isOpen(DailyActiveDefine.CRAZY_WEEK.getValue())) {
            //然后再根据新的功能进行开启
            int weekDay = TimeUtils.getDayOfWeek(TimeUtils.Time());
            OpenFunc(newFuncs.get(weekDay));
        }
    }

    @Override
    public int getId() {
        return ScriptEnum.CrazyWeekScript;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }

    /**
     * 初始化数据
     */
    private ConcurrentHashMap<Integer, int[]>  getWeekFuncData(){
        ConcurrentHashMap<Integer, int[]> weekMap = Manager.crazyWeekManager.getWeekDayFuncMap();
        if(weekMap.isEmpty())
        {//填充功能ID
            weekMap.put(1,new int[]{FunctionStart.CrazyMon});
            weekMap.put(2,new int[]{FunctionStart.CrazyTue});
            weekMap.put(3,new int[]{FunctionStart.CrazyWed});
            weekMap.put(4,new int[]{FunctionStart.CrazyThur});
            weekMap.put(5,new int[]{FunctionStart.CrazyFir});
            weekMap.put(6,new int[]{FunctionStart.CrazySat});
            weekMap.put(7,new int[]{FunctionStart.CrazySun});
        }
        return weekMap;
    }

    /**
     * 清理狂欢周数据
     */
    private void clearWeekFuncData(){
        ConcurrentHashMap<Integer, int[]> weekMap = Manager.crazyWeekManager.getWeekDayFuncMap();
        weekMap.clear();
    }

    /**
     * 开启功能
     * @param funcIDs
     */
    private void OpenFunc(int[] funcIDs){
        //1.判断数据是否为空
        if(funcIDs == null){
            return;
        }
        //2.设置开启
        boolean isChange = false;
        Set<Integer> allOpenFunc = Manager.crazyWeekManager.getOpenedFuncs();
        for (int i = 0; i <funcIDs.length ; i++) {
            if(!allOpenFunc.contains(funcIDs[i])){
                Manager.crazyWeekManager.getOpenedFuncs().add(funcIDs[i]);
                isChange = true;
            }
        }
        if(isChange) {
            notifyAllOnlinePlayer(funcIDs, true);
        }
    }

    /**
     * 关闭功能
     * @param funcIDs
     */
    private void CloseFunc(int[] funcIDs){
        //1.判断数据是否为空
        if(funcIDs == null){
            return;
        }
        //2.设置关闭
        boolean isChange = false;
        Set<Integer> allOpenFunc = Manager.crazyWeekManager.getOpenedFuncs();
        for (int i = 0; i <funcIDs.length ; i++) {
            if(allOpenFunc.contains(funcIDs[i])) {
                Manager.crazyWeekManager.getOpenedFuncs().remove(funcIDs[i]);
                isChange = true;
            }
        }
        if(isChange) {
            notifyAllOnlinePlayer(funcIDs, false);
        }
    }

    /**
     * 功能信息改变后的,发送给所有在线玩家
     * @param funcIDs
     * @param isOpen
     */
    private void notifyAllOnlinePlayer(int[] funcIDs,boolean isOpen){
        for (Player player: Manager.playerManager.getOnLines()) {
            Manager.controlManager.deal().changePlayerFunc(player,funcIDs);
            funcSwitchCallBack(player,funcIDs,isOpen);
        }
    }

    /**
     * 功能切换的回调
     * @param player
     * @param funcIDs
     * @param isOpen
     */
    private void funcSwitchCallBack(Player player,int[] funcIDs,boolean isOpen){
        for (int i = 0; i < funcIDs.length ; i++) {
            if(funcIDs[i] == FunctionStart.CrazySat){
                if (isOpen) {
                   // Manager.buffManager.deal().onAddBuff(player,player, 1001);
                } else {
                   // Manager.buffManager.deal().onRemoveBuff(player, 1001);
                }
            }
        }

    }
}
