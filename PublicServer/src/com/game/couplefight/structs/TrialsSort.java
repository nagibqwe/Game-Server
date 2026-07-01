package com.game.couplefight.structs;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 海选排名排序
 * @Auther: gouzhongliang
 * @Date: 2021/7/16 10:19
 */
public class TrialsSort implements Comparator<RankInfo> {

    public static TrialsSort instance = new TrialsSort();

    @Override
    public int compare(RankInfo t1, RankInfo t2) {
        int s1 = t1.getScore();
        int s2 = t2.getScore();

        if(s1 > s2){
            return -1;
        }else if(s1 < s2){
            return 1;
        }
        return 0;
    }

}
