package com.game.backpack.sort;

import com.game.backpack.structs.Item;

import java.util.Comparator;

/**
 * Created by 瞿冰冰
 * 2019/8/12
 */
public class ItemSort implements Comparator<Item> {

    @Override
    public int compare(Item o2, Item o1) {
        int modleid = o2.getItemModelId() - o1.getItemModelId();
        if (modleid > 0) {
            return -1;
        }else if(modleid < 0){
            return 3;
        }
        int num = o2.getNum() - o1.getNum();
        if (num > 0) {
            return -2;
        }else if(num < 0){
            return  2;
        }
        int grid = o2.getGridId() - o1.getGridId();
        if (grid > 0) {
            return -3;
        }else if(grid < 0){
            return 1;
        }
        return 0;
    }
}