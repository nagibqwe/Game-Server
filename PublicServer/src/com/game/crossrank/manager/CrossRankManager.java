package com.game.crossrank.manager;

import com.game.crossrank.scripts.ICrossRank;
import com.game.crossrank.timer.CrossWorldLvTimer;
import com.game.db.bean.CrossRankBean;
import com.game.db.dao.CrossRankDao;
import com.game.manager.Manager;
import com.game.script.ScriptEnum;
import com.game.server.MainServer;
import com.game.servermatch.manager.ServerMatchManager;
import com.game.servermatch.structs.GameServerInfo;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by cxl on 2020/4/9.
 */
public class CrossRankManager {

    private final static Logger log = LogManager.getLogger(CrossRankManager.class);

    public static int LEVEL_RANK = 101;//等级排行

    public static int FIGHT_RANK  = 102;//战力排行

    public static int RANK_MAX = 50; ///    排行榜最大排行

    public static int SERVER_MAX = 10; //单服入选最多10个

    public static int MAX_CROSS_LEVEL = 5;//跨服排行只取前五



    private CrossRankDao dao = new CrossRankDao();

    private ConcurrentHashMap<Long, CrossRankBean> allCrosRankMap  = new ConcurrentHashMap<>();

    private ConcurrentHashMap<Long,CrossRankBean> allLevelCrossRankBeanMap = new ConcurrentHashMap<>();

    private ConcurrentHashMap<Long,CrossRankBean> allFightCrossRankBeanMap = new ConcurrentHashMap<>();

    private  ConcurrentHashMap<Integer,ConcurrentHashMap<Long,CrossRankBean>> serverIdKeyCrossRankMap = new ConcurrentHashMap<>();


    public void stop(){
        deal().updateSql();
        log.info("public服务器停止了！写入跨服排行数据");
    }

    public  void loadCrossRank(){
        initTimer();
        List<CrossRankBean>  crossRankBeans =  Manager.crossRankManager.getDao().selectAll();
        if (crossRankBeans == null  || crossRankBeans.size()<=0)
            return;
        ConcurrentHashMap<Long, CrossRankBean> serverCrossData = null;
        List<GameServerInfo> newAllServerlist = new ArrayList<>(ServerMatchManager.infos.values());
        HashMap<Integer,GameServerInfo> serverInfoHashMap = new HashMap<>();
        for (GameServerInfo g:  newAllServerlist){
            serverInfoHashMap.put(g.getServerId() ,g);
        }
        for (CrossRankBean bean : crossRankBeans){
            if (serverInfoHashMap.containsKey(bean.getServerId())){
                if ( serverInfoHashMap.get(bean.getServerId()).getIsMerge()){
                    continue;
                }
            }
            if ( serverIdKeyCrossRankMap.containsKey( bean.getServerId())){
                serverCrossData =  serverIdKeyCrossRankMap.get(bean.getServerId());
                serverCrossData.put(bean.getRoleId(),bean);
            }else{
                serverCrossData = new ConcurrentHashMap();
                serverCrossData.put(bean.getRoleId(),bean);
                serverIdKeyCrossRankMap.put(bean.getServerId(),serverCrossData);
            }
        }
        initRank();
    }

    public void initRank(){
      deal().crossRankSort();
    }

    private void initTimer(){
        log.info("跨服世界等级 timer 初始化成功");
        MainServer.getInstance().addTimerEvent(new CrossWorldLvTimer(-1, 60000));
    }

    private enum Singleton {

        INSTANCE;
        CrossRankManager manager;

        Singleton() {
            this.manager = new CrossRankManager();
        }

        CrossRankManager getProcessor() {
            return manager;
        }
    }

    public static CrossRankManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    public ICrossRank deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.CrossRankScript);
        if (is instanceof ICrossRank) {
            return (ICrossRank) is;
        } else {
            return null;
        }
    }

    public CrossRankDao getDao() {
        return dao;
    }

    public void setDao(CrossRankDao dao) {
        this.dao = dao;
    }

    public ConcurrentHashMap<Long, CrossRankBean> getAllLevelCrossRankBeanMap() {
        return allLevelCrossRankBeanMap;
    }

    public void setAllLevelCrossRankBeanMap(ConcurrentHashMap<Long, CrossRankBean> allLevelCrossRankBeanMap) {
        this.allLevelCrossRankBeanMap = allLevelCrossRankBeanMap;
    }

    public ConcurrentHashMap<Long, CrossRankBean> getAllFightCrossRankBeanMap() {
        return allFightCrossRankBeanMap;
    }

    public void setAllFightCrossRankBeanMap(ConcurrentHashMap<Long, CrossRankBean> allFightCrossRankBeanMap) {
        this.allFightCrossRankBeanMap = allFightCrossRankBeanMap;
    }

    public ConcurrentHashMap<Integer, ConcurrentHashMap<Long, CrossRankBean>> getServerIdKeyCrossRankMap() {
        return serverIdKeyCrossRankMap;
    }

    public void setServerIdKeyCrossRankMap(ConcurrentHashMap<Integer, ConcurrentHashMap<Long, CrossRankBean>> serverIdKeyCrossRankMap) {
        this.serverIdKeyCrossRankMap = serverIdKeyCrossRankMap;
    }

    public ConcurrentHashMap<Long, CrossRankBean> getAllCrosRankMap() {
        return allCrosRankMap;
    }

    public void setAllCrosRankMap(ConcurrentHashMap<Long, CrossRankBean> allCrosRankMap) {
        this.allCrosRankMap = allCrosRankMap;
    }
}
