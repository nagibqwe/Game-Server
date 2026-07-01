package com.game.shihai.script;

import com.game.player.structs.Player;
import game.message.ShiHaiMessage;

public interface IShiHaiHandler {
    /**
     * 获取识海数据
     */
    void shiHaiData(Player player, ShiHaiMessage.ReqShiHaiData messInfo);

    /**
     * 提升识海
     */
    void upLevel(Player player, ShiHaiMessage.ReqUpLevel messInfo);
}
