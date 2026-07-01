package com.game.universe.struts;

import java.util.List;

public class GuildBattleInfo {
    private long masterId;
    private List<Long> secMasterId;

    public long getMasterId() {
        return masterId;
    }

    public void setMasterId(long masterId) {
        this.masterId = masterId;
    }

    public List<Long> getSecMasterId() {
        return secMasterId;
    }

    public void setSecMasterId(List<Long> secMasterId) {
        this.secMasterId = secMasterId;
    }
}
