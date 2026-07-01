/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.player.script;

import com.game.pet.structs.Pet;
import com.game.player.structs.Player;
import com.game.robot.struct.Robot;
import com.game.structs.Fighter;

/**
 *
 * @author soko <xuchangming@haowan123.com>
 */
public interface IPlayerBattle {

    void doDie(Player diePlayer, Fighter attacker);

    void doDie(Pet pet, Fighter attacker);

    void doDie(Robot robot, Fighter attacker);

    void beAttack(Player player, Fighter attacker, long damage);

    void beAttack(Pet pet, Fighter attacker, long damage);

    void removeHatredPlayer(Player player, long ortherRoleId);
}
