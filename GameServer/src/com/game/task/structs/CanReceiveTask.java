
package com.game.task.structs;

import com.game.player.structs.Player;

import java.util.List;

/**
 *
 * @author admin
 */
public class CanReceiveTask extends Task{

    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
    
    @Override
    public boolean finishTask(Player player, boolean isGM) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void changeTask(Player player) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public byte acqType() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Integer> targetModels() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int targetNum() {
        return 0;
    }

    @Override
    public void release() {

    }
}
