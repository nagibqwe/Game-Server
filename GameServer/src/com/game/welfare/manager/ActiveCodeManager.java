package com.game.welfare.manager;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.welfare.script.IExchangeGiftScript;
import com.game.welfare.script.IWelfareScript;
import game.message.WelfareMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;

public class ActiveCodeManager {
    private static final Logger log = LogManager.getLogger(ActiveCodeManager.class);
    // 激活码使用CD
    private final int USE_CD = 3000; // 5s
    private final transient ConcurrentHashMap<Long, Long> useCD = new ConcurrentHashMap<>();

    private enum Singleton {
        INSTANCE;
        ActiveCodeManager manager;

        Singleton() {
            this.manager = new ActiveCodeManager();
        }

        ActiveCodeManager getProcessor() {
            return manager;
        }
    }

    public static ActiveCodeManager getInstance() {
        return ActiveCodeManager.Singleton.INSTANCE.getProcessor();
    }

    private ActiveCodeManager() { }

    /**
     * 使用激活码
     *
     * @param player
     * @param activeCode
     */
    public void useActiveCode(Player player, String activeCode) {
        if (player == null)
            return;

        if (isCD(player.getId()))
            return;

        IWelfareScript script = Manager.welfareManager.getScript(WelfareMessage.WelfareType.ExchangeGift);
        if (script == null) {
            log.error("使用激活码出错了！没有找到脚本实例");
            return;
        }

        ((IExchangeGiftScript) script).onReqExchangeGift(player, activeCode);
    }

    private boolean isCD(long id) {
        long time = useCD.getOrDefault(id,0L);
        long now = System.currentTimeMillis();

        if (now - time < USE_CD)
            return true;

        useCD.put(id, now);
        return false;
    }
}
