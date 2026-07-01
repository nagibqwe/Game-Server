package com.game.skill.script;

import com.game.player.structs.Player;
import com.game.skill.structs.Skill;
import game.core.script.IScript;
import game.message.SkillMessage;

public interface ISkillScript extends IScript {

    void initSkillList(Player player, SkillMessage.ResSkillOnline messInfo);

    void addSkill(Player player, int id, int level);

    void updateSkill(Player player, SkillMessage.ResUpdateSkill messInfo);

    void onResUpSkillStar(Player player, SkillMessage.ResUpSkillStar messInfo);

    void sendReqOneKeyUpSkill(Player player);
}
