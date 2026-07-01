package com.game.yed;

import java.util.ArrayList;

public class YedInstruction {

    public int instructionLength = 0;
    //public BinaryBuffer data = null;
    public String txtInstruction = "";
    public ArrayList<Object> cmds = new ArrayList<Object>();

    public void Load(YedData yd, BinaryBuffer buffer) {
        instructionLength = buffer.ReadInt();
        byte[] buf = buffer.Read(instructionLength);
        BinaryBuffer data = BinaryBuffer.Allocate(buf, Endian.Little);
        GenerateCommands(data);
        int txtlen = buffer.ReadInt();
        byte[] txtbytes = buffer.Read(txtlen);
        txtInstruction = BinaryBuffer.Bytes2String(txtbytes, true);
        if (instructionLength == 0) {
//			#if UNITY_EDITOR
//			System.out.println("YedInstruction error:" + txtInstruction);
//			#endif
        }
    }

    public void GenerateCommands(BinaryBuffer ptr) {
        ptr.ResetReaderIndex();
        while (ptr.ReadableBytes() > 0) {
            int instr = ptr.NReadInt();
            cmds.add(instr);
            if (instr >= YedAI.EInstr.OP_FIRST_OP && instr <= YedAI.EInstr.OP_LAST_OP) {
                //HandleOp(instr);
            } else if (instr >= YedAI.EInstr.CMP_FIRST_CMP && instr <= YedAI.EInstr.CMP_LAST_CMP) {
                //HandleCmp(instr);
            } else if (instr == YedAI.EInstr.FUNC) {
                int idx = ptr.NReadInt();
                int argc = ptr.NReadInt();
                cmds.add(argc);
                //int funcNameLen = ptr.ReadInt();
                //byte[] funcNameBytes = ptr.Read(funcNameLen);
                //String funcName = BinaryBuffer.Bytes2String(funcNameBytes);
                String funcName = ptr.NReadString();
                cmds.add(funcName);
                //HandleFunc(funcName, argc);

            } else if (instr == YedAI.EInstr.CONSTANT) {
                float number = ptr.NReadFloat();
                cmds.add(number);
                //PushNumber(number);
            } else if (instr == YedAI.EInstr.PARAM) {
                //int strLen = ptr.ReadInt();
                //byte[] strBytes = ptr.Read(strLen);
                //String str = BinaryBuffer.Bytes2String(strBytes);
                String str = ptr.NReadString();
                cmds.add(str);
                //PushParam(str);
            } else if (instr == YedAI.EInstr.STRING) {
                //int strLen = ptr.ReadInt();
                //byte[] strBytes = ptr.Read(strLen);
                //String str = BinaryBuffer.Bytes2String(strBytes);
                String str = ptr.NReadString();
                cmds.add(str);
                //PushString(str);
            }
        }
    }
}
