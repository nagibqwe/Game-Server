package com.game.map.structs;

public class MapPeople extends BaseNpc {

    @Override
    protected void onForceStopMove() {

    }

    @Override
    public String toString() {
        return "玩家【" + name + "】 curPos = " + curPos + " modelId=" + modelId + " id =" + id;
    }
}
