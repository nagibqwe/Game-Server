package com.game.register.script;

import com.game.player.structs.Player;
import game.core.script.IScript;
import game.message.RegisterMessage;

import java.util.List;

public interface IRegisterScript extends IScript {

    /**
     * 请求登录游戏
     */
    void loginGame(Player player);

    /**
     * 登录验证成功
     * @param player
     * @param infoList
     */
    void loginGameSuccess(Player player, List<RegisterMessage.RoleBaseInfo> infoList);

    /**
     * 进入游戏,客户端准备完毕，请求加载完成
     */
    void reqLoadFinish(Player player);

    /**
     * 所有机器人退出游戏
     */
    void allQuitGame(long except);

    void quitGame(Player player, boolean isFromServer);

    void quitGame(long userId);
}
