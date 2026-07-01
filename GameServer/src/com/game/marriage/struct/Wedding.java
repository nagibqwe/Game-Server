package com.game.marriage.struct;

/**
 * @Desc TODO
 * @Date 2021/3/27 17:23
 * @Auth ZUncle
 */
public class Wedding {

    int type;           //求婚类型
    long targetId;      //求婚对象Id

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getTargetId() {
        return targetId;
    }

    public void setTargetId(long targetId) {
        this.targetId = targetId;
    }
}
