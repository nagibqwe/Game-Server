package com.game.map.structs;

import com.game.monster.structs.Monster;
import com.game.npc.structs.Npc;
import com.game.npc.structs.Tombstone;
import com.game.player.structs.Player;
import com.game.robot.struct.Robot;
import com.game.skill.structs.SkillMagic;
import com.game.structs.GameObject;
import com.game.structs.Gather;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 区域
 */
public class Area extends GameObject {

    protected static Logger log = LogManager.getLogger(Area.class);

    //玩家列表
    private ConcurrentHashMap<Long, Player> players = new ConcurrentHashMap<>();
    //机器人列表
    private ConcurrentHashMap<Long, Robot> robots = new ConcurrentHashMap<>();
    //怪物列表
    private ConcurrentHashMap<Long, Monster> monsters = new ConcurrentHashMap<>();
    //采集物表
    private ConcurrentHashMap<Long, Gather> collects = new ConcurrentHashMap<>();
    //NPC表
    private ConcurrentHashMap<Long, Npc> npcs = new ConcurrentHashMap<>();
    //地图魔法
    private ConcurrentHashMap<Long, SkillMagic> magics = new ConcurrentHashMap<>();
    //墓碑
    private ConcurrentHashMap<Long, Tombstone> tombstone = new ConcurrentHashMap<>();
    //采集物表
    private final ConcurrentHashMap<Long, GroundBuff> groundBuffs = new ConcurrentHashMap<>();

    public ConcurrentHashMap<Long, Robot> getRobots() {
        return robots;
    }

    public void setRobots(ConcurrentHashMap<Long, Robot> robots) {
        this.robots = robots;
    }

    public ConcurrentHashMap<Long, Npc> getNpcs() {
        return npcs;
    }

    public void setNpcs(ConcurrentHashMap<Long, Npc> npcs) {
        this.npcs = npcs;
    }

    public Collection<Player> getPlayers() {
        return players.values();
    }

    public void addPlayer(long id, Player val) {
//        if (players.containsKey(id)) {
//            LOGGER.error("area里面添加角色的时候已经有这个key了 " + val.getName() + " 原来的名字:" + players.get(id) == null ? "null" : players.get(id).getName());
//        }
        players.put(id, val);
    }

    public void removePlayer(long id) {
        players.remove(id);
    }

    public boolean containPlayer(long id) {
        return players.containsKey(id);
    }

    public void setPlayers(ConcurrentHashMap<Long, Player> players) {
        this.players = players;
    }

    public ConcurrentHashMap<Long, Monster> getMonsters() {
        return monsters;
    }

    public void setMonsters(ConcurrentHashMap<Long, Monster> monsters) {
        this.monsters = monsters;
    }

    public Collection<SkillMagic> getMagics() {
        return magics.values();
    }

    public void setMagics(ConcurrentHashMap<Long, SkillMagic> magics) {
        this.magics = magics;
    }

    public void addMagic(SkillMagic m){
        magics.put(m.getId(), m);
    }

    public void removeMagic(long id){
        magics.remove(id);
    }

    public ConcurrentHashMap<Long, GroundBuff> getGroundBuffs() {
        return groundBuffs;
    }

    public ConcurrentHashMap<Long, Gather> getCollects() {
        return collects;
    }

    public void setCollects(ConcurrentHashMap<Long, Gather> collects) {
        this.collects = collects;
    }

    public ConcurrentHashMap<Long, Tombstone> getTombstone() {
        return tombstone;
    }

    public void setTombstone(ConcurrentHashMap<Long, Tombstone> tombstone) {
        this.tombstone = tombstone;
    }

    @Override
    public void release() {

    }
}
