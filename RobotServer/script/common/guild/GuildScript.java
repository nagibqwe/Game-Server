package common.guild;

import com.game.guild.script.IGuildScript;
import com.game.player.structs.Player;
import com.game.script.ScriptEnum;
import game.message.GuildMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GuildScript implements IGuildScript {

    private static final Logger log = LogManager.getLogger(GuildScript.class);

    @Override
    public int getId() {
        return ScriptEnum.GuildBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void onResGuildInfo(Player player, GuildMessage.ResGuildInfo messInfo) {

    }

    @Override
    public void sendReqGuildInfo(Player player) {

    }
}
