/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.behavior.structs;

/**
 * 事件执行完后的调用
 *
 * @author xuchangming <xysoko@qq.com>
 */
public interface IBehaviorEvent {

    void BehaviorOver(BaseBehavior behavior);//事件处理完后的回调
}
