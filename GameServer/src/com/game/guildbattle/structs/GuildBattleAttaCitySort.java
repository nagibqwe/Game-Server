package com.game.guildbattle.structs;


import java.util.Comparator;
import java.util.Map;

/**
 * @Description
 * @auther lw
 * @create 2020-02-15 17:03
 */
public class GuildBattleAttaCitySort implements Comparator<Map.Entry<Long, GuildBattleMember>> {

    @Override
    public int compare(Map.Entry<Long, GuildBattleMember> o1, Map.Entry<Long, GuildBattleMember> o2) {
        if (o2.getValue().getDestroyNum() + o2.getValue().getRepairNum() - (o1.getValue().getDestroyNum() + o1.getValue().getRepairNum()) > 0) {
            return 1;
        }
        return -1;
    }

}
