/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.behavior.structs;

/**
 * @author zenghai
 */
public interface IBehavior {

    boolean Cancel(BaseBehavior behavior);

    void action(BaseBehavior behavior);
}
