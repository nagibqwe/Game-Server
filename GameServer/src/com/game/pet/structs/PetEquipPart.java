package com.game.pet.structs;

import com.game.backpack.manager.BackpackManager;
import com.game.backpack.structs.Item;
import game.message.PetMessage;

/**宠物装备的位置*/
public class PetEquipPart {

    //该位置装备的宠物装备
    private Item petEquip;

    //强化等级(跟着位置走)
    private int strengthLv;

    //附魂等级(跟着位置走)
    private int soulLv;

    //是否激活
    private boolean active;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Item getPetEquip() {
        return petEquip;
    }

    public void setPetEquip(Item petEquip) {
        this.petEquip = petEquip;
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

    public PetMessage.PetAssistantCellInfo bytesWriteToPb(int cellId) {
        PetMessage.PetAssistantCellInfo.Builder pb = PetMessage.PetAssistantCellInfo.newBuilder();
        pb.setCellId(cellId);
        pb.setSoulLv(soulLv);
        pb.setStrengthLv(strengthLv);
        pb.setOpen(active);
        if (petEquip != null) {
            pb.setEquip(BackpackManager.getInstance().manager().buildItemInfo(petEquip));
        }
        return pb.build();
    }
}
