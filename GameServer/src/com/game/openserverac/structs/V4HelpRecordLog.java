package com.game.openserverac.structs;

public class V4HelpRecordLog {

    public long getHelpPlayerId() {
        return helpPlayerId;
    }

    public void setHelpPlayerId(long helpPlayerId) {
        this.helpPlayerId = helpPlayerId;
    }

    public long getBeHelpPlayerId() {
        return beHelpPlayerId;
    }

    public void setBeHelpPlayerId(long beHelpPlayerId) {
        this.beHelpPlayerId = beHelpPlayerId;
    }

    private long helpPlayerId;
    private  long beHelpPlayerId;
}
