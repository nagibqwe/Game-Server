package com.game.setting.script;

import com.game.player.structs.Player;
import com.game.setting.struct.FeedBackInfo;
import game.core.script.IScript;
import game.message.SettingMessage;

public interface ISettingScript extends IScript {

    void playerOnline(Player player);

    void onReqSendSetting(Player player, SettingMessage.ReqSendSetting mess);

    void onReqCommitFeedback(Player player, SettingMessage.ReqCommitFeedback mess);

    void sendGMFeedback(long playerId, FeedBackInfo info);

    /**
     * 更改服务器名字
     * @param player
     * @param messInfo
     */
    void changeServerName(Player player, SettingMessage.ReqChangeServerName messInfo);
}
