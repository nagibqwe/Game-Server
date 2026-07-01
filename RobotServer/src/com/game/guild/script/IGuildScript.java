package com.game.guild.script;

import com.game.player.structs.Player;
import game.core.script.IScript;
import game.message.GuildMessage;

public interface IGuildScript extends IScript {

    void onResGuildInfo(Player player, GuildMessage.ResGuildInfo messInfo);

    void sendReqGuildInfo(Player player);
}
