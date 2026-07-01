package com.game.functionTask.struct;

/**
 * @Auther: gouzhongliang
 * @Date: 2021/9/28 15:08
 */
public class FunctionTask {
    //ID
    private int id;
    //进度
    private int num;
    //是否领取
    private boolean get;
    //是否完成
    private boolean complete;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public boolean isGet() {
        return get;
    }

    public void setGet(boolean get) {
        this.get = get;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }
}
