/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.friend.manager;

import com.game.friend.struct.Friend;
import com.game.friend.struct.RelationInfo;

import java.util.Comparator;

/**
 * 好友排序比较器
 * 在线，亲密度高，等级高的玩家优先显示
 */
public class RelationInfoSort implements Comparator<RelationInfo> {

    /**
     * 降序排列
     * @param o1
     * @param o2
     * @return
     */
    @Override
    public int compare(RelationInfo o1, RelationInfo o2) {

        //是否都在线或离线
        if (o1.getInfo().isOnLine() != o2.getInfo().isOnLine()) {
            if (o1.getInfo().isOnLine()) {
                return -1;
            }
            return 1;
        }

        //好友需要比较亲密度
        if (o1 instanceof Friend && o2 instanceof Friend) {
            Friend f1 = (Friend) o1;
            Friend f2 = (Friend) o2;
            //是否亲密度相同
            if (f1.getIntimacy() != f2.getIntimacy()) {
                return f2.getIntimacy() - f1.getIntimacy();
            }
        }


        //最后比较等级
        return o2.getInfo().getLevel() - o1.getInfo().getLevel();
    }

}
