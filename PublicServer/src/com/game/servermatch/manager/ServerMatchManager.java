package com.game.servermatch.manager;

import com.game.gameserver.structs.ServerInfo;
import com.game.servermatch.script.IServerMatchManagerScript;
import com.game.servermatch.structs.AxisEvent;
import com.game.servermatch.structs.ConditionParser;
import com.game.servermatch.structs.GameServerInfo;
import com.game.servermatch.structs.LeagueConfig;
import com.game.servermatch.structs.TimeAxis;
import com.game.servermatch.structs.TimeEventBean;
import com.game.servermatch.structs.TriggerParser;
import com.game.manager.Manager;
import com.game.script.ScriptEnum;
import com.game.utils.ServerParamUtil;
import game.core.script.IScript;
import game.core.util.TimeUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 服务器分组
 * @author cxl
 */
public class ServerMatchManager {
    
    private final static Logger log = LogManager.getLogger(ServerMatchManager.class);
    //管理的服务器列表
    public static HashMap<String,GameServerInfo> infos = new HashMap<>();

    //GM后台设置的匹配分组服务器列表信息 key1 -> 大组ID key2 groupId ->服务器列表 serverKey
    public static HashMap<Integer,HashMap<Integer,List<String>>> gm_OperatingGroup = new HashMap<>();

    //大组ID->stageID->stage内的组ID-list<gameinfo>
    public static ConcurrentHashMap<Integer,ConcurrentHashMap<Integer,ConcurrentHashMap<Integer,List<GameServerInfo>>>> serverStageGrouplist = new ConcurrentHashMap<>();

    //匹配中的服务器数据
    public static HashMap<Integer,List<String>> groupIDWithServerKey = new HashMap<>();

    public static TimeAxis timeAxis = new TimeAxis();  //保存到系统表里

    public static int matchMonth ;//当月匹配的月份
    
    public static boolean isLoad = false;//是否加载完成


    //六个阶段服务器匹配数量
    public static final int serverMatchStage_1  = 1;
    public static final int serverMatchStage_2  = 2;
    public static final int serverMatchStage_4  = 4;
    public static final int serverMatchStage_8  = 8;
    //public static final int serverMatchStage_16 = 16;
    //public static final int serverMatchStage_32 = 32;


    //跨服模式：0：所有的服务器都需要满足对应的等级，否则活动都不能进入。1：达到要求的平均等级的服务器可以进入。2：达到平均等级后，对应的跨服的所有服务器的玩家均可进入。
    public static final int crossMatchType_0 = 0;
    public static final int crossMatchType_1 = 1;
    public static final int crossMatchType_2 = 2;  //TODO 由于跨服机制改了  2暂时废弃
    
    
    //启动服务器加载数据
    public static void loadAll(){
        //获取时间
        for (TimeEventBean time : LeagueConfig.timeBeans) {
            //创建一个放入
            timeAxis.addAxisEvent(initTimeEvent(time));
        }
//        ServerParamUtil.immediateSave(timeAxisStr, JSON.toJSONString(timeAxis));
        deal().markOff(null);//划分服务器
        isLoad = true;
    }

    
    //服务器停止
    public static void stop(){
        ServerParamUtil.savaGameServerInfo();
        ServerParamUtil.saveOperatingGroup();
        log.info("服务器关闭，保存时间事件数据完成：");
    }

    public static void addGameServer(ServerInfo info){
        if(info != null){
            boolean isNeedRepair = false;
            String skey = makeKey(info.getPlatName(),info.getServerId());
            if(infos.containsKey(skey)){
                //是否有合并的服务器
                infos.get(skey).setServerWorldLv(info.getServerWorldLv());
                infos.get(skey).setOpenTime(info.getOpenTime());
                infos.get(skey).setServerIp(info.getServerIp());
                infos.get(skey).setSids(info.getSids());
                if (infos.get(skey).getFirstConnectTime() <=0){
                    infos.get(skey).setFirstConnectTime(TimeUtils.Time());
                }
                for(Integer id : info.getSids()){
                    if(id == info.getServerId()){
                        continue;
                    }
                    String key = makeKey(info.getPlatName(),id);
                    if(infos.containsKey(key)){
                        GameServerInfo g =  infos.get(key);
                        g.setIsMerge(true);//合过服的标记为合服
                        isNeedRepair =true;
                        //infos.remove(key);
                    }
                }
            }else{
                infos.put(skey, buildGameServer(info));
            }

            deal().markOff(isNeedRepair ==true ? info : null);//加入一个进来对服务器从新分组，万一有合服情况，将会从新分配新的goupid

            ServerParamUtil.savaGameServerInfo();
        }
    }
    //移除server
    public static GameServerInfo buildGameServer(ServerInfo info){
        GameServerInfo gameInfo = new GameServerInfo();
        gameInfo.setOpenTime(info.getOpenTime());
        gameInfo.setPlatName(info.getPlatName());
        gameInfo.setPort(info.getPort());
        gameInfo.setServerId(info.getServerId());
        gameInfo.setServerIp(info.getServerIp());
        gameInfo.setServerType(info.getServerType());
        gameInfo.setSids(info.getSids());
        gameInfo.setServerWorldLv(info.getServerWorldLv());
        gameInfo.setFirstConnectTime(TimeUtils.Time());
        return gameInfo;
    }
    //拷贝一个新的
    public static GameServerInfo buildGameServer(GameServerInfo info){
        GameServerInfo gameInfo = new GameServerInfo();
        gameInfo.setOpenTime(info.getOpenTime());
        gameInfo.setPlatName(info.getPlatName());
        gameInfo.setPort(info.getPort());
        gameInfo.setServerId(info.getServerId());
        gameInfo.setServerIp(info.getServerIp());
        gameInfo.setServerType(info.getServerType());
        gameInfo.setSids(info.getSids());
        gameInfo.setServerWorldLv(info.getServerWorldLv());
        gameInfo.setIsMerge(info.getIsMerge());
        gameInfo.setbigGroupID(info.getbigGroupID());
        gameInfo.setFirstConnectTime(info.getFirstConnectTime());
        gameInfo.stageWithGroupIndex = info.stageWithGroupIndex;
        return gameInfo;
    }


    public static String makeKey(String plat, int sid) {
        return plat + "_" + sid;
    }

    
    public static AxisEvent initTimeEvent(TimeEventBean time){
        AxisEvent event = new AxisEvent();
        event.id = time.getTrigger();
        event.repeat = time.isRepeat();
        event.nature = time.getNature();
        event.spaced = time.getSpaced();
        long now = TimeUtils.Time();
        long next = time.getInitial();
        event.startup = next;
//        long initialDelay = next > now ? next : now;
        
        if (event.spaced > 0) {
            if(next > now){
                event.startup = next;
            }else{
                event.startup += event.spaced; 
            }
        }
        if(event.nature>0){
            event.startup = LeagueConfig.getNature(event.nature);
        }
        log.info("初始的时间="+ TimeUtils.format2string(next, "yyyy-MM-dd HH:mm:ss")+" 计算的时间="+TimeUtils.format2string(event.startup, "yyyy-MM-dd HH:mm:ss"));
//        event.startup = initialDelay;
        event.setTriggr(TriggerParser.parseType(time.getTrigger()));
        event.setCondition(ConditionParser.parseType(time.getCondition()));
        return event;
    }


    public static IServerMatchManagerScript deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.ServerMatchManagerScript);
        if (is instanceof IServerMatchManagerScript) {
            return (IServerMatchManagerScript) is;
        }
        throw new NullPointerException("公共服没有实现联赛系统！");
    }
}
