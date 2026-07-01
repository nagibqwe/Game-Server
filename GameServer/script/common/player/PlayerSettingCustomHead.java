package common.player;

import com.game.manager.Manager;
import com.game.map.structs.MapUtils;
import com.game.player.script.ImPlayerScript;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerWorldInfo;
import com.game.player.structs.SavePlayerLevel;
import com.game.script.structs.ScriptEnum;
import com.game.utils.MessageUtils;
import game.core.script.IScript;
import game.message.PlayerMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PlayerSettingCustomHead  extends ImPlayerScript implements IScript {

    private static final Logger log = LogManager.getLogger(ChangeRoleName.class);

    @Override
    public int getId() {
        return ScriptEnum.PlayerCustomHeadScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    //
    @Override
    public void playerSettingCustomHead(Player player,String customHeadPath,boolean useCustomHead) {
        player.setCustomHeadPath(customHeadPath);
        player.setUseCustomHead(useCustomHead);
        Manager.playerManager.savePlayer(player, SavePlayerLevel.RightNow); //新角色名需同步到是世界服的，要先addRoleName，再savePlayer


        //通知客户端改名成功
        PlayerMessage.ResPlayerSettingCustomHeadResult.Builder resMsg = PlayerMessage.ResPlayerSettingCustomHeadResult.newBuilder();
        resMsg.setIsSuccess(true);
        resMsg.setHead(MapUtils.getHead(player));
        resMsg.setRoleId(player.getId());

        PlayerWorldInfo pwi = Manager.playerManager.getPlayerWorldInfo(player.getId());
        if(pwi != null){
            //TODO 同步社交服务器
            PlayerMessage.G2SSynPlayerSocialInfo.Builder mPlayer = PlayerMessage.G2SSynPlayerSocialInfo.newBuilder();
            mPlayer.setGlobalPlayerWorldInfo(pwi.toGlobalPlayerWorldInfo());
            mPlayer.setType(2);
            MessageUtils.send_to_social(PlayerMessage.G2SSynPlayerSocialInfo.MsgID.eMsgID_VALUE, mPlayer.build().toByteArray());
        }




        //广播给周围玩家
        MessageUtils.send_to_roundPlayer(player, PlayerMessage.ResPlayerSettingCustomHeadResult.MsgID.eMsgID_VALUE, resMsg.build().toByteArray());

        if (player.getTeamId() > 0) {
            Manager.teamManager.deal().updateTeamInfoToLeader(player.getTeamId());
        }
    }
}
