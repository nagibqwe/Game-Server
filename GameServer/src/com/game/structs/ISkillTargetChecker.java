package com.game.structs;

/**
 * Created by huhu on 2017/9/22.
 */
public interface ISkillTargetChecker {
    int Invoke(Fighter attacker, Fighter defer, STCheckerMaker state);

}
