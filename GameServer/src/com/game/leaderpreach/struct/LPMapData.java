package com.game.leaderpreach.struct;

import com.game.map.structs.MapObject;

import java.util.List;

/**
 * @explain: desc
 * @time Created on 2019/11/8 16:21.
 * @author: tc
 */
public class LPMapData {
	private MapObject mapObject;
	private List<LPPlayerData> playerDataList; // 进入的玩家列表

	public LPMapData(MapObject mapObject, List<LPPlayerData> playerDataList) {
		this.mapObject = mapObject;
		this.playerDataList = playerDataList;
	}

	public MapObject getMapObject() {
		return mapObject;
	}

	public void setMapObject(MapObject mapObject) {
		this.mapObject = mapObject;
	}

	public List<LPPlayerData> getPlayerDataList() {
		return playerDataList;
	}

	public void setPlayerDataList(List<LPPlayerData> playerDataList) {
		this.playerDataList = playerDataList;
	}
}
