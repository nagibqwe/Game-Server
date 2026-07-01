package com.game.devilseries.structs;

import com.game.backpack.manager.BackpackManager;
import com.game.backpack.structs.Equip;
import com.game.backpack.structs.Item;
import game.message.DevilSeriesMessage;

public class DevilEquipPart {
    /**
     * 部位id
     */
    private int id;
    /**
     * 当前装备
     */
    private Equip equip;

    public Equip getEquip() {
        return equip;
    }

    public void setEquip(Equip equip) {
        this.equip = equip;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public DevilSeriesMessage.DevilEquipPart.Builder toProto() {
        DevilSeriesMessage.DevilEquipPart.Builder partProto = DevilSeriesMessage.DevilEquipPart.newBuilder();
        partProto.setId(id);
        if(equip != null){
            partProto.setEquip(BackpackManager.getInstance().manager().buildItemInfo(equip));
        }
        return partProto;
    }
}
