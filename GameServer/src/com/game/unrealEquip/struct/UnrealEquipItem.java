package com.game.unrealEquip.struct;

import com.game.backpack.structs.Item;
import com.game.player.structs.Player;

public class UnrealEquipItem  extends Item {

    //品质
    private int color;
    //部位
    private int part;
    //阶
    private int grade;
    //职业
    private int gender;
    //套装ID
    private int suit;

    /**
     * 圣装专用（其他类型的装备不能使用）
     1-2：对应圣装第一套的套装
     3-4：对应圣装第二套的套装
     5-6：对应圣装第三套的套装
     （如需要扩展，需要程序代码支持）
     * @return
     */
    private int holySuitType;

    public boolean use(Player player, int userNum, int index, long actionId) {
        return false;
    }

    @Override
    public boolean unuse(Player player, int unUseNum, long actionId) {
        return false;
    }



    public void setColor(int color){this.color = color;}

    public int getColor(){return color;}

    public void setPart(int part){this.part = part;}

    public int getPart(){return part;}

    public void setGrade(int grade){this.grade = grade;}

    public int getGrade(){return grade;}

    public void setGender(int gender){this.gender = gender;}

    public int getGender(){return gender;}

    public void setSuit(int suit){this.suit = suit;}

    public int getSuit(){return suit;}

    public int getHolySuitType() {
        return holySuitType;
    }

    public void setHolySuitType(int holySuitType) {
        this.holySuitType = holySuitType;
    }

    @Override
    public void release() {

    }

}
