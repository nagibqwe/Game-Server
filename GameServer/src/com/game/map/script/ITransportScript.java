package com.game.map.script;

import com.game.map.structs.MapObject;
import com.game.player.structs.Player;
import game.core.map.Position;
import game.message.MapMessage;


public interface ITransportScript {

    /**
     * 传送点传送
     * @param player
     * @param transportId
     */
    void onReqTransport(Player player, int transportId);

    /**
     * 跳跃
     * @param player
     * @param transportId
     */
    void onJumpFly(Player player, int transportId);

    void OnReqTransportControl(Player player, MapMessage.ReqTransportControl messInfo);

    void moveToOtherWorldMap(Player player, int mapid, Position pos, int transportId);
    

    void ResCurMapTransport(Player player, MapObject map, Position pos, int type, long param);

    /**
     * 能否进入地图，条件不满足会给出提示
     * @param player
     * @param mapID
     * @return
     */
    boolean canEnterMap(Player player, int mapID);
}
