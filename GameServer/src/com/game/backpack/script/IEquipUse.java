/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.backpack.script;

import com.game.backpack.structs.Equip;
import com.game.player.structs.Player;

/**
 * @author soko <xuchangming@haowan123.com>
 */
public interface IEquipUse {

    void useEquip(Player player, Equip equip);

    void unUserEquip(Player player, Equip equip);

}
