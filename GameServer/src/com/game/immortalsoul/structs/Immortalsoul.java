package com.game.immortalsoul.structs;

import com.game.backpack.structs.Item;
import com.game.player.structs.Player;

/**
 * Created by 542 on 2019/7/5.
 */
public class Immortalsoul extends Item {

    private  long uid;
    private int itemID;
    private int level;
    private int exp;
    private int cacheExp;
    private int location;
    private int quality;
    //互斥类型
    private int type;


    public boolean use(Player player, int userNum, int index, long actionId) {
        return false;
    }

    @Override
    public boolean unuse(Player player, int unUseNum, long actionId) {
        return false;
    }

    public  long getUid(){return  uid;}

    public  void  setUid(long Uid){this.uid = Uid;}

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public int getLevel() {
        return level;
    }
    public void setLevel(int level) {
        this.level = level;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getCacheExp() {
        return cacheExp;
    }

    public void setCacheExp(int cacheExp) {
        this.cacheExp = cacheExp;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public int getQuality() {
        return quality;
    }
    public void setQuality(int quality) {
        this.quality = quality;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public void release() {

    }
}
