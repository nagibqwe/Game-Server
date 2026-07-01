package com.game.player.script;

import com.game.player.structs.Player;
import game.core.script.IScript;

/**
 * 实名认证
 */
public interface ICertifyScript extends IScript {

    void playerOnline(Player player);

    void onReqNoticeCertifySuccess(Player player);
}
