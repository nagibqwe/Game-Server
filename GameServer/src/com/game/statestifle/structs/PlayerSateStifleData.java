package com.game.statestifle.structs;

import com.game.nature.structs.Nature;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 瞿冰冰
 * 2019/9/4
 */
public class PlayerSateStifleData {

    private int level = 1;              //等级

    private int star = 0;               //星数

    private Nature nature = new Nature();

    private Map<Integer, SoulSpiritInfo> spiritMap = new HashMap<>();       //器灵列表

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public Nature getNature() {
        return nature;
    }

    public void setNature(Nature nature) {
        this.nature = nature;
    }

    public Map<Integer, SoulSpiritInfo> getSpiritMap() {
        return spiritMap;
    }

    public void setSpiritMap(Map<Integer, SoulSpiritInfo> spiritMap) {
        this.spiritMap = spiritMap;
    }

    @Override
    public String toString() {
        return "PlayerSateStifleData{" +
                "level=" + level +
                ", star=" + star +
                '}';
    }
}
