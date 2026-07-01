package com.game.commercialize.manager;

import com.data.FunctionStart;
import com.game.commercialize.inter.ICommercialize;
import com.game.commercialize.inter.IDailyRecharge;
import com.game.commercialize.struct.DailyRechargeActivity;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import game.core.script.IScript;
import game.core.script.ScriptManager;
import game.message.CommercializeMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 商业化管理类
 */
public class CommercializeManager {
    private static final Logger log = LogManager.getLogger(CommercializeManager.class);
    private enum Singleton {
        INSTANCE;
        CommercializeManager manager;

        Singleton() {
            this.manager = new CommercializeManager();
        }

        CommercializeManager getProcessor() {
            return manager;
        }
    }

    public static CommercializeManager getInstance() {
        return CommercializeManager.Singleton.INSTANCE.getProcessor();
    }

    private CommercializeManager() { }

    // 玩家每日累充数据
    private ConcurrentHashMap<Long, DailyRechargeActivity> dailyAccRechargeRecord = new ConcurrentHashMap<>();
    public ConcurrentHashMap<Long, DailyRechargeActivity> getDailyAccRechargeRecord() {
        return dailyAccRechargeRecord;
    }
    public void setDailyAccRechargeRecord(ConcurrentHashMap<Long, DailyRechargeActivity> dailyAccRechargeRecord) {
        this.dailyAccRechargeRecord = dailyAccRechargeRecord;
    }
    public DailyRechargeActivity getPlayerDailyAccRecharge(long roleId) {
        return dailyAccRechargeRecord.get(roleId);
    }

    /**
     * 玩家上线
     * @param player
     */
    public void playerOnline(Player player) {
        for (int i = CommercializeMessage.Commercialize.TypeStart_VALUE + 1; i < CommercializeMessage.Commercialize.TypeEnd_VALUE; i++) {
            playerOnline(player, CommercializeMessage.Commercialize.valueOf(i));
        }
    }

    public void playerOnline(Player player, CommercializeMessage.Commercialize commercialize) {
        ICommercialize iScript = getScript(commercialize);
        if (iScript == null)
            return;

        // 上线刷新
        iScript.playerOnline(player);

        // 推送数据
        iScript.onReqCommercialize(player);
    }

    /**
     * 充值了一笔钱
     * @param player
     * @param commercializeID
     * @param gold
     */
    public void recharge(Player player, int commercializeID, int gold,int totalFee) {
        log.info(player.getInfo() + " 收到充值，商业ID: " + commercializeID + " gold:" + gold);
        for (int i = CommercializeMessage.Commercialize.TypeStart_VALUE + 1; i < CommercializeMessage.Commercialize.TypeEnd_VALUE; i++) {
            ICommercialize iScript = getScript(CommercializeMessage.Commercialize.valueOf(i));
            if (iScript == null)
                continue;

            // 充值了一笔钱
            iScript.recharge(player, commercializeID, gold, totalFee);
        }
    }

    /**
     * 获取商业化脚本
     * @param commercialize
     * @return
     */
    private int getScriptID(CommercializeMessage.Commercialize commercialize) {
        switch (commercialize) {
            case DailyRecharge:
                return ScriptEnum.DailyRechargeBaseScript;
            case FCCharge:
                return ScriptEnum.FCChargeBaseScript;
            case NewDailyRecharge:
                return ScriptEnum.DailyRechargeTotalScript;
            default:
                return -1;
        }
    }

    /**
     * 活动是否开启
     * @param player
     * @param commercialize
     * @return
     */
    public boolean isOpen(Player player, CommercializeMessage.Commercialize commercialize) {
        int iSwitch = -1;
        switch (commercialize) {
            case DailyRecharge:
                iSwitch = FunctionStart.DailyRechargeForm;
                break;
            case FCCharge:
                return Manager.controlManager.deal().isOpenFunction(player, FunctionStart.FirstCharge)
                        || Manager.controlManager.deal().isOpenFunction(player, FunctionStart.ReCharge);
            default:
        }

        if (iSwitch == -1)
            return false;

        return Manager.controlManager.deal().isOpenFunction(player, iSwitch);
    }

    /**
     * 商业化脚本
     * @param commercialize
     * @return
     */
    public ICommercialize getScript(CommercializeMessage.Commercialize commercialize) {
        int id = getScriptID(commercialize);
        if (id == -1) {
            log.error("商业化类型错误：" + commercialize.getNumber());
            return null;
        }

        IScript script = ScriptManager.getInstance().GetScriptClass(id);
        if (script == null) {
            log.error("没有找到该商业化类型的脚本：" + commercialize.getNumber() + " 脚本ID：" + id);
            return null;
        }
        return (ICommercialize) script;
    }

    /**
     * 每日累充脚本
     * @return
     */
    public IDailyRecharge dailyRecharge() {
        IScript drs = Manager.scriptManager.GetScriptClass(ScriptEnum.DailyRechargeBaseScript);
        if (drs instanceof IDailyRecharge) {
            return (IDailyRecharge) drs;
        }
        log.error("没有找到具体的接口实现类！不会走到这里，请注意实现！dailyRecharge");
        return null;
    }
}
