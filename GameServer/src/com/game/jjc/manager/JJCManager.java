package com.game.jjc.manager;

import com.game.jjc.script.IJJCCloneScript;
import com.game.jjc.script.IJJCHandler;
import com.game.jjc.structs.JJC;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.utils.MessageUtils;
import game.core.script.IScript;
import java.util.concurrent.ConcurrentHashMap;

import game.message.JJCMessage;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;

public class JJCManager {

    public final Logger log = LogManager.getLogger(JJCManager.class);

    //所有缓存
    private final ConcurrentHashMap<Long, JJC> alls = new ConcurrentHashMap<>();

    //不分职业排名
    private final ConcurrentHashMap<Integer,JJC> allRanks = new ConcurrentHashMap<>();
    /***
     * 限制同一时间玩家不能被同时挑战
     * key是玩家id，value是挑战结束时间
     * */
    private final ConcurrentHashMap<Long, Long> challengedLock = new ConcurrentHashMap<>();
    /**
     * 竞技场玩家
     * */
    public static final int JJC_Player = 1;
    /**
     * 竞技场机器人
     * */
    public static final int JJC_Robert = 2;
    /**
     * 竞技场开始标志
     * */
    public static final int JJC_Begin = 3;
    /**
     * 竞技场结束标志
     * */
    public static final int JJC_End = 4;

    /**
     * 竞技场每轮初始化时间标记
     * */
    public static final int JJC_RoundTime = 5;
    /**
     * 副本配置ID
     * */
    public static final int modelId = 8000;

    /**
     * 机器人与真实玩家ID 分界线
     */
    public static final int RobotMaxID = 5000001;

    /**
     * 用枚举来实现单例
     */
    public enum Singleton {

        INSTANCE;
        JJCManager manager;

        Singleton() {
            this.manager = new JJCManager();
        }

        JJCManager getProcessor() {
            return manager;
        }
    }

    //JJCManager
    public static JJCManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    public IJJCHandler deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.JJCManagerScriptBaseScript);
        if (is instanceof IJJCHandler) {
            return (IJJCHandler) is;
        } else {
            log.error("没有找到具体的实例！");
            return null;
        }
    }

    public IJJCCloneScript scriptclone() throws Exception {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.JJCCloneActivityScript);
        if (is instanceof IJJCCloneScript) {
            return (IJJCCloneScript) is;
        }
        throw new Exception("没有实现竞技场的脚本！");
    }

    public ConcurrentHashMap<Long, JJC> getAlls() {
        return alls;
    }

    public  ConcurrentHashMap<Integer,JJC> getAllRanks() {
        return allRanks;
    }

    public ConcurrentHashMap<Long, Long> getChallengedLock() {
        return challengedLock;
    }

    public void sendFirstRewardInfoToClient(Player player) {
        JJCMessage.ResGetFirstReward.Builder getFirstRewardBuiler = JJCMessage.ResGetFirstReward.newBuilder();
        getFirstRewardBuiler.setRank(player.getJjcHistoryMaxRank());
        getFirstRewardBuiler.addAllRewardList(player.getJjcFirstRewardList());
        MessageUtils.send_to_player(player, JJCMessage.ResGetFirstReward.MsgID.eMsgID_VALUE, getFirstRewardBuiler.build().toByteArray());
    }
}
