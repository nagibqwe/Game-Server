package com.game.couplefight.client;

import com.game.player.structs.Player;
import com.game.team.structs.Team;
import game.core.command.TeamHandler;
import game.core.message.RMessage;
import game.message.CouplefightMessage;

/**
 * @Auther: gouzhongliang
 * @Date: 2021/10/18 9:31
 */
public class ResFightResultHandler extends TeamHandler<CouplefightMessage.ResFightResult> {

    @Override
    public void handler(RMessage mess, Player player, Team team, CouplefightMessage.ResFightResult data) {
        log.info("战斗结算 team:{}, type:{}, win:{}, score:{}",team, data.getType(), data.getWin(), data.getScore());
    }
}
