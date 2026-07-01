package com.game.title.script;

import com.game.player.structs.Player;
import game.core.script.IScript;

public interface ITitleScript extends IScript {

    /**
     * 玩家上线，发送称号数据
     */
    void sendTitleInfo(Player player);

    /**
     * 称号过期检测
     */
    void titleTimeOutCheck(Player player);

    /**
     * 使用称号道具
     * 效果：激活称号 或 延长称号限时时间
     */
    void useTitleItem(Player player, int itemId, int num, int reason);

    /**
     * 卸载称号
     * @param player
     * @param id
     */
    void uninstallTitle(Player player, int id);

    /**
     * 制裁某种类型的称号
     * @param player
     * @param type
     */
    void uninstallTitleByType(Player player, int type);

    /**
     * 请求激活称号
     * @param player
     * @param id
     * @param cost  是否消耗道具
     */
    void onReqActiveTitle(Player player, int id, boolean cost);

    /**
     * 请求穿戴称号
     */
    void onReqWearTitle(Player player, int id);

    /**
     * 请求卸下称号
     */
    void onReqDownTitle(Player player, int id);
}
