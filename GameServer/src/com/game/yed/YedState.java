package com.game.yed;

/**
 * Yed的状态,
 * 状态与状态之间会有各种链接.
 */
public class YedState {

    public String name = "";
    //连接状态
    public boolean isJunctionState = false;
    //判定状态
    public boolean isDecisionState = false;
    //操作数量
    public int numInstructions = 0;
    //操作的数组
    public YedInstruction[] instructions = null;
    //过渡数量
    public int numTransitions = 0;
    //过渡数组
    public YedTransition[] transitions = null;
    //是否退出
    public boolean isExit = false;

    //解析为状态
    public void Load(YedData yd, BinaryBuffer buffer) {
        int namelen = buffer.ReadInt();
        byte[] namebytes = buffer.Read(namelen);
        name = BinaryBuffer.Bytes2String(namebytes, true);

        isExit = "exit".equals(name.toLowerCase());

        int type = buffer.ReadInt();
        if (type == 1) {
            //这个是圆形节点,本身没有函数需要执行
            isJunctionState = true;
            isDecisionState = false;
        } else if (type == 2) {
            //这个是菱形节点
            isJunctionState = false;
            isDecisionState = true;
        }else{
            //这个是矩形节点
            isJunctionState = false;
            isDecisionState = false;
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

    @Override
    public String toString() {
        return "YedState{" + "name=" + name + ", numInstructions=" + numInstructions + ", numTransitions=" + numTransitions + ", isExit=" + isExit + '}';
    }
}
