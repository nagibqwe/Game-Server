package com.game.monster.script;

import com.data.bean.Cfg_Monster_Bean;
import com.game.map.structs.MapObject;
import com.game.monster.structs.Monster;
import com.game.structs.Entity;
import com.game.structs.Hatred;

public interface IMonsterAi {
    
    void setConfigYedAi(Monster monster, Cfg_Monster_Bean config);

    void setYedAi(Monster monster, String yed);

    void doYedAi(Monster monster);
    /**
     * 检查周围是否有可攻击玩家
     * @param monster
     * @param config
     * @return
     */
    boolean doAngerAi(Monster monster, Cfg_Monster_Bean config);
    
    void reset(Monster monster, Cfg_Monster_Bean config);
    
    void sendEntitySpeed(Entity entity);
    
    void sendMonsterRun(Entity entity);
    
    Hatred getCurMainTarget(Monster monster);
    
    Hatred changeMainTarget(Monster monster);
    
    boolean tryUseSkill(Monster monster, Cfg_Monster_Bean config, Hatred curMainTarget);

    /**
     * 地图怪物行为执行
     * @param map
     * @param offset
     */
    void behavior(MapObject map, long offset);
    
}
