/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.copymap.sort;

import java.util.Comparator;
import java.util.Map.Entry;
import java.util.Objects;


public class ScoreSortLongLong implements Comparator<Entry<Long, Long>> {

    @Override
    public int compare(Entry<Long, Long> o1, Entry<Long, Long> o2) {
        if (o1.getValue() < o2.getValue()) {
            return 1;
        }

        if (Objects.equals(o1.getValue(), o2.getValue())) {
            return 0;
        }

        return -1;
    }

}
