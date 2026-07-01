package com.game.map.manager;

import com.game.map.structs.MapGps;
import game.core.map.Position;

/**
 * map Gps常用操作函数
 *
 * @author soko
 */
public class MapGpsUtil {

    public static void CopyGPS(MapGps from, MapGps to) {
        to.setLine(from.getLine());
        to.setMapId(from.getMapId());
        to.setModelId(from.getModelId());
        to.setPos(new Position(from.getPos().getX(), from.getPos().getY()));
    }

    public static void CopyGPS(MapGps from, int[] pos, MapGps to) {
        to.setLine(from.getLine());
        to.setMapId(from.getMapId());
        to.setModelId(from.getModelId());
        to.setPos(new Position(pos[0], pos[1]));
    }
}
