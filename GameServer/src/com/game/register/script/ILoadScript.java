/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.register.script;

import com.game.player.structs.Player;

/**
 * 客户端地图加载完成
 * @author soko <xuchangming@haowan123.com>
 */
public interface ILoadScript {
    public void OnReqLoadFinish(Player player);
    
    public void OnReqMainUIGuideID(Player player);
    
    public void OnReqUpdateMainUIGuideID(Player player,int lastId);
    
    public void reconnect(Player player);
    
    public void EnterGameMap(Player player);

}
