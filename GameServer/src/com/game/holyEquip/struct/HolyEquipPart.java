package com.game.holyEquip.struct;

/**
 * 圣装部位 by 542 on 2019/10/24.
 */
public class HolyEquipPart {


    public int part ;

    public int partLevel ;

    private HolyEquipItem equipItem;


    public void setEquipItem(HolyEquipItem holyEquipItem){this.equipItem = holyEquipItem;}


    public HolyEquipItem getEquipItem(){return equipItem;}

    public void setPart(int part){this.part = part;}

    public int getPart(){return part;}

    public void setPartLevel(int level){this.partLevel = level;}

    public int getPartLevel(){return  partLevel;}
}
