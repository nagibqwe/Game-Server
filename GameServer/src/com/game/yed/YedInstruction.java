package com.game.yed;

import java.util.ArrayList;

/**
 * Yed的执行指令
 */
public class YedInstruction {

    public int instructionLength = 0;
    public String txtInstruction = "";
    public ArrayList<Object> cmds = new ArrayList<Object>();
    public ArrayList<YedCommand> cmdOrders = new ArrayList<YedCommand>();

    /**
     * 解析指令
     * @param yd
     * @param buffer
     */
    public void Load(YedData yd, BinaryBuffer buffer) {
        instructionLength = buffer.ReadInt();
        byte[] buf = buffer.Read(instructionLength);
        BinaryBuffer data = BinaryBuffer.Allocate(buf, Endian.Little);
        GenerateCommands(data);
        int txtlen = buffer.ReadInt();
        byte[] txtbytes = buffer.Read(txtlen);
        txtInstruction = BinaryBuffer.Bytes2String(txtbytes, true);
    }

    /**
     * 生成命令序列
     * @param ptr
     */
    public void GenerateCommands(BinaryBuffer ptr) {
        ptr.ResetReaderIndex();
        cmdOrders.clear();
        while (ptr.ReadableBytes() > 0) {
            YedCommand cmd = new YedCommand();
            cmd.Load(ptr);
            cmdOrders.add(cmd);
        }
    }
}
