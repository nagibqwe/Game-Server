package com.gm.project.gmtool.hefu.tool.tables.game;

import com.gm.project.gmtool.hefu.tool.tables.interfaces.BaseTableHandler;

import java.sql.SQLException;

/**
 * @author gaozhaoguang
 * @desc 元宝改变的日志记录,直接合并
 * @date Created on 2021/1/18 22:00
 **/
public class goldchange extends BaseTableHandler {

    @Override
    public void doBefore(Object args) throws SQLException {
        WriteLog("开始处理 goldchange 表... ... ");
    }

    @Override
    public void doProcess(Object args) throws SQLException {
        WriteLog("goldchange 表处理完毕！");
    }

}