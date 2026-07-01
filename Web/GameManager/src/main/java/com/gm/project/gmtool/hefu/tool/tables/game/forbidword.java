package com.gm.project.gmtool.hefu.tool.tables.game;

import com.gm.project.gmtool.hefu.tool.tables.interfaces.BaseTableHandler;

import java.sql.SQLException;

/**
 * @author gaozhaoguang
 * @desc 禁言数据,清掉合过来的数据
 * @date Created on 2021/1/18 21:59
 **/
public class forbidword extends BaseTableHandler {

    @Override
    public void doBefore(Object args) throws SQLException {
        WriteLog("开始处理 forbidword 表... ... ");
    }

    @Override
    public void doProcess(Object args) throws SQLException {
        task.clearFromData("forbidword");
        WriteLog("forbidword 表处理完毕！");
    }
}