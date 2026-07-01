/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.backgrand.structs;

import java.util.Comparator;

/**
 * 排行数据排序
 *
 * @author soko <xuchangming@haowan123.com>
 */
public class RankListSort implements Comparator<RankData> {

    @Override
    public int compare(RankData r1, RankData r2) {
        if (r1.getRank() > r2.getRank()) {
            return 1;
        } else if (r1.getRank() < r2.getRank()) {
            return -1;
        } else {
            return 0;
        }
    }
}
