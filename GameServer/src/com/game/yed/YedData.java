package com.game.yed;

/**
 * 对应一个Yed文件,相当于一个AI脚本
 * 一个脚本对应多个状态,状态之间又各种条件链接.
 */
public class YedData {
    public String name = "";
    public int numStates = 0;
    public YedState[] states = null;

    public void Load(BinaryBuffer buffer) {
        int version = buffer.ReadInt();
        int checkEndian = buffer.ReadInt();
        int namelen = buffer.ReadInt();
        byte[] namebytes = buffer.Read(namelen);
        name = BinaryBuffer.Bytes2String(namebytes, true);


        numStates = buffer.ReadInt();
        states = new YedState[numStates];
        for (int i = 0; i < numStates; ++i) {
            states[i] = new YedState();
        }
        for (int i = 0; i < numStates; ++i) {
            states[i].Load(this, buffer);
        }
    }

    /**
     * 返回Entry状态
     * @return
     */
    public YedState Entry() {
        String entryName = "entry";
        for (int i = 0; i < numStates; ++i) {
            if (states[i].name.toLowerCase().equals(entryName.toLowerCase())) {
                return states[i];
            }
        }
        return null;
    }
}
