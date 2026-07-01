package com.game.buff.script;

import com.game.structs.Fighter;

/**
 * @Desc TODO
 * @Date 2020/9/21 11:35
 * @Auth ZUncle
 */
public interface IBuffEffect {

    /**
     * 是否定身
     * @param fighter
     * @return
     */
    boolean haveDing(Fighter fighter);

    /**
     * 是否眩晕
     * @param fighter
     * @return
     */
    boolean haveDizziness(Fighter fighter);

    /**
     * 免控BUFF
     * @param fighter
     * @return
     */
    boolean haveMiaokong(Fighter fighter);

    /**
     * 免控BUFF
     * @param fighter
     * @return
     */
    boolean haveJinLiao(Fighter fighter);

}
