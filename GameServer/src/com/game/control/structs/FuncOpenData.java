package com.game.control.structs;

public class FuncOpenData {

    /**
     * 功能Id
     */
    private int id;

    /**
     * 功能开放状态：0关闭，1开放
     */
    private int openState;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOpenState() {
        return openState;
    }

    public void setOpenState(int openState) {
        this.openState = openState;
    }

}
