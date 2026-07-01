package common.item;

import com.game.backpack.script.IItemUse;
import com.game.backpack.structs.Item;
import com.game.chat.structs.Notify;
import com.game.manager.Manager;
import  com.data.MessageString;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.data.ItemChangeReason;
import com.game.utils.MessageUtils;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 万寿修为丹
 *
 * @author admin
 */
public class WanShouXiuWeiDan implements IScript, IItemUse {

    private static final Logger log = LogManager.getLogger(WanShouXiuWeiDan.class);

    @Override
    public int getId() {
        return ScriptEnum.WanShouXiuWeiDanBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public boolean useItem(Player player, Item item, int num, long actionId, boolean otherOpt) {

        if (player.getLevel() >= 250) {//5转以下才能使用九零 一起玩 www.901 75.com
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.PlayerLvOverUseLv, 5 + "");
            return false;
        }

        if (!Manager.backpackManager.manager().canUse(player, item, num)) {
            return false;
        }

        if (Manager.backpackManager.manager().onRemoveItem(player, item, num, ItemChangeReason.OwnUseDec, actionId)) {
            Manager.backpackManager.manager().doEffects(player, item, num, actionId);
        }

        return true;
    }

    @Override
    public boolean unUseItem(Player player, Item aThis, int useNum, long actionId) {
        return false;
    }

}
