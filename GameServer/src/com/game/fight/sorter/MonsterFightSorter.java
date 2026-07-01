package com.game.fight.sorter;

import com.game.structs.Fighter;
import com.game.utils.Utils;

/**
 * Created by huhu on 2017/8/24.
 */
public class MonsterFightSorter implements java.util.Comparator<Fighter> {

    public Fighter op;

    @Override
    public int compare(Fighter o1, Fighter o2) {
        float d1 = Utils.getDistance(o1, op);
        float d2 = Utils.getDistance(o2, op);
        return d1 < d2 ? -1 : (d1 > d2 ? 1 : 0);
    }
}
