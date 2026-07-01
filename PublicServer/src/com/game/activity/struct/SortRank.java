/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.activity.struct;

import com.game.db.bean.ActivityRankBean;
import java.util.Comparator;

/**
 *  排行数据
 * @author zhaibiao
 */
public class SortRank implements Comparator<ActivityRankBean>{

    @Override
    public int compare(ActivityRankBean o1, ActivityRankBean o2) {
        if(o1.getRankDate()>o2.getRankDate()){
            return -1;
        }else if(o1.getRankDate()<o2.getRankDate()){
            return 1;
        }else{
            return 0;
        }
    }
    
}
