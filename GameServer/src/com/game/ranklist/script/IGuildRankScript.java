package com.game.ranklist.script;

import com.game.guild.structs.Guild;
import com.game.guild.structs.GuildMember;
import com.game.player.structs.Player;

import java.util.HashMap;
import java.util.List;

public interface IGuildRankScript extends IRankScript {
    /**
     * 获取公会排行列表
     */
    List<Guild> sortGuildRankOnly();

    /**
     * 公会排行榜
     */
    void sortGuildRank();

    /**
     * 福地特殊排序
     * @return
     */
    List<Guild> sortFuDiGuildRank();

    /**
     * gm查看公会的战斗力
     * @param player
     * @param guildName
     * @param isFuDi
     * @return
     */
    void gmFuDiPower(Player player, String guildName, boolean isFuDi);
}
