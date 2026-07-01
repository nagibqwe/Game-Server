package com.game.yed;

/**
 * Yed的状态块之间的链接,链接上有条件,用于检测是否可以进入目标状态
 */
public class YedTransition {
    public YedState target = null;
    public YedInstruction condition = null;

    //解析为链接
    public void Load(YedData yd, BinaryBuffer buffer) {
        int targetIdx = buffer.ReadInt();
        target = yd.states[targetIdx];
        condition = new YedInstruction();
        condition.Load(yd, buffer);
    }
}
