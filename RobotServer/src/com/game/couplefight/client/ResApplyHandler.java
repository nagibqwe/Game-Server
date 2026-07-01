package com.game.couplefight.client;

import com.game.couplefight.CoupleManager;
import com.game.player.structs.Player;
import com.game.structs.SessionAttribute;
import com.game.team.structs.Team;
import com.game.team.manager.TeamManager;
import game.core.command.Handler;
import game.core.message.RMessage;
import game.message.CouplefightMessage;
import org.apache.mina.core.session.IoSession;

/**
 * 报名成功返回
 * @Auther: gouzhongliang
 * @Date: 2021/8/10 17:25
 */
public class ResApplyHandler extends Handler {
    @Override
    public void action(RMessage mess) {
        CouplefightMessage.ResApply res = (CouplefightMessage.ResApply)mess.getData();
        IoSession iosession = mess.getSession();
        Player player = (Player) iosession.getAttribute(SessionAttribute.PLAYER.getValue());
        Team team = TeamManager.getInstance().getByPlayer(player.getId());
        if(res.getSuccess() == 0 && team != null){
            CoupleManager.getInstance().addTeam(team);
        }
    }
}
