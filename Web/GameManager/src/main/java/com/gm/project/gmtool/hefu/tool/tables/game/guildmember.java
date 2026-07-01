package com.gm.project.gmtool.hefu.tool.tables.game;

import com.gm.project.gmtool.hefu.tool.tables.interfaces.BaseTableHandler;

import java.sql.SQLException;

/**
 * @author gaozhaoguang
 * @desc 帮会成员直接合并
 * @date Created on 2021/1/18 22:01
 **/
public class guildmember extends BaseTableHandler {

    @Override
    public void doBefore(Object args) throws SQLException {
        WriteLog("开始处理 guildmember 表... ... ");
    }

    @Override
    public void doProcess(Object args) throws SQLException {
        WriteLog("guildmember 表处理完毕！");
    }
}