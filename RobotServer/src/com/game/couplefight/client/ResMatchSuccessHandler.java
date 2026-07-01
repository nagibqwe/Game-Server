package com.game.couplefight.client;

import com.game.couplefight.CoupleManager;
import com.game.player.structs.Player;
import com.game.team.structs.Team;
import com.game.team.manager.TeamManager;
import game.core.command.PlayerHandler;
import game.core.message.RMessage;
import game.message.CouplefightMessage;

/**
 * @Auther: gouzhongliang
 * @Date: 2021/10/16 15:56
 */
public class ResMatchSuccessHandler extends PlayerHandler<CouplefightMessage.ResMatchSuccess> {

    @Override
    public void handler(RMessage mess, Player player, CouplefightMessage.ResMatchSuccess data) {
        Team team = TeamManager.getInstance().getByPlayer(player.getId());
        CoupleManager.getInstance().matchSuccess(team);

        CouplefightMessage.ReqMatchConfirm.Builder req = CouplefightMessage.ReqMatchConfirm.newBuilder();
        req.setConfirm(true);
        player.sendMsg(CouplefightMessage.ReqMatchConfirm.MsgID.eMsgID_VALUE, req.build().toByteArray());
    }
}
