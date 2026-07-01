package com.game.yed;

/**
 * @author gaozhaoguang
 * @desc Yed中的一个操作处理
 * @date Created on 2020/8/15 17:51
 **/
public class YedCommand {
    /**
     * 操作码
     */
    public int opCode;
    /**
     * 命令参数
     */
    public Object[] Params;

    public void Load(BinaryBuffer ptr){
        opCode = ptr.NReadInt();
        switch (opCode){
            case YedOpCode.FUNC:{
                Params = new Object[2];
                int idx = ptr.NReadInt();
                Params[0] = ptr.NReadInt();
                Params[1] = ptr.NReadString();
                break;
            }
            case YedOpCode.CONSTANT:{
                Params = new Object[1];
                Params[0] =  ptr.NReadFloat();
                break;
            }
            case YedOpCode.STRING:
            case YedOpCode.PARAM:{
                Params = new Object[1];
                Params[0] =  ptr.NReadString();
                break;
            }
            default:
                Params = new Object[0];
                break;
        }
    }
}
