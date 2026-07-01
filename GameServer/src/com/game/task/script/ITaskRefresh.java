/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.task.script;

import com.game.player.structs.Player;
import com.game.task.structs.Task;

/**
 * 任务刷新街口
 * @author admin
 */
public interface ITaskRefresh {
    
    boolean refreshTask(Player player, int state);
    
    /**
     * 任务修复，因为玩家升级导致的一些任务不能完成的情况
     * @param player 
     */
    void repairTask(Player player);
}
