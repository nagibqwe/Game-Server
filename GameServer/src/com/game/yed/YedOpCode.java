package com.game.yed;

/**
 * @author gaozhaoguang
 * @desc Yed执行的操作符定义
 * @date Created on 2020/8/15 16:38
 **/
public class YedOpCode {
    public static final int OP_FIRST_OP = 0;
    public static final int OP_ADD = OP_FIRST_OP;
    public static final int OP_SUB = OP_ADD + 1;
    public static final int OP_MUL = OP_SUB + 1;
    public static final int OP_DIV = OP_MUL + 1;
    public static final int OP_MOD = OP_DIV + 1;
    public static final int OP_NOT = OP_MOD + 1;
    public static final int OP_AND = OP_NOT + 1;
    public static final int OP_OR = OP_AND + 1;
    public static final int OP_BITAND = OP_OR + 1;
    public static final int OP_BITOR = OP_BITAND + 1;
    public static final int OP_BITXOR = OP_BITOR + 1;
    public static final int OP_UADD = OP_BITXOR + 1;
    public static final int OP_USUB = OP_UADD + 1;
    public static final int OP_LAST_OP = OP_USUB;

    public static final int CMP_FIRST_CMP = 20;
    public static final int CMP_EQ = CMP_FIRST_CMP;
    public static final int CMP_NEQ = CMP_EQ + 1;
    public static final int CMP_LT = CMP_NEQ + 1;
    public static final int CMP_GT = CMP_LT + 1;
    public static final int CMP_LTE = CMP_GT + 1;
    public static final int CMP_GTE = CMP_LTE + 1;
    public static final int CMP_LAST_CMP = CMP_GTE;

    public static final int FUNC = 30;

    public static final int CONSTANT = 40;

    public static final int STRING = 50;

    public static final int PARAM = 60;
}
