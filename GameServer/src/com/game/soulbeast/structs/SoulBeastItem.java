package com.game.soulbeast.structs;

import com.game.backpack.structs.Item;
import com.game.player.structs.Player;

/**
 * Created by zcd on 2018/5/14.
 */
public class SoulBeastItem extends Item{
    /**
     * @param player
     * @param userNum  使用数量
     * @param actionId
     * @return
     */
    @Override
    public boolean use(Player player, int userNum, int index, long actionId) {
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
