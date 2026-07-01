package com.game.entity.script;

import com.game.structs.Entity;
import com.game.structs.Fighter;
import game.core.map.Position;

/**
 * Created by huhu on 2017/9/26.
 */
public interface IEntityScript {

    void runToTarget(Entity ths, Fighter target, float attackDis);

    void runToPosition(Entity ths, Position targetpos);

}
