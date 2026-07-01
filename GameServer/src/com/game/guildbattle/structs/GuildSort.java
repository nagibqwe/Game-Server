package com.game.guildbattle.structs;

import com.game.guild.structs.Guild;

import java.util.Comparator;

/**
 * @Description
 * @auther lw
 * @create 2020-02-11 17:03
 */
public class GuildSort implements Comparator<Guild> {

    @Override
    public int compare(Guild o1, Guild o2) {
        if (o2.gainGuildPower() - o1.gainGuildPower() > 0) {
            return 1;
        }
        return -1;
    }

}
