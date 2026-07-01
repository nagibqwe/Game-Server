package com.game.guildbattle.structs;


import java.util.Comparator;
import java.util.Map;

/**
 * @Description
 * @auther lw
 * @create 2020-02-15 17:03
 */
public class GuildBattleRecordSort implements Comparator<Map.Entry<Long, GuildBattleMember>> {

    @Override
    public int compare(Map.Entry<Long, GuildBattleMember> o1, Map.Entry<Long, GuildBattleMember> o2) {
        if (o2.getValue().getRecord() - o1.getValue().getRecord() > 0) {
            return 1;
        }
        return -1;
    }

}
