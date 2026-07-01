package com.game.team.client;

import com.game.player.manager.PlayerManager;
import com.game.player.structs.Player;
import game.core.command.PlayerHandler;
import game.core.message.RMessage;
import game.message.TeamMessage;

/**
 * @Auther: gouzhongliang
 * @Date: 2021/10/19 9:24
 */
public class ResWaitListHandler extends PlayerHandler<TeamMessage.ResWaitList> {

    @Override
    public void handler(RMessage mess, Player player, TeamMessage.ResWaitList data) {
        for(TeamMessage.TeamInfo t : data.getTeamsList()){
            if(t.getMembersCount() < 2){
                TeamMessage.TeamMember member = t.getMembers(0);
                int career = 0;
                if(member.getCareer() == 0){
                    career = 1;
                }
                Player p = PlayerManager.getInstance().deal().getPlayerNoTeam(career);
                if(p != null){
                    p.joinTeam(t.getTeamId());
                }
                break;
            }
        }
    }
}
