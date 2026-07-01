package com.game.yed;

public class YedState {
    public String name = "";
    public boolean isJunctionState = false;
    public boolean isDecisionState = false;
    public int numInstructions = 0;
    public YedInstruction[] instructions = null;
    public int numTransitions = 0;
    public YedTransition[] transitions = null;
    public boolean isExit = false;

    public void Load(YedData yd, BinaryBuffer buffer) {
        int namelen = buffer.ReadInt();
        byte[] namebytes = buffer.Read(namelen);
        name = BinaryBuffer.Bytes2String(namebytes, true);

        isExit = "exit".equals(name.toLowerCase());

        int type = buffer.ReadInt();
        if (type == 1) {
            isJunctionState = true;
            isDecisionState = false;
        } else if (type == 2) {
            isJunctionState = false;
            isDecisionState = true;
        }

        numInstructions = buffer.ReadInt();
        instructions = new YedInstruction[numInstructions];
        for (int i = 0; i < numInstructions; ++i) {
            instructions[i] = new YedInstruction();
            instructions[i].Load(yd, buffer);
        }

        numTransitions = buffer.ReadInt();
        transitions = new YedTransition[numTransitions];
        for (int i = 0; i < numTransitions; ++i) {
            transitions[i] = new YedTransition();
            transitions[i].Load(yd, buffer);
        }

    }
}
