package com.game.guildbattle.structs;


import java.util.Comparator;


/**
 * @Description
 * @auther lw
 * @create 2020-03-03 11:03
 */
public class GuildBattleResSort implements Comparator<GuildBattleData> {

    @Override
    public int compare(GuildBattleData o1, GuildBattleData o2) {
        if (o2.getMemberList().values().stream().map(GuildBattleMember::getRecord).reduce(0, Integer::sum) -
                o1.getMemberList().values().stream().map(GuildBattleMember::getRecord).reduce(0, Integer::sum) > 0) {
            return 1;
        }
        return -1;
    }

}
