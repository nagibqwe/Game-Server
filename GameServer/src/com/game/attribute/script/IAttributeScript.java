package com.game.attribute.script;

import com.game.attribute.BaseIntAttribute;
import com.game.attribute.BaseSystemIntAttribute;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerAttributeType;
import game.core.script.IScript;


public interface IAttributeScript extends IScript {

    /**
     *
     * @return
     */
     PlayerAttributeType getType();

    /**
     * 获取属性
     *
     * @param player
     * @return
     */
     BaseIntAttribute getPlayerAttribute(Player player, boolean sycRank);

    /**
     * 获取系统属性
     * @param player
     * @return
     */
    BaseSystemIntAttribute getPlayerSystemAttribute(Player player);

}
