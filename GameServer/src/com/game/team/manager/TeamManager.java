/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.team.manager;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.team.script.ITeamHandler;
import com.game.team.structs.MatchTeam;
import com.game.team.structs.TeamInfo;
import game.core.script.IScript;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class TeamManager {

    private final ConcurrentHashMap<Long, TeamInfo> teams = new ConcurrentHashMap<>();

    /**
     * 队伍匹配数据：队伍id->匹配数据
     */
    private final ConcurrentHashMap<Long, MatchTeam> matchTeams = new ConcurrentHashMap<>();

    private enum Singleton {

        INSTANCE;
        TeamManager manager;

        Singleton() {
            this.manager = new TeamManager();
        }

        TeamManager getProcessor() {
            return manager;
        }
    }

    public static TeamManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    public ITeamHandler deal() {
        IScript script = Manager.scriptManager.GetScriptClass(ScriptEnum.TeamHandlerBaseScript);
        if (script instanceof ITeamHandler) {
            ITeamHandler teamHandler = (ITeamHandler) script;
            return teamHandler;
        } else {
            return null;
        }
    }


    public TeamInfo getTeam(long id) {
        return teams.get(id);
    }

    public ConcurrentHashMap<Long, TeamInfo> getTeams() {
        return teams;
    }

    public ConcurrentHashMap<Long, MatchTeam> getMatchTeams() {
        return matchTeams;
    }

    /**
     * 玩家上线处理
     * @param player
     */
    public void playerOnLine(Player player) {
        deal().playerOnLine(player);
    }

    /**
     * 玩家离线处理
     * @param player
     */
    public void playerOffLine(Player player) {
        deal().playerOffLine(player);
    }

    /**
     * 退出队伍
     * @param player
     */
    public void OnQuitTeam(Player player) {
        deal().OnQuitTeam(player);
    }

}
