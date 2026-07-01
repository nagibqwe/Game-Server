package game.core.command;

import com.game.player.structs.Player;
import com.game.structs.SessionAttribute;
import com.game.team.structs.Team;
import com.game.team.manager.TeamManager;
import game.core.message.RMessage;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Auther: gouzhongliang
 * @Date: 2021/10/18 11:22
 */
public abstract class TeamHandler<T> extends PlayerHandler<T>{

    protected Logger log = LoggerFactory.getLogger(TeamHandler.class);

    @Override
    public void handler(RMessage mess, Player player, T data) {
        Team team = TeamManager.getInstance().getByPlayer(player.getId());
        if(team == null){
            handler(mess, player, team, (T)mess.getData());
        }else{
            log.error("玩家不在队伍中，player:{}", player.toString());
        }
    }

    public abstract void handler(RMessage mess, Player player, Team team, T data);
}
