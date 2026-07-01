/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.fight.state;

import com.game.structs.Entity;

/**
 * @author zenghai
 */
public interface IFightState {

    void add(Entity owner);

    void romove(Entity owner);

}
