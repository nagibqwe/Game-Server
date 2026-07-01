package com.game.boss.script;

import com.game.player.structs.Player;
import game.core.script.IScript;
import game.message.BossMessage;

public interface IBossScript extends IScript {

    void updateBossCount(Player player, int type, int num);

    void onResOpenDreamBoss(Player player, BossMessage.ResOpenDreamBoss messInfo);

    void sendReqOpenDreamBoss(Player player, int type);

    void sendReqSuitGemBossPanel(Player player, int type);

    void onResSuitGemBossPanel(Player player, BossMessage.ResSuitGemBossPanel messInfo);

}
