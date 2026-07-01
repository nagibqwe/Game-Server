package com.game.skill.manager;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.skill.script.ISkillScript;
import com.game.skill.structs.SkillMagic;
import com.game.structs.Fighter;
import com.game.utils.Utils;
import game.core.map.Position;
import game.core.script.IScript;

import game.message.FightMessage;

public class SkillManager {
    private enum Singleton {
        INSTANCE;
        SkillManager manager;

        Singleton() {
            this.manager = new SkillManager();
        }

        SkillManager getProcessor() {
            return manager;
        }
    }

    public static SkillManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    public void addSkill(Player player, int skillId) {
        Manager.skillManager.deal().addSkill(player, skillId);
    }

    public void removeSkill(Player player, int skillId) {
        deal().removeSkill(player, skillId);
    }

    public ISkillScript deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.SkillBaseScript);
        if (is instanceof ISkillScript) {
            return (ISkillScript) is;
        } else {
            return null;
        }
    }

    public FightMessage.SkillBaseInfo.Builder buildSkillBaseInfo(Fighter attacker, int skillId, int serial, Fighter beAttacker){
        FightMessage.SkillBaseInfo.Builder info = FightMessage.SkillBaseInfo.newBuilder();
        info.setSerial(serial);
        info.setSkillID(skillId);
        info.setUserID(attacker.getId());
        if( beAttacker != null){
            Position disPos = Utils.getDir(attacker.gainCurPos(), beAttacker.gainCurPos());
            info.setDirX(disPos.getX());
            info.setDirY(disPos.getY());
        }
        else
        {
            info.setDirX(attacker.getDir().getX());
            info.setDirY(attacker.getDir().getY());
        }
        return info;
    }

    //创建技能召唤物
    public SkillMagic createMagic(String key){
        SkillMagic ret = new SkillMagic();
        ret.setMagicIndex(key);
        ret.onCreate();
        return ret;
    }
}
