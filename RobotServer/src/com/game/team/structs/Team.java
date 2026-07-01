package com.game.team.structs;

import com.game.player.manager.PlayerManager;
import com.game.player.structs.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 队伍
 * @Auther: gouzhongliang
 * @Date: 2021/8/3 17:27
 */
public class Team {
    /**ID*/
    private long id;
    /**队伍中的玩家*/
    private Map<Long, Player> players = new ConcurrentHashMap<>();

    private Player leader = null;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Map<Long, Player> getPlayers() {
        return players;
    }

    public void setPlayers(Map<Long, Player> players) {
        this.players = players;
    }

    public Player getLeader() {
        return leader;
    }

    public void setLeader(Player leader) {
        this.leader = leader;
    }

    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", players=" + players +
                '}';
    }

    public synchronized void addPlayer(Player player, boolean leader) {
        players.put(player.getId(), player);
        if(leader){
            this.leader = player;
        }

    }

}
