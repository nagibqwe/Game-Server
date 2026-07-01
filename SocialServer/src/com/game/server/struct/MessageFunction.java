package com.game.server.struct;

/**
 * @Desc TODO
 * @Date 2021/8/2 17:00
 * @Auth ZUncle
 */
public enum MessageFunction {

    Home(142),  //家园协议

    ;
    final int functionId;

    MessageFunction(int functionId) {
        this.functionId = functionId;
    }

    public int getFunctionId() {
        return functionId;
    }
}
