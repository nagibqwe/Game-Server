/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.jjc.structs;

/**
 *
 * @author zenghai <zenghai@haowan123.com>
 */
public class JJCTool {

    //战报类型枚举
    public static final int JJClogType0 = 0; //胜 你成功挑战[XXXX]，排名上升至 XXXX
    public static final int JJClogType1 = 1; //胜 你成功击退了[XXXXX]的进攻，排名保持不变
    public static final int JJClogType2 = 2; //败 你挑战[XXXX]失败，排名保持不变
    public static final int JJClogType3 = 3; //败 [XXXX]在竞技场中战胜了你，你的排名下降到 99999

}
