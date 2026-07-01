package com.game.recharge.manager;

import com.game.db.bean.RechargeBean;
import com.game.db.bean.RechargeTotalMoneyBean;
import com.game.db.dao.RechargeDao;
import com.game.db.dao.RechargeTotalMoneyDao;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.recharge.handler.PlayerRechargeHandler;
import com.game.recharge.script.IDiscountRechargeScript;
import com.game.recharge.script.IRechargeReward;
import com.game.recharge.script.IRechargeScript;
import com.game.recharge.structs.Recharge;
import com.game.recharge.structs.RechargeDefine;
import com.game.recharge.structs.RechargeItemInfo;
import com.game.recharge.structs.ServerOrderData;
import com.game.script.structs.ScriptEnum;
import game.core.command.CommandProcessor;
import game.core.script.IScript;
import io.netty.channel.Channel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @explain: 充值管理器
 * @time Created on 2019/11/21 15:32.
 * @author: tc
 */
public class RechargeManager extends CommandProcessor {
    private final Logger log = LogManager.getLogger("RechargeManager");
    private final RechargeDao dao = new RechargeDao();

    // 充值Map
    private ConcurrentHashMap<String, RechargeBean> rechargeMap = new ConcurrentHashMap<>();
    // 玩家的充值记录
    private ConcurrentHashMap<Long, List<String>> playerRechargeMap = new ConcurrentHashMap<>();

    public RechargeManager(String threadName) {
        super(threadName);
    }


    public String md5 = "";//验证版本

    public TreeMap<Integer, RechargeItemInfo> rechargeItemInfoMap = new TreeMap<>();//所有商品

    //商品Json
    private String rechargeItemJson = "";

    private ConcurrentHashMap<String,ServerOrderData> serverOrderMap = new ConcurrentHashMap<>();




    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        INSTANCE;
        RechargeManager manager;

        Singleton() {
            this.manager = new RechargeManager("RechargeManager");
        }

        RechargeManager getProcess() {
            return manager;
        }
    }


    public ConcurrentHashMap<String, ServerOrderData> getServerOrderMap() {
        return serverOrderMap;
    }

    public void setServerOrderMap(ConcurrentHashMap<String, ServerOrderData> serverOrderMap) {
        this.serverOrderMap = serverOrderMap;
    }

    public static RechargeManager getInstance() {
        return Singleton.INSTANCE.getProcess();
    }

    public RechargeDao getDao() {
        return dao;
    }

    public ConcurrentHashMap<String, RechargeBean> getRechargeMap() {
        return rechargeMap;
    }

    public ConcurrentHashMap<Long, List<String>> getPlayerRechargeMap() {
        return playerRechargeMap;
    }

    public String getRechargeItemJson() {
        return rechargeItemJson;
    }

    public void setRechargeItemJson(String rechargeItemJson) {
        this.rechargeItemJson = rechargeItemJson;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public TreeMap<Integer, RechargeItemInfo> getRechargeItemInfoMap() {
        return rechargeItemInfoMap;
    }

    public void setRechargeItemInfoMap(TreeMap<Integer, RechargeItemInfo> rechargeItemInfoMap) {
        this.rechargeItemInfoMap = rechargeItemInfoMap;
    }



    /**
     * 获取玩家的充值列表
     * @param roleID
     * @return
     */
    public List<RechargeBean> getRechargeList(long roleID) {
        List<RechargeBean> list = new ArrayList<>();
        List<String> orderlist = getPlayerRechargeMap().get(roleID);
        if (orderlist == null)
            return list;

        for (String order_id : orderlist) {
            RechargeBean bean = getRechargeMap().get(order_id);
            if (bean == null) {
                log.error(roleID + " have error order_id:" + order_id);
                continue;
            }
            if (bean.getStatus().equals(RechargeDefine.STA_ERROR)) continue;
            list.add(bean);
        }
        return list;
    }

    /**
     * 脚本接口
     *
     * @return Script
     */
    public IRechargeScript deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.RechargeManagerBaseScript);
        if (is  == null) {
            return  null;
        }
        return (IRechargeScript)is;
    }

    /**
     * 超值折扣充值脚本
     * @return
     */
    public IDiscountRechargeScript discountScript() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.DiscountRechargeScript);
        if (is  == null) {
            return  null;
        }
        return (IDiscountRechargeScript)is;
    }

    /**
     * 获取支持充值的脚本
     * @param scriptId
     * @return
     */
    public IRechargeReward recharge(int scriptId) {
        IScript is = Manager.scriptManager.GetScriptClass(scriptId);
        if (is == null) {
            log.error("该脚本ID类型不支持充值：" + scriptId);
            return null;
        }
        return (IRechargeReward) is;
    }

    /**
     * load all recharge data
     */
    public void load() {
        List<RechargeBean> list = dao.selectAll();
        if (list == null)
            return;

        for (int i = 0; i < list.size(); i++) {
            deal().addOrder(list.get(i));
        }
    }

    /**
     * 玩家上线发送充值数据
     *
     * @param player 玩家数据
     */
    public void playerOnLine(Player player) {
        Manager.rechargeManager.deal().onPlayerOnline(player);
    }

    /**
     * 添加充值
     * @param channel
     * @param recharge
     * @param data
     * @param src
     */
    public void AddRecharge(Channel channel, Recharge recharge, String data, Byte src) {
        addCommand(new PlayerRechargeHandler(channel, recharge, data, src));
    }

    @Override
    public void writeError(String message) {
        log.error("充值进程时失败错了：" + message);
    }

    @Override
    public void writeError(String message, Throwable t) {
        log.error("充值进程时失败错了：" + message, t);
    }
}
