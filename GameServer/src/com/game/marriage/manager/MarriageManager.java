package com.game.marriage.manager;

import com.data.MessageString;
import com.game.db.bean.MarryBean;
import com.game.db.dao.MarryDao;
import com.game.manager.Manager;
import com.game.marriage.command.AgreeInvitCommand;
import com.game.marriage.command.InvitCommand;
import com.game.marriage.command.PurInvitNumCommand;
import com.game.marriage.command.SelectWeddingCommand;
import com.game.marriage.script.IMarriageScript;
import com.game.marriage.script.IMarryActivityScript;
import com.game.marriage.script.IMarryCloneScript;
import com.game.marriage.script.IMarryWallScript;
import com.game.marriage.struct.Marriage;
import com.game.marriage.struct.MarryDeclaration;
import com.game.marriage.struct.WeddingMapInfo;
import com.game.marriage.timer.MarriageTimer;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerWorldInfo;
import com.game.script.structs.ScriptEnum;
import com.game.server.DbSqlName;
import com.game.server.GameServer;
import com.game.utils.ServerParamUtil;
import game.core.script.IScript;
import game.core.util.TimeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description
 * @auther admin
 * @create 2020-06-02 15:59
 */
public class MarriageManager {

    private static final Logger log = LogManager.getLogger(MarriageManager.class);

    private long curMarriageId = 0;

    final ConcurrentHashMap<Long, Marriage> marriageList = new ConcurrentHashMap<>();

    final ConcurrentHashMap<Long, MarryDeclaration> declarations = new ConcurrentHashMap<>();   //爱情宣言数据
    final ConcurrentHashMap<Long, Integer> intimacyRank = new ConcurrentHashMap<>();

    private WeddingMapInfo wedding;

    public ConcurrentHashMap<Long, Marriage> getMarriageList() {
        return marriageList;
    }

    /**
     * 玩家情缘商店购买记录
     */
    final ConcurrentHashMap<Long, ConcurrentHashMap<Integer,Integer>> marryActivityShopBuyCountMap = new ConcurrentHashMap<>();

    public ConcurrentHashMap<Long, ConcurrentHashMap<Integer,Integer>> getMarryActivityShopBuyCountMap() {
        return marryActivityShopBuyCountMap;
    }

    public ConcurrentHashMap<Long, MarryDeclaration> getDeclarations() {
        return declarations;
    }

    public long getCurMarriageId() {
        return curMarriageId;
    }

    public void setCurMarriageId(long curMarriageId) {
        this.curMarriageId = curMarriageId;
    }

    public WeddingMapInfo getWedding() {
        return wedding;
    }

    public void setWedding(WeddingMapInfo wedding) {
        this.wedding = wedding;
    }

    public ConcurrentHashMap<Long, Integer> getIntimacyRank() {
        return intimacyRank;
    }

    public static MarriageManager getInstance() {
        return MarriageManager.Singleton.INSTANCE.getProcessor();
    }

    private enum Singleton {

        INSTANCE;
        MarriageManager processor;

        Singleton() {
            this.processor = new MarriageManager();
        }

        MarriageManager getProcessor() {
            return processor;
        }
    }

    public IMarriageScript manager() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.MarriageScript);
        if (is instanceof IMarriageScript) {
            return (IMarriageScript) is;
        } else {
            log.error("没有找到具体的接口实现类！不会走到这里，请注意实现！");
            return null;
        }
    }

    public IMarryWallScript wall() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.MarriageScript);
        if (is instanceof IMarryWallScript) {
            return (IMarryWallScript) is;
        } else {
            log.error("没有找到具体的接口实现类！不会走到这里，请注意实现！");
            return null;
        }
    }

    public IMarryCloneScript clone() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.MarryHoneyScript);
        if (is instanceof IMarryCloneScript) {
            return (IMarryCloneScript) is;
        } else {
            log.error("没有找到具体的接口实现类！不会走到这里，请注意实现！");
            return null;
        }
    }

    /**
     * 情缘活动脚本
     * @return
     */
    public IMarryActivityScript activity() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.MarryActivityScript);
        if (is instanceof IMarryActivityScript) {
            return (IMarryActivityScript) is;
        } else {
            log.error("没有找到具体的接口实现类！不会走到这里，请注意实现！");
            return null;
        }
    }

    public void reqSelectMarriage(Player player, int id) {
        GameServer.getInstance().getAssistThread().addCommand(new SelectWeddingCommand(player, id));
    }

    public void reqPurInvitNum(Player player) {
        GameServer.getInstance().getAssistThread().addCommand(new PurInvitNumCommand(player));
    }

    public void reqInvit(Player player, long roleId, int type) {
        GameServer.getInstance().getAssistThread().addCommand(new InvitCommand(player, roleId, type));
    }

    public void reqAgreeInvit(Player player, long roleId, boolean isAgree) {
        GameServer.getInstance().getAssistThread().addCommand(new AgreeInvitCommand(player, roleId, isAgree));
    }


    public void load() {
        log.info("开始加载所有婚姻数据...");
        MarryDao dao = new MarryDao();
        List<MarryBean> beans = dao.selectAll();
        if (beans != null) {
            for (MarryBean bean : beans) {
                Marriage marriage = bean.toMarriage(bean);
                if (marriage == null) {
                    log.error("婚姻数据异常");
                    continue;
                }
                getMarriageList().put(marriage.getId(), marriage);
            }
        }
        log.info("加载婚姻数据结束... len={}", this.marriageList.size());

        List<MarryDeclaration> marryDeclarations = dao.selectAllDeclaration();
        if (marryDeclarations != null) {
            for (MarryDeclaration md : marryDeclarations) {
                this.getDeclarations().put(md.getRoleId(), md);
            }
        }
        log.info("加载爱情宣言结束 len={}...", this.getDeclarations().size());

        int nowMin = (int) (TimeUtils.TimeSec() / 60);
        Iterator<Map.Entry<Long, Integer>> iter = ServerParamUtil.weddingList.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<Long, Integer> entry = iter.next();
            Marriage marriage = Manager.marriageManager.getMarriageList().get(entry.getKey());

            if (marriage == null) {
                iter.remove();
                log.error("婚宴数据不存在:" + entry.getValue());
                continue;
            }
            if (entry.getValue() < nowMin) {
                iter.remove();
                marriage.setNum(marriage.getNum() - 1);
                dao.update(DbSqlName.MARRIAGE_UPDATE.getName(), marriage.toMarrayBean());

                Manager.mailManager.sendMailToPlayer(marriage.getMarriageId(), 1, MessageString.System, MessageString.Marry_Dinner_Return_Mail_Title, MessageString.Marry_Dinner_Return_Mail);
                Manager.mailManager.sendMailToPlayer(marriage.getBeMarriageId(), 1, MessageString.System, MessageString.Marry_Dinner_Return_Mail_Title, MessageString.Marry_Dinner_Return_Mail);
            }
        }
        GameServer.getInstance().getAssistThread().addTimerEvent(new MarriageTimer());
    }

    public PlayerWorldInfo getMarryTarget(Player player) {

        Marriage marriage = Manager.marriageManager.getMarriageList().get(player.getMarriageUid());
        if (marriage == null) {
            return null;
        }
        long roleId = 0;
        if (marriage.getMarriageId() == player.getId()) {
            roleId = marriage.getBeMarriageId();
        }

        if (marriage.getBeMarriageId() == player.getId()) {
            roleId = marriage.getMarriageId();
        }
        return Manager.playerManager.getPlayerWorldInfo(roleId);
    }
}
