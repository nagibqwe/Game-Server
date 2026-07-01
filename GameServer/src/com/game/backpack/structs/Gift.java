package com.game.backpack.structs;

import com.data.ItemChangeReason;
import com.game.manager.Manager;
import com.game.player.structs.Player;

/**
 * @author Administrator
 */
public class Gift extends Item {

    @Override
    public boolean use(Player player, int useNum, int index, long actionId) {
        if (Manager.backpackManager.manager().useGift(player, useNum, index, actionId, this)) {
            Manager.backpackManager.manager().onRemoveItem(player, this, useNum, ItemChangeReason.OwnUseDec, actionId);
            return true;
        }
        return false;
    }

    @Override
    public boolean unuse(Player player, int unUseNum, long actionId) {
        return false;
    }

    @Override
    public void release() {

    }
}
