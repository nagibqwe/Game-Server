/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.player.timer;

import com.game.behavior.structs.BaseBehavior;
import com.game.fight.script.IFightManagerScript;
import com.game.manager.Manager;
import com.game.map.structs.MapObject;
import com.game.pet.structs.Pet;
import com.game.player.script.IPlayerAi;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.skill.structs.SkillLockTrajectory;
import com.game.skill.structs.SkillMagic;
import game.core.timer.TimerEvent;
import java.util.Iterator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 玩家角色的行为准则使用
 *
 * @author soko <xuchangming@haowan123.com>
 */
public class PlayerBehaviorTimer extends TimerEvent {

    private static final Logger log = LogManager.getLogger(PlayerBehaviorTimer.class);
    private final MapObject map;
    private long beginTime = 0;

    public PlayerBehaviorTimer(MapObject mm) {
        super(-1, 0,50);
        this.map = mm;
        beginTime = System.currentTimeMillis();//如果有改时间也不能改变此值，因为需要精确的时间运转毫秒值
    }

    @Override
    public void action() {
        try {
            long now = System.currentTimeMillis();
            long offset = now - beginTime;
            beginTime = now;
            Iterator<Player> iter = map.getPlayers().values().iterator();

            //开始攻击
            IFightManagerScript is = (IFightManagerScript) Manager.scriptManager.GetScriptClass(ScriptEnum.FightManagerBaseScript);

            IPlayerAi iabs =(IPlayerAi) Manager.scriptManager.GetScriptClass(ScriptEnum.PlayerAiCommonScript);

            Player player;
            while (iter.hasNext()) {
                player = iter.next();
                if (null == player) {
                    // map.removePlayer(iter);//？这个点是有问题的
                    log.error("player is null! 异常了", new NullPointerException());
                    continue;
                }

                dealPet(player, offset, is);
                // huhu 召唤物的
                for(long magicid : player.getMagics()) {
                    SkillMagic magic = map.getMagic(magicid);
                    if(magic != null){
                        magic.UpdateBehavior(offset);
                    }
                }

                Iterator<SkillLockTrajectory> stIter = player.getLockTrajectories().iterator();
                while(stIter.hasNext()){
                    SkillLockTrajectory st = stIter.next();
                    if( is.doSkillLockTrajectoryEndToDelete(player, st)){
                        stIter.remove();
                    }
                }

                if (iabs != null ) {
                    iabs.OnPlayerAi(player);
                }
                if (player.BehaviorList().isEmpty()) {
                    continue;
                }
                MapObject temp = Manager.mapManager.getMap(player.gainMapId());
                if (!map.equals(temp)) {
                    //清理玩家的地图退出
                    Manager.mapManager.manager().clearPlayer(map, player);
                    continue;
                }

                Iterator<BaseBehavior> it = player.BehaviorList().iterator();
                while (it.hasNext()) {
                    BaseBehavior bb = it.next();

                    if (bb == null) {
                        log.error("行为数据为空， 则清除！");
                        it.remove();
                        continue;
                    }

                    bb.Update(offset);
                    if (bb.IsOver()) {
                        it.remove();
                    }
                }
            }
        } catch (Exception e) {
            log.error("执行怪物行为的时候， 地出错了！", e);
        }
    }

    private void dealPet(Player player, long offset, IFightManagerScript is) {
        Pet pet = Manager.petManager.getBattlePet(player);
        if (pet == null) {
            return;
        }

        Iterator<SkillLockTrajectory> skillLockIter = pet.getLockTrajectories().iterator();
        while(skillLockIter.hasNext()){
            SkillLockTrajectory st = skillLockIter.next();
            if( is.doSkillLockTrajectoryEndToDelete(pet, st)){
                skillLockIter.remove();
            }
        }

        if (pet.BehaviorList().isEmpty()) {
            return;
        }

        Iterator<BaseBehavior> it = pet.BehaviorList().iterator();
        while (it.hasNext()) {
            BaseBehavior bb = it.next();

            if (bb == null) {
                log.error("行为数据为空， 则清除！");
                it.remove();
                continue;
            }

            bb.Update(offset);
            if (bb.IsOver()) {
                it.remove();
            }
        }
    }
}
