package com.game.vip.structs;

import game.message.VipMessage;

/**
 * @Description
 * @auther lw
 * @create 2020-01-01 14:26
 */
public class VipTarget {

    private int id;

    private int progress;

    private boolean state;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

//    public VipMessage.VipTarget.Builder toMsg() {
//        VipMessage.VipTarget.Builder builder = VipMessage.VipTarget.newBuilder();
//        builder.setId(id);
//        builder.setProg(progress);
//        builder.setState(state);
//        return builder;
//    }

}
