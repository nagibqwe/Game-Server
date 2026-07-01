package com.game.couplefight.client;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.team.structs.Team;
import com.game.team.manager.TeamManager;
import game.core.command.PlayerHandler;
import game.core.message.RMessage;
import game.message.CouplefightMessage;

/**
 * @Auther: gouzhongliang
 * @Date: 2021/10/16 15:26
 */
public class ResMatchStartHandler extends PlayerHandler<CouplefightMessage.ResMatchStart> {
    @Override
    public void handler(RMessage mess, Player player, CouplefightMessage.ResMatchStart data) {
        if(data.getSuccess()){
            Team team = TeamManager.getInstance().getByPlayer(player.getId());
            Manager.coupleManager.addMatch(team);
        }else{
            log.info("进入匹配失败,因为{}", data.getReason());
        }
    }
}
