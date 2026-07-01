package com.game.ninedaysfocused.structs;

import java.util.Comparator;

/**
 * Created by 542 on 2019/7/24.
 */
public class NineDaysSort  implements Comparator<NineDaysCrossPlayer> {

    @Override
    public int compare(NineDaysCrossPlayer o1, NineDaysCrossPlayer o2) {
        if (o1.getFightPoint() > o2.getFightPoint()) {
            return 1;
        }
        return -1;
    }
}
