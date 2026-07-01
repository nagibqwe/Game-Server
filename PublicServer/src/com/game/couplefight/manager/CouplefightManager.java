package com.game.couplefight.manager;

import com.data.bean.Cfg_Marry_battle_time_Bean;
import com.game.couplefight.script.ICouplefightScript;
import com.game.couplefight.structs.*;
import com.game.couplefight.timer.CouplefightGroupsTimer;
import com.game.couplefight.timer.CouplefightTimer;
import com.game.db.bean.CouplefightBean;
import com.game.db.bean.CouplefightGuessBean;
import com.game.db.bean.CouplefightTeamBean;
import com.game.db.dao.CouplefightDao;
import com.game.manager.Manager;
import com.game.script.ScriptEnum;
import game.core.command.CommandProcessor;
import game.core.script.IScript;
import game.core.util.JsonUtils;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * 仙侣对决管理器
 * @author  gzl
 * @date 2021-07-01
 */
public class CouplefightManager extends CommandProcessor {
    private final Logger logger = LogManager.getLogger(CouplefightManager.class);

    public ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    /**活动状态*/
    private volatile int status = 0;
    //状态链
    private int[] statusArr = {0,1,2,3,4,5,6,7,8,9,10,11};

    //活动状态
    /**关闭*/
    public static final int status_close = 0;
    /**报名*/
    public static final int status_apply = 1;
    /**准备海选赛*/
    public static final int status_select_pre = 2;
    /**海选赛*/
    public static final int status_select = 3;
    /**小组赛准备*/
    public static final int status_group_pre = 4;
    /**小组赛*/
    public static final int status_group = 5;
    /**冠军赛准备*/
    public static final int status_champion_pre = 6;
    /**地榜赛准备*/
    public static final int status_di_pre = 7;
    /**地榜赛*/
    public static final int status_di = 8;
    /**天榜赛准备*/
    public static final int status_tian_pre = 9;
    /**天榜赛*/
    public static final int status_tian = 10;
    /**结束*/
    public static final int status_over = 11;

    //奖励类型
    /**海选赛每场奖励*/
    public static final int award_select = 1;
    /**小组赛每场奖励*/
    public static final int award_group = 2;
    /**冠军赛每场奖励*/
    public static final int award_champion = 3;
    /**海选赛场次奖励*/
    public static final int award_select_count = 4;
    /**小组赛排名奖励*/
    public static final int award_group_rank = 5;
    /**冠军赛(地榜）排名奖励*/
    public static final int award_champion_di_rank = 6;
    /**冠军赛(天榜）排名奖励*/
    public static final int award_champion_tian_rank = 7;

    /**全局定时器*/
    private CouplefightTimer timer = new CouplefightTimer();

    /**配置信息*/
    private Cfg_Marry_battle_time_Bean beforeBean = null;
    private Cfg_Marry_battle_time_Bean currentBean = null;

    /**游戏场次信息*/
    private int game = -1;

    /**服务器 对应的服务器组*/
    private Map<String, Integer> servers = new ConcurrentHashMap<>();

    /**战斗中的房间信息*/
    private Map<Long, CoupleFightRoom> rooms = new ConcurrentHashMap<>();

    /**选拔赛数据 跨服组id-数据*/
    private Map<Integer, CoupleData> datas = new ConcurrentHashMap<>();

    private CouplefightDao dao = new CouplefightDao();

    public CouplefightManager() {
        super(CouplefightManager.class.getSimpleName());
    }

    public CoupleData getDatasByGroupId(int groupId) {
        CoupleData data = datas.get(groupId);
        return data;
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        CouplefightManager manager;

        Singleton() {
            this.manager = new CouplefightManager();
        }

        CouplefightManager getProcessor() {
            return manager;
        }
    }

    public static CouplefightManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    @Override
    public void writeError(String message) {
        logger.error(message);
    }

    @Override
    public void writeError(String message, Throwable t) {
        logger.error(message,t);
    }

    public ICouplefightScript getScript() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.CouplefightScript);
        if (is == null) {
            logger.error("未找到脚本CauplefightScript={}", ScriptEnum.CouplefightScript);
            return null;
        }
        return (ICouplefightScript) is;
    }

    public Integer getGroup(String serverId){
        return servers.get(serverId);
    }

    public Map<String, Integer> getServers() {
        return servers;
    }

    public Map<Long, CoupleFightRoom> getRooms() {
        return rooms;
    }

    public void setRooms(Map<Long, CoupleFightRoom> rooms) {
        this.rooms = rooms;
    }

    public void setServers(Map<String, Integer> servers) {
        this.servers = servers;
    }

    public int getStatus() {
        return status;
    }

    public CouplefightDao getDao() {
        return dao;
    }

    public int getNextStatus() {
        int next = status + 1;
        if(next > statusArr[statusArr.length - 1]){
            return 0;
        }
        return next;
    }


    public void setStatus(int status) {
        this.status = status;
    }

    public CouplefightTimer getTimer() {
        return timer;
    }

    public Map<Integer, CoupleData> getDatas() {
        return datas;
    }

    public void setDatas(Map<Integer, CoupleData> datas) {
        this.datas = datas;
    }

    /**
     * 初始化
     */
    public void init(){
        Manager.couplefightManager.getScript().doInit();
    }

    public Cfg_Marry_battle_time_Bean getBeforeBean() {
        return beforeBean;
    }

    public void setBeforeBean(Cfg_Marry_battle_time_Bean beforeBean) {
        this.beforeBean = beforeBean;
    }

    public Cfg_Marry_battle_time_Bean getCurrentBean() {
        return currentBean;
    }

    public void setCurrentBean(Cfg_Marry_battle_time_Bean currentBean) {
        this.currentBean = currentBean;
    }

    public int getGame() {
        return game;
    }

    public void setGame(int game) {
        this.game = game;
    }
}
