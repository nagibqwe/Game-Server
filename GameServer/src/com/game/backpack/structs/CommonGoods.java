package com.game.backpack.structs;

import com.data.CfgManager;
import com.data.bean.Cfg_Item_Bean;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.data.ItemChangeReason;

/**
 * 普通物品也支持类型特效处理
 *
 * @author Administrator
 */
public class CommonGoods extends Item {
    @Override
    public boolean use(Player player, int useNum, int index, long actionId) {

        if (!Manager.backpackManager.manager().canUse(player, this, useNum)) {
            return false;
        }

        if (Manager.backpackManager.manager().onRemoveItem(player, this, useNum, ItemChangeReason.OwnUseDec, actionId)) {
            Manager.backpackManager.manager().doEffects(player, this, useNum, actionId);
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
