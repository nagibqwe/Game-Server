package game.core.command;

import com.game.player.structs.Player;
import com.game.structs.SessionAttribute;
import game.core.message.RMessage;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Auther: gouzhongliang
 * @Date: 2021/10/16 11:22
 */
public abstract class PlayerHandler<T> extends BaseHandler{

    @Override
    public void handler(RMessage mess) {
        IoSession iosession = mess.getSession();
        Player player = (Player) iosession.getAttribute(SessionAttribute.PLAYER.getValue());
        if(player != null){
            handler(mess, player, (T)mess.getData());
        }else{
            log.error("玩家不存在");
        }
    }

    public abstract void handler(RMessage mess, Player player, T data);
}
