package com.game.yed;

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
