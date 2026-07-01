package com.game.skill.script;

import com.game.structs.Entity;

/**
 * Created by huhu on 2017/9/26.
 */
public interface ISkillMagicAiScript {
    /**
     * ai循环调用
     * @param owner
     */
    void AiUpdate(Entity owner);
}
