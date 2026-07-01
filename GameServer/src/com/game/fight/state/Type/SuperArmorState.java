/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.fight.state.Type;

import com.game.fight.state.FightState;
import com.game.fight.structs.FightEnum;
import com.game.structs.Entity;

/**
 * @author zenghai
 */
public class SuperArmorState extends FightState {

    @Override
    public void add(Entity owner) {

        owner.addFightState(FightEnum.SuperArmorCount);

    }

    @Override
    public void romove(Entity owner) {

        owner.removeFightState(FightEnum.SuperArmorCount);

    }

}
