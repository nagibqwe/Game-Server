package com.game.auction.manager;

import com.game.auction.command.AuctionCommand;
import com.game.auction.command.AuctionFastPurCommand;
import com.game.auction.command.AuctionOutCommand;
import com.game.auction.command.AuctionPurCommand;
import com.game.auction.scripts.IAuctionScript;
import com.game.auction.structs.AuctionInfo;
import com.game.auction.timer.AuctionTimer;
import com.game.backpack.structs.Item;
import com.game.db.bean.AuctionBean;
import com.game.db.dao.AuctionDao;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.server.GameServer;
import game.core.script.IScript;
import game.core.util.JsonUtils;
import game.message.EquipMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description
 * @auther lw
 * @create 2019-10-08 11:39
 */
public class AuctionManager {

    private static final Logger logger = LogManager.getLogger(AuctionManager.class);

    private ConcurrentHashMap<Long, AuctionInfo> auctions = new ConcurrentHashMap<>();

    private final AuctionDao auctionDao = new AuctionDao();

    public ConcurrentHashMap<Long, AuctionInfo> getAuctions() {
        return auctions;
    }

    public void setAuctions(ConcurrentHashMap<Long, AuctionInfo> auctions) {
        this.auctions = auctions;
    }

    public void load() {
        List<AuctionBean> list = auctionDao.selectAll();
        for (AuctionBean bean : list) {
            AuctionInfo auctionInfo = new AuctionInfo();
            auctionInfo.setItem(JsonUtils.parseObject(bean.getAuctionItem(), Item.class));
            auctionInfo.setId(bean.getAuctionId());
            auctionInfo.setOwnId(bean.getAuctionOwnId());
            auctionInfo.setPrice(bean.getAuctionPrice());
            auctionInfo.setRoleId(bean.getAuctionRoleId());
            auctionInfo.setTime(bean.getAuctionTime());
            auctionInfo.setGuildId(bean.getAuctionGuild());
            auctionInfo.setPassword(bean.getPassword());
            auctions.put(auctionInfo.getId(), auctionInfo);
        }
        logger.info("加载竞拍数据完成,总共条数:" + auctions.size());
        Calendar calendar = Calendar.getInstance();
        int second = calendar.get(Calendar.SECOND);
        int millis = calendar.get(Calendar.MILLISECOND);
        long delay = 60 * 1000 - second * 1000 - millis;
        GameServer.getInstance().getAssistThread().addTimerEvent(new AuctionTimer(delay));
    }

    public IAuctionScript manager() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.AuctionBaseScript);
        if (is instanceof IAuctionScript) {
            return (IAuctionScript) is;
        } else {
            logger.error("IAuctionScript接口脚本错误");
            return null;
        }
    }

    public void auctionOut(Player player, long auctionId) {
        GameServer.getInstance().getAssistThread().addCommand(new AuctionOutCommand(player, auctionId));
    }

    public void auctionPur(Player player, long auctionId, String password) {
        GameServer.getInstance().getAssistThread().addCommand(new AuctionPurCommand(player, auctionId, password));
    }

    public void auctionFastPur(Player player, EquipMessage.ReqEquipSyn mess) {
        GameServer.getInstance().getAssistThread().addCommand(new AuctionFastPurCommand(player, mess));
    }

    public void auction(Player player, long auctionId, int price) {
        GameServer.getInstance().getAssistThread().addCommand(new AuctionCommand(player, auctionId, price));
    }

    private enum Singleton {
        INSTANCE;
        AuctionManager manager;

        Singleton() {
            manager = new AuctionManager();
        }

        AuctionManager getProcessor() {
            return manager;
        }
    }

    public static AuctionManager getInstance() {
        return AuctionManager.Singleton.INSTANCE.getProcessor();
    }


}
