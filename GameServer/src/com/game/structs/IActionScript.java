/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.structs;

import com.game.map.structs.MapObject;

/**
 *
 * @author zenghai
 */
public interface IActionScript {
    
    public boolean action(MapObject map, long heart);
    
}
