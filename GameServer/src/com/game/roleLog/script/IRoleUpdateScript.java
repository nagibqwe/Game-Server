/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.roleLog.script;

import com.game.player.structs.Player;
import java.sql.Connection;
import java.util.List;

/**
 * 处理更新rolestate日志的接口
 *
 * @author soko <xuchangming@haowan123.com>
 */
public interface IRoleUpdateScript {

    /**
     * 保存玩家数据
     *
     * @param Threadname 线程名字
     * @param connection 数据库连接
     * @param updates 更新的玩家数据
     * @param fieldNum 字段的个数
     */
    public void OndealSave(String Threadname, Connection connection, List<Player> updates, int fieldNum);

    /**
     * 创建角色的时候就开始保存玩家的角色数据了
     *
     * @param Threadname
     * @param connection
     * @param pp
     * @param fieldNum
     */
    public void OndealCreateSave(String Threadname, Connection connection, Player pp, int fieldNum);
}
