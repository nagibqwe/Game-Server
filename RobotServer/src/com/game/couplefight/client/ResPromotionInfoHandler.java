package com.game.couplefight.client;

import com.game.couplefight.CoupleManager;
import com.game.player.structs.Player;
import com.game.team.structs.Team;
import game.core.command.TeamHandler;
import game.core.message.RMessage;
import game.message.CouplefightMessage;

/**
 * @Auther: gouzhongliang
 * @Date: 2021/10/18 9:38
 */
public class ResPromotionInfoHandler extends TeamHandler<CouplefightMessage.ResPromotionInfo> {

    @Override
    public void handler(RMessage mess, Player player, Team team, CouplefightMessage.ResPromotionInfo data) {
        int type = data.getType();
        if(type == 2){
            CoupleManager.getInstance().addGroupTeam(team);
        }else if(type == 3){
            CoupleManager.getInstance().addChampionDiTeam(team);
        }else if(type == 4){
            CoupleManager.getInstance().addChampionTianTeam(team);
        }
    }
}
