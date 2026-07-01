package com.game.yed;

public class YedTransition {
    public YedState target = null;
    public YedInstruction condition = null;

    public void Load(YedData yd, BinaryBuffer buffer) {
        int targetIdx = buffer.ReadInt();
        target = yd.states[targetIdx];
        condition = new YedInstruction();
        condition.Load(yd, buffer);
    }
}
