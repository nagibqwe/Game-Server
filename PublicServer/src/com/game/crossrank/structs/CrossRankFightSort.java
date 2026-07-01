package com.game.crossrank.structs;

import com.game.db.bean.CrossRankBean;

import java.util.Comparator;

/**
 * Created by CXL on 2020/4/9.
 */
public class CrossRankFightSort implements Comparator<CrossRankBean> {

    @Override
    public int compare(CrossRankBean r1, CrossRankBean r2) {
        if (r1.getFightPower() > r2.getFightPower()) {
            return -1;
        } else if (r1.getFightPower() < r2.getFightPower()) {
            return 1;
        } else {
            return 0;
        }
    }
}
