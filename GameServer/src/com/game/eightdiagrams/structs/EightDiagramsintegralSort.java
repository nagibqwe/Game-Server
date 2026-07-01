package com.game.eightdiagrams.structs;

import com.game.db.bean.EightIntegralRankBean;

import java.util.Comparator;

/**
 * Created by 542 on 2019/9/28.
 */
public class EightDiagramsintegralSort implements Comparator<EightIntegralRankBean> {

    public int compare(EightIntegralRankBean r1, EightIntegralRankBean r2) {
        if (r1.getAllIntegral() < r2.getAllIntegral()) {
            return 1;
        } else if (r1.getAllIntegral() > r2.getAllIntegral()) {
            return -1;
        } else {
            return 0;
        }
    }
}
