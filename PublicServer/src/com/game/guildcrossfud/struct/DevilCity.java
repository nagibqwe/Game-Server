package com.game.guildcrossfud.struct;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashMap;

/**
 * @Desc TODO  魔王缝隙
 * @Date 2021/4/29 16:32
 * @Auth ZUncle
 */
public class DevilCity {

    @JsonIgnore
    transient long devilRoomId;                                         //魔王缝隙ID
    @JsonIgnore
    transient HashMap<Integer, FudBoss> devilBoss = new HashMap<>();    //魔王缝隙boss数据

    public long getDevilRoomId() {
        return devilRoomId;
    }

    public void setDevilRoomId(long devilRoomId) {
        this.devilRoomId = devilRoomId;
    }

    public HashMap<Integer, FudBoss> getDevilBoss() {
        return devilBoss;
    }

    public void setDevilBoss(HashMap<Integer, FudBoss> devilBoss) {
        this.devilBoss = devilBoss;
    }
}
