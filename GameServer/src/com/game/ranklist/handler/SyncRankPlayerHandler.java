package com.game.ranklist.handler;

import com.game.db.bean.RankPlayer;
import com.game.manager.Manager;
import com.game.server.GameServer;
import game.core.command.ICommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 用于同步排行榜的数据
 */
public class SyncRankPlayerHandler implements ICommand {

    private static final Logger log = LogManager.getLogger(SyncRankPlayerHandler.class);

    private final RankPlayer rankplayer;

    //rank类型，0表示所有类型
    private int rankType;

    public SyncRankPlayerHandler(RankPlayer rankplayer, int rankType) {
        this.rankplayer = rankplayer;
        this.rankType = rankType;
    }

    @Override
    public void action() {
        try {
            if (rankplayer == null) {
                return;
            }
            if (GameServer.getInstance().IsFightServer()) {
                return;
            }
            Manager.rankListManager.deal().OnSyncRankPlayer(rankplayer, rankType);
        } catch (Exception ex) {
            log.error(ex, ex);
        }
    }

}
