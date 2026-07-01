package com.game.map.structs;

public class MapPet extends BaseNpc {

    @Override
    protected void onForceStopMove() {

    }

    @Override
    public String toString() {
        return "宠物【" + name + "】 curPos = " + curPos + " modelId=" + modelId + " id =" + id;
    }
}
