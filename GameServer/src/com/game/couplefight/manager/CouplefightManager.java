package com.game.couplefight.manager;

import com.data.MessageString;
import com.game.chat.structs.Notify;
import com.game.couplefight.scripts.ICoupleEscort;
import com.game.couplefight.scripts.ICoupleShop;
import com.game.couplefight.scripts.ICouplefightScript;
import com.game.couplefight.struct.CoupleEscortData;
import com.game.couplefight.struct.PreTeam;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.team.structs.TeamInfo;
import com.game.utils.MessageUtils;
import com.game.utils.Utils;
import game.core.script.IScript;
import game.message.CouplefightMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Auther: gouzhongliang
 * @Date: 2021/7/1 15:44
 */
public class CouplefightManager {

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


    private  boolean isStart = false;//护送活动是否开启

    static final Logger log = LogManager.getLogger(CouplefightManager.class);

    public static CouplefightManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    /**
     * 准备报名的玩家信息
     */
    private Map<Long, PreTeam> preApplys = new ConcurrentHashMap<>();

    /**开启的功能*/
    private Set<Integer> funcIds = new HashSet<>();

    /**活动状态*/
    private int status;

    private HashMap<Long, CoupleEscortData> coupleEscortDatas = new HashMap<>();

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Set<Integer> getFuncIds() {
        return funcIds;
    }

    public void setFuncIds(Set<Integer> funcIds) {
        this.funcIds = funcIds;
    }

    public Map<Long, PreTeam> getPreApplys() {
        return preApplys;
    }

    public HashMap<Long, CoupleEscortData> getCoupleEscortDatas() {
        return coupleEscortDatas;
    }

    public void setCoupleEscortDatas(HashMap<Long, CoupleEscortData> coupleEscortDatas) {
        this.coupleEscortDatas = coupleEscortDatas;
    }


    public ICoupleShop getCoupleShop(){
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.CoupleShopScript);
        if (is instanceof ICoupleShop) {
            return (ICoupleShop) is;
        } else {
            log.error("没有找到具体的接口实现类！不会走到这里，请注意实现！");
            return null;
        }
    }

    public ICouplefightScript getScript(){
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.CouplefightScript);
        if (is instanceof ICouplefightScript) {
            return (ICouplefightScript) is;
        } else {
            log.error("没有找到具体的接口实现类！不会走到这里，请注意实现！");
            return null;
        }
    }

    public ICoupleEscort getCoupleEscort(){
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.CoupleEscortScript);
        if (is instanceof ICoupleEscort) {
            return (ICoupleEscort) is;
        } else {
            log.error("没有找到具体的接口实现类！不会走到这里，请注意实现！");
            return null;
        }
    }

    public void init() {
        getScript().init();
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

    public boolean isStart() {
        return isStart;
    }

    public void setStart(boolean start) {
        isStart = start;
    }
}
