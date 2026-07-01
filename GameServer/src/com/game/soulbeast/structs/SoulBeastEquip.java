package com.game.soulbeast.structs;

import com.game.backpack.structs.Item;
import com.game.player.structs.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zcd on 2018/5/7.
 */
public class SoulBeastEquip extends Item {

    //等级
    private int level;

    //当前经验
    private int curExp;

    /**
     * @param player
     * @param userNum  使用数量
     * @param actionId
     * @return
     */
    @Override
    public boolean use(Player player, int userNum, int index, long actionId) {
        return false;
    }

    @Override
    public boolean unuse(Player player, int unUseNum, long actionId) {
        return false;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getCurExp() {
        return curExp;
    }

    public void setCurExp(int curExp) {
        this.curExp = curExp;
    }


    @Override
    public String toString() {
        return "SoulBeastEquip{" +
                "id=" + id +
                ", level=" + level +
                ", curExp=" + curExp +
                '}';
    }

    @Override
    public void release() {

    }
}
