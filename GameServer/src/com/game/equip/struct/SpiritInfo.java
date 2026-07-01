package com.game.equip.struct;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

import static com.game.equip.struct.EquipDefine.EquipPart_Num;

public class SpiritInfo {

    private int spiritId;

    private List<Integer> equipList = new ArrayList<>(EquipPart_Num);

    private boolean isActive;

    @JsonIgnore
    transient boolean activeExt;    //特殊战力激活

    public SpiritInfo() {}

    public SpiritInfo(int spiritId) {
        this.spiritId = spiritId;
        for (int i = 0; i < EquipPart_Num; i++) {
            equipList.add(0);
        }
    }

    public int getSpiritId() {
        return spiritId;
    }

    public void setSpiritId(int spiritId) {
        this.spiritId = spiritId;
    }

    public List<Integer> getEquipList() {
        return equipList;
    }

    public void setEquipList(List<Integer> equipList) {
        this.equipList = equipList;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isActiveExt() {
        return activeExt;
    }

    public void setActiveExt(boolean activeExt) {
        this.activeExt = activeExt;
    }
}
