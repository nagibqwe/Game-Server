package com.game.worldanswer.structs;

import java.util.Comparator;

/**
 * Created by clc on 2019/7/15.
 */
public class AnswerRank implements Comparator<Answer> {
    @Override
    public int compare(Answer r1, Answer r2) {
        if (r1.getIntegral() > r2.getIntegral()) {
            return 1;
        } else if (r1.getIntegral() < r2.getIntegral()) {
            return -1;
        } else {
            return 0;
        }
    }
}
