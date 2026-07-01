package com.game.copymap.structs;

/**
 * 位面地图内数据
 */
public class PlaneMapData {
    /**
     * 第几波怪物
     */
    private int loop = 1;

    /**
     * 所有怪物刷新完毕
     */
    private boolean end;

    /**
     * 获取 第几波怪物
     *
     * @return loop 第几波怪物
     */
    public int getLoop() {
        return this.loop;
    }

    /**
     * 设置 第几波怪物
     *
     * @param loop 第几波怪物
     */
    public void setLoop(int loop) {
        this.loop = loop;
    }

    /**
     * 获取 所有怪物刷新完毕
     *
     * @return end 所有怪物刷新完毕
     */
    public boolean isEnd() {
        return this.end;
    }

    /**
     * 设置 所有怪物刷新完毕
     *
     * @param end 所有怪物刷新完毕
     */
    public void setEnd(boolean end) {
        this.end = end;
    }
}
