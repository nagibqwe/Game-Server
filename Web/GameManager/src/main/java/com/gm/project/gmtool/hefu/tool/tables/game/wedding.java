package com.gm.project.gmtool.hefu.tool.tables.game;

import com.gm.project.gmtool.hefu.tool.tables.interfaces.BaseTableHandler;

import java.sql.SQLException;

/**
 * @author gaozhaoguang
 * @desc 婚宴数据,逻辑上已经无用,直接合并
 * @date Created on 2021/1/18 22:05
 **/
public class wedding  extends BaseTableHandler {

    @Override
    public void doBefore(Object args) throws SQLException {
        WriteLog("开始处理 wedding 表... ... ");
    }

    @Override
    public void doProcess(Object args) throws SQLException {
        WriteLog("wedding 表处理完毕！");
    }
}