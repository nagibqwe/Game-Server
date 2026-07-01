/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.behavior.structs;

/**
 * 行为状态值
 *
 * @author soko <xuchangming@haowan123.com>
 */
public class BehaviorStatus {

    public final static int ACTIVITYSTATUS_INITIALIZED = 1;                // 初始化
    public final static int ACTIVITYSTATUS_OVERED = 2;                        // 结束
    public final static int ACTIVITYSTATUS_CANCEL = 3;                        // 取消
    public final static int ACTIVITYSTATUS_RUN = 4;                            // 执行
    public final static int ACTIVITYSTATUS_UNKNOW = 5;                        // 未知
}
