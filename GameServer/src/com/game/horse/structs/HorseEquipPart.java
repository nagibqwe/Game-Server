package com.game.horse.structs;

import com.game.backpack.manager.BackpackManager;
import com.game.backpack.structs.Item;
import com.game.structs.GameObject;
import game.message.HorseMessage;

/**
 * 坐骑脉轮部位信息
 */
public class HorseEquipPart {
    /**
     * 当前装备
     */
    private Item item;
    //强化等级(跟着位置走)
    private int strengthLv;
    //附魂等级(跟着位置走)
    private int soulLv;
    //是否激活
    private boolean active;

    public HorseEquipPart(){}

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getStrengthLv() {
        return strengthLv;
    }

    public void setStrengthLv(int strengthLv) {
        this.strengthLv = strengthLv;
    }

    public int getSoulLv() {
        return soulLv;
    }

    public void setSoulLv(int soulLv) {
        this.soulLv = soulLv;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public HorseMessage.HorseEquipPart bytesWriteToPb(int cellId) {
        HorseMessage.HorseEquipPart.Builder pb = HorseMessage.HorseEquipPart.newBuilder();
        pb.setId(cellId);
        pb.setSoulLv(soulLv);
        pb.setStrengthLv(strengthLv);
        pb.setOpen(active);
        if(item != null){
            pb.setEquip(BackpackManager.getInstance().manager().buildItemInfo(item));
        }
        return pb.build();
    }

}
