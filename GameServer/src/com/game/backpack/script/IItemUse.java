/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.backpack.script;

import com.game.backpack.structs.Item;
import com.game.player.structs.Player;

/**
 * 脚本物品使用接口
 *
 * @author soko <xuchangming@haowan123.com>
 */
public interface IItemUse {

    boolean useItem(Player player, Item aThis, int useNum, long actionId, boolean otherOpt);

    boolean unUseItem(Player player, Item aThis, int useNum, long actionId);

}
