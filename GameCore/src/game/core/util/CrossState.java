/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.core.util;

/**
 *跨服的状态值
 * @author xuchangming <xysoko@qq.com>
 */
public class CrossState {
    public static final int PCS_LOCAL = 0;//本地逻辑中
    public static final int PCS_REQUIRE = 1;//正在请求进入跨服的状态
    public static final int PCS_PIPEI = 2;//正在匹配的状态
    public static final int PCS_FIGHT = 3;//正在战场的状态
    
}
