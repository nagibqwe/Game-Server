package com.game.monster.script;

import com.game.player.structs.Player;
import game.core.map.IMapObject;

/**
 * 用来控制因为玩家任务地图上的entity出现和消失，可以应用于所有Entity如monster，npc，等等
 * @author admin
 */
public interface ITaskEntityIsShow {
    
    boolean canSee(Player player, IMapObject entity);
    
}
