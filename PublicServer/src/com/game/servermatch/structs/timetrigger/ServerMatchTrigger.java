/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.servermatch.structs.timetrigger;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.game.manager.Manager;
import com.game.servermatch.manager.ServerMatchManager;
import com.game.servermatch.structs.Trigger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 服务器匹配
 * @author zhaibiao
 */
public class ServerMatchTrigger implements Trigger{

    @JsonIgnore
    private static final Logger logger = LogManager.getLogger("ServerMatchTrigger");
    @Override
    public boolean active() {
        try{
            //从新划分
            ServerMatchManager.deal().markOff(null);//划分服务器
            Manager.fudManager.deal().allocCity(true);
           // ServerMatchManager.deal().getLeagueGuild();//获取16只公会
            return true;
        }catch(Exception e){
            logger.error(e,e);
            return false;
        }
        
    }
}
