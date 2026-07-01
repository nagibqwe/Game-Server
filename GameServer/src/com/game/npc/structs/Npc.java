package com.game.npc.structs;

import com.data.CfgManager;
import com.data.bean.Cfg_Npc_Bean;
import com.data.bean.Cfg_Skill_Bean;
import com.data.struct.ReadArray;
import com.game.cooldown.structs.Cooldown;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.skill.structs.Skill;
import com.game.structs.Entity;
import com.game.structs.Fighter;
import game.core.map.IMapObject;
import game.core.map.Position;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Npc extends Entity{

    //npc方向
    private int npcdir = 0;

    //冷却列表
    protected ConcurrentHashMap<String, Cooldown> cooldowns = new ConcurrentHashMap<>();

    @Override
    public int getType() {
        return Fighter.NPC_TYPE;
    }

    @Override
    public String getSrcName() {
        return null;
    }

    public int getNpcDir() {
        return npcdir;
    }

    public void setNpcDir(int dir) {
        this.npcdir = dir;
    }

    @Override
    public boolean canSee(IMapObject player) {
        Player pp = (Player) player;
        return Manager.npcManager.manager().canSee(pp, this);
    }

    /**
     * 获取任务隐藏id集合
     *
     * @return
     */
    @Override
    public HashMap<Integer, List<Integer>> gainHideTaskIds() {
        final Cfg_Npc_Bean cfg_npcBean = CfgManager.getCfg_Npc_Container().getValueByKey(this.getModelId());

        if (cfg_npcBean.getTaskShow().size() == 0){
            return null;
        }
        HashMap<Integer, List<Integer>> reulst = new HashMap<>(16);
        for (int i = 0; i < cfg_npcBean.getTaskShow().size(); i++) {
            ReadArray<Integer> next = cfg_npcBean.getTaskShow().get(i);
            if (reulst.containsKey(next.get(0))) {
                reulst.get(next.get(0)).add(next.get(1));
            } else {
                List<Integer> temp = new ArrayList<>();
                temp.add(next.get(1));
                reulst.put(next.get(0), temp);
            }
        }
        return reulst;
    }

    @Override
    public void changeCurPos(Position pos) {
        curGps.setPos(pos);
    }

    @Override
    public void changeCurPos(Position pos, boolean isBroadcast) {
        curGps.setPos(pos);
    }

    @Override
    public void doDie(Fighter attacker) {

    }

    @Override
    public void beAttack(Fighter attacker, Cfg_Skill_Bean skill, int skilllevel, long damage) {

    }

    @Override
    public void onHpChange(Fighter attacker) {

    }

    @Override
    public void reset() {

    }

    @Override
    public int gainFinalMoveSpeed() {
        return getAttribute().gainFinalMoveSpeed();
    }

    @Override
    public long getBrithProtect() {
        return 0;
    }

    @Override
    public long getFixDecHp() {
        return 0;
    }

    @Override
    public ConcurrentHashMap<String, Cooldown> getCooldowns() {
        return cooldowns;
    }

    @Override
    public ConcurrentHashMap<Integer, Skill> getSkills() {
        return null;
    }

    @Override
    public long getFightPoint() {
        return Manager.playerAttAttributeManager.deal().calcFightPower(attribute);
    }

    @Override
    public void release() {

    }
}
