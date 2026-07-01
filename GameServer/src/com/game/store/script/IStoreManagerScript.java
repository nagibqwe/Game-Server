package com.game.store.script;

import com.game.backpack.structs.Item;
import com.game.player.structs.Player;

/**
 *
 * @author Administrator
 */
public interface IStoreManagerScript {

    //整理仓库
    void storeClearUp(Player player, boolean isgm);

    //仓库移动
    void storeMoveItem(Player player, Item item, int toCellId);

    int getStoreFirstEmptGridId(Player player);
}
